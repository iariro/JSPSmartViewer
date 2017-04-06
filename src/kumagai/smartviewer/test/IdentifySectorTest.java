package kumagai.smartviewer.test;

import junit.framework.*;
import kumagai.smartviewer.*;

public class IdentifySectorTest
	extends TestCase
{
	public void testGetWordString()
	{
		byte [] data =
			new byte []
			{
				0x45, 0x54, 0x35, 0x38, 0x34, 0x34,
				0x39, 0x34, 0x42, 0x31, 0x39, 0x56, 0x57, 0x32
			};
		assertEquals("TE8544491BV92W", IdentifySector.getWordString(data, 0, data.length));
	}
}
