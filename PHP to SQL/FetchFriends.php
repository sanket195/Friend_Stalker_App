<?php

$servername = "mssql6.gear.host";
$username = "friend_stalker";
$password = "Di78B?!Qu5ou";
$dbname="friend_stalker";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$email = 'juhi@gmail.com';
		
$sql = "SELECT * FROM friends WHERE user_email='".$email."'";
$result = $conn->query($sql);
while($row1 = $result->fetch_assoc()) {
$friendEmail_array[] = $row1['friend_email'];
}
echo json_encode($friendEmail_array);
?>