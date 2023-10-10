package com.crm.dpeda.controller;

import com.crm.dpeda.base.BaseController;
import com.crm.dpeda.base.ResultInfo;
import com.crm.dpeda.enums.StateStatus;
import com.crm.dpeda.query.SaleChanceQuery;
import com.crm.dpeda.service.SaleChanceService;
import com.crm.dpeda.utils.CookieUtil;
import com.crm.dpeda.utils.LoginUserUtil;
import com.crm.dpeda.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController{
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 分页条件查询营销机会数据列表 (分页多条件查询)
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request) {
        if(flag != null && flag == 1) {
            //查询客户开发计划
            //1、设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            //2、设置指派人（当前登录用户的id）
            //设置指派人(当前登录用户的ID) 从cookie中获取当前登录用户的ID
            int userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }

        //查询营销机会数据
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
    * 打开营销机会管理页面
    * @return
    */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }


    //营销添加数据
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        //从cookie中获取当前登录的userName
        /**
         * 注意：这里的cookie是根据request请求获取浏览器中的信息
         * 获取来进行添加是当前哪位人添加信息说明
         */
        String userName = CookieUtil.getCookieValue(request, "userName");
        //设置用户名创建人到营销模块saleChance对象中
        saleChance.setCreateMan(userName);
        //调用service层的添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("添加成功");
    }

    @RequestMapping("toSaleChancePage")
    public String toAddUpdatePage(Integer saleChanceId,HttpServletRequest request){
        //判断saleChanceId是否为空
        if(saleChanceId != null) {
            //通过id查询营销用户
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            //将数据设置到request域中
            request.setAttribute("saleChance",saleChance);
        }
        //如果是修改操作那么需要将修改的数据映射在页面中
        return "saleChance/add_update";
    }

    /**
     * @param saleChance
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        //调用service层的添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("更新成功");
    }

    /**
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteSalChance(Integer[] ids) {
        saleChanceService.deleteSaleChance(ids);
        return success("删除成功");
    }

    /**
     * 客户开发————修改营销的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @PostMapping("/updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult) {
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功");
    }
}
