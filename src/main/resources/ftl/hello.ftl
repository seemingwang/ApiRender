<html>
  <head>
        <title>freemarker测试</title>
        <style>
        	#pp h1 {
        		color: blue
        	}
#pp .clearFix:after{
 clear:both;
 display:block;
 visibility:hidden;
 height:0;
 line-height:0;
 content:'';

}
#pp .clearFix{zoom:1;}
#pp .tt span {
	color:red;
}
        </style>
    </head>
    <body id="pp">
    	<#assign e = obj.getTestString("ak47")>
    	<span>${e}</span>
 	<#if root ??>
        <#assign classList = root.classes()>
        <#list classList as class>
        	<div class="tt">
        		<h1>${class}</h1>
        		<span>welcome</span>
        		<#assign methodList = class.methods()>
        		<#list methodList as method>
        				<div>
        					${method}
        				</div>
        			<div>
        				<#assign params = method.tags("param")>
        				<div class="clearFix">
        					<div style="float:left;margin-right:120px">
        						需要参数 
        					</div> 
        					<div style="float:left">
        					<#list params as p>
        						<#assign text = p.text()>
        						<div>
        							<span>${text}</span>
        						</div>
        					</#list>
        					</div>

        				</div>
        				<div class="clearFix">
							<div style="float:left;margin-right:120px">
        						返回值
        					</div>
        					<#assign return = method.tags("return")>
        					<#if (return ? size > 0)>
        						<#assign returnV = return[0].text()>
        						<div style= "float:left">
        							${returnV}
        						</div> 
        					</#if>
        				</div>
        			</div>
        		</#list> 
        	</div>
        </#list>
    </#if>
    </body>
</html>