package com.crm.dpeda.controller;

import com.crm.dpeda.base.BaseController;
import com.crm.dpeda.base.ResultInfo;
import com.crm.dpeda.query.CustomerQuery;
import com.crm.dpeda.service.CustomerService;
import com.crm.dpeda.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    /**
     * 打开客户信息管理
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "customer/customer";
    }

    /**
     * 多条件查询
     * @param customerQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("添加成功！！！");
    }

    /**
     * 打开添加或者修改页面
     * @return
     */
    @RequestMapping("/toAddOrUpdateCustomerPage")
    public String toAddOrUpdateCustomerPage(){
        return "customer/add_update";
    }

    @RequestMapping("/toCustomerOrderPage")
    public String toCustomerOrderPage(Integer customerId, Model model){
        Customer customer = customerService.selectByPrimaryKey(customerId);
        model.addAttribute("customer",customer);
        return "customer/customer_order";
    }
}
