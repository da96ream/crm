package com.crm.dpeda.query;

import com.crm.dpeda.base.BaseQuery;

public class CustomerQuery extends BaseQuery {

    private String customerName; //客户名称
    private String customerNo; //客户编号
    private String level; //客户级别

    private String time; //订单时间
    private Integer type; //金额区间 1（0-1000） 2（1000-3000） 3（3000-5000） 4（5000+）

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() { return customerNo; }

    public void setCustomerNo(String customerNp) {
        this.customerNo = customerNp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
