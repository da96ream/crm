layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form;

    /**
     * 表格数据渲染
     */
    var tableIns = table.render({
        elem: '#customerList',
        url: ctx+'/customer/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerListTable",
        cols: [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'name', title: '客户名',align:"center"},
            {field: 'fr', title: '法人',  align:'center'},
            {field: 'khno', title: '客户编号', align:'center'},
            {field: 'area', title: '地区', align:'center'},
            {field: 'cusManager', title: '客户经理',  align:'center'},
            {field: 'level', title: '客户级别', align:'center'},
            {field: 'postCode', title: '邮编', align:'center'},
            {title: '操作', templet:'#customerListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        //表格重载
        tableIns.reload({
            where: {
                customerName: $("[name='name']").val(),
                customerNo: $("[name='khno']").val(),
                level: $("[name='level']").val()
            },
            page: {
                curr: 1
            }
        })
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(customers)',function (data) {
        if(data.event == "add") {
            //打开窗口
            openAddOrUpdateCustomerDialog();
        } else if(data.event == "order") {
            //获取被选中的行
            var checkStatus = table.checkStatus(data.config.id);
            //客户订单数据查看（传递被选中的记录）
            openCustomerOrderDialog(checkStatus.data);
        }
    });

    /**
     * 打开添加或者修改窗口
     */
    function openAddOrUpdateCustomerDialog(id) {
        var title = "添加数据";
        var url = ctx + "/customer/toAddOrUpdateCustomerPage";

        //判断id是否为空。如果不为空，则为更新操作
        if(id != null && id != '') {
            title = "修改数据";
            url += "?id="+id;
        }

        layui.layer.open({
            type: 2,
            title: title,
            shadeClose: false,
            shade: 0.5,
            area: ['650px', '500px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * 打开客户订单对话框
     * @param data
     */
    function openCustomerOrderDialog(data) {
        //判断用户是否选择客户
        if(data.length == 0) {
            layer.msg("请选择客户记录", {icon:5});
            return;
        }
        if(data.length > 1) {
            layer.msg("暂不支持批量查看", {icon:5});
            return;
        }
        //打开窗口
        layui.layer.open({
            type: 2,
            title: "订单信息查看",
            shadeClose: false,
            shade: 0.5,
            area: ['650px', '500px'],
            maxmin: true,
            content: ctx+"/customer/toCustomerOrderPage?customerId="+data[0].id
        });
    }

    /**
     * 行工具栏监听
     */
    table.on('tool(customers)', function (data) {
        if (data.event == "edit") {
            //打开对话框
            openAddOrUpdateCustomerDialog(data.data.id);
        } else if(data.event == "del") {
            console.log(data.data);
            //删除客户信息
            deleteCustomer(data.data.id);
        }

    });

    /**
     * 删除客户信息
     * @param id
     */
    function deleteCustomer(id) {
        //删除单条用户记录
        layer.confirm('确认要删除吗？',{icon:3,title:'系统提示'},function (index) {
            //关闭确认框
            layer.close(index);

            //发送ajax请求
            $.ajax({
                type: "post",
                url: ctx + "/customer/delete",
                data: {
                    id: id
                },
                success: function (result) {
                    //判断删除结果
                    if(result.code == 200) {
                        layer.msg("删除成功",{icon:6});
                        //重载表格
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg,{icon: 5});
                    }
                }
            })
        })
    }
});
