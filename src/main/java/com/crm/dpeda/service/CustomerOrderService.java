package com.crm.dpeda.service;

import com.crm.dpeda.base.BaseService;
import com.crm.dpeda.dao.CustomerOrderMapper;
import com.crm.dpeda.query.CustomerOrderQuery;
import com.crm.dpeda.vo.CustomerOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {
    @Resource
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 分页条件查询客户数据订单列表
     * @param customerOrderQuery
     * @return
     */
    public Map<String,Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery){
        Map<String,Object> map = new HashMap<>();
        //1.开启分页功能
        PageHelper.startPage(customerOrderQuery.getPage(),customerOrderQuery.getLimit());
        //2.调用dao层的查询方法，返回查询结果
        List<CustomerOrder> customerOrders = customerOrderMapper.selectByParams(customerOrderQuery);
        //3.使用pageInfo封装查询结果
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrders);
        //4.设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 通过订单Id查询对应的订单记录
     * @param orderId
     * @return
     */
    public Map<String,Object> queryOrderById(Integer orderId){
        return customerOrderMapper.queryOrderById(orderId);
    }
}
