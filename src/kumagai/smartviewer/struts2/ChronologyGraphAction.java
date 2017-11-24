package kumagai.smartviewer.struts2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

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
import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartGraphDocument;
import kumagai.smartviewer.SmartGraphDocumentPointList;
import kumagai.smartviewer.SmartctlOutput;

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
	public String targetName;

	public String mode;
	public int [] ids;
	public String graphType;
	public String field;
	public SmartGraphDocument document;
	public String chartPointLists;
	public String message;
	public int filenumlimit;
	public String scaling;

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

		if (target != null)
		{
			// 必要なパラメータは指定されている

			SmartDataList smartDataList = new SmartDataList();
			String [] filenames = new File(target.path).list();
			if (filenames != null)
			{
				// リストを取得できた

				Arrays.sort(filenames);
				for (int i=0 ; i<filenames.length && i<filenumlimit ;i++)
				{
					String filename = filenames[filenames.length > filenumlimit ? filenames.length - filenumlimit + i : i];

					File file = new File(target.path, filename);
					FileInputStream stream = new FileInputStream(file);
					int size = (int)file.length();
					byte [] data = new byte [size];
					stream.read(data);
					stream.close();

					if (target.type.equals("binary"))
					{
						// バイナリ

						smartDataList.addAll(new SmartDataList(data));
					}
					else
					{
						// smartctl出力

						BufferedReader reader =
							new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));

						String line;
						ArrayList<String> lines = new ArrayList<String>();
						while ((line = reader.readLine()) != null)
						{
							lines.add(line);
						}

						SmartctlOutput smartctlOutput =
							new SmartctlOutput(lines.toArray(new String [0]));
						ArrayList<SmartAttribute> attributes =
							smartctlOutput.getSmartAttributeList();

						smartDataList.add(new SmartData(filename, attributes));
					}
				}
			}

			int [] ids = null;
			String field = null;
			if (mode.equals("specifyid"))
			{
				// ID指定モード

				ids = this.ids;
				field = this.field;
			}
			else if (mode.equals("ascending") || mode.equals("descending"))
			{
				// 増加する属性のみ

				ids = smartDataList.getAscOrDescAttributeIds(mode);
				field = "raw";
			}

			ISmartFieldGetter smartFieldGetter = ChronologyGraph.getSmartFieldGetter(field);

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
							(smartDataList, ChronologyGraph.screen, id, smartFieldGetter));
				}
				document = new SmartGraphDocument(smartGraphDocumentPointLists);

				return "graph1";
			}
			else if (graphType.equals("HighCharts"))
			{
				// HighCharts

				StringBuffer chartPointLists =
					ChronologyGraph.createHighChartsPoints
						(ids, smartDataList, smartFieldGetter, scaling != null);

				this.chartPointLists = chartPointLists.toString();

				return "graph2";
			}
		}

		return "error";
	}
}
