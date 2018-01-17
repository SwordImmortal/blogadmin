var StringUtils = {
	isBlank : function(str) {
		return !str || str.trim().length == 0;
	},
	isNotBlank : function(str) {
		return str && str.trim().length != 0;
	},
	isString : function(obj) { // 判断对象是否是字符串
		return Object.prototype.toString.call(obj) === "[object String]";
	}
}
var ArrayUtils = {
	// 判断数组是否有重复
	isRepeat : function(arr) {
		var hash = {};
		for ( var i in arr) {
			if (hash[arr[i]])
				return true;
			hash[arr[i]] = true;
		}
		return false;
	}
}

// 获取一个字符串中一个单词的下一个单词
var getNextWord = function(str, word) {
	str = str.substring(str.indexOf(word) + word.length).trim();
	var nextWord = "";
	for (var i = 0; i < str.length; i++) {
		if (str.charAt(i) == " " || str.charAt(i) == ";") {
			break;
		}
		nextWord += str.charAt(i);
	}
	return nextWord;
}
var request = {
	getString : function ajaxback(url, data, callback) {
		$.ajax({
			type : "post",
			// dataType : "text",
			url : url,
			data : data,
			success : function(result) {
				callback ? callback(result) : $("#newVal").val(result);
			},
			error : function() {
				layer.msg("服务器内部错误！");
			}
		});
	},
	getJson : function ajaxback(url, data, callback) {
		$.ajax({
			type : "post",
			dataType : "json",
			url : url,
			data : data,
			success : function(result) {
				callback ? callback(result) : $("#newVal").val(result);
			}
		});
	}
}
