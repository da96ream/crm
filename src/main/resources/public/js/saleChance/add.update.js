layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 监听表单的submit事件
     * form.on('submit(提交按钮的lay-filter属性值)', function (data) {
     *
     * });
     */
    form.on('submit(addOrUpdateSaleChance)', function (data) {
        //提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...",{
            icon: 16,
            time: false,
            shade: 0.5
        });

        //定义url
        var url = ctx + "/sale_chance/add";

        //通过判断是否有id来判断是添加操作还是修改操作
        //通过获取隐藏域中的id值
        var saleChanceId = $("[name='id']").val();
        if(saleChanceId != null && saleChanceId != '') {
            //更新操作
            url = ctx + "/sale_chance/update";
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
     * 点击取消按钮，关闭当前弹出层
     */
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

    /**
     * 加载指派人的下拉框
     */
    $.ajax({
        type: "get",
        url: ctx + "/user/queryAllSales",
        data: {},
        success: function (data) {
            // console.log(data);
            //判断返回的数据是否为空
            if (data != null) {
                //获取隐藏域设置的指派人ID
                var assignManId = $("#assignManId").val();
                //遍历返回的数据
                for(var i = 0; i < data.length; i++) {
                    var opt = "";
                    //如果循环得到的id与隐藏域的id相等，则表示被选中
                    if(assignManId == data[i].id) {
                        //设置下拉选项选中
                        opt = "<option value='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                    } else {
                        //设置下拉选项
                        opt = "<option value='"+data[i].id+"'>"+data[i].uname+"</option>";
                    }
                    //将下拉项设置到下拉框中
                    $("#assignMan").append(opt);
                }
            }
            //重新渲染下拉框的内容
            layui.form.render("select");
        }
    });
});