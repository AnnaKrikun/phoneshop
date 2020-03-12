package com.es.core.configurer.phone;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import static com.es.core.constants.FieldConstants.*;

public class PhoneResultExtractor {

    protected Phone readPropertiesToPhone(ResultSet resultSet) throws SQLException {
        Phone phone = new Phone();

        phone.setId(resultSet.getLong(PHONE_ID));
        phone.setBrand(resultSet.getString(BRAND));
        phone.setModel(resultSet.getString(MODEL));
        phone.setPrice(resultSet.getBigDecimal(PRICE));
        phone.setDisplaySizeInches(resultSet.getBigDecimal(DISPLAY_SIZE_INCHES));
        phone.setWeightGr(resultSet.getInt(WEIGHT_GR));
        phone.setLengthMm(resultSet.getBigDecimal(LENGTH_MM));
        phone.setWidthMm(resultSet.getBigDecimal(WIDTH_MM));
        phone.setHeightMm(resultSet.getBigDecimal(HEIGHT_MM));
        phone.setAnnounced(resultSet.getDate(ANNOUNCED));
        phone.setDeviceType(resultSet.getString(DEVICE_TYPE));
        phone.setOs(resultSet.getString(OS));
        phone.setDisplayResolution(resultSet.getString(DISPLAY_RESOLUTION));
        phone.setPixelDensity(resultSet.getInt(PIXEL_DENSITY));
        phone.setDisplayTechnology(resultSet.getString(DISPLAY_TECHNOLOGY));
        phone.setBackCameraMegapixels(resultSet.getBigDecimal(BACK_CAMERA_MEGAPIXELS));
        phone.setFrontCameraMegapixels(resultSet.getBigDecimal(FRONT_CAMERA_MEGAPIXELS));
        phone.setRamGb(resultSet.getBigDecimal(RAM_GB));
        phone.setInternalStorageGb(resultSet.getBigDecimal(INTERNAL_STORAGE_GB));
        phone.setBatteryCapacityMah(resultSet.getInt(BATTERY_CAPACITY_MAH));
        phone.setTalkTimeHours(resultSet.getBigDecimal(TALK_TIME_HOURS));
        phone.setStandByTimeHours(resultSet.getBigDecimal(STAND_BY_TIME_HOURS));
        phone.setBluetooth(resultSet.getString(BLUETOOTH));
        phone.setPositioning(resultSet.getString(POSITIONING));
        phone.setImageUrl(resultSet.getString(IMAGE_URL));
        phone.setDescription(resultSet.getString(DESCRIPTION));
        phone.setColors(new HashSet<>());
        return phone;
    }

    protected void addColor(Phone phone, ResultSet resultSet) throws SQLException {
        Long colorId = resultSet.getLong(COLOR_ID);
        if (colorId > 0) {
            phone.getColors().add(new Color(colorId, resultSet.getString(COLOR_CODE)));
        }
    }
}
