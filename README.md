
### Project Structure

#### Backend (Spring Boot)
```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── xpertlink/
│   │   │               ├── XpertLinkApplication.java
│   │   │               ├── config/
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── JwtUtil.java
│   │   │               │   └── MailConfig.java
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── UserController.java
│   │   │               │   ├── AdminController.java
│   │   │               │   ├── ExpertController.java
│   │   │               │   ├── ChatController.java
│   │   │               │   ├── VideoCallController.java
│   │   │               │   └── OtpController.java
│   │   │               ├── model/
│   │   │               │   ├── User.java
│   │   │               │   ├── Admin.java
│   │   │               │   ├── Expert.java
│   │   │               │   ├── Consultation.java
│   │   │               │   ├── Service.java
│   │   │               │   ├── Message.java
│   │   │               │   └── VideoCall.java
│   │   │               ├── repository/
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── AdminRepository.java
│   │   │               │   ├── ExpertRepository.java
│   │   │               │   ├── ConsultationRepository.java
│   │   │               │   ├── MessageRepository.java
│   │   │               │   └── VideoCallRepository.java
│   │   │               ├── service/
│   │   │               │   ├── UserService.java
│   │   │               │   ├── AdminService.java
│   │   │               │   ├── ExpertService.java
│   │   │               │   ├── ConsultationService.java
│   │   │               │   ├── MailService.java
│   │   │               │   ├── MessageService.java
│   │   │               │   └── VideoCallService.java
│   │   │               ├── dto/
│   │   │               │   ├── UserDto.java
│   │   │               │   ├── AdminDto.java
│   │   │               │   ├── ExpertDto.java
│   │   │               │   ├── AuthRequest.java
│   │   │               │   ├── MessageDto.java
│   │   │               │   └── VideoCallDto.java
│   │   │               └── util/
│   │   │                   ├── OtpUtil.java
│   │   │                   └── EmailTemplate.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── schema.sql
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── xpertlink/
│                       └── XpertLinkApplicationTests.java
└── pom.xml
```

#### Frontend (React)
```
frontend/
├── public/
│   ├── index.html
│   └── favicon.ico
├── src/
│   ├── components/
│   │   ├── Auth/
│   │   │   ├── Login.js
│   │   │   ├── Register.js
│   │   │   ├── OtpVerification.js
│   │   ├── Dashboard/
│   │   │   ├── Dashboard.js
│   │   │   ├── Chat/
│   │   │   │   ├── Chat.js
│   │   │   │   ├── ChatRoom.js
│   │   │   ├── VideoCall/
│   │   │       ├── VideoCall.js
│   │   │       ├── VideoRoom.js
│   │   ├── Admin/
│   │   │   ├── AdminDashboard.js
│   │   │   ├── ManageUsers.js
│   │   │   ├── ManageServices.js
│   │   ├── Expert/
│   │   │   ├── ExpertDashboard.js
│   │   │   ├── ManageConsultations.js
│   │   │   ├── ManageServices.js
│   │   ├── Common/
│   │       ├── Header.js
│   │       ├── Footer.js
│   ├── services/
│   │   ├── authService.js
│   │   ├── userService.js
│   │   ├── adminService.js
│   │   ├── expertService.js
│   │   ├── chatService.js
│   │   ├── videoCallService.js
│   │   ├── mailService.js
│   │   └── otpService.js
│   ├── App.js
│   ├── index.js
│   └── App.css
└── package.json
```

Here’s a brief description of each component, model, and DTO, including the fields they should contain:

### Backend

#### Models

1. **`User`**:
    - `id`: Unique identifier
    - `username`: User’s login name
    - `password`: User’s password (hashed)
    - `email`: User’s email address
    - `fullName`: Full name of the user
    - `address`: User’s address
    - `age`: User’s age
    - `gender`: User’s gender
    - `role`: User’s role (e.g., USER, ADMIN)
    - `enabled`: Indicates if the user account is active
    - `otpCode`: One-time password for verification
    - `otpExpiryTime`: Expiry time for OTP
    - `dateCreated`: Date the user account was created

2. **`Admin`**:
    - `id`: Unique identifier
    - `username`: Admin’s login name
    - `password`: Admin’s password (hashed)
    - `email`: Admin’s email address
    - `fullName`: Full name of the admin
    - `role`: Role of the admin (e.g., ADMIN)

3. **`Expert`**:
    - `id`: Unique identifier
    - `username`: Expert’s login name
    - `password`: Expert’s password (hashed)
    - `email`: Expert’s email address
    - `fullName`: Full name of the expert
    - `specialization`: Area of expertise
    - `experience`: Years of experience
    - `bio`: Short biography of the expert
    - `rating`: Average rating from users

4. **`Consultation`**:
    - `id`: Unique identifier
    - `expertId`: ID of the expert
    - `userId`: ID of the user requesting the consultation
    - `scheduledTime`: Time of the scheduled consultation
    - `status`: Current status of the consultation (e.g., PENDING, COMPLETED)

5. **`Service`**:
    - `id`: Unique identifier
    - `name`: Name of the service
    - `description`: Detailed description of the service
    - `price`: Price of the service

6. **`Message`**:
    - `id`: Unique identifier
    - `fromUser`: Sender of the message
    - `toUser`: Receiver of the message
    - `text`: Content of the message
    - `timestamp`: Time when the message was sent

7. **`VideoCall`**:
    - `id`: Unique identifier
    - `callerId`: ID of the user initiating the call
    - `receiverId`: ID of the user receiving the call
    - `startTime`: Time when the call started
    - `endTime`: Time when the call ended
    - `status`: Status of the call (e.g., IN_PROGRESS, ENDED)

#### DTOs

1. **`UserDto`**:
    - `username`: User’s login name
    - `email`: User’s email address
    - `fullName`: Full name of the user
    - `address`: User’s address
    - `age`: User’s age
    - `gender`: User’s gender
    - `role`: User’s role
    - `enabled`: Indicates if the user account is active

2. **`AdminDto`**:
    - `username`: Admin’s login name
    - `email`: Admin’s email address
    - `fullName`: Full name of the admin
    - `role`: Role of the admin

3. **`ExpertDto`**:
    - `username`: Expert’s login name
    - `email`: Expert’s email address
    - `fullName`: Full name of the expert
    - `specialization`: Area of expertise
    - `experience`: Years of experience
    - `bio`: Short biography of the expert
    - `rating`: Average rating from users

4. **`AuthRequest`**:
    - `email`: User’s email address
    - `password`: User’s password

5. **`MessageDto`**:
    - `fromUser`: Sender of the message
    - `toUser`: Receiver of the message
    - `text`: Content of the message

6. **`VideoCallDto`**:
    - `callerId`: ID of the user initiating the call
    - `receiverId`: ID of the user receiving the call
    - `startTime`: Time when the call started
    - `endTime`: Time when the call ended
    - `status`: Status of the call

This structure will help ensure that your models and DTOs are well-defined and cover the necessary fields for functionality in your application.
