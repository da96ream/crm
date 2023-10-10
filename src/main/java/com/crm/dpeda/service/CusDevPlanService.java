package com.crm.dpeda.service;

import com.crm.dpeda.base.BaseService;
import com.crm.dpeda.dao.CusDevPlanMapper;
import com.crm.dpeda.dao.SaleChanceMapper;
import com.crm.dpeda.query.CusDevPlanQuery;
import com.crm.dpeda.query.SaleChanceQuery;
import com.crm.dpeda.utils.AssertUtil;
import com.crm.dpeda.vo.CusDevPlan;
import com.crm.dpeda.vo.SaleChance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /*** 多条件分页查询营销机会数据
     * 返回的数据格式必须满足layui中的数据表格要求的格式
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        //List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        //另一种写法：注销2和3的信息
        //PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChances);
        //3、使用pageInfo封装查询结果,得到分页好的列表
        PageInfo<CusDevPlan> pageInfo = new PageInfo<> (cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }


    /**
     * 添加客户开发计划
     * 1、参数校验
     *  营销机会ID 非空，数据存在
     *  计划项内容 非空
     *  计划时间 非空
     * 2、设置参数的默认值
     *  是否有效 默认有效
     *  创建时间 系统当前时间
     *  修改时间 系统当前时间
     * 3、执行添加操作，判断受影响的行数
     * @param cusDevPlan
     * */
    @Transactional
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        //1、参数校验
        checkCusDevPlanParams(cusDevPlan);
        //2、设置相关字段的默认值
        //是否有效 默认有效
        cusDevPlan.setIsValid(1);
        //创建时间 系统当前时间
        cusDevPlan.setCreateDate(new Date());
        //修改时间 系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        //3、执行添加操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)!=1,"添加失 败");
    }

    /**
     * 参数校验
     * 营销机会ID 非空，数据存在
     * 计划项内容 非空
     * 计划时间 非空
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //营销机会id 非空
        Integer saleChanceId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(saleChanceId == null && saleChanceMapper.selectByPrimaryKey(saleChanceId) == null,"数 据异常，请重试");
        //计划项内容 非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划项内 容不能为空");
        //计划时间 非空
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(), "计划时间不能为空");
    }

    /**
     * 更新操作
     * 1.数据校验
     * 计划项 id 非空/数据存在
     * saleChanceId 营销机会的id 非空 / 数据存在
     * 计划内容 非空 * 计划时间 非空
     * 2.设置默认值 * update_date
     * 3.执行添加操作，判断
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        //1、参数校验
        AssertUtil.isTrue(null == cusDevPlan.getId() || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"数据异常，请重试");
        //与添加数据共用
        checkCusDevPlanParams(cusDevPlan);
        //2、设置相关字段的默认值
        cusDevPlan.setUpdateDate(new Date());
        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"更新失败");
    }

    /**
     * 删除操作
     * @param id
     */
    public void deleteCusDevPlan(Integer id) {
        //1、判断id是否为空，且数据存在
        AssertUtil.isTrue(null == id,"待删除记录不存在");
        //2、通过id查询计划项对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        //设置 is_valid = 0
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        //执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"删除失败");
    }
}
