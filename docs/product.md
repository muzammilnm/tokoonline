# Product API Spec

## GET List Product API
Endpoint : GET /products

Params :
- name (optional)
- price (optional)
- categoryIds (optional)

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        [
            {
                "id": "001",
                "name": "Buku tulis",
                "description": "buku tulis cetak",
                "price": 50000
            },
            {
                "id": "002",
                "name": "Buku panduan",
                "description": "buku panduan cetak",
                "price": 45000
            },
        ]
    },
    "page": {
        "size": 10,
        "total": 100,
        "totalPage": 10,
        "current": 2,

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
            "product is not found"
        ]
    }
}
```

## GET Product API
Endpoint : GET /products/{id}

Response Body Success :
```json
{
    "code": 200,
    "status": "OK",
    "data": {
        "id": "001",
        "name": "Buku panduan",
        "description": "buku panduan cetak",
        "stock": 20,
        "price": 200000

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

## Create Product API
Endpoint : POST /products

Headers :
- Authorization : token

Request Body :
```json
{
    "name": "Handphone",
    "description": "Introducing the XYZ Mobile X10: a powerful and stylish smartphone designed for the modern user. With its octa-core processor, advanced triple-camera system, vibrant AMOLED display, and long-lasting battery life, the X10 delivers an exceptional mobile experience. Stay connected, capture stunning photos, and enjoy entertainment on the go with the XYZ Mobile X10",
    "price": 2000000,
    "stock": 5,
    "categoryIds": ["ae91837d-2121-47ce-ab22-36aa38fbb06b"]
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
        "name": [
            "must be null"
        ]
    }
}
```

## Update Product API
Endpoint : PUT /products

Headers :
- Authorization : token

Request Body :
```json
{
    "name": "Handphone",
    "description": "Introducing the XYZ Mobile X10: a powerful and stylish smartphone designed for the modern user. With its octa-core processor, advanced triple-camera system, vibrant AMOLED display, and long-lasting battery life, the X10 delivers an exceptional mobile experience. Stay connected, capture stunning photos, and enjoy entertainment on the go with the XYZ Mobile X10",
    "price": 2500000,
    "stock": 18,
    "categoryIds": ["ae91837d-2121-47ce-ab22-36aa38fbb06b"]
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
        "name": [
            "must be null"
        ]
    }
}
```

## Remove Product API
Endpoint : DELETE /products/{id}

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
            "Product is not found"
        ]
    }
}
```