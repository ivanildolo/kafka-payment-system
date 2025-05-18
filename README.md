## Kafka Payment System

#### Connect docker mongodb
docker exec -it order-db mongosh "mongodb://admin:123456@localhost:27017"
show dbs                  // Lista todos os bancos de dados
use nome_do_banco          // Seleciona um banco de dados
show collections          // Lista as coleções do banco atual
db.minha_colecao.find()   // Lista documentos de uma coleção
exit                      // Sai do shell
#### Run kafka, redpanda-console and databases
docker-compose up --build -d kafka redpanda-console order-db product-db inventory-db payment-db