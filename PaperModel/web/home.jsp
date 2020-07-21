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
        <title>Paper Model</title>
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
    </head>
    <body>
        <form action="SearchController" method="POST">
            <h2 style="text-align: center">Chọn mẫu mô hình giấy của bạn</h2>
            <label>Chọn cấp độ kĩ năng của bạn</label><br/>
            <select name="sltSkillLevel">
                <option value="1">Mới bắt đầu</option>
                <option value="2">Tàm tạm</option>
                <option value="3">Quen thuộc</option>
                <option value="4">Thành thạo</option>
                <option value="5">Chuyên gia</option>
            </select><br/>
            <br/> <label>Chọn độ khó của mô hình</label><br/>
            <select name="sltDifficulty">
                <option value="1">Cực dễ</option>
                <option value="2">Dễ</option>
                <option value="3">Trung Bình</option>
                <option value="4">Khó</option>
                <option value="5">Cực khó</option>
            </select><br/>
            <br/> <label>Thời gian bạn có để làm mô hình (giờ)</label><br/>
            <input type="text" name="txtHour"required="true"/><br/><br/>
            <input type="submit" value="Tìm Kiếm"/>
        </form>
        <c:if test="${not empty requestScope.LISTMODELS}">
            <c:set var="models" value="${requestScope.LISTMODELS}"/>
            <h3>Tìm thấy: ${models.size()} kết quả</h3>
            <!-- pagination -->
            <div style="margin-bottom: 40px;margin-left: 10px">
                <c:forEach begin="0" end="${models.size() - 1}" step="40" varStatus="counter">
                    <a style="float: left; margin-right: 10px; text-decoration: underline; color: blue; cursor: pointer"
                       onclick="paginate(${counter.count})">${counter.count}</a>
                </c:forEach>
            </div>
            <br/>
            <c:forEach var="model" items="${models}">
                <div style="float: left;width: 200px;height: 200px;padding:  10px;text-align: center" 
                     class="model">
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
            <script>
                initPagination();
            </script>
        </c:if >
    </body>
</html>
