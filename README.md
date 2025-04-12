# Pharmacy POS System

A Point of Sale system for pharmacies with both PHP and Spring Boot implementations.

## Table of Contents
- [Overview](#overview)
- [GitHub Setup](#github-setup)
- [Database Setup](#database-setup)
- [PHP Implementation](#php-implementation)
- [Spring Boot Implementation](#spring-boot-implementation)
- [Troubleshooting](#troubleshooting)

## Overview

This Pharmacy POS system provides a complete solution for pharmacy point-of-sale operations. It includes:
- User authentication system
- Product management
- Sales processing
- Inventory tracking
- Reports generation

The system can be run either as a PHP implementation (using XAMPP) or as a Spring Boot application.

## GitHub Setup

### Pushing the Project to GitHub

1. **Create a GitHub Repository**
   - Go to [GitHub](https://github.com) and sign in
   - Click the "+" icon in the top right, then "New repository"
   - Name your repository (e.g., "pharmacy-pos")
   - Add a description (optional)
   - Choose visibility (Public or Private)
   - Click "Create repository"

2. **Initialize and Push Your Project**
   ```bash
   # Initialize git repository (in your project folder)
   git init

   # Create .gitignore file
   echo "# Java and Maven files" > .gitignore
   echo "target/" >> .gitignore
   echo ".mvn/" >> .gitignore
   echo "*.class" >> .gitignore
   echo ".DS_Store" >> .gitignore
   echo "*.log" >> .gitignore
   echo ".idea/" >> .gitignore
   echo ".vscode/" >> .gitignore

   # Add all files to git
   git add .

   # Commit the changes
   git commit -m "Initial commit"

   # Add GitHub as the remote repository (replace YOUR_USERNAME with your GitHub username)
   git remote add origin https://github.com/YOUR_USERNAME/pharmacy-pos.git

   # Push to GitHub
   git push -u origin master
   ```

### Cloning the Project

1. **Clone the Repository**
   ```bash
   # Replace YOUR_USERNAME with the actual GitHub username
   git clone https://github.com/YOUR_USERNAME/pharmacy-pos.git
   cd pharmacy-pos
   ```

## Database Setup

1. **Install XAMPP**
   - Download and install XAMPP from [apachefriends.org](https://www.apachefriends.org/)
   - Start the XAMPP Control Panel
   - Start the Apache and MySQL services by clicking the "Start" buttons

2. **Create and Import the Database**
   - Open your browser and go to http://localhost/phpmyadmin
   - Create a new database:
     - Click on "New" in the left sidebar
     - Enter "sales" as the database name (case-sensitive)
     - Select "utf8_general_ci" as collation
     - Click "Create"
   - Import the database schema:
     - Select the "sales" database from the left sidebar
     - Click the "Import" tab at the top
     - Click "Choose File" and select the "sales.sql" file from your project
     - Click "Go" or "Import" to execute the SQL script

## PHP Implementation

1. **Deploy the PHP Files**
   - Copy all PHP files to XAMPP's web directory:
     - **Windows**: Copy to `C:\xampp\htdocs\pharmacy-pos`
     - **Mac**: Copy to `/Applications/XAMPP/htdocs/pharmacy-pos`
     - **Linux**: Copy to `/opt/lampp/htdocs/pharmacy-pos`

2. **Configure Database Connection**
   - Verify `connect.php` has the correct database settings:
     ```php
     $db_host = 'localhost';
     $db_user = 'root';
     $db_pass = ''; // Default XAMPP has empty password
     $db_name = 'sales';
     ```

3. **Access the Application**
   - Ensure Apache and MySQL are running in XAMPP Control Panel
   - Open your browser and go to: http://localhost/pharmacy-pos
   - Log in with one of these credentials:
     - Username: admin, Password: admin
     - Username: cashier, Password: cashier

## Spring Boot Implementation

1. **Prerequisites**
   - Ensure you have Java 21 installed:
     ```bash
     java -version
     ```
   - Ensure you have Maven installed:
     ```bash
     mvn -version
     ```

2. **Configure Application**
   - Check `src/main/resources/application.properties` has correct database settings:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/sales?useSSL=false&serverTimezone=UTC
     spring.datasource.username=root
     spring.datasource.password=
     ```

3. **Build and Run**
   - Build the project:
     ```bash
     mvn clean package
     ```
   - Run the application:
     ```bash
     java -jar target/pos-0.0.1-SNAPSHOT.jar
     ```
   - Access the application at http://localhost:8080
   - Log in with the same credentials as the PHP implementation

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running in XAMPP Control Panel
- Verify the database name is exactly 'sales' (case-sensitive)
- Check that database credentials match your XAMPP setup (default username: root, password: empty)

### PHP Application Issues
- Ensure Apache is running in XAMPP Control Panel
- Check file permissions (read/write access to the web directory)
- Verify PHP version compatibility (XAMPP includes compatible PHP version)

### Spring Boot Application Issues
- Ensure you're using Java 21 or compatible version
- Verify the MySQL connector is properly included in the pom.xml
- Check for any firewall issues that might be blocking port 8080
- Make sure port 8080 is not already in use by another application

### Browser Issues
- Clear browser cache and cookies
- Try a different browser
- Enable JavaScript if disabled

If you encounter any specific errors not covered here, please open an issue in the GitHub repository with details about the error message and steps to reproduce.