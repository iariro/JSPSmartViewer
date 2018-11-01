package kumagai.smartviewer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * ファイルアクセスユーティリティ
 */
public class FileUtility
{
	/**
	 * 指定のファイルの全行を読み取る
	 * @param file 対象ファイル
	 * @return 内容行リスト
	 */
	static public String [] readAllLines(File file)
		throws IOException
	{
		FileInputStream stream = new FileInputStream(file);
		int size = (int)file.length();
		byte [] data = new byte [size];
		stream.read(data);
		stream.close();
	
		BufferedReader reader =
			new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
	
		String line;
		ArrayList<String> lines = new ArrayList<String>();
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
		reader.close();
	
		return lines.toArray(new String [] {});
	}
}
