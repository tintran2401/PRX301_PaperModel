<%-- 
    Document   : detail
    Created on : Jul 17, 2020, 12:51:02 AM
    Author     : TiTi
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Model Detail</title>
    </head>
    <style type="text/css">
        .margin{
            margin-top: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
    </style>
    <body>
        <c:if test="${not empty requestScope.MODELDETAIL}">
            <c:set var="model" value="${requestScope.MODELDETAIL}"/>
            <div style="padding-top: 10px;padding-left: 10px">
                <div  style="float: left;padding-right: 30px;">
                    <div style="padding-bottom: 10px;"> <img src="${model.imageSrc}" width="200px" height="200px"/></div>
                    <div style="text-align: center" ><a href="${model.link}"> <button>Download Model</button></a></div>  
                </div>
                <div style="width: 200;height: 200">
                    <div style="float:left;padding-right: 20px;">
                        <div class="margin">Tên:</div>
                        <div class="margin">Số tờ:</div>
                        <div class="margin">Số chi tiết:</div>
                        <div class="margin">Độ khó:</div>
                        <div class="margin">Hướng dẫn:</div>
                    </div>
                    <div style="float:left;">
                        <div class="margin">${model.name}</div>  
                        <div class="margin">${model.numOfSheets}</div>  
                        <div class="margin">${model.numOfParts}</div>  
                        <div class="margin">${model.difficulty}</div>  
                        <c:if test="${ model.hasInstruction}">
                            <div class="margin">Có</div>
                        </c:if>
                        <c:if test="${(not model.hasInstruction) || (empty model.hasInstruction)}">
                            <div class="margin">Không</div>
                        </c:if>
                    </div>
                </div>


            </div>

        </c:if>
    </body>
</html>
