### Connect docker mongodb
docker exec -it order-db mongosh "mongodb://admin:123456@localhost:27017"

#### Run kafka, redpanda-console and databases
docker-compose up --build -d kafka redpanda-console order-db product-db inventory-db payment-db