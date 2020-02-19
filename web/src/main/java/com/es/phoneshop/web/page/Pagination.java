package com.es.phoneshop.web.page;

public class Pagination {
    private int currentPageNumber;
    private int leftPaginationBorder;
    private int rightPaginationBorder;

    public Pagination(int currentPageNumber, int leftPaginationBorder, int rightPaginationBorder) {
        this.currentPageNumber = currentPageNumber;
        this.leftPaginationBorder = leftPaginationBorder;
        this.rightPaginationBorder = rightPaginationBorder;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getLeftPaginationBorder() {
        return leftPaginationBorder;
    }

    public void setLeftPaginationBorder(int leftPaginationBorder) {
        this.leftPaginationBorder = leftPaginationBorder;
    }

    public int getRightPaginationBorder() {
        return rightPaginationBorder;
    }

    public void setRightPaginationBorder(int rightPaginationBorder) {
        this.rightPaginationBorder = rightPaginationBorder;
    }
}
