layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 数据表格渲染
     */
    var tableIns = table.render({
        id:'userTable',
        elem: '#userList',
        height: 'full-125',
        cellMinWidth:95,
        url: ctx + '/user/list',
        page: true,
        limit: 10,
        toolbar: '#toolbarDemo',
        cols: [[
            {type:'checkbox', fixed:'center'},
            {field: 'id', title: '编号',  sort: true, fixed: 'left'},
            {field: 'userName', title: '用户名称', align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'email', title: '邮箱', align:'center'},
            {field: 'phone', title: '手机号码', align:'center'},
            {title:'操作',templet:'#userListBar', fixed: 'right', align:'center', minWidth:150}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        tableIns.reload({
            where: {
                userName: $("[name='userName']").val(),
                email: $("[name='email']").val(),
                phone: $("[name='phone']").val()
            },
            page: {
                curr: 1
            }
        });
    });

    /**
     * 头部工具栏监听
     */
    table.on('toolbar(users)',function (data) {
        if(data.event == "add") {
            //打开窗口
            openAddOrUpdateUserDialog();
        } else if(data.event == "del") {
            //获取数据表格选中的行数据 table.checkStatus('数据表格的ID属性值');
            var checkStatus = table.checkStatus(data.config.id);
            // console.log(checkStatus);
            //获取所有被选中的记录对应的数据
            var checkedUserData = checkStatus.data;
            //删除多条记录
            deleteUsers(checkedUserData);
        }
    });

    /**
     * 批量删除
     */
    function deleteUsers(checkedUserData) {
        //判断用户是否选择了要删除的记录
        if(checkedUserData.length == 0) {
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
            for(var i=0; i<checkedUserData.length; i++) {
                if(i < checkedUserData.length-1) {
                    ids = ids + checkedUserData[i].id + "&ids=";
                } else {
                    ids = ids + checkedUserData[i].id;
                }
            }
            // console.log(ids);

            //发送ajax请求
            $.ajax({
                type: "post",
                url: ctx + "/user/delete",
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

    /**
     * 打开添加或修改窗口
     */
    function openAddOrUpdateUserDialog(id) {
        var title = "添加用户";
        var url = ctx + "/user/toAddOrUpdateUserPage";

        //判断id是否为空；如果为空，则为添加操作，否则是修改操作
        if (id != null && id != '') {
            title = "更新用户";
            url += "?id="+id;
        }
        layui.layer.open({
            type: 2,
            title: title,
            shadeClose: false,
            shade: 0.5,
            area: ['650px', '400px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * 行工具栏监听
     */
    table.on('tool(users)', function (data) {
        if (data.event == "edit") {
            //打开对话框
            openAddOrUpdateUserDialog(data.data.id);
        } else if (data.event == "del") {
            //删除单条用户记录
            layer.confirm('确认要删除吗？',{icon:3,title:'系统提示'},function (index) {
                //关闭确认框
                layer.close(index);

                //发送ajax请求
                $.ajax({
                    type: "post",
                    url: ctx + "/user/delete",
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

    });
});