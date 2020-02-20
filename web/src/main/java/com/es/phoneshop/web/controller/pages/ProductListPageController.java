package com.es.phoneshop.web.controller.pages;

import com.es.core.service.CartService;
import com.es.phoneshop.web.service.ProductPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import static com.es.phoneshop.web.constants.ControllerConstants.PRODUCT_LIST_MAPPING;
import static com.es.phoneshop.web.constants.ControllerConstants.PRODUCT_LIST_PAGE_NAME;

@Controller
@RequestMapping(value = PRODUCT_LIST_MAPPING)
public class ProductListPageController {
    private static final String PHONE_PAGE = "phonePage";
    private static final String SEARCH_QUERY_REQUEST_PARAM = "searchQuery";
    private static final String SORT_REQUEST_PARAM = "sort";
    private static final String ORDER_REQUEST_PARAM = "order";
    private static final String PAGE_REQUEST_PARAM = "page";
    private static final String SEARCH_QUERY_DEFAULT_VALUE = "";
    private static final String SORT_DEFAULT_VALUE = "brand";
    private static final String ORDER_DEFAULT_VALUE = "asc";
    private static final String PAGE_DEFAULT_VALUE = "1";
    private static final String CART = "cart";

    @Resource
    private ProductPageService productPageService;
    @Resource
    private CartService cartService;

    @GetMapping
    public String showProductList(@RequestParam(name = SEARCH_QUERY_REQUEST_PARAM,
            defaultValue = SEARCH_QUERY_DEFAULT_VALUE) String searchQuery,
                                  @RequestParam(name = SORT_REQUEST_PARAM, defaultValue = SORT_DEFAULT_VALUE)
                                          String sort,
                                  @RequestParam(name = ORDER_REQUEST_PARAM, defaultValue = ORDER_DEFAULT_VALUE)
                                          String order,
                                  @RequestParam(name = PAGE_REQUEST_PARAM, defaultValue = PAGE_DEFAULT_VALUE)
                                          int pageNumber,
                                  Model model) {
        model.addAttribute(PHONE_PAGE, productPageService.getPhonePage(searchQuery, sort, order, pageNumber));
        model.addAttribute(CART, cartService.getCart());
        return PRODUCT_LIST_PAGE_NAME;
    }
}
