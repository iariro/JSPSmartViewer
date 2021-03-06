package kumagai.smartviewer.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ktool.datetime.DateTime;

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
		String outputPath =
			getServletContext().getInitParameter("SmartctlOutputPath");
		String line = request.getReader().readLine();
		String hostname = line;
		DateTime now = new DateTime();
		String datetime =
			String.format(
				"%04d%02d%02d%02d%02d%02d",
				now.getYear(),
				now.getMonth(),
				now.getDay(),
				now.getHour(),
				now.getMinute(),
				now.getSecond());
		String filename = String.format("%s_smartctl", datetime);
		File file = new File(new File(outputPath, hostname), filename);
		FileWriter writer = new FileWriter(file);
		PrintWriter stream = new PrintWriter(writer);

		while ((line = request.getReader().readLine()) != null)
		{
			stream.println(line);
		}

		stream.close();

		response.getWriter().printf(datetime);
	}
}
