package kumagai.smartviewer.test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import junit.framework.TestCase;
import kumagai.smartviewer.DriveSize;
import kumagai.smartviewer.DriveSizeListFromDf;
import kumagai.smartviewer.FileUtility;

public class DriveSizeListFromDfTest
	extends TestCase
{
	public void testMac()
		throws ParseException, NumberFormatException, IOException
	{
		ArrayList<DriveSize> driveSizes =
			new DriveSizeListFromDf(FileUtility.readAllLines(new File("testdata/mac_20181101075628_df")));

		for (DriveSize driveSize : driveSizes)
		{
			System.out.println(driveSize);
		}
		assertEquals(6, driveSizes.size());
		assertEquals("/dev/disk1s1", driveSizes.get(0).partition);
		assertEquals(121123069952L, driveSizes.get(0).totalSize);
	}

	public void testUbuntu()
		throws ParseException, NumberFormatException, IOException
	{
		ArrayList<DriveSize> driveSizes =
			new DriveSizeListFromDf(FileUtility.readAllLines(new File("testdata/ubuntu_20181220094741_df")));

		for (DriveSize driveSize : driveSizes)
		{
			System.out.println(driveSize);
		}
		assertEquals(12, driveSizes.size());
		assertEquals("udev", driveSizes.get(0).partition);
		assertEquals(1933414400L, driveSizes.get(0).totalSize);
	}
}
