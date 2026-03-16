<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="currentUser" value="${sessionScope.user}" />

<c:if test="${not empty currentUser}">
  <div class="sidebar-user-box">
    <div class="sidebar-user-avatar">
      <i class="fa-solid fa-circle-user"></i>
    </div>
    <div class="sidebar-user-info">
      <div class="sidebar-user-name">
        <c:choose>
          <c:when test="${not empty currentUser.fullName}">
            <c:out value="${currentUser.fullName}" />
          </c:when>
          <c:otherwise>
            <c:out value="${currentUser.username}" />
          </c:otherwise>
        </c:choose>
      </div>
      <div class="sidebar-user-role">
        <c:choose>
          <c:when test="${currentUser.roleID == 1}">ADMIN</c:when>
          <c:when test="${currentUser.roleID == 2}">STAFF</c:when>
          <c:when test="${currentUser.roleID == 3}">CUSTOMER</c:when>
          <c:otherwise>User</c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</c:if>