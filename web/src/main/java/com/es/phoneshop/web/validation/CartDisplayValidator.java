package com.es.phoneshop.web.validation;

import com.es.core.service.StockService;
import com.es.phoneshop.web.cart.CartDisplay;
import com.es.phoneshop.web.cart.CartItemDisplay;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class CartDisplayValidator implements Validator {
    private static final String QUANTITY_FIELD_FORMAT = "cartDisplayItems[%s].quantity";
    private static final String QUANTITY_NOT_ENOUGH = "quantity.not.enough";

    private StockService stockService;

    @Autowired
    public CartDisplayValidator(StockService stockService) {
        this.stockService = stockService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CartDisplayValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        CartDisplay cartDisplay = (CartDisplay) object;
        List<CartItemDisplay> cartItemDisplays = cartDisplay.getCartDisplayItems();
        if (CollectionUtils.isNotEmpty(cartItemDisplays)) {
            for (int i = 0; i < cartItemDisplays.size(); i++) {
                checkQuantity(i, cartItemDisplays.get(i).getQuantity(), cartItemDisplays.get(i).getPhoneId(), errors);
            }
        }
    }

    private void checkQuantity(int index, Long quantity, Long phoneId, Errors errors) {
        long available = stockService.getByPhoneId(phoneId).get().getStock() -
                stockService.getByPhoneId(phoneId).get().getReserved();
        if (quantity != null && quantity > available) {
            errors.rejectValue(String.format(QUANTITY_FIELD_FORMAT, index), QUANTITY_NOT_ENOUGH);
        }
    }
}
