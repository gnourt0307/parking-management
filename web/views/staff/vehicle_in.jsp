<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!doctype html>
    <html lang="en">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Vehicle Check-In</title>
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
            <h2><i class="fa-solid fa-arrow-right-to-bracket"></i> Vehicle Check-In</h2>

            <div class="check-in-layout">
              <div class="form-section">
                <c:if test="${not empty sessionScope.successMsg}">
                  <div class="alert alert-success">${sessionScope.successMsg}</div>
                  <c:remove var="successMsg" scope="session" />
                </c:if>
                <c:if test="${not empty sessionScope.errorMsg}">
                  <div class="alert alert-danger">${sessionScope.errorMsg}</div>
                  <c:remove var="errorMsg" scope="session" />
                </c:if>
                <form action="VehicleIn" method="post">
                  <input type="hidden" name="action" value="checkin" />
                  <div class="form-group">
                    <label for="licensePlate">License Plate *</label>
                    <input type="text" id="licensePlate" name="licensePlate" placeholder="e.g. 29A-12345" required />
                  </div>
                  <div class="form-group">
                    <label for="vehicleType">Vehicle Type *</label>
                    <select id="vehicleType" name="vehicleType" required>
                      <!-- Theo Example_data_v3.sql: 1=Motorbike, 2=Car, 3=Bicycle, 4=Electric Car -->
                      <option value="1">Motorbike</option>
                      <option value="2">Car</option>
                      <option value="3">Bicycle</option>
                      <option value="4">Electric Car</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label for="assignedSlot">Assign Slot *</label>
                    <select id="assignedSlot" name="assignedSlot" required>
                      <c:forEach items="${slots}" var="s">
                        <option value="${s.slotID}"
                                data-typeid="${s.typeID}"
                                data-status="${s.status}"
                                data-zonename="${s.zone.zoneName}"
                                data-slotname="${s.slotName}">
                          ${s.zone.zoneName} - ${s.slotName}
                        </option>
                      </c:forEach>
                    </select>
                  </div>
                  <button type="submit" class="btn btn-success btn-full-large">
                    Confirm Check-In
                  </button>
                </form>
              </div>

              <!-- Slot map by vehicle type + ticket preview -->
              <div class="slots-section">
                <h3><i class="fa-solid fa-map-location-dot"></i> Parking Slot Map</h3>

                <div id="slotGrid" class="slot-grid-container"></div>

                <hr class="mt-20 mb-10" />
                <h4><i class="fa-solid fa-ticket"></i> Ticket Preview</h4>
                <div id="ticketPreview" class="ticket-preview-box">
                  <p><strong>License Plate:</strong> <span id="previewPlate">-</span></p>
                  <p><strong>Vehicle Type:</strong> <span id="previewType">-</span></p>
                  <p><strong>Zone - Slot:</strong> <span id="previewSlot">-</span></p>
                  <p><strong>Entry Time:</strong> <span id="previewTime">-</span></p>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>

      <jsp:include page="../includes/footer.jsp" />

      <script src="static/js/staff_edit.js"></script>
    </body>

    </html>