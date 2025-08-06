# healthcare-service
The **Healthcare Service API** is a Spring Boot application that provides RESTful endpoints to manage Patients, Doctors, and Appointments in a healthcare system.

## **Base URL**
```
http://localhost:8080/api
````

---

## **Endpoints**

### **Patients**

#### 1. Create Patient
**POST** `/patients`
```json
{
  "name": "John Doe",
  "dob": "1990-05-10",
  "gender": "Male",
  "phone": "1234567890"
}
````

**Response:**

```json
{
  "id": 1,
  "name": "John Doe",
  "dob": "1990-05-10",
  "gender": "Male",
  "phone": "1234567890",
  "createdAt": "2025-07-21T16:30:40.098127"
}
```

#### 2. Get All Patients

**GET** `/patients`

```json
[
  {
    "id": 1,
    "name": "John Doe",
    "dob": "1990-05-10",
    "gender": "Male",
    "phone": "1234567890",
    "createdAt": "2025-07-21T16:30:40.098127"
  }
]
```

#### 3. Get Patient By ID

**GET** `/patients/{id}`

#### 4. Update Patient

**PUT** `/patients/{id}`

```json
{
  "name": "John Smith",
  "dob": "1990-05-10",
  "gender": "Male",
  "phone": "0987654321"
}
```

#### 5. Delete Patient

**DELETE** `/patients/{id}`

---

### **Doctors**

#### 1. Create Doctor

**POST** `/staff`

```json
{
  "name": "Dr. Emily Clark",
  "specialization": "Cardiologist",
  "phone": "9876543210"
}
```

#### 2. Get All Doctors

**GET** `/staff`

#### 3. Get Doctor By ID

**GET** `/staff/{id}`

#### 4. Update Doctor

**PUT** `/staff/{id}`

```json
{
  "name": "Dr. Emily Watson",
  "specialization": "Neurologist",
  "phone": "1122334455"
}
```

#### 5. Delete Doctor

**DELETE** `/staff/{id}`

---

### **Appointments**

#### 1. Create Appointment

**POST** `/appointments`

```json
{
  "patientId": 1,
  "doctorId": 2,
  "appointmentDate": "2025-07-25T14:30:00",
  "status": "SCHEDULED"
}
```

#### 2. Get All Appointments

**GET** `/appointments`

#### 3. Get Appointment By ID

**GET** `/appointments/{id}`

#### 4. Update Appointment

**PUT** `/appointments/{id}`

```json
{
  "patientId": 1,
  "doctorId": 2,
  "appointmentDate": "2025-07-30T11:00:00",
  "status": "COMPLETED"
}
```

#### 5. Delete Appointment

**DELETE** `/appointments/{id}`

---

## **Validation Rules**

* **Patient**

   * `name` must be 3-50 characters.
   * `dob` must be in the past.
   * `gender` must be `Male`, `Female`, or `Other`.
   * `phone` must be 10-15 digits.
* **Appointment**

   * `appointmentDate` must be in the future.
   * `status` must be `SCHEDULED`, `COMPLETED`, or `CANCELLED`.

---

## **Running the Application**

1. Clone the repository and setup Docker Desktop
2. Verify Installation via Terminal:
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

       docker-compose down -v

---

## **Future Enhancements**

* Add pagination for patient and appointment endpoints.
* Integrate Spring Security with JWT.
* Add Swagger/OpenAPI documentation.

---
