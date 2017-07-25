<html>
<head>
<style>
	${css}
</style>
</head>
</html>
<body>
<div>
	<div class="${getCssName("clearFix")}" style="width:100%">
	<#list children as c>
		${c.html}
	</#list>
	</div>
</div>
</body>