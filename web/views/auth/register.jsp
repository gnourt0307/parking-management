<!doctype html>
<html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Register</title>
        <link rel="stylesheet" href="static/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    </head>

    <body>
        <jsp:include page="../includes/navbar_mainpage.jsp" />

        <div class="container main-content">
            <div class="auth-container">
                <h2><i class="fa-solid fa-user-plus"></i> Guest Registration</h2>
                <form action="Register" method="POST">
                    <div class="form-group">
                        <label for="fullname">Full Name</label>
                        <input type="text" id="fullname" name="fullname" required />
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone number</label>
                        <input type="text" id="phone" name="phone" required />
                    </div>
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" required />
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required />
                    </div>

                    <button type="submit" class="btn w-100"><i class="fa-solid fa-address-card"></i> Register</button>
                </form>
                <p style="color: #d9534f" >${error}</p>
                <p>Already have an account? <a href="Login">Login here</a></p>
            </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />
    </body>

</html>