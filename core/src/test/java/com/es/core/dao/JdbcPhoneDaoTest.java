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
    private static final String ERROR_IN_FIND_ALL_PHONES = "Error: Number of found / expected phones = ";
    private static final int OFFSET_OUT_OF_RANGE = 100;
    private static final int AMOUNT_OF_PHONES_IN_DB = 10;
    private static final int LIMIT_OUT_OF_RANGE_POSITIVE = 100;
    private static final int LIMIT_ZERO = 0;
    private static final int OFFSET_ZERO = 0;
    private static final Long ADDED_PHONES = 1L;
    private static final String COUNT_PHONES = "select count (*) from phones";
    private static final String FIND_MAX_ID = "select max(id) from phones";

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
    public void throwIllegalArgumentExceptionWhenSaveNullBrand() {
        phone.setBrand(null);
        phoneDao.save(phone);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void throwIllegalArgumentExceptionWhenSaveNullModel() {
        phone.setModel(null);
        phoneDao.save(phone);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void throwIllegalArgumentExceptionWhenSave2PhonesWithEqualBrands() {
        phone.setBrand(BRAND);
        phone1.setBrand(BRAND);

        phoneDao.save(phone);
        phoneDao.save(phone1);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void throwIllegalArgumentExceptionWhenSave2PhonesWithEqualModels() {
        phone.setModel(MODEL);
        phone1.setModel(MODEL);

        phoneDao.save(phone);
        phoneDao.save(phone1);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void throwIllegalArgumentExceptionWhenSave2PhonesWithEqualBrandsAndModels() {
        phone.setBrand(BRAND);
        phone1.setBrand(BRAND);
        phone.setModel(MODEL);
        phone1.setModel(MODEL);

        phoneDao.save(phone);
        phoneDao.save(phone1);
    }

    @Test
    @DirtiesContext
    public void savePhoneSuccessfully() {
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
    public void getPhoneByKeySuccessfully() {
        optionalPhone = phoneDao.get(EXISTING_KEY);
        Assert.isTrue(optionalPhone.isPresent(), ERROR_IN_EXISTING_KEY);
    }

    @Test
    @DirtiesContext
    public void getPhoneByNotExistingKey() {
        optionalPhone = phoneDao.get(NOT_EXISTING_KEY);
        Assert.isTrue(!optionalPhone.isPresent(), ERROR_IN_EXISTING_KEY);
    }

    @Test
    @DirtiesContext
    public void findAllPhonesSuccessfully() {
        phones = phoneDao.findAll(OFFSET_SUCCESS, LIMIT_SUCCESS);
        Assert.isTrue(phones.size() == LIMIT_SUCCESS,
                ERROR_IN_FIND_ALL_PHONES + phones.size() + " " + LIMIT_SUCCESS);
    }

    @Test
    @DirtiesContext
    public void findAllPhonesWithOutOfRangeOffset() {
        phones = phoneDao.findAll(OFFSET_OUT_OF_RANGE, LIMIT_SUCCESS);
        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

    @Test
    @DirtiesContext
    public void findAllPhonesWithOutOfRangePositiveLimit() {
        phones = phoneDao.findAll(OFFSET_SUCCESS, LIMIT_OUT_OF_RANGE_POSITIVE);
        Assert.isTrue(phones.size() == AMOUNT_OF_PHONES_IN_DB - OFFSET_SUCCESS,
                ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" +
                        (AMOUNT_OF_PHONES_IN_DB - OFFSET_SUCCESS));
    }


    @Test
    @DirtiesContext
    public void findAllPhonesWithZeroLimit() {
        phones = phoneDao.findAll(OFFSET_SUCCESS, LIMIT_ZERO);
        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

    @Test
    @DirtiesContext
    public void findAllPhonesWithOutOfRangeOffsetAndLimit() {
        phones = phoneDao.findAll(OFFSET_OUT_OF_RANGE, LIMIT_OUT_OF_RANGE_POSITIVE);
        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

    @Test
    @DirtiesContext
    public void findAllPhonesWithZeroOffsetAndLimit() {
        phones = phoneDao.findAll(OFFSET_ZERO, LIMIT_ZERO);
        Assert.isTrue(phones.isEmpty(), ERROR_IN_FIND_ALL_PHONES + phones.size() + "/" + ZERO_SIZE);
    }

}
