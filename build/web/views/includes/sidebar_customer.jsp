<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Customer Navigation Bar Component -->
<%String uri=request.getRequestURI();%>
    <!-- Mobile Header -->
    <div class="mobile-header">
        <a href="Profile" class="mobile-header-brand">
            <c:choose>
                <c:when test="${sessionScope.user.roleID == 1}"><i class="fa-solid fa-user-tie"></i> Admin Profile</c:when>
                <c:when test="${sessionScope.user.roleID == 2}"><i class="fa-solid fa-user-gear"></i> Staff Profile</c:when>
                <c:otherwise><i class="fa-solid fa-user"></i> Customer Portal</c:otherwise>
            </c:choose>
        </a>
        <button class="mobile-toggle-btn" onclick="toggleSidebar()">
            <i class="fa-solid fa-bars"></i>
        </button>
    </div>

    <!-- Sidebar Overlay -->
    <div class="sidebar-overlay" onclick="toggleSidebar()"></div>

    <aside class="sidebar">
        <a href="Profile" class="navbar-brand">
            <c:choose>
                <c:when test="${sessionScope.user.roleID == 1}"><i class="fa-solid fa-user-tie"></i> Admin Profile</c:when>
                <c:when test="${sessionScope.user.roleID == 2}"><i class="fa-solid fa-user-gear"></i> Staff Profile</c:when>
                <c:otherwise><i class="fa-solid fa-user"></i> Customer Portal</c:otherwise>
            </c:choose>
        </a>
        <ul class="nav-links">
            <li><a href="Profile" class="<%= uri.toLowerCase().contains("profile") ? "active" : "" %>"><i
                        class="fa-solid fa-id-card"></i> Profile</a></li>
            <li><a href="History" class="<%= uri.toLowerCase().contains("history") ? "active" : "" %>"><i
                        class="fa-solid fa-clock-rotate-left"></i> Parking History</a></li>
                        
            <c:if test="${sessionScope.user.roleID == 1}">
                <li><a href="Dashboard" class="text-warning mt-3"><i class="fa-solid fa-arrow-left"></i> Back to Admin Panel</a></li>
            </c:if>
            <c:if test="${sessionScope.user.roleID == 2}">
                <li><a href="VehicleIn" class="text-warning mt-3"><i class="fa-solid fa-arrow-left"></i> Back to Staff Panel</a></li>
            </c:if>
        </ul>
        <div class="sidebar-footer">
            <jsp:include page="header.jsp" />
            <a href="Logout"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>
        </div>
    </aside>

    <script>
        function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar');
            const overlay = document.querySelector('.sidebar-overlay');
            if (sidebar) sidebar.classList.toggle('active');
            if (overlay) overlay.classList.toggle('active');
        }
    </script>