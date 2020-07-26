package sunrose;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.Statement;

public class sunroseDB {

    //  Database credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "3434245";

    public static void main(String[] argv){
        connectDB();
    }

    private static void connectDB(){
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }

        try {
            Statement statement = connection.createStatement();

            String selectTableSQL = "select\n" +
                    "o.id ,\n" +
                    "o.products\n" +
                    "from sunrose.orders o \n" +
                    "where o.orderstatusid = 3 and o.products is not null\n" +
                    "order by o.orderdate desc\n";
            ResultSet rs = statement.executeQuery(selectTableSQL);

            System.out.println(selectTableSQL);
            Statement statement2 = connection.createStatement();
            while (rs.next()) {
                String id = rs.getString("id");
                String products = rs.getString("products");

                System.out.println("orderId: " + id);
                System.out.println("products\t: " + products);

                String[] lines = products.split(",");
                String[] subLines = new String[2];
                for (int i=0; i< lines.length; i++){
                    subLines = lines[i].split(":");

                    System.out.println("\tproductId: " + subLines[0]);
                    System.out.println("\tamount: " + subLines[1]);

                    String insertTableSQL = "INSERT INTO sunrose.orderlines" +
                            " (id, orderid, productid, amount)" +
                            "VALUES(uuid_generate_v1(), "+ id + "," + subLines[0] + "," + subLines[1] + ");";

                    statement2.executeUpdate(insertTableSQL);
                }

            }

        } catch (SQLException e) {
            System.out.println("Can't read or update table");
            e.printStackTrace();
        }
    }

    private static void pars(){
        String str = "678:1,219:1,1031:1,";


        String[] lines = str.split(",");

        String[] subLines = new String[lines.length * 2];

        for(int i = 0; i < lines.length; i++ ){
            subLines = lines[i].split(":");
        }

        for(String word: subLines){
            System.out.println(word);
        }
    }
}

