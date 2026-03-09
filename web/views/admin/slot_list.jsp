<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!doctype html>
    <html lang="en">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Manage Slots</title>
      <link rel="stylesheet" href="static/css/style.css" />
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
      <style>
        .modal {
          display: none;
          position: fixed;
          z-index: 1000;
          left: 0;
          top: 0;
          width: 100%;
          height: 100%;
          background-color: rgba(0, 0, 0, 0.5);
        }

        .modal-content {
          background-color: #fff;
          margin: 10% auto;
          padding: 20px;
          border-radius: 8px;
          width: 400px;
          max-width: 90%;
        }

        .close-btn {
          color: #aaa;
          float: right;
          font-size: 28px;
          font-weight: bold;
          cursor: pointer;
        }

        .close-btn:hover {
          color: #000;
        }

        .form-group {
          margin-bottom: 15px;
        }

        .form-group label {
          display: block;
          margin-bottom: 5px;
          font-weight: bold;
        }

        .form-group input,
        .form-group select {
          width: 100%;
          padding: 8px;
          border: 1px solid #ccc;
          border-radius: 4px;
        }

        .alert {
          padding: 10px;
          margin-bottom: 15px;
          border-radius: 4px;
        }

        .alert-success {
          background-color: #d4edda;
          color: #155724;
          border: 1px solid #c3e6cb;
        }

        .alert-danger {
          background-color: #f8d7da;
          color: #721c24;
          border: 1px solid #f5c6cb;
        }
      </style>
    </head>

    <body>
      <div class="admin-layout">
        <!-- Left Sidebar -->
        <jsp:include page="../includes/sidebar_admin.jsp" />

        <!-- Main Content Area -->
        <main class="admin-main">
          <div class="container main-content full-width">
            <div class="page-header gap-10">
              <h2><i class="fa-solid fa-car-side"></i> Slot Management</h2>
              <button class="btn btn-success" onclick="openAddModal()"><i class="fa-solid fa-plus"></i> Add New
                Slot</button>
            </div>

            <c:if test="${not empty sessionScope.successMsg}">
              <div class="alert alert-success">${sessionScope.successMsg}</div>
              <c:remove var="successMsg" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.errorMsg}">
              <div class="alert alert-danger">${sessionScope.errorMsg}</div>
              <c:remove var="errorMsg" scope="session" />
            </c:if>

            <form action="Slots" method="GET" class="filter-section d-flex align-items-center"
              style="gap: 15px; margin-bottom: 20px; flex-wrap: nowrap;">
              <div style="display:flex; align-items:center; gap:8px;">
                <label for="zoneFilter" style="margin:0; white-space:nowrap;">Filter by Zone:</label>
                <select name="zoneFilter" id="zoneFilter" class="filter-select" style="min-width: 120px;">
                  <option value="all" ${empty param.zoneFilter or param.zoneFilter=='all' ? 'selected' : '' }>All Zones
                  </option>
                  <c:forEach items="${zones}" var="z">
                    <c:set var="isSelected" value="false" />
                    <c:if test="${not empty param.zoneFilter and param.zoneFilter != 'all'}">
                      <c:if test="${param.zoneFilter == z.zoneID}">
                        <c:set var="isSelected" value="true" />
                      </c:if>
                    </c:if>
                    <option value="${z.zoneID}" ${isSelected ? 'selected' : '' }>${z.zoneName}</option>
                  </c:forEach>
                </select>
              </div>

              <div style="display:flex; align-items:center; gap:8px; flex-grow: 1;">
                <label for="search" style="margin:0; white-space:nowrap;">Search Slot:</label>
                <input type="text" name="search" id="search" class="filter-input" value="${param.search}"
                  placeholder="Slot Name..." style="flex-grow: 1;">
              </div>

              <div style="display:flex; gap:10px;">
                <button type="submit" class="btn btn-search">Search</button>
                <a href="Slots" class="btn btn-secondary"
                  style="text-decoration: none; padding: 8px 12px; border-radius:4px; border:1px solid #ccc; background:#f4f4f4; color:#333; display: flex; align-items: center; justify-content: center; height: 100%; box-sizing: border-box;">Clear</a>
              </div>
            </form>

            <table class="data-table">
              <thead>
                <tr>
                  <th style="width: 15%;">Zone</th>
                  <th style="width: 15%;">Slot Name</th>
                  <th style="width: 20%;">Vehicle Type</th>
                  <th style="width: 20%;">Status</th>
                  <th style="width: 30%; text-align: left;">Actions</th>
                </tr>
              </thead>
              <tbody>
                <c:choose>
                  <c:when test="${empty slots}">
                    <tr>
                      <td colspan="5" style="text-align: center;">No slots found.</td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach items="${slots}" var="slot">
                      <tr>
                        <td>${slot.zone.zoneName}</td>
                        <td>${slot.slotName}</td>
                        <td>
                          <c:forEach items="${types}" var="t">
                            <c:if test="${t.typeID == slot.typeID}">${t.typeName}</c:if>
                          </c:forEach>
                        </td>
                        <td>
                          <c:set var="upperStatus" value="${slot.status.toUpperCase()}" />
                          <c:choose>
                            <c:when test="${upperStatus == 'AVAILABLE'}">
                              <span class="status-available"
                                style="text-transform: capitalize;">${slot.status.toLowerCase()}</span>
                            </c:when>
                            <c:when test="${upperStatus == 'OCCUPIED'}">
                              <span class="status-occupied"
                                style="text-transform: capitalize;">${slot.status.toLowerCase()}</span>
                            </c:when>
                            <c:otherwise>
                              <span
                                style="font-weight: bold; color: #f39c12; text-transform: capitalize;">${slot.status.toLowerCase()}</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td style="white-space: nowrap; display: flex; gap: 5px;">
                          <c:set var="ticket" value="${activeTickets[slot.slotID]}" />
                          <c:set var="tPlate" value="${empty ticket ? 'N/A' : ticket.licensePlate}" />
                          <c:set var="tOwner" value="${empty ticket.customer ? 'N/A' : ticket.customer.fullName}" />
                          <c:set var="tPhone" value="${empty ticket.customer ? 'N/A' : ticket.customer.phone}" />
                          <c:set var="tTime"
                            value="${empty ticket ? 'N/A' : ticket.entryTime.toString().replace('T', ' ')}" />
                          <button class="btn btn-info btn-sm"
                            onclick="openDetailModal('${slot.slotName}', '${slot.zone.zoneName}', '${slot.status}', '${tPlate}', '${tOwner}', '${tPhone}', '${tTime}')">
                            <i class="fa-solid fa-eye"></i> Detail
                          </button>
                          <button class="btn btn-sm"
                            onclick="openEditModal(${slot.slotID}, '${slot.slotName}', ${slot.zone.zoneID}, ${slot.typeID}, '${slot.status}')"><i
                              class="fa-solid fa-pen-to-square"></i> Edit</button>
                          <form action="Slots" method="POST" style="display:inline;"
                            onsubmit="return confirm('Are you sure you want to delete this slot?');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="slotID" value="${slot.slotID}">
                            <button type="submit" class="btn btn-danger btn-sm"><i class="fa-solid fa-trash"></i>
                              Delete</button>
                          </form>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </tbody>
            </table>
          </div>

          <jsp:include page="../includes/footer.jsp" />
        </main>
      </div>

      <!-- Add Slot Modal -->
      <div id="addModal" class="modal">
        <div class="modal-content">
          <span class="close-btn" onclick="closeModal('addModal')">&times;</span>
          <h3>Add New Slot</h3>
          <form action="Slots" method="POST">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
              <label>Zone</label>
              <select name="zoneID" required>
                <c:forEach items="${zones}" var="z">
                  <option value="${z.zoneID}">${z.zoneName}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label>Slot Name</label>
              <input type="text" name="slotName" required>
            </div>
            <div class="form-group">
              <label>Vehicle Type</label>
              <select name="typeID" required>
                <c:forEach items="${types}" var="t">
                  <option value="${t.typeID}">${t.typeName}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label>Status</label>
              <select name="status" required>
                <option value="Available">Available</option>
                <option value="Maintenance">Maintenance</option>
              </select>
            </div>
            <button type="submit" class="btn btn-success" style="width: 100%;">Save Slot</button>
          </form>
        </div>
      </div>

      <!-- Edit Slot Modal -->
      <div id="editModal" class="modal">
        <div class="modal-content">
          <span class="close-btn" onclick="closeModal('editModal')">&times;</span>
          <h3>Edit Slot</h3>
          <form action="Slots" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="slotID" id="editSlotID">
            <div class="form-group">
              <label>Zone</label>
              <select name="zoneID" id="editZoneID" required>
                <c:forEach items="${zones}" var="z">
                  <option value="${z.zoneID}">${z.zoneName}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label>Slot Name</label>
              <input type="text" name="slotName" id="editSlotName" required>
            </div>
            <div class="form-group">
              <label>Vehicle Type</label>
              <select name="typeID" id="editTypeID" required>
                <c:forEach items="${types}" var="t">
                  <option value="${t.typeID}">${t.typeName}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label>Status</label>
              <select name="status" id="editStatus" required>
                <option value="Available">Available</option>
                <option value="Occupied">Occupied</option>
                <option value="Maintenance">Maintenance</option>
              </select>
            </div>
            <button type="submit" class="btn btn-success" style="width: 100%;">Update Slot</button>
          </form>
        </div>
      </div>

      <!-- Detail Slot Modal -->
      <div id="detailModal" class="modal">
        <div class="modal-content">
          <span class="close-btn" onclick="closeModal('detailModal')">&times;</span>
          <h3>Slot Details</h3>
          <div style="margin-top: 15px; line-height: 1.8;">
            <p><strong>Slot Name:</strong> <span id="detailSlotName"></span></p>
            <p><strong>Zone:</strong> <span id="detailZoneName"></span></p>
            <p><strong>Status:</strong> <span id="detailStatus" style="text-transform: capitalize;"></span></p>

            <div id="detailOwnerInfo" style="display: none;">
              <hr style="margin: 15px 0; border: 0; border-top: 1px solid #eee;">
              <p><strong>License Plate:</strong> <span id="detailLicensePlate"></span></p>
              <p><strong>Owner Name:</strong> <span id="detailOwnerName"></span></p>
              <p><strong>Owner Phone:</strong> <span id="detailOwnerPhone"></span></p>
              <p><strong>Entry Time:</strong> <span id="detailEntryTime"></span></p>
            </div>
            <div id="detailEmptyInfo" style="display: none;">
              <hr style="margin: 15px 0; border: 0; border-top: 1px solid #eee;">
              <p style="color: #666; font-style: italic; text-align: center;">No vehicle is currently parked here.</p>
            </div>
          </div>
          <div style="margin-top: 20px; text-align: right;">
            <button class="btn btn-secondary" onclick="closeModal('detailModal')">Close</button>
          </div>
        </div>
      </div>

      <script>
        function openAddModal() {
          document.getElementById('addModal').style.display = 'block';
        }
        function openEditModal(id, name, zone, type, status) {
          document.getElementById('editSlotID').value = id;
          document.getElementById('editSlotName').value = name;
          document.getElementById('editZoneID').value = zone;
          document.getElementById('editTypeID').value = type;
          document.getElementById('editStatus').value = status;
          document.getElementById('editModal').style.display = 'block';
        }
        function openDetailModal(name, zone, status, plate, owner, phone, time) {
          document.getElementById('detailSlotName').textContent = name;
          document.getElementById('detailZoneName').textContent = zone;
          document.getElementById('detailStatus').textContent = status.toLowerCase();

          document.getElementById('detailLicensePlate').textContent = plate ? plate : 'N/A';
          document.getElementById('detailOwnerName').textContent = owner ? owner : 'N/A';
          document.getElementById('detailOwnerPhone').textContent = phone ? phone : 'N/A';
          document.getElementById('detailEntryTime').textContent = time ? time.substring(0, 19) : 'N/A';

          document.getElementById('detailModal').style.display = 'block';
        }
        function closeModal(modalId) {
          document.getElementById(modalId).style.display = 'none';
        }
        // Close modal if user clicks outside of it
        window.onclick = function (event) {
          if (event.target.className === 'modal') {
            event.target.style.display = 'none';
          }
        }
      </script>
    </body>

    </html>