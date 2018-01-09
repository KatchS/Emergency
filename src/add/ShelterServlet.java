package add;

import infrastructure.ConnectionParam;
import infrastructure.Glob;
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

@WebServlet(name = "ShelterServlet",urlPatterns = "/shelter")
public class ShelterServlet extends HttpServlet {

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
            output.put("suc",insertShelter(input));
        } catch (JSONException e) {
            e.printStackTrace();
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
    private boolean insertShelter(JSONObject input) throws JSONException {
        String query = "INSERT INTO " + SHELTERS + " (" + ADDRESS + ", " + NAME + ") VALUES (?,?)";
        try(
                Connection connection = DriverManager.getConnection(SQL_URL + SQL_DATABASE, SQL_USERNAME,SQL_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ){
            preparedStatement.setString(1,input.getString(ConnectionParam.ADDRESS));
            preparedStatement.setString(2,input.getString(ConnectionParam.NAME));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
