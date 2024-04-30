# Review API Spec

## GET List Review API
Endpoint : GET /reviews

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        [
            {
                "name": "muzammil"
                "productId": "1",
                "rating": 5,
                "comment": "best product"
            },
            {
                "name": "riko",
                "productId": "2",
                "rating": 4,
                "comment": "best product"
            }
        ]
    },
    ,
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
            "review is not found"
        ]
    }
}
```

## Create Review API
Endpoint : POST /reviews

Headers :
- Authorization : token

Request Body :
```json
{
    "userId": "001",
    "productId": 1,
    "rating": 5,
    "comment": "best product"
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
        "rating": [
            "must be null"
        ]
    }
}
```

## Update Review API
Endpoint : PUT /reviews

Headers :
- Authorization : token

Request Body :
```json
{
    "userId": "001",
    "productId": 1,
    "rating": 5,
    "comment": "best product"
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
        "comment": [
            "must be null"
        ]
    }
}
```

## Remove Review API
Endpoint : DELETE /reivews/{id}

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
    "status": "NOT_FOUND",
    "errors": {
        "message": [
            "reivew is not found"
        ]
    }
}
```

