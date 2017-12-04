package kumagai.smartviewer.struts2;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.Prediction;
import kumagai.smartviewer.SmartAttributeTable;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.ValueAndHourCollection;

/**
 * 故障予測結果表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/predictfailure.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class PredictFailureAction
{
	public String targetName;
	public int powerOnHoursId;
	public int valueId;

	public String attributeName;
	public ArrayList<Prediction> predictFailure;

	/**
	 * 故障予測結果表示アクション。
	 * @return 処理結果
	 */
	@Action("predictfailure")
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

			ValueAndHourCollection valueAndHourCollection =
				smartDataList.getFluctuationPoint(powerOnHoursId, valueId);

			predictFailure = valueAndHourCollection.predictFailure();

			attributeName = SmartAttributeTable.getName(valueId);

			return "success";
		}

		return "error";
	}
}
