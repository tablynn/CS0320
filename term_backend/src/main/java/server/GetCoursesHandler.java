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
 * This class retrieves all the course data for the web application from the SQL database
 * and is run when a fetch to the API server is made with the endpoint getCourseData.
 */
public class GetCoursesHandler implements Route{

  /**
   * Handle method that generates and returns a response to the API server. The response is
   * in the form of a list of list of strings, where the inner list is all the information
   * related to one course.
   *
   * @param request
   * @param response
   * @return courseInformation - list of list of strings containing all course data
   */
  @Override
  public Object handle(Request request, Response response) {
    List<List<String>> courseInformation = new ArrayList<List<String>>();

    try {
      // Load the driver and establish a connection to the database
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      // Tell the database to enforce foreign keys
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep;
      ResultSet rs;

      // Set rs to the result of querying for all rows in the classes table
      prep = conn.prepareStatement("select * from classes");
      rs = prep.executeQuery();
      System.out.println("executing get course data");

      // For each relevant row, generate innerList and add innerList to courseInformation
      while(rs.next()){
        List<String> innerList = new ArrayList<String>();
        String title = rs.getString(2);
        String instructor = rs.getString(3);
        String instructorEmail = rs.getString(4);
        //String description = rs.getString(5);
        innerList.add(title);
        innerList.add(instructor);
        //innerList.add(description);
        innerList.add(instructorEmail);
        courseInformation.add(innerList);
        //System.out.println("info: " + title + " " + instructor + " " + description + " " + instructorEmail);
      }

    } catch (SQLException e){
      System.out.println("caught this exception in GetCoursesHandler: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception in GetCoursesHandler: " + f);
    }

    // Serializes responses into JSON format
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(List.class).toJson(courseInformation);
  }
}
