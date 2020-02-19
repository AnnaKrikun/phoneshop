package com.es.phoneshop.web.service;

import com.es.phoneshop.web.page.ProductListPage;

public interface ProductPageService {
    ProductListPage getPhonePage(String searchQuery, String sort, String order, int pageNumber);
}
