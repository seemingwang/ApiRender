<div class = "${getCssName("AnnotationDescNode")}">
	<#assign type = annotationType()>
	<#if type == "POST" >
		 请求方法${type}
	<#elseif type == "GET">
		 请求方法${type}
	<#elseif type == "Path">
		 路径:${value()}
	<#elseif type == "Consumes">
		 接收类型:${value()}
	<#elseif type == "Produces">
		  返回类型:${value()}
	<#else>
	<h1>注解名${annotationType()}</h1>
	<h1>注解值${value()}</h1>
	</#if>
</div>