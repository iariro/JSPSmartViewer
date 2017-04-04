package kumagai.smartviewer;

import java.awt.*;
import java.text.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import ktool.xml.*;

public class SmartGraphDocument
	extends KDocument
{
	static private final String fontFamily = "MS-Mincho";
	static private final Point origin = new Point(70, 20);
	static private final int xmargin = 10;

	public SmartGraphDocument(SmartGraphDocumentPointList documentPointList)
		throws ParserConfigurationException,
		TransformerConfigurationException,
		TransformerFactoryConfigurationError,
		ParseException
	{
		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		Element element = createElement("title");
		top.appendChild(element);
		element.appendChild(createTextNode("SMARTグラフ"));

		element = createElement("text");
		element.setAttribute(
			"x",
			String.valueOf(origin.x + 5));
		element.setAttribute(
			"y",
			String.valueOf(origin.y + 20));
		element.setAttribute("font-family", fontFamily);
		element.appendChild(
			createTextNode("SMARTグラフ"));
		top.appendChild(element);

		// 枠線
		element = createElement("rect");
		element.setAttribute
			("x", String.valueOf(origin.x - xmargin));
		element.setAttribute
			("y", String.valueOf(origin.y));
		element.setAttribute
			("width", String.valueOf(documentPointList.scaleX * documentPointList.hourRange + xmargin * 2));
		element.setAttribute
			("height", String.valueOf(documentPointList.scaleY * documentPointList.maxY));
		element.setAttribute
			("fill", "#dddddd");
		element.setAttribute
			("stroke", "black");
		top.appendChild(element);

		String pointsString = new String();

		for (SmartGraphDocumentPoint point : documentPointList)
		{
			// 折れ線の頂点
			pointsString +=
				String.format("%d %d", origin.x + point.x, origin.y + point.y);

			// 頂点のマーク
			element = createElement("rect");
			element.setAttribute(
				"x",
				String.valueOf(origin.x + point.x - 2));
			element.setAttribute("y", String.valueOf(point.y - 2));
			element.setAttribute("width", "4");
			element.setAttribute("height", "4");
			element.setAttribute("fill", "blue");
			element.setAttribute("stroke", "blue");
			top.appendChild(element);
		}

		// 折れ線描画
		element = createElement("polyline");
		element.setAttribute("points", pointsString);
		element.setAttribute("stroke", "blue");
		element.setAttribute("fill", "none");
		element.setAttribute("stroke-width", "2");
		top.appendChild(element);		
	}
}
