<%@ page contentType="text/html;charset=utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script>
        $(document).ready(function() {
            $("#loginForm").on("submit", function(event) {
                event.preventDefault();
                const id = event.target.id.value;
                const pwd = event.target.pwd.value;
                $.ajax({
                    type:"POST",
                    url:"/login/loginCheck",
                    data:{id:id, pwd:pwd},
                    success:function(data) {
                        console.log(data);
                    },
                    error:function() {
                        console.log("에러");
                    }
                });
            });
        });
    </script>
</head>
<body>
    <form id="loginForm">
        <p><input type="text" name="id"></p>
        <p><input type="text" name="pwd"></p>
        <p><input type="submit"></p>
    </form>
</body>
</html>