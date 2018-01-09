package add;

import infrastructure.Glob;
import infrastructure.ConnectionParam;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static infrastructure.ConnectionParam.*;

@WebServlet(name = "add.UserServlet",urlPatterns = "/add")
public class UserServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver()); // create the object that will start the link
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject input = Glob.makeJsonObject(request);
        JSONObject output = new JSONObject();
        try {
            output.put("suc",insertUser(input));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("the data was not inserted, a problem with the Json creation");
        }
        Glob.send(output,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    /**
     * insert a user to the db
     *
     * @param input the Json object containing the data of the user
     * @return true if the insertion succeeded
     * @throws JSONException in case data could not be extracted from the given Json object)
     */
    private boolean insertUser(JSONObject input) throws JSONException {
        String query = "INSERT INTO " + USERS + " (" + ADDRESS + ", " + NAME + ", " + PHONE_NUMBER + ", " + VOLUNTEER + ") VALUES (?,?,?,?)";
        try(
                Connection connection = DriverManager.getConnection(SQL_URL + SQL_DATABASE, SQL_USERNAME,SQL_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            preparedStatement.setString(1,input.getString(ConnectionParam.ADDRESS));
            preparedStatement.setString(2,input.getString(ConnectionParam.NAME));
            preparedStatement.setString(3, input.getString(ConnectionParam.PHONE_NUMBER));
            preparedStatement.setInt(4, input.getInt(ConnectionParam.VOLUNTEER));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
