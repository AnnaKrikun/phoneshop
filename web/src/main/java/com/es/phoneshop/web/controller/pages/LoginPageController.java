package com.es.phoneshop.web.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.es.phoneshop.web.constants.ControllerConstants.LOGIN;
import static com.es.phoneshop.web.constants.ControllerConstants.LOGIN_PAGE_NAME;

@Controller
@RequestMapping(LOGIN)
public class LoginPageController {

    @GetMapping
    public String getLoginPage() {
        return LOGIN_PAGE_NAME;
    }
}
