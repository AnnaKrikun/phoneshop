package com.es.phoneshop.web.validation;

import com.es.core.model.phone.Phone;
import com.es.core.service.PhoneService;
import com.es.core.service.StockService;
import com.es.phoneshop.web.cart.QuickProductEntry;
import com.es.phoneshop.web.cart.QuickProductEntryItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class QuickProductEntryValidator implements Validator {
    private static final String PHONE_IDS = "quickProductEntryItems[%s].phoneId";
    private static final String PHONE_QUANTITIES = "quickProductEntryItems[%s].phoneQuantity";

    private static final String NOT_POSITIVE = "not.positive";
    private static final String NOT_A_NUMBER = "not.number";
    private static final String NOT_EXISTING_PRODUCT = "not.existing.product";
    private static final String QUANTITY_NOT_ENOUGH = "quantity.not.enough";
    private static final String NULL_PRICE = "null.price";

    @Resource
    private PhoneService phoneService;
    @Resource
    private StockService stockService;

    @Override
    public boolean supports(Class<?> aClass) {
        return QuickProductEntryValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        QuickProductEntry quickProductEntry = (QuickProductEntry) object;
        int i = 0;
        for (QuickProductEntryItem item : quickProductEntry.getQuickProductEntryItems()) {
            if (isNotBlank(item.getPhoneId()) && item.getPhoneQuantity() != null) {
                if (checkIfPositiveLong(item.getPhoneId(), item.getPhoneQuantity(), i, errors)) {
                    Long phoneId = Long.valueOf(item.getPhoneId());
                    Long phoneQuantity = item.getPhoneQuantity();
                    if (checkPhone(phoneId, i, errors) && checkStock(phoneId, phoneQuantity, i, errors)) {
                        quickProductEntry.getValidData().put(phoneId, phoneQuantity);
                    }
                }
            }
            i++;
        }
    }

    private boolean checkIfPositiveLong(String phoneId, Long phoneQuantity, int i, Errors errors) {
        boolean phoneIdIsValid = parseToPositiveLong(phoneId, i, errors);
        boolean phoneQuantityIsValid = checkIfPositive(phoneQuantity, i, errors);
        if (phoneIdIsValid && phoneQuantityIsValid) {
            return true;
        }
        return false;
    }

    private boolean checkStock(Long phoneId, Long phoneQuantity, int index, Errors errors) {
        long available = stockService.getAvailableStock(phoneId);
        if (phoneQuantity > available) {
            errors.rejectValue(String.format(PHONE_QUANTITIES, index), QUANTITY_NOT_ENOUGH);
            return false;
        }
        return true;
    }

    private boolean checkPhone(Long phoneId, int index, Errors errors) {
        Optional<Phone> phone = phoneService.get(phoneId);
        if (!phone.isPresent()) {
            errors.rejectValue(String.format(PHONE_IDS, index), NOT_EXISTING_PRODUCT);
            return false;
        }
        if (phone.get().getPrice() == null) {
            errors.rejectValue(String.format(PHONE_IDS, index), NULL_PRICE);
            return false;
        }
        return true;
    }

    private boolean checkIfPositive(Long value, int index, Errors errors) {
        if (value < 0) {
            errors.rejectValue(String.format(PHONE_QUANTITIES, index), NOT_POSITIVE);
            return false;
        }
        return true;
    }

    private boolean parseToPositiveLong(String stringValue, int index, Errors errors) {
        try {
            Long value = Long.valueOf(stringValue);
            if (value < 0) {
                errors.rejectValue(String.format(PHONE_IDS, index), NOT_POSITIVE);
                return false;
            }
        } catch (NumberFormatException e) {
            errors.rejectValue(String.format(PHONE_IDS, index), NOT_A_NUMBER);
            return false;
        }
        return true;
    }

    private boolean isNotBlank(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return true;
    }
}
