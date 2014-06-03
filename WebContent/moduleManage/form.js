serializeObject = function(form){
	/**
	 * 将form表单元素的值序列化成对象
	 * 这个方法很重要，
	 */
	var o = {};
	$.each(form.serializeArray(),function(index){
		if(o[this['name']]){
			o[this['name']]=o[this['name']]+","+this['value'];
		}else{
			o[this['name']]=this['value'];
		}
	});
	return o;
};