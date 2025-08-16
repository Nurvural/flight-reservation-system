# Flight Reservation Microservices System

## Proje Açıklaması
Bu proje, microservice mimarisi ile geliştirilmiş bir uçuş rezervasyon sistemidir. Sistem; uçuş yönetimi, kullanıcı yönetimi, rezervasyon ve admin işlemleri gibi modülleri içerir. Redis ile cache yönetimi, Kafka ile event-driven akış ve JWT ile güvenlik uygulanmıştır.  

## Teknolojiler
- Java 21 / Spring Boot
- Spring Data JPA
- Stream API
- Spring Validation
- Global Exception Handling
- DTO & Mapper (MapStruct)
- Transactional işlemler
- Lombok  
- PostgreSQL
- Redis
- Apache Kafka
- JWT & Spring Security
- SLF4J / Logback
- JUnit / Mockito
- Swagger/OpenAPI
  
## Özellikler
- **Flight Service**: Uçuş CRUD ve listeleme
- **Reservation Service**: Rezervasyon oluşturma, Kafka event üretimi, cache
- **User Service**: Kullanıcı yönetimi ve JWT tabanlı authentication
- **Admin Controller**: Airport & Flight CRUD, rezervasyon yönetimi
- **Redis**: Flight ve Reservation sorguları için cache
- **Kafka**: Reservation event’leri publish/subscribe
- **PostgreSQL**: Her microservice kendi veritabanına sahiptir (dev ortamda aynı DB kullanılabilir)

## Mimari
- Microservice yapısı ile bağımsız servisler
- Event-driven akış Kafka ile yönetilir
- Redis ile performans optimizasyonu
- JWT ve Spring Security ile güvenlik


