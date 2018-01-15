package kumagai.smartviewer;

/**
 * smartctlの出力から構築可能なSMAR IDENTIFY情報
 */
public class SmartIdentifyFromSmartctl
{
	public String modelName;
	public String serialNumber;
	public String firmwareVersion;

	/**
	 * smartctlの出力から構築可能なSMAR IDENTIFY情報
	 * @param lines smartctlの出力
	 */
	public SmartIdentifyFromSmartctl(String[] lines)
	{
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

				if (lines[i].startsWith("Device Model:"))
				{
					modelName = lines[i].substring(12).trim();
				}
				if (lines[i].startsWith("Serial Number:"))
				{
					serialNumber = lines[i].substring(14).trim();
				}
				if (lines[i].startsWith("Firmware Version:"))
				{
					firmwareVersion = lines[i].substring(17).trim();
				}
			}
		}
	}
}
