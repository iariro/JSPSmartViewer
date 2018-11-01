package kumagai.smartviewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * dfコマンド出力から構築可能なDriveSizeオブジェクトのリスト
 */
public class DriveSizeListFromDf
	extends ArrayList<DriveSize>
{
	static private final Pattern patternHeader =
		Pattern.compile("Filesystem  *([0-9]*)-blocks.*");
	static private final Pattern patternValue =
			Pattern.compile("(.{4}[^ ]*) *([0-9]*) *([0-9]*) *([0-9]*) *([0-9]*)% *([0-9]*) *([0-9]*) *([0-9]*)% *(.*)");

	static public void main(String[] args)
		throws IOException
	{
		ArrayList<DriveSize> driveSizes = new DriveSizeListFromDf(FileUtility.readAllLines(new File("testdata/20181101075628_df")));

		for (DriveSize driveSize : driveSizes)
		{
			System.out.println(driveSize);
		}
	}

	/**
	 * ストリームからパーティションの容量情報リストを構築する
	 * @param lines df出力を保持する文字列配列
	 */
	public DriveSizeListFromDf(String [] lines)
		throws NumberFormatException, IOException
	{
		long blockSize = 0;

		for (String  line : lines)
		{
			Matcher matcher = patternHeader.matcher(line);
			if (matcher.matches())
			{
				// ヘッダ行

				blockSize = Integer.valueOf(matcher.group(1));
			}

			matcher = patternValue.matcher(line);
			if (matcher.matches())
			{
				// 値の行

				long total = Long.valueOf(matcher.group(2)) * blockSize;
				long used = Long.valueOf(matcher.group(3)) * blockSize;
				add(new DriveSize(matcher.group(1), total, used));
			}
		}
	}
}
