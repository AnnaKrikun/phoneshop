package com.es.core.dao;

import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/test-config.xml")
public class JdbcPhoneDaoTest {
    private static final String ERROR_IN_EXISTING_KEY = "Result for existing key is empty";
    private static final String ERROR_IN_PHONE_SAVE = "Error: Number of added / found phones = ";
    private static final String ERROR_ID_GENERATED_ID = "Error: Expected / found ID after saving = ";
    private static final String BRAND = "brand";
    private static final String MODEL = "model";
    private static final Long EXISTING_KEY = 1001L;
    private static final Long NOT_EXISTING_KEY = -2020L;
    private static final int OFFSET_SUCCESS = 1;
    private static final int LIMIT_SUCCESS = 5;
    private static final int ZERO_SIZE = 0;
    private static final Long ZERO = 0L;
    private static final String ERROR_IN_FIND_ALL_PHONES = "Error: Number of found / expected phones = ";
    private static final int OFFSET_OUT_OF_RANGE = 100;
    private static final int LIMIT_OUT_OF_RANGE_POSITIVE = 100;
    private static final int LIMIT_ZERO = 0;
    private static final int OFFSET_ZERO = 0;
    private static final Long ADDED_PHONES = 1L;
    private static final String COUNT_PHONES = "select count (*) from phones";
    private static final String FIND_MAX_ID = "select max(id) from phones";
    private static final String EMPTY_QUERY = "";
    private static final int EXPECTED_SIZE = 10;
    private static final String QUERY = "NOKIA";
    private static final int EXPECTED_SIZE_QUERY = 2;
    private static final String ASC = "asc";

    private Phone phone, phone1;
    private List phones;
    private Optional<Phone> optionalPhone;

    @Autowired
    private PhoneDao phoneDao;

    @Autowired
    private JdbcTemplate jdbcTemplateTest;

    @Before
    public void init() {
        phone = new Phone();
        phone1 = new Phone();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void shouldThrowIllegalArgumentExceptionWhenSaveNullBrand() {
        phone.setBrand(null);
        phoneDao.save(phone);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void shouldThrowIllegalArgumentExceptionWhenSaveNullModel() {
        phone.setModel(null);
        phoneDao.save(phone);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void shouldThrowIllegalArgumentExceptionWhenSave2PhonesWithEqualBrands() {
        phone.setBrand(BRAND);
        phone1.setBrand(BRAND);

        phoneDao.save(phone);
        phoneDao.save(phone1);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void shouldThrowIllegalArgumentExceptionWhenSave2PhonesWithEqualModels() {
        phone.setModel(MODEL);
        phone1.setModel(MODEL);

        phoneDao.save(phone);
        phoneDao.save(phone1);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void shouldThrowIllegalArgumentExceptionWhenSave2PhonesWithEqualBrandsAndModels() {
        phone.setBrand(BRAND);
        phone1.setBrand(BRAND);
        phone.setModel(MODEL);
        phone1.setModel(MODEL);

        phoneDao.save(phone);
        phoneDao.save(phone1);
    }

    @Test
    @DirtiesContext
    public void shouldSavePhoneWithNullIdSuccessfully() {
        Long amountBeforeSave = jdbcTemplateTest.queryForObject(COUNT_PHONES, Long.class);
        Long maxIdBeforeSave = jdbcTemplateTest.queryForObject(FIND_MAX_ID, Long.class);

        phone.setModel(MODEL);
        phone.setBrand(BRAND);

        phoneDao.save(phone);

        Long amountAfterSave = jdbcTemplateTest.queryForObject(COUNT_PHONES, Long.class);
        Long maxIdAfterSave = jdbcTemplateTest.queryForObject(FIND_MAX_ID, Long.class);

        Assert.isTrue(ADDED_PHONES.equals(amountAfterSave - amountBeforeSave),
                ERROR_IN_PHONE_SAVE + ADDED_PHONES + " / " + (amountAfterSave - amountBeforeSave));
        Assert.isTrue(ADDED_PHONES.equals(maxIdAfterSave - maxIdBeforeSave),
                ERROR_ID_GENERATED_ID + (maxIdBeforeSave + ADDED_PHONES) + " / " + maxIdAfterSave);
    }

    @Test
    @DirtiesContext
    public void shouldUpdatePhoneSuccessfully() {
        Long amountBeforeSave = jdbcTemplateTest.queryForObject(COUNT_PHONES, Long.class);

        phone.setId(1001L);
        phone.setModel(MODEL);
        phone.setBrand(BRAND);

        phoneDao.save(phone);

        Long amountAfterSave = jdbcTemplateTest.queryForObject(COUNT_PHONES, Long.class);

        Assert.isTrue(ZERO.equals(amountAfterSave - amountBeforeSave),
                ERROR_IN_PHONE_SAVE + ZERO + " / " + (amountAfterSave - amountBeforeSave));
    }

    @Test
    @DirtiesContext
    public void shouldGetPhoneByKeySuccessfully() {
        optionalPhone = phoneDao.get(EXISTING_KEY);

        Assert.isTrue(optionalPhone.isPresent(), ERROR_IN_EXISTING_KEY);
    }

    @Test
    @DirtiesContext
    public void shouldNotGetPhoneByNotExistingKey() {
        optionalPhone = phoneDao.get(NOT_EXISTING_KEY);
        Assert.isTrue(!optionalPhone.isPresent(), ERROR_IN_EXISTING_KEY);
    }

    @Test
    @DirtiesContext
    public void shouldFindAllPhonesSuccessfully() {
        phones = phoneDao.getAll(OFFSET_SUCCESS, LIMIT_SUCCESS);

        Assert.isTrue(phones.size() == LIMIT_SUCCESS,
                ERROR_IN_FIND_ALL_PHONES + phones.size() + " " + LIMIT_SUCCESS);
    }

    @Test
    @DirtiesContext
    public void shouldNotFindAllPhonesWithOutOfRangeOffset() {
        phones = phoneDao.getAll(OFFSET_OUT_OF_RANGE, LIMIT_SUCCESS);

        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

    @Test
    @DirtiesContext
    public void shouldNotFindAllPhonesWithZeroLimit() {
        phones = phoneDao.getAll(OFFSET_SUCCESS, LIMIT_ZERO);

        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

    @Test
    @DirtiesContext
    public void shouldNotFindAllPhonesWithOutOfRangeOffsetAndLimit() {
        phones = phoneDao.getAll(OFFSET_OUT_OF_RANGE, LIMIT_OUT_OF_RANGE_POSITIVE);

        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

    @Test
    @DirtiesContext
    public void shouldNotFindAllPhonesWithZeroOffsetAndLimit() {
        phones = phoneDao.getAll(OFFSET_ZERO, LIMIT_ZERO);

        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

    @Test
    @DirtiesContext
    public void shouldCountPhonesByEmptyQueryCorrectly() {
        int countPhones = phoneDao.countPhonesByQuery(EMPTY_QUERY);

        Assert.isTrue(countPhones == EXPECTED_SIZE, ERROR_IN_FIND_ALL_PHONES + countPhones +
                "/" + EXPECTED_SIZE);
    }

    @Test
    @DirtiesContext
    public void shouldCountPhonesByQueryCorrectly() {
        int countPhones = phoneDao.countPhonesByQuery(QUERY);

        Assert.isTrue(countPhones == EXPECTED_SIZE_QUERY, ERROR_IN_FIND_ALL_PHONES + countPhones +
                "/" + EXPECTED_SIZE_QUERY);
    }

    @Test
    @DirtiesContext
    public void shouldGetPhonesByQueryCorrectly() {
        List<Phone> phones = phoneDao.getPhonesByQuery(QUERY, BRAND, ASC, ZERO_SIZE, EXPECTED_SIZE);

        Assert.isTrue(phones.size() == EXPECTED_SIZE_QUERY, ERROR_IN_FIND_ALL_PHONES + phones.size() +
                "/" + EXPECTED_SIZE_QUERY);
    }


}
