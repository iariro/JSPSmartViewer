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
 * dfの出力POSTの受付
 */
public class ReceiveDfOutputServlet
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
		DateTime now = new DateTime();
		String filename =
			String.format(
				"%04d%02d%02d%02d%02d%02d_df",
				now.getYear(),
				now.getMonth(),
				now.getDay(),
				now.getHour(),
				now.getMinute(),
				now.getSecond());
		File file = new File(outputPath, filename);
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
