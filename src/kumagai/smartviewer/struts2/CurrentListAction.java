package kumagai.smartviewer.struts2;

import java.io.*;
import java.util.*;
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
	public String targetName;

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

			String [] filenames = new File(target.path).list();
			if (filenames != null && filenames.length > 0)
			{
				// ファイルは１個でもある

				String filename = filenames[filenames.length - 1];
				File file = new File(target.path, filename);
				FileInputStream stream = new FileInputStream(file);
				int size = (int)file.length();
				byte [] data = new byte [size];
				stream.read(data);
				stream.close();

				if (target.type.equals("binary"))
				{
					// バイナリ

					SmartDataList smartDataList = new SmartDataList(data);

					if (smartDataList.size() > 0)
					{
						// データは１件でもある

						SmartData smartData =
							smartDataList.get(smartDataList.size() - 1);
						datetime = smartData.getDateTime();
						attributes = smartData.attributes;
					}
				}
				else
				{
					// smartctl

					BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));

					String line;
					ArrayList<String> lines = new ArrayList<String>();
					while ((line = reader.readLine()) != null)
					{
						lines.add(line);
					}

					SmartctlOutput smartctlOutput = new SmartctlOutput(lines.toArray(new String [0]));
					attributes = smartctlOutput.getSmartAttributeList();
				}
			}

			return "success";
		}
		else
		{
			// 必要なパラメータは指定されていない

			return "error";
		}
	}}
