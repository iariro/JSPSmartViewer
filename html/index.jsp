<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>SMART Viewer</title>
	</head>

	<body>
		<h1>SMART Viewer</h1>

		<div class=hatena-body>
		<div class=main>

		<s:iterator value="targets">
			<div class=day>
				<h2><span class=title><s:property value="name" /></span></h2>
				<div class=body>
				<div class=section>
					<table>
						<tr><th>type</th><td><s:property value="type" /></td></tr>
						<tr><th>path</th><td><s:property value="path" /></td></tr>
					</table>
					<s:form action="disktop" theme="simple">
						<input type="hidden" name="targetName" value="<s:property value='name' />">
						<s:submit value="表示" />
					</s:form>
				</div>
				</div>
			</div>
		</s:iterator>

		</div>
		</div>

	</body>
</html>
