package com.es.phoneshop.web.service.impl;

import com.es.phoneshop.web.page.Pagination;
import com.es.phoneshop.web.service.PageService;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements PageService {

    private static final int FIRST_PAGE_NUMBER = 1;
    private static int AMOUNT_OF_NEIGHBORING_PAGES = 3;

    @Override
    public Pagination getPagination(int itemsNumber, int pageNumber) {
        int difference = pageNumber - AMOUNT_OF_NEIGHBORING_PAGES;
        int leftBorder = (difference > 0) ? difference : FIRST_PAGE_NUMBER;

        int pageAmount = countPageAmount(itemsNumber);
        difference = pageNumber + AMOUNT_OF_NEIGHBORING_PAGES;
        int rightBorder = (difference < pageAmount) ? difference : pageAmount;

        return new Pagination(pageNumber, leftBorder, rightBorder);
    }

    @Override
    public int countPageAmount(int itemsNumber) {
        if (itemsNumber == 0) {
            return 1;
        }
        return (int) Math.ceil((double) itemsNumber / AMOUNT_OF_PHONES_ON_PAGE);
    }

    @Override
    public int normalizedPageNumber(int itemsNumber, int pageNumber) {
        int pageAmount = countPageAmount(itemsNumber);
        if (pageNumber >= FIRST_PAGE_NUMBER && pageNumber <= pageAmount) {
            return pageNumber;
        }
        return pageAmount;
    }
}
