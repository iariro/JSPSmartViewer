package kumagai.smartviewer.struts2;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.smartviewer.*;

/**
 * ディスクごとのトップページ表示アクション。
 * @author kumagai
 */
@Namespace("/smartviewer")
@Results
({
	@Result(name="success", location="/smartviewer/disktop.jsp"),
	@Result(name="error", location="/smartviewer/error.jsp")
})
public class DiskTopPageAction
{
	public String targetName;

	public String modelName;
	public String serialNumber;
	public String firmwareVersion;
	public TreeMap<Integer, String> attributes = new TreeMap<Integer, String>();
	public String message;

	/**
	 * ディスクごとのトップページ表示アクション。
	 * @return 処理結果
	 */
	@Action("disktop")
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

				SmartAttributeList smartAttributeList = null;

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

					// 最新を見る
					SmartDataList smartDataList = new SmartDataList(data);

					if (smartDataList.size() > 0)
					{
						SmartData smartData =
							smartDataList.get(smartDataList.size() - 1);

						modelName = smartData.identify.getModelName();
						serialNumber = smartData.identify.getSerialNumber();
						firmwareVersion = smartData.identify.getFirmwareVersion();
						smartAttributeList = smartData.attributes;
					}
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
					smartAttributeList = smartctlOutput.getSmartAttributeList();
				}

				for (SmartAttribute attribute : smartAttributeList)
				{
					attributes.put(
						attribute.getId(),
						String.format(
							"%02X %s",
							attribute.getId(),
							SmartAttributeTable.getName(attribute.getId())));
				}
			}

			return "success";
		}
		else
		{
			// 対象パス情報は取得できなかった

			message = String.format("対象パス情報は取得できなかった targetName=%s", targetName);

			return "error";
		}
	}
}
