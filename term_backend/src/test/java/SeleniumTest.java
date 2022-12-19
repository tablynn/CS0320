import static org.junit.Assert.assertEquals;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.time.Duration;
import java.util.List;
import server.AddStudentHandler;
import server.GetCourseWaitlistHandler;
import server.GetCoursesHandler;
import server.RecommendHandler;
import server.RemoveStudentHandler;
import spark.Spark;

public class SeleniumTest {
  private RemoteWebDriver driver = null;
  private String indexPath = "http://localhost:3000";

  /** Set spark port to 0 before test suite is run */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /**
   * Set up for a new test
   */
  @BeforeEach
  public void setup() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();

    // Headless = no UI displayed; good for CI
    options.addArguments("--headless");
    this.driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

    // Restarts the entire Spark server for every test
    Spark.get("getCourseData", new GetCoursesHandler());
    Spark.get("getCourseWaitlist", new GetCourseWaitlistHandler());
    Spark.get("addStudent", new AddStudentHandler());
    Spark.get("removeStudent", new RemoveStudentHandler());
    Spark.get("recommendCourse", new RecommendHandler());
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /**
   * Remove references created by a test, reset to old state, etc.
   */
  @After
  public void tearDown() {
    driver.quit();

    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/getCourseData");
    Spark.unmap("/getCourseWaitlist");
    Spark.unmap("/addStudent");
    Spark.unmap("/removeStudent");
    Spark.unmap("/recommendCourse");
    Spark.stop();
    Spark.awaitStop();
  }

  @Test
  public void testCorrect() {
    driver.get(indexPath);
    assertEquals("Can You Guess The Sequence?", driver.getTitle());
  }
}
