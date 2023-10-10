package com.crm.dpeda.service;

import com.crm.dpeda.base.BaseService;
import com.crm.dpeda.dao.UserMapper;
import com.crm.dpeda.model.UserModel;
import com.crm.dpeda.utils.AssertUtil;
import com.crm.dpeda.utils.Md5Util;
import com.crm.dpeda.utils.UserIDBase64;
import com.crm.dpeda.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {
    //自动注入 或者你调用 @Autowired
    @Resource
    private UserMapper userMapper;

    /**
     *         Service层(业务逻辑层:非空判断、条件判断等业务逻辑处理)
     *             1.参数判断,判断用户姓名、用户密码非空弄
     *                  如果参数为空，抛出异常(异常被控制层捕获并处理)
     *             2.调用数据访问层，通过用户名查询用户记录，返回用户对象
     *             3.判断用户对象是否为空
     *                  如果对象为空,，抛出异常(异常被控制层捕获并处理)
     *             4.判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
     *                  如果密码不相等,抛出异常(异常被控制层捕获并处理)
     *             5.如果密码正确，登录成功
     */
    public  UserModel userLogin(String userName,String userPwd){
        //1.参数判断,判断用户姓名、用户密码非空弄
        checkLoginParams(userName,userPwd);
        //2.调用数据访问层，通过用户名查询用户记录，返回用户对象
        User user=userMapper.queryUserByName(userName);
        //3.判断用户对象是否为空
        AssertUtil.isTrue(user==null,"用户名不存在！");
        //4.判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
        checkUserPwd(userPwd,user.getUserPwd());
        //返回构建的用户对象
        return   buildUserInfo(user);
    }

    /**
     * 构建需要进行返回的客户端的用户对象
     * 从数据库中提取处理的信息还是加密的，所以我们这边的信息也要都加密了一下，才给到客户端
     */
    private UserModel buildUserInfo(User user){
        //封装
        UserModel userModel=new UserModel();
        //没有进行调用加密的信息
        //userModel.setUserId(user.getId());
        //经过MD5的加密后的ID信息
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 判断密码是否正确
     * 密码判断
     *      先将客户端传递的密码加密，再与数据库中查询到的密码作比较
     * 注意事项：数据库中的用户密码 -->  4QrcOUm6Wau+VuBX8g+IPg==
     *      4QrcOUm6Wau+VuBX8g+IPg==  实际上是通过MD5算法加密字符串过后的密码
     * @param userPwd 用户输入的密码
     * @param pwd 数据库中的密码
     */
    private void checkUserPwd(String userPwd, String pwd) {
        //将客户端的传递到的密码进行加密认证
        userPwd= Md5Util.encode(userPwd);
        //判断是否相等
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户密码不正确！");
    }

    private void checkLoginParams(String userName, String userPwd) {
        //验证用户姓名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        //验证用户密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空！");
    }


    /**
     * Service层
     *         1.接收四个参数(用户ID、原始密码、新密码、确认密码)
     *         2．通过用户ID查诲用户记录，返回用户对象
     *         3.参数校验
     *              待更新用户记录是否存在(用户对象是否为空)
     *              判断原始密码是否为空
     *              判断原始密码是否正确(查询的用户对象中的用户密码是否原始密码一致)
     *              判断新密码是否为空
     *              判断新密码是否与原始密码一致(不允许新密码与原始密码)
     *              判断确认密码是否为空
     *              判断确认密码是否与新密码一致
     *         4.设置用户的新密码
     *              需要将新密码通过指定算法进行加密(md5加密)
     *         5．执行更新操作，判断受影响的行数
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        //通过用户ID查诲用户记录，返回用户对象
        User user=userMapper.selectByPrimaryKey(userId);
        //待更新用户记录是否存在(用户对象是否为空)
        AssertUtil.isTrue(null==user,"待更新记录不存在！！！");
        //参数校验
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        //设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        userMapper.updateByPrimaryKeySelective(user);
        //执行更新操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败！");
    }

    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
        //判断原始密码是否正确(查询的用户对象中的用户密码是否原始密码一致)(注意事项：这里的数据库密码是进行加密过的！)
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确！！！");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空！");
        //判断新密码是否与原始密码一致(不允许新密码与原始密码)
        AssertUtil.isTrue((oldPwd.equals(newPwd)),"新密码不与原始密码不一致");
        //判断确定密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确定密码不能为空");
        //判断确定密码是否与新密码一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确定密码与新密码不一致！");
    }

    /**
     *  查询所有的销售人员
     *  @return
     */
    public List<Map<String,Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

}





