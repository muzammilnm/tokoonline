# Payment API Spec

## GET List Payment API
Endpoint : GET /payments

Header : 
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
                "orderId": "001",
                "Date": "2024-03-20",
                "amount": 200000,
                "method": "transfer"
            },
            {
                "id": "002",
                "orderId": "002",
                "Date": "2024-03-20",
                "amount": 240000,
                "method": "transfer"
            }
        ]
    },
    "page": {
        "size": 10,
        "total": 100,
        "totalPage": 10,
        "current": 2
    }
}
```

Response Body Error :
```json
{
    "code": 404,
    "status": "NOT_FOUND",
    "errors": {
        "message": [
            "payment is not found"
        ]
    }
}
```

## GET Payment API
Endpoint : GET /payments/{id}

Headers :
- Authorization : token

Params :
- id : "002"

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": "002",
        "orderId": "002",
        "Date": "2024-03-20",
        "amount": 240000,
        "method": "transfer"
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
            "product is not found"
        ]
    }
}
```

## Create Payment API
Enpoint : POST /payments

Headers :
- Authorization : token

Request Body : 
```json
{
    "orderId": "001",
    "method": "Transfer",
    "amount": 200000,
    "date": "2024-04-29",
    "status": "payment"
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
        "method": [
            "must be null"
        ]
    }
}
```

## Update Payment API
Endpoint : PUT /payments

Headers :
- Authorization : token

Request Body :
```json
{
    "orderId": "001",
    "method": "Transfer",
    "amount": 200000,
    "date": "2024-04-29",
    "status": "payment"
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
        "method": [
            "must be null"
        ]
    }
}
```

## Remove Payment API
Endpoint : DELETE /payments/{id}

Headers :
- Authorization : token

Params :
- id : "001"

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
    "status": "NOT_FOUND",
    "errors": {
        "message": [
            "Payment is not found"
        ]
    }
}
```