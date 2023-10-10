package com.crm.dpeda.service;

import com.crm.dpeda.base.BaseService;
import com.crm.dpeda.dao.CustomerLossMapper;
import com.crm.dpeda.query.CustomerLossQuery;
import com.crm.dpeda.vo.CustomerLoss;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {

    @Resource
    private CustomerLossMapper customerLossMapper;
    /**
     * 分类条件查询客户流失数据列表
     * @param customerLossQuery
     * @return
     */
    public Map<String,Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        Map<String, Object> map = new HashMap<>();
        //1.开启分页功能
        PageHelper.startPage(customerLossQuery.getPage(), customerLossQuery.getLimit());
        //2.调用dao层的查询方法，返回查询结果
        List<CustomerLoss> customerLosses = customerLossMapper.selectByParams(customerLossQuery);
        //3.使用pageInfo封装查询结果
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLosses);
        //4.设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

}
