import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import okio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.AddStudentHandler;
import server.GetCourseWaitlistHandler;
import server.GetCoursesHandler;
import server.RecommendHandler;
import server.RemoveStudentHandler;
import spark.Spark;


/**
 *
 */
public class UnitTest {

  /**
   * Tests successful addition to a waitlist.
   * @throws IOException
   */
  @Test
  public void testSuccessfulAddition() {
    AddStudentHandler addStudentHandler = new AddStudentHandler();
    List<String> result = addStudentHandler.handleTables("Alissa Simon","alissa_simon@brown.edu","CSCI 1470: Deep Learning","mock.sqlite3");
    List<String> expected = Arrays.asList("Tabitha Lynn", "Alissa Simon");
    assertEquals(expected, result);
  }

  /**
   * Tests unsuccessful addition to a waitlist, because the student is already added.
   * @throws IOException
   */
  @Test
  public void testRepetitiveAddition() {
    AddStudentHandler addStudentHandler = new AddStudentHandler();
    List<String> result = addStudentHandler.handleTables("Tabitha Lynn","Tabitha_lynn@brown.edu","CSCI 1470: Deep Learning","mock.sqlite3");
    List<String> expected = Arrays.asList("Tabitha Lynn", "Alissa Simon");
    assertEquals(expected, result);
  }

  /**
   * Tests successful removal to a waitlist.
   * @throws IOException
   */
  @Test
  public void testSuccessfulRemoval() {
    RemoveStudentHandler removeStudentHandler = new RemoveStudentHandler();
    List<String> result = removeStudentHandler.handleTables("Alissa Simon","CSCI 1470: Deep Learning","mock.sqlite3");
    List<String> expected = Arrays.asList("Tabitha Lynn");
    assertEquals(expected, result);
  }

  /**
   * Tests unsuccessful removal from a waitlist (person doesn't exist to be removed)
   * @throws IOException
   */
  @Test
  public void testUnsuccessfulRemoval() {
    RemoveStudentHandler removeStudentHandler = new RemoveStudentHandler();
    List<String> result = removeStudentHandler.handleTables("Alissa Simon","CSCI 1470: Deep Learning","mock.sqlite3");
    List<String> expected = Arrays.asList("Tabitha Lynn");
    assertEquals(expected, result);
  }

  @Test
  public void testSuccessfulGetCourseWaitlist(){
    GetCourseWaitlistHandler getCourseWaitlistHandler = new GetCourseWaitlistHandler();
    Moshi moshi = new Moshi.Builder().build();
    //returns serialized list
    Object responses = getCourseWaitlistHandler.handleTables("CSCI 1470:"
        + " Deep Learning","mock.sqlite3");
    List<String> tabs = new ArrayList<>(Arrays.asList("Tabitha Lynn","Tabitha_lynn@brown.edu"));
    List<List<String>> expectString = new ArrayList<>(Arrays.asList(tabs));
    String expected =
        moshi.adapter(List.class).toJson(expectString);
    assertEquals(expected, responses);
  }

  //NO POSSIBLE RECOMMEDATIONS TEST
  @Test
  public void testRecommendHandler(){
    RecommendHandler recommendHandler = new RecommendHandler();
    Object responses = recommendHandler.handleTables("CSCI 1470:"
        + " Deep Learning","mock.sqlite3");
    List<String> courseInfo = Arrays.asList("No recommendation could be provided because either no other students "
        + "are on the waitlist for this course or the other students are solely on the waitlist for this course");
    Moshi moshi = new Moshi.Builder().build();
    String expected =
        moshi.adapter(List.class).toJson(courseInfo);
    assertEquals(expected, responses);

  }

}
