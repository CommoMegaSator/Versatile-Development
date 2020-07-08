$(document).ready(function(){

    $('#btn-register').on('click', function(){
        document.body.style.cursor = "progress";
        register();
    });
});

function register(){
    let nickname = $('#nickname').val();
    let email = $('#email').val().toLowerCase();
    let password = $('#password').val();
    let userForRegistration = {
        nickname: nickname,
        email: email,
        password: password
    };

    if (registrationFormValidation(userForRegistration)){
        $('#btn-register').hide();
        $.ajax({
            url: document.location.href,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userForRegistration),
            complete: function(serverResponse){
                document.body.style.cursor = "auto";
                if(serverResponse.status == 409){
                    $('#btn-register').show();
                    alert("This user already exists!")
                }
                else if(serverResponse.status == 201){
                    alert("To complete the registration, go to the URL that was sent on your email!");
                    window.location.href = "login";
                }
                else if(serverResponse.status == 400){
                    $('#btn-register').show();
                    alert("Credentials is not valid!")
                }
            }
        });
    }
}

function registrationFormValidation(form) {
    let confirm = $('#confirm').val();

    if (form.nickname.length === 0 || form.email.length === 0 || form.password.length === 0 || confirm.length === 0) {
        alert("All fields should be filled!");return false;}

    else if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(form.email)){
        if (form.nickname.length < 4 || form.nickname.length > 25) {
            alert("Nickname should consist 4-25 symbols!");return false;}
        else if (form.email.length < 4 || form.email.length > 25) {
            alert("Email should consist 4-25 symbols!");return false;}
        else if (form.password.length < 8 || form.password.length > 25 || confirm.length < 8 || confirm.length > 25) {
            alert("Password should consist 8-25 symbols!");return false;}
        else if (form.password !== confirm){
            alert("Passwords do not match!");return false;}
        return true;}

    else {alert("You have entered an invalid email address!");return false;}
}

function languageSelector() {
        let selectedOption = $('#locales').val();
        if (selectedOption != ''){
            window.location.replace(location.pathname + '?lang=' + selectedOption);
    }
}

function parseUrl(url) {
    var m = url.match(/^(([^:\/?#]+:)?(?:\/\/((?:([^\/?#:]*):([^\/?#:]*)@)?([^\/?#:]*)(?::([^\/?#:]*))?)))?([^?#]*)(\?[^#]*)?(#.*)?$/),
        r = {
            hash: m[10] || "",
            host: m[3] || "",
            hostname: m[6] || "",
            href: m[0] || "",
            origin: m[1] || "",
            pathname: m[8] || (m[1] ? "/" : ""),
            port: m[7] || "",
            protocol: m[2] || "",
            search: m[9] || "",
            username: m[4] || "",
            password: m[5] || ""
        };
    if (r.protocol.length == 2) {
        r.protocol = "file:///" + r.protocol.toUpperCase();
        r.origin = r.protocol + "//" + r.host;
    }
    r.href = r.origin + r.pathname + r.search + r.hash;
    return r;
}


$('a').click(function(event) {
    let userURL = $(this).attr('href');
    let uri = parseUrl(userURL);
    let myServerHost = parseUrl(document.location.href);

    if (myServerHost.host != uri.host && uri.host != "") {
        event.preventDefault();
        $('#redirectingModal').modal('show');
        $('#redirecter').click(function(){window.location.href = userURL});
    }
});


$('#deleteAccount').click(function(event){
    event.preventDefault();
    $('#deleteAccountModal').modal('show');
});

$('#exitButton').click(function(event){
    event.preventDefault();
    $('#exitingModal').modal('show');
});

$('#cancel-deleting').click(function (event) {
    event.preventDefault();
    location.reload();
});

function deleteUser(nickname) {
    $('#deleteAccountModal').modal('show');

    $('#deleteAccountButton').on('click', function () {
        let deleteMessage = 'Deleted Successfully';
        let nicknameAsJson =  JSON.stringify({nickname : nickname, token: deleteToken});

        $.ajax({
            url: document.location.href,
            type: 'DELETE',
            data: nicknameAsJson,
            contentType: "application/json; charset=utf-8",
            complete: function (serverResponse) {
                if (serverResponse.status == 400){alert("Something went wrong.");window.location.href = document.location.protocol + "//" + document.location.host + '/all';}
                if (serverResponse.status == 409){alert("You can`t delete your own account here!");window.location.href = document.location.protocol + "//" + document.location.host + '/all';}
                if (serverResponse.status == 200){alert(deleteMessage);window.location.href = document.location.protocol + "//" + document.location.host + '/all';}
            }
        });
    });
}

$('#updateAccountData').click(function () {
    if ($('#newpass').val() !== $('#retypedPass').val())alert("Passwords is not same");
    else {
        let userData = {
            firstname: $('#firstname').val(),
            lastname: $('#lastname').val(),
            email: $('#email').val(),
            gender: $('#gender').val(),
            birthday: $('#birthday').val(),
            nationality: $('#nationality').val().toLowerCase(),
            aboutUser: $('#about-me').val(),
            password: $('#newpass').val()
        };

        $.ajax({
            url: document.location.href,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            complete: function (serverResponse) {
                if (serverResponse.status == 200) {
                    alert("Account was updated successfully");
                    location.reload();
                } else if (serverResponse.status == 204) {
                    alert("User data is empty!")
                }
            }
        });
    }
});
