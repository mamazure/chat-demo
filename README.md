# DEMO chat application

## Requirements
Java 17, Kafka

## How to run locally
### Download Kafka
### Start it up
* `.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`
* `.\bin\windows\kafka-server-start.bat .\config\server.properties`
* (Optional) `.\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic message`
* `.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic test`
* `.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning`

### Login
There are two users: user and admin with the same password: "password"
