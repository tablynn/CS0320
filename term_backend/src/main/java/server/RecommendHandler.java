package server;

import com.squareup.moshi.Moshi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

// Query Parameters: className
// http://localhost:3231/recommendCourse?className=CSCI%200320:%20Introduction%20to%20Software%20Engineering

/**
 * This class uses the name of the current class (className) to generate a class recommendation and
 * returns the course information for that recommended class. The recommendation is generated by
 * retrieving all the students enrolled in the class (className), getting all the other classes
 * those students are on the waitlist for, and counting how often those other classes occur (by
 * populating a hashmap). The next highest enrollment for a class other than the current class is
 * returned as a recommendation
 */
public class RecommendHandler implements Route{

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
    System.out.println("recommend handler called");

    // If query parameter is valid run helper handleTables
    if (qm.hasKey("className")){
      String className = qm.value("className");
      return handleTables(className, "waitlist.sqlite3");
    } else {
      return "failure with the provided query parameters";
    }
  }

  /**
   * Helper method that connects to the database, gets the course's waitlist, figures out
   * how often students on the waitlist are enrolled in other courses, and returns the
   * information about a recommended course in the form of list of strings.
   *
   * @param className - name of class
   * @param dbName - name of sql file connection is established to
   * @return courseInformation - information for recommended class
   */
  public String handleTables(String className, String dbName){
    List<String> courseInformation = new ArrayList<String>();

    try {
      // Load the driver and establish a connection to the database
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + dbName;
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      // Tell the database to enforce foreign keys
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep = null;
      ResultSet rs = null;

      // Get class_id from "classes" table
      Integer classID = this.getClassID(prep, conn, rs, className);
      System.out.println("the class " + className + " has classID " + classID);

      // Get that course's waitlist
      List<Integer> studentIDList = this.createStudentIDList(prep, conn, rs, classID);
      System.out.println("created a list of relevant student IDs: " + studentIDList);

      // Create & populate hashmap mapping course name to number of times it appears
      HashMap<Integer, Integer> courseValues = new HashMap<Integer, Integer>();
      this.populateHashmap(prep, conn, rs, studentIDList, courseValues);

      // The key/value pair with the maximum value will be the current class so remove it
      if(courseValues.containsKey(classID)){
        courseValues.remove(classID);
      }

      System.out.println("courseValues hashmap: " + courseValues);
      System.out.println("courseValues keys: " + courseValues.keySet());
      System.out.println("courseValues values: " + courseValues.values());

      // If there are no other possible courses to recommend, provide informative statement
      if(courseValues.keySet().isEmpty()){
        courseInformation.add("No recommendation could be provided for this course.");
      } else {
        // Find the classID that the most students are also enrolled in (recommendedClassID)
        Integer recommendedClassID = this.getHighestValCourse(courseValues);

        courseInformation = this.getCourseInfo(prep, conn, rs, recommendedClassID);
        System.out.println(courseInformation);
      }

    } catch (SQLException e){
      System.out.println("caught this exception in RecommendHandler: " + e);
    } catch (ClassNotFoundException f){
      System.out.println("caught this exception in RecommendHandler: " + f);
    }

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

  /**
   * Helper method that uses the 'enrollments' table to create a list of studentIDs on the
   * waitlist of a particular course (classID).
   * @param prep - Prepared Statement
   * @param conn - Connection
   * @param rs - Result Set
   * @param classID - classID whose waitlist we want to get
   * @return studentIDList - waitlist for course (list of studentIDs)
   * @throws SQLException
   */
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

  //helper method that populates Hashmap of counts of students in other classes

  /**
   * Helper method populates Hashmap that maps the classID to the number of times it
   * comes up.
   *
   * @param prep - Prepared Statement
   * @param conn - Connection
   * @param rs - Result Set
   * @param studentIDList - list of studentIDs on course's waitlist
   * @param courseValues - hashmap mapping courseID -> relevance
   * @throws SQLException
   */
  private void populateHashmap(PreparedStatement prep, Connection conn, ResultSet rs, List<Integer> studentIDList, HashMap<Integer, Integer> courseValues)
      throws SQLException {
    // For each student_id in studentIDList
    for (int i = 0; i < studentIDList.size(); i ++){
      // Get all courses corresponding to that student ID and add/update map
      prep = conn.prepareStatement("select * from enrollments WHERE student_id = ?");
      prep.setInt(1, studentIDList.get(i));
      rs = prep.executeQuery();

      // For each class the current student is also on the waitlist for ...
      while(rs.next()){
        Integer currClassID = rs.getInt(3);
        // If course already in hashmap then update the count
        if (courseValues.containsKey(currClassID)){
          Integer newValue = courseValues.get(currClassID) + 1;
          courseValues.replace(currClassID, newValue);
          // Else put it in
        } else {
          courseValues.put(currClassID, 1);
        }
      }
    }
  }

  /**
   * Helper method that obtains the course with the highest count of other matching student
   * enrollments. Iterates through Hashmap, assigning the highest value to max until it
   * has iterated through the whole map.
   *
   * @param courseValues - hashmap mapping courseID -> relevance
   * @return
   */
  private Integer getHighestValCourse(HashMap<Integer, Integer> courseValues){
    Map.Entry<Integer, Integer> max = null;
    for (Map.Entry<Integer, Integer> entry : courseValues.entrySet()) {
      if (max == null || entry.getValue().compareTo(max.getValue()) > 0) {
        max = entry;
      }
    }
    System.out.println("the value being returned by getHighestValCourse is: " + max.getKey());
    return max.getKey();
  }


  /**
   * Helper method that gets all relevant course information for recommended course.
   *
   * @param prep - Prepared Statement
   * @param conn - Connection
   * @param rs - Result Set
   * @param classID - recommended course class ID
   * @return courseInformation - list of strings containing recommended course information
   * @throws SQLException
   */
  private List<String> getCourseInfo(PreparedStatement prep, Connection conn, ResultSet rs, Integer classID)
      throws SQLException {
    List<String> courseInformation = new ArrayList<String>();

    prep = conn.prepareStatement("select * from classes WHERE class_id = ?");
    prep.setInt(1, classID);
    rs = prep.executeQuery();

    while(rs.next()){
      courseInformation.add(rs.getString(2)); // add title
      courseInformation.add(rs.getString(3)); // add instructor
      courseInformation.add(rs.getString(5)); // add description
      courseInformation.add(rs.getString(4)); // add instructorEmail
    }
    return courseInformation;
  }
}
