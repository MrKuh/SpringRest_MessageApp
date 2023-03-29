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


function displayExamBlock(display) {
    if(display){
        document.getElementById("examBlock").style.display = "inline";
    }else{
        document.getElementById("examBlock").style.display = "none";
    }
}
function displayEditExamBlock(display) {
    if(display){
        document.getElementById("editExamBlock").style.display = "inline";
    }else{
        document.getElementById("editExamBlock").style.display = "none";
    }
}

function onloadMethods(){
    getToken();
    if(token != null){
        console.log(token);
        getAllClassNames();
    }else{
        window.location.href = "./login.html";
    }
}

function getAllClassNames(){
    fetch('./api/classname/all',{
        headers:{
            Authorization: "Bearer " + token
        }
    })
        .then(response =>
        {
            console.log(response.status )
            if(response.status == 200){ //created
                response.json().then(data => {
                    let html = "";
                    data.forEach(d => html += `<option value=\"${d.classId}\">${d.classname}</option>`);


                    document.getElementById("classnameSelect").innerHTML = html;
                    getStudentsOfClass(0);
                });
            }else{
                console.log(token);
            }


        });


}

var page = 0;
var pageAmount = 1;


function getPage(pageNo){
    getStudentsOfClass(pageNo);
}

function getNextPage(){
    if(page < pageAmount-1){
        page++;
        getStudentsOfClass(page);
    }
}

function getPreviousPage(){
    if(page > 0){
        page--;
        getStudentsOfClass(page);
    }


}


function getStudentsOfClass(pageNo){
    var classId = document.getElementById("classnameSelect").value;

    console.log(classId);

    fetch('./api/student/classname/' + classId + "?pageNo="+ pageNo,{
        headers:{
            Authorization: "Bearer " + token
        }
    })
    .then(response => response.json())
    .then(data =>
    {
        pageAmount = data.totalPages;
        showExamsOfNewStudent(data.content[0].studentId,data.content[0].firstname,data.content[0].lastname);
        let htmlTable = "";
        data.content.forEach(d => htmlTable += `<tr>
                                        <td>${d.firstname}</td>
                                        <td>${d.lastname}</td>
                                        <td><button type="button" class="btn btn-outline-dark" onclick="showExamsOfNewStudent(${d.studentId},'${d.firstname}','${d.lastname}')">View Exams</button></td>
                                    </tr>`);

        let htmlPages =
            `<li class="page-item"><a class="page-link" href="#" onclick="getPreviousPage()">Previous</a></li>`;

        for (let i = 0; i < pageAmount; i++) {
            htmlPages += `<li class="page-item"><a class="page-link" href="#" onclick="getPage(`+ i +`)">`+ (i+1) +`</a></li>`;

        }
        htmlPages += `<li class="page-item"><a class="page-link" href="#" onclick="getNextPage()">Next</a></li>`;

        document.getElementById("studentTable").innerHTML = htmlTable;
        document.getElementById("paginationList").innerHTML = htmlPages;
        displayExamBlock(false);
        displayEditExamBlock(false);
    });
}
function showExamsOfNewStudent(studentId, firstname, lastname){
    displayEditExamBlock(false);
    showExamsOfStudent(studentId,firstname,lastname);

}

function showExamsOfStudent(studentId, firstname, lastname){
    fetch('./api/exam/student/' + studentId,{
        headers:{
            Authorization: "Bearer " + token
        }
    })
        .then(response => response.json())
        .then(data =>
        {
            let htmlTable = "";
            data.forEach(d => htmlTable += `<tr>
                                        <td>${d.dateOfExam}</td>
                                        <td>${d.duration}</td>
                                        <td>${d.subject.shortname}</td>
                                        <td><button type="button" class="btn btn-outline-warning no-wrap" 
                                              onclick="getEditExamOfStudent(${d.examId},${d.studentId},'${d.dateOfExam}',${d.duration},${d.subject.subjectId})">
                                              Edit
                                            </button>
                                        <button type="button" class="btn btn-outline-danger no-wrap" 
                                              onclick="removeExamOfStudent(${d.examId})">
                                              Remove
                                            </button></td>
                                    </tr>`);
            document.getElementById("examTBody").innerHTML = htmlTable;
            document.getElementById("studentHeading").innerHTML = "Exams of Student: "+firstname+ " "+ lastname;
            getAddExamRow(studentId);
        });

}

function getAddExamRow(studentId){
    fetch('./api/subject/all',{
        headers:{
            Authorization: "Bearer " + token
        }
    })
        .then(response => response.json())
        .then(data =>
        {
            let html = `<tr>
                            <td> <input type="date" class="form-control" id="dateOfExamInput"></td>
                            <td> <input type="number" class="form-control" id="durationInput"></td>
                            <td> <select class="form-select" id="subjectInput" aria-label="Default select example">`;
            data.forEach(d => html += `<option value=\"${d.subjectId}\">${d.shortname}</option>`);

            html +=`        </select></td>
                            <td>
                            <button type="button" class="btn btn-outline-success no-wrap" 
                                onclick="addExamToStudent(dateOfExamInput,durationInput,${studentId},subjectInput)">
                            Add
                            </button></td>
                          </tr>`;


            document.getElementById("examTBody").innerHTML += html
            displayExamBlock(true);
        });
}

function addExamToStudent(dateOfExamInput,durationInput,studentId,subjectInput){
    let exam = {
        dateOfExam: dateOfExamInput.value,
        duration: durationInput.value,
        studentId: studentId,
        subjectId: subjectInput.value
    };

    fetch('./api/exam',
        {
            method:"POST",
            headers:{
                "Content-Type":"application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(exam)
        })
        .then(response =>
        {
            if(response.status == 201){ //created
                response.json().then(data => showExamsOfStudent(data.student.studentId, data.student.firstname, data.student.lastname));
            }else{
                alert("Invalid Request " + response.status + " " + response.statusText);
            }
        })
}

function removeExamOfStudent(examId){

    fetch('./api/exam/' + examId,
        {
            method:"DELETE",
            headers:{
                Authorization: "Bearer " + token
            }
        })
        .then(response =>
        {
            if(response.status == 200){
                response.json().then(data => showExamsOfStudent(data.student.studentId, data.student.firstname, data.student.lastname));
            }else{
                alert("Invalid Request " + response.status + " " + response.statusText);
            }
        })
}

function getEditExamOfStudent(examId , studentId, dateOfExam, duration, subjectId){

    fetch('./api/subject/all',{
        headers:{
            Authorization: "Bearer " + token
        }
    })
        .then(response => response.json())
        .then(data =>
        {
            let html = `<tr>
                            <td> <input type="date" class="form-control" id="dateOfExamEditInput" value="${dateOfExam}"></td>
                            <td> <input type="number" class="form-control" id="durationEditInput" value="${duration}"></td>
                            <td> <select class="form-select" id="subjectEditInput" aria-label="Default select example">`;
            data.forEach(d => {
                if(d.subjectId === subjectId){
                    html += `<option value=\"${d.subjectId}\" selected>${d.shortname}</option>`;
                }else{
                    html += `<option value=\"${d.subjectId}\">${d.shortname}</option>`;
                }
            });
            html +=`        </select></td>
                            <td>
                            <button type="button" class="btn btn-outline-success no-wrap" 
                                onclick="editExamOfStudent(${examId}, ${studentId} ,dateOfExamEditInput,durationEditInput,subjectEditInput)">
                            Save
                            </button></td>
                          </tr>`;


            document.getElementById("editExamTBody").innerHTML = html;
            displayEditExamBlock(true);
        });

}

function editExamOfStudent(examId, studentId , dateOfExamEditInput, durationEditInput, subjectEditInput){

    let exam = {
        dateOfExam: dateOfExamEditInput.value,
        duration: durationEditInput.value,
        studentId: studentId,
        subjectId: subjectEditInput.value
    };

    fetch('./api/exam/' + examId,
        {
            method:"PATCH",
            headers:{
                "Content-Type":"application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(exam)
        })
        .then(response =>
        {
            if(response.status == 200){
                response.json().then(data => showExamsOfStudent(data.student.studentId, data.student.firstname, data.student.lastname));
            }else{
                alert("Invalid Request " + response.status + " " + response.statusText);
            }
        })


}

