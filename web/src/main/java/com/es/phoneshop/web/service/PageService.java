package com.es.phoneshop.web.service;

import com.es.phoneshop.web.page.Pagination;

public interface PageService {

    int AMOUNT_OF_PHONES_ON_PAGE = 10;

    Pagination getPagination(int itemsNumber, int pageNumber);

    int countPageAmount(int itemsNumber);

    int normalizedPageNumber(int itemsNumber, int pageNumber);
}
