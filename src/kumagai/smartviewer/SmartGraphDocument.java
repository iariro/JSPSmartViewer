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

/**
 * SMART値遷移グラフSVGドキュメント
 */
public class SmartGraphDocument
	extends KDocument
{
	static private final String [] lineColors =
		new String [] { "blue", "red", "green", "purple" };

	static private final ISmartFieldGetter smartAttributeCurrent =
		new SmartAttributeCurrentGetter();
	static private final ISmartFieldGetter smartAttributeRawValue =
		new SmartAttributeRawValueGet();

	static private final String fontFamily = "MS-Mincho";
	static private final Point origin = new Point(70, 30);
	static private final int xmargin = 10;

	/**
	 * テストコード
	 * @param args [0]=属性ID／[1]=current/raw
	 * @throws ParserConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 * @throws ParseException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static void main(String[] args)
		throws ParserConfigurationException, TransformerFactoryConfigurationError,
		ParseException, IOException, TransformerException
	{
		int attributeId;
		ISmartFieldGetter smartFieldGetter;

		if (args.length >= 2)
		{
			// 引数指定あり

			attributeId = Integer.valueOf(args[1]);
			if (args[2].equals("current"))
			{
				// currentが指定された

				smartFieldGetter = smartAttributeCurrent;
			}
			else
			{
				// current以外が指定された

				smartFieldGetter = smartAttributeRawValue;
			}
		}
		else
		{
			// 引数指定なし

			attributeId = 4;
			smartFieldGetter = smartAttributeRawValue;
		}

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
			new SmartGraphDocumentPointList(
				points,
				new Dimension(800, 580),
				attributeId,
				smartFieldGetter);
		ArrayList<SmartGraphDocumentPointList> smartGraphDocumentPointLists =
			new ArrayList<SmartGraphDocumentPointList>();
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

	/**
	 * SMART値遷移グラフSVGドキュメント
	 * @param documentPointLists
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 * @throws ParseException
	 */
	public SmartGraphDocument(ArrayList<SmartGraphDocumentPointList> documentPointLists)
		throws ParserConfigurationException,
		TransformerConfigurationException,
		TransformerFactoryConfigurationError,
		ParseException
	{
		SmartGraphDocumentPointList documentPointList = null;

		if (documentPointLists.size() > 0)
		{
			// １件はある

			documentPointList = documentPointLists.get(0);
		}

		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		Element element = createElement("title");
		top.appendChild(element);
		element.appendChild(createTextNode("SMARTグラフ"));

		// タイトル
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
			("height", String.valueOf(documentPointList.scaleY * documentPointList.valueRange));
		element.setAttribute
			("fill", "#dddddd");
		element.setAttribute
			("stroke", "black");
		top.appendChild(element);

		// 縦軸目盛
		for (int i=documentPointList.minY ; i<=documentPointList.maxY ; i+=10)
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

			if (i % 10 == 0)
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

		// 横軸目盛：次の時間区切りまで行き、あとは１時間刻み
		DateTime date =
			new DateTime(
				documentPointList.minHour.getYear(),
				documentPointList.minHour.getMonth(),
				documentPointList.minHour.getDay(),
				documentPointList.minHour.getHour(),
				0,
				0);
		date.add(3600);

		for ( ;
			date.compareTo(documentPointList.maxHour)<0 ;
			date.add(3600))
		{
			int diffDay = date.diff(documentPointList.minHour).getHour();

			int tickLength = 5;
			if (date.getHour() == 0)
			{
				// 日の変わり目

				tickLength = 15;
			}

			element = createElement("line");
			element.setAttribute(
				"x1",
				String.valueOf(origin.x + documentPointList.scaleX * diffDay));
			element.setAttribute(
				"y1",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.valueRange));
			element.setAttribute(
				"x2",
				String.valueOf(origin.x + documentPointList.scaleX * diffDay));
			element.setAttribute(
				"y2",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.valueRange + tickLength));
			element.setAttribute("stroke", "black");
			top.appendChild(element);

			if (date.getHour() == 0)
			{
				// 日の変わり目

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
					String.valueOf(origin.y + documentPointList.scaleY * documentPointList.valueRange + 30));
				element.setAttribute("font-family", fontFamily);
				element.setAttribute("text-anchor", "middle");
				element.appendChild(createTextNode(dateText));
				top.appendChild(element);
			}
		}

		int colorIndex = 0;
		int index = 0;

		for (SmartGraphDocumentPointList documentPointList2 : documentPointLists)
		{
			String color = lineColors[colorIndex];
			index++;
			colorIndex = (colorIndex + 1) % lineColors.length;

			// 凡例
			element = createElement("line");
			element.setAttribute(
				"x1",
				String.valueOf(origin.x + 10));
			element.setAttribute(
				"y1",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.valueRange - 20 * index));
			element.setAttribute(
				"x2",
				String.valueOf(origin.x + 50));
			element.setAttribute(
				"y2",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.valueRange - 20 * index));
			element.setAttribute("stroke", color);
			top.appendChild(element);

			element = createElement("text");
			element.setAttribute(
				"x",
				String.valueOf(origin.x + 60));
			element.setAttribute(
				"y",
				String.valueOf(origin.y + documentPointList.scaleY * documentPointList.valueRange - 20 * index + 5));
			element.setAttribute("font-family", fontFamily);
			element.appendChild(
				createTextNode(
					String.format(
						"%02X %s",
						documentPointList2.attributeId,
						SmartAttributeTable.getName(documentPointList2.attributeId))));
			top.appendChild(element);

			for (int i=0 ; i<documentPointList2.size() ; i++)
			{
				SmartGraphDocumentPoint point0 = null;
				if (i > 0)
				{
					point0 = documentPointList2.get(i - 1);
				}
				SmartGraphDocumentPoint point = documentPointList2.get(i);

				// 折れ線描画
				if (i > 0)
				{
					element = createElement("line");
					element.setAttribute("x1", Integer.toString(origin.x + point0.x));
					element.setAttribute("y1", Integer.toString(origin.y + point0.y));
					element.setAttribute("x2", Integer.toString(origin.x + point.x));
					element.setAttribute("y2", Integer.toString(origin.y + point.y));
					element.setAttribute("stroke", color);
					element.setAttribute("stroke-width", "2");
					if ((point.x - point0.x) / documentPointList.scaleX > 8)
					{
						element.setAttribute("stroke-dasharray", "8,3");
					}
					top.appendChild(element);
				}

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
		}
	}
}
