<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Registrar usuario</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet" href="/MDB-Free/login/assets/css/styles.min.css">
</head>

<body>
<div class="login-dark">
    <form method="post" action="/register-user">
        <h2 class="sr-only">Registro de usuario</h2>
        <div class="illustration"><i class="icon ion-ios-locked-outline"></i></div>
        <div class="form-group"><input class="form-control" type="text" name="username" placeholder="Username" value="<#if (username??)>${username}</#if>" required></div>
        <div class="form-group"><input class="form-control" type="text" name="name" placeholder="Nombre Completo" value="<#if (name??)>${name}</#if>" required></div>
        <div class="form-group"><input class="form-control" type="password" name="password" placeholder="Password" value="<#if (password??)>${password}</#if>" required></div>
        <#if (error??) >
            <div class="alert alert-danger" role="alert">
                Usuario ha sido tomado!
            </div>
        </#if>
        <#if (complete??)>
            <div class="alert alert-danger" role="alert">
                Complete todos los datos!
            </div>
        </#if>
        <div class="form-group"><button class="btn btn-primary btn-block" type="submit">Log In</button></div></form>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
</body>

</html>