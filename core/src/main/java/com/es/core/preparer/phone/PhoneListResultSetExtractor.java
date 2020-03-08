package com.es.core.preparer.phone;

import com.es.core.model.phone.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.es.core.constants.FieldConstants.PHONE_ID;

@Component
public class PhoneListResultSetExtractor extends PhoneResultExtractor implements ResultSetExtractor<List<Phone>> {

    @Override
    public List<Phone> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Phone> phoneMap = new HashMap<>();
        List<Phone> phoneList = new ArrayList<>();
        while (resultSet.next()) {
            Long phoneId = resultSet.getLong(PHONE_ID);
            Phone changePhone = phoneMap.get(phoneId);
            if (changePhone == null) {
                changePhone = readPropertiesToPhone(resultSet);
                phoneMap.put(phoneId, changePhone);
                phoneList.add(changePhone);
            }
            addColor(changePhone, resultSet);
        }
        return phoneList;
    }
}


