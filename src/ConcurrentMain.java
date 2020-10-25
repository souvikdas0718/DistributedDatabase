import java.sql.SQLException;

public class ConcurrentMain {
    public static void main(String[] args) {

        try {
            DBConnection transaction1 = new DBConnection();
            DBConnection transaction2 = new DBConnection();

            transaction1.connect();
            transaction2.connect();
            transaction1.start();
            transaction2.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
