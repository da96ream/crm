package com.crm.dpeda.query;

import com.crm.dpeda.base.BaseQuery;

/**
 * 营销机会管理的查询类
 */
public class SaleChanceQuery extends BaseQuery{
    //分页参数在BaseQuery中已经定义了（我们只需要继承BaseQuery类即可）

    //营销机会管理 条件参数
    private String customerName; //客户名
    private String createMan; //创建人
    private Integer state; //分配状态。0=未分配；1=已分配


    //客户开发计划 条件
    private String devResult; //开发状态。0=未开发；1=开发中
    private Integer assignMan; //指派人

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

}
