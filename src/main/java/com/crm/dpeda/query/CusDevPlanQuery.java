package com.crm.dpeda.query;

import com.crm.dpeda.base.BaseQuery;

public class CusDevPlanQuery extends BaseQuery {
    //查询营销功能的id
    private Integer saleChanceId;

    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}
