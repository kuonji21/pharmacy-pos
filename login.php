<?php
	//Start session
	session_start();
	
	//Array to store validation errors
	$errmsg_arr = array();
	
	//Validation error flag
	$errflag = false;
	
	// Include the database connection
	require_once('connect.php');
	
	// Use the connection from connect.php
	try {
		$conn = $db;
	} catch(PDOException $e) {
		die('Failed to connect to server: ' . $e->getMessage());
	}
	
	//Function to sanitize values received from the form. Prevents SQL injection
	function clean($str) {
		$str = @trim($str);
		if(get_magic_quotes_gpc()) {
			$str = stripslashes($str);
		}
		return $str; // No need for mysqli_real_escape_string with PDO
	}
	
	//Sanitize the POST values
	$login = ($_POST['username']);
	$password = ($_POST['password']);
	
	//Input Validations
	if($login == '') {
		$errmsg_arr[] = 'Username missing';
		$errflag = true;
	}
	if($password == '') {
		$errmsg_arr[] = 'Password missing';
		$errflag = true;
	}
	
	//If there are input validations, redirect back to the login form
	if($errflag) {
		$_SESSION['ERRMSG_ARR'] = $errmsg_arr;
		session_write_close();
		header("location: index.php");
		exit();
	}
	
	//Create query for MySQL
	$qry = "SELECT id, name, position FROM user WHERE username = :login AND password = :password";
	$stmt = $conn->prepare($qry);
	$stmt->bindParam(':login', $login);
	$stmt->bindParam(':password', $password);
	$stmt->execute();
	
	//Check whether the query was successful or not
	if($stmt) {
		if($stmt->rowCount() > 0) {
			//Login Successful
			session_regenerate_id();
			$member = $stmt->fetch(PDO::FETCH_ASSOC);
			$_SESSION['SESS_MEMBER_ID'] = $member['id'];
			$_SESSION['SESS_FIRST_NAME'] = $member['name'];
			$_SESSION['SESS_LAST_NAME'] = $member['position'];
			//$_SESSION['SESS_PRO_PIC'] = $member['profImage'];
			session_write_close();
			header("location: main/index.php");
			exit();
		}else {
			//Login failed
			header("location: index.php");
			exit();
		}
	}else {
		die("Query failed");
	}
?>