package kumagai.smartviewer.struts2;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.ChronologyGraph;
import kumagai.smartviewer.ISmartFieldGetter;
import kumagai.smartviewer.MonthlyAscendList;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartGraphDocument;
import kumagai.smartviewer.SmartGraphDocumentPointList;

/**
 * 属性値遷移グラフ表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="graph1", location="/smartviewer/chronologygraph.jsp"),
	@Result(name="graph2", location="/smartviewer/chronologygraph2.jsp"),
	@Result(name="monthlygraph", location="/smartviewer/monthlygraph.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class ChronologyGraphAction
{
	public String targetName;

	public String mode;
	public int [] ids;
	public String graphType;
	public String [] fields;
	public SmartGraphDocument document;
	public LinkedHashMap<String, String> chartPointLists = new LinkedHashMap<>();
	public String message;
	public int filenumlimit;
	public String scaling;

	public String categories;
	public String series;

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

		ViewTarget target = null;
		if (targetName != null)
		{
			// 必要なパラメータは指定されている

			ViewTargetList targets = new ViewTargetList(context);
			for (ViewTarget target2 : targets)
			{
				if (target2.name.equals(targetName))
				{
					// 対象のエントリである

					target = target2;
				}
			}
		}

		SmartDataList smartDataList = null;
		if (target != null)
		{
			// 必要なパラメータは指定されている

			smartDataList = target.loadSmartDataList(filenumlimit);
		}

		if (smartDataList != null)
		{
			// データ取得できた

			int [] ids = null;
			String [] fields = null;
			if (mode.equals("specifyid"))
			{
				// ID指定モード

				ids = this.ids;
				fields = this.fields;
			}
			else if (mode.equals("ascending") || mode.equals("descending"))
			{
				// 増加する属性のみ

				ids = smartDataList.getAscOrDescAttributeIds(mode);
				fields = new String []{"raw"};
			}

			
			ArrayList<ISmartFieldGetter> smartFieldGetters = new ArrayList<>();
			for (String field : fields)
			{
				smartFieldGetters.add(ISmartFieldGetter.getSmartFieldGetter(field));
			}

			if (ids == null || ids.length <= 0)
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
							(smartDataList, ChronologyGraph.screen, id, smartFieldGetters.get(0)));
				}
				document = new SmartGraphDocument(smartGraphDocumentPointLists);

				return "graph1";
			}
			else if (graphType.equals("Line"))
			{
				// Line

				StringBuffer chartPointLists =
					ChronologyGraph.createHighChartsPoints
						(ids, smartDataList, smartFieldGetters.toArray(new ISmartFieldGetter[]{}), scaling != null);

				this.chartPointLists.put(target.name, chartPointLists.toString());

				return "graph2";
			}
			else if (graphType.equals("MonthlyAscend"))
			{
				// MonthlyAscend

				MonthlyAscendList monthlyAscendList = new MonthlyAscendList(ids[0], smartDataList, smartFieldGetters.get(0));
				categories = monthlyAscendList.createCategoriesString();
				series = monthlyAscendList.createSeriesString();
				return "monthlygraph";
			}
		}

		return "error";
	}
}
