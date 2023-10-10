package com.crm.dpeda.dao;

import com.crm.dpeda.base.BaseMapper;
import com.crm.dpeda.vo.SaleChance;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /*int deleteByPrimaryKey(Integer id);

    int insert(SaleChance record);

    int insertSelective(SaleChance record);

    SaleChance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SaleChance record);

    int updateByPrimaryKey(SaleChance record);*/

    /**
     * 多条件查询的接口不需要单独定义
     * 由于多个模块涉及到多条件查询操作，所以将对应的多条件查询功能定义在父接口BaseMapper
     */

}