package kumagai.smartviewer.struts2;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartAttributeTable;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;

/**
 * ディスクごとのトップページ表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/disktop.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class DiskTopPageAction
{
	public String targetName;

	public String modelName;
	public String serialNumber;
	public String firmwareVersion;
	public TreeMap<Integer, String> attributes = new TreeMap<Integer, String>();
	public String message;

	/**
	 * ディスクごとのトップページ表示アクション。
	 * @return 処理結果
	 */
	@Action("disktop")
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
			// 対象パス情報は取得できた

			SmartDataList smartDataList = target.loadSmartDataList(1);
			SmartData smartData = smartDataList.get(smartDataList.size() - 1);
			modelName = smartData.modelName;
			serialNumber = smartData.serialNumber;
			firmwareVersion = smartData.firmwareVersion;
			ArrayList<SmartAttribute> smartAttributeList = smartData.attributes;

			for (SmartAttribute attribute : smartAttributeList)
			{
				attributes.put(
					attribute.getId(),
					String.format(
						"%d %s",
						attribute.getId(),
						SmartAttributeTable.getName(attribute.getId())));
			}

			return "success";
		}
		else
		{
			// 対象パス情報は取得できなかった

			message = String.format("対象パス情報は取得できなかった targetName=%s", targetName);

			return "error";
		}
	}
}
