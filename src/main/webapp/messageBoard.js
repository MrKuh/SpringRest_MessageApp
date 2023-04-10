
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

function reloadMessages() {
    const messageTableBody = document.getElementById("message-table-body");
    messageTableBody.innerHTML = "";
    getToken()
    getAllMessages()
        .then(messages => {
            messages.forEach(message => {
                console.log(message)
                const tr = document.createElement("tr");
                const tdMessage = document.createElement("td");
                const tdAuthor = document.createElement("td");

                tdMessage.textContent = message.content;
                tdAuthor.textContent = message.sender.email;
                tr.appendChild(tdMessage);
                tr.appendChild(tdAuthor);
                messageTableBody.appendChild(tr);
            });
        });
}



// API Calls
async function getAllMessages() {
    const response = await fetch("/api/messages/all");
    return await response.json();
}

async function addMessage() {
    let message = document.getElementById("message-content").value;
    console.log(message)
    const response = await fetch("/api/messages/add" + "?message="+ message, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        }
    }).then(
        () => {
            reloadMessages()
        }
    );
}