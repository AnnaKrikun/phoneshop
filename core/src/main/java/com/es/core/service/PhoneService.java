package com.es.core.service;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneService {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> getAll(int offset, int limit);

    int countPhonesByQuery(String searchQuery);

    List<Phone> getPhonesByQuery(String searchQuery, String sort, String order, int offset, int limit);
}
