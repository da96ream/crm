package com.crm.dpeda.controller;

import com.crm.dpeda.base.BaseController;
import com.crm.dpeda.query.CustomerLossQuery;
import com.crm.dpeda.service.CustomerLossService;
import com.crm.dpeda.vo.CustomerLoss;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/customer_loss")
public class CustomerLossController extends BaseController {
    @Resource
    private CustomerLossService customerLossService;

    /**
     * 打开客户流失页面
     * @return
     */
    @RequestMapping("/index")
    public String index(){ return "customerLoss/customer_loss"; }

    /**
     * 多条件查询
     * @param customerLossQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery){
        return customerLossService.queryCustomerLossByParams(customerLossQuery);
    }

    @RequestMapping("/toCustomerLossPage")
    public String toCustomerLossPage(Integer lossId, Model model){
        //通过流失客户的id 查询对应的流失客户记录
        CustomerLoss customerLoss = customerLossService.selectByPrimaryKey(lossId);
        model.addAttribute("customerLoss",customerLoss);
        return "customerLoss/customer_rep";
    }
}
