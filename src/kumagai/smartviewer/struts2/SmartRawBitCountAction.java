package kumagai.smartviewer.struts2;

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
	public LinkedHashMap<String, String> rawBitCount = new LinkedHashMap<>();

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

			SmartDataList smartDataList = target.loadSmartDataList(100);
			LinkedHashMap<Integer, int[]> rawBitCount = smartDataList.getRawBitCount();
			for (Entry<Integer, int[]> attribute : rawBitCount.entrySet())
			{
				String counts = new String();
				for (int count : attribute.getValue())
				{
					counts += String.format(" %d", count);
				}
				this.rawBitCount.put(SmartAttributeTable.getName(attribute.getKey()), counts);
			}
		}

		return "success";
	}
}
