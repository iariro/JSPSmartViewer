package kumagai.smartviewer;

import java.util.regex.*;

/**
 * 文字列変換ユーティリティ
 * @author kumagai
 */
public class StringUtility
{
	static private final Pattern formatFilename = Pattern.compile("(....)(..)(..)(..)(..)(..)");

	public static void main(String[] args)
	{
		String filename = "20170825123456";
		System.out.println(convertDateTimeFilename(filename));
	}

	/**
	 * smartctl分のファイル名を日付文字列に変換
	 * @param filename smartctl分のファイル名
	 * @return 日付文字列
	 */
	public static String convertDateTimeFilename(String filename)
	{
		Matcher matcher = formatFilename.matcher(filename);
		if (matcher.matches())
		{
			// マッチする

			return String.format("%s/%s/%s %s:%s:%s", matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6));
		}

		return null;
	}
}
