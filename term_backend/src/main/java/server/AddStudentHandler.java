package server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

//Query Parameters: student name, student email, class_id

public class AddStudentHandler implements Route{
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    // If the query parameters are valid ...
    if (qm.hasKey("studentName") && qm.hasKey("email") && qm.hasKey("className")){
      String studentName = qm.value("studentName");
      String studentEmail = qm.value("email");
      String className = qm.value("className");

      return handleTables(studentName, studentEmail, className);

    } else {
      return "failure with the provided query parameters";
    }
  }

  public List<String> handleTables(String studentName, String studentEmail, String className){
    List<String> studentWaitlist = new ArrayList<String>();
    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep;
      ResultSet rs;

      // Check if student already exists in "students" table
      prep = conn.prepareStatement("select * from students WHERE name = ?");
      prep.setString(1, studentName);
      rs = prep.executeQuery();
      Boolean studentExistsInStudents = false;
      Integer studentID = 0;

      while(rs.next()){
        studentExistsInStudents = true;
        studentID = rs.getInt(1);
      }

      // If student doesn't exist in "students" table, add student to it
      if(studentExistsInStudents == false){
        System.out.println("student " + studentName + " does not exist in 'students' table");
        studentID = this.addToStudents(prep, conn, rs, studentName, studentEmail);
        System.out.println("student " + studentName + " was added to 'students' table at " + studentID);
      } else {
        System.out.println("student " + studentName + " already exists in 'students table");
      }

      // Get class_id from "classes" table
      Integer classID = this.getClassID(prep, conn, rs, className);
      System.out.println("the class " + className + " has classID " + classID);

      // Check if student exists in "enrollments" table with the correct course
      Boolean studentExistsInEnrollmentsWithCorrectClass = this.checkIfStudentExistsInEnrollments(prep, conn, rs, studentID, classID);

      // If student doesn't exist in "enrollments" with this specific course then add them
      if(studentExistsInEnrollmentsWithCorrectClass == false){
        System.out.println("student "+ studentName + " does not exist correctly with class " + className + " in 'enrollments'");
        this.addToEnrollments(prep, conn, rs, studentID, classID);
      } else {
        System.out.println("student already exists in 'enrollments' table with the correct course");
      }

      // Return: list of names of students in waitlist in order for that specific course
      studentWaitlist = this.createReturnedWaitlist(prep, conn, rs, classID);

    } catch (SQLException e){
      System.out.println("caught this exception: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception: " + f);
    }
    return studentWaitlist;
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

  private Integer addToStudents(PreparedStatement prep, Connection conn, ResultSet rs, String studentName, String studentEmail)
      throws SQLException {
    Integer studentID = 0;
    // Find max student_id
    prep = conn.prepareStatement("SELECT MAX(student_id) from students");
    rs = prep.executeQuery();
    while(rs.next()){
      studentID = rs.getInt(1) + 1;
    }

    // Insert new student into "students" table
    prep = conn.prepareStatement("INSERT INTO students VALUES (?, ?, ?, ?);");
    prep.setInt(1, studentID);
    prep.setString(2, studentName);
    prep.setString(3, studentEmail);
    prep.addBatch();
    prep.executeBatch();

    return studentID;
  }

  private void addToEnrollments(PreparedStatement prep, Connection conn, ResultSet rs, Integer studentID, Integer classID)
      throws SQLException {
    Integer enrollID = 0;

    // Find max enrollment_id
    prep = conn.prepareStatement("SELECT MAX(enroll_id) from enrollments");
    rs = prep.executeQuery();
    while(rs.next()){
      enrollID = rs.getInt(1);
      enrollID = enrollID + 1;
    }

    // Insert new student/course pair into 'enrollments' table
        prep = conn.prepareStatement("INSERT INTO enrollments VALUES (?, ?, ?);");
        prep.setInt(1, enrollID);
        prep.setInt(2, studentID);
        prep.setInt(3, classID);
        prep.addBatch();
        prep.executeBatch();

    System.out.println("studentID " + studentID + " and classID " + classID + " were added to 'enrollments' at enrollID " + enrollID);
  }

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
