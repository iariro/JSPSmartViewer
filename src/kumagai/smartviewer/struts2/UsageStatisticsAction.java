package kumagai.smartviewer.struts2;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.UsageStatistics;

/**
 * PC使用状況の統計情報表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/usagestatistics.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class UsageStatisticsAction
{
	public String targetName;
	public UsageStatistics usageStatistics;

	/**
	 * PC使用状況の統計情報表示アクション。
	 * @return 処理結果
	 */
	@Action("usagestatistics")
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
			// 対象パス情報は取得できた

			smartDataList = target.loadSmartDataList(null);
		}

		if (smartDataList != null)
		{
			// データ取得できた

			usageStatistics = smartDataList.getUsageStatistics();

			return "success";
		}

		return "error";
	}
}
