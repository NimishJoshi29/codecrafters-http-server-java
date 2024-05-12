simple HTTP Server in Java

Endpoints - 
    / - responds with 200 OK and empty body.
    /echo/{string} - responds with the {string} as body.
    /user-agent - responds with the User Agent information found in the header.
    responds with 404 not found for all other requests.