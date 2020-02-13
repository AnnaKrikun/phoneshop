package com.es.phoneshop.web.service.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import com.es.phoneshop.web.page.Pagination;
import com.es.phoneshop.web.page.PhonePage;
import com.es.phoneshop.web.service.PageService;
import com.es.phoneshop.web.service.PhonePageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PhonePageServiceImpl implements PhonePageService {
    @Resource
    private PageService pageService;

    @Resource
    private PhoneDao phoneDao;

    @Override
    public PhonePage getPhonePage(String searchQuery, String sort, String order, int pageNumber) {
        int itemsNumber = countPhonesByQuery(searchQuery);
        int normalizedPageNumber = pageService.normalizedPageNumber(itemsNumber, pageNumber);

        int offset = ((normalizedPageNumber - 1) * pageService.AMOUNT_OF_PHONES_ON_PAGE);
        int limit = pageService.AMOUNT_OF_PHONES_ON_PAGE;

        List<Phone> phoneList = getPhoneList(searchQuery, sort, order, offset, limit);

        Pagination pagination = pageService.getPagination(itemsNumber, pageNumber);

        return new PhonePage(phoneList, pagination);
    }

    private List<Phone> getPhoneList(String searchQuery, String sort, String order, int offset, int limit) {
        return phoneDao.getPhonesByQuery(searchQuery, sort, order, offset, limit);
    }

    private int countPhonesByQuery(String searchQuery) {
        return phoneDao.countPhonesByQuery(searchQuery);
    }
}
