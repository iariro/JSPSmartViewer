package kumagai.smartviewer.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import ktool.datetime.*;

/**
 * smartctlの出力POSTの受付
 */
public class ReceiveSmartctlOutputServlet
	extends HttpServlet
{
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
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

		response.getWriter().printf("<html><body>%s OK</body></html>", file.toString());
	}
}
