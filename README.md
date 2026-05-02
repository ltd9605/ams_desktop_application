# 📑 AMS Desktop Application

## 🚀 Project Overview
The AMS desktop application enables seamless operation in both **Online** and **Offline** modes.

The system is designed to handle:

- Processing the admissions process
- Processing admission applications
- Checking public admission results
- Printing admission notices
---

## 🧩 Core Modules

### 📊 Analysis Dashboard

---

### 🌐 Public Portal
- Allows candidates to check admission results
- Requirements:

- Registration number

- National ID card
- Fast and user-friendly interface

---

### 🎓 Admission Application Processing
- Automatic admission based on custom criteria
- Importing and processing high school academic results
- Supports flexible admissions processes

---

## 🛠 Technologies Used

### 💻 User Interface (This repository)

- **Framework:** JavaFX 25

- **Language** Language:** Java 25
- **Building Tool:** Maven **Communication:** Java Native HttpClient (JSON-based API)

---

### 🌐 Backend (External Repository)

Backend handles:

- Business logic

- Database operations

- Authentication & authorization

🔗 **Source Code:** 👉 https://github.com/dungtq2k5/ams-backend

- **Framework:** Spring Boot 4.0.5 
- **Database:** PostgreSQL 
- **Security:** Spring Security (JWT) 
- **Deployment:** Docker 
---
## ⚡ How to Setup

### 📌 Prerequisites
Make sure you have the following installed:

- **Java 21+** (recommended Java 25)
- **Maven 3.9+**
- **Git**
- Backend server running

---

### 🔧 1. Clone the Repository
```bash
git clone https://github.com/ltd9605/ams_desktop_application.git
cd ams_desktop_application
```
###⚙️ 2. Configure Backend Connection
Open project in your IDE (recommended: IntelliJ IDEA)

Go to:

src/main/java/com/ams/ams_app/network/ApiClient.java

src/main/java/com/ams/ams_app/util/NetworkUtil.java

Update base URL if needed:
```bash
private static final String BASE_URL = "your_backend_base_url";
```
###🚀 3. Run the Application
Using Maven:
```bash
mvn clean install
mvn javafx:run
```
Or run Main.java directly from your IDE.
###🌐 4. Backend Setup (Required)
Make sure backend is running before using online features
###🧪 5. Verify Setup
Launch the app
Try:
Login
View dashboard

If everything works → setup successful ✅
