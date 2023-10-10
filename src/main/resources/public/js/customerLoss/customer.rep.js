layui.use(['table', 'layer', "form"], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    var tableIns = table.render({
        elem: '#customerRepList',
        url: ctx + '/customer_rep/list?lossId=' + $("input[name='id']").val(),
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerRepListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'measure', title: '暂缓措施', align: "center"},
            {field: 'createDate', title: '创建时间', align: "center"},
            {field: 'updateDate', title: '更新时间', align: "center"},
            {title: '操作', fixed: "right", align: "center", minWidth: 150, templet: "#customerRepListBar"}
        ]]
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(customerReps)', function (data) {
        if (data.event == "add") {
            //打开添加/更新暂缓数据的页面
            openAddOrUpdateCustomerReprDialog();

        } else if (data.event == "confirm") {
            //确认流失操作（更新流失客户的流失状态）
            updateCustomerLossState();
        }
    });

    /**
     * 监听行工具栏
     */
    table.on('tool(customerReps)', function (data) {
        if (data.event == "edit") {
            //打开添加/更新暂缓数据的页面
            openAddOrUpdateCustomerReprDialog(data.data.id);

        } else if (data.event == "del") {
            //删除暂缓操作
            deleteCustomerRepr(data.data.id);
        }
    });

    /**
     * 打开添加/更新暂缓数据的页面
     */
    function openAddOrUpdateCustomerReprDialog(id) {
        var title = "添加数据";
        var url = ctx + "/customer_rep/toAddOrUpdateCustomerReprPage?lossId=" + $("[name='id']").val();

        //判断id如果不为空，则表示更新操作
        if (id != null && id != "") {
            title = "更新数据";
            url += "&id=" + id;
        }

        layui.layer.open({
            type: 2,
            title: title,
            area: ['480px', '200px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * 删除操作
     * @param id
     */
    function deleteCustomerRepr(id) {
        layer.confirm('确定要删除该记录吗？', {icon: 3, title: "暂缓管理"}, function (index) {
            //关闭确认框
            layer.close(index);

            //发送ajax请求，删除记录
            $.ajax({
                type: "post",
                url: ctx + "/customer_rep/delete",
                data: {
                    id: id
                },
                success: function (result) {
                    if (result.code == 200) {
                        layer.msg("删除成功", {icon: 6});
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });
    }

    /**
     * 确认流失操作（更新流失客户的流失状态）
     */
    function updateCustomerLossState() {
        layer.confirm('确定标记当前客户为确认流失吗？', {icon: 3, title: "系统提示"}, function (index) {
            //关闭确认框
            layer.close(index);

            //prompt层 输入框
            layer.prompt({title: '请输入流失原因', formType: 2}, function (text, index) {
                //关闭输入框
                layer.close(index);

                /**
                 * 发送ajax请求给后台，更新指定流失客户的流失状态
                 *  1、指定流失客户   流失客户ID （隐藏域）
                 *  2、流失原因      输入框的内容（text）
                 */
                $.ajax({
                    type: "post",
                    url: ctx + "/customer_loss/updateCustomerLossStateById",
                    data: {
                        id: $("[name='id']").val(), // 流失客户的ID
                        lossReason: text // 流失原因
                    },
                    dataType: "json",
                    success: function (result) {
                        if (result.code == 200) {
                            layer.msg('确认流失成功', {icon: 6});
                            //关闭窗口
                            layer.closeAll("iframe");
                            //刷新父页面
                            parent.location.reload();
                        } else {
                            layer.msg(result.msg, {icon: 5});
                        }
                    }
                });

            });

        });
    }
});
