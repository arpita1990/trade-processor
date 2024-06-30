# Trade Processor Application

This application is a Spring Boot application that processes trades and quotes from Kafka topics, creates output objects, and sends them to another Kafka topic.

## High-Level Architecture


                      +--------------------+
                      |                    |
                      |   Quote Topic      |
                      |                    |
                      +-----+--------------+
                            |
                            v
                      +-----+--------------+
                      |                    |
                      |  Quote Consumer    |
                      |                    |
                      +-----+--------------+
                            |
                            v
                      +-----+--------------+
                      |                    |
                      | In-Memory Cache    |
                      |  (Latest Quotes)   |
                      |                    |
                      +-----+--------------+
                            |
                            |
                            |
                            v                    +-----+--------------+             +-----+--------------+      
                            |                    |                    |             |                    |
                            |------>-------------|   Output Processor |------>------|   Output Topic     |             
                            |                    |  (Calculate mid_px)|             |                    |
                            |                    |                    |             |                    |
                            |                    +-----+--------------+             +-----+--------------+
                            |                      
                            ^
                            |
                      +-----+--------------+
                      |                    |
                      |  Trade Consumer    |
                      |                    |
                      +--------------------+
                            |
                            ^
                            |                             
                      +-----+--------------+
                      |                    |
                      |  Trade topic       |
                      |                    |
                      +--------------------+


The application consists of several components:

- `TradeConsumer`: This component listens to the `trade-topic` Kafka topic, retrieves trades, and processes them.
- `QuoteConsumer`: This component listens to the `quote-topic` Kafka topic, retrieves quotes, and stores them in a cache.
- `OutputService`: This service is used to create `Output` objects from `Trade` and `Quote` objects.
- `KafkaConfig`: This configuration class sets up the Kafka consumers and producers.

The application uses Spring Kafka for interacting with Kafka topics. It uses the `JsonDeserializer` to deserialize `Trade` and `Quote` objects from JSON format, and the `JsonSerializer` to serialize `Output` objects to JSON format.

## Caching

The application uses a `QuoteCache` for storing `Quote` objects. This cache is implemented using a `ConcurrentHashMap` where each key is a `String` and the value is a `BlockingQueue` of `Quote` objects. 

The `BlockingQueue` is a queue data structure that supports operations that wait for the queue to become non-empty when retrieving an element, and wait for space to become available in the queue when storing an element. This is particularly useful in a multi-threaded environment where you want to control how an object is shared between threads.

In the context of the application, the `QuoteCache` is used to store `Quote` objects that are consumed from a Kafka topic. When a `Trade` object is consumed, the application retrieves a `Quote` from the `QuoteCache` using the `take` method. This method retrieves and removes the head of the queue, waiting if necessary until an element becomes available.

The `QuoteCache` uses a `ConcurrentHashMap` to store multiple `BlockingQueue` instances, each associated with a unique key. This allows the application to maintain separate queues for different keys, which would represent quotes for different instruments.

The `QuoteCache` provides several methods to interact with the cache:

- `put`: This method adds a `Quote` to the queue associated with the given key. It will clear the queue first to ensure there is always a single and latest `Quoute` available in the queue. If the queue does not exist, it is created.
- `get`: This method retrieves, but does not remove, the head of the queue associated with the given key. If the queue does not exist, it is created.
- `take`: This method retrieves and removes the head of the queue associated with the given key, waiting if necessary until an element becomes available. If the queue does not exist, it is created.
- `size`, `isEmpty`, `containsKey`, `containsValue`, `clear`: These methods provide additional functionality to interact with the cache.

This implementation of `QuoteCache` ensures that `Quote` objects are stored and retrieved in a thread-safe manner, and that the application can wait for a `Quote` to become available if necessary.


## Running the Application

To run the main Trade Processor Application in `PowerShell`, use the following command in your terminal:

```bash
./mvnw spring-boot:run -D"spring-boot.run.arguments=--spring.profiles.active=default"
```
This command uses Maven Wrapper to start the Spring Boot application with the default profile, which is suitable for production or development environments without mock data publishing.

## Running the Mock Data Publisher
If you want to run the application in `PowerShell` with mock data publishing enabled, you need to activate the mock profile. This can be done by using the following command:

```bash
./mvnw spring-boot:run -D"spring-boot.run.arguments=--spring.profiles.active=mock"
```