package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

// Query Parameters: className
// Gets the class_id from 'classes' table for courseName
// Create list of student_ids paired with the class_id in 'enrollments' table
// Create a hashmap (key = courseID, value = number of times it shows up)
// Iterate through each student_id in list and update hashmap accordingly
// Get second highest key/value pair from hashmap
// Get and return course information for that course

public class RecommendHandler implements Route{
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();
    System.out.println("recommend handler called");

    // If the query parameters are valid ...
    if (qm.hasKey("className")){
      String className = qm.value("className");
      return handleTables(className);
    } else {
      return "failure with the provided query parameters";
    }
  }

  public List<String> handleTables(String className){
    List<String> courseInformation = new ArrayList<String>();

    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep = null;
      ResultSet rs = null;

      // Get class_id from "classes" table
      Integer classID = this.getClassID(prep, conn, rs, className);
      System.out.println("the class " + className + " has classID " + classID);

      // Get that course's waitlist
      List<Integer> studentIDList = this.createStudentIDList(prep, conn, rs, classID);
      System.out.println("created a list of relevant student IDs: " + studentIDList);

      // Create hashmap mapping course name to number of times it appears
      HashMap<Integer, Integer> courseValues = new HashMap<Integer, Integer>();

      // for each student_id in studentIDList
      for (int i = 0; i < studentIDList.size(); i ++){
        // get all courses corresponding to that student ID and add/update map
        prep = conn.prepareStatement("select * from enrollments WHERE student_id = ?");
        prep.setInt(1, studentIDList.get(i));
        rs = prep.executeQuery();

        // for each class the current student is on the waitlist for ...
        while(rs.next()){
          Integer currClassID = rs.getInt(3);
          // if course already in hashmap then update the count
          if (courseValues.containsKey(currClassID)){
            Integer newValue = courseValues.get(currClassID) + 1;
            courseValues.replace(currClassID, newValue);
          // else, if course not in hashmap then put it in
          } else {
            courseValues.put(currClassID, 1);
          }
        }
      }

      // The key/value pair with the maximum value will be couresName so remove it
      if(courseValues.containsKey(classID)){
        courseValues.remove(classID);
      }

      // Find highest key/value pair in hashmap
      Map.Entry<Integer, Integer> max = null;
      for (Map.Entry<Integer, Integer> entry : courseValues.entrySet()) {
        if (max == null || entry.getValue().compareTo(max.getValue()) > 0) {
          max = entry;
        }
      }

      System.out.println("max1: " + max);

      // max2 is the class_ID with the highest number of occurences in other student waitlists
      courseInformation = this.getCourseInfo(prep, conn, rs, max.getValue());
      System.out.println(courseInformation);

    } catch (SQLException e){
      System.out.println("caught this exception: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception: " + f);
    }

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

  private List<Integer> createStudentIDList(PreparedStatement prep, Connection conn, ResultSet rs, Integer classID)
      throws SQLException {
    List<Integer> studentIDList = new ArrayList<Integer>();

    prep = conn.prepareStatement("select * from enrollments WHERE class_id = ?");
    prep.setInt(1, classID);
    rs = prep.executeQuery();

    while(rs.next()){
      studentIDList.add(rs.getInt(2));
    }
    return studentIDList;
  }

  private List<String> getCourseInfo(PreparedStatement prep, Connection conn, ResultSet rs, Integer classID)
      throws SQLException {
    List<String> courseInformation = new ArrayList<String>();

    prep = conn.prepareStatement("select * from classes WHERE class_id = ?");
    prep.setInt(1, classID);
    rs = prep.executeQuery();

    while(rs.next()){
      String title = rs.getString(2);
      String instructor = rs.getString(3);
      String instructorEmail = rs.getString(4);
      String description = rs.getString(5);

      courseInformation.add(title);
      courseInformation.add(instructor);
      courseInformation.add(description);
      courseInformation.add(instructorEmail);
    }
    return courseInformation;
  }
}
