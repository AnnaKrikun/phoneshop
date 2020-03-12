package com.es.phoneshop.web.controller;

import com.es.core.exception.ProductNotFoundException;
import com.es.core.service.CartService;
import com.es.phoneshop.web.cart.CartItemStringData;
import com.es.phoneshop.web.cart.CartStatus;
import com.es.phoneshop.web.errors.ErrorMessageCreator;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.es.phoneshop.web.constants.ControllerConstants.AJAX_CART_MAPPING;

@Controller
@RequestMapping(value = AJAX_CART_MAPPING, produces = {MediaType.APPLICATION_JSON_VALUE})
public class AjaxCartController {
    private static final String SUCCESS = "Added successfully!";
    private static final String ERROR = "Error!";

    @Resource
    private CartService cartService;
    @Resource
    private ErrorMessageCreator errorMessageCreator;

    private final Validator cartItemStringDataValidator;

    public AjaxCartController(@Qualifier("cartItemStringDataValidator") Validator cartItemStringDataValidator) {
        this.cartItemStringDataValidator = cartItemStringDataValidator;
    }

/*    private final Validator cartLongDataValidator;

    @Autowired
    public AjaxCartController(@Qualifier("cartLongDataValidator") Validator cartLongDataValidator) {
        this.cartLongDataValidator = cartLongDataValidator;
    }*/


    @PostMapping(produces = "application/json")
    public @ResponseBody
    CartStatus addPhone(@RequestBody @Valid CartItemStringData cartItemStringData, BindingResult bindingResult) {
        cartItemStringDataValidator.validate(cartItemStringData, bindingResult);
        if (!bindingResult.hasErrors()) {
            Long[] cartItemLongData = getLongValues(cartItemStringData);
            cartService.addPhone(cartItemLongData[0], cartItemLongData[1]);
            return createCartStatus(true, null, SUCCESS);
        }
        String error = errorMessageCreator.createErrorMessage(bindingResult);
        return createCartStatus(false, error, null);
    }

/*
    @PostMapping(produces = "application/json")
    public @ResponseBody
    CartStatus addPhone(@RequestBody @Valid CartItemLongData cartItemLongData, BindingResult bindingResult) {
        cartLongDataValidator.validate(cartItemLongData, bindingResult);
        if (!bindingResult.hasErrors()) {
            cartService.addPhone(cartItemLongData.getPhoneId(), cartItemLongData.getQuantity());
            return createCartStatus(true, null, SUCCESS);
        }
        String error = errorMessageCreator.createErrorMessage(bindingResult);
        return createCartStatus(false, error, null);
    }
*/

    @ExceptionHandler({InvalidFormatException.class, ProductNotFoundException.class, IllegalStateException.class})
    public @ResponseBody
    CartStatus handleException() {
        return createCartStatus(false, ERROR, null);
    }

    private CartStatus createCartStatus(boolean valid, String error, String success) {
        CartStatus cartStatus = new CartStatus();
        cartStatus.setTotalQuantity(cartService.getCartTotalQuantity());
        cartStatus.setTotalPrice(cartService.getCartTotalPrice());
        if (valid == true) {
            cartStatus.setValid(true);
            cartStatus.setSuccessMessage(success);
        } else {
            cartStatus.setValid(false);
            cartStatus.setErrorMessage(error);
        }
        return cartStatus;
    }

    private Long[] getLongValues(CartItemStringData cartItemStringData) {
        Long[] longValues = new Long[2];
        longValues[0] = Long.valueOf(cartItemStringData.getPhoneIdString().trim());
        longValues[1] = Long.valueOf(cartItemStringData.getQuantityString().trim());
        return longValues;
    }

}
