package com.crm.dpeda.dao;

import com.crm.dpeda.base.BaseMapper;
import com.crm.dpeda.vo.Customer;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {
    /*int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);*/

    Customer queryCustomerByName(String name);
}