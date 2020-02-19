package com.es.phoneshop.web.page;

import com.es.core.model.phone.Phone;

import java.util.List;

public class ProductListPage {
    private List<Phone> phoneList;
    private Pagination pagination;

    public ProductListPage() {
    }

    public ProductListPage(List<Phone> phoneList, Pagination pagination) {
        this.phoneList = phoneList;
        this.pagination = pagination;
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
