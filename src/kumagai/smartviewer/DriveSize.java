package kumagai.smartviewer;

/**
 * ディスクドライブサイズ情報
 */
public class DriveSize
{
	public String partition;
	public long totalSize;
	public long freeSize;

	/**
	 * 指定の値をメンバーに割り当て
	 * @param partition パーティション名
	 * @param totalSize 総サイズ
	 * @param freeSize 空きサイズ
	 */
	public DriveSize(String partition, long totalSize, long freeSize)
	{
		this.partition = partition;
		this.totalSize = totalSize;
		this.freeSize = freeSize;
	}

	/**
	 * バイナリデータからドライブサイズ情報を構築。
	 * @param binary ドライブサイズ情報を保持するバイナリ
	 */
	public DriveSize(byte [] binary, int offset)
	{
		if (binary.length < offset + 4)
		{
			return;
		}

		int partitionNameSize = SmartData.getIntFromBytes(binary, offset);
		if (binary.length < offset + 4 + partitionNameSize + 8 + 8)
		{
			return;
		}

		partition = new String(binary, offset + 4, partitionNameSize - 1);
		totalSize = SmartData.getLongFromBytes(binary, offset + 4 + partitionNameSize);
		freeSize = SmartData.getLongFromBytes(binary, offset + 4 + partitionNameSize + 8);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("partition=%s total=%,d free=%,d", partition, totalSize, freeSize);
	}
}
