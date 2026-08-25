# Smart Equipment Booking & Maintenance System

A full-stack web application for managing shared equipment, bookings, availability, and maintenance activities within an organization or university environment.

#### Tech Stack

### Frontend

* Angular
* TypeScript
* HTML5
* CSS

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Spring Security
* REST API

### Database

* PostgreSQL

### Development & Collaboration

* Git
* GitHub
* GitHub Pull Requests

---

#### Project Objective

The Smart Equipment Booking & Maintenance System provides a centralized platform for managing shared equipment and their booking and maintenance activities.

The system helps users check equipment availability, make booking requests, and report damaged equipment while allowing administrators to manage equipment, approve bookings, and monitor maintenance activities.

---

#### User Roles

###  Admin

* Manage equipment
* Manage equipment categories
* View all bookings
* Approve or reject booking requests
* Manage maintenance requests
* View system dashboard
* Monitor equipment availability

###  User

* Register and log in
* View available equipment
* Search and filter equipment
* Submit booking requests
* View booking history
* Cancel eligible bookings
* Report damaged equipment
* View maintenance status

---

#### Core Features

###  Authentication & Authorization

* User registration
* User login
* JWT-based authentication
* Role-based access control
* Protected API endpoints

###  Equipment Management

* Add equipment
* Edit equipment
* Delete equipment
* View equipment details
* Manage equipment categories
* Track equipment location
* Track equipment availability

### Equipment Booking

* View available equipment
* Select booking date and time
* Submit booking requests
* Approve or reject booking requests
* View booking status
* View booking history
* Cancel bookings

### Booking Conflict Detection

The system checks existing bookings before creating a new booking.

This prevents multiple users from booking the same equipment during overlapping time periods.

### Maintenance Management

* Report damaged equipment
* View maintenance requests
* Update maintenance status
* Track maintenance history
* Mark repaired equipment as available

### Dashboard

The admin dashboard provides an overview of:

* Total equipment
* Available equipment
* Currently booked equipment
* Equipment under maintenance
* Pending booking requests
* Booking statistics

---

#### Booking Workflow

```text
User selects equipment
        ↓
Select date & time
        ↓
Check equipment availability
        ↓
Check booking conflicts
        ↓
Create booking request
        ↓
Admin review
        ↓
 ┌───────────────┐
 │               │
Approved       Rejected
 │
 ↓
Booking Confirmed
```

---

#### Maintenance Workflow

```text
User reports equipment issue
             ↓
     Maintenance Request
             ↓
       Under Maintenance
             ↓
       Repair Completed
             ↓
         Available
```

---

#### Development Progress

### Project Setup

* [ ] GitHub repository setup
* [ ] Angular project setup
* [ ] Spring Boot project setup
* [ ] PostgreSQL database setup

### Authentication

* [ ] User registration
* [ ] User login
* [ ] JWT authentication
* [ ] Role-based authorization

### Equipment

* [ ] Add equipment
* [ ] Edit equipment
* [ ] Delete equipment
* [ ] View equipment
* [ ] Search and filter equipment

### Booking

* [ ] Create booking
* [ ] Availability checking
* [ ] Booking conflict detection
* [ ] Booking approval/rejection
* [ ] Booking history
* [ ] Booking cancellation

### Maintenance

* [ ] Report equipment issue
* [ ] View maintenance requests
* [ ] Update maintenance status
* [ ] Maintenance history

### Dashboard

* [ ] Admin dashboard
* [ ] Equipment statistics
* [ ] Booking statistics
* [ ] Maintenance statistics

### Finalization

* [ ] Frontend and backend integration
* [ ] API testing
* [ ] Error handling
* [ ] Form validation
* [ ] Responsive UI
* [ ] Final testing
* [ ] Documentation
