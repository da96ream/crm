package com.crm.dpeda.exceptions;

import com.alibaba.fastjson.JSON;
import com.crm.dpeda.base.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    /**
     * 异常处理方法
     *  方法的返回值：
     *      1、返回视图
     *      2、返回json数据
     *  提问：我们知道方法返回值有json、视图两种，但是你怎么确定呢？
     *  如何判断方法的返回值？
     *      可以通过方法上是否声明@ResponseBody注解（反射机制获取方法上的注解）
     *          如果未声明，则返回视图
     *          如何声明了，则返回json数据
     *
     * @param request request请求对象
     * @param response response响应对象
     * @param handler 方法对象
     * @param e 异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {

        /**
         * 非法请求拦截的信息
         *      判断是否抛出异常：未登录
         *          如果抛出异常，就要求用户登录。重定向到我们登录页面
         */
        //判断是否未登录
        if (e instanceof NoLoginException){
            //重定向到登录页面
            ModelAndView mv=new ModelAndView("redirect:/index");
            return mv;
        }


        /**
         * 设置默认的异常处理
         */
        ModelAndView modelAndView=new ModelAndView("error");
        //设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请等等游戏再试试！！！");

        //判断  HandlerMethod  对象
        if (handler instanceof HandlerMethod){
            //类型转换
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            //获取方法上声明的@ResponseBody注解的对象，数据表示方法，否则就表示空，代表视图
            ResponseBody responseBody=handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (responseBody==null){
                //判断异常类型的是否自定义的异常
                if (e instanceof ParamsException){
                    ParamsException p= (ParamsException) e;
                    //s设置异常信息
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }else if (e instanceof  AuthException){
                    AuthException a= (AuthException) e;
                    modelAndView.addObject("code",a.getCode());
                    modelAndView.addObject("msg",a.getMsg());
                }
                return modelAndView;
            }else {
                //设置默认异常
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请等等游戏再试试！！！");

                //判断异常类型是否自定义
                if (e instanceof ParamsException){
                    ParamsException p= (ParamsException) e;
                    //s设置异常信息
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }else if (e instanceof  AuthException){
                    AuthException a= (AuthException) e;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }
                //设置响应类型的以及编码方式（JSON）
                response.setContentType("application/json;charset=UTF-8");
                //得到字符串的输出流
                PrintWriter pw=null;
                try {
                    pw=response.getWriter();
                    String json= JSON.toJSONString(resultInfo);
                    pw.write(json);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }finally {
                    //如果对象不为空，就关闭！
                    if (pw!=null){
                        pw.close();
                    }
                }
                return null;
            }
        }
        return modelAndView;
    }
}
