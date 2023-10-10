package com.crm.dpeda.controller;

import com.crm.dpeda.base.BaseController;
import com.crm.dpeda.base.ResultInfo;
import com.crm.dpeda.enums.StateStatus;
import com.crm.dpeda.query.CusDevPlanQuery;
import com.crm.dpeda.query.SaleChanceQuery;
import com.crm.dpeda.service.CusDevPlanService;
import com.crm.dpeda.service.SaleChanceService;
import com.crm.dpeda.utils.LoginUserUtil;
import com.crm.dpeda.vo.CusDevPlan;
import com.crm.dpeda.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("/index")
    public String index(){
        return "cusDevPlan/index";
    }

    /**
     * 打开计划项开发或详情页面
     * @param id
     * @return
     */
    @RequestMapping("/toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        //通过id 查询营销功能的ID对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //设置作用域
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }


    /**
     * 分页条件查询营销机会数据列表 (分页多条件查询)
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {

        //查询营销机会数据
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }


    /**
     * 添加计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("添加成功");
    }

    /**
     * 打开添加或修改页面
//     * @param request
//     * @param sId
//     * @param id
     * @return
     */
    @RequestMapping("/toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(HttpServletRequest request,Integer sId,Integer id) {
        //HttpServletRequest request,Integer sId,Integer id
        //将营销机会id存储到request域中
        request.setAttribute("sId",sId);
        //通过id查询计划项记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        //存储计划项数据到request域中
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }

    /**
     * 修改计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("更新成功");
    }

    /**
     * 删除操作
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id) {
        cusDevPlanService.deleteCusDevPlan(id);
        return success("删除成功");
    }
}
