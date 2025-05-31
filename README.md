## Kafka Payment System
![Arquitetura](./assets/saga-orquestrado.png)

### Arquitetura
![Arquitetura](./assets/arquitetura.png)

#### Connect docker mongodb
docker exec -it order-db mongosh "mongodb://admin:123456@localhost:27017"
show dbs                  // Lista todos os bancos de dados
use nome_do_banco          // Seleciona um banco de dados
show collections          // Lista as coleções do banco atual
db.minha_colecao.find()   // Lista documentos de uma coleção
exit                      // Sai do shell

### 02 - Execução geral via automação com script em Python

Basta executar o arquivo `build.py`. Para isto, **é necessário ter o Python 3 instalado**.

Para executar, basta apenas executar o seguinte comando no diretório raiz do repositório:

`python build.py`

Será realizado o `build` de todas as aplicações, removidos todos os containers e em sequência, será rodado o `docker-compose`.

#### Run kafka, redpanda-console and databases
docker-compose up --build -d kafka redpanda-console order-db product-db inventory-db payment-db

## Acessando a aplicação

Para acessar as aplicações e realizar um pedido, basta acessar a URL:

http://localhost:3000/swagger-ui.html

Você chegará nesta página:

![Swagger](./assets/documentacao.png)

As aplicações executarão nas seguintes portas:

* Order-Service: 3000
* Product-Validation-Service: 8090
* Payment-Service: 8091
* Inventory-Service: 8092
* Apache Kafka: 9092
* Redpanda Console: 8081
* PostgreSQL (Product-DB): 5432
* PostgreSQL (Payment-DB): 5433
* PostgreSQL (Inventory-DB): 5434
* MongoDB (Order-DB): 27017

## Acessando tópicos com Redpanda Console

Para acessar o Redpanda Console e visualizar tópicos e publicar eventos, basta acessar:

http://localhost:8081

Você chegará nesta página:

![Redpanda](./assets/redpanda.png)

## Dados da API

É necessário conhecer o payload de envio ao fluxo da saga, assim como os produtos cadastrados e suas quantidades.

### Produtos registrados e seu estoque

Existem 3 produtos iniciais cadastrados no serviço `product-validation-service` e suas quantidades disponíveis em `inventory-service`:

* **COMIC_BOOKS** (4 em estoque)
* **BOOKS** (2 em estoque)
* **MOVIES** (5 em estoque)
* **MUSIC** (9 em estoque)

### Endpoint para iniciar a saga


**POST** http://localhost:3000/api/order

Payload:

```json
{
  "products": [
    {
      "product": {
        "code": "COMIC_BOOKS",
        "unitValue": 15.50
      },
      "quantity": 3
    },
    {
      "product": {
        "code": "BOOKS",
        "unitValue": 9.90
      },
      "quantity": 1
    }
  ]
}
```

Resposta:

```json
{
  "id": "65235b034a6fa17dc661679b",
  "products": [
    {
      "product": {
        "code": "COMIC_BOOKS",
        "unitValue": 15.5
      },
      "quantity": 3
    },
    {
      "product": {
        "code": "BOOKS",
        "unitValue": 9.9
      },
      "quantity": 1
    }
  ],
  "createdAt": "2023-10-09T01:44:35.655",
  "transactionId": "1696815875655_44ae5c2d-5549-427f-861c-9eef24676b7c",
  "totalAmount": 0,
  "totalItems": 0
}
```


### Endpoint para visualizar a saga

É possível recuperar os dados da saga pelo **orderId** ou pelo **transactionId**, o resultado será o mesmo:

**GET** http://localhost:3000/api/event?orderId=65235b034a6fa17dc661679b

**GET** http://localhost:3000/api/event?transactionId=1696815875655_44ae5c2d-5549-427f-861c-9eef24676b7c

Resposta:

```json
{
  "id": "65235b034a6fa17dc661679c",
  "transactionId": "1696815875655_44ae5c2d-5549-427f-861c-9eef24676b7c",
  "orderId": "65235b034a6fa17dc661679b",
  "payload": {
    "id": "65235b034a6fa17dc661679b",
    "products": [
      {
        "product": {
          "code": "COMIC_BOOKS",
          "unitValue": 15.5
        },
        "quantity": 3
      },
      {
        "product": {
          "code": "BOOKS",
          "unitValue": 9.9
        },
        "quantity": 1
      }
    ],
    "createdAt": "2023-10-09T01:44:35.655",
    "transactionId": "1696815875655_44ae5c2d-5549-427f-861c-9eef24676b7c",
    "totalAmount": 56.4,
    "totalItems": 4
  },
  "source": "ORDER_SERVICE",
  "status": "SUCCESS",
  "eventHistory": [
    {
      "source": "ORDER_SERVICE",
      "status": "SUCCESS",
      "message": "Saga started!",
      "createdAt": "2023-10-09T01:44:35.728"
    },
    {
      "source": "PRODUCT_VALIDATION_SERVICE",
      "status": "SUCCESS",
      "message": "Products are validated successfully!",
      "createdAt": "2023-10-09T01:44:36.196"
    },
    {
      "source": "PAYMENT_SERVICE",
      "status": "SUCCESS",
      "message": "Payment realized successfully!",
      "createdAt": "2023-10-09T01:44:36.639"
    },
    {
      "source": "INVENTORY_SERVICE",
      "status": "SUCCESS",
      "message": "Inventory updated successfully!",
      "createdAt": "2023-10-09T01:44:37.117"
    },
    {
      "source": "ORDER_SERVICE",
      "status": "SUCCESS",
      "message": "Saga finished successfully!",
      "createdAt": "2023-10-09T01:44:37.21"
    }
  ],
  "createdAt": "2023-10-09T01:44:37.209"
}
```

### Acesso ao MongoDB

Para conectar-se ao MongoDB via linha de comando (cli) diretamente do docker-compose, basta executar o comando abaixo:

**docker exec -it order-db mongosh "mongodb://admin:123456@localhost:27017"**

Para listar os bancos de dados existentes:

**show dbs**

Para selecionar um banco de dados:

**use admin**

Para visualizar as collections do banco:

**show collections**

Para realizar queries e validar se os dados existem:

**db.order.find()**

**db.event.find()**

**db.order.find(id=ObjectId("65235b034a6fa17dc661679b"))**

**db.order.find({ "products.product.code": "COMIC_BOOKS"})**

## Autor

### Ivanildo Silva de Lima
### Desenvolvedor de Software Full-stack: Back-End / Front-End
