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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AddStudentHandler;
import server.GetCoursesHandler;
import server.RecommendHandler;
import server.RemoveStudentHandler;
import spark.Spark;

public class IntegrationTest {


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

  @Test
  public void testIntegrationGetCourseData() throws IOException {
    Spark.get("getCourseData", new GetCoursesHandler());
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection clientConnection = tryRequest("getCourseData");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    List<List<String>> responses =
        moshi.adapter(List.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    List<String> cs32 = new ArrayList<String>(Arrays.asList("CSCI 0320: Introduction to "
        + "Software Engineering","Tim Nelson","tn@cs.brown.edu","Focuses on designing, building, "
        + "testing, and maintaining systems collaboratively."));
    List<String> cs33 = new ArrayList<String>(Arrays.asList("CSCI 0330: Fundamentals of "
        + "Computer Systems","Malte Schwarzkopf","malte_schwarzkopf@brown.edu","Covers fundamental "
        + "concepts, principles, and abstractions that underlie the design and engineering of computer systems."));
    List<String> cs19 = new ArrayList<String>(Arrays.asList("CSCI 0190: Accelerated Introduction to "
        + "CS","Tim Nelson","tn@cs.brown.edu","A one-semester introduction to CS covering "
        + "programming integrated with core data structures, algorithms, and analysis techniques."));
    List<String> cs1470 = new ArrayList<String>(Arrays.asList("CSCI 1470: Deep Learning ","Ritambhara"
        + " Singh","ritambhara_singh@brown.edu","In this course, you will get an overview of the "
        + "prominent techniques of deep learning and their applications."));
    List<List<String>> actual = new ArrayList<>(Arrays.asList(cs32,cs33,cs19,cs1470));
    assertEquals(actual, responses);

    Spark.unmap("/getCourseData");
    Spark.stop();
    Spark.awaitStop();
  }

  @Test
  public void testIntegrationAddStudent() throws IOException, ClassNotFoundException, SQLException {
    Spark.get("addStudent", new AddStudentHandler());
    Spark.init();
    Spark.awaitInitialization();
    AddStudentHandler addStudentHandler = new AddStudentHandler();


    HttpURLConnection clientConnection = tryRequest("addStudent?studentName=Tanya%20Qu&"
        + "email=tanya_qu@brown.edu&className=CSCI%201470:%20Deep%20Learning");
    assertEquals(200, clientConnection.getResponseCode());

    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
    Connection conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    // Tell the database to enforce foreign keys
    stat.executeUpdate("PRAGMA foreign_keys=ON;");

    PreparedStatement prep;
    ResultSet rs = null;

    prep = conn.prepareStatement("select * from students WHERE email = ?");
    prep.setString(1, "tanya_qu@brown.edu");
    rs = prep.executeQuery();
    Boolean studentExistsInStudents = false;
    Integer studentID = 0;

    while(rs.next()){
      studentExistsInStudents = true;
      studentID = rs.getInt(1);
    }
    assertTrue(addStudentHandler.checkIfStudentExistsInEnrollments(prep,conn, rs,studentID,4));

  }


  @Test
  public void testIntegrationRemoveStudent() throws IOException, ClassNotFoundException, SQLException {
    Spark.get("removeStudent", new RemoveStudentHandler());
    Spark.init();
    Spark.awaitInitialization();
    AddStudentHandler addStudentHandler = new AddStudentHandler();


    HttpURLConnection clientConnection = tryRequest("removeStudent?studentName=Tanya%20Qu&"
        + "className=CSCI%201470:%20Deep%20Learning");
    assertEquals(200, clientConnection.getResponseCode());

    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
    Connection conn = DriverManager.getConnection(urlToDB);
    Statement stat = conn.createStatement();
    // Tell the database to enforce foreign keys
    stat.executeUpdate("PRAGMA foreign_keys=ON;");

    PreparedStatement prep;
    ResultSet rs = null;

    prep = conn.prepareStatement("select * from students WHERE email = ?");
    prep.setString(1, "tanya_qu@brown.edu");
    rs = prep.executeQuery();
    Boolean studentExistsInStudents = false;
    Integer studentID = 0;

    while(rs.next()){
      studentExistsInStudents = true;
      studentID = rs.getInt(1);
    }
    assertFalse(addStudentHandler.checkIfStudentExistsInEnrollments(prep,conn, rs,studentID,4));

  }

  @Test
  public void testIntegrationRecommend(){
  }

  /**
   * remaining testing:
   * attempting to add a duplicate
   * recommend
   * get course waitlist test
   */

}
