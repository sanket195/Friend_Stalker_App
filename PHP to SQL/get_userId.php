
<?php

$servername = "mssql6.gear.host";
$username = “friend_stalker”;
$password = "Di78B?!Qu5ou";
$dbname=“friend_stalker”;


$user_email =“sanket@gmail.com";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "SELECT * FROM friends WHERE user_email='".$user_email;
$result = $conn->query($sql);





?>