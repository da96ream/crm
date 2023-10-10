package com.crm.dpeda.dao;

import com.crm.dpeda.base.BaseMapper;
import com.crm.dpeda.vo.CusDevPlan;
import com.crm.dpeda.vo.User;

import java.util.List;
import java.util.Map;

public interface CusDevPlanMapper extends BaseMapper<CusDevPlan,Integer> {
    /*int deleteByPrimaryKey(Integer id);

    int insert(CusDevPlan record);

    int insertSelective(CusDevPlan record);

    CusDevPlan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CusDevPlan record);

    int updateByPrimaryKey(CusDevPlan record);*/

    User queryUserByName(String userName);
    //查询所有的销售人员
    List<Map<String,Object>> queryAllSales();

}