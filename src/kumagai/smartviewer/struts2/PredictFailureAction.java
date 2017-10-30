package kumagai.smartviewer.struts2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.Prediction;
import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartAttributeTable;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartctlOutput;
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

					if (target.type.equals("binary"))
					{
						// バイナリ

						smartDataList.addAll(new SmartDataList(data));
					}
					else
					{
						// smartctl出力

						BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));

						String line;
						ArrayList<String> lines = new ArrayList<String>();
						while ((line = reader.readLine()) != null)
						{
							lines.add(line);
						}

						SmartctlOutput smartctlOutput = new SmartctlOutput(lines.toArray(new String [0]));
						ArrayList<SmartAttribute> attributes = smartctlOutput.getSmartAttributeList();

						smartDataList.add(new SmartData(filename, attributes));
					}
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
