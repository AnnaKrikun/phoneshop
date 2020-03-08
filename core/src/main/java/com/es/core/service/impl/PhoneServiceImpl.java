package com.es.core.service.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import com.es.core.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {
    private PhoneDao phoneDao;

    @Autowired
    public PhoneServiceImpl(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public Optional<Phone> get(Long key) {
        return phoneDao.get(key);
    }

    @Override
    public void save(Phone phone) {
        phoneDao.save(phone);
    }

    @Override
    public List<Phone> getAll(int offset, int limit) {
        return phoneDao.getAll(offset, limit);
    }

    @Override
    public int countPhonesByQuery(String searchQuery) {
        return phoneDao.countPhonesByQuery(searchQuery);
    }

    @Override
    public List<Phone> getPhonesByQuery(String searchQuery, String sort, String order, int offset, int limit) {
        return phoneDao.getPhonesByQuery(searchQuery, sort, order, offset, limit);
    }
}
