<html>

<head>
<title>ET Phone Home</title>
</head>
<?
$date=Date("D M j G:i:s T Y");
print($date);
print("<br>");

?>

<?
$path="vawr-log.out";

echo $_SERVER[QUERY_STRING], "<br>";
print($_SERVER[QUERY_STRING]);
$data=$date . ": " . $_SERVER[QUERY_STRING] . "\r\n";

file_put_contents($path, $data, FILE_APPEND);

$parts = parse_url($url);
parse_str($parts['query'], $query);
echo $query['email'];

$out = $query['email'];
print($out);

?>
<body>
</body>
</html>
