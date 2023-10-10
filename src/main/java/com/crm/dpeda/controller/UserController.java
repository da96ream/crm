package com.crm.dpeda.controller;

import com.crm.dpeda.base.BaseController;
import com.crm.dpeda.base.ResultInfo;
import com.crm.dpeda.exceptions.ParamsException;
import com.crm.dpeda.model.UserModel;
import com.crm.dpeda.service.UserService;
import com.crm.dpeda.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;


    /**
     * 用户登录方法
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo=new ResultInfo();
        //封装登录用户名、密码信息等等
        UserModel userModel=userService.userLogin(userName,userPwd);
        //设置将对象数据返回给请求(错误的、正确的信息）
        resultInfo.setResult(userModel);
//        try {
//            //封装登录用户名、密码信息等等
//            UserModel userModel=userService.userLogin(userName,userPwd);
//            //设置将对象数据返回给请求(错误的、正确的信息）
//            resultInfo.setResult(userModel);
//        }catch (ParamsException p){
//            //自定义异常
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            //未知的异常信息
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败！");
//        }
        return resultInfo;
    }

    //打开修改密码页面
    @RequestMapping("/toPasswordPage")
    public  String toPasswordPage(){
        return "user/password";
    }



    @PostMapping("updatePwd")
    @ResponseBody  //转换json
    public ResultInfo updateUserPassword(String oldPassword, String newPassword, String repeatPassword,
                                         HttpServletRequest request){
        ResultInfo resultInfo=new ResultInfo();
        //获取cookie的id
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePassword(userId,oldPassword,newPassword,repeatPassword);
//        try {
//            //获取cookie的id
//            Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
//            userService.updatePassword(userId,oldPassword,newPassword,repeatPassword);
//        }catch (ParamsException p){
//            //自定义异常
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            //未知的异常信息
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败！");
//            e.printStackTrace();
//        }
        return resultInfo;
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("/queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales() {
        return userService.queryAllSales();
    }
}
