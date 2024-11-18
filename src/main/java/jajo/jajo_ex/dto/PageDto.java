package jajo.jajo_ex.dto;

public class PageDto {
    private int page;   //현재 페이지
    private int startPage;  //시작 페이지
    private int endPage;    //끝 페이지
    private int totalCount; //게시글 총 갯수
    private int rowPerPage; //페이지 당 게시글 행 수
    private int lastPage; // 마지막 페이지
    private int start; //SQL 쿼리에 사용할 변수
    private int end; // SQL 쿼리에 사용할 변수

    public PageDto() {
        this.page = 1;
        this.rowPerPage = 10;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        pageingData();
    }

    public void pageingData() {
        this.lastPage = (int) Math.ceil((double) totalCount / (double) this.rowPerPage);
        this.endPage = (int) Math.ceil((double) page / (double) this.rowPerPage) * this.rowPerPage;
        if(this.lastPage < this.endPage) {
            this.endPage = this.lastPage;
        }

        this.startPage = this.endPage - this.rowPerPage + 1;

        if(this.startPage < 1) {
            this.startPage = 1;
        }

        this.start = (this.page- 1) * this.rowPerPage;
        this.end = this.rowPerPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getRowPerPage() {
        return rowPerPage;
    }

    public void setRowPerPage(int rowPerPage) {
        this.rowPerPage = rowPerPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
