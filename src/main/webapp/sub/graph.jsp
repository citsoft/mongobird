<%@page import="net.cit.tetrad.rrd.utils.TetradRrdConfig"%>
<%@page import="net.cit.tetrad.common.Config"%>
<%@page import="net.cit.tetrad.utility.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.BufferedInputStream"%>
<%

out.clear();
out = pageContext.pushBody();
String filePath = CommonUtils.getDefaultRrdImgPath();
String fileName = CommonUtils.getDefaultFileName(request.getParameter("fileName"));
if(fileName.indexOf("../")>=0||fileName.indexOf("./")>=0){
	 response.sendRedirect("../wrongRequest.html");
}
File file = new File(filePath + fileName);
byte b[] = new byte[(int)file.length()];

if (file.isFile())
{
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
            int read = 0;
            while ((read = fin.read(b)) != -1){
                        outs.write(b,0,read);
            }
            outs.close();
            fin.close();
}

%>
