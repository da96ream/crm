package com.crm.dpeda.dao;

import com.crm.dpeda.base.BaseMapper;
import com.crm.dpeda.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {
    /*int deleteByPrimaryKey(Integer id);

    int insert(CustomerOrder record);

    int insertSelective(CustomerOrder record);

    CustomerOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerOrder record);

    int updateByPrimaryKey(CustomerOrder record);*/

    Map<String,Object> queryOrderById(Integer orderId);
}