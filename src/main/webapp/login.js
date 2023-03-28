const jwt = require("jsonwebtoken");
require("dotenv").config();

function getToken(cookies){
    let token;
    cookies.split(';').forEach(cookie => {
        if(cookie.split('=')[0] === "token"){
            token = cookie.split('=')[1];
        }
    });

    return token;
}

function sendToPage(){
    token = getToken(document.cookie)
    const user = jwt.verify(token, process.env.MY_SECRET);
    fetch('./login')
        .then(response => {
        if(response.status != 200){ //created
            //response.json().then(data => showExamsOfStudent(data.student.studentId, data.student.firstname, data.student.lastname));
            console.log("wow");
        }else{
            alert("Invalid Request " + response.status + " " + response.statusText);
        }
    })
}