package com.crm.dpeda.query;

import com.crm.dpeda.base.BaseQuery;

public class OrderDetailsQuery extends BaseQuery {
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
