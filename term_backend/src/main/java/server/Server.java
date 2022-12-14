package server;

import static spark.Spark.after;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the
 * various handlers.
 */
public class Server {

  /**
   * Main method starts Spark and runs the various handlers for each endpoint.
   *
   * @param args
   */
  public static void main(String[] args) {
    Spark.port(3231);

    after((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "*");
    });

    // Setting up the various endpoints for the API Server
    Spark.get("getCourseData", new GetCoursesHandler());
    Spark.get("getCourseWaitlist", new GetCourseWaitlistHandler());
    Spark.get("addStudent", new AddStudentHandler());
    Spark.get("removeStudent", new RemoveStudentHandler());
    Spark.get("recommendCourse", new RecommendHandler());
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}