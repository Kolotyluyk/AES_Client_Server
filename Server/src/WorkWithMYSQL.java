/**
 * Created by Сергій on 26.11.2016.
 */
import java.sql.*;

public class WorkWithMYSQL
{

    public static String getPass(String login)
    {String password="";
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
             String myUrl = "jdbc:mysql://localhost:3306/Cripto";
             Connection conn = DriverManager.getConnection(myUrl,"root","");

              String query = "SELECT password FROM user WHERE login=\'"+ login+"\'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next())
            {
                 password = rs.getString("password");
            }
            st.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return password;
    }
}
