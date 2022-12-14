package server;

import com.squareup.moshi.Moshi;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

// http://localhost:3231/getCourseData

/**
 * This class retrieves the course data for the web application and is run when a fetch to the API
 * server is made with the endpoint getCourseData
 */
public class GetCoursesHandler implements Route{

  @Override
  public Object handle(Request request, Response response) {
    List<List<String>> courseInformation = new ArrayList<List<String>>();

    try {
      //Load the driver and establish a connection to the database
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      //Tell the database to enforce foreign keys
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep;
      ResultSet rs;

      //Set rs to the result of querying for all rows in the classes table
      prep = conn.prepareStatement("select * from classes");
      rs = prep.executeQuery();

      //While there is still a next row, write each of the values of each attribute in the row to
      //a String, and add each String to an inner list. Then, add the list corresponding to the
      //data for a single class to an outer list of classes
      while(rs.next()){
        List<String> tempList = new ArrayList<String>();
        String title = rs.getString(2);
        String instructor = rs.getString(3);
        String instructorEmail = rs.getString(4);
        String description = rs.getString(5);
        tempList.add(title);
        tempList.add(instructor);
        tempList.add(description);
        tempList.add(instructorEmail);
        courseInformation.add(tempList);
        System.out.println("info: " + title + " " + instructor + " " + description + " " + instructorEmail);
      }

    } catch (SQLException e){
      System.out.println("caught this exception: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception: " + f);
    }

    // Serializes responses into JSON format
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(List.class).toJson(courseInformation);
  }
}
