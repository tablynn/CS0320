package server;

import java.util.ArrayList;
import java.util.List;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetCoursesHandler implements Route{

  //Query Parameters: class name

  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap qm = request.queryMap();

    // If the query parameters are valid ...
    if (qm.hasKey("className")){
      String className = qm.value("className");

      return handleTables(className);

    } else {
      return "failure with the provided query parameters";
    }
  }

  public List<List<String>> handleTables(String className){
    List<List<String>> courseInformation = new ArrayList<List<String>>();


    return courseInformation;

    // Get the class id from the "classes" table using the class name
    // Return: course information in a List<List<String>>
  }

}
