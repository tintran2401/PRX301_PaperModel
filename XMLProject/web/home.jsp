<%-- 
    Document   : home
    Created on : Jul 15, 2020, 10:39:22 PM
    Author     : TiTi
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="SearchController" method="POST">
            <h2 style="text-align: center">Chọn mẫu mô hình giấy của bạn</h2>
            <label>Chọn cấp độ kĩ năng của bạn</label><br/>
            <select name="sltSkillLevel">
                <option value="1" class="bg-super-easy">Mới bắt đầu</option>
                <option value="2" class="bg-easy">Tàm tạm</option>
                <option value="3" class="bg-normal">Quen thuộc</option>
                <option value="4" class="bg-hard">Thành thạo</option>
                <option value="5" class="bg-super-hard">Chuyên gia</option>
            </select><br/>
            <br/> <label>Chọn độ khó của mô hình</label><br/>
            <select name="sltDifficulty">
                <option value="1" class="bg-super-easy">Cực dễ</option>
                <option value="2" class="bg-easy">Dễ</option>
                <option value="3" class="bg-normal">Trung Bình</option>
                <option value="4" class="bg-hard">Khó</option>
                <option value="5" class="bg-super-hard">Cực khó</option>
            </select><br/>
            <br/> <label>Thời gian bạn có để làm mô hình (giờ)</label><br/>
            <input type="number" name="txtHour" min="1"/><br/><br/>
            <input type="submit" value="Search"/>
        </form>
        <c:if test="${not empty requestScope.LISTMODELS}">
            <h1>List model:</h1><br/>
            <c:set var="models" value="${requestScope.LISTMODELS}"/>
            <c:forEach var="model" items="${models}" varStatus="counter">
                <div style="float: left;width: 200px;height: 200px;padding:  10px;text-align: center" >
                    <div>  <img src="${model.imageSrc}" width="100px" height="100px"/></div>
                    <div>${model.name}</div>
                    <div>Độ khó: ${model.difficulty}</div>
                    <div>Thời gian: ${model.totalHour} h</div>
                    <div>
                        <form action="ViewDetailController" method="POST">
                            <input type="hidden" value="${model.link}" name="modelLink"/>
                            <input type="submit" value="Xem chi tiết"/>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:if>
    </body>
</html>
