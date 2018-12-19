package kumagai.smartviewer.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		BufferedReader reader = request.getReader();

		String line = reader.readLine();
		String hostname = line;

		line = reader.readLine();
		String datetime = line;

		String filename = String.format("%s_df", datetime);
		File file = new File(new File(outputPath, hostname), filename);
		FileWriter writer = new FileWriter(file);
		PrintWriter stream = new PrintWriter(writer);
		while ((line = reader.readLine()) != null)
		{
			stream.println(line);
		}

		stream.close();

		response.getWriter().printf("<html><body>%s OK</body></html>", file.toString());
	}
}
