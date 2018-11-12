<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>SMART Viewer</title>
		<script>
		window.onload = function()
		{
			document.graphform1.fieldselect.selectedIndex = 1;
		};
		</script>
	</head>

	<body>
		<h1>SMART Viewer - <s:property value='targetName' /></h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<h2>モデル情報</h2>
			<table>
			<tr><th>Model</th><td><s:property value="modelName" /></td>
			<tr><th>Serial</th><td><s:property value="serialNumber" /></td>
			<tr><th>Firmware</th><td><s:property value="firmwareVersion" /></td>
			</table>

			<h2>最新の属性値</h2>
			<br>
			<s:form action="currentlist" theme="simple">
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:submit value="最新の属性値" />
			</s:form>

			<h2>故障予測</h2>
			<br>
			<s:form action="predictfailure" theme="simple">
				powerOnHoursID：<input type="text" name="powerOnHoursId" value="9" size="3">
				valueID：<input type="text" name="valueId" value="9" size="3">
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:submit value="故障予測" />
			</s:form>
			<s:form action="usagestatistics" theme="simple">
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<input type="checkbox" name="interpolateHour" checked>時間補完する
				<input type="checkbox" name="includeZeroHour" checked>０時間を含める
				<s:submit value="PC使用状況の統計情報表示" />
			</s:form>

			<h2>ドライブサイズ</h2>
			<br>
			<s:form action="drivesizegraph" theme="simple" name="graphform0">
				<s:select name="graphType" list="#{ 'HighCharts':'HighCharts', 'SVG':'SVG' }" />
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:select name="filenumlimit" list="#{ '50':'50', '100':'100', '200':'200', '300':'300' }" />日分
				<s:submit value="ドライブサイズの遷移" />
			</s:form>

			<h2>属性選択</h2>
			<br>
			<s:form action="chronologygraph" theme="simple" name="graphform1">
				<input type="hidden" name="mode" value="specifyid">
				<s:select name="ids" list="attributes" multiple="true" size="10" value="9" />
				<br>
				<s:select name="graphType" list="#{ 'HighCharts':'HighCharts', 'SVG':'SVG' }" />
				<s:select name="field" list="#{ 'current':'カレント値', 'raw':'RAW値' , 'raw2':'RAW値2バイト' }" id="fieldselect" />
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:select name="filenumlimit" list="#{ '50':'50', '100':'100', '200':'200', '300':'300' }" />日分
				<input type="checkbox" name="scaling">スケールを合わせる
				<s:submit value="属性値の遷移" />
			</s:form>

			<h2>上昇・下降する値のみ</h2>
			<br>
			<s:form action="chronologygraph" theme="simple" name="graphform2">
				<s:select name="mode" list="#{ 'ascending':'上昇', 'descending':'下降・変化なし' }" />
				<s:select name="graphType" list="#{ 'HighCharts':'HighCharts', 'SVG':'SVG' }" />
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:select name="filenumlimit" list="#{ '50':'50', '100':'100', '200':'200', '300':'300' }" />日分
				<input type="checkbox" name="scaling" checked>スケールを合わせる
				<s:submit value="属性値の遷移" />
			</s:form>

			<h2>RAWビットカウント</h2>
			<br>
			<s:form action="smartrawbitcount" theme="simple">
				<input type="hidden" name="targetName" value="<s:property value='targetName' />">
				<s:submit />
			</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
