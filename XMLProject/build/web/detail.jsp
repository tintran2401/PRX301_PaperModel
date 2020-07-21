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
    <script>
        let models = [];
        let pageSize = 40;

        function initPagination() {
            models = document.getElementsByClassName('model');
            paginate(1);
        }

        /**
         * Show page <code>pageNum</code>
         * @param {type} pageNum
         */
        function paginate(pageNum) {
            for (let model of models) {
                showModel(model, false);
            }
            let startIndex = (pageNum - 1) * pageSize;
            for (let i = startIndex; i < startIndex + pageSize; i++) {
                showModel(models[i], true);
            }
        }

        /**
         * show or hide a model
         * @param {type} model
         * @param {boolean} isShowed
         */
        function showModel(model, isShowed) {
            model.style.display = isShowed ? 'block' : 'none';
        }
    </script>
    <body>
        <c:if test="${not empty requestScope.MODELDETAIL}">
            <a href="home.jsp">Trang Chủ</a>
            <c:set var="model" value="${requestScope.MODELDETAIL}"/>
            <div style="padding-top: 10px;padding-left: 10px;  height: 280px">
                <div  style="float: left;padding-right: 30px;">
                    <div style="padding-bottom: 10px;"> <img src="${model.imageSrc}" width="200px" height="200px"/></div>
                    <div style="text-align: center" ><a href="${model.link}"> <button>Download Model</button></a></div>  
                </div>
                <div>
                    <div style="float:left;padding-right: 20px;">
                        <div class="margin">Tên mô hình:</div>
                        <div class="margin">Số tờ:</div>
                        <c:if test="${not empty model.numOfParts}">
                            <div class="margin">Số chi tiết:</div>
                        </c:if>
                        <div class="margin">Độ khó:</div>
                        <div class="margin">Hướng dẫn:</div>
                    </div>
                    <div style="float:left;width: 200px">
                        <div class="margin">${model.name}</div>  
                        <div class="margin">${model.numOfSheets}</div>  
                        <c:if test="${not empty model.numOfParts}">
                            <div class="margin">${model.numOfParts}</div>  
                        </c:if>
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
        <div>
            <h1>Mô hình liên quan:</h1>
            <c:if test="${not empty requestScope.RELATEDMODELS}">
                <c:set var="models" value="${requestScope.RELATEDMODELS}"/>
                <!-- pagination -->
                <div style="margin-bottom: 40px;margin-left: 10px">
                    <c:forEach begin="0" end="${models.size() - 1}" step="40" varStatus="counter">
                        <a style="float: left; margin-right: 10px; text-decoration: underline; color: blue; cursor: pointer"
                           onclick="paginate(${counter.count})">${counter.count}</a>
                    </c:forEach>
                </div>
                <c:forEach var="model" items="${models}">
                    <div style="float: left;width: 200px;height: 200px;padding:  10px;text-align: center" class="model">
                        <div>  <img src="${model.imageSrc}" width="100px" height="100px"/></div>
                        <div>${model.name}</div>
                        <div>Độ khó: ${model.difficulty}</div>
                        <div>
                            <form action="ViewDetailController" method="POST">
                                <input type="hidden" value="${model.link}" name="modelLink"/>
                                <input type="submit" value="Xem chi tiết"/>
                            </form>
                        </div>
                    </div>
                </c:forEach>
                <script>
                    initPagination();
                </script>
            </c:if >
        </div>
    </body>
</html>
