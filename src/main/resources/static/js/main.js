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
};


$('a').click(function(event) {
    let userURL = $(this).attr('href');
    let uri = parseUrl(userURL);
    let myServerHost = parseUrl(serverHost);

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