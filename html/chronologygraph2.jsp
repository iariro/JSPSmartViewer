<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script type="text/javascript" src="http://code.highcharts.com/highcharts.js"></script>
		<title>SMART Viewer</title>
	</head>

	<body>
		<s:iterator value="chartPointLists">
			<div id="chart_<s:property value='key' />" style="width:1300px; height:700px"></div>
			<script type="text/javascript">
			function draw()
			{
				Highcharts.setOptions({ global: { useUTC: false } });
				options =
				{
					chart: {renderTo: "chart_<s:property value='key' />", zoomType:'xy', plotBackgroundColor: 'lightgray'},
					title: {text: 'S.M.A.R.T. - <s:property value='targetName' /> <s:property value='key' />'},
					xAxis: {title: null, type: 'datetime'},
					yAxis: {allowDecimals: false },
					plotOptions: {series:{marker:{enabled:true}}},
					<s:if test='%{mode.equals("ascending") && scaling != null}'>yAxis: {max: 1000},</s:if>
					series: [ <s:property value='value' /> ]
				};
				chart = new Highcharts.Chart(options);
			};
			document.body.onload = draw();
			</script>
		</s:iterator>
	</body>
</html>
