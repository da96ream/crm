layui.use(['form', 'layer', 'formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    //引入formSelects
    var formSelects = layui.formSelects;

    /**
     * 表单submit提交
     */
    form.on('submit(addOrUpdateUser)', function (data) {
        //提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...",{
            icon: 16,
            time: false,
            shade: 0.5
        });
        console.log(data.field);

        //定义url
        var url = ctx + "/user/add";

        //判断用户ID是否为空，如果不为空则为更新操作
        if ($("[name='id']").val()) {
            //更新操作
            url = ctx + "/user/update";
        }

        //发送ajax请求
        $.post(url, data.field, function (result) {
            if(result.code == 200) {
                layer.msg("操作成功", {icon:6});
                //关闭加载层
                layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //刷新父窗口，重新加载数据
                parent.location.reload();
            } else {
                layer.msg(result.msg,{icon: 5});
            }
        })
        return false;
    })

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

    /**
     * 加载角色下拉框
     * formSelects.config(ID, Options, isJson);
     *
     * @param ID        xm-select的值
     * @param Options   配置项
     * @param isJson    是否传输json数据, true将添加请求头 Content-Type: application/json; charset=UTF-8
     */
    formSelects.config("selectId",{
        type: "post",  //请求方式
        searchUrl: ctx+"/role/queryAllRoles?userId="+$("[name='id']").val(),  //请求地址
        keyName: "roleName",  //下拉框的文本内容，要与返回的数据中对应的key一致
        keyVal: "id"
    },true);


});