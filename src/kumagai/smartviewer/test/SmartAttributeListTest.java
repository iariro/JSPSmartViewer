package kumagai.smartviewer.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import kumagai.smartviewer.SmartAttribute;
import kumagai.smartviewer.SmartctlOutput;

public class SmartAttributeListTest
	extends TestCase
{
	public void test()
	{
		String [] lines =
			{
				"smartctl 6.5 2016-05-07 r4318 [Darwin 16.6.0 x86_64] (local build)",
				"Copyright (C) 2002-16, Bruce Allen, Christian Franke, www.smartmontools.org",
				"",
				"=== START OF READ SMART DATA SECTION ===",
				"SMART Attributes Data Structure revision number: 40",
				"Vendor Specific SMART Attributes with Thresholds:",
				"ID# ATTRIBUTE_NAME          FLAG     VALUE WORST THRESH TYPE      UPDATED  WHEN_FAILED RAW_VALUE",
				"  1 Raw_Read_Error_Rate     0x000f   100   100   000    Pre-fail  Always       -       0",
				"  5 Reallocated_Sector_Ct   0x000f   100   100   000    Pre-fail  Always       -       0",
				"  9 Power_On_Hours          0x0032   100   100   000    Old_age   Always       -       820",
				" 12 Power_Cycle_Count       0x0032   100   100   000    Old_age   Always       -       10497",
				"169 Unknown_Attribute       0x0022   100   100   010    Old_age   Always       -       610059421408",
				"173 Wear_Leveling_Count     0x0022   190   190   100    Old_age   Always       -       502531621068",
				"174 Host_Reads_MiB          0x0030   100   100   000    Old_age   Offline      -       14363844",
				"175 Host_Writes_MiB         0x0030   100   100   000    Old_age   Offline      -       18259514",
				"192 Power-Off_Retract_Count 0x0032   100   100   000    Old_age   Always       -       38",
				"194 Temperature_Celsius     0x0022   063   063   000    Old_age   Always       -       37 (Min/Max 14/83)",
				"197 Current_Pending_Sector  0x0032   000   000   000    Old_age   Always       -       0",
				"199 UDMA_CRC_Error_Count    0x003e   100   100   000    Old_age   Always       -       0",
				"244 Unknown_Attribute       0x0002   000   000   000    Old_age   Always       -       0",
				""
			};
		SmartctlOutput smartctlOutput = new SmartctlOutput(lines);
		ArrayList<SmartAttribute> attributeList = smartctlOutput.getSmartAttributeList();
		assertEquals(13, attributeList.size());

		assertEquals(9, attributeList.get(2).getId());
		assertEquals(100, attributeList.get(2).getCurrent());
		assertEquals(820, attributeList.get(2).getRawValue());

		assertEquals(169, attributeList.get(4).getId());
		assertEquals(100, attributeList.get(4).getCurrent());
		assertEquals(610059421408L, attributeList.get(4).getRawValue());

		assertEquals(244, attributeList.get(12).getId());
		assertEquals(0, attributeList.get(12).getCurrent());
		assertEquals(0, attributeList.get(12).getRawValue());
	}
}
