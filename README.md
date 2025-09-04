✈️ Flight Reservation Microservices System

🔹 Proje Hakkında
Bu proje, uçuş rezervasyonlarını yönetmek için geliştirilmiş microservices tabanlı bir backend sistemidir. Kullanıcılar uçuşları görüntüleyebilir, rezervasyon oluşturabilir, ödeme yapabilir ve yönetici işlemlerini gerçekleştirebilir. Sistem JWT tabanlı authentication ve rol yönetimi ile güvenli bir yapı sağlar.

🛠️ Teknoloji Yığını

Backend & Core
- Java 21 / Spring Boot
- Spring Data JPA
- REST API
- WebClient
- Spring WebFlux
- Stream API
- Spring Validation
- Global Exception Handling
- DTO & Mapper (MapStruct)
- Transactional işlemler
- Lombok  
- PostgreSQL
- JWT & Spring Security

 Microservices & Architecture
- Spring Cloud 
- Eureka 
- API Gateway (Spring Cloud Gateway) → Microservice yönlendirmesi
- Resiliency (Circuit Breaker / Resilience4J) → Hata toleransı

Caching & Performance
- Redis → Uçuş sorguları ve rezervasyon cache’leme
- Spring Cache → Cache yönetimi

 Messaging & Event-Driven
- RabbitMQ veya Kafka → Rezervasyon oluşturulduğunda event üretmek için

Logging & Monitoring
- SLF4J / Logback 

Testing / Quality
- JUnit 
- Mockito 
