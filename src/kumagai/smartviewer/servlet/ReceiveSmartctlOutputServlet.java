package kumagai.smartviewer.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import ktool.datetime.*;

/*

smartctl 6.5 2016-05-07 r4318 [Darwin 16.6.0 x86_64] (local build)
Copyright (C) 2002-16, Bruce Allen, Christian Franke, www.smartmontools.org

=== START OF READ SMART DATA SECTION ===
SMART Attributes Data Structure revision number: 40
Vendor Specific SMART Attributes with Thresholds:
ID# ATTRIBUTE_NAME          FLAG     VALUE WORST THRESH TYPE      UPDATED  WHEN_FAILED RAW_VALUE
  1 Raw_Read_Error_Rate     0x000f   100   100   000    Pre-fail  Always       -       0
  5 Reallocated_Sector_Ct   0x000f   100   100   000    Pre-fail  Always       -       0
  9 Power_On_Hours          0x0032   100   100   000    Old_age   Always       -       820
 12 Power_Cycle_Count       0x0032   100   100   000    Old_age   Always       -       10497
169 Unknown_Attribute       0x0022   100   100   010    Old_age   Always       -       610059421408
173 Wear_Leveling_Count     0x0022   190   190   100    Old_age   Always       -       502531621068
174 Host_Reads_MiB          0x0030   100   100   000    Old_age   Offline      -       14363844
175 Host_Writes_MiB         0x0030   100   100   000    Old_age   Offline      -       18259514
192 Power-Off_Retract_Count 0x0032   100   100   000    Old_age   Always       -       38
194 Temperature_Celsius     0x0022   063   063   000    Old_age   Always       -       37 (Min/Max 14/83)
197 Current_Pending_Sector  0x0032   000   000   000    Old_age   Always       -       0
199 UDMA_CRC_Error_Count    0x003e   100   100   000    Old_age   Always       -       0
244 Unknown_Attribute       0x0002   000   000   000    Old_age   Always       -       0

*/
public class ReceiveSmartctlOutputServlet
	extends HttpServlet
{
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		DateTime now = new DateTime();
		String filename =
			String.format(
				"C:/temp/smart_mac/%04d%02d%02d%02d%02d%02d",
				now.getYear(),
				now.getMonth(),
				now.getDay(),
				now.getHour(),
				now.getMinute(),
				now.getSecond());
		File file = new File(filename);
		FileWriter writer = new FileWriter(file);
		PrintWriter stream = new PrintWriter(writer);

		String line;
		while ((line = request.getReader().readLine()) != null)
		{
			stream.println(line);
		}

		stream.close();

		response.getWriter().printf("<html><body>%s</body></html>", file.toString());
	}
}
