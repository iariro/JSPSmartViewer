package kumagai.smartviewer;

import java.io.*;
import java.util.*;

/**
 * SMART情報一式オブジェクトのコレクション
 */
public class SmartDataList
	extends ArrayList<SmartData>
{
	/**
	 * テストコード
	 * @param args 未使用
	 * @throws IOException
	 */
	public static void main(String[] args)
		throws IOException
	{
		String basePath = "C:\\temp\\smart\\";

		for (String filename : new File(basePath).list())
		{
			File file = new File(basePath, filename);
			FileInputStream stream = new FileInputStream(file);
			int size = (int)file.length();
			byte [] data = new byte [size];
			stream.read(data);
			SmartDataList smartDataList = new SmartDataList(data);
			System.out.printf("%s %d %d\n", filename, size, smartDataList.size());

			for (SmartData smartData : smartDataList)
			{
				System.out.printf(smartData.getDateTime());

				for (SmartAttribute attribute : smartData.attributes)
				{
					System.out.printf(
						" %d=%d",
						attribute.getId(),
						attribute.getCurrent());
				}
				System.out.println();
			}
		}
	}

	/**
	 * 既定のコンストラクタ
	 */
	public SmartDataList()
	{
	}

	/**
	 * ログファイルの内容からSMART情報一式オブジェクトのコレクションを構築
	 * @param data ログファイル内容バイナリ
	 */
	public SmartDataList(byte [] data)
	{
		for (int offset=0 ; offset<data.length ; )
		{
			SmartData smartData = new SmartData(data, offset);
			add(smartData);
			offset += smartData.bufferSize;
		}
	}
}
