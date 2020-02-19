$(document).ready(function () {
    function addToCart() {
        var parentTr = $(this).parent().parent();
        var id = parentTr.find(".phone-id").val();
        var quantity = parentTr.find(".phone-quantity").val();
        var requestData = {};
        requestData["phoneIdString"] = id;
        requestData["quantityString"] = quantity;

        $.ajax({
            url: "ajaxCart",
            type: "POST",
            contentType: "application/json;charset=UTF-8",
            dataType: "json",
            data: JSON.stringify(requestData),
            success: function (cartStatus) {
                if (cartStatus.isValid) {
                    parentTr.find(".error-message").text("");
                    parentTr.find(".success-message").text(cartStatus.successMessage);
                } else {
                    parentTr.find(".success-message").text("");
                    parentTr.find(".error-message").text(cartStatus.errorMessage);
                }
                $(".cart-total-quantity").text(cartStatus.totalQuantity);
                $(".cart-total-price").text(cartStatus.totalPrice);
            }
        })
    }

    function init() {
        $(".add-to-cart").bind("click", addToCart);
    }

    $(init);
});

