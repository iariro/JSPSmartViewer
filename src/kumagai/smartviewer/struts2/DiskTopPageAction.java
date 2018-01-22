package kumagai.smartviewer.struts2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartAttributeTable;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartIdentifyFromSmartctl;
import kumagai.smartviewer.SmartctlOutput;

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

				ArrayList<SmartAttribute> smartAttributeList = null;

				for (int i=filenames.length-1 ; i>=0 ; i--)
				{
					String filename = filenames[i];
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
							// SMART情報あり。

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
						SmartIdentifyFromSmartctl smartIdentify = smartctlOutput.getSmartIdentify();
						modelName = smartIdentify.modelName;
						serialNumber = smartIdentify.serialNumber;
						firmwareVersion = smartIdentify.firmwareVersion;
					}

					if (smartAttributeList.size() > 0)
					{
						// 属性情報あり。

						break;
					}
				}

				for (SmartAttribute attribute : smartAttributeList)
				{
					attributes.put(
						attribute.getId(),
						String.format(
							"%d %s",
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
