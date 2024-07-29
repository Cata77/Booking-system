# Booking System Application

The Booking System Application is a sophisticated platform designed to replicate the core functionalities of Airbnb. It allows users to browse, book, and review a wide 
range of accommodations. The application leverages a microservices architecture complemented by a comprehensive backend database schema to ensure robustness, scalability, 
and user satisfaction.

## System Design

![System design drawio](https://github.com/user-attachments/assets/bfcf2751-34e9-4b85-8e07-bd502babff42)

## Database Design

![Airbnb ERD drawio](https://github.com/user-attachments/assets/061d41ed-a32b-4529-9e0d-b1beb43bd764)

## Key Components:

- Frontend: A user interface for customers to interact with the system.
- API Gateway: Manages and routes incoming requests to appropriate microservices.
- Microservices:
  1. User Service: Handles user registration, authentication, and profile management.
  2. Hotel Service: Manages hotel information, room details, and availability.
  3. Booking Service: Processes booking requests and manages reservations.
  4. Search Service: Provides advanced search functionality for hotels.
  5. Payment Service: Handles secure payment processing.
  6. Analytics Service: Collects and analyzes booking data for insights.

Databases:
- User DB: Stores user information.
- Hotel DB: Contains hotel and room details.
- Booking DB: Stores booking information.

Elasticsearch: 
- Powers the search functionality for efficient hotel queries.
  
Redis:
- Caches frequently accessed data for improved performance.
  
Kafka: 
- Facilitates event-driven communication between services.

Supporting Services:
- Discovery Service: For service registration and discovery.
- Config Service: Centralized configuration management.
- Keycloak: Provides secure authentication and authorization.


Monitoring and Analytics:
- Kibana, Prometheus, and Grafana for system monitoring and data visualization.

## Current Status and Future Tasks:
The project is a work in progress, with significant components and features still to be completed. The key tasks ahead include:

- Frontend Development: Creating a responsive and user-friendly interface to engage users effectively.
- Kafka Integration: Implementing Kafka for event-driven architecture to support asynchronous communication between services.
- Payment Service Implementation: Developing a secure and reliable payment processing system.
- Analytics and Monitoring: Setting up analytics to derive actionable insights from user data and establishing comprehensive monitoring to track system performance.
- DevOps Pipeline Creation: Building a continuous integration and deployment pipeline to automate testing, deployment, and scaling processes.
- Upon completion of these components, the project aims to deliver a competitive and user-friendly booking platform, meeting high standards of performance and user experience.
