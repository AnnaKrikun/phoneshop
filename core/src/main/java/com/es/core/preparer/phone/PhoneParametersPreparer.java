package com.es.core.preparer.phone;

import com.es.core.model.phone.Phone;
import com.es.core.preparer.ParametersPreparer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.es.core.constants.FieldConstants.*;

@Component
public class PhoneParametersPreparer implements ParametersPreparer<Phone> {

    @Override
    public Map<String, Object> fillMapForSaving(Phone phone) {
        Map<String, Object> map = new HashMap<>();
        map.put(BRAND, phone.getBrand());
        map.put(MODEL, phone.getModel());
        map.put(PRICE, phone.getPrice());
        map.put(DISPLAY_SIZE_INCHES, phone.getDisplaySizeInches());
        map.put(WEIGHT_GR, phone.getWeightGr());
        map.put(LENGTH_MM, phone.getLengthMm());
        map.put(WIDTH_MM, phone.getWidthMm());
        map.put(HEIGHT_MM, phone.getHeightMm());
        map.put(ANNOUNCED, phone.getAnnounced());
        map.put(DEVICE_TYPE, phone.getDeviceType());
        map.put(OS, phone.getOs());
        map.put(DISPLAY_RESOLUTION, phone.getDisplayResolution());
        map.put(PIXEL_DENSITY, phone.getPixelDensity());
        map.put(DISPLAY_TECHNOLOGY, phone.getDisplayTechnology());
        map.put(BACK_CAMERA_MEGAPIXELS, phone.getBackCameraMegapixels());
        map.put(FRONT_CAMERA_MEGAPIXELS, phone.getFrontCameraMegapixels());
        map.put(RAM_GB, phone.getRamGb());
        map.put(INTERNAL_STORAGE_GB, phone.getInternalStorageGb());
        map.put(BATTERY_CAPACITY_MAH, phone.getBatteryCapacityMah());
        map.put(TALK_TIME_HOURS, phone.getTalkTimeHours());
        map.put(STAND_BY_TIME_HOURS, phone.getStandByTimeHours());
        map.put(BLUETOOTH, phone.getBluetooth());
        map.put(POSITIONING, phone.getPositioning());
        map.put(IMAGE_URL, phone.getImageUrl());
        map.put(DESCRIPTION, phone.getDescription());
        map.put(PHONE_ID, phone.getId());
        return map;
    }

    @Override
    public Object[] getPreparedParameters(Phone phone) {
        Object[] preparedParameters = new Object[FIELD_AMOUNT];
        preparedParameters[0] = phone.getBrand();
        preparedParameters[1] = phone.getModel();
        preparedParameters[2] = phone.getPrice();
        preparedParameters[3] = phone.getDisplaySizeInches();
        preparedParameters[4] = phone.getWeightGr();
        preparedParameters[5] = phone.getLengthMm();
        preparedParameters[6] = phone.getWidthMm();
        preparedParameters[7] = phone.getHeightMm();
        preparedParameters[8] = phone.getAnnounced();
        preparedParameters[9] = phone.getDeviceType();
        preparedParameters[10] = phone.getOs();
        preparedParameters[11] = phone.getDisplayResolution();
        preparedParameters[12] = phone.getPixelDensity();
        preparedParameters[13] = phone.getDisplayTechnology();
        preparedParameters[14] = phone.getBackCameraMegapixels();
        preparedParameters[15] = phone.getFrontCameraMegapixels();
        preparedParameters[16] = phone.getRamGb();
        preparedParameters[17] = phone.getInternalStorageGb();
        preparedParameters[18] = phone.getBatteryCapacityMah();
        preparedParameters[19] = phone.getTalkTimeHours();
        preparedParameters[20] = phone.getStandByTimeHours();
        preparedParameters[21] = phone.getBluetooth();
        preparedParameters[22] = phone.getPositioning();
        preparedParameters[23] = phone.getImageUrl();
        preparedParameters[24] = phone.getDescription();
        preparedParameters[25] = phone.getId();
        return preparedParameters;
    }
}
