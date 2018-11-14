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
		<h1>SMART Viewer - <s:property value='targetName' /></h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<h2><s:property value="datetime" /> SMART RAW値ビット集計</h2>
			<s:iterator value="rawBitCount">
				<div id="chart_<s:property value='id' />" style="width:800px; height:200px"></div>
				<script type="text/javascript">
				function draw()
				{
					Highcharts.setOptions({ global: { useUTC: false } });
					options =
					{
						chart: {type: 'column', renderTo: "chart_<s:property value='id' />", plotBackgroundColor: 'lightgray'},
						title: {text: <s:property value='idAndName' />},
						legend: {enabled:false},
						xAxis: {tickInterval:8},
						yAxis: {title:null},
						series: [ <s:property value='pointList' /> ]
					};
					chart = new Highcharts.Chart(options);
				};
				document.body.onload = draw();
				</script>
			</s:iterator>

		</div>
		</div>
		</div>

	</body>
</html>
