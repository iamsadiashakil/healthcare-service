# healthcare-service
This project contains APIs for managing patients and staff with a PostgreSQL database (Dockerized).
## Basic Setup
1. Download and install Docker Desktop
2. Verify Installation 
Open Terminal and check:
```
docker --version
docker-compose --version
```
3. Setup the database using the command below:
```
docker-compose up -d
```
4. Run the project using the command below
```
mvn spring-boot:run
```
5. If you shut down the project.

   i. Stop DB:
      ```
      docker-compose down
      ```
   ii. Remove all containers and volumes (reset DB):

       ``` 
       docker-compose down -v
       ```
