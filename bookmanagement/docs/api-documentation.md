# Book Management API Documentation

## Endpoints

### Search Books
- URL: `/bookmanagement/search/books`
- Method: `GET`
- Query Parameters:
    - `title` (optional): String
    - `authorName` (optional): String
- Success Response:
    - Code: 200
    - Content: `[...array of books...]`
- Error Response:
    - Code: 404 Not Found
    - Content: `{ "errorCode": "notfound" }`
- Examples:
    - Request: `/bookmanagement/search/books?title=The Tale of Peter Rabbit`
    - Response: `200 OK [ { "bookId": 1, "title": "The Tale of Peter Rabbit", "authorName": "Beatrix Potter" } ]`
