let token;
function getToken(){
    var cookies =  document.cookie;
    cookies.split(';').forEach(cookie => {
        if(cookie.split('=')[0] === "token"){
            token = cookie.split('=')[1];
            console.log(token)
        }
    });
}

function onloadMethods(){
    getToken();
    if(token != null){
        window.location.href = "./examView.html";
    }
}

function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/;SameSite=None";
}

function loginMe(){
    let exam = {
        email: document.getElementById("emailInput").value,
        password: document.getElementById("passwordInput").value
    };

    fetch('./auth/login',
        {
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body: JSON.stringify(exam)
        })
        .then(response =>
        {
            if(response.status == 200){
                response.json().then(data => {
                    console.log(data);
                    setCookie("token",data, 0.1);
                    window.location.href = "./examView.html";
                });
            }else{
                alert("Invalid Request " + response.status + " " + response.statusText);
            }
        })
}

function registerMe(){
    let exam = {
        email: document.getElementById("emailInput").value,
        password: document.getElementById("passwordInput").value
    };

    fetch('./auth/register',
        {
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body: JSON.stringify(exam)
        })
        .then(response =>
        {
            if(response.status == 200){
                response.json().then(data => console.log(data));
            }else{
                alert("Invalid Request " + response.status + " " + response.statusText);
            }
        })
}

function testToken(){
    fetch('./api/classname/all',{
        headers:{
            Authorization: "Bearer " + token
        }
    })
        .then(response => response.json())
        .then(data =>
        {
            data.forEach(d => console.log(d));
        });
}