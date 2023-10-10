package com.crm.dpeda.service;

import com.crm.dpeda.base.BaseService;
import com.crm.dpeda.dao.OrderDetailsMapper;
import com.crm.dpeda.query.OrderDetailsQuery;
import com.crm.dpeda.vo.OrderDetails;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {

    @Resource
    private OrderDetailsMapper orderDetailsMapper;
    /**
     * 分类条件查询订单详情列表
     * @param orderDetailsQuery
     * @return
     */
    public Map<String,Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery){
        Map<String,Object> map = new HashMap<>();
        //1.开启分页功能
        PageHelper.startPage(orderDetailsQuery.getPage(),orderDetailsQuery.getLimit());
        //2.调用dao层的查询方法，返回查询结果
        List<OrderDetails> orderDetailsList = orderDetailsMapper.selectByParams(orderDetailsQuery);
        //3.使用pageInfo封装查询结果
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsList);
        //4.设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;

    }
}
