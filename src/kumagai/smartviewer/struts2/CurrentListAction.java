package kumagai.smartviewer.struts2;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.SmartAttributeAndThreshold;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartErrorLog;

/**
 * カレント値表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/currentlist.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class CurrentListAction
{
	public String targetName;

	public String datetime;
	public ArrayList<SmartAttributeAndThreshold> attributes;
	public SmartErrorLog errorLog;

	/**
	 * カレント値表示アクション。
	 * @return 処理結果
	 */
	@Action("currentlist")
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
			datetime = smartData.getDateTime();
			attributes = smartData.geSmartAttributeAndThresholdList();
			errorLog = smartData.errorLog;

			return "success";
		}
		else
		{
			// 必要なパラメータは指定されていない

			return "error";
		}
	}}
