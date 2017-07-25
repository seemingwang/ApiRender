<div class = "${getCssName("ClassDisplayNode")}">
	<#if getField("comment") != "">
		<p style="color:blue">//${getField("comment")}</p>
	</#if>
	<#if getField("final") == "true">
		<#if getField("fieldName") != "">
			${getField("fieldName")}(${getField("name")})
		<#else>
			${getField("name")}
		</#if>
	<#else>
		<#if getField("fieldName") != "">
			${getField("fieldName")}(${getField("name")})
		<#else>
			${getField("name")}
		</#if>
		<div>
		<div>${getField("startWrapper")}</div>
		<#list children as c>
			<div style="margin-left:30px">
				${c.html}
			</div>
			<#if c_has_next>
				<div style="margin-left:30px">
				${getField("separator")}
				</div>
			</#if>
		</#list>
		<div>${getField("endWrapper")}</div>
		</div>
	</#if>
</div>