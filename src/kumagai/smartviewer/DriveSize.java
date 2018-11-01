package kumagai.smartviewer;

/**
 * ディスクドライブサイズ情報
 */
public class DriveSize
{
	public String driveLetter;
	public long totalSize;
	public long freeSize;

	/**
	 * 指定の値をメンバーに割り当て
	 * @param driveLetter パーティション名
	 * @param totalSize 総サイズ
	 * @param freeSize 空きサイズ
	 */
	public DriveSize(String driveLetter, long totalSize, long freeSize)
	{
		this.driveLetter = driveLetter;
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

		int driveLetterSize = SmartData.getIntFromBytes(binary, offset);
		if (binary.length < offset + 4 + driveLetterSize + 8 + 8)
		{
			return;
		}

		driveLetter = new String(binary, offset + 4, driveLetterSize - 1);
		totalSize = SmartData.getLongFromBytes(binary, offset + 4 + driveLetterSize);
		freeSize = SmartData.getLongFromBytes(binary, offset + 4 + driveLetterSize + 8);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("partition=%s total=%d free=%d", driveLetter, totalSize, freeSize);
	}
}
