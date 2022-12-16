import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
import server.RemoveStudentHandler;
import spark.Spark;


/**
 * Testing class for GetHandler, LoadHandler and the "loadCSV" and "getCSV" spark requests.
 */
public class BackendTest {

  /**
   * Starts the server before all tests
   */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /**
   * Before each test is run, clears shared state this.csvData and sets up Spark.
   */
  @BeforeEach
  public void setup(){
    // Restart spark
    //Spark.get("getData", new GetDataHandler();
    Spark.init();
    Spark.awaitInitialization();
  }

  /**
   * After each test is run, resets Spark.
   */
  @AfterEach
  public void teardown(){
    // Stops spark form listening on both endpoints
    // Spark.unmap("getData");
    Spark.awaitStop();
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   * @param apiCall the call string, including endpoint
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  static private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Tests that an invalid API call gives an API error.
   */
  @Test
  public void testInvalidAPICall() throws IOException{
    HttpURLConnection clientConnection = tryRequest("hi");
    // Don't get an OK response - API provides an error message
    assertEquals(404, clientConnection.getResponseCode());
  }

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




}



  /**
   * List of tests
   * Test that call of addStudent endpoint successfully adds to database and returns new list
   * with new student
   *
   * Test that call of removeStudent scuccessfully removes and returns new list
   *
   * test that you can't add urself twice
   *
   *
   */