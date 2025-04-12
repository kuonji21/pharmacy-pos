# Oracle Database Migration Guide

This document outlines the steps taken to migrate the Pharmacy Point of Sales system from MySQL (XAMPP/phpMyAdmin) to Oracle Database Express Edition 21.3 with SQL Developer.

## Changes Made

The following files have been updated:

1. `application.properties` - Updated Spring Boot database configuration to use Oracle
2. `connect.php` - Updated PHP PDO connection to use Oracle OCI driver
3. `oracle_schema.sql` - Created new SQL script to set up the Oracle database schema

## Installation Steps

### 1. Install Oracle Database Express Edition 21.3

1. Download Oracle Database XE 21.3 from [Oracle's website](https://www.oracle.com/database/technologies/xe-downloads.html)
2. Run the installer and follow the on-screen instructions
3. Set a password for the SYS and SYSTEM accounts when prompted
4. Complete the installation and make sure the Oracle service is running

### 2. Install Oracle SQL Developer

1. Download Oracle SQL Developer from [Oracle's website](https://www.oracle.com/tools/downloads/sqldev-downloads.html)
2. Extract the files and run the application (no installer required for Windows)

### 3. Connect to Oracle Database

1. Open SQL Developer and connect using the SYSTEM account:
   - Username: SYSTEM
   - Password: password
   - Connection Type: Basic
   - Hostname: localhost
   - Port: 1521
   - SID: XE

2. No need to create a separate user since we'll use the SYSTEM schema directly. This is equivalent to the 'sales' database in MySQL but in Oracle, schemas are tied to users.

### 4. Run the Schema Script

1. In SQL Developer, make sure you're connected as SYSTEM user:
   - Username: SYSTEM
   - Password: password
   - Connection Type: Basic
   - Hostname: localhost
   - Port: 1521
   - SID: XE

2. Open the `oracle_schema.sql` script in SQL Developer
3. Run the script to create all tables, sequences, triggers, and initial data in the SYSTEM schema

## Additional Configuration

### Java Application (Spring Boot)

1. Add Oracle JDBC driver to your project by adding this to your `pom.xml`:
   ```xml
   <dependency>
       <groupId>com.oracle.database.jdbc</groupId>
       <artifactId>ojdbc8</artifactId>
       <version>21.3.0.0</version>
   </dependency>
   ```

2. Update the password in `application.properties` to match the one you set for the sales user.

### PHP Application

1. Enable the Oracle OCI8 extension in PHP:
   - For Windows: Uncomment `extension=oci8_12c` or `extension=oci8` in your php.ini file
   - For Linux: Install the php-oci8 package

2. Install the Oracle Instant Client from [Oracle's website](https://www.oracle.com/database/technologies/instant-client/downloads.html)

3. Update the password in `connect.php` to match the one you set for the sales user.

## Common Issues and Troubleshooting

1. **Connection Issues**: 
   - Ensure Oracle service is running
   - Verify that the TNS listener is started
   - Check that firewall allows connections on port 1521

2. **SQL Syntax Differences**:
   - Replace backticks (`) with double quotes (")
   - LIMIT/OFFSET syntax is different in Oracle, use ROWNUM or ROW_NUMBER()
   - Date functions might need to be adjusted

3. **PHP OCI Extension**:
   - If you get "Call to undefined function oci_connect()", ensure the OCI8 extension is properly installed and enabled

4. **Case Sensitivity**:
   - Oracle treats unquoted identifiers as uppercase by default
   - Use double quotes for case-sensitive identifiers

## Verifying the Migration

1. Start your application after all configuration changes
2. Check database connectivity
3. Test the main functionality of your application
4. Use SQL Developer to verify that data is being properly stored in the Oracle database

For any Oracle-specific SQL syntax questions, refer to the [Oracle Database Documentation](https://docs.oracle.com/en/database/oracle/oracle-database/).