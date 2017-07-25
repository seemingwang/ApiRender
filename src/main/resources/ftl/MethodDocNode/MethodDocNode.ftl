<div class = "${getCssName("MethodDocNode")}  ${getCssName("clearFix")}">
	<p>注解</p>
	<#assign l = getAnnotationList()>
	<#assign comment = getM().getRawCommentText()>
	<#assign path = getBasePath()>
	<div>Path:${path}</div>
	<#list l as c>
		
		<#if c.annotationType() != "Path">
		<div class="${getCssName("annotationWrapper")}">
			${c.html}
		</div>
		</#if>
	</#list>
	<div  style="width:100%">
		<div>函数体</div>
    	<p>${comment}</p>
		<div>${m}</div>
	</div>

	<#if getParams()??>
		<#if (getParams()?size>0) >
			<p>请求参数</p>
			<#list getParams() as p>
				${p.html}
			</#list>
		</#if>
	</#if>
	<p>返回值</p>
	${getField("return")}
</div>
