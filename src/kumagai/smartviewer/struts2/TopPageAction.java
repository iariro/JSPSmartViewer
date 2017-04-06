package kumagai.smartviewer.struts2;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.smartviewer.*;

/**
 * トップページ表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/index.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class TopPageAction
{
	public String modelName;
	public String serialNumber;
	public TreeMap<Integer, String> attributes = new TreeMap<Integer, String>();

	/**
	 * カレント値表示アクション。
	 * @return 処理結果
	 */
	@Action("index")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();
		String smartFilePath = context.getInitParameter("SmartFilePath");
		if (smartFilePath != null)
		{
			// 必要なパラメータは指定されている

			String [] filenames = new File(smartFilePath).list();
			if (filenames != null && filenames.length > 0)
			{
				// ファイルは１個でもある

				// 最新を見る
				String filename = filenames[filenames.length - 1];
				File file = new File(smartFilePath, filename);
				FileInputStream stream = new FileInputStream(file);
				int size = (int)file.length();
				byte [] data = new byte [size];
				stream.read(data);
				stream.close();

				SmartDataList smartDataList = new SmartDataList(data);

				if (smartDataList.size() > 0)
				{
					SmartData smartData =
						smartDataList.get(smartDataList.size() - 1);

					modelName = smartData.identify.getModelName();
					serialNumber = smartData.identify.getSerialNumber();

					for (SmartAttribute attribute : smartData.attributes)
					{
						attributes.put(
							attribute.getId(),
							String.format(
								"%02X %s",
								attribute.getId(),
								SmartAttributeTable.getName(attribute.getId())));
					}
				}
			}

			return "success";
		}

		return "error";
	}
}
