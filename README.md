# Advanced Programming and Technologies Workshop

A web application for the Advanced Programming and Technologies module at Itahari International College.

## Repository

This project is available on GitHub: [https://github.com/v-eenay/advanced-programming-tutorials-workshops.git](https://github.com/v-eenay/advanced-programming-tutorials-workshops.git)

## Features

- User Registration and Login
- Role-based Access Control (Admin and Student roles)
- User Profile Management
- Responsive Design

## Setup Instructions

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Tomcat 10.0 or higher
- MySQL Database Server
- Maven

### Database Setup

1. Install MySQL if you haven't already
2. Create a database named `user_db`
3. Run the database initialization script:
   ```
   mysql -u root -p < src/main/resources/schema.sql
   ```

### Configuration

1. Create an `application.properties` file in the `src/main/resources/` directory by copying the template:
   ```
   cp src/main/resources/application.properties.template src/main/resources/application.properties
   ```

2. Edit the `application.properties` file to set your database credentials:
   ```properties
   # Database Configuration
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/user_db
   db.username=your_username
   db.password=your_password
   ```

   These are the only properties required for the project to function.

3. Make sure the database user has the necessary permissions to create tables and insert data.

### Running the Application

1. Build the project using Maven:
   ```
   mvn clean package
   ```

2. Deploy the generated WAR file to Tomcat:
   - Copy the WAR file from `target/ap-tutorial-workshop-1.0-SNAPSHOT.war` to Tomcat's `webapps` directory
   - Start Tomcat

3. Access the application:
   ```
   http://localhost:8080/ap-tutorial-workshop-1.0-SNAPSHOT/
   ```

## Default Users

The application comes with two default users:

1. Admin User:
   - Email: admin@example.com
   - Password: admin123

2. Student User:
   - Email: student@example.com
   - Password: student123

## Project Structure

- `src/main/java/com/example/aptutorialworkshop/controllers/`: Servlet controllers
- `src/main/java/com/example/aptutorialworkshop/models/`: Data models
- `src/main/java/com/example/aptutorialworkshop/dao/`: Data Access Objects
- `src/main/java/com/example/aptutorialworkshop/services/`: Business logic
- `src/main/java/com/example/aptutorialworkshop/utils/`: Utility classes
- `src/main/webapp/`: Web resources (JSP, CSS, JS, images)
- `src/main/resources/`: Configuration files

## Security Features

- **Session Management**: Implemented secure session handling for authenticated users
- **Password Security**: Uses BCrypt for secure password hashing
- **Role-based Access**: Different dashboards and access levels for admin and regular users

## Notes

- Cookies and filters are not yet implemented but placeholders are available for future implementation
- Default user passwords will be automatically hashed on first login

## Version Control

### Git Configuration

The project includes a `.gitignore` file that excludes sensitive configuration files like `application.properties` from version control.

### Working with Git

1. Clone the repository:
   ```
   git clone https://github.com/v-eenay/advanced-programming-tutorials-workshops.git
   ```

2. Create your own `application.properties` file from the template and configure your database settings.

3. Do not commit the `application.properties` file as it contains sensitive database credentials.

## Module Information

- Module: Advanced Programming and Technologies
- Module Leader: Binay Koirala
- Module Tutor: Sujan Subedi
- Institution: Itahari International College
