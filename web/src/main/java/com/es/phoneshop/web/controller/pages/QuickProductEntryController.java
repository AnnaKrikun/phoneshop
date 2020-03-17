package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.phoneshop.web.cart.QuickProductEntry;
import com.es.phoneshop.web.errors.ErrorMessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.es.phoneshop.web.constants.ControllerConstants.*;

@Controller
@RequestMapping(QUICK_PRODUCT_ENTRY)
public class QuickProductEntryController {
    private static final String ADD = "/add";

    private static final String INPUTS_COUNT = "inputsCount";
    private static final String CART = "cart";
    private static final String QUICK_PRODUCT_ENTRY = "quickProductEntry";
    private static final String ERRORS = "errors";

    @Value("10")
    private int inputsCount;

    @Resource
    private CartService cartService;
    @Resource
    private ErrorMessageCreator errorMessageCreator;

    private Validator quickProductEntryValidator;

    @Autowired
    public QuickProductEntryController(@Qualifier("quickProductEntryValidator") Validator quickProductEntryValidator) {
        this.quickProductEntryValidator = quickProductEntryValidator;
    }

    @GetMapping
    public String quickOrder(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute(INPUTS_COUNT, inputsCount);
        model.addAttribute(CART, cart);
        model.addAttribute(QUICK_PRODUCT_ENTRY, new QuickProductEntry());
        return QUICK_PRODUCT_ENTRY_PAGE_NAME;
    }

    @PostMapping(ADD)
    public String makeQuickOrder(@Valid QuickProductEntry quickProductEntry,
                                 BindingResult bindingResult,
                                 Model model) {
        quickProductEntryValidator.validate(quickProductEntry, bindingResult);
        if (bindingResult.hasErrors()) {
            addErrorAttribute(model, bindingResult);
            addAttributes(model, quickProductEntry);
            return QUICK_PRODUCT_ENTRY_PAGE_NAME;
        }
        addAttributes(model, quickProductEntry);
        return REDIRECT_QUICK_PRODUCT_ENTRY;
    }

    private void addAttributes(Model model, QuickProductEntry quickProductEntry) {
        Cart cart = cartService.getCart();
        cartService.addPhones(quickProductEntry.getValidData());
        model.addAttribute(CART, cart);
        model.addAttribute(INPUTS_COUNT, inputsCount);
    }

    private void addErrorAttribute(Model model, BindingResult bindingResult) {
        String errors = errorMessageCreator.createErrorMessage(bindingResult);
        model.addAttribute(ERRORS, errors);
    }
}
