<!-- Staff Navigation Bar Component -->
<%String uri=request.getRequestURI();%>
    <!-- Mobile Header -->
    <div class="mobile-header">
        <a href="VehicleIn" class="mobile-header-brand"><i class="fa-solid fa-user-gear"></i> Staff Panel</a>
        <button class="mobile-toggle-btn" onclick="toggleSidebar()">
            <i class="fa-solid fa-bars"></i>
        </button>
    </div>

    <!-- Sidebar Overlay -->
    <div class="sidebar-overlay" onclick="toggleSidebar()"></div>

    <aside class="sidebar">
        <a href="VehicleIn" class="navbar-brand"><i class="fa-solid fa-user-gear"></i> Staff Panel</a>
        <ul class="nav-links">
            <li><a href="VehicleIn" class="<%= uri.contains(" vehicle_in") ? "active" : "" %>"><i
                        class="fa-solid fa-arrow-right-to-bracket"></i> Check-In</a></li>
            <li><a href="VehicleOut" class="<%= uri.contains(" vehicle_out") ? "active" : "" %>"><i
                        class="fa-solid fa-arrow-right-from-bracket"></i> Check-Out</a></li>
            <li><a href="StaffHistory" class="<%= uri.contains("StaffHistory") ? "active" : "" %>"><i
                        class="fa-solid fa-clock-rotate-left"></i> History</a></li>
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