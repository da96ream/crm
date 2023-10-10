layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 表单submit监听
     */
    form.on('submit(addOrUpdateCustomer)', function (data) {
        //提交数据时的加载层 （https://layer.layui.com/）
        var index = top.layer.msg("数据提交中,请稍后...",{
            icon: 16,
            time: false,
            shade: 0.8
        });

        //请求的地址
        var url = ctx + "/customer/add"; // 添加操作

        //判断id是否为空
        if($('[name="id"]').val()) {
            url = ctx + "/customer/update"; //更新操作
        }

        //发送ajax请求
        $.post(url, data.field, function (result) {
            if (result.code == 200) {
                top.layer.msg("操作成功！",{icon:6});
                //关闭加载层
                top.layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //刷新父窗口，重新加载数据
                parent.location.reload();
            } else {
                layer.msg(result.msg, {icon:5});
            }
        });

        return false;
    });

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
});