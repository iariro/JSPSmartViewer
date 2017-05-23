package kumagai.smartviewer.struts2;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.xml.transform.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import kumagai.smartviewer.*;

/**
 * 属性値遷移グラフ表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="graph1", location="/smartviewer/chronologygraph.jsp"),
	@Result(name="graph2", location="/smartviewer/chronologygraph2.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class ChronologyGraphAction
{
	static private final ISmartFieldGetter smartAttributeCurrent =
		new SmartAttributeCurrentGetter();
	static private final ISmartFieldGetter smartAttributeRawValue =
		new SmartAttributeRawValueGet();
	static private final ISmartFieldGetter smartAttributeRawValue2 =
		new SmartAttributeRawValue2Getter();
	static private final Dimension screen = new Dimension(1000, 580);

	public int [] ids;
	public String graphType;
	public String field;
	public SmartGraphDocument document;
	public String chartPointLists;
	public String message;

	/**
	 * グラフSVGドキュメントを文字列として取得。
	 * @return 文字列によるグラフSVGドキュメント
	 */
	public String getXml()
		throws TransformerFactoryConfigurationError, TransformerException
	{
		// XML書き出し準備。
		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

		StringWriter writer = new StringWriter();

		// XML書き出し。
		document.write(transformer, writer);

		return writer.toString();
	}

	/**
	 * 属性値遷移グラフ表示アクション。
	 * @return 処理結果
	 */
	@Action("chronologygraph")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();
		String smartFilePath = context.getInitParameter("SmartFilePath");

		if (smartFilePath != null)
		{
			// 必要なパラメータは指定されている

			SmartDataList points = new SmartDataList();
			String [] filenames = new File(smartFilePath).list();
			if (filenames != null)
			{
				// リストを取得できた

				for (String filename : filenames)
				{
					File file = new File(smartFilePath, filename);
					FileInputStream stream = new FileInputStream(file);
					int size = (int)file.length();
					byte [] data = new byte [size];
					stream.read(data);
					stream.close();

					points.addAll(new SmartDataList(data));
				}
			}

			ISmartFieldGetter smartFieldGetter;
			if (field == null || field.equals("current"))
			{
				// current指定、またはフィールド指定なしの場合

				smartFieldGetter = smartAttributeCurrent;
			}
			else if (field == null || field.equals("raw"))
			{
				// raw指定の場合

				smartFieldGetter = smartAttributeRawValue;
			}
			else
			{
				// current,raw以外指定の場合

				smartFieldGetter = smartAttributeRawValue2;
			}

			if (ids.length <= 0)
			{
				// ID未選択

				message = "IDが１個も選択されていません";

				return "error";
			}

			if (graphType.equals("SVG"))
			{
				// SVG

				ArrayList<SmartGraphDocumentPointList> smartGraphDocumentPointLists =
					new ArrayList<SmartGraphDocumentPointList>();
				for (int id : ids)
				{
					smartGraphDocumentPointLists.add(
						new SmartGraphDocumentPointList
							(points, screen, id, smartFieldGetter));
				}
				document = new SmartGraphDocument(smartGraphDocumentPointLists);

				return "graph1";
			}
			else if (graphType.equals("HighCharts"))
			{
				// HighCharts

				StringBuffer chartPointLists = new StringBuffer();

				for (int id : ids)
				{
					if (chartPointLists.length() > 0)
					{
						// ２個目以降

						chartPointLists.append(",");
					}

					ChartPointList chartPointList =
						new ChartPointList(SmartAttributeTable.getName(id));

					for (SmartData smartData : points)
					{
						for (SmartAttribute attribute : smartData.attributes)
						{
							if (attribute.getId() == id)
							{
								// 対象のIDの属性である

								chartPointList.put(
									smartData.getDateTime(),
									smartFieldGetter.get(attribute));
								break;
							}
						}
					}

					chartPointLists.append(chartPointList.toString());
				}

				this.chartPointLists = chartPointLists.toString();

				return "graph2";
			}
		}

		return "error";
	}
}
