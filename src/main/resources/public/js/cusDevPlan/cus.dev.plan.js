layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    var tableIns = table.render({
        id: 'saleChanceTable',
        elem: '#saleChanceList',
        height: 'full-125',
        cellMinWidth: 95,
        url: ctx + '/sale_chance/list?flag=1', //设置flag参数，表明查询客户开发计划
        page: true,
        limit: 10,
        toolbar: '#toolbarDemo', //绑定头部工具栏
        cols: [[
            {type:'checkbox', fixed:'center'},
            {field:'id', title:'编号', sort:true, fixed:'left'},
            {field:'chanceSource', title:'机会来源', align:'center'},
            {field:'customerName', title:'客户名称', align:'center'},
            {field:'cgjl', title:'成功几率', align:'center'},
            {field:'linkMan', title:'联系人', align:'center'},
            {field:'createMan', title:'创建人', align:'center'},
            {field:'devResult', title:'开发状态', align:'center', templet:function (d) {
                //调用函数，返回格式化的结果
                return formatDevResult(d.devResult);
            }},
            //通过 templet 来绑定行工具栏
            {title: '操作', templet:'#op',fixed:'right',align:'center',minWidth:150}
        ]]
    });

    /**
     * 格式化开发结果
     *  0 = 未开发
     *  1 = 开发中
     *  2 = 开发成功
     *  3 = 开发失败
     *  其他 = 未知
     * @param devResult
     */
    function formatDevResult(devResult) {
        if(devResult == 0) {
            return "<div style='color:grey;'>未开发</div>";
        } else if(devResult == 1) {
            return "<div style='color:orange;'>开发中</div>";
        } else if(devResult == 2) {
            return "<div style='color:green;'>开发成功</div>";
        } else if(devResult == 3) {
            return "<div style='color:red;'>开发失败</div>";
        } else {
            return "<div style='color:blue;'>未知</div>";
        }
    }

    /**
     * 搜索按钮点击事件
     */
    $(".search_btn").click(function () {
        //表格重载：多条件查询（默认还是会去请求表格渲染时的url）
        tableIns.reload({
            where: {
                //通过文本框/下拉框的值，设置传递的参数
                customerName: $("[name='customerName']").val(),
                createMan: $("[name='createMan']").val(),
                devResult: $("#devResult").val()
            },
            page: {
                curr: 1,
            }
        })
    });

    /**
     * 行工具栏监听
     */
    table.on('tool(saleChances)', function (data) {
        //判断事件类型
        if(data.event == 'dev') {
            //开发
            openCusDevPlanDialog('计划项数据开发', data.data.id);

        } else if(data.event == 'info') {
            //详情
            openCusDevPlanDialog('计划项数据维护', data.data.id);
        }
    });

    /**
     * 打开计划项开发与详情页面
     * @param title
     * @param id
     */
    function openCusDevPlanDialog(title, id) {

        layui.layer.open({
            type: 2,
            title: title,
            shadeClose: false,
            shade: 0.5,
            area: ['700px', '500px'],
            maxmin: true,
            content: ctx+"/cus_dev_plan/toCusDevPlanPage?id="+id
        });
    }

});
