package infrastructure;

public class ConnectionParam {

    // columns names
    public static final String ADDRESS = "address";
    public static final String NAME = "name";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String VOLUNTEER = "volunteer";

    // the Strings that represent the needed info to connect to MySQL
    public static final String SQL_USERNAME = ""; // the user name after creating the mysql server
    public static final String SQL_PASSWORD = ""; // the password after creating the mysql server
    public static final String SQL_URL = "jdbc:mysql:///"; // jdbc:mysql://{the computer ip, or localhost for this computer}:3306
    public static final String SQL_DATABASE = "Hitnadvut_schema";


    //tables names
    public static final String SHELTERS = "Shelters";
    public static final String USERS = "Users";

}
