package com.es.phoneshop.web.validation;

import com.es.phoneshop.web.cart.CartItemStringData;
import com.es.core.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CartItemStringDataValidator implements Validator {
    private static final String QUANTITY = "quantityString";
    private static final String PHONE_ID = "phoneIdString";
    private static final String PHONE_ID_IS_EMPTY = "PhoneId can't empty!";
    private static final String QUANTITY_IS_EMPTY = "Quantity can't be empty!";
    private static final String QUANTITY_LESS_EQUAL_ZERO = "Quantity can't be less than 1!";
    private static final String QUANTITY_NOT_ENOUGH = "Not enough quantity!";
    private static final String NOT_NUMBER = "Not a number!";

    private StockService stockService;

    @Autowired
    public CartItemStringDataValidator(StockService stockService) {
        this.stockService = stockService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CartItemStringDataValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        CartItemStringData cartItemStringData = (CartItemStringData) object;
        checkIfBlank(cartItemStringData, errors);
        try {
            long quantity = Long.parseLong(cartItemStringData.getQuantityString());
            long phoneId = Long.parseLong(cartItemStringData.getPhoneIdString());
            checkQuantity(quantity, phoneId, errors);
        } catch (NumberFormatException exception) {
            errors.rejectValue(QUANTITY, NOT_NUMBER);
        }
    }

    private void checkQuantity(Long quantity, Long phoneId, Errors errors) {
        if (quantity <= 0) {
            errors.rejectValue(QUANTITY, QUANTITY_LESS_EQUAL_ZERO);
        }
        int available = stockService.getStockById(phoneId).get().getStock() -
                stockService.getStockById(phoneId).get().getReserved();
        if (quantity > available) {
            errors.rejectValue(QUANTITY, QUANTITY_NOT_ENOUGH);
        }
    }

    private void checkIfBlank(CartItemStringData cartItemStringData, Errors errors) {
        if (StringUtils.isBlank(cartItemStringData.getPhoneIdString())) {
            errors.rejectValue(PHONE_ID, PHONE_ID_IS_EMPTY);
        }
        if (StringUtils.isBlank(cartItemStringData.getQuantityString())) {
            errors.rejectValue(QUANTITY, QUANTITY_IS_EMPTY);
        }
    }
}
