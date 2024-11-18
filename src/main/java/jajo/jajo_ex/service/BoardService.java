package jajo.jajo_ex.service;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.dto.PageDto;
import jajo.jajo_ex.repository.BoardRepository;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;

public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Long save(Board board) {
        boardRepository.save(board);
        return board.getNo();
    }

    public List<Board> findBoards() {
        return boardRepository.findAll();
    }

    public Board findNo(Long no) {
        return boardRepository.findByNo(no);
    }
    @Transactional
    public void deleteBoard(Long no) {
        boardRepository.delete(no);
    }

    public List<Board> selectBoardList(PageDto pageDto) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.findBoardAll(pageDto);
    }
    public Board isPresentBoard(Long no) {
        return boardRepository.isPresentBoard(no);
    }

    public List<Board> searchByHint(PageDto pageDto, String hint) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByHint(pageDto, hint);
    }

    public List<Board> searchByUser(PageDto pageDto, String user) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByUser(pageDto, user);
    }

    public List<Board> searchByTitle(PageDto pageDto, String title) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByTitle(pageDto, title);
    }

    public List<Board> searchByRecommend(PageDto pageDto) {
        int count = boardRepository.countAll(pageDto);

        if(count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByRecommend(pageDto);
    }

//    private String createFilePath(MultipartFile file) throws IOException {
//
//        Path uploadPath = Paths.get(filePath); // 기본경로
//
//        // ** 만약 경로가 없다면, 경로 생성
//        if(!Files.exists(uploadPath))
//            Files.createDirectories(uploadPath);
//
//        // ** 파일명 추출
//        String originalFileName = file.getOriginalFilename();
//        System.out.println(originalFileName); //실행해서 확장자 붙어잇는지 아닌지 여부확인
//
//
//        // ** 확장자 추출
//        String formatType = originalFileName.substring(
//                originalFileName.lastIndexOf("."));
//        System.out.println(formatType);
//
//
//        // ** 파일 이름만 남김
//        originalFileName = originalFileName.substring(
//                0, originalFileName.lastIndexOf(".")
//        );
//
//        System.out.println(originalFileName);
//
//        // ** UUID생성
//        String uuid = UUID.randomUUID().toString();
//
//
//        // ** 저장할 파일의 경로 설정
//        Path path = uploadPath.resolve(
//                uuid + originalFileName + formatType
//        );
//
//        // ** 저장
//        Files.copy(
//                file.getInputStream(), // 업로드된 파일의 입력 스트림
//                path, // 저장할 경로
//                StandardCopyOption.REPLACE_EXISTING // 이미 파일이 존재하면 덮어쓰기
//        );
//        return "";
//    }

//    @Transactional
//    public void save(BoardRequestDto requestDto, MultipartFile[] files) throws IOException{
////        BoardRepository.save1(requestDto.toEntity());
//
//        // ** 파일 정보 저장
//        for(MultipartFile file : files){
//            createFilePath(file);
//            FileDto fileDTO = new FileDto();
//            fileDTO.setFileName(file.getOriginalFilename());
//
//            fileRepository.save(fileDTO.toEntity());
//        }
//
//    }
}
