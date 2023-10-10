layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     * table.render({
     *     elem: "数据表格的id",
     *     url: "后端接口"
     *     ....
     * });
     */
    var tableIns = table.render({
        id: 'saleChanceTable',
        elem: '#saleChanceList',
        height: 'full-125',
        cellMinWidth: 95,
        url: ctx + '/sale_chance/list',
        page: true,
        limit: 10,
        limits:[10,20,20],
        toolbar: '#toolbarDemo', //绑定头部工具栏
        cols: [[
            {type:'checkbox', fixed:'center'},
            {field:'id', title:'编号', sort:true, fixed:'left'},
            {field:'chanceSource', title:'机会来源', align:'center'},
            {field:'customerName', title:'客户名称', align:'center'},
            {field:'cgjl', title:'成功几率', align:'center'},
            {field:'linkMan', title:'联系人', align:'center'},
            {field:'linkPhone', title:'联系号码', align:'center'},
            {field:'description', title:'描述', align:'center'},
            {field:'createMan', title:'创建人', align:'center'},
            {field:'uname', title:'分配人', align:'center'},
            {field:'assignTime', title:'分配时间', align:'center'},
            {field:'createDate', title:'创建时间', align:'center'},
            {field:'updateDate', title:'结束时间', align:'center'},
            {field:'state', title:'状态', align:'center',templet:function (d) {
                //调用函数，返回格式化的结果
                return formatState(d.state);
            }},
            {field:'devResult', title:'开发状态', align:'center', templet:function (d) {
                //调用函数，返回格式化的结果
                return formatDevResult(d.devResult);
            }},
            //通过 templet 来绑定行工具栏
            {title: '操作', templet:'#saleChanceListBar',fixed:'right',align:'center',minWidth:150}
        ]]
    });

    /**
     * 格式化分配状态
     *  0 = 未分配
     *  1 = 已分配
     *  其他 = 未知
     * @param state
     */
    function formatState(state) {
        if(state == 0) {
            return "<div style='color:grey;'>未分配</div>";
        } else if(state == 1) {
            return "<div style='color:green;'>已分配</div>";
        } else {
            return "<div style='color:red;'>未知</div>";
        }
    }

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
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        //表格重载：多条件查询（默认还是会去请求表格渲染时的url）
        tableIns.reload({
            where: {
                //通过文本框/下拉框的值，设置传递的参数
                customerName: $("[name='customerName']").val(),
                createMan: $("[name='createMan']").val(),
                state: $("#state").val()
            },
            page: {
                curr: 1,
            }
        })
    });

    /**
     * 监听头部工具栏
     * table.on('toolbar(头部工具栏的id属性值)',function (data) {
     *
     * });
     */
    table.on('toolbar(saleChances)',function (data) {
        // console.log(data.event); 对应的元素上设置的lay-event属性值
        if(data.event == 'add') {
            //添加操作
            openSaleChanceDialog();
        } else if(data.event == 'del') {
            //删除操作
            deleteSaleChance(data);
        }
    });

    /**
     * 打开添加/修改窗口
     *  如果id为空，则为添加操作；id不为空，则为修改操作。
     */
    function openSaleChanceDialog(saleChanceId) {
        var title = '添加数据';
        var url = ctx + '/sale_chance/toSaleChancePage';

        //判断id是否为空
        if (saleChanceId != null && saleChanceId != '') {
            //更新操作
            title = '修改数据';
            //把id传递过去
            url += '?saleChanceId='+saleChanceId;
        }

        layui.layer.open({
            type: 2,
            title: title,
            shadeClose: false,
            shade: 0.5,
            area: ['500px', '610px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * 行工具栏监听
     * table.on('tool(行工具栏的id属性值)',function (data) {
     *
     * });
     */
    table.on('tool(saleChances)', function (data) {
        // console.log(data);
        //判断事件类型
        if(data.event == 'edit') {
            //获取到待更新记录的id
            var saleChanceId = data.data.id;
            openSaleChanceDialog(saleChanceId);
        } else if(data.event == 'del') {
            //弹出确认框
            layer.confirm('确认要删除吗？',{icon:3,title:'系统提示'},function (index) {
                //关闭确认框
                layer.close(index);

                //发送ajax请求
                $.ajax({
                    type: "post",
                    url: ctx + "/sale_chance/delete",
                    data: {
                        ids: data.data.id
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
    })

    /**
     * 批量删除
     * @param data
     */
    function deleteSaleChance(data) {
        //获取数据表格选中的行数据 table.checkStatus('数据表格的ID属性值');
        var checkStatus = table.checkStatus("saleChanceTable");
        // console.log(checkStatus);
        //获取所有被选中的记录对应的数据
        var saleChanceData = checkStatus.data;
        //判断用户是否选择的记录
        if (saleChanceData.length < 1) {
            layer.msg("请选择要删除的记录",{icon:5});
            return;
        }

        //弹出确认框
        layer.confirm("确认要删除选中的记录吗？",{icon:3,title:"系统提示"},function (index) {
            //关闭确认框
            layer.close(index);
            //传递的参数是数组  ids=1&ids=2&ids=3
            var ids = "ids=";
            //循环选中行记录的数据
            for(var i=0; i<saleChanceData.length; i++) {
                if(i < saleChanceData.length-1) {
                    ids = ids + saleChanceData[i].id + "&ids=";
                } else {
                    ids = ids + saleChanceData[i].id;
                }
            }
            // console.log(ids);

            //发送ajax请求
            $.ajax({
                type: "post",
                url: ctx + "/sale_chance/delete",
                data: ids, //传递的参数是一个数组
                success: function (result) {
                    if(result.code == 200) {
                        layer.msg("删除成功",{icon:6});
                        //重载表格
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg,{icon: 5});
                    }
                }
            });
        });
    }
});