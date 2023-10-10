package com.crm.dpeda.query;

import com.crm.dpeda.base.BaseQuery;

public class CustomerOrderQuery extends BaseQuery {
    private Integer cusId;//客户id
    public Integer getCusId(){return cusId;};
    public void setCusId(Integer cusId){this.cusId = cusId;};
}
