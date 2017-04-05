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

	public int getRawValue()
	{
		return
			((data[8] & 0xff) << 24) +
			((data[7] & 0xff) << 16) +
			((data[6] & 0xff) << 8) +
			((data[5] & 0xff));
	}

	public String getRawValueDump()
	{
		String string = new String();
		
		for (int i=5 ;i<12 ; i++)
		{
			if (i > 5)
			{
				string += " ";
			}

			string += String.format("%02X", data[i]);
		}
		return string;
	}
}
