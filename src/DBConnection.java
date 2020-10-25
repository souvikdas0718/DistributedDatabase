import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection extends Thread {
    public static boolean flag = true;
    Connection localConnection = null;
    Connection remoteConnection = null;
    Statement localStatement = null;
    Statement remoteStatement = null;
    String jdbcDriver = "com.mysql.jdbc.Driver";

    public void connect() {
        try {
            Class.forName(jdbcDriver);
            localConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment2_Local", "root", "rootpass");
            localConnection.setAutoCommit(false);
            remoteConnection = DriverManager.getConnection("jdbc:mysql://34.70.102.41:3306/assignment2_remote", "root1", "root");
            remoteConnection.setAutoCommit(false);
        } catch (Exception errorMessage) {
            System.out.println(errorMessage);
        }
    }

    public void run() {
        System.out.println("Current Thread Id:" + Thread.currentThread().getId());
        while (flag) {
            flag = false;
            String startTransaction = "START TRANSACTION";
            String localUpdateOne = "update customers set customer_city = 'halifax', customer_state = 'NS' where customer_zip_code_prefix = 24220";
            String localUpdatetwo = "update geolocation set geolocation_city  = 'halifax', geolocation_state = 'NS' where geolocation_zip_code_prefix = 01001";
            String remoteUpdate = "update sellers set seller_city  = 'halifax', seller_state = 'NS' where seller_zip_code_prefix = 1026";
            String remoteInsert = "insert into products(product_id, product_category_name, product_name_lenght, product_description_lenght,\n" +
                    "                     product_photos_qty, product_weight_g, product_length_cm, product_height_cm, product_width_cm)\n" +
                    "values ('004938452c98ff9ab28e9e7b4bfe98qr', 'halifax_hoodies', 48, 500, 5, 2500, 100, 25, 50)";
            String remoteDelete = "delete from product_category_name where product_category_name_english = 'food_drink'";
            String endTransaction = "COMMIT";

            try {
                localStatement = localConnection.createStatement();
                remoteStatement = remoteConnection.createStatement();

                localStatement.execute(startTransaction);
                localStatement.execute(localUpdateOne);
                localStatement.execute(localUpdatetwo);
                localStatement.execute(endTransaction);

                remoteStatement.execute(startTransaction);
                remoteStatement.execute(remoteUpdate);
                remoteStatement.execute(remoteInsert);
                remoteStatement.execute(remoteDelete);
                remoteStatement.execute(endTransaction);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
                flag = true;
                break;
            }
        }
    }

    public void disconnect() {
        try {
            if (localConnection != null) {
                localConnection.close();
            }
            if (localStatement != null) {
                localStatement.close();
            }
            if (remoteConnection != null) {
                remoteConnection.close();
            }
            if (remoteStatement != null) {
                remoteStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

