layui.use(['table', 'layer', "form"], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    var tableIns = table.render({
        elem: '#customerLossList',
        url: ctx + '/customer_loss/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerLossListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'cusNo', title: '客户编号', align: "center"},
            {field: 'cusName', title: '客户名称', align: "center"},
            {field: 'cusManager', title: '客户经理', align: "center"},
            {field: 'lossReason', title: '流失原因', align: "center"},
            {field: 'confirmLossTime', title: '确认流失时间', align: "center"},
            {title: '操作', fixed: "right", align: "center", minWidth: 150, templet: "#op"}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        tableIns.reload({
            where: {
                customerName: $("[name='cusName']").val(),
                customerNo: $("[name='cusNo']").val(),
                state: $("#state").val()  //流失状态
            },
            page: {
                curr: 1
            }
        });

    });

    /**
     * 监听行工具栏
     */
    table.on('tool(customerLosses)', function (data) {
        if (data.event == "add") {
            //打开添加暂缓的页面
            openCustomerLossDialog("暂缓措施维护", data.data.id);

        } else if (data.event == "info") {
            //打开详情页面
            openCustomerLossDialog("暂缓措施查看", data.data.id);

        }
    });

    /**
     * 打开添加暂缓或者详情页面
     * @param title
     * @param lossId
     */
    function openCustomerLossDialog(title, lossId) {
        layui.layer.open({
            type: 2,
            title: title,
            area: ['650px', '500px'],
            content: ctx+"/customer_loss/toCustomerLossPage?lossId="+lossId,
            maxmin: true
        });
    }


});
