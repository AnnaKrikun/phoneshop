package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.phoneshop.web.exception.PageNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import static com.es.phoneshop.web.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = PRODUCT_DETAILS_MAPPING)
public class ProductDetailsPageController {
    private static final String PHONE = "phone";
    private static final String CART = "cart";

    @Resource
    private PhoneService phoneService;
    @Resource
    private CartService cartService;

    @GetMapping
    public String showProductList(@PathVariable Long phoneId, Model model) {
        Phone phone = phoneService.get(phoneId).orElseThrow(PageNotFoundException::new);
        model.addAttribute(PHONE, phone);
        model.addAttribute(CART, cartService.getCart());
        return PRODUCT_DETAILS_PAGE_NAME;
    }

    @ExceptionHandler({PageNotFoundException.class})
    public String pageNotFound() {
        return NOT_FOUND_PAGE_NAME;
    }
}
