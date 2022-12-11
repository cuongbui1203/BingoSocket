<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <form action="" method="POST">
        <label for="">Loại sản phẩm</label>
        <select name="SANPHAM" id="">
            <option value="Máy tính xách tay">Máy tính xách tay</option>
            <option value="Loại 2">Loại 2</option>
            <option value="Loại 3">Loại 3</option>
        </select>
        <br>
        <input type="submit" name="timkiem" value="Tìm kiếm">
    </form>

    <?php
        $conn = mysqli_connect('MAY10', 'root', '123456', 'QL_SP');

    if (isset($_POST['timkiem'])) {

        $sanpham = $_POST['SANPHAM'];

        $sql = "SELECT * FROM LOAISANPHAM WHERE SANPHAM = $sanpham ";
        $result = mysqli_query($conn, $sql);

        if ($result->num_rows > 0) {
    ?>
    <table>
        <thead>
            <th>STT</th>
            <th>Mã sản phẩm</th>
            <th>Tên sản phẩm</th>
            <th>Đơn giá</th>
        </thead>
        <tbody>
            <?php
                $i = 1;
                while ($row = $result->fetch_assoc()) {
                    echo '<tr><td>' + $i + '</td>' +
                        '<td>' + $row['MaSanPham'] + '</td>' +
                        '<td>' + $row['TenSanPham'] + '</td>' +
                        '<td>' + $row['DonGia'] + '</td></tr>';
                }
            }
        }
    
?>
        </tbody>

    </table>

</body>

</html>