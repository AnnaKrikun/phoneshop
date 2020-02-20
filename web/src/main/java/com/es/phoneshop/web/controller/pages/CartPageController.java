package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.phoneshop.web.cart.CartDisplay;
import com.es.phoneshop.web.cart.CartItemDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.es.phoneshop.web.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = CART_MAPPING)
public class CartPageController {
    private static final String PHONE_ID = "phoneId";
    private static final String CART_DISPLAY = "cartDisplay";
    private static final String CART = "cart";
    private static final String UPDATE = "update";
    private static final String REMOVE = "remove";

    @Resource
    private CartService cartService;

    private final Validator cartDisplayValidator;

    @Autowired
    public CartPageController(@Qualifier("cartDisplayValidator") Validator cartDisplayValidator) {
        this.cartDisplayValidator = cartDisplayValidator;
    }


    @GetMapping
    public String getCart(Model model) {
        setCartDisplayAttribute(model);
        return CART_PAGE_NAME;
    }

    @PutMapping(params = UPDATE)
    public String updateCart(@Valid CartDisplay cartDisplay, BindingResult bindingResult, Model model) {
        cartDisplayValidator.validate(cartDisplay, bindingResult);
        if (bindingResult.hasErrors()) {
            if (cartDisplay.getCartDisplayItems() != null) {
                setPropertiesCartUpdateDisplay(cartDisplay, model);
            }
            return CART_PAGE_NAME;
        }
        Map<Long, Long> mapForUpdate = createMapForUpdate(cartDisplay);
        cartService.update(mapForUpdate);
        return REDIRECT_CART_PAGE;
    }

    @PostMapping(params = REMOVE)
    public String deleteProductFormCart(@RequestParam(PHONE_ID) Long phoneId) {
        cartService.remove(phoneId);
        return REDIRECT_CART_PAGE;
    }

    private Map<Long, Long> createMapForUpdate(CartDisplay cartDisplay) {
        Map<Long, Long> mapForUpdate = cartDisplay.getCartDisplayItems().stream()
                .collect(Collectors.toMap(CartItemDisplay::getPhoneId, CartItemDisplay::getQuantity));
        return mapForUpdate;

    }

    private void setCartDisplayAttribute(Model model) {
        Cart cart = cartService.getCart();
        List<CartItemDisplay> cartDisplayItems = cart.getCartItems().stream()
                .map(item -> new CartItemDisplay(item.getPhone(), item.getQuantity()))
                .collect(Collectors.toList());

        model.addAttribute(CART_DISPLAY, new CartDisplay(cartDisplayItems));
        model.addAttribute(CART, cartService.getCart());
    }

    private void setPropertiesCartUpdateDisplay(CartDisplay cartDisplay, Model model) {
        Cart cart = cartService.getCart();
        Map<Long, CartItem> items = cart.getCartItems().stream()
                .collect(Collectors.toMap(item -> item.getPhone().getId(), item -> item));

        cartDisplay.getCartDisplayItems()
                .forEach(cartDisplayItem ->
                        setPropertiesCartItem(cartDisplayItem, items.get(cartDisplayItem.getPhoneId())));
        model.addAttribute(CART, cartService.getCart());
    }

    private void setPropertiesCartItem(CartItemDisplay cartDisplayItem, CartItem cartItem) {
        Phone phone = cartItem.getPhone();
        if (phone != null) {
            cartDisplayItem.setBrand(phone.getBrand());
            cartDisplayItem.setImageUrl(phone.getImageUrl());
            cartDisplayItem.setModel(phone.getModel());
            cartDisplayItem.setPrice(phone.getPrice());
            cartDisplayItem.setDisplaySizeInches(phone.getDisplaySizeInches());
            cartDisplayItem.setColors(phone.getColors());
        }
    }
}
