package com.crm.dpeda.service;

import com.crm.dpeda.base.BaseService;
import com.crm.dpeda.dao.CustomerMapper;
import com.crm.dpeda.query.CusDevPlanQuery;
import com.crm.dpeda.query.CustomerQuery;
import com.crm.dpeda.utils.AssertUtil;
import com.crm.dpeda.utils.PhoneUtil;
import com.crm.dpeda.vo.CusDevPlan;
import com.crm.dpeda.vo.Customer;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService extends BaseService<Customer,Integer> {

    @Resource
    private CustomerMapper customerMapper;

    /*** 多条件分页查询营销机会数据
     * 返回的数据格式必须满足layui中的数据表格要求的格式
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        //List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        //另一种写法：注销2和3的信息
        //PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChances);
        //3、使用pageInfo封装查询结果,得到分页好的列表
        PageInfo<Customer> pageInfo = new PageInfo<> (customerMapper.selectByParams(customerQuery));
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
        //参数添加
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //判断客户名称是否具有唯一性
        Customer temp = customerMapper.queryCustomerByName(customer.getName());
        //判断客户是否存在
        AssertUtil.isTrue(temp!=null,"客户名称不能重复！");
        //设置参数的默认值
        customer.setIsValid(1);
        //设置客户流失状态
        customer.setState(0);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        //客户编号
        String khno = "KH" +System.currentTimeMillis();
        customer.setKhno(khno);
        //执行添加操作
        AssertUtil.isTrue(customerMapper.insertSelective(customer)!=1,"添加失败！！！");
    }

    /**
     * 1.参数校验
     *  客户名称 name
     *      非空，名称项（注意名称不能重复）
     *  法人代表
     *      非空
     *  手机号码
     *      非空，格式正确
     */
    private void checkCustomerParams(String name,String fr,String phone){
        AssertUtil.isTrue(StringUtils.isBlank(name),"客户名称不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人代表不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确！");
    }

}
