import java.sql.*;

public class DistributedTransaction {

    public static void main(String[] args) throws SQLException {

        String jdbcDriver = "com.mysql.jdbc.Driver";
        Connection localConnection = null;
        Connection localConnection2 = null;
        Connection remoteConnection = null;

        try {
            Class.forName(jdbcDriver);
            localConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment2_Local", "root", "rootpass");
            if (localConnection != null) {
                System.out.println("Local DB Connected");
            } else {
                System.out.println("Local DB Connection Aborted");
            }
        } catch (Exception errorMessage) {
            System.out.println(errorMessage);
        }
        try {
            Class.forName(jdbcDriver);
            localConnection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment2_Local", "root", "rootpass");
            if (localConnection2 != null) {
                System.out.println("Local second DB Connected");
            } else {
                System.out.println("Local second DB Connection Aborted");
            }
        } catch (Exception errorMessage) {
            System.out.println(errorMessage);
        }
        try {
            Class.forName(jdbcDriver);
            remoteConnection = DriverManager.getConnection("jdbc:mysql://34.70.102.41:3306/assignment2_remote", "root1", "root");
            if (remoteConnection != null) {
                System.out.println("Remote DB Connected");
            } else {
                System.out.println("Remote DB Connection Aborted");
            }
        } catch (Exception errorMessage) {
            System.out.println(errorMessage);
        }

        //Setting autocommit as false
        localConnection.setAutoCommit(false);
        localConnection2.setAutoCommit(false);
        remoteConnection.setAutoCommit(false);

        Statement localStatement = localConnection.createStatement();
        Statement localStatement2 = localConnection2.createStatement();
        Statement remoteStatement = remoteConnection.createStatement();
        ResultSet localResultSet = null;
        ResultSet localResultSet2 = null;
        ResultSet remoteResultSet = null;
        String querySequenceOneT1 = "select * from customers where customer_zip_code_prefix= 01151";
        String querySequenceOneT2 = "select * from customers where customer_zip_code_prefix= 01151";
        String querySequenceTwoT1 = "update customers set customer_city = 'T1 City' where customer_zip_code_prefix = 01151;";
        String querySequenceTwoT2 = "update customers set customer_city = 'T2 City' where customer_zip_code_prefix = 01151;";

        try {
            localResultSet = localStatement.executeQuery(querySequenceOneT1);
            if (localResultSet.next()) {
                System.out.println("SequenceOneT1 Entries Found");
            } else {
                System.out.println("No entries");
            }
            localResultSet2 = localStatement2.executeQuery(querySequenceOneT2);
            if (localResultSet2.next()) {
                System.out.println("SequenceOneT2 Entries Found");
            } else {
                System.out.println("No entries");
            }
            int localResultSetSeqTwo = localStatement.executeUpdate(querySequenceTwoT1);
            if (localResultSetSeqTwo > 0) {
                System.out.println("SequenceTwoT1 Entries Updated");
            } else {
                System.out.println("SequenceTwoT1 no entries updated");
            }

            int localTwoResultSetSeqTwo = localStatement2.executeUpdate(querySequenceTwoT2);
            if (localTwoResultSetSeqTwo > 0) {
                System.out.println("SequenceTwoT2 Entries Updated");
            } else {
                System.out.println("SequenceTwoT2 no entries updated");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            localConnection2.commit();
            localConnection.commit();

            if (localConnection != null) {
                localConnection.close();
            }
            if (localConnection2 != null) {
                localConnection2.close();
            }
            if (localStatement != null) {
                localStatement.close();
            }
            if (localStatement2 != null) {
                localStatement2.close();
            }
            if (localResultSet != null) {
                localResultSet.close();
            }
            if (localResultSet2 != null) {
                localResultSet2.close();
            }
        }
    }
}
