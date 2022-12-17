import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class testdbJ {


    /**
     * Main method that connects to the SQL database, creates the three tables,
     * and initially populates them.
     *
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
      // These lines load the driver class and establish a connection to the database
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + "mock.sqlite3";
      Connection conn = DriverManager.getConnection(urlToDB);
      // These two lines establish that foreign keys will be enforced in the database
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");

      //Create a table of classes that has attributes for a class_id, title, name, email, and description
      //The class ID is a unique integer assigned to a class in ascending order and serves as primary key
      PreparedStatement prep;
      prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS classes("
          + "class_id INTEGER,"
          + "title TEXT,"
          + "instructorName TEXT,"
          + "instructorEmail TEXT,"
          + "description TEXT,"
          + "PRIMARY KEY (class_id))");
      prep.executeUpdate();

      //Add to the classes table data for 4 rows
      prep = conn.prepareStatement(
          "INSERT INTO classes VALUES (?, ?, ?,?,?);");
      prep.setInt(1, 4);
      prep.setString(2, "CSCI 1470: Deep Learning");
      prep.setString(3, "Ritambhara Singh");
      prep.setString(4, "ritambhara_singh@brown.edu");
      prep.setString(5,"In this course, you will get an overview of the prominent techniques of deep learning and their applications.");
      prep.addBatch();

      prep.executeBatch();

      //Create a table students that has attributes student_id, name, email, and primary key
      //The student ID is a unique integer assigned in ascending order and serves as primary key
      prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS students("
          + "student_id INTEGER,"
          + "name TEXT,"
          + "email TEXT,"
          + "PRIMARY KEY (student_id))");
      prep.executeUpdate();

      //Insert into the students table vaues for 5 rows
      prep = conn.prepareStatement(
          "INSERT INTO students VALUES (?, ?, ?);");
      prep.setInt(1, 1);
      prep.setString(2, "Tabitha Lynn");
      prep.setString(3, "Tabitha_lynn@brown.edu");
      prep.addBatch();


      prep.executeBatch();


      //Create a table enrollments that has attributes enroll_id, student_id, class_id
      //The student_id and class_id correspond to the IDs in the students and classes table
      //The IDs are set as foreign keys, meaning that any changes made to the keys such as deletion
      //will be reflected in the students and classes tables
      //The enroll_id is a unique ID that identifies each new enrollment of a student in a class waitlist
      prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS enrollments("
          + "enroll_id INTEGER,"
          + "student_id INTEGER,"
          + "class_id INTEGER,"
          + "PRIMARY KEY (enroll_id),"
          + "FOREIGN KEY (student_id) REFERENCES students(student_id)"
          + "ON DELETE CASCADE ON UPDATE CASCADE,"
          + "FOREIGN KEY (class_id) REFERENCES classes(class_id)"
          + "ON DELETE CASCADE ON UPDATE CASCADE);");
      prep.executeUpdate();

      //Insert into the enrollments table values for three rows
      prep = conn.prepareStatement(
          "INSERT INTO enrollments VALUES (?, ?, ?);");

      prep.setInt(1, 300);
      prep.setInt(2, 1);
      prep.setInt(3, 4);
      prep.addBatch();
      prep.executeBatch();
    }
  }

