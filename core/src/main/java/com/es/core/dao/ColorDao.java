package com.es.core.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;

import java.util.List;

public interface ColorDao {
    List<Color> getAllColors();
    void saveColor(Phone phone);
    void deleteColorsFromPone2Color(Phone phone);
}
