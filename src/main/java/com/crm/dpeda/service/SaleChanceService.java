package com.crm.dpeda.service;

import com.crm.dpeda.base.BaseService;
import com.crm.dpeda.dao.SaleChanceMapper;
import com.crm.dpeda.enums.DevResult;
import com.crm.dpeda.enums.StateStatus;
import com.crm.dpeda.query.SaleChanceQuery;
import com.crm.dpeda.utils.AssertUtil;
import com.crm.dpeda.utils.PhoneUtil;
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
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /*** 多条件分页查询营销机会数据
     * 返回的数据格式必须满足layui中的数据表格要求的格式
     * @param saleChanceQuery * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        //List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        //另一种写法：注销2和3的信息
        //PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChances);
        //3、使用pageInfo封装查询结果,得到分页好的列表
        PageInfo<SaleChance> pageInfo = new PageInfo<> (saleChanceMapper.selectByParams(saleChanceQuery));
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加营销机会
     *  1、参数校验
     *      customerName 客户名称 非空
     *      linkMan 联系人 非空
     *      linkPhone 联系号码 非空，手机号码格式正确
     *  2、设置相关参数的默认值
     *      createMan 创建人 当前登录用户名
     *      assignMan 指派人
     *          如果未设置指派人 (默认)
     *              state 分配状态 (0=未分配,1=已分配)
     *                  0 = 未分配
     *           assignTime 指派时间
     *              设置为 null
     *           devResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)
     *              0 = 未开发 (默认)
     *          如果设置了指派人
     *              state 分配状态 (0=未分配,1=已分配)
     *                  1 = 已分配
     *             assignTime 指派时间
     *                  系统当前时间
     *             devResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)
     *                  1=开发中
     *       isValid 是否有效 (0=无效,1=有效)
     *          设置为有效1=有效
     *       createDate 创建时间
     *          默认是系统当前时间
     *       updateDate
     *          默认是系统当前时间
     *   3、执行添加操作，判断受影响的行数
     * @param saleChance
     */

    @Transactional
    public void addSaleChance(SaleChance saleChance) {
        //1、参数校验（客户名称、联系人、联系电话）
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(),
                saleChance.getLinkPhone());
        //2、设置相关字段的默认值
        //is_valid是否有效 (0=无效,1=有效), 默认设置有效为 1
        saleChance.setIsValid(1);
        //createDate创建时间 默认系统当前时间
        saleChance.setCreateDate(new Date());
        //updateDate 默认系统当前时间
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if(StringUtils.isBlank(saleChance.getAssignMan())) {
            //如果为空，则表示未设置指派人
            // state 分配状态(0=未分配,1=已分配) 默认设置： 0=未分配
            /**
             * 注意事项：这里 StateStatus 类是枚举类，设置了分配状态信息
             * 为什么要这么设置使用枚举呢？ 原因： 因为我们在这里不设置分配状态类枚举，
             * 那么我们到后期进行测试、维护需要对前面这些设置了状态信息重新设置调整，后期不方便测 试、维护！
             * 所以我们在这里创建状态类，使用枚举信息定好状态，后期只需要对状态类设置就可以啦！ */
            saleChance.setState(StateStatus.UNSTATE.getType());
            //设置 assignTime指派时间为 设置为 null
            saleChance.setAssignTime(null);
            //设置 devResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败) 当前 0 为 未开发
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else {
            //如果不为空，则已经设置了指派人
            //设置 state分配状态(0=未分配,1=已分配) 当前设置 1=已分配
            saleChance.setState(StateStatus.STATED.getType());
            //设置 assignTime指派时间为 系统当前时间
            saleChance.setAssignTime(new Date());
            //设置 devResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败) 当前 1为 开发中
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //3、执行添加操作
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1, "添加失 败");
    }

    /**
     *
     * @param customerName
     *  @param linkMan
     *  @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        //customerName 客户名称 非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        //linkMan 联系人 非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        //linkPhone 联系号码 非空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        //判断手机号码格式是否正确
        //linkPhone 联系号码 非空，手机号码格式正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系电话格式不正确");
    }

/**
 *修改数据
 *  1.校验参数
 *      id属性是必须存在的，查询数据库校验(营销功能ID 非空，数据库中对应的记录存在)
 *      customerName 客户名称 非空
 *      linkMan 联系人 非空
 *      linkPhone 手机号码 非空，手机号11位正则校验(手机号码格式正确)
 *  2.默认值
 *      uupdateDate 修改时间 设置为当前系统时间
 *      assignMan 指派人
 *      判断是否指派了工作人员
 *          1.修改前没有分配人
 *              修改后没有分配人
 *                  不做任何操作
 *              修改后有分配人
 *                  assignTime指派时间
 *                  分配状态 1 = 已分配
 *                  开发状态 1 = 开发中
 *          2.修改前有分配人
 *              修改后没有分配人
 *                  assignTime指派时间 设置为 null
 *                  分配状态 0 = 未分配
 *                  开发状态 0 = 未开发中
 *             修改后有分配人
 *              判断更改后的人员和更改前的人员有没有变动,是否同一个指派人
 *                  没有变动不做操作(如果是，则不需要操作)
 *                  有变动，assignTime最新的时间(如果不是是，则需要更新 assignTime指派时间 设置为当前系统时间)
 *          3.执行修改操作，判断是否修改成功
 * @param saleChance
 */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        //1、参数校验
        //id属性是必须存在的，查询数据库校验(营销功能ID 非空，数据库中对应的记录存在)
        AssertUtil.isTrue(null == saleChance.getId(),"待更新记录不存在");
        //通过主键查询saleChance对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断数据库中对应的记录是否存在
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        //参数校验：共用 添加操作的参数校验 信息

        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //2、设置相关字段的默认值
        //uupdateDate 修改时间 设置为当前系统时间
        saleChance.setUpdateDate(new Date());
        //assignMan 指派人
        //判断原始数据是否存在 assignMan 指派人
        if(StringUtils.isBlank(temp.getAssignMan())) {
            //判断用户在修改时是否指定了指派人
            if(!StringUtils.isBlank(saleChance.getAssignMan())) {
                //修改分配时间。系统当前时间
                saleChance.setAssignTime(new Date());
                //修改分配状态。1 = 已分配
                saleChance.setState(StateStatus.STATED.getType());
                //修改开发状态。1 = 开发中
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        }else {
            //原始数据存在指派人
            //用户在修改未指定了指派人。（也就是说原来数据库中有值，现在修改成null）
            if(StringUtils.isBlank(saleChance.getAssignMan())) {
                //assignTime指派时间 设置为 null
                saleChance.setAssignTime(null);
                //修改分配状态。0 = 未分配
                saleChance.setState(StateStatus.UNSTATE.getType());
                //修改开发状态。0 = 未开发
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else {
                //修改前有值，修改后也有值。
                //判断修改前后是否是同一个用户指派人
                if(!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    //更新指派时间
                    saleChance.setAssignTime(new Date());
                }else {
                    //更新指派时间为修改前的时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
        //3、执行更新操作
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1," 更新失败");
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids) {
        //1、判断id是否为空
        AssertUtil.isTrue(null == ids && ids.length < 1, "待删除记录不存在");
        //2、执行删除操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "数据删除失 败");
    }

    /**
     * 客户开发————修改营销的开发状态
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id,Integer devResult) {
        //1、判断id是否为空
        AssertUtil.isTrue(null == id,"待更新记录不存在");
        //2、通过id查询营销机会的数据
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //3、判断对象是否为空
        AssertUtil.isTrue(null == saleChance, "待更新记录不存在");
        //4、设置开发状态
        saleChance.setDevResult(devResult);
        //5、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"开发状态更新失败");
    }
}