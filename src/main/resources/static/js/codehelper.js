//vue tools
var tools = new Vue({
    el: '#tools',
    data: {
        oldVal: "",
        newVal: ""
    },
    methods: {
        // 大写转小写
        bigToSmall: function() {
            this.newVal = this.oldVal.toLowerCase();
        },
        // 小写转大写
        smallToBig: function() {
            this.newVal = this.oldVal.toUpperCase();
        },
        // 清屏
        clear: function() {
            this.oldVal = "";
            this.newVal = "";
        },
        // 提取sql
        extractSql: function() {
            var sql = this.oldVal.match(/\"(.*?)\"/g).join("").replace(/"/g, "");
            request.getString("/formatSql", {
                sql: sql
            });
        },
        // 转stringBuilder
        stringBuilder: function() {
            var arr = this.oldVal.split("\n");
            this.newVal = "StringBuilder sql = new StringBuilder();\n";
            for (var i = 0; i < arr.length; i++) {
                if (StringUtils.isNotBlank(arr[i])) {
                    this.newVal += "sql.append(\" " + arr[i] + " \");\n";
                }
            }
        },
        // 获取set方法
        getSetMethod: function() {
            var oldStrArray = this.oldVal.split("\n");
            var objectName = "";
            var className = "";
            this.newVal = "";
            for (var i = 0; i < oldStrArray.length; i++) {
                if (oldStrArray[i].indexOf(" class ") != -1) {
                    className = getNextWord(oldStrArray[i], "class");
                    objectName = className.substring(0, 1).toLowerCase() + className.substring(1);
                }
                if (oldStrArray[i].indexOf("private ") != -1) {
                    var type = getNextWord(oldStrArray[i], "private");
                    var filed = getNextWord(oldStrArray[i], type);
                    this.newVal += objectName + ".set" + filed.substring(0, 1).toUpperCase() + filed.substring(1) + "(" + filed + ");\n";
                }
            }
            String newObj = className + " " + objectName + " = new " + className + "();\n";
            this.newVal = newObj + this.newVal;
        },
        // js 格式化
        jsFormat: function() {
            if (StringUtils.isNotBlank(this.oldVal)) {
                this.newVal = js_beautify(this.oldVal);
            }
        },
        // html 格式化
        htmlFormat: function() {
            if (StringUtils.isNotBlank(this.oldVal)) {
                this.newVal = style_html(this.oldVal);
            }
        },
        // sql 格式化
        sqlFormat: function() {
            if (StringUtils.isNotBlank(this.oldVal)) {
                request.getString("/formatSql", {
                    sql: this.oldVal
                });
            }
        },
        // 获取查询SQL
        getSelectSql: function() {
            if (StringUtils.isNotBlank(this.oldVal)) {
                request.getString("/getSelectSql", {
                    tableName: this.oldVal.replace(/'/g, ""),
                    dataSourceName: $('.radio-inline input[name="optionsRadiosinline"]:checked ').val()
                });
            }
        },
        // 获取Mapper
        getMapper: function() {
            if (StringUtils.isNotBlank(this.oldVal)) {
                request.getString("/getMapper", {
                    tableName: this.oldVal.replace(/'/g, ""),
                    dataSourceName: $('.radio-inline input[name="optionsRadiosinline"]:checked ').val(),
                    checkNull: $("#checkNull").is(':checked')
                });
            }
        },
        // 获取实体
        getEntity: function() {
            if (StringUtils.isNotBlank(this.oldVal)) {
                request.getString("/getEntity", {
                    tableName: this.oldVal.replace(/'/g, ""),
                    dataSourceName: $('.radio-inline input[name="optionsRadiosinline"]:checked ').val(),
                    hibernateAnnotation: $("#hibernateAnnotation").is(':checked'),
                    doradoAnnotation: $("#doradoAnnotation").is(':checked'),
                    fieldAnnotation: $("#fieldAnnotation").is(':checked'),
                    baseEntity: $("#baseEntity").is(':checked')
                });
            }
        }
    }
});

// ------------------------sql 拼接-----------------------------
// 根据数据类型获取匹配方式
var getMatchType = function(dataType) {
    return dataType == 'String' ? {
        selected: '=',
        options: [{
            text: '相等',
            value: '='
        },
        {
            text: '模糊匹配',
            value: 'like'
        },
        {
            text: '不等于',
            value: '!='
        }]
    }: {
        selected: '=',
        options: [{
            text: '相等',
            value: '='
        },
        {
            text: '不等于',
            value: '!='
        },
        {
            text: '大于',
            value: '>'
        },
        {
            text: '小于',
            value: '<'
        },
        {
            text: '大于等于',
            value: '>='
        },
        {
            text: '小于等于',
            value: '<='
        }]
    };
};

// 根据数据类型获取判断空方式
var getVerifyEmpty = function(dataType) {
    return dataType == 'String' ? {
        selected: 'StringUtils.isNotBlank(param)',
        options: [{
            text: 'isNotBlank',
            value: 'StringUtils.isNotBlank(param)'
        },
        {
            text: 'isNotEmpty',
            value: 'StringUtils.isNotEmpty(param)'
        },
        {
            text: '!= null',
            value: 'param != null'
        }]
    }: {
        selected: 'param != null && param > 0',
        options: [{
            text: '!= null && > 0',
            value: 'param != null && param > 0'
        },
        {
            text: '!= null',
            value: 'param != null'
        }]
    };
};

// 初始化默认参数配置
var initParameter = {
    "name": "",
    "appendSql": "",
    "dataType": {
        selected: 'String',
        options: [{
            text: 'String',
            value: 'String'
        },
        {
            text: 'Long',
            value: 'Long'
        },
        {
            text: 'Integer',
            value: 'Integer'
        }]
    },
    "matchType": getMatchType("String"),
    "verifyEmpty": getVerifyEmpty("String")
};

// vue 基本配置
var config = new Vue({
    el: '#config',
    data: {
        assignmentType: {
            selected: 1,
            options: [{
                text: '命名参数',
                value: 1
            },
            {
                text: '占位符',
                value: 2
            }]
        },
        frame: {
            selected: 'springjdbc',
            options: [{
                text: 'springjdbc',
                value: 'springjdbc'
            },
            {
                text: 'hibernate',
                value: 'hibernate'
            }]
        },
        initSql: "",
        returnType: "Object"
    }
});

// vue 参数列表
var params = new Vue({
    el: '#parameters',
    data: {
        parameters: [],
        sql: ""
    },
    methods: {
        del: function(index) {
            if (this.parameters.length == 1) {
                layer.msg('参数至少要有一个');
            } else {
                this.parameters.splice(index, 1);
            }
        },
        add: function() {
            var newObject = $.extend(true, {},
            initParameter);
            this.parameters.push(newObject);
        },
        changeAppendSql: function(index) {
            var name = this.parameters[index].name.trim();
            var assignmentType = config.assignmentType.selected;
            var matchType = this.parameters[index].matchType.selected;
            this.parameters[index].appendSql = name ? "AND " + name + " " + matchType + (assignmentType == 1 ? ":" + name: "?") : "";
        },
        changeDataType: function(index) {
            var dataType = this.parameters[index].dataType.selected;
            this.parameters[index].matchType = getMatchType(dataType);
            this.parameters[index].verifyEmpty = getVerifyEmpty(dataType);
        },
        reset: function() {
            this.parameters = [];
            this.parameters.push($.extend(true, {},
            initParameter));
        },
        joinSql: function() {
            this.sql = doJoinSql();
        }
    }
});
params.parameters.push($.extend(true, {},initParameter));

// 拼接sql
var doJoinSql = function() {
    var frame = config.frame.selected;
    var assignmentType = config.assignmentType.selected;
    var initSql = config.initSql;
    var returnType = config.returnType.trim();
    var errorMsg = ""; // 错误信息
    var sqlName = frame == "springjdbc" ? "sql": "hql"; // sql变量名字
    var nameArray = []; // name数组集
    var sql = "";
    sql += "StringBuilder " + sqlName + " = new StringBuilder();"
    if (assignmentType == 1) {
        sql += "Map<String, Object> parameters = new HashMap<String, Object>();"
    } else {
        sql += "List<Object> parameters = new ArrayList<Object>();"
    }
    var sqlArray = initSql.split("\n");
    sqlArray.forEach(function(e) {
        if (StringUtils.isNotBlank(e)) {
            sql += sqlName + '.append(" ' + e + ' ");';
        }
    });
    params.parameters.forEach(function(parameter, index) {
        var name = parameter.name.trim();
        var dataType = parameter.dataType.selected;
        var verifyEmpty = parameter.verifyEmpty.selected;
        var appendSql = parameter.appendSql.trim();
        var matchType = parameter.matchType.selected;
        nameArray.push(name);
        if (!errorMsg) {
            if (!name) {
                errorMsg = "名称不能为空";
            } else if (matchType == 'like' && dataType != "String") {
                errorMsg = "模糊匹配的参数类型必须是String";
            } else if (ArrayUtils.isRepeat(nameArray)) {
                errorMsg = "名称重复";
            } else if (assignmentType == 1 && appendSql.indexOf("?") > -1) {
                errorMsg = "赋值方式为命名参数，appendSql不允许包含字符 ? ";
            } else if (assignmentType == 2 && appendSql.indexOf(":") > -1) {
                errorMsg = "赋值方式为占位符，appendSql不允许包含字符 : ";
            }
        }
        if (errorMsg && errorMsg.indexOf("条数据不符合规则") == -1) {
            errorMsg += ",第" + (index + 1) + "条数据不符合规则"
        }
        // 获取变量值
        sql += dataType + " " + name + " = MapUtils.get" + dataType + "(params, \"" + name + "\");";
        // 变量判断是否为空
        sql += "if (" + verifyEmpty.replace(/param/g, name) + ") {";
        // 拼接sql
        if (!appendSql) {
            appendSql = "AND " + name + " " + matchType + ":" + name;
        }
        if (appendSql.indexOf("and") != 0 && appendSql.indexOf("AND") != 0) {
            appendSql += "AND ";
        }
        if (frame == "springjdbc") {
            sql += "  sql.append(\" " + appendSql + " \");";
        } else {
            sql += "  hql.append(\" " + appendSql + " \");";
        }

        likeName = matchType == 'like' ? "Common.ConvertLikeParamer(" + name + ")": name;
        // 参数赋值
        if (assignmentType == 1) {
            sql += "  parameters.put(\"" + name + "\", " + likeName + ");";
        } else {
            sql += "  parameters.add(" + likeName + ");";
        }
        sql += "}";
    });
    if (errorMsg) {
        layer.msg(errorMsg);
        return errorMsg;
    }
    // 执行sql 返回结果
    sql += "List<" + returnType + "> lists = ";
    if (frame == "springjdbc" && assignmentType == 1) {
        sql += "this.getNamedParameterJdbcTemplate().queryForList(" + sqlName + ".toString(), parameters);";
    } else if (frame == "springjdbc" && assignmentType == 2) {
        sql += "this.getJdbcTemplate().queryForList(" + sqlName + ".toString(), parameters.toArray());";
    } else if (frame == "hibernate" && assignmentType == 1) {
        sql += "hdao.find(" + sqlName + ".toString(), parameters);";
    } else if (frame == "hibernate" && assignmentType == 2) {
        sql += "hdao.find(" + sqlName + ".toString(), parameters.toArray());";
    }
    return sql.replace(/;/g, ";\n").replace(/{/g, "{\n").replace(/}/g, "}\n");
}