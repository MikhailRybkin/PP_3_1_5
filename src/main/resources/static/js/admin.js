$(async function () {
    await getUsers();
    await newUser();
    await updateUser();
    await deleteUser()
})

async function getUsers() {
    let data = $('#userData');
    data.empty();
    fetch("http://localhost:8080/api/roles")
        .then(response => response.json())
        .then(roles => {
            console.log(roles);
            roles.forEach(role => {
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.name;
                $('#addRoles')[0].appendChild(el);
            })
        })

    fetch("http://localhost:8080/api/admin")
        .then(response => response.json())
        .then(userData => {
            console.log(userData);
            if(userData.length > 0) {
                userData.forEach((u) => {
                    let tableFilling = `$(
                        <tr>
                            <td>${u.id}</td>
                            <td>${u.firstName}</td>
                            <td>${u.lastName}</td>
                            <td>${u.age}</td>
                            <td>${u.email}</td>
                            <td>${u.rolesToString}</td>
                            <td>
                                <button type="button" data-userid="${u.id}" data-action="update" class="btn btn-info" 
                                data-toggle="modal" data-target="#updateModal">Update</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${u.id}" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#deleteModal">Delete</button>
                            </td>
                        </tr>
                    )`;
                    data.append(tableFilling);
                })
            }
        })
}

async function newUser() {
    const form = document.forms["userForm"];

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        let addRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) addRoles.push({
                id : form.roles.options[i].value,
                name : form.roles.options[i].text
            })
        }
        fetch("http://localhost:8080/api/admin", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: form.firstName.value,
                lastName: form.lastName.value,
                age: form.age.value,
                email: form.email.value,
                password: form.password.value,
                roles: addRoles
            })
        }).then(() => {
            form.reset();
            getUsers();
            $('#usersTable').click();
        })
    })
}


$('#updateModal').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.attr('data-userid');
    updateModal(id);
})

async function updateModal(id) {
    let form = document.forms["modalUpdate"];
    let user = await fetch(`http://localhost:8080/api/admin/${id}`)
    .then(response => response.json());

    $('#roleUpdate').empty();
    fetch("http://localhost:8080/api/roles")
        .then(response => response.json())
        .then(roles => {
            console.log(roles);
            roles.forEach(role => {
                let select = false;
                for (let i = 0; i < user.roles.length; i++) {
                    if (user.roles[i].name === role.name) {
                        select = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.name;
                if (select) {
                    el.selected = true;
                }
                $('#roleUpdate')[0].appendChild(el);
            })
        });
    form.id.value = user.id;
    form.firstNameUpdate.value = user.firstName;
    form.lastNameUpdate.value = user.lastName;
    form.ageUpdate.value = user.age;
    form.emailUpdate.value = user.email;
    form.passwordUpdate.value = user.password;
}

async function updateUser() {
    const form = document.forms["modalUpdate"];
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        let updateRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) updateRoles.push({
                id : form.roles.options[i].value,
                name : form.roles.options[i].text
            })
        }
        fetch("http://localhost:8080/api/admin", {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: form.id.value,
            firstName: form.firstNameUpdate.value,
            lastName: form.lastNameUpdate.value,
            age: form.ageUpdate.value,
            email: form.emailUpdate.value,
            password: form.passwordUpdate.value,
            roles: updateRoles
        })
        }).then(() => {
            getUsers();
            $('#closeUpdateButton').click();
        })
    })
}

$('#deleteModal').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.attr('data-userid');
    deleteModal(id);
})

async function deleteModal(id) {
    let form = document.forms["modalDelete"];
    let user = await fetch(`http://localhost:8080/api/admin/${id}`)
        .then(response => response.json());

    $('#roleDelete').empty();
    fetch("http://localhost:8080/api/roles")
        .then(response => response.json())
        .then(roles => {
            console.log(roles);
            roles.forEach(role => {
                let select = false;
                for (let i = 0; i < user.roles.length; i++) {
                    if (user.roles[i].name === role.name) {
                        select = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.name;
                if (select) {
                    el.selected = true;
                }
                $('#roleDelete')[0].appendChild(el);
            })
        });
    form.id.value = user.id;
    form.firstNameDelete.value = user.firstName;
    form.lastNameDelete.value = user.lastName;
    form.ageDelete.value = user.age;
    form.emailDelete.value = user.email;
    form.passwordDelete.value = user.password;
}

async function deleteUser() {
    $("#deleteButton").on('click', async (e) => {
        e.preventDefault();
        fetch(`http://localhost:8080/api/admin/${document.forms["modalDelete"].id.value}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
            }).then(() => {
                getUsers();
                $('#closeDeleteButton').click();
            })
        }
    ) 
}