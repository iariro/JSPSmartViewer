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

import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartAttributeAndThreshold;
import kumagai.smartviewer.SmartAttributeAndThresholdList;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartErrorLog;
import kumagai.smartviewer.SmartThreshold;
import kumagai.smartviewer.SmartctlOutput;
import kumagai.smartviewer.StringUtility;

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
	public ArrayList<SmartAttributeAndThreshold> attributes;
	public SmartErrorLog errorLog;

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

						SmartDataList smartDataList = new SmartDataList(data);

						if (smartDataList.size() > 0)
						{
							// データは１件でもある

							SmartData smartData =
								smartDataList.get(smartDataList.size() - 1);
							datetime = smartData.getDateTime();
							attributes = smartData.geSmartAttributeAndThresholdList();
							errorLog = smartData.errorLog;
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
						ArrayList<SmartAttribute> smartAttributeList = smartctlOutput.getSmartAttributeList();
						ArrayList<SmartThreshold> smartThresholdList = smartctlOutput.getSmartThresholdList();
						attributes = new SmartAttributeAndThresholdList(smartAttributeList, smartThresholdList);
						datetime = StringUtility.convertDateTimeFilename(filename);
					}

					if (attributes.size() > 0)
					{
						// 属性あり。
						break;
					}
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
