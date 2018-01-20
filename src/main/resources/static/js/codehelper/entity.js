//vue tools
var tools = new Vue({
    el: '#tools',
    data: {
        oldVal: "",
        newVal: ""
    },
    methods: {
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
                    hibernateAnnotation: $("#hibernateAnnotation").is(':checked'),
                    doradoAnnotation: $("#doradoAnnotation").is(':checked'),
                    fieldAnnotation: $("#fieldAnnotation").is(':checked'),
                    baseEntity: $("#baseEntity").is(':checked')
                });
            }
        }
    }
});
