package infrastructure;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class Glob {

    /**
     * create a Json object out of a received http request
     *
     * @param request the data received from client
     * @return a Json object that contains all the data from the client
     */
    public static JSONObject makeJsonObject(HttpServletRequest request){
        try {
            InputStream in = request.getInputStream();
            byte[] buffer = new byte[256];
            StringBuilder sb = new StringBuilder();
            int actuallyRead;
            while((actuallyRead = in.read(buffer)) != -1){
                sb.append(new String(buffer,0,actuallyRead));
            }
            JSONObject data = new JSONObject(sb.toString());
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void send(JSONObject output,HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(output.toString());
    }
}
