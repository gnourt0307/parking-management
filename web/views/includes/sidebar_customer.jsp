<!-- Customer Navigation Bar Component -->
<%String uri=request.getRequestURI();%>
    <!-- Mobile Header -->
    <div class="mobile-header">
        <a href="Profile" class="mobile-header-brand"><i class="fa-solid fa-user"></i> Customer Portal</a>
        <button class="mobile-toggle-btn" onclick="toggleSidebar()">
            <i class="fa-solid fa-bars"></i>
        </button>
    </div>

    <!-- Sidebar Overlay -->
    <div class="sidebar-overlay" onclick="toggleSidebar()"></div>

    <aside class="sidebar">
        <a href="Profile" class="navbar-brand"><i class="fa-solid fa-user"></i> Customer Portal</a>
        <ul class="nav-links">
            <li><a href="Profile" class="<%= uri.contains(" profile") ? "active" : "" %>"><i
                        class="fa-solid fa-id-card"></i> Profile</a></li>
            <li><a href="History" class="<%= uri.contains(" history") ? "active" : "" %>"><i
                        class="fa-solid fa-clock-rotate-left"></i> History</a></li>
        </ul>
        <div class="sidebar-footer">
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