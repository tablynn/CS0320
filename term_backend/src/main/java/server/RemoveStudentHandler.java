package server;

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

//Query Parameters: student name, class name
// http://localhost:3231/removeStudent?studentName=Christine%20Wu&className=CSCI%201470:%20Deep%20Learning

public class RemoveStudentHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    // If the query parameters are valid ...
    if (qm.hasKey("studentName") && qm.hasKey("className")){
      String studentName = qm.value("studentName");
      String className = qm.value("className");

      return handleTables(studentName, className);

    } else {
      return "failure with the provided query parameters";
    }
  }

  public List<String> handleTables(String studentName, String className){
    List<String> courseInformation = new ArrayList<String>();

    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep = null;
      ResultSet rs = null;

      // Get class_id from 'classes' table
      Integer classID = this.getClassID(prep, conn, rs, className);
      System.out.println("the class " + className + " has classID " + classID);

      // Get student_id from 'students' table
      Integer studentID = this.getStudentID(prep, conn, rs, studentName);

      // Check if classID-studentID pair exists in 'enrollments' table
      Boolean studentExistsInEnrollmentsWithCorrectClass = this.checkIfStudentExistsInEnrollments(prep, conn, rs, studentID, classID);

      // If pair exists, remove pair from 'enrollments' table
      if(studentExistsInEnrollmentsWithCorrectClass){
        courseInformation.add("student exists in enrollments table with correct class");
      } else {
        courseInformation.add("student doesn't exist in enrollments table with correct class so cannot be removed");
      }

    } catch (SQLException e){
      System.out.println("caught this exception: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception: " + f);
    }

    System.out.println("created list of student names to be returned: " + courseInformation);
    return courseInformation;
  }

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

  private Integer getStudentID(PreparedStatement prep, Connection conn, ResultSet rs, String studentName)
      throws SQLException {
    prep = conn.prepareStatement("select * from students WHERE name = ?");
    prep.setString(1, studentName);
    rs = prep.executeQuery();
    Integer studentID = 0;

    while(rs.next()){
      studentID = rs.getInt(1);
    }
    return studentID;
  }

  private Boolean checkIfStudentExistsInEnrollments(PreparedStatement prep, Connection conn, ResultSet rs, Integer studentID, Integer classID)
      throws SQLException {
    prep = conn.prepareStatement("select * from enrollments WHERE student_id = ?");
    prep.setInt(1, studentID);
    rs = prep.executeQuery();
    Boolean studentExistsInEnrollmentsWithCorrectClass = false;

    while(rs.next()){
      Integer currClassID = rs.getInt(3);
      if(currClassID == classID){
        studentExistsInEnrollmentsWithCorrectClass = true;
      }
    }
    return studentExistsInEnrollmentsWithCorrectClass;
  }
}
