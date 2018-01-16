package kumagai.smartviewer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * smartctlの出力から構築可能なSMAR IDENTIFY情報
 */
public class SmartIdentifyFromSmartctl
{
	static private final Pattern identifyItemPattern = Pattern.compile(".*: *(.*)");

	public String modelName;
	public String serialNumber;
	public String firmwareVersion;

	/**
	 * smartctlの出力から構築可能なSMAR IDENTIFY情報
	 * @param lines smartctlの出力
	 */
	public SmartIdentifyFromSmartctl(String[] lines)
	{
		modelName = new String();
		boolean section = false;
		for (int i=0 ; i<lines.length ; i++)
		{
			if (!section)
			{
				// セクションに入っていない

				if (lines[i].equals("=== START OF INFORMATION SECTION ==="))
				{
					// セクションに入った

					section = true;
				}
			}
			else
			{
				// セクションに入った

				String value = null;
				Matcher matcher = identifyItemPattern.matcher(lines[i]);
				if (matcher.matches())
				{
					// 属性値を含む文字列

					value = matcher.group(1);
				}

				if (lines[i].startsWith("Model Family:"))
				{
					if (!modelName.isEmpty())
					{
						modelName += " / ";
					}
					modelName += value;
				}
				else if (lines[i].startsWith("Device Model:"))
				{
					if (!modelName.isEmpty())
					{
						modelName += " / ";
					}
					modelName += value;
				}
				if (lines[i].startsWith("Serial Number:"))
				{
					serialNumber = value;
				}
				if (lines[i].startsWith("Firmware Version:"))
				{
					firmwareVersion = value;
				}
			}
		}
	}
}
