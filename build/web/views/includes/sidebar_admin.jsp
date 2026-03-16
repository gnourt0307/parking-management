<!-- Admin Navigation Bar Component -->
<%String uri=request.getRequestURI();%>
    <!-- Mobile Header -->
    <div class="mobile-header">
        <a href="Dashboard" class="mobile-header-brand"><i class="fa-solid fa-user-tie"></i> Admin Panel</a>
        <button class="mobile-toggle-btn" onclick="toggleSidebar()">
            <i class="fa-solid fa-bars"></i>
        </button>
    </div>

    <!-- Sidebar Overlay -->
    <div class="sidebar-overlay" onclick="toggleSidebar()"></div>

    <aside class="sidebar">
        <a href="Dashboard" class="navbar-brand"><i class="fa-solid fa-user-tie"></i> Admin Panel</a>
        <ul class="nav-links">
            <li><a href="Dashboard" class="<%= uri.contains(" dashboard") ? "active" : "" %>"><i
                        class="fa-solid fa-chart-line"></i> Dashboard</a></li>
            <li><a href="Users" class="<%= uri.contains(" user_list") ? "active" : "" %>"><i
                        class="fa-solid fa-users"></i> Users</a></li>
            <li><a href="Zones" class="<%= uri.contains(" zone_list") ? "active" : "" %>"><i
                        class="fa-solid fa-layer-group"></i> Zones</a></li>
            <li><a href="Slots" class="<%= uri.contains(" slot_list") ? "active" : "" %>"><i
                        class="fa-solid fa-car-side"></i> Slots</a></li>
            <li><a href="Pricing" class="<%= uri.contains(" pricing_list") ? "active" : "" %>"><i
                        class="fa-solid fa-money-bill"></i> Pricing</a></li>
            <li><a href="Report" class="<%= uri.contains(" report") ? "active" : "" %>"><i
                        class="fa-solid fa-file-invoice-dollar"></i> Reports</a></li>
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