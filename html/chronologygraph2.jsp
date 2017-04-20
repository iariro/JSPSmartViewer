<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script src="//code.highcharts.com/highcharts.js"></script>
		<title>SMART Viewer</title>
	</head>

	<body>
		<div id="chart" style="width:800px; height:600px"></div>
		<script type="text/javascript">
		function draw()
		{
			options =
			{
				chart: {renderTo: 'chart', zoomType:'xy'},
				title: {text: 'S.M.A.R.T.'},
				xAxis: {title: null, type: 'datetime'},
				series: [ <s:property value="chartPointLists" /> ]
			};
			chart = new Highcharts.Chart(options);
		};
		document.body.onload = draw();
		</script>
	</body>
</html>
