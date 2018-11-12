package kumagai.smartviewer.struts2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import kumagai.smartviewer.DriveSizeListFromDf;
import kumagai.smartviewer.FileUtility;
import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartIdentifyFromSmartctl;
import kumagai.smartviewer.SmartThreshold;
import kumagai.smartviewer.SmartctlOutput;

/**
 * 閲覧対象のパス情報
 */
public class ViewTarget
{
	public final String path;
	public final String type;
	public final String name;
	public final String pc;

	/**
	 * 指定の値をフィールドに割り当てる
	 * @param path パス文字列
	 * @param type binary/smartctl
	 * @param name 名前
	 * @param pc desktop/note
	 */
	public ViewTarget(String path, String type, String name, String pc)
	{
		this.path = path;
		this.type = type;
		this.name = name;
		this.pc = pc;
	}

	/**
	 * ターゲットパスの全ファイルを読み込みSMARTデータリストを返却する
	 * @param filenumlimit リミット件数
	 * @return SMARTデータリスト
	 */
	public SmartDataList loadSmartDataList(Integer filenumlimit)
		throws FileNotFoundException, IOException
	{
		SmartDataList smartDataList = new SmartDataList();

		String [] filenames = new File(path).list();
		if (filenames != null)
		{
			// リストを取得できた

			if (type.equals("binary"))
			{
				// バイナリ

				Arrays.sort(filenames);
				for (int i=0 ; i<filenames.length && (filenumlimit == null || i<filenumlimit) ;i++)
				{
					int index = i;
					if ((filenumlimit != null) && (filenames.length > filenumlimit))
					{
						// リミットの指定あり、リミットを上回る。

						index = filenames.length - filenumlimit + i;
					}
					String filename = filenames[index];

					File file = new File(path, filename);
					FileInputStream stream = new FileInputStream(file);
					int size = (int)file.length();
					byte [] data = new byte [size];
					stream.read(data);
					stream.close();

					smartDataList.addAll(new SmartDataList(data));
				}
			}
			else if (type.equals("smartctl"))
			{
				// smartctl・df出力

				ArrayList<String> smartctlFiles = new ArrayList<>();
				for (String filename : filenames)
				{
					if (filename.length() == 14 ||
						filename.endsWith("_smartctl"))
					{
						smartctlFiles.add(filename);
					}
				}

				Collections.sort(smartctlFiles);
				for (int i=0 ; i<smartctlFiles.size() && (filenumlimit == null || i<filenumlimit) ;i++)
				{
					int index = i;
					if ((filenumlimit != null) && (smartctlFiles.size() > filenumlimit))
					{
						// リミットの指定あり、リミットを上回る。

						index = smartctlFiles.size() - filenumlimit + i;
					}
					String smartctlFilename = smartctlFiles.get(index);

					File smartctlFile = new File(path, smartctlFilename);
					String [] lines = FileUtility.readAllLines(smartctlFile);
					SmartctlOutput smartctlOutput = new SmartctlOutput(lines);
					SmartIdentifyFromSmartctl smartIdentify = smartctlOutput.getSmartIdentify();
					ArrayList<SmartAttribute> attributes = smartctlOutput.getSmartAttributeList();
					ArrayList<SmartThreshold> thresholds = smartctlOutput.getSmartThresholdList();

					DriveSizeListFromDf driveSizeArray = null;
					if (smartctlFilename.endsWith("_smartctl"))
					{
						// dfとペアのファイル名

						String dfFilename = smartctlFilename.replace("smartctl", "df");
						File dfFile = new File(path, dfFilename);

						if (dfFile.exists())
						{
							// dfファイルあり

							lines = FileUtility.readAllLines(dfFile);
							driveSizeArray = new DriveSizeListFromDf(lines);
						}
					}

					smartDataList.add(new SmartData(smartctlFilename, smartIdentify, attributes, thresholds, driveSizeArray));
				}
			}
		}
		return smartDataList;
	}
}
