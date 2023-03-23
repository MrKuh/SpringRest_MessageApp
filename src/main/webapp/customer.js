function getCustomers()
{
    fetch('./customer/all')
        .then(response => response.json())
        .then(data =>
        {
            let html = "<table>";

            data.forEach(d => html += `<tr><td>${d.id}</td><td>${d.name}</td><td>${d.address}</td></tr>`)

            html += "</table";

            document.getElementById("allCustomers").innerHTML = html;
        });
}

function getCustomer(id)
{
    fetch('./customer/'+id.value)
        .then(response =>
        {
            if(response.status != 200)
                throw new Error("Invalid Request " + response.status + " " + response.statusText);
            return response.json()
        })
        .then(d =>
        {
            let html = "<table>";
            html += `<tr><td>${d.id}</td><td>${d.name}</td><td>${d.address}</td><td>${d.sales}</td></tr>`;
            html += "</table";

            document.getElementById("singleCustomer").innerHTML = html;
        })
        .catch(error => alert(error));
}

function addCustomer(tfAddId, tfAddName, tfAddAddress, tfAddSales)
{
    let data = {
        id: tfAddId.value,
        name: tfAddName.value,
        address: tfAddAddress.value,
        sales: tfAddSales.value
    };

    fetch('./customer',
        {
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body: JSON.stringify(data)
        })
        .then(response =>
        {
            if(response.status == 201){ //created
                alert(response.headers.get("location"));
            }else{
                alert("Invalid Request " + response.status + " " + response.statusText);
            }
        });
}

function deleteCustomer(id)
{
    fetch('./customer/'+id.value,
        {
            method:"DELETE"
        })
        .then(response => {
            alert(response.status + " " + response.statusText);
        });
}

function updateCustomer(updateAddId, updateAddName, updateAddAddress, updateAddSales)
{
    let data = {
        id: updateAddId.value,
        name: updateAddName.value,
        address: updateAddAddress.value,
        sales: updateAddSales.value
    };

    fetch('./customer',
        {
            method:"PUT",
            headers:{
                "Content-Type":"application/json"
            },
            body: JSON.stringify(data)
        })
        .then(response =>
        {
            if(response.status == 201){ //created
                alert(response.headers.get("location"));
            }else{
                alert("Invalid Request " + response.status + " " + response.statusText);
            }
        });
}


