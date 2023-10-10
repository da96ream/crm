package com.crm.dpeda.controller;

import com.crm.dpeda.base.BaseController;
import com.crm.dpeda.service.UserService;
import com.crm.dpeda.utils.LoginUserUtil;
import com.crm.dpeda.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 跳转系统登录页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 跳转系统欢迎页
     * @return
     */
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 跳转后台管理主页面
     * **注意事项：我们现在做的登录信息，目前只是超链接跳转，一跳转完成，信息值就失效了！**
     *
     * **所以我们需要对后端的controller器进行加强！**
     *
     *  * 提问：我们这里怎么获取用户对象信息？通常我们通过 ID 查询对象信息，但是我们这里有ID吗？
     *  * 答：我们点击任意主页面的信息都存在！那我们怎么获取ID呢？我们可以通过查询当前页面的主键的一个用户对象信息？
     *  * 提问：那边我们的主键怎么查啊？ 我们可以通过查询 cookies 信息来获取，当我们登录成功，cookies 会存储
     *  * 一个信息到我们后台。有同学可能会有疑问?  我们  cookies 信息存在吗？存在，只要你有请求都会有cookies .
     *
     * @return
     */
    @RequestMapping("/main")
    public String main(HttpServletRequest request) {
        //获取cookie的用户ID
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象，设置session会话的作用域
        User user=userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);
        return "main";
    }
}