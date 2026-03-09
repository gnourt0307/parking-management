<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!doctype html>
    <html lang="en">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Manage Zones</title>
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
        .form-group textarea {
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
              <h2><i class="fa-solid fa-layer-group"></i> Zone Management</h2>
              <button class="btn btn-success" onclick="openAddModal()"><i class="fa-solid fa-plus"></i> Add New
                Zone</button>
            </div>

            <c:if test="${not empty sessionScope.successMsg}">
              <div class="alert alert-success">${sessionScope.successMsg}</div>
              <c:remove var="successMsg" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.errorMsg}">
              <div class="alert alert-danger">${sessionScope.errorMsg}</div>
              <c:remove var="errorMsg" scope="session" />
            </c:if>

            <form action="Zones" method="GET" class="filter-section d-flex align-items-center"
              style="gap: 15px; margin-bottom: 20px;">
              <div style="display:flex; align-items:center; gap:8px;">
                <label for="zoneFilter" style="margin:0; white-space:nowrap;">Select Zone:</label>
                <select name="zoneFilter" id="zoneFilter" class="filter-input" style="min-width: 150px;">
                  <option value="all" ${empty param.zoneFilter or param.zoneFilter eq 'all' ? 'selected' : '' }>All
                    Zones</option>
                  <c:forEach items="${allZones}" var="z">
                    <c:choose>
                      <c:when test="${param.zoneFilter == 'all'}">
                        <option value="${z.zoneID}">${z.zoneName}</option>
                      </c:when>
                      <c:otherwise>
                        <option value="${z.zoneID}" ${param.zoneFilter==z.zoneID ? 'selected' : '' }>${z.zoneName}
                        </option>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>

              <div style="display:flex; align-items:center; gap:8px; flex-grow: 1;">
                <label for="search" style="margin:0; white-space:nowrap;">Vehicle Type:</label>
                <input type="text" name="search" id="search" class="filter-input" value="${param.search}"
                  placeholder="Vehicle Type..." style="flex-grow: 1;">
              </div>

              <div style="display:flex; gap:10px;">
                <button type="submit" class="btn btn-search">Search</button>
                <a href="Zones" class="btn btn-secondary"
                  style="text-decoration: none; padding: 8px 12px; border-radius:4px; border:1px solid #ccc; background:#f4f4f4; color:#333; display: flex; align-items: center; justify-content: center; height: 100%; box-sizing: border-box;">Clear</a>
              </div>
            </form>

            <table class="data-table">
              <thead>
                <tr>
                  <th style="width: 15%;">Zone Name</th>
                  <th style="width: 20%;">Vehicle Type</th>
                  <th style="width: 25%;">Description</th>
                  <th style="width: 10%;">Capacity</th>
                  <th style="width: 30%; text-align: left;">Actions</th>
                </tr>
              </thead>
              <tbody>
                <c:choose>
                  <c:when test="${empty zones}">
                    <tr>
                      <td colspan="5" style="text-align: center;">No zones found.</td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach items="${zones}" var="zone">
                      <tr>
                        <td>${zone.zoneName}</td>
                        <td style="text-transform: capitalize;">${zone.typeName}</td>
                        <td>${zone.description}</td>
                        <td>${zone.capacity} slots</td>
                        <td style="white-space: nowrap; display: flex; gap: 5px;">
                          <button class="btn btn-info btn-sm"
                            onclick="openDetailModal('${zone.zoneName}', '${zone.capacity}', '${zone.typeName}', '${zone.description}')"><i
                              class="fa-solid fa-circle-info"></i> Detail</button>
                          <button class="btn btn-sm"
                            onclick="openEditModal(${zone.zoneID}, '${zone.zoneName}', '${zone.typeName}', '${zone.description}')"><i
                              class="fa-solid fa-pen-to-square"></i> Edit</button>
                          <form action="Zones" method="POST" style="display:inline;"
                            onsubmit="return confirm('Are you sure you want to delete this zone?');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="zoneID" value="${zone.zoneID}">
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

      <!-- Add Zone Modal -->
      <div id="addModal" class="modal">
        <div class="modal-content">
          <span class="close-btn" onclick="closeModal('addModal')">&times;</span>
          <h3>Add New Zone</h3>
          <form action="Zones" method="POST">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
              <label>Zone Name</label>
              <input type="text" name="zoneName" required>
            </div>
            <div class="form-group">
              <label>Vehicle Type</label>
              <input type="text" name="vehicleTypeName" class="filter-input" required style="width:100%;"
                list="vehicleTypesList" placeholder="Select or type new...">
              <datalist id="vehicleTypesList">
                <c:forEach items="${vehicleTypesList}" var="t">
                  <option value="${t.typeName}"></option>
                </c:forEach>
              </datalist>
            </div>
            <div class="form-group">
              <label>Description</label>
              <textarea name="description" rows="3" required></textarea>
            </div>
            <button type="submit" class="btn btn-success" style="width: 100%;">Save Zone</button>
          </form>
        </div>
      </div>

      <!-- Edit Zone Modal -->
      <div id="editModal" class="modal">
        <div class="modal-content">
          <span class="close-btn" onclick="closeModal('editModal')">&times;</span>
          <h3>Edit Zone</h3>
          <form action="Zones" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="zoneID" id="editZoneID">
            <div class="form-group">
              <label>Zone Name</label>
              <input type="text" name="zoneName" id="editZoneName" required>
            </div>
            <div class="form-group">
              <label>Vehicle Type</label>
              <input type="text" name="vehicleTypeName" id="editVehicleTypeName" class="filter-input" required
                style="width:100%;" list="editVehicleTypesList" placeholder="Select or type new...">
              <datalist id="editVehicleTypesList">
                <c:forEach items="${vehicleTypesList}" var="t">
                  <option value="${t.typeName}"></option>
                </c:forEach>
              </datalist>
            </div>

            <div class="form-group">
              <label>Description</label>
              <textarea name="description" id="editDescription" rows="3" required></textarea>
            </div>
            <button type="submit" class="btn btn-success" style="width: 100%;">Update Zone</button>
          </form>
        </div>
      </div>

      <!-- Detail Zone Modal -->
      <div id="detailModal" class="modal">
        <div class="modal-content" style="max-width: 500px;">
          <span class="close-btn" onclick="closeModal('detailModal')">&times;</span>
          <h3 style="margin-top: 0; color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;">Zone Details
          </h3>
          <div
            style="background: #f9f9f9; padding: 15px; border-radius: 8px; border-left: 4px solid #4CAF50; margin-top: 15px;">
            <p style="margin: 8px 0;"><strong>Zone Name:</strong> <span id="detailZoneName"></span></p>
            <p style="margin: 8px 0;"><strong>Vehicle Types:</strong> <span id="detailVehicleTypes"></span></p>
            <p style="margin: 8px 0;"><strong>Capacity:</strong> <span id="detailCapacity"></span> slots</p>
            <p style="margin: 8px 0;"><strong>Description:</strong> <span id="detailDescription"></span></p>
          </div>
          <div style="margin-top: 20px; text-align: right;">
            <button type="button" class="btn btn-secondary" onclick="closeModal('detailModal')">Close</button>
          </div>
        </div>
      </div>

      <script>
        function openDetailModal(name, cap, vtypes, desc) {
          document.getElementById('detailZoneName').textContent = name;
          document.getElementById('detailCapacity').textContent = cap;
          document.getElementById('detailVehicleTypes').textContent = vtypes;
          document.getElementById('detailDescription').textContent = desc !== 'null' && desc !== '' ? desc : 'N/A';
          document.getElementById('detailModal').style.display = 'block';
        }

        function openAddModal() {
          document.getElementById('addModal').style.display = 'block';
        }
        function openEditModal(id, name, typeName, desc) {
          document.getElementById('editZoneID').value = id;
          document.getElementById('editZoneName').value = name;
          document.getElementById('editVehicleTypeName').value = typeName;
          document.getElementById('editDescription').value = desc !== 'null' && desc !== '' ? desc : '';
          document.getElementById('editModal').style.display = 'block';
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