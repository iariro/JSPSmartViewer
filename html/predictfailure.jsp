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
		<div class=day>


			<h2>故障予測結果表示</h2>
			<table>
				<tr>
					<th>基準時間</th>
					<th>基準値</th>
					<th>現在時間</th>
					<th>現在値</th>
					<th>残り時間</th>
					<th>予測故障日</th>
				</tr>
				<s:iterator value="predictFailure">
					<tr>
						<td align="right"><s:property value="time1" /></td>
						<td align="right"><s:property value="value1" /></td>
						<td align="right"><s:property value="time2" /></td>
						<td align="right"><s:property value="value2" /></td>
						<td align="right"><s:property value="remainingHour" /></td>
						<td align="right"><s:property value="deadDate" /></td>
					</tr>
				</s:iterator>
			</table>

		</div>
		</div>
		</div>

	</body>
</html>
