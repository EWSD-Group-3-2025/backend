# API Documentation

## Overview
This API provides endpoints for user authentication, chat functionality, and user management. It allows users to register, authenticate, send messages, create chat rooms, and manage their accounts.

---

## Base URL
```
Development: http://localhost:3000/api/v1
Production: https://team-smurfs-backend.up.railway.app/api/v1
```

---

# Authentication API

## 1. User Login
**Endpoint:**
```
POST /auth/login
```
**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securepassword"
}
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "meta": {
    "method": "POST",
    "endpoint": "/auth/login"
  },
  "data": {
    "accessToken": "eyJhbGciOiJI...",
    "refreshToken": "eyJhbGciOiJI..."
  },
  "message": "Login successful",
  "duration": 0.45
}
```

## 2. User Registration
**Endpoint:**
```
POST /auth/register
```
**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123"
}
```
**Response:**
```json
{
  "success": 1,
  "code": 201,
  "meta": {
    "method": "POST",
    "endpoint": "/auth/register"
  },
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  },
  "message": "User registered successfully",
  "duration": 0.45
}
```

## 3. Refresh Token
**Endpoint:**
```
POST /auth/refresh
```
**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJI..."
}
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": {
    "accessToken": "new_access_token"
  },
  "message": "Token refreshed successfully",
  "duration": 0.32
}
```

## 4. Get Current User
**Endpoint:**
```
GET /auth/me
```
**Headers:**
```
Authorization: Bearer <access_token>
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  },
  "message": "User details retrieved successfully",
  "duration": 0.32
}
```

---

# Chat API

## 5. Create or Get Chat Room
**Endpoint:**
```
POST /chat/room
```
**Headers:**
```
Authorization: Bearer <access_token>
```
**Query Parameters:**
```
senderId=1&receiverId=2
```
**Response:**
```json
{
  "success": 1,
  "code": 201,
  "data": {
    "roomId": 10,
    "participants": [1, 2]
  },
  "message": "Chat room created or retrieved successfully",
  "duration": 0.32
}
```

## 6. Send Message
**Endpoint:**
```
POST /chat/{roomId}/message
```
**Headers:**
```
Authorization: Bearer <access_token>
```
**Query Parameters:**
```
senderId=1&content=Hello
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": {
    "messageId": 101,
    "roomId": 10,
    "senderId": 1,
    "content": "Hello"
  },
  "message": "Message sent successfully",
  "duration": 0.32
}
```

## 7. Get Messages By Room
**Endpoint:**
```
GET /chat/{roomId}/messages
```
**Headers:**
```
Authorization: Bearer <access_token>
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": [
    {
      "messageId": 101,
      "roomId": 10,
      "senderId": 1,
      "content": "Hello"
    }
  ],
  "message": "Messages retrieved successfully",
  "duration": 0.32
}
```

---

# User API

## 8. Retrieve All Users
**Endpoint:**
```
GET /users
```
**Headers:**
```
Authorization: Bearer <access_token>
```
**Query Parameters:**
```
?page=1&limit=10
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com"
    }
  ],
  "message": "Users retrieved successfully",
  "duration": 0.32
}
```

## 9. Change Password
**Endpoint:**
```
POST /users/change-password
```
**Headers:**
```
Authorization: Bearer <access_token>
```
**Request Body:**
```json
{
  "oldPassword": "OldPass123",
  "newPassword": "NewPass456"
}
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": true,
  "message": "Password changed successfully",
  "duration": 0.32
}
```

## 10. Check Username Availability
**Endpoint:**
```
GET /users/exists
```
**Headers:**
```
Authorization: Bearer <access_token>
```
**Query Parameters:**
```
username=johndoe
```
**Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": {
    "username": "johndoe",
    "exists": false
  },
  "message": "Username available",
  "duration": 0.32
}
```
---

# Global Exception Handling

## Overview
The API provides a centralized exception handling mechanism using `GlobalExceptionHandler`. The response format includes metadata (`meta`) with request details.

## Example Error Response
```json
{
  "success": 0,
  "code": 400,
  "meta": {
    "method": "POST",
    "endpoint": "/auth/register"
  },
  "data": "Invalid argument provided.",
  "message": "Validation failed.",
  "duration": 0.32
}
```

## Handled Exceptions
| Exception Type                 | HTTP Status Code | Message |
|--------------------------------|-----------------|---------|
| IllegalArgumentException       | 400             | Invalid argument provided. |
| ConstraintViolationException   | 422             | Validation failed. |
| EntityNotFoundException        | 404             | Entity not found. |
| DuplicateEntityException       | 409             | Duplicate entity detected. |
| BadRequestException            | 400             | Bad request. |
| SecurityException              | 401             | Security violation. |
| Exception (fallback)           | 500             | An unexpected error occurred. |

---

# Notes
- Ensure to replace `<access_token>` with a valid JWT token.
- All responses include `meta` data containing the request method and endpoint.
- Error handling ensures consistent responses for exceptions.
- Pagination parameters (`page` and `limit`) are optional.
- `refreshToken` should be used to get a new `accessToken` when it expires.