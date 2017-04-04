package kumagai.smartviewer;

public class SmartAttribute
{
	byte [] data;

	public SmartAttribute(byte [] data, int offset)
	{
		this.data = new byte [12];
		System.arraycopy(data, offset, this.data, 0, 12);
	}

	public int getId()
	{
		return data[0] & 0xff;
	}

	public String getIdInHex()
	{
		return String.format("%2X", getId());
	}

	public int getCurrent()
	{
		return data[3] & 0xff;
	}

	public int getWorst()
	{
		return data[4] & 0xff;
	}
}
