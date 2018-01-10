$(function() {
    $("#execute").click(function() {
        var sql = $("#sql").val();
        var param = $("#param").val();
        if (StringUtils.isBlank(sql)) {
            layer.msg("sql不能为空");
            return;
        }
        if (StringUtils.isBlank(param)) {
            layer.msg("参数不能为空");
            return;
        }
        if (!sql.includes(":") && !sql.includes("?")) {
            layer.msg("sql必须包含冒号或者问号");
            return;
        }
        if (sql.includes(":") && sql.includes("?")) {
            layer.msg("sql不能同时包含冒号或者问号");
            return;
        }
        sql += " ";
        // 判断命名参数还是占位符
        var isMap = sql.includes(":");
        var data = null;
        try {
            // 将json字符串转换成json对象或者数组
            data = JSON.parse(param);
        } catch(error) {
            if (isMap) {
                layer.msg("参数格式有误，占位符赋值，必须是json对象格式");
                return;
            }
            data = param.split(",");
        }
        // 如果是命名参数
        if (isMap) {　　
            for (var key in data) {
                var value = data[key];
                if (StringUtils.isString(value)) {
                    value = "'" + value + "'";
                } else if (value instanceof Array) {
                    value = value.join(",");
                }
//              sql = sql.replace(new RegExp(":" + key + "(\\W)", "gm"), value+"$1");　
                sql = sql.replace(new RegExp(":" + key + "\\b", "gm"), value);　
            }
        } else {
            if (!data instanceof Array) {
                layer.msg("参数不是json数组");
                return;
            }
            var types = ["string", "datetime","timestamp","date"];
            data.forEach(function(item) {
            	// mybatis 日志参数，会包含类型，例如：460(Long)
                if (StringUtils.isString(item) && item.includes("(") && item.includes(")")) {
                    var type = item.substring(item.lastIndexOf("(") + 1, item.lastIndexOf(")")); 
                    item = item.substring(0, item.lastIndexOf("("));
                    if (types.indexOf(type.toLowerCase()) != -1) {
                        item = "'" + item + "'";
                    }
                } else {
                    if (StringUtils.isString(item)) {
                        item = "'" + item + "'";
                    }
                }
                sql = sql.replace("?", item);
            });
        }
        if (sql.includes("?")) {
            layer.msg("有参数未赋值");
        }
        $("#result").val(sql);
    });
});