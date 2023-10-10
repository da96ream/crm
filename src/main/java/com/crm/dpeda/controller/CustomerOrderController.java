package com.crm.dpeda.controller;

import com.crm.dpeda.base.BaseController;
import com.crm.dpeda.query.CustomerOrderQuery;
import com.crm.dpeda.service.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class CustomerOrderController extends BaseController {
    @Resource
    private CustomerOrderService customerOrderService;

    /**
     * 分页条件查询客户订单数据
     * @param customerOrderQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery){
        return customerOrderService.queryCustomerOrderByParams(customerOrderQuery);
    }

    /**
     * 打开订单详情页面
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping("/toOrderDetailPage")
    public String toOrderDetailPage(Integer orderId, Model model){
        Map<String,Object> map = customerOrderService.queryOrderById(orderId);
        model.addAttribute("order",map);
        return "customer/customer_order_detail";
    }
}
