<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

        <c:if test="${not empty sessionScope.successMsg}">
          <div class="alert alert-success">${sessionScope.successMsg}</div>
          <c:remove var="successMsg" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.errorMsg}">
          <div class="alert alert-danger">${sessionScope.errorMsg}</div>
          <c:remove var="errorMsg" scope="session" />
        </c:if>

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

          <div class="invoice-section">
            <h3 class="invoice-header"><i class="fa-solid fa-file-invoice"></i> Invoice</h3>

            <c:choose>
              <c:otherwise>
                <p><strong>Ticket Code:</strong> ${ticket.ticketCode}</p>
                <p><strong>License Plate:</strong> ${ticket.licensePlate}</p>
                <p><strong>Zone - Slot:</strong> ${ticket.slot.zone.zoneName} - ${ticket.slot.slotName}</p>
                <p><strong>Check-In Time:</strong> ${entryTimeFormatted}</p>
                <div class="form-group" style="margin: 12px 0 0;">
                  <label style="display:flex; align-items:center; gap:10px; font-weight:600;">
                    <input type="checkbox" id="lostTicketCheckbox" data-total-with-lost="${totalWithLost}" />
                    Lost ticket
                  </label>
                </div>
                <p id="lostFeeRow" style="display:none; margin-top: 6px;">
                  <strong>Lost Ticket Fee:</strong> <span id="lostFeeValue">${lostFee}</span>
                </p>

                <div class="invoice-total-container">
                  <h2 class="invoice-total-text">Total: <span id="totalValue">${baseAmount}</span></h2>
                </div>

                <div class="invoice-actions">
                  <form id="confirmForm" action="VehicleOut" method="post" style="display:inline-block;">
                    <input type="hidden" name="action" value="confirm" />
                    <input type="hidden" name="ticketID" value="${ticket.ticketID}" />
                    <input type="hidden" name="paymentMethod" value="CASH" />
                    <input type="hidden" name="lostTicket" id="lostTicketHidden" value="false" />
                    <button class="btn btn-success btn-flex-large" type="submit">
                      Confirm Payment
                    </button>
                  </form>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </main>
  </div>
  <jsp:include page="../includes/footer.jsp" />
  <script src="static/js/staff_edit.js?v=2"></script>
</body>

</html>