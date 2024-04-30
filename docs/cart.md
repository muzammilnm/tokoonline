# Cart API Spec

## GET List Cart
Endpoint : GET /cart

Headers :
- Authorization : token

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        [
            {
                "id": "001",
                "quantity": 3
            },
            {
                "id": "002",
                "quantity": 5
            },
        ]
    },
    "page": null
}
```

Response Body Error :
```json
{
    "code": 404,
    "status": "NOT_FOUND",
    "errors": {
        "name": [
            "cart is not found"
        ]
    }
}
```

## Create Cart API
Endpoint : POST /cart

Headers :
- Authorization : token

Request Body :
```json
{
    "userId": 1,
    "productId": 1,
    "quantity": 5
}
```

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": null,
    "page": null
}
```

Response Body Error :
```json
{
    "code": 400,
    "status": "BAD_REQUEST",
    "errors": {
        "userId": [
            "must be null"
        ]
    }
}
```

## Update Cart API
Endpoint : PUT /cart

Headers :
- Authorization : token

Request Body :
```json
{
    "userId": 1,
    "productId": 1,
    "quantity": 5
}
```

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": null,
    "page": null
}
```

Response Body Error :
```json
{
    "code": 400,
    "status": "BAD_REQUEST",
    "errors": {
        "userId": [
            "must be null"
        ]
    }
}
```

## Remove Cart API
Endpoint : DELETE /cart

Headers :
- Authorization : token

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": null,
    "page": null
}
```

Response Body Error :
```json
{
    "code": 404,
    "status": "BAD_REQUEST",
    "errors": {
        "message": "cart is not found"
    }
}
```