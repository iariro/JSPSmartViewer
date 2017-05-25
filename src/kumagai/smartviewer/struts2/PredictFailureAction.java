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

				ValueAndHourCollection valueAndHourCollection =
					points.getFluctuationPoint(9, 9);

				predictFailure = valueAndHourCollection.predictFailure();

				return "success";
			}
		}
		return "error";
	}
}
