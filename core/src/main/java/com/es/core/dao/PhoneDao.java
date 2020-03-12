package com.es.core.dao;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> getAll(int offset, int limit);

    int countPhonesByQuery(String searchQuery);

    List<Phone> getPhonesByQuery(String searchQuery, String sort, String order, int offset, int limit);
}
