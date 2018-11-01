package kumagai.smartviewer.struts2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import kumagai.smartviewer.DriveSizeListFromDf;
import kumagai.smartviewer.FileUtility;
import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartData;
import kumagai.smartviewer.SmartDataList;
import kumagai.smartviewer.SmartctlOutput;

/**
 * 閲覧対象のパス情報
 */
public class ViewTarget
{
	public final String path;
	public final String type;
	public final String name;

	/**
	 * 指定の値をフィールドに割り当てる
	 * @param path パス文字列
	 * @param type binary/smartctl
	 * @param name 名前
	 */
	public ViewTarget(String path, String type, String name)
	{
		this.path = path;
		this.type = type;
		this.name = name;
	}

	/**
	 * ターゲットパスの全ファイルを読み込みSMARTデータリストを返却する
	 * @param filenumlimit リミット件数
	 * @return SMARTデータリスト
	 */
	public SmartDataList loadSmartDataList2(Integer filenumlimit)
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
					if (filename.endsWith("_smartctl"))
					{
						smartctlFiles.add(filename);
					}
				}

				Collections.sort(smartctlFiles);
				for (int i=0 ; i<filenames.length && (filenumlimit == null || i<filenumlimit) ;i++)
				{
					int index = i;
					if ((filenumlimit != null) && (filenames.length > filenumlimit))
					{
						// リミットの指定あり、リミットを上回る。

						index = filenames.length - filenumlimit + i;
					}
					String smartctlFilename = filenames[index];
					String dfFilename = smartctlFilename.replace("smartctl", "df");

					File smartctlFile = new File(path, smartctlFilename);
					String [] lines = FileUtility.readAllLines(smartctlFile);
					SmartctlOutput smartctlOutput = new SmartctlOutput(lines);
					ArrayList<SmartAttribute> attributes = smartctlOutput.getSmartAttributeList();

					DriveSizeListFromDf driveSizeArray = null;
					File dfFile = new File(path, dfFilename);
					if (dfFile.exists())
					{
						lines = FileUtility.readAllLines(dfFile);
						driveSizeArray = new DriveSizeListFromDf(lines);
					}

					smartDataList.add(new SmartData(smartctlFilename, attributes, driveSizeArray));
				}
			}
		}
		return smartDataList;
	}

	/**
	 * ターゲットパスの全ファイルを読み込みSMARTデータリストを返却する
	 * @param filenumlimit リミット件数
	 * @return SMARTデータリスト
	 */
	public SmartDataList loadSmartDataList(Integer filenumlimit)
		throws FileNotFoundException, IOException
	{
		String [] filenames = new File(path).list();
		if (filenames != null)
		{
			// リストを取得できた

			SmartDataList smartDataList = new SmartDataList();
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

				if (type.equals("binary"))
				{
					// バイナリ

					smartDataList.addAll(new SmartDataList(data));
				}
				else
				{
					// smartctl出力

					BufferedReader reader =
						new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));

					String line;
					ArrayList<String> lines = new ArrayList<String>();
					while ((line = reader.readLine()) != null)
					{
						lines.add(line);
					}

					SmartctlOutput smartctlOutput =
						new SmartctlOutput(lines.toArray(new String [0]));
					ArrayList<SmartAttribute> attributes =
						smartctlOutput.getSmartAttributeList();

					smartDataList.add(new SmartData(filename, attributes, null));
				}
			}
			return smartDataList;
		}
		return null;
	}
}
