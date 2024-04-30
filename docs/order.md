# Order API Spec

## GET List Order API
Endpoint : GET /orders

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
                "date": "2024-04-29",
                "status": "in order",
                "amount": 40000,
                "quantity": 1,
                "price": 40000,
                "productId": 4
            },
            {
                "id": "002",
                "date": "2024-04-29",
                "status": "in order",
                "amount": 100000,
                "quantity": 2,
                "price": 50000,
                "productId": 5
            },
        ]
    },
    "page": {
        "size": 10,
        "total": 2,
        "totalPage": 1,
        "current": 1
    }
}
```

Response Body Error :
```json
{
    "code": 404,
    "status": "NOT_FOUND",
    "errors": {
        "name": [
            "order is not found"
        ]
    }
}
```

## GET Order API
Endpoint : GET /orders/{id}

Headers :
- Authorization : token

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        {
            "id": "001",
            "date": "2024-04-29",
            "status": "in order",
            "amount": 40000,
            "quantity": 1,
            "price": 40000,
            "productId": 4
        }
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
            "order is not found"
        ]
    }
}
```

## Create Order API
Endpoint : POST /orders

Headers :
- Authorization : token

Request Body :
```json
[
    {
        "productId": 3,
        "quantity": 1,
        "price": 30000
    },
    {
        "productId": 2,
        "quantity": 2,
        "price": 35000
    }
]
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
        "productId": [
            "must be null"
        ]
    }
}
```

## Remove Product API
Endpoint : DELETE /orders/{id}

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
    "code": 400,
    "status": "NOT_FOUND",
    "errors": {
        "message": [
            "order is not found"
        ]
    }
}
```