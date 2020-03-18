$(document).ready(function(){

    $('#btn-register').on('click', function(){
        register();
    });
});

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
                alert("To complete the registration, go to the URL that was sent on your email!");
                window.location.href = "login";
            }
            if(serverResponse.status == 409){
                alert("This user already exists!")
            }
            if(serverResponse.status == 400){
                alert("Credentials is not valid!")
            }
        }
    });
}

function languageSelector() {
        let selectedOption = $('#locales').val();
        if (selectedOption != ''){
            window.location.replace(location.pathname + '?lang=' + selectedOption);
    }
}