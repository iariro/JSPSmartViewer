package kumagai.smartviewer.struts2;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.smartviewer.*;

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

		if (target != null)
		{
			// 対象パス情報は取得できた

			SmartDataList smartDataList = new SmartDataList();
			String [] filenames = new File(target.path).list();
			if (filenames != null)
			{
				// リストを取得できた

				for (String filename : filenames)
				{
					File file = new File(target.path, filename);
					FileInputStream stream = new FileInputStream(file);
					int size = (int)file.length();
					byte [] data = new byte [size];
					stream.read(data);
					stream.close();

					smartDataList.addAll(new SmartDataList(data));
				}

				ValueAndHourCollection valueAndHourCollection =
					smartDataList.getFluctuationPoint(powerOnHoursId, valueId);

				predictFailure = valueAndHourCollection.predictFailure();

				attributeName = SmartAttributeTable.getName(valueId);

				return "success";
			}
		}

		return "error";
	}
}
