package jajo.jajo_ex.controller;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.BoardFile;
import jajo.jajo_ex.domain.BoardV2;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.BoardRequestDto;
import jajo.jajo_ex.dto.PageDto;
import jajo.jajo_ex.dto.QuillDataDTO;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.service.BoardFileService;
import jajo.jajo_ex.service.BoardService;
import jajo.jajo_ex.service.BoardServiceV2;
import jajo.jajo_ex.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class BoardControllerV2 {
    private final BoardServiceV2 boardService;
    private final MemberService memberService;
    private final BoardFileService boardFileService;

    public BoardControllerV2(BoardServiceV2 boardService, MemberService memberService, BoardFileService boardFileService) {
        this.boardService = boardService;
        this.memberService = memberService;
        this.boardFileService = boardFileService;
    }

//    private static final Pattern Base64_Image_PATTERN =
//            Pattern.compile("^data:image/(png|jpeg|jpg);base64,(.+)$");
//
//    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> base64ToUrl(Map<String, Object> delta, String[] url) throws IOException {
        List<Map<String, Object>> ops = (List<Map<String, Object>>) delta.get("ops");
        List<Map<String, Object>> updateOps = new ArrayList<>();
        int num = 0;

        for(Map<String, Object> op: ops) {
            Object insert = op.get("insert");

            if(insert instanceof Map<?, ?> insertMap && insertMap.containsKey("image")) {
                String imageData = (String) insertMap.get("image");

                Map<String, Object> newInsert = Map.of("image", url[num++]);
                op.put("insert", newInsert);
            }
            updateOps.add(op);
        }
        return Map.of("ops", updateOps);
    }

    public String getTime() {
        ZonedDateTime zdateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return zdateTime.format(formatter);
    }

    public String getDir(String boardType) {
        String fileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
        fileDir += boardType + "\\";
        return fileDir;
    }

    public String transferImageName(String name) {
        String ext = name.substring(name.lastIndexOf(".") + 1);
        name = name.substring(0, name.lastIndexOf("."));
        return getTime() + "_" + name + "." + ext;
    }

    public File saveImage(String boardType, String fileName) {
        return new File(getDir(boardType), fileName);
    }


    @PostMapping("/newBoardV2")
    public ResponseEntity<?> saveBoard(@SessionAttribute(required=false, name="principal") Member principal, Model model,
                                       @RequestPart(value="info") QuillDataDTO quillData,
                                       @RequestPart(value="thumbnail", required =false) MultipartFile thumbnail,
                                       @RequestPart(value="file", required =false) MultipartFile[] file) throws IOException {
        if (principal != null) model.addAttribute("member", principal);
        Member member = memberService.findById(principal);

        String allFile = "";
        String thumbnail_img = "";
        String title = quillData.getTitle();

        Map<String, Object> delta = quillData.getDelta();
        System.out.println("델타: " + delta);
        String boardType = "";
        boardType += quillData.getBoardType();

        if(!thumbnail.isEmpty()) {
            String thumbnailName = thumbnail.getOriginalFilename();
            thumbnail_img = transferImageName(thumbnailName);
            thumbnail.transferTo(saveImage(boardType, thumbnail_img));
        }
        String[] imageUrls = new String[file.length];
        int num = 0;

        for(MultipartFile f: file) {
            if (!f.isEmpty()) {
                String fileName = f.getOriginalFilename();
                String img = transferImageName(fileName);
//                String imageUrl = saveDir(boardType) + img;
                String imageUrl = "/uploadImage/" + boardType + "/" + img;
                imageUrls[num++] = imageUrl;
                allFile += img + ", ";
                f.transferTo(saveImage(boardType, img));
            }
        }

        delta = base64ToUrl(delta, imageUrls);

        BoardV2 board = BoardV2.builder()
                .member(member)
                .boardType(quillData.getBoardType())
                .title(quillData.getTitle())
                .delta(quillData.getDelta())
                .build();

        boardService.save(board);

        BoardFile boardFile = BoardFile.builder()
                .boardV2(board)
                .thumbnail(thumbnail_img)
                .fileName(allFile)
                .build();

        boardFileService.save(boardFile);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("boardType", boardType);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/recommend")
    @ResponseBody
    public ResponseDto<?> increase(@RequestBody QuillDataDTO quillData) {
        boardService.increaseRecommend(quillData.getNo());
        return ResponseDto.success(1);
    }

    @DeleteMapping("/deleteForm")
    public ResponseDto<Integer> deleteOne(@RequestParam("no") Long no, @RequestParam("boardType") String boardType){

        List<BoardFile> boardFile = boardFileService.findByBoardV2(no);

        String removeFile = boardFile.get(0).getFileName();
        String removeThumbnailName = boardFile.get(0).getThumbnail();

        if(!Objects.equals(removeThumbnailName, "")) {
            String DeleteThumbnailDir = getDir(boardType) + removeThumbnailName;
            File DeleteThumbnail = new File(DeleteThumbnailDir);
            if(DeleteThumbnail.delete()) {
                System.out.println(removeThumbnailName + ": 삭제 완료");
            }
        }

        for (String removeFileName : removeFile.split(",")) {
            removeFileName = removeFileName.trim();
            if (removeFileName != "") {
                String DeleteFileDir = getDir(boardType) + removeFileName;
                File DeleteFile = new File(DeleteFileDir);
                if (DeleteFile.delete()) {
                    System.out.println(removeFileName + ": 삭제 완료");
                }
            }
        }
        boardService.deleteBoard(no);
        return ResponseDto.success(1);
    }

    @PostMapping("/updateBoardV2")
    @ResponseBody
    public Map<String, Object> updateBoard(@SessionAttribute(required=false, name="principal") Member principal, Model model,
                                      @RequestBody QuillDataDTO quillDataDto) {
        if (principal != null) model.addAttribute("member", principal);
        Member member = memberService.findById(principal);
        Long no = quillDataDto.getNo();
        model.addAttribute("no", no);

        Map<String, Object> response = new HashMap<>();
        response.put("no", no);

        return response;
    }
}
