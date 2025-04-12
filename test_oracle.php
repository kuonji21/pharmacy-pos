<?php
// Oracle Connection Test Script
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>Oracle Database Connection Test</h1>";

// Include the database connection
require_once('connect.php');

echo "<h2>Connection Status</h2>";
echo "Connected to Oracle Database successfully!<br>";

// Test the USER table structure
try {
    echo "<h2>USER Table Structure</h2>";
    $stmt = $db->query("SELECT column_name, data_type, data_length FROM user_tab_columns WHERE table_name = 'USER'");
    if ($stmt->rowCount() > 0) {
        echo "<table border='1'>";
        echo "<tr><th>Column Name</th><th>Data Type</th><th>Length</th></tr>";
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            echo "<tr>";
            echo "<td>" . $row['COLUMN_NAME'] . "</td>";
            echo "<td>" . $row['DATA_TYPE'] . "</td>";
            echo "<td>" . $row['DATA_LENGTH'] . "</td>";
            echo "</tr>";
        }
        echo "</table>";
    } else {
        echo "No columns found for the USER table. The table might not exist or you don't have permission.<br>";
    }
} catch (PDOException $e) {
    echo "Error querying table structure: " . $e->getMessage() . "<br>";
}

// List all tables
try {
    echo "<h2>All Tables</h2>";
    $stmt = $db->query("SELECT table_name FROM user_tables");
    if ($stmt->rowCount() > 0) {
        echo "<ul>";
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            echo "<li>" . $row['TABLE_NAME'] . "</li>";
        }
        echo "</ul>";
    } else {
        echo "No tables found in the schema.<br>";
    }
} catch (PDOException $e) {
    echo "Error listing tables: " . $e->getMessage() . "<br>";
}

// Test query for users
try {
    echo "<h2>User Records</h2>";
    $stmt = $db->query("SELECT * FROM \"USER\"");
    if ($stmt->rowCount() > 0) {
        echo "<table border='1'>";
        echo "<tr>";
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        foreach ($row as $key => $value) {
            echo "<th>" . $key . "</th>";
        }
        echo "</tr>";
        
        // Reset pointer
        $stmt = $db->query("SELECT * FROM \"USER\"");
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            echo "<tr>";
            foreach ($row as $value) {
                echo "<td>" . $value . "</td>";
            }
            echo "</tr>";
        }
        echo "</table>";
    } else {
        echo "No users found in the USER table.<br>";
    }
} catch (PDOException $e) {
    echo "Error querying users: " . $e->getMessage() . "<br>";
}

// Test login query with hardcoded admin credentials
try {
    echo "<h2>Test Login Query</h2>";
    $login = 'admin';
    $password = 'admin';
    
    echo "Testing login with username: $login and password: $password<br>";
    
    $qry = "SELECT \"ID\", \"NAME\", \"POSITION\" FROM \"USER\" WHERE \"USERNAME\" = :login AND \"PASSWORD\" = :password";
    $stmt = $db->prepare($qry);
    $stmt->bindParam(':login', $login);
    $stmt->bindParam(':password', $password);
    $stmt->execute();
    
    if ($stmt->rowCount() > 0) {
        echo "Login successful!<br>";
        $member = $stmt->fetch(PDO::FETCH_ASSOC);
        echo "User ID: " . $member['ID'] . "<br>";
        echo "Name: " . $member['NAME'] . "<br>";
        echo "Position: " . $member['POSITION'] . "<br>";
    } else {
        echo "Login failed. No matching user found.<br>";
    }
} catch (PDOException $e) {
    echo "Error testing login: " . $e->getMessage() . "<br>";
}

echo "<h2>PHP Info</h2>";
echo "PHP Version: " . phpversion() . "<br>";

// Check if OCI8 is enabled
if (extension_loaded('oci8')) {
    echo "OCI8 Extension: Enabled<br>";
} else {
    echo "OCI8 Extension: Not enabled! This is required for Oracle connections.<br>";
}
?>