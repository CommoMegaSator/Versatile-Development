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

        let token = $('#_csrf').attr('content');
        let header = $('#_csrf_header').attr('content');

        $.ajax({
            url: document.location.href,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userForRegistration),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
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
                else {
                    $('#btn-register').show();
                    alert("Something went wrong")
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
            alert("Nickname should consist 4-25 symbols!");
            return false;
        }
        else if (form.email.length < 4 || form.email.length > 25) {
            alert("Email should consist 4-25 symbols!");
            return false;
        }
        else if (!form.password.match("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])([a-zA-Z0-9@$!%*?&]{8,25})$")){
            alert("Password should have at least: \n" +
                "1) 8-25 characters long;\n" +
                "2) One lowercase;\n" +
                "3) One uppercase;\n" +
                "4) One number;\n" +
                "5) One special character;\n" +
                "6) No whitespaces.");
            return false;
        }
        else if (form.password !== confirm){
            alert("Passwords do not match!");
            return false;
        }
        else return true;
        }
    else {
        alert("You have entered an invalid email address!");
        return false;
    }
}

function languageSelector() {
        let selectedOption = $('#locales').val();
        if (selectedOption != ''){
            window.location.replace(location.pathname + '?lang=' + selectedOption);
    }
}

function parseUrl(url) {
    let m = url.match(/^(([^:\/?#]+:)?(?:\/\/((?:([^\/?#:]*):([^\/?#:]*)@)?([^\/?#:]*)(?::([^\/?#:]*))?)))?([^?#]*)(\?[^#]*)?(#.*)?$/),
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
        let token = $('#_csrf').attr('content');
        let header = $('#_csrf_header').attr('content');

        $.ajax({
            url: document.location.href,
            type: 'DELETE',
            data: nicknameAsJson,
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            complete: function (serverResponse) {
                if (serverResponse.status == 400){alert("Something went wrong.");window.location.href = document.location.protocol + "//" + document.location.host + '/all';}
                if (serverResponse.status == 409){alert("You can`t delete your own account here!");window.location.href = document.location.protocol + "//" + document.location.host + '/all';}
                if (serverResponse.status == 200){alert(deleteMessage);window.location.href = document.location.protocol + "//" + document.location.host + '/all';}
            }
        });
    });
}

function sendUpdatedInfo() {
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
    let token = $('#_csrf').attr('content');
    let header = $('#_csrf_header').attr('content');

    $.ajax({
        url: document.location.href,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(userData),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
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

$('#updateAccountData').click(function () {
    let newPassword = $('#newpass').val();
    let retypedPassword = $('#retypedPass').val();

    if (newPassword !== retypedPassword)alert("Passwords is not same");
    else if (newPassword.length > 0 && retypedPassword.length > 0) {
        if (!newPassword.match("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])([a-zA-Z0-9@$!%*?&]{8,25})$")){
            alert("Password should have at least: \n" +
                "1) 8-25 characters long;\n" +
                "2) One lowercase;\n" +
                "3) One uppercase;\n" +
                "4) One number;\n" +
                "5) One special character;\n" +
                "6) No whitespaces.");
        } else sendUpdatedInfo();
    }
    else {
        sendUpdatedInfo();
    }
});