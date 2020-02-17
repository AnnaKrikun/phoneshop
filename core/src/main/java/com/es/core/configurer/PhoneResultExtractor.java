package com.es.core.configurer;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class PhoneResultExtractor {

    protected Phone readPropertiesToPhone(ResultSet resultSet) throws SQLException {
        Phone phone = new Phone();

        phone.setId(resultSet.getLong(FieldConstants.PHONE_FIELD_NAMES[25]));
        phone.setBrand(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[0]));
        phone.setModel(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[1]));
        phone.setPrice(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[2]));
        phone.setDisplaySizeInches(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[3]));
        phone.setWeightGr(resultSet.getInt(FieldConstants.PHONE_FIELD_NAMES[4]));
        phone.setLengthMm(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[5]));
        phone.setWidthMm(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[6]));
        phone.setHeightMm(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[7]));
        phone.setAnnounced(resultSet.getDate(FieldConstants.PHONE_FIELD_NAMES[8]));
        phone.setDeviceType(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[9]));
        phone.setOs(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[10]));
        phone.setDisplayResolution(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[11]));
        phone.setPixelDensity(resultSet.getInt(FieldConstants.PHONE_FIELD_NAMES[12]));
        phone.setDisplayTechnology(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[13]));
        phone.setBackCameraMegapixels(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[14]));
        phone.setFrontCameraMegapixels(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[15]));
        phone.setRamGb(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[16]));
        phone.setInternalStorageGb(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[17]));
        phone.setBatteryCapacityMah(resultSet.getInt(FieldConstants.PHONE_FIELD_NAMES[18]));
        phone.setTalkTimeHours(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[19]));
        phone.setStandByTimeHours(resultSet.getBigDecimal(FieldConstants.PHONE_FIELD_NAMES[20]));
        phone.setBluetooth(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[21]));
        phone.setPositioning(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[22]));
        phone.setImageUrl(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[23]));
        phone.setDescription(resultSet.getString(FieldConstants.PHONE_FIELD_NAMES[24]));
        phone.setColors(new HashSet<>());
        return phone;
    }

    protected void addColor(Phone phone, ResultSet resultSet) throws SQLException {
        Long colorId = resultSet.getLong(FieldConstants.COLOR_FIELD_NAMES[0]);
        if (colorId > 0) {
            phone.getColors().add(new Color(colorId, resultSet.getString(FieldConstants.COLOR_FIELD_NAMES[1])));
        }
    }
}
