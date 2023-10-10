package com.crm.dpeda.dao;

import com.crm.dpeda.base.BaseMapper;
import com.crm.dpeda.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    /*    int deleteByPrimaryKey(Integer id);
    int insert(User record);
    int insertSelective(User record);
    User selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(User record);
    int updateByPrimaryKey(User record);*/

    //通过用户名查询用户记录，返回用户对象
    User queryUserByName(String userName);
    //查询所有的销售人员
    List<Map<String,Object>> queryAllSales();
}