$(document).ready(function(){

    $('#btn-login').on('click', function(){
        signUp();
    });

    $('#btn-register').on('click', function(){
        register();
    });
});

function signUp(){
    let userLogin = $('#userLogin').val();
    let userPassword = $('#userPassword').val();
    let userForLogining = {
        login: userLogin,
        password: userPassword
    };

    console.log(JSON.stringify(userForLogining));

    $.ajax({
        url: serverHost + 'login',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(userForLogining),
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
        complete: function(serverResponse){
            if (serverResponse.status == 400)alert("Your login or password is incorrect");
        }
    });
}

function register(){
    let firstname = $('#firstname').val();
    let lastname = $('#lastname').val();
    let nickname = $('#nickname').val();
    let email = $('#email').val();
    let age = $('#age').val();
    let password = $('#password').val();
    let userForRegistration = {
        firstname: firstname,
        lastname: lastname,
        nickname: nickname,
        email: email,
        age: age,
        password: password
    };

    console.log(JSON.stringify(userForRegistration));

    $.ajax({
        url: serverHost + 'registration',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(userForRegistration),
        complete: function(serverResponse){
            if(serverResponse.status == 201){
                alert("Registered Successfully");
                window.location.href = "login.html";
            }
            if(serverResponse.status == 409){
                alert("This user already exists")
            }
            if(serverResponse.status == 400){
                alert("Please complete all fields")
            }
        }
    });
}

