# healthcare-service
## Getting Started
This project contains APIs for managing patients and staff with a PostgreSQL database (Dockerized).
### Setup database locally using Docker
1. Download and install Docker Desktop
2. Verify Installation 
3. Open Terminal and check:
      `docker --version`
      `docker-compose --version`
4. To run the database use the command below:
docker-compose up -d
### Run the project using the command below
mvn spring-boot:run

curl http://localhost:8080/api/patients

### To Stop DB:
docker-compose down
### Remove all containers and volumes (reset DB):
docker-compose down -v
