layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /**
     * 表单submit提交
     * form.on('submit(表单lay-filter的属性值)', function(data) {
     *           return false; //阻止表单提交
     * });
     */
    form.on('submit(login)', function(data){
        //发送ajax请求，传递用户姓名与密码，执行用户登录操作
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        /*表单元素的非空效验*/
        /**
         * 发送ajax请求，传递用户姓名与密码，执行用户登录操作
         * 注意事项：这里的  ctx  表示是后端的 BaseContrller 的读取当前项目路径
         *         这里的 result 表示后端的 ResultInfo 和  UserController 的 ResultInfo 数据
         */
        $.ajax({
            type: "post",   //请求方式
            url: ctx + "/user/login",  //请求项目路径访问
            data: {
                userName: data.field.username,  //表示用户名
                userPwd: data.field.password    //表示用户密码
            },
            success:function (result) {    //result是回调函数，用来接受后端的返回数据：controller层的数据信息
                //注意事项：//做前端页面查询测试改为 @PostMapping("login")  那样可以看到测试的状态码
                console.log(result);        //打印输出一下
                //判断是否登录成功，如果code=200，则表示成功，否则表示失败
                if (result.code==200){
                    layer.msg("登录成功",function () {

                        //登录成功
                        /**
                         * 设置用户是登录状态,这里考虑到怎么知道你是登录成功的信息呢?
                         * 1。利用session会话
                         *      保存用户信息,如果会话存在，则用户是登录状态;否则是非登录状态。
                         *      缺点:服务器关闭,会话则会失效
                         * 2。利用cookie
                         *      保存用户信息,cookie未失效，则用户是登录状态
                         */
                        //判断用户是否选择记住密码。如果选中，则设置cookie对象7天有效
                        if ($("#rememberMe").prop("checked")){
                            //选中。
                            //将用户信息存储到cookie中
                            //判断用户是否选择记住密码。如果选中，则设置cookie对象7天有效
                            $.cookie("userIdStr",result.result.userIdStr,{expires:7});
                            $.cookie("userName",result.result.userName,{expires:7});
                            $.cookie("trueName",result.result.trueName,{expires:7});
                        }else {
                            $.cookie("userIdStr",result.result.userIdStr);
                            $.cookie("userName",result.result.userName);
                            $.cookie("trueName",result.result.trueName);
                        }
                        //跳转到主页  //这里是进行超链接的跳转
                        window.location.href = ctx + "/main";
                    });
                }else {
                    //登录失败
                    layer.msg(result.msg,{icon:5})
                }
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
});