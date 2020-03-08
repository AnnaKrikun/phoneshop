package com.es.phoneshop.web.controller.pages;

import com.es.core.service.CartService;
import com.es.phoneshop.web.cart.CartDisplay;
import com.es.phoneshop.web.helper.CartHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

import static com.es.phoneshop.web.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = CART_MAPPING)
public class CartPageController {
    private static final String PHONE_ID = "phoneId";
    private static final String UPDATE = "update";
    private static final String REMOVE = "remove";

    @Resource
    private CartService cartService;
    @Resource
    private CartHelper cartHelper;

    private final Validator cartDisplayValidator;

    @Autowired
    public CartPageController(@Qualifier("cartDisplayValidator") Validator cartDisplayValidator) {
        this.cartDisplayValidator = cartDisplayValidator;
    }

    @GetMapping
    public String getCart(Model model) {
        cartHelper.setCartDisplayAttribute(model);
        return CART_PAGE_NAME;
    }

    @PutMapping(params = UPDATE)
    public String updateCart(@Valid CartDisplay cartDisplay, BindingResult bindingResult, Model model) {
        cartDisplayValidator.validate(cartDisplay, bindingResult);
        if (bindingResult.hasErrors()) {
            if (cartDisplay.getCartDisplayItems() != null) {
                cartHelper.setPropertiesCartUpdateDisplay(cartDisplay, model);
            }
            return CART_PAGE_NAME;
        }
        Map<Long, Long> mapForUpdate = cartHelper.createMapForUpdate(cartDisplay);
        cartService.update(mapForUpdate);
        return REDIRECT_CART_PAGE;
    }

    @PostMapping(params = REMOVE)
    public String deleteProductFormCart(@RequestParam(PHONE_ID) Long phoneId) {
        cartService.remove(phoneId);
        return REDIRECT_CART_PAGE;
    }


}
