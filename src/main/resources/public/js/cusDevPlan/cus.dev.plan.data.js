layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    var tableIns = table.render({
        id: 'cusDevPlanTable',
        elem: '#cusDevPlanList',
        height: 'full-125',
        cellMinWidth: 95,
        url: ctx + '/cus_dev_plan/list?saleChanceId='+$("[name='id']").val(),
        page: true,
        limit: 10,
        toolbar: '#toolbarDemo', //绑定头部工具栏
        cols: [[
            {type:'checkbox', fixed:'center'},
            {field:'id', title:'编号', sort:true, fixed:'left'},
            {field:'planItem', title:'计划项内容', align:'center'},
            {field:'planDate', title:'计划时间', align:'center'},
            {field:'exeAffect', title:'执行效果', align:'center'},
            //通过 templet 来绑定行工具栏
            {title: '操作', templet:'#cusDevPlanListBar',fixed:'right',align:'center',minWidth:150}
        ]]
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(cusDevPlans)', function (data) {
        if(data.event == 'add') {  //添加计划项
            //打开窗口
            openAddOrUpdateCusDevPlanDialog();
        } else if(data.event == 'success') {  //开发成功
            updateSaleChanceDevResult(2);

        } else if(data.event == 'failed') {  //开发失败
            updateSaleChanceDevResult(3);
        }
    })

    /**
     * 修改营销机会的开发状态
     */
    function updateSaleChanceDevResult(devResult) {
        //弹出确认框
        layer.confirm("您确定要执行该操作吗？",{icon:3},function (index) {
            //得到需要被更新的营销机会的id
            var saleChanceId = $("[name='id']").val();
            //发送ajax请求，修改营销机会的开发状态
            $.post(ctx+"/sale_chance/updateSaleChanceDevResult",{
                id: saleChanceId,
                devResult: devResult
            },function (result) {
                if(result.code == 200) {
                    layer.msg("更新成功",{icon: 6});
                    //关闭窗口
                    layer.closeAll("iframe");
                    //刷新父窗口
                    parent.location.reload();
                } else {
                    layer.msg(result.msg,{icon: 5});
                }
            })

        });
    }

    /**
     * 打开添加或更新页面
     * @param id
     */
    function openAddOrUpdateCusDevPlanDialog(id) {
        var title = "添加计划项";
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId="+$("[name='id']").val();

        //判断计划项id是否为空
        if(id != null && id != "") {
            title = "更新计划项";
            url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId="+$("[name='id']").val()+"&id="+id;
        }

        layui.layer.open({
            type: 2,
            title: title,
            shadeClose: false,
            shade: 0.5,
            area: ['500px', '300px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * 监听行工具栏
     */
    table.on('tool(cusDevPlans)', function (data) {
        if(data.event == "edit") {
            //打开窗口
            openAddOrUpdateCusDevPlanDialog(data.data.id);

        } else if(data.event == "del") {
            //删除计划项
            deleteCusDevPlan(data.data.id);
        }
    });

    /**
     * 删除计划项
     */
    function deleteCusDevPlan(id) {
        layer.confirm("您确定要删除该记录吗？",{icon:3}, function (index) {
            //发送ajax请求
            $.post(ctx+"/cus_dev_plan/delete",{id: id}, function (result) {
                if(result.code == 200) {
                    layer.msg("删除成功",{icon: 6});
                    //刷新数据表格
                    tableIns.reload();
                } else {
                    layer.msg(result.msg,{icon: 5});
                }
            })
        })
    }
});
