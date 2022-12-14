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

/**
 * This class removes a student from a waitlist and retreieves an updated waitlist
 * It is run when a fetch to the API server is made with the endpoint removeStudent given parameters
 * studentName email and className
 */
public class RemoveStudentHandler implements Route {

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

    // If query parameters are valid run helper handleTables
    if (qm.hasKey("studentName") && qm.hasKey("className")){
      String studentName = qm.value("studentName");
      String className = qm.value("className");

      return handleTables(studentName, className);

    } else {
      return "failure with the provided query parameters";
    }
  }

  /**
   * Helper method that connects to the database, checks if there is an enrollment matching
   * the student and class criteria to remove the student, and then deletes the row from
   * the 'enrollments' table.
   *
   * @param className - name of class
   * @param studentName - name of student
   * @return studentWaitlist - updated waitlist for the course
   */
  public List<String> handleTables(String studentName, String className){
    List<String> studentWaitlist = new ArrayList<String>();

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

      // Get class_id from 'classes' table
      Integer classID = this.getClassID(prep, conn, rs, className);
      System.out.println("the class " + className + " has classID " + classID);

      // Get student_id from 'students' table
      Integer studentID = this.getStudentID(prep, conn, rs, studentName);
      System.out.println("the student " + studentName + " has studentID " + studentID);

      // Check if classID-studentID pair exists in 'enrollments' table
      prep = conn.prepareStatement("select * from enrollments WHERE student_id = ?");
      prep.setInt(1, studentID);
      rs = prep.executeQuery();
      Boolean studentExistsInEnrollmentsWithCorrectClass = false;
      Integer enrollID = 0;

      // If studentID and classID exist in one row in 'enrollments' table then update values
      while(rs.next()){
        Integer currClassID = rs.getInt(3);
        if(currClassID == classID){
          studentExistsInEnrollmentsWithCorrectClass = true;
          enrollID = rs.getInt(1);
        }
      }

      // If pair exists, remove pair from 'enrollments' table
      if(studentExistsInEnrollmentsWithCorrectClass){
        this.removeStudent(prep, conn, rs, enrollID);
        System.out.println("removed the pair: studentID= " + studentID + " classID= " + classID + " from enrollments table");
      }

      // Return: list of names of students in waitlist in order for that specific course
      studentWaitlist = this.createReturnedWaitlist(prep, conn, rs, classID);

    } catch (SQLException e){
      System.out.println("caught this exception in RemoveStudentHandler: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception in RemoveStudentHandler: " + f);
    }

    return studentWaitlist;
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


  /**
   * Helper method that gets the student_ID from the 'students' table using studentName.
   *
   * @param prep - Prepared Statement
   * @param conn - Connection
   * @param rs - Result Set
   * @param studentName - name of student
   * @return studentID - id of student
   * @throws SQLException
   */
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


  /**
   * Helper method that deletes the row from the 'enrollments' table based on the
   * unique enroll_id value.
   *
   * @param prep - Prepared Statement
   * @param conn - Connection
   * @param rs - Result Set
   * @param enrollID - unique enrollment ID to be deleted
   * @throws SQLException
   */
  private void removeStudent(PreparedStatement prep, Connection conn, ResultSet rs, Integer enrollID)
      throws SQLException {

    prep = conn.prepareStatement("DELETE FROM enrollments WHERE enroll_id = ?");
    prep.setInt(1, enrollID);
    prep.executeUpdate();
    System.out.println("removing the enrollID: " + enrollID);
  }

  /**
   * Helper method that generates a list of list of strings that represent the waitlist of students
   * for a given course. Uses the 'enrollments' table to create a list of studentIDs and then uses
   * the 'students' table to turn that into a list of student names.
   *
   * @param prep - PreparedStatement
   * @param conn - Conenction
   * @param rs - ResultSet
   * @param classID - classID
   * @return studentWaitlist - list of list of strings with students on waitlist.
   * @throws SQLException
   */
  private List<String> createReturnedWaitlist(PreparedStatement prep, Connection conn, ResultSet rs, Integer classID)
      throws SQLException {
    List<String> studentWaitlist = new ArrayList<String>();
    List<String> studentIDList = new ArrayList<String>();

    prep = conn.prepareStatement("select * from enrollments WHERE class_id = ?");
    prep.setInt(1, classID);
    rs = prep.executeQuery();

    while(rs.next()){
      studentIDList.add(rs.getString(2));
    }
    System.out.println("created a list of relevant student IDs: " + studentIDList);

    prep = conn.prepareStatement("select * from students WHERE student_id = ?");
    for (int i = 0; i < studentIDList.size(); i++){
      prep.setString(1, studentIDList.get(i));
      rs = prep.executeQuery();

      while(rs.next()){
        studentWaitlist.add(rs.getString(2));
      }
    }
    System.out.println("created list of student names to be returned: " + studentWaitlist);
    return studentWaitlist;
  }
}
