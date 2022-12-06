package server;

import java.util.ArrayList;
import java.util.List;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

//Query Parameters: student name, student email, student year, class_id

public class AddStudentHandler implements Route{
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    // If the query parameters are valid ...
    if (qm.hasKey("studentName") && qm.hasKey("email") && qm.hasKey("year") && qm.hasKey("className")){
      String studentName = qm.value("studentName");
      String studentEmail = qm.value("email");
      String studentYear = qm.value("year");
      String className = qm.value("className");

      return handleTables(studentName, studentEmail, studentYear, className);

    } else {
      return "failure with the provided query parameters";
    }
  }

  public List<String> handleTables(String studentName, String studentEmail, String studentYear, String className){
    List<String> studentWaitlist = new ArrayList<String>();

    return studentWaitlist;


    // If the student doesn't exist in "students" table, add the student there
    // Get the class id from the "classes" table using the class name
    // If the student's ID doesn't exist in the "enrollments" table, add the student there with the class_ID
    // Return: list of names of students in waitlist in order for that specific course


  }

}
