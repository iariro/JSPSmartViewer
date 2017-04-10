package kumagai.smartviewer.convert;

import java.io.*;

/**
 * データ変換
 */
public class ConvertNewData
{
	/**
	 * データ変換
	 * @param args [0]=旧ファイル名 [1]=新ファイル [2]=3/4
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args)
		throws NumberFormatException, IOException
	{
		if (args.length == 3)
		{
			FileInputStream inputStream = new FileInputStream(args[0]);
			FileOutputStream outputStream = new FileOutputStream(args[1]);
			convert(inputStream, outputStream, Integer.valueOf(args[2]));
			outputStream.close();
			inputStream.close();
			System.out.printf("%s -> %s\n", args[0], args[1]);
		}
	}

	private static void convert(InputStream inputStream, FileOutputStream outputStream, int blockNum)
		throws IOException
	{
		do
		{
			byte [] header3 = new byte [16];
			int readSize = inputStream.read(header3);

			if (readSize <= 0)
			{
				break;
			}

			String magic = "SMARTCrawlResult";
			byte [] header1 = magic.getBytes();
			outputStream.write(header1, 0, header1.length);

			byte [] header2 = new byte [32];
			outputStream.write(header2, 0, header2.length);

			outputStream.write(header3, 0, header3.length);

			byte [] header4 = new byte [4];
			header4[0] = (byte)(blockNum & 0xff);
			header4[1] = (byte)((blockNum & 0xff00) >> 8);
			header4[2] = (byte)((blockNum & 0xff0000) >> 16);
			header4[3] = (byte)((blockNum & 0xff000000) >> 24);
			outputStream.write(header4, 0, header4.length);

			byte [] blockHeader = new byte [12];
			byte [] blockBody = new byte [512];

			if (blockNum >= 1)
			{
				// IDENTIFY
				blockHeader[0] = 10;
				blockHeader[1] = 0x00;
				blockHeader[2] = 0x00;
				blockHeader[3] = 0x00;

				blockHeader[4] = 0x00;
				blockHeader[5] = (byte)0xec;
				blockHeader[6] = 0x00;
				blockHeader[7] = 0x00;

				blockHeader[8] = 0x00;
				blockHeader[9] = 0x02; // 512
				blockHeader[10] = 0x00;
				blockHeader[11] = 0x00;
				outputStream.write(blockHeader);

				inputStream.read(blockBody);
				outputStream.write(blockBody);
			}

			if (blockNum >= 2)
			{
				// SMART Value
				blockHeader[4] = 0x00;
				blockHeader[5] = (byte)0xb0;
				blockHeader[6] = (byte)0xd0;
				blockHeader[7] = 0x00;

				blockHeader[8] = 0x00;
				blockHeader[9] = 0x02; // 512
				blockHeader[10] = 0x00;
				blockHeader[11] = 0x00;
				outputStream.write(blockHeader);

				inputStream.read(blockBody);
				outputStream.write(blockBody);
			}

			if (blockNum >= 3)
			{
				// threshold
				blockHeader[4] = 0x00;
				blockHeader[5] = (byte)0xb0;
				blockHeader[6] = (byte)0xd1;
				blockHeader[7] = 0x00;

				blockHeader[8] = 0x00;
				blockHeader[9] = 0x02; // 512
				blockHeader[10] = 0x00;
				blockHeader[11] = 0x00;
				outputStream.write(blockHeader);

				inputStream.read(blockBody);
				outputStream.write(blockBody);
			}

			if (blockNum >= 4)
			{
				// threshold
				blockHeader[4] = 0x00;
				blockHeader[5] = (byte)0xb0;
				blockHeader[6] = (byte)0xd5;
				blockHeader[7] = 0x00;

				blockHeader[8] = 0x00;
				blockHeader[9] = 0x02; // 512
				blockHeader[10] = 0x00;
				blockHeader[11] = 0x00;
				outputStream.write(blockHeader);

				inputStream.read(blockBody);
				outputStream.write(blockBody);
			}
		}
		while (true);
	}
}
