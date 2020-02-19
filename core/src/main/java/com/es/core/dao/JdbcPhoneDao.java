package com.es.core.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private static final String SELECT_PHONE_BY_ID = "select * from phones where id = ?";
    private static final String SELECT_PHONE2COLOR_BY_ID = "select * from phone2color where phoneId = ?";
    private static final String SELECT_PHONES_OFFSET_LIMIT = "select * from phones offset ? limit ?";
    private static final String COLOR_ID = "colorId";
    private static final String PHONES_TABLE_NAME ="phones";
    private static final String GENERATED_KEY_NAME = "id";
    private static final String DUPLICATE_ENTRY_MESSAGE = " Duplicate entry, such kind of item already exists";
    private static final String[] FIELD_NAMES = {"id", "brand", "model", "price", "displaySizeInches", "weightGr",
            "lengthMm", "widthMm", "heightMm", "announced", "deviceType", "os", "displayResolution", "pixelDensity",
            "displayTechnology", "backCameraMegapixels", "frontCameraMegapixels", "ramGb", "internalStorageGb",
            "batteryCapacityMah", "talkTimeHours", "standByTimeHours", "bluetooth", "positioning", "imageUrl",
            "description"};

    private final ColorDao colorDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcPhoneDao(JdbcTemplate jdbcTemplate, ColorDao colorDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.colorDao = colorDao;
    }

    public Optional<Phone> get(final Long key) {
        List<Phone> phones = jdbcTemplate.query(SELECT_PHONE_BY_ID, new Object[]{key},
                new BeanPropertyRowMapper<>(Phone.class));
        if (phones != null && !phones.isEmpty()) {
            setPhoneColors(phones.get(0));
            return Optional.of(phones.get(0));
        }
        return Optional.empty();
    }

    public void save(final Phone phone) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(PHONES_TABLE_NAME)
                .usingGeneratedKeyColumns(GENERATED_KEY_NAME);
        Map<String,Object> parameters = new HashMap<>();
        fillMapForSaving(parameters, phone);
        try {
            simpleJdbcInsert.execute(parameters);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(DUPLICATE_ENTRY_MESSAGE, e);
        }
    }

    public List<Phone> findAll(int offset, int limit) {
        List<Phone> phones = jdbcTemplate.query(SELECT_PHONES_OFFSET_LIMIT, new Object[]{offset, limit},
                new BeanPropertyRowMapper(Phone.class));
        for (Phone phone : phones) {
            setPhoneColors(phone);
        }
        return phones;
    }

    private void setPhoneColors(Phone phone) {
        List<Long> colorIds = jdbcTemplate.query(SELECT_PHONE2COLOR_BY_ID, new Object[]{phone.getId()},
                (resultSet, i) -> resultSet.getLong(COLOR_ID));
        if (colorIds != null) {
            setPhoneColorsByIds(phone, colorIds);
        }
    }

    private void setPhoneColorsByIds(Phone phone, List<Long> colorIds) {
        Set<Color> phoneColors = new HashSet<>();
        List<Color> allColors = colorDao.getAllColors();
        for (Long colorId : colorIds) {
            Optional<Color> colors = allColors.stream().filter(color -> color.getId().equals(colorId)).findFirst();
            colors.ifPresent(phoneColors::add);
        }
        phone.setColors(phoneColors);
    }

    protected void fillMapForSaving(Map<String, Object> map, Phone phone){
        map.put(JdbcPhoneDao.FIELD_NAMES[0], phone.getId());
        map.put(JdbcPhoneDao.FIELD_NAMES[1], phone.getBrand());
        map.put(JdbcPhoneDao.FIELD_NAMES[2], phone.getModel());
        map.put(JdbcPhoneDao.FIELD_NAMES[3], phone.getPrice());
        map.put(JdbcPhoneDao.FIELD_NAMES[4], phone.getDisplaySizeInches());
        map.put(JdbcPhoneDao.FIELD_NAMES[5], phone.getWeightGr());
        map.put(JdbcPhoneDao.FIELD_NAMES[6], phone.getLengthMm());
        map.put(JdbcPhoneDao.FIELD_NAMES[7], phone.getWidthMm());
        map.put(JdbcPhoneDao.FIELD_NAMES[8], phone.getHeightMm());
        map.put(JdbcPhoneDao.FIELD_NAMES[9], phone.getAnnounced());
        map.put(JdbcPhoneDao.FIELD_NAMES[10], phone.getDeviceType());
        map.put(JdbcPhoneDao.FIELD_NAMES[11], phone.getOs());
        map.put(JdbcPhoneDao.FIELD_NAMES[12], phone.getDisplayResolution());
        map.put(JdbcPhoneDao.FIELD_NAMES[13], phone.getPixelDensity());
        map.put(JdbcPhoneDao.FIELD_NAMES[14], phone.getDisplayTechnology());
        map.put(JdbcPhoneDao.FIELD_NAMES[15], phone.getBackCameraMegapixels());
        map.put(JdbcPhoneDao.FIELD_NAMES[16], phone.getFrontCameraMegapixels());
        map.put(JdbcPhoneDao.FIELD_NAMES[17], phone.getRamGb());
        map.put(JdbcPhoneDao.FIELD_NAMES[18], phone.getInternalStorageGb());
        map.put(JdbcPhoneDao.FIELD_NAMES[19], phone.getBatteryCapacityMah());
        map.put(JdbcPhoneDao.FIELD_NAMES[20], phone.getTalkTimeHours());
        map.put(JdbcPhoneDao.FIELD_NAMES[21], phone.getStandByTimeHours());
        map.put(JdbcPhoneDao.FIELD_NAMES[22], phone.getBluetooth());
        map.put(JdbcPhoneDao.FIELD_NAMES[23], phone.getPositioning());
        map.put(JdbcPhoneDao.FIELD_NAMES[24], phone.getImageUrl());
        map.put(JdbcPhoneDao.FIELD_NAMES[25], phone.getDescription());
    }

}
