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
	 * @param fileNumLimit リミット件数
	 * @return SMARTデータリスト
	 */
	public SmartDataList loadSmartDataList(Integer fileNumLimit)
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
				for (int i=0 ; i<filenames.length && (fileNumLimit == null || i<fileNumLimit) ;i++)
				{
					int index = i;
					if ((fileNumLimit != null) && (filenames.length > fileNumLimit))
					{
						// リミットの指定あり、リミットを上回る。

						index = filenames.length - fileNumLimit + i;
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
				int count = 0;
				for (int i=0 ; i<smartctlFiles.size() ;i++)
				{
					String smartctlFilename = smartctlFiles.get(smartctlFiles.size() - i - 1);

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

					if (attributes.size() > 0 && thresholds.size() > 0)
					{
						smartDataList.add(
							0,
							new SmartData(
								smartctlFilename,
								smartIdentify,
								attributes,
								thresholds,
								driveSizeArray));

						count++;

						if (fileNumLimit != null && count >= fileNumLimit)
						{
							// 制限指定あり・制限を超えた

							break;
						}
					}
				}
			}
		}
		return smartDataList;
	}
}
