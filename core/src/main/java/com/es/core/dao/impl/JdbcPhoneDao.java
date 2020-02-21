package com.es.core.dao.impl;

import com.es.core.configurer.phone.PhoneListResultSetExtractor;
import com.es.core.configurer.phone.PhoneParametersPreparer;
import com.es.core.dao.ColorDao;
import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private final static String SELECT_PHONE_BY_ID_QUERY = "select phones.id AS phoneId, brand, model, " +
            "price, displaySizeInches, weightGr, lengthMm, widthMm, " +
            "heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, " +
            "frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, " +
            "description, colors.id AS colorId, colors.code AS colorCode from phones " +
            "left join phone2color on phones.id = phone2color.phoneId " +
            "left join colors on colors.id = phone2color.colorId " +
            "where phones.id = ?";

    private final static String SELECT_ALL_PHONES_QUERY = "select limited.id AS phoneId, brand, model, " +
            "price, displaySizeInches, weightGr, lengthMm, widthMm, " +
            "heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, " +
            "frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, " +
            "description, colors.id AS colorId, colors.code AS colorCode from " +
            "(select * from phones where id in (select phoneId from stocks where stock > 0) offset ? limit ? ) as limited " +
            "left join phone2color on limited.id = phone2color.phoneId " +
            "left join colors on colors.id = phone2color.colorId ";

    private final static String UPDATE_PHONE = "update phones set brand = ? ,model = ? ," +
            "price = ? ,displaySizeInches = ? ,weightGr = ? ,lengthMm = ? ,widthMm = ? ," +
            "heightMm = ? ,announced = ? ,deviceType = ? ,os = ? ,displayResolution = ? ," +
            "pixelDensity = ? ,displayTechnology = ? ,backCameraMegapixels = ? ," +
            "frontCameraMegapixels = ? ,ramGb = ? ,internalStorageGb = ? ,batteryCapacityMah = ? ," +
            "talkTimeHours = ? ,standByTimeHours = ? ,bluetooth = ? ,positioning = ? ,imageUrl = ? ," +
            "description = ? where id = ? ";

    private final static String QUERY_OF_PHONE_COUNT_BY_QUERY = "select COUNT(1) from phones where price > 0 and " +
            "(lower(brand) like ? or lower(model) like ?)";

    private static final String DUPLICATE_ENTRY_MESSAGE = " Duplicate entry, such kind of item already exists";
    private static final String PART_1_SELECT_PHONES_BY_SEARCH_QUERY_ORDERED = "select limitedPhones.id AS phoneId, " +
            "brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, " +
            "heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, " +
            "frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, " +
            "description, colors.id AS colorId, colors.code AS colorCode from " +
            "(select * from phones where price > 0 and (lower(brand) like ? or lower(model) like ?) order by ";
    private static final String PART_2_SELECT_PHONES_BY_SEARCH_QUERY_ORDERED_OFFSET = " offset ? limit ? ) as limitedPhones " +
            "left join phone2color on limitedPhones.id = phone2color.phoneId " +
            "left join colors on colors.id = phone2color.colorId";
    private static final String SEARCH_QUERY_FORMAT = "%%%s%%";
    private static final CharSequence DELIMITER = " ";

    private ColorDao colorDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private PhoneParametersPreparer parametersPreparer;
    private PhoneListResultSetExtractor phoneListResultSetExtractor;

    @Autowired
    public JdbcPhoneDao(ColorDao colorDao, JdbcTemplate jdbcTemplate, SimpleJdbcInsert simpleJdbcInsert,
                        PhoneParametersPreparer phoneParametersPreparer,
                        PhoneListResultSetExtractor phoneListResultSetExtractor) {
        this.colorDao = colorDao;
        this.jdbcTemplate = jdbcTemplate;
        this.parametersPreparer = phoneParametersPreparer;
        this.phoneListResultSetExtractor = phoneListResultSetExtractor;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }


    public Optional<Phone> get(Long key) {
        List<Phone> phones = jdbcTemplate.query(SELECT_PHONE_BY_ID_QUERY, phoneListResultSetExtractor, key);
        if (CollectionUtils.isNotEmpty(phones)) {
            return Optional.of(phones.get(0));
        }
        return Optional.empty();
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(SELECT_ALL_PHONES_QUERY, phoneListResultSetExtractor, offset, limit);
    }

    @Override
    public int countPhonesByQuery(String searchQuery) {
        String preparedSearchQuery = String.format(SEARCH_QUERY_FORMAT, searchQuery.toLowerCase());
        return jdbcTemplate.queryForObject(QUERY_OF_PHONE_COUNT_BY_QUERY, Integer.class, preparedSearchQuery,
                preparedSearchQuery);
    }

    @Override
    public List<Phone> getPhonesByQuery(String searchQuery, String sort, String order, int offset, int limit) {
        String preparedSearchQuery = String.format(SEARCH_QUERY_FORMAT, searchQuery.toLowerCase());
        String preparedOrder = String.join(DELIMITER, sort, order);
        return jdbcTemplate.query(PART_1_SELECT_PHONES_BY_SEARCH_QUERY_ORDERED + preparedOrder
                        + PART_2_SELECT_PHONES_BY_SEARCH_QUERY_ORDERED_OFFSET, phoneListResultSetExtractor,
                preparedSearchQuery, preparedSearchQuery, offset, limit);
    }

    public void save(Phone phone) {
        if (phone.getId() == null) {
            savePhone(phone);
        } else {
            updatePhone(phone);
        }
    }

    private void updatePhone(Phone phone) {
        jdbcTemplate.update(UPDATE_PHONE, parametersPreparer.getPreparedParameters(phone));
        colorDao.deleteColorsFromPone2Color(phone);
        colorDao.saveColor(phone);
    }

    private void savePhone(Phone phone) {
        Map<String, Object> parameters = parametersPreparer.fillMapForSaving(phone);
        try {
            Long phoneId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
            phone.setId(phoneId);
            colorDao.saveColor(phone);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(DUPLICATE_ENTRY_MESSAGE, e);
        }
    }
}
