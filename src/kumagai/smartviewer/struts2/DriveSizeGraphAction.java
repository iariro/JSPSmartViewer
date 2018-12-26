package kumagai.smartviewer.struts2;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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
import kumagai.smartviewer.DriveSize;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartGraphDocument;

/**
 * ドライブサイズグラフ表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="graph1", location="/smartviewer/chronologygraph.jsp"),
	@Result(name="graph2", location="/smartviewer/chronologygraph2.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class DriveSizeGraphAction
{
	public String targetName;
	public int filenumlimit;
	public String graphType;

	public SmartGraphDocument document;
	public LinkedHashMap<String,String> chartPointLists;

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
	@Action("drivesizegraph")
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

			ArrayList<String> paritions = new ArrayList<>();
			for (int i=smartDataList.size() - 1 ; i>=0 ; i--)
			{
				ArrayList<DriveSize> driveSize = smartDataList.get(i).driveSizeArray;
				if (driveSize != null)
				{
					for (DriveSize size : driveSize)
					{
						paritions.add(size.partition);
					}
					break;
				}
			}

			Collections.sort(paritions);
			chartPointLists =
				ChronologyGraph.createDriveSizeHighChartsPoints
					(smartDataList, paritions, target.type.equals("smartctl"));

			return "graph2";
		}

		return "error";
	}
}
