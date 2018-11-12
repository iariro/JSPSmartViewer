package kumagai.smartviewer;

import java.util.TreeMap;

/**
 * 属性名テーブル
 */
public class SmartAttributeTable
	extends TreeMap<Integer, String>
{
	static private final SmartAttributeTable smartAttributeTable =
		new SmartAttributeTable();

	/**
	 * 属性名を取得
	 * @param id 属性ID
	 * @return 属性名／null=不明
	 */
	static public String getName(int id)
	{
		if (smartAttributeTable.containsKey(id))
		{
			// テーブルに存在する

			return smartAttributeTable.get(id);
		}
		else
		{
			// 存在しない

			return "Unknown";
		}
	}

	/**
	 * 属性名テーブルを構築
	 */
	public SmartAttributeTable()
	{
		put(1,		"Raw Read Error Rate");
		put(2,		"Throughput Performance");
		put(3,		"Spin Up Time");
		put(4,		"Start/Stop Count");
		put(5,		"Reallocated Sectors Count");
		put(6,		"Read Channel Margin");
		put(7,		"Seek Error Rate");
		put(8,		"Seek Time Performance");
		put(9,		"Power-On Hours");
		put(10,		"Spin Retry Count");
		put(11,		"Recalibration Retries");
		put(12,		"Device Power Cycle Count");
		put(13,		"Soft Read Error Rate");
		put(22,		"Current Helium Level");
		put(160,	"Uncorrectable_Sector_Count");
		put(161,	"Valid_Spare_Block");
		put(165,	"Maximum_Erase_Count");
		put(167,	"Average_Erase_Count");
		put(169,	"Remaining life left");
		put(170,	"Available Reserved Space");
		put(171,	"SSD Program Fail Count");
		put(172,	"SSD Erase Fail Count");
		put(173,	"SSD Wear Leveling Count");
		put(174,	"Unexpected power loss count");
		put(175,	"Power Loss Protection Failure");
		put(176,	"Erase Fail Count");
		put(177,	"Wear Range Delta");
		put(179,	"Used Reserved Block Count Total");
		put(180,	"Unused Reserved Block Count Total");
		put(181,	"Program Fail Count Total or Non-4K Aligned Access Count");
		put(182,	"Erase Fail Count");
		put(183,	"SATA Downshift Error Count or Runtime Bad Block");
		put(184,	"End-to-End error / IOEDC");
		put(185,	"Head Stability");
		put(186,	"Induced Op-Vibration Detection");
		put(187,	"Reported Uncorrectable Errors");
		put(188,	"Command Timeout");
		put(189,	"High Fly Writes");
		put(190,	"Temperature Difference or Airflow Temperature");
		put(191,	"G-sense Error Rate");
		put(192,	"Power-off Retract Count");
		put(193,	"Load/Unload Cycle Count");
		put(194,	"Temperature");
		put(195,	"Hardware ECC recovered");
		put(196,	"Reallocation Event Count");
		put(197,	"Current Pending Sector Count");
		put(198,	"Off-Line Scan Uncorrectable Sector Count");
		put(199,	"UltraDMA CRC Error Count");
		put(200,	"Write Error Rate (Multi Zone Error Rate)");
		put(201,	"Soft Read Error Rate");
		put(202,	"Data Address Mark Error");
		put(203,	"Run Out Cancel");
		put(204,	"Soft ECC Correction");
		put(205,	"Thermal Asperity Rate");
		put(206,	"Flying Height");
		put(207,	"Spin High Current");
		put(208,	"Spin Buzz");
		put(209,	"Offline Seek Performance");
		put(210,	"Vibration During Write");
		put(211,	"Vibration During Read");
		put(212,	"Shock During Write");
		put(220,	"Disk Shift");
		put(221,	"G-Sense Error Rate");
		put(222,	"Loaded Hours");
		put(223,	"Load/Unload Retry Count");
		put(224,	"Load Friction");
		put(225,	"Load/Unload Cycle Count");
		put(226,	"Load-in Time");
		put(227,	"Torque Amplification Count");
		put(228,	"Power-Off Retract Count");
		put(230,	"GMR Head Amplitude");
		put(231,	"Life Left (SSD) or Temperature");
		put(232,	"Endurance Remaining or Available Reserved Space");
		put(233,	"Media Wearout Indicator (SSD) or Power-On Hours ");
		put(234,	"Average erase count AND Maximum Erase Count ");
		put(235,	"Good Block Count AND System(Free) Block Count ");
		put(240,	"Head Flying Hours");
		put(241,	"Total LBAs Written");
		put(242,	"Total LBAs Read");
		put(243,	"Total LBAs Written Expanded");
		put(244,	"Total LBAs Read Expanded");
		put(249,	"NAND Writes (1GiB)");
		put(250,	"Read Error Retry Rate");
		put(251,	"Minimum Spares Remaining");
		put(252,	"Newly Added Bad Flash Block");
		put(254,	"Free Fall Protection");
	}
}
