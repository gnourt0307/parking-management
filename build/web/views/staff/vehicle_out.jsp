<!doctype html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Vehicle Check-Out</title>
  <link rel="stylesheet" href="static/css/style.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>

<body>
  <div class="admin-layout">
    <!-- Left Sidebar -->
    <jsp:include page="../includes/sidebar_staff.jsp" />

    <!-- Main Content Area -->
    <main class="admin-main">
      <div class="container main-content width-full-max-none">
        <h2><i class="fa-solid fa-arrow-right-from-bracket"></i> Vehicle Check-Out</h2>

        <div class="checkout-layout">
          <div class="search-section">
            <form action="VehicleOut" method="post">
              <input type="hidden" name="action" value="search" />
              <div class="form-group">
                <label for="plateSearch">Enter License Plate</label>
                <div class="d-flex gap-10">
                  <input type="text" id="plateSearch" name="plateSearch" placeholder="License Plate..." required
                    class="flex-1" value="${param.plateSearch}" />
                  <button type="submit" class="btn"><i class="fa-solid fa-magnifying-glass"></i> Search</button>
                </div>
              </div>
            </form>
          </div>

          <c:if test="${not empty ticket}">
            <div class="invoice-section">
              <h3 class="invoice-header"><i class="fa-solid fa-file-invoice"></i> Invoice</h3>

              <p><strong>Ticket ID:</strong> ${ticket.ticketID}</p>
              <p><strong>Ticket Code:</strong> ${ticket.ticketCode}</p>
              <p><strong>License Plate:</strong> ${ticket.licensePlate}</p>
              <p><strong>Check-In Time:</strong> ${entryTimeFormatted}</p>

              <div class="invoice-total-container">
                <h2 class="invoice-total-text">Total: ${totalAmount}</h2>
              </div>

              <div class="invoice-actions">
                <form action="VehicleOut" method="post" style="display:inline-block;">
                  <input type="hidden" name="action" value="confirm" />
                  <input type="hidden" name="ticketID" value="${ticket.ticketID}" />
                  <input type="hidden" name="paymentMethod" value="CASH" />
                  <button class="btn btn-success btn-flex-large" type="submit">
                    Confirm Payment
                  </button>
                </form>
                <form action="VehicleOut" method="post" style="display:inline-block; margin-left:10px;">
                  <input type="hidden" name="action" value="lost" />
                  <input type="hidden" name="ticketID" value="${ticket.ticketID}" />
                  <button class="btn btn-danger btn-flex-large" type="submit">
                    Mark as Lost Ticket
                  </button>
                </form>
              </div>
            </div>
          </c:if>
        </div>
      </div>
    </main>
  </div>
  <jsp:include page="../includes/footer.jsp" />
</body>

</html>