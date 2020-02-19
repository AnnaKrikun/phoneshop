package com.es.phoneshop.web.errors;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ErrorMessageCreator {

    public String createErrorMessage(BindingResult bindingResult) {
        String errorMessage = "";
        for (Object object : bindingResult.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                errorMessage += fieldError.getCode() + '\n';
            }
        }
        return errorMessage;
    }
}
