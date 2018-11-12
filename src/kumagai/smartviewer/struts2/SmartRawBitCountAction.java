package kumagai.smartviewer.struts2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.SmartAttributeTable;
import kumagai.smartviewer.SmartDataList;

/**
 * SMART RAW値のビット集計アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/smartrawbitcount.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class SmartRawBitCountAction
{
	public String targetName;
	public ArrayList<SmartRawBitCount> rawBitCount = new ArrayList<>();

	/**
	 * SMART RAW値のビット集計アクション。
	 * @return 処理結果
	 */
	@Action("smartrawbitcount")
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

			SmartDataList smartDataList = target.loadSmartDataList(null);
			LinkedHashMap<Integer, int[]> rawBitCount = smartDataList.getRawBitCount();
			for (Entry<Integer, int[]> attribute : rawBitCount.entrySet())
			{
				StringBuffer json = new StringBuffer();
				json.append(String.format("{name: '%s', data:[", attribute.getKey()));
				for (int i=0 ; i<attribute.getValue().length ; i++)
				{
					if (i > 0)
					{
						json.append(",");
					}
					json.append(String.format("%d", attribute.getValue()[i]));
				}
				json.append(String.format("]}"));
				this.rawBitCount.add(new SmartRawBitCount(attribute.getKey(), SmartAttributeTable.getName(attribute.getKey()), json.toString()));
			}
		}

		return "success";
	}
}
