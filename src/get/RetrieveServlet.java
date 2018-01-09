package get;

import infrastructure.ConnectionParam;
import infrastructure.Glob;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static infrastructure.ConnectionParam.*;

@WebServlet(name = "RetrieveServlet",urlPatterns = "/get")
public class RetrieveServlet extends HttpServlet {

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
        JSONObject output = null;
        try {
            int action = input.getInt("action");
            switch(action){
                case 0:
                    output = get("SELECT " + PHONE_NUMBER + ", " + NAME + ", " + ADDRESS + " FROM " + USERS + " WHERE " + VOLUNTEER + "=" + input.getInt("volunteer"),true);
                    break;//"SELECT phoneNumber, name, address FROM " + USERS + " WHERE volunteer=" + input.getInt("volunteer"),true);
                case 1:
                    output = get("SELECT * FROM " + SHELTERS,false);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glob.send(output,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * read from the db the inserted query
     * @param query the query to run
     * @param users true if you search for users, false if you search for shelters
     * @return a Json object contains a Json array with all the data from the query
     * @throws JSONException in case of a problem creating the Json object
     */
    private JSONObject get(String query, boolean users) throws JSONException {
        try(
                Connection connection = DriverManager.getConnection(SQL_URL + SQL_DATABASE, SQL_USERNAME,SQL_PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ){
            JSONArray array = new JSONArray();
            while(resultSet.next()){
                JSONObject item = new JSONObject();
                item.put(ConnectionParam.NAME,resultSet.getString(ConnectionParam.NAME));
                item.put(ConnectionParam.ADDRESS,resultSet.getString(ConnectionParam.ADDRESS));
                if(users)
                    item.put(ConnectionParam.PHONE_NUMBER,resultSet.getString(ConnectionParam.PHONE_NUMBER));
                array.put(item);
            }
            JSONObject output = new JSONObject();
            output.put(users ? "users" : "shelters",array);
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
