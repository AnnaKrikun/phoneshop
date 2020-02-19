package com.es.phoneshop.web.controller.pages;

import javax.annotation.Resource;

import com.es.phoneshop.web.service.ProductPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {

    @Resource
    private ProductPageService productPageService;

    @GetMapping
    public String showProductList(@RequestParam(name = "searchQuery", defaultValue = "") String searchQuery,
                                  @RequestParam(name = "sort", defaultValue = "brand") String sort,
                                  @RequestParam(name = "order", defaultValue = "asc") String order,
                                  @RequestParam(name="page", defaultValue = "1") int pageNumber,
                                  Model model) {
        model.addAttribute("phonePage", productPageService.getPhonePage(searchQuery, sort, order, pageNumber));
        return "productList";
    }
}
