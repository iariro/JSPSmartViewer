package kumagai.smartviewer.struts2;

import java.io.*;
import javax.servlet.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;

import kumagai.smartviewer.*;

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
	public String datetime;
	public SmartAttributeList attributes;

	/**
	 * カレント値表示アクション。
	 * @return 処理結果
	 */
	@Action("currentlist")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();
		String smartFilePath = context.getInitParameter("SmartFilePath");
		if (smartFilePath != null)
		{
			String [] filenames = new File(smartFilePath).list();
			if (filenames != null && filenames.length > 0)
			{
				// ファイルは１個でもある

				String filename = filenames[filenames.length - 1];
				File file = new File(smartFilePath, filename);
				FileInputStream stream = new FileInputStream(file);
				int size = (int)file.length();
				byte [] data = new byte [size];
				stream.read(data);
				SmartDataList smartDataList = new SmartDataList(data);

				if (smartDataList.size() > 0)
				{
					SmartData smartData = smartDataList.get(smartDataList.size() - 1);
					datetime = smartData.getDateTime();
					attributes = smartData.attributes;
				}
			}

			return "success";
		}
		else
		{
			return "error";
		}
	}	
}
