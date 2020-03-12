package com.es.core.service;

import com.es.core.dao.PhoneDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.impl.SessionCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionCartServiceTest {
    private static final Integer STOCK = 10;
    private static final Integer RESERVED = 1;
    private static final Long QUANTITY = 1L;
    private static final int COUNT_ITEMS = 1;
    private static final String TEXT_FOR_PHONE = "text";
    private static final Long PHONE_ID = 10L;
    private static final Long[] ColorIds = {1000L, 1001L, 1002L, 1003L, 1004L, 1005L, 1006L, 1007L, 1008L, 1009L, 1010L, 1011L, 1012L, 1013L};
    private static final String[] ColorCodes = {"Black", "White", "Yellow", "Blue", "Red", "Purple", "Gray", "Green", "Pink", "Gold",
            "Silver", "Orange", "Brown", "256"};

    @InjectMocks
    private CartService cartService = new SessionCartService();

    @Mock
    private Cart mockCart;

    @Mock
    private PhoneDao mockPhoneDao;

    @Mock
    private StockDao mockStockDao;

    private Phone phoneWithoutPrice;

    private List<CartItem> cartItemsList;
    private List<Phone> phoneList;
    private List<Stock> stockList;
    private List<Color> colorList;

    @Before
    public void init() {
        initColorList();
        initPhoneList();
        initPhoneWithoutPrice();
        initStockList();
        initMockPhoneDao();
        initMockCart();
        initStockDao();
    }

    private void initStockList() {
        stockList = new ArrayList<>();
        phoneList.stream().forEach(phone -> stockList.add(new Stock(phone, STOCK, RESERVED)));
    }

    private void initStockDao() {
        when(mockStockDao.getStockById(isA(Long.class))).thenReturn(Optional.empty());
        for (Stock stock : stockList) {
            when(mockStockDao.getStockById(stock.getPhone().getId())).thenReturn(Optional.of(stock));
        }
    }

    private void initColorList() {
        colorList = new ArrayList<>();
        for (int i = 0; i < ColorIds.length; i++) {
            colorList.add(new Color(ColorIds[i], ColorCodes[i]));
        }
    }

    private void initPhoneList() {
        phoneList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            phoneList.add(createPhone(Integer.toString(i), Integer.toString(i), (long) i, i));
        }
    }

    private void initPhoneWithoutPrice() {
        phoneWithoutPrice = createPhone(TEXT_FOR_PHONE, TEXT_FOR_PHONE, PHONE_ID, 1);
        phoneWithoutPrice.setPrice(null);
        phoneList.add(phoneWithoutPrice);
    }

    private void initMockPhoneDao() {
        when(mockPhoneDao.get(isA(Long.class))).thenReturn(Optional.empty());
        for (Phone phone : phoneList) {
            when(mockPhoneDao.get(phone.getId())).thenReturn(Optional.of(phone));
        }
    }

    private void initMockCart() {
        cartItemsList = new ArrayList<>();
        when(mockCart.getCartItems()).thenReturn(cartItemsList);
    }

    protected Phone createPhone(String brand, String model, Long id, int countColors) {
        Phone phone = new Phone();
        phone.setBrand(brand);
        phone.setModel(model);
        phone.setPrice(new BigDecimal(100));

        Set<Color> colors = new HashSet<>(colorList.subList(0, countColors));

        phone.setColors(colors);
        phone.setId(id);
        return phone;
    }

    @Test
    public void shouldAddPhoneCorrectly() {
        int finalQuantity = 2;
        Phone phone = phoneList.get(1);

        cartService.addPhone(phone.getId(), QUANTITY);

        verify(mockPhoneDao).get(eq(phone.getId()));
        verify(mockCart).setTotalPrice(eq(phone.getPrice()));
        verify(mockCart).setTotalQuantity(eq(QUANTITY));
        verify(mockStockDao).update(phone.getId(), finalQuantity);

        assertEquals(COUNT_ITEMS, cartItemsList.size());
    }

    @Test
    public void shouldAddPhoneWhichIsInAlreadyCartCorrectly() {
        Phone phone = phoneList.get(1);

        Long finalQuantity = QUANTITY * 2;
        BigDecimal finalPrice = phone.getPrice().multiply(new BigDecimal(2));
        int finalCountItems = COUNT_ITEMS * 2;

        cartService.addPhone(phone.getId(), QUANTITY);
        cartService.addPhone(phone.getId(), QUANTITY);

        verify(mockPhoneDao, times(2)).get(eq(phone.getId()));

        verify(mockCart).setTotalPrice(eq(phone.getPrice()));
        verify(mockCart).setTotalQuantity(eq(QUANTITY));

        verify(mockCart).setTotalPrice(eq(finalPrice));
        verify(mockCart).setTotalQuantity(eq(finalQuantity));

        verify(mockStockDao).update(phone.getId(), finalCountItems);

        assertEquals(COUNT_ITEMS, cartItemsList.size());
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldThrowPhoneNotFoundExceptionWhenAddNotExistingPhone() {
        Phone phone = new Phone();
        phone.setId(100L);

        cartService.addPhone(phone.getId(), QUANTITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenAddPhoneWithNullPrice() {
        cartService.addPhone(phoneWithoutPrice.getId(), QUANTITY);
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldThrowPhoneNotFoundExceptionWhenAddPhoneWithoutStock() {
        Phone phone = new Phone();
        phone.setId(2000L);

        cartService.addPhone(phone.getId(), QUANTITY);
    }

    @Test(expected = OutOfStockException.class)
    public void shouldThrowOutOfStockExceptionWhenAddPhoneOutOfStock() {
        Phone phone = phoneList.get(1);

        cartService.addPhone(phone.getId(), 100L);
    }

    @Test
    public void shouldRemovePhoneCorrectly() {
        BigDecimal finalPrice = new BigDecimal(200);
        Long finalQuantity = 2L;
        int expectedSize = 2;
        List<CartItem> cartItems = initNotEmptyCart();

        Phone phone = phoneList.get(2);

        cartService.remove(phone.getId());

        verify(mockCart).setTotalPrice(eq(finalPrice));
        verify(mockCart).setTotalQuantity(eq(finalQuantity));
        verify(mockStockDao).getStockById(phone.getId());
        verify(mockStockDao).update(phone.getId(), 0);

        assertEquals(expectedSize, cartItems.size());
    }

    @Test
    public void shouldNotRemovePhoneWithNullId() {
        int expectedSize = 3;
        List<CartItem> cartItems = initNotEmptyCart();

        cartService.remove(null);

        assertEquals(expectedSize, cartItems.size());
    }

    @Test
    public void shouldDoNothingWhenRemoveNotExistingCartItem() {
        int expectedSize = 3;
        List<CartItem> cartItems = initNotEmptyCart();
        Phone phone = phoneList.get(5);

        cartService.remove(phone.getId());

        assertEquals(expectedSize, cartItems.size());
    }

    @Test
    public void shouldUpdateCartCorrectly() {
        List<CartItem> cartItems = initNotEmptyCart();

        Map<Long, Long> items = new HashMap<>();
        items.put(2L, 1L);
        items.put(5L, 1L);
        items.put(4L, 1L);


        cartService.update(items);

        verify(mockCart, atLeast(3)).getCartItems();

        assertEquals(3, cartItems.size());
    }

    private List<CartItem> initNotEmptyCart() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(phoneList.get(2), 1L));
        cartItems.add(new CartItem(phoneList.get(3), 1L));
        cartItems.add(new CartItem(phoneList.get(4), 1L));
        when(mockCart.getCartItems()).thenReturn(cartItems);
        return cartItems;
    }


}
