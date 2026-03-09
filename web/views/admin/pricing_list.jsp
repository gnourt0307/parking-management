<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="models.Pricing" %>
<%@page import="models.VehicleType" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Pricing Configuration</title>
        <link rel="stylesheet" href="static/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    </head>
    <body>
        <div class="admin-layout">
            <!-- Left Sidebar -->
            <jsp:include page="../includes/sidebar_admin.jsp"/>

            <!-- Main Content Area -->
            <main class="admin-main">
                <div class="container main-content full-width">
                    <div class="page-header">
                        <h2><i class="fa-solid fa-money-bill"></i> Pricing Configuration</h2>
                        <button class="btn btn-success"><i class="fa-solid fa-plus"></i> Add New Pricing Rule</button>
                    </div>

                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Vehicle Type</th>
                                <th>Hourly Rate</th>
                                <th>Daily Rate</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${requestScope.pricingList != null && !requestScope.pricingList.isEmpty()}">
                                <c:forEach var="pricing" items="${requestScope.pricingList}">
                                    <tr>
                                        <td>${pricing.vehicleType.typeName}</td>
                                        <td>$${pricing.hourlyRate}</td>
                                        <td>$${pricing.dailyRate}</td>
                                        <td>
                                            <button class="btn btn-sFm"><i class="fa-solid fa-pen-to-square"></i> Edit</button>
                                            <button class="btn btn-danger btn-sm"><i class="fa-solid fa-trash"></i> Delete</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <jsp:include page="../includes/footer.jsp"/>
            </main>
        </div>
        <script src="../static/js/admin_edit.js"></script>
    </body>
</html>



