<!doctype html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Admin Dashboards</title>
  <link rel="stylesheet" href="static/css/style.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>

<body>
  <div class="admin-layout">
    <!-- Left Sidebar -->
    <jsp:include page="../includes/sidebar_admin.jsp" />

    <!-- Main Content Area -->
    <main class="admin-main">
      <div class="container main-content full-width">
        <h2><i class="fa-solid fa-chart-pie"></i> Dashboard Overview</h2>

        <div class="dashboard-cards">
          <div class="card">
            <h3>Total Capacity</h3>
            <div class="value">500</div> <!<!-- Fix for auto -->
          </div>
          <div class="card">
            <h3>Vehicles Parked</h3>
            <div class="value">325</div> <!<!-- Fix for auto -->
          </div>
          <div class="card">
            <h3>Available Slots</h3>
            <div class="value text-success">175</div> <!<!-- Fix for auto -->
          </div>
          <div class="card">
            <h3>Today's Revenue</h3>
            <div class="value">$1,250</div> <!<!-- Fix for auto -->
          </div>
        </div>

        <h3><i class="fa-solid fa-clock-rotate-left"></i> Recent Activity</h3> <!<!-- Fix for auto -->
        <table class="data-table">
          <thead>
            <tr>
              <th>Time</th>
              <th>License Plate</th>
              <th>Action</th>
              <th>Staff</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>10:30 AM</td>
              <td>29A-123.45</td>
              <td>
                <span class="alert-success badge">Check-In</span>
              </td>
              <td>Staff01</td>
            </tr>
            <tr>
              <td>10:25 AM</td>
              <td>30F-987.65</td>
              <td>
                <span class="alert-danger badge">Check-Out</span>
              </td>
              <td>Staff02</td>
            </tr>
            <tr>
              <td>10:15 AM</td>
              <td>51C-456.78</td>
              <td>
                <span class="alert-success badge">Check-In</span>
              </td>
              <td>Staff01</td>
            </tr>
          </tbody>
        </table>
      </div>

      <jsp:include page="../includes/footer.jsp" />
    </main>
  </div>
  <script src="static/js/admin_edit.js"></script>
</body>

</html>
