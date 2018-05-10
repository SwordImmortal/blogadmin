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
var getVerifyEmptyType = function(dataType) {
    return dataType == 'String' ? {
        selected: 1,
        options: [{
            text: '不为空白',
            value: 1
        },
        {
            text: '不为空',
            value: 2
        },
        {
            text: '不为null',
            value: 3
        }]
    }: {
        selected: 4,
        options: [{
            text: '不为null并且大于0',
            value: 4
        },
        {
            text: '不为null',
            value: 3
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
    "verifyEmpty": getVerifyEmptyType("String")
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
            selected: 'mybatis',
            options: [{
                text: 'springjdbc',
                value: 'springjdbc'
            },
            {
                text: 'hibernate',
                value: 'hibernate'
            },
            {
                text: 'mybatis',
                value: 'mybatis'
            }]
        },
        initSql: "",
        mapperId: "",
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
        	let name = this.parameters[index].name.trim();
        	let assignmentType = config.assignmentType.selected;
        	let matchType = this.parameters[index].matchType.selected;
            let isMybatis = (config.frame.selected == "mybatis");
            let appendSql = "";
            if(!name){
            	
            }else if(isMybatis){
            	if(matchType == "like"){
            		appendSql = "CONCAT('%',#{" + name+ "},'%')";
            	}else{
            		appendSql = "#{" + name+ "}";
            	}
            }else if(assignmentType == 1){
            	appendSql = ":" + name;
            }else {
            	appendSql = "?";
            }
            this.parameters[index].appendSql = name?("AND " + name + " " + matchType + " " + appendSql):"";
        },
        changeDataType: function(index) {
            var dataType = this.parameters[index].dataType.selected;
            this.parameters[index].matchType = getMatchType(dataType);
            this.parameters[index].verifyEmpty = getVerifyEmptyType(dataType);
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
params.parameters.push($.extend(true, {},
initParameter));

/**
 * 获取判断条件
 * @param    {string}  frame	框架
 * @param    {number}   verifyEmptyType  判空类型
 * @param    {string}  name   字段名称
 */
function getVerify(frame, verifyEmptyType, name) {
    let result = "";
    if (frame == "springjdbc" || frame == "hibernate") {
        switch (verifyEmptyType) {
        case 1:
            result = "StringUtils.isBlank(param)";break;
        case 2:
            result = "StringUtils.isEmpty(param)";break;
        case 3:
            result = "param != null";break;
        case 4:
            result = "param != null && param > 0";break;
        default:
            result = "";
        }
        result = "if(" + result + ") {";
    } else if (frame == "mybatis") {
        switch (verifyEmptyType) {
        case 1:
            result = "'param != null and .trim() != \"\"'";break;
        case 2:
            result = "param != null and param !=''";break;
        case 3:
            result = "param != null";break;
        case 4:
            result = "param != null and param > 0";break;
        default:
            result = "";
        }
        if (result.indexOf("'") != 0) {
            result = '"' + result + '"';
        }
        result = "\t<if test=" + result + ">";
    }
    return result.replace(/param/g, name);
}
// 拼接sql
var doJoinSql = function() {
    let frame = config.frame.selected;
    let isMybatis = (frame == "mybatis");
    let assignmentType = config.assignmentType.selected;
    let initSql = config.initSql;
    let returnType = config.returnType.trim();
    let mapperId = config.mapperId.trim();
    let errorMsg = "";
    let sqlName = frame == "springjdbc" ? "sql": "hql"; // sql变量名字
    let nameArray = []; // name数组集
    let sql = [];
    if (isMybatis) {
        sql.push("<select id=\"" + mapperId + "\" parameterType=\"java.util.Map\" resultType=\"" + returnType + "\">");
    } else {
        sql.push("StringBuilder " + sqlName + " = new StringBuilder();");
        if (assignmentType == 1) {
            sql.push("Map<String, Object> parameters = new HashMap<String, Object>();");
        } else {
            sql.push("List<Object> parameters = new ArrayList<Object>();)");
        }
    }
    var sqlArray = initSql.split("\n");
    sqlArray.forEach(function(e) {
        if (StringUtils.isNotBlank(e)) {
            if (isMybatis) {
                sql.push("\t" + e);
            } else {
                sql.push(sqlName + '.append(" ' + e + ' ");');
            }
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
            errorMsg += ",第" + (index + 1) + "条数据不符合规则";
        }
        if (!isMybatis) {
            // 获取变量值
            sql.push(dataType + " " + name + " = MapUtils.get" + dataType + "(params, \"" + name + "\");");
        }
        // 变量判断是否为空
        sql.push(getVerify(frame, verifyEmpty, name));
        if (isMybatis) {
            sql.push("\t\t" + appendSql);
        } else if (frame == "springjdbc") {
            sql.push("  sql.append(\" " + appendSql + " \");");
        } else {
            sql.push("  hql.append(\" " + appendSql + " \");");
        }
        if (isMybatis) {
       	 sql.push("\t</if>");
       }else{
            likeName = matchType == 'like' ? "Common.ConvertLikeParamer(" + name + ")": name;
            // 参数赋值
            if (assignmentType == 1) {
                sql.push("  parameters.put(\"" + name + "\", " + likeName + ");");
            } else {
                sql.push("  parameters.add(" + likeName + ");");
            }
            sql.push("}");
        }

    });
    if (errorMsg) {
        layer.msg(errorMsg);
        return errorMsg;
    }
    let resultSql = "";
    if (isMybatis) {
    	resultSql +="</select>";
    }else{
        // 执行sql 返回结果
    	resultSql +="List<" + returnType + "> lists = ";
        if (frame == "springjdbc" && assignmentType == 1) {
        	resultSql +="this.getNamedParameterJdbcTemplate().queryForList(" + sqlName + ".toString(), parameters);";
        } else if (frame == "springjdbc" && assignmentType == 2) {
        	resultSql +="this.getJdbcTemplate().queryForList(" + sqlName + ".toString(), parameters.toArray());";
        } else if (frame == "hibernate" && assignmentType == 1) {
            resultSql +="hdao.find(" + sqlName + ".toString(), parameters);";
        } else if (frame == "hibernate" && assignmentType == 2) {
        	resultSql +="hdao.find(" + sqlName + ".toString(), parameters.toArray());";
        }	
    }
    sql.push(resultSql);
    return sql.join("\n")
}