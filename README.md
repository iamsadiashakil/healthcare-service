Here's the updated README.md file with the current API structure and endpoints:

# Healthcare Service API

The **Healthcare Service API** is a Spring Boot application that provides RESTful endpoints for a comprehensive healthcare management system, including authentication, patient management, staff management, and healthcare proxy functionality.

## **Swagger UI**
```
http://localhost:8080/swagger-ui/index.html
```

---

## **Authentication**

### Login
```
POST /api/auth/login
```
Request:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
Response:
```json
{
  "token": "jwt.token.here",
  "userType": "STAFF|PATIENT_PROXY"
}
```

### Register Healthcare Proxy
```
POST /api/auth/register/healthcare-proxy
```
Request:
```json
{
  "email": "proxy@example.com",
  "password": "password123"
}
```

### Register Staff (Admin only)
```
POST /api/auth/register/staff
```
Request:
```json
{
  "email": "staff@example.com",
  "password": "password123"
}
```

### Forgot Password
```
POST /api/auth/forgot-password
```
Request:
```json
{
  "email": "user@example.com"
}
```

### Reset Password
```
POST /api/auth/reset-password
```
Request:
```json
{
  "token": "reset-token",
  "newPassword": "newPassword123",
  "confirmPassword": "newPassword123"
}
```

---

## **Healthcare Proxy Endpoints**

### Get Proxy Profile
```
GET /api/proxies/profile
```
Requires: JWT Token (PATIENT_PROXY role)

### Update Proxy Profile
```
PUT /api/proxies/profile
```
Request:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "updated@example.com",
  "phone": "1234567890"
}
```

### Get Assigned Patient
```
GET /api/proxies/patient
```
Returns patient details assigned to the proxy

### Send Message to Staff
```
POST /api/proxies/patient/messages?staffId=1
```
Request:
```json
{
  "content": "Message content here"
}
```

---

## **Patient Endpoints**

### Get Patient Allergies
```
GET /api/patients/{patientId}/allergies
```
Returns list of allergies for the patient

### Get Patient Vitals
```
GET /api/patients/{patientId}/vitals
```
Returns list of vital records for the patient

### Get Vitals by Type
```
GET /api/patients/{patientId}/vitals/{type}
```
Supported types: blood_pressure, heart_rate, etc.

### Get Patient Messages
```
GET /api/patients/{patientId}/messages[?staffId=1]
```
Optional staffId parameter to filter messages

### Get Staff for Patient
```
GET /api/patients/{patientId}/staff
```
Returns list of staff members associated with the patient

---

## **Staff Endpoints**

### Get All Patients
```
GET /api/staff/patients
```
Returns list of all patients

### Search Patients
```
GET /api/staff/patients/search?query=john
```
Searches patients by name, email, etc.

### Get Patient Details
```
GET /api/staff/patients/{patientId}
```
Returns detailed patient information

### Get/Add Patient Allergies
```
GET /api/staff/patients/{patientId}/allergies
POST /api/staff/patients/{patientId}/allergies
```
Manage patient allergy records

### Get Patient Vitals
```
GET /api/staff/patients/{patientId}/vitals
GET /api/staff/patients/{patientId}/vitals/{type}
```
View patient vital records

### Manage Appointments
```
GET /api/staff/appointments
GET /api/staff/appointments/{appointmentId}
PUT /api/staff/appointments/{appointmentId}
```
View and update appointments

### Patient Messaging
```
GET /api/staff/patients/{patientId}/messages
POST /api/staff/patients/{patientId}/messages
```
View and send messages to patients

### Staff Profile Management
```
GET /api/staff/profile
PUT /api/staff/profile
```
View and update staff profile

### Get Assigned Patients' Proxies
```
GET /api/staff/{staffId}/assigned-patients
```
Returns healthcare proxies for patients assigned to the staff member

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
      ```
      docker-compose down -v
      ```

---

## **Email Configuration**

### Using Gmail SMTP (for testing)
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your.email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

🔐 To create a Gmail App Password:
1. Go to: https://myaccount.google.com/apppasswords
2. Generate an app password for Mail and "Other" app
3. Save the 16-digit password (requires 2FA to be enabled)

---