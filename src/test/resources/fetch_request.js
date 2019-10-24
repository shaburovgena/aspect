// GET all
fetch('/group/').then(response => response.json().then(console.log))

// GET one
fetch('/group/2').then(response => response.json().then(console.log))


// POST add new one
fetch(
    '/group',
    {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: 'Fourth group (4)', id: 10 })
    }
).then(result => result.json().then(console.log))
// POST get value
fetch(
    '',
    {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    }
).then(result => result.json().then(console.log))

// PUT save existing
fetch(
    '/group/4',
    {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: 'Fourth group', id: 10 })
    }
).then(result => result.json().then(console.log));

// DELETE existing
fetch('/group/4', { method: 'DELETE' }).then(result => console.log(result))


$ curl -i -X POST -H "Content-Type:application/json" -d '{"firstName": "Frodo", "lastName": "Baggins"}' http://localhost:8080/people
HTTP/1.1 201 Created
Server: Apache-Coyote/1.1