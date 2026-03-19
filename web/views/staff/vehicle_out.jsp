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
          <div class="search-section" style="flex: 2;">
            <div style="display:flex; gap: 15px; margin-bottom: 20px;">
              <div class="form-group flex-1">
                <label for="typeFilter">Filter Vehicle Type</label>
                <select id="typeFilter" onchange="filterTable()">
                  <option value="ALL">All Types</option>
                  <c:forEach items="${vehicleTypes}" var="vt">
                    <option value="${vt.typeID}">${vt.typeName}</option>
                  </c:forEach>
                </select>
              </div>
              <div class="form-group flex-1">
                <label for="plateFilter">Select Plate in Parking</label>
                <select id="plateFilter" onchange="filterTableByPlate()">
                  <option value="ALL">All Plates</option>
                  <c:forEach items="${activeTickets}" var="t">
                      <option value="${t.licensePlate}" data-type="${t.typeID}">${t.licensePlate}</option>
                  </c:forEach>
                </select>
              </div>
            </div>

            <div class="table-responsive" style="max-height: 480px; overflow-y: auto;">
              <table class="data-table" id="activeTicketsTable">
                <thead style="position: sticky; top: 0; background: #fff; z-index: 1;">
                  <tr>
                    <th>License Plate</th>
                    <th>Type</th>
                    <th>Entry Time</th>
                    <th>Zone - Slot</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  <c:choose>
                    <c:when test="${empty activeTickets}">
                       <tr><td colspan="5" style="text-align:center;">No vehicles currently parked.</td></tr>
                    </c:when>
                    <c:otherwise>
                      <c:forEach items="${activeTickets}" var="t">
                        <tr class="ticket-row" data-type="${t.typeID}" data-plate="${t.licensePlate}">
                          <td><strong>${t.licensePlate}</strong></td>
                          <td>${t.vehicleType.typeName}</td>
                          <td>${t.entryTimeFormatted}</td>
                          <td>${t.slot.zone.zoneName} - ${t.slot.slotName}</td>
                          <td>
                            <button type="button" class="btn btn-sm btn-primary" onclick="selectForCheckout('${t.licensePlate}')">Check-Out</button>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </tbody>
              </table>
            </div>

            <form id="loadInvoiceForm" action="VehicleOut" method="post" style="display:none;">
               <input type="hidden" name="action" value="search" />
               <input type="hidden" name="plateSearch" id="hiddenPlateSearch" />
            </form>
          </div>

          <c:set var="hasTicket" value="${not empty ticket}" />
          <div class="invoice-section">
            <h3 class="invoice-header"><i class="fa-solid fa-file-invoice"></i> Invoice</h3>
            <p><strong>Ticket Code:</strong> <span>${hasTicket ? ticket.ticketCode : '-'}</span></p>
            <p><strong>License Plate:</strong> <span>${hasTicket ? ticket.licensePlate : '-'}</span></p>
            <p><strong>Zone - Slot:</strong>
              <span>
                <c:choose>
                  <c:when test="${hasTicket}">
                    ${ticket.slot.zone.zoneName} - ${ticket.slot.slotName}
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </span>
            </p>
            <p><strong>Check-In Time:</strong> <span>${hasTicket ? entryTimeFormatted : '-'}</span></p>

            <div class="form-group" style="margin: 12px 0 0;">
              <label style="display:flex; align-items:center; gap:10px; font-weight:600;">
                <input type="checkbox"
                       id="lostTicketCheckbox"
                       data-total-with-lost="${totalWithLost}"
                       ${hasTicket ? "" : "disabled"} />
                Lost ticket
              </label>
            </div>
            <p id="lostFeeRow" style="display:none; margin-top: 6px;">
              <strong>Lost Ticket Fee:</strong> <span id="lostFeeValue">${hasTicket ? lostFee : ''}</span>
            </p>

            <div class="invoice-total-container">
              <h2 class="invoice-total-text">Total: <span id="totalValue">${hasTicket ? baseAmount : '0 ₫'}</span></h2>
            </div>

            <div class="invoice-actions">
              <form id="confirmForm" action="VehicleOut" method="post" style="display:inline-block;">
                <input type="hidden" name="action" value="confirm" />
                <input type="hidden" name="ticketID" value="${hasTicket ? ticket.ticketID : ''}" />
                <input type="hidden" name="paymentMethod" value="CASH" />
                <input type="hidden" name="lostTicket" id="lostTicketHidden" value="false" />
                <button class="btn btn-success btn-flex-large" type="submit" ${hasTicket ? "" : "disabled"}>
                  Confirm Payment
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
  <jsp:include page="../includes/footer.jsp" />
  <script src="static/js/staff_edit.js?v=2"></script>
  <script>
    function filterTable() {
        var type = document.getElementById('typeFilter').value;
        var plateSelect = document.getElementById('plateFilter');
        
        // Hide/show options in plateSelect based on type
        for(var i=1; i<plateSelect.options.length; i++) {
            var opt = plateSelect.options[i];
            if(type === 'ALL' || opt.getAttribute('data-type') === type) {
                opt.style.display = '';
            } else {
                opt.style.display = 'none';
            }
        }
        plateSelect.value = 'ALL'; // reset plate filter when type changes
        filterRows();
    }

    function filterTableByPlate() {
        filterRows();
    }

    function filterRows() {
        var type = document.getElementById('typeFilter').value;
        var plate = document.getElementById('plateFilter').value;
        var rows = document.querySelectorAll('.ticket-row');
        
        rows.forEach(function(row) {
            var rowType = row.getAttribute('data-type');
            var rowPlate = row.getAttribute('data-plate');
            var matchType = (type === 'ALL' || type === rowType);
            var matchPlate = (plate === 'ALL' || plate === rowPlate);
            
            if(matchType && matchPlate) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    function selectForCheckout(plate) {
        if(confirm("Load invoice for vehicle " + plate + "?")) {
            document.getElementById('hiddenPlateSearch').value = plate;
            document.getElementById('loadInvoiceForm').submit();
        }
    }
  </script>
</body>

</html>