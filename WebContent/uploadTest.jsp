<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form method="post" action="uploadtest.do" enctype="multipart/form-data">
  <div id="uploadDiv">
    <input type="file" name="file1"/>
    <input type="text" name="storeFileName" value="文件名称2"/><br/>
    <input type="file" name="file2"/>
    <input type="text" name="storeFileName" value="文件名称1"/>
  </div>
  <input type="submit" value="上传"/>
</form>
<br/>
<br/>
<form method="post" action="uploadtest1.do" enctype="multipart/form-data">
  <div id="uploadDiv">
    <input type="file" name="file1"/>
    <input type="file" name="file2"/>
  </div>
  <input type="submit" value="上传"/>
</form>
</body>
</html>