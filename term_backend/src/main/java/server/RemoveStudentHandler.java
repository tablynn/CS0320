package server;

import java.util.ArrayList;
import java.util.List;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

//Query Parameters: student name, class name

public class RemoveStudentHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    // If the query parameters are valid ...
    if (qm.hasKey("studentName") && qm.hasKey("className")){
      String studentName = qm.value("studentName");
      String class_id = qm.value("className");

      return handleTables(studentName, class_id);

    } else {
      return "failure with the provided query parameters";
    }
  }

  public List<String> handleTables(String studentName, String class_id){
    List<String> studentWaitlist = new ArrayList<String>();

    return studentWaitlist;

    // Search for student name in "students" table and get the student ID
    // Search for course name in "classes" table and get the class ID
    // Remove the student ID and respective class ID from the "enrollments" table
  }



}
