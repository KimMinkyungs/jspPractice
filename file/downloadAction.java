package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bbs.Bbs;

/**
 * Servlet implementation class downloadAction
 */
@WebServlet("/downloadAction")
public class downloadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
String fileName = request.getParameter("file");
		
		int bbsID = 0;
		
		if (request.getParameter("bbsID")!=null){
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		
		

		String directory = this.getServletContext().getRealPath("/upload/");
		File file = new File(directory +"/" + bbsID + "/" + fileName);
		
		String mimeType = getServletContext().getMimeType(file.toString());
		if(mimeType == null) {
			response.setContentType("application/octet-stream");
		}
		
		String downloadName = null;
		
		if (request.getHeader("user-agent").indexOf("MSIE")== -1) {
			downloadName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		}else {
			downloadName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		}
		
		response.setHeader("Content-Disposition", "attachment;filename=\""
		+ downloadName + "\";");
		
		FileInputStream fileInputStream = new FileInputStream(file);
		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		byte b[] = new byte[1024];
		int data = 0;
		
		while((data = (fileInputStream.read(b,0,b.length))) != -1) {
			servletOutputStream.write(b,0,data);
		}
		
		servletOutputStream.flush();
		servletOutputStream.close();
		fileInputStream.close();
			
	}
}