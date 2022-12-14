package server;

import com.squareup.moshi.Moshi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

// http://localhost:3231/getCourseWaitlist?className=CSCI%201470:%20Deep%20Learning

/**
 * This class retrieves the waitlist data for a single class and is run when a fetch to the API
 * server is made with the endpoint getCourseWaitlist given request parameter className.
 */
public class GetCourseWaitlistHandler implements Route {

  /**
   * Handle method checks query parameters and calls helper method handleTables to
   * generate the API server response.
   *
   * @param request
   * @param response
   * @return - list of list of strings containing course information
   */
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    // If query parameter is valid run helper handleTables
    if (qm.hasKey("className")){
      String className = qm.value("className");
      return handleTables(className);
    } else {
      return "failure with the provided query parameters";
    }
  }

  /**
   * Helper method that gets the classID, uses the classID to get all students on
   * that course's waitlist from the 'enrollments' table, and generates a list of
   * strings containing the names of the students on that course's waitlist.
   *
   * @param className - name of class
   * @return
   */
  public Object handleTables(String className){
    List<List<String>> courseInformation = new ArrayList<List<String>>();

    try {
      // Load the driver and establish a connection to the database
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      // Tell the database to enforce foreign keys
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep = null;
      ResultSet rs = null;

      // Get class_id from "classes" table
      Integer classID = this.getClassID(prep, conn, rs, className);
      System.out.println("the class " + className + " has classID " + classID);

      // Use classID to create list of studentIDs on that course's waitlist
      List<String> studentIDList = new ArrayList<String>();

      prep = conn.prepareStatement("select * from enrollments WHERE class_id = ?");
      prep.setInt(1, classID);
      rs = prep.executeQuery();

      // Create list of student IDs matching class ID queried on
      while(rs.next()){
        studentIDList.add(rs.getString(2));
      }
      System.out.println("created a list of relevant student IDs: " + studentIDList);

      // Use student ID list to get all student names/emails from students table query
      prep = conn.prepareStatement("select * from students WHERE student_id = ?");
      for (int i = 0; i < studentIDList.size(); i++){
        prep.setString(1, studentIDList.get(i));
        rs = prep.executeQuery();

        // For each relevant row, generate innerList and add to courseInformation
        while(rs.next()){
          List<String> innerList = new ArrayList<String>();
          String studentName = rs.getString(2);
          String email = rs.getString(3);
          innerList.add(studentName);
          innerList.add(email);
          courseInformation.add(innerList);
        }
      }

    } catch (SQLException e){
      System.out.println("caught this exception in GetCourseWaitlistHandler: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception in GetCourseWaitlistHandler: " + f);
    }

    System.out.println("created list of student names to be returned: " + courseInformation);

    // Serializes responses into JSON format
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(List.class).toJson(courseInformation);
  }

  /**
   * Helper method that gets the class_ID from the 'classes' table using className.
   *
   * @param prep - PreparedStatement
   * @param conn - Connection
   * @param rs - ResultSet
   * @param className - name of the class
   * @return classID - classID of className
   * @throws SQLException
   */
  private Integer getClassID(PreparedStatement prep, Connection conn, ResultSet rs, String className)
      throws SQLException {
    prep = conn.prepareStatement("select * from classes WHERE title = ?");
    prep.setString(1, className);
    rs = prep.executeQuery();
    Integer classID = 0;

    while (rs.next()) {
      classID = rs.getInt(1);
    }
    return classID;
  }
}
