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
            var newObj = className + " " + objectName + " = new " + className + "();\n";
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
        // json 转 Java
        jsonToJava: function() {
            if (!isJSON(this.oldVal)) {
                layer.msg("参数格式有误，必须是json格式");
                return;
            }
            var javaStr = "";
            var getObjStr = function(name, obj) {
                javaStr += "\n";
                javaStr += "Map<String,Object> " + name + " = Maps.newHashMap();";
                var putMap = function(key, value) {
                    javaStr += name + '.put("' + key + '",' + value + ');';
                }
                for (var key in obj) {
                    var value = obj[key];
                    if (typeof value == "object") {
                        if (isArray(value)) {
                            getObjArray(key, value);
                            putMap(key, key);
                        } else {
                            getObjStr(key, value);
                            putMap(key, key);
                        }
                    } else if (isString(value)) {
                        putMap(key, '"' + value + '"');
                    } else {
                        putMap(key, value);
                    }
                }
            };

            var getObjArray = function(name, array) {
                javaStr += "\n";
                javaStr += "List<Object> " + name + " = Lists.newArrayList();";
                var i = 1;
                array.forEach(function(item) {
                    if (typeof item == "object") {
                        var separate = "Map";
                        if (isArray(item)) {
                            separate = "List";
                        }
                        var mapName = name + separate;
                        if (array.length != 1) {
                            mapName += i++;
                        }
                        if (isArray(item)) {
                            getObjArray(mapName, item);
                        } else {
                            getObjStr(mapName, item);
                        }
                        javaStr += name + ".add(" + mapName + ");";

                    } else if (isString(value)) {
                        javaStr += name + '.add("' + item + '");';
                    } else {
                        javaStr += name + ".add(" + item + ");";
                    }
                });
            };

            var str = JSON.parse(this.oldVal);
            if (isArray(str)) {
                getObjArray("list", str);
            } else {
                getObjStr("map", str);
            }
            this.newVal = javaStr.replace(/;/g, ";\n");
        }
    }
});