package com.es.phoneshop.web.validation;

import com.es.core.model.phone.Phone;
import com.es.core.service.PhoneService;
import com.es.core.service.StockService;
import com.es.phoneshop.web.cart.QuickProductEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class QuickProductEntryValidator implements Validator {
    private static final String PHONE_IDS = "phoneIds";
    private static final String PHONE_QUANTITIES = "phoneQuantities";

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

        String[] phoneIds = quickProductEntry.getPhoneIds();
        String[] phoneQuantities = quickProductEntry.getPhoneQuantities();

        for (int i = 0; i < phoneIds.length; i++) {
            if (isNotBlank(phoneIds[i]) && isNotBlank(phoneQuantities[i])) {
                if (checkIfPositiveLong(phoneIds[i], phoneQuantities[i], errors)) {
                    Long phoneId = Long.valueOf(phoneIds[i]);
                    Long phoneQuantity = Long.valueOf(phoneQuantities[i]);
                    if (checkPhone(phoneId, errors) &&
                            checkStock(phoneId, phoneQuantity, errors)) {
                        quickProductEntry.getValidData().put(phoneId, phoneQuantity);
                    }
                }
            }
        }
    }

    private boolean checkStock(Long phoneId, Long phoneQuantity, Errors errors) {
        long available = stockService.getAvailableStock(phoneId);
        if (phoneQuantity > available) {
            errors.rejectValue(PHONE_QUANTITIES, QUANTITY_NOT_ENOUGH);
            return false;
        }
        return true;
    }

    private boolean checkPhone(Long phoneId, Errors errors) {
        Optional<Phone> phone = phoneService.get(phoneId);
        if (!phone.isPresent()) {
            errors.rejectValue(PHONE_IDS, NOT_EXISTING_PRODUCT);
            return false;
        }
        if (phone.get().getPrice() == null) {
            errors.rejectValue(PHONE_IDS, NULL_PRICE);
            return false;
        }
        return true;
    }

    private boolean checkIfPositiveLong(String phoneId, String phoneQuantity, Errors errors) {
        boolean phoneIdIsValid = parseToPositiveLong(phoneId, PHONE_IDS, errors);
        boolean phoneQuantityIsValid = parseToPositiveLong(phoneQuantity, PHONE_QUANTITIES, errors);
        if (phoneIdIsValid && phoneQuantityIsValid) {
            return true;
        }
        return false;
    }

    private boolean parseToPositiveLong(String stringValue, String field, Errors errors) {
        try {
            Long value = Long.valueOf(stringValue);
            if (value < 0) {
                errors.rejectValue(field, NOT_POSITIVE);
                return false;
            }
        } catch (NumberFormatException e) {
            errors.rejectValue(field, NOT_A_NUMBER);
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
