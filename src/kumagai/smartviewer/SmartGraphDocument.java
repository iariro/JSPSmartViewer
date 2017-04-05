package kumagai.smartviewer;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import ktool.datetime.*;
import ktool.xml.*;

public class SmartGraphDocument
	extends KDocument
{
	static private final String fontFamily = "MS-Mincho";
	static private final Point origin = new Point(70, 30);
	static private final int xmargin = 10;
	
	public static void main(String[] args)
		throws ParserConfigurationException, TransformerFactoryConfigurationError, ParseException, IOException, TransformerException
	{
		String smartFilePath = "C:\\temp\\smart\\";
		
		SmartDataList points = new SmartDataList();
		String [] filenames = new File(smartFilePath).list();
		for (String filename : filenames)
		{
			File file = new File(smartFilePath, filename);
			FileInputStream stream = new FileInputStream(file);
			int size = (int)file.length();
			byte [] data = new byte [size];
			stream.read(data);
			points.addAll(new SmartDataList(data));
		}

		SmartGraphDocumentPointList smartGraphDocumentPointList =
			points.getSmartGraphDocumentPointList(new Dimension(800, 580), 5);
		ArrayList<SmartGraphDocumentPointList> smartGraphDocumentPointLists = new ArrayList<SmartGraphDocumentPointList>();
		smartGraphDocumentPointLists.add(smartGraphDocumentPointList);
		SmartGraphDocument document = new SmartGraphDocument(smartGraphDocumentPointLists);
		FileOutputStream stream = new FileOutputStream("../out.svg");
		
		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		
		document.write(transformer, new OutputStreamWriter(stream));

		stream.close();
	}

	public SmartGraphDocument(ArrayList<SmartGraphDocumentPointList> documentPointLists)
		throws ParserConfigurationException,
		TransformerConfigurationException,
		TransformerFactoryConfigurationError,
		ParseException
	{
		SmartGraphDocumentPointList documentPointList = null;
		
		if (documentPointLists.size() > 0)
		{
			documentPointList = documentPointLists.get(0);
		}

		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		Element element = createElement("title");
		top.appendChild(element);
		element.appendChild(createTextNode("SMARTグラフ"));

		element = createElement("text");
		element.setAttribute(
			"x",
			String.valueOf(origin.x));
		element.setAttribute(
			"y",
			String.valueOf(origin.y - 5));
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

		// 縦軸目盛
		for (int i=0 ; i<=documentPointList.maxY ; i+=10)
		{
			element = createElement("line");
			element.setAttribute(
				"x1",
				String.valueOf(origin.x - xmargin - (i % 50 == 0 ? 10 : 5)));
			element.setAttribute(
				"y1",
				String.valueOf(origin.y + documentPointList.scaleY * (documentPointList.maxY - i)));
			element.setAttribute(
				"x2",
				String.valueOf(origin.x - xmargin));
			element.setAttribute(
				"y2",
				String.valueOf(origin.y + documentPointList.scaleY * (documentPointList.maxY - i)));
			element.setAttribute("stroke", "black");
			top.appendChild(element);

			if (i % 50 == 0)
			{
				// 縦軸ラベル表示のタイミングである

				element = createElement("text");
				element.setAttribute(
					"x",
					String.valueOf(origin.x - xmargin - 15));
				element.setAttribute(
					"y",
					String.valueOf(origin.y + documentPointList.scaleY * (documentPointList.maxY - i) + 5));
				element.setAttribute("font-family", fontFamily);
				element.setAttribute("text-anchor", "end");
				element.appendChild(
					createTextNode(String.valueOf(i)));
				top.appendChild(element);
			}
		}
		
		for (DateTime date=new DateTime(documentPointList.minHour) ;
			date.compareTo(documentPointList.maxHour)<0 ;
			date.add(1))
		{
			if (date.compareTo(documentPointList.maxHour) < 0)
			{
				if (date.getMinute() == 0 && date.getSecond() == 0)
				{
					int diffDay = date.diff(documentPointList.minHour).getHour();

					int tickLength = 5;
					if (date.getHour() == 0)
					{
						tickLength = 15;
					}

					element = createElement("line");
					element.setAttribute(
						"x1",
						String.valueOf(origin.x + documentPointList.scaleX * diffDay));
					element.setAttribute(
						"y1",
						String.valueOf(origin.y + documentPointList.scaleY * documentPointList.maxY));
					element.setAttribute(
						"x2",
						String.valueOf(origin.x + documentPointList.scaleX * diffDay));
					element.setAttribute(
						"y2",
						String.valueOf(origin.y + documentPointList.scaleY * documentPointList.maxY + tickLength));
					element.setAttribute("stroke", "black");
					top.appendChild(element);
					
					if (date.getHour() == 0)
					{
						String dateText;

						if (diffDay == 0 || date.getMonth() == 1)
						{
							// 先頭または月初め

							dateText =
								String.format("%d/%02d/%02d", date.getYear(), date.getMonth(), date.getDay());
						}
						else
						{
							// 先頭でも月初めでもない

							dateText =
								String.format("%02d/%02d", date.getMonth(), date.getDay());
						}

						element = createElement("text");
						element.setAttribute(
							"x",
							String.valueOf(origin.x + documentPointList.scaleX * diffDay));
						element.setAttribute(
							"y",
							String.valueOf(origin.y + documentPointList.scaleY * documentPointList.maxY + 30));
						element.setAttribute("font-family", fontFamily);
						element.setAttribute("text-anchor", "middle");
						element.appendChild(createTextNode(dateText));
						top.appendChild(element);
					}
				}
			}
		}

		String [] colors = new String [] { "blue", "red", "green", "purple" };
		int colorIndex = 0;
		int index = 0;

		for (SmartGraphDocumentPointList documentPointList2 : documentPointLists)
		{
			String color = colors[colorIndex];
			index++;
			colorIndex = (colorIndex + 1) % colors.length;

			// 凡例
			element = createElement("line");
			element.setAttribute(
				"x1",
				String.valueOf(origin.x + 10));
			element.setAttribute(
				"y1",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.maxY - 20 * index));
			element.setAttribute(
				"x2",
				String.valueOf(origin.x + 50));
			element.setAttribute(
				"y2",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.maxY - 20 * index));
			element.setAttribute("stroke", color);
			top.appendChild(element);

			element = createElement("text");
			element.setAttribute(
				"x",
				String.valueOf(origin.x + 60));
			element.setAttribute(
				"y",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.maxY - 20 * index + 5));
			element.setAttribute("font-family", fontFamily);
			element.appendChild(createTextNode(String.valueOf(documentPointList2.attributeId)));
			top.appendChild(element);
			
			String pointsString = new String();

			for (SmartGraphDocumentPoint point : documentPointList2)
			{
				if (pointsString.length() > 0)
				{
					pointsString += ", ";
				}

				// 折れ線の頂点
				pointsString +=
					String.format("%d %d", origin.x + point.x, origin.y + point.y);

				// 頂点のマーク
				element = createElement("rect");
				element.setAttribute(
					"x",
					String.valueOf(origin.x + point.x - 2));
				element.setAttribute("y", String.valueOf(origin.y + point.y - 2));
				element.setAttribute("width", "4");
				element.setAttribute("height", "4");
				element.setAttribute("fill", color);
				element.setAttribute("stroke", color);
				top.appendChild(element);
			}

			// 折れ線描画
			element = createElement("polyline");
			element.setAttribute("points", pointsString);
			element.setAttribute("stroke", color);
			element.setAttribute("fill", "none");
			element.setAttribute("stroke-width", "2");
			top.appendChild(element);
		}
	}
}
