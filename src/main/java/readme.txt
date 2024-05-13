Simple HTTP Server in Java

Endpoints-
    / - responds with 200 OK and blank body.
    /user-agents - responds with the user agent value in the request header.
    /echo/{string} - responds with the {string} as body
    /files/{filename} - responds with contents of {filename} if present, otherwise 404 not found.
    POST /files/{filename} - creates a new file with name {filename} and writes to it the body of HTTP request.
    For all other requests, responds with 404 not found.