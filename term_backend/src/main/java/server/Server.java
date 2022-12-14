package server;

import static spark.Spark.after;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the
 * various handlers.
 */
public class Server {

  /**
   * Main method.
   *
   * @param args - arguments provided to main method
   */
  public static void main(String[] args) {
    Spark.port(3231);

    after((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "*");
    });

    // Setting up the handler for GET and the endpoints
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