package database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 * This class creates and initially populates the SQL database.
 */
public class dbJ {

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
    String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
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
        "INSERT INTO classes VALUES (?, ?, ?, ?, ?);");

    prep.setInt(1, 1);
    prep.setString(2, "CSCI 0130: User Interfaces and User Experience");
    prep.setString(3, "Talie Massachi");
    prep.setString(4, "talie_massachi@brown.edu");
    prep.setString(5, "In this course, students will learn the principles of user experience design, methods for designing and prototyping interfaces, and user interface evaluation.");
    prep.addBatch();

    prep.setInt(1, 2);
    prep.setString(2, "CSCI 0320: Introduction to Software Engineering");
    prep.setString(3, "Tim Nelson");
    prep.setString(4, "timothy_nelson@brown.edu");
    prep.setString(5,"Focuses on designing, building, testing, and maintaining systems collaboratively.");
    prep.addBatch();

    prep.setInt(1, 3);
    prep.setString(2, "CSCI 1280: Intermediate 3D Computer Animation");
    prep.setString(3, "Barbara J Meier");
    prep.setString(4, "barbara_meier@brown.edu");
    prep.setString(5, "Continues work begun in CSCI 1250 with deeper exploration of technical and artistic aspects of 3D computer animation.");
    prep.addBatch();

    prep.setInt(1, 4);
    prep.setString(2, "CSCI 1380: Distributed Computer Systems");
    prep.setString(3, "Nikos Vasilakis");
    prep.setString(4, "nikos_vasilakis@brown.edu");
    prep.setString(5, "Explores the fundamental principles and practice underlying networked information systems.");
    prep.addBatch();

    prep.setInt(1, 5);
    prep.setString(2, "CSCI 1420: Machine Learning");
    prep.setString(3, "Stephen Back");
    prep.setString(4, "stephen_bach@brown.edu");
    prep.setString(5, "We explore the theory and practice of statistical machine learning, focusing on computational methods for supervised and unsupervised learning.");
    prep.addBatch();

    prep.setInt(1, 6);
    prep.setString(2, "CSCI 1440: Algorithmic Game Theory");
    prep.setString(3, "Amy R Greenwald");
    prep.setString(4, "amy_greenwald@brown.edu");
    prep.setString(5, "This course examines topics in game theory and mechanism design from a computer scientist's perspective.");
    prep.addBatch();

    prep.setInt(1, 7);
    prep.setString(2, "CSCI 1660: Introduction to Computer Systems Security");
    prep.setString(3, "Bernardo Palazzi and Nicholas Anthony Frank DeMarinis");
    prep.setString(4, "bernardo_palazzi@brown.edu and nicholas_demarinis@brown.edu");
    prep.setString(5, "This course teaches principles of computer security from an applied viewpoint and provides hands-on experience on security threats and countermeasures.");
    prep.addBatch();

    prep.setInt(1, 8);
    prep.setString(2, "CSCI 1950U: Topics in 3D Game Engine Development");
    prep.setString(3, "Daniel C Ritchie");
    prep.setString(4, "daniel_ritchie@brown.edu");
    prep.setString(5, "Covers core techniques in 3D game development with an emphasis on engine architecture.");
    prep.addBatch();

    prep.setInt(1, 9);
    prep.setString(2, "CSCI 1951A: Data Science");
    prep.setString(3, "Lorenzo De Stefani");
    prep.setString(4, "lorenzo_destefani@brown.edu");
    prep.setString(5, "This course provides an overview of techniques and tools involved and how they work together.");
    prep.addBatch();

    prep.setInt(1, 10);
    prep.setString(2, "CSCI 1951I: CS for Social Change");
    prep.setString(3, "Lachlan Kermode");
    prep.setString(4, "lachlan_kermode@brown.edu");
    prep.setString(5, "Students will be placed in small teams to collaboratively work on projects that will range from developing a chatbot to aid community engagement to conducting geospatial data analytics.");
    prep.addBatch();

    prep.setInt(1, 11);
    prep.setString(2, "CSCI 1952Q: Algorithmic Aspects of Machine Learning");
    prep.setString(3, "Yu Cheng");
    prep.setString(4, "yu_cheng@brown.edu");
    prep.setString(5, "In this course, we will explore the theoretical foundations of machine learning and deep learning.");
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
    prep.setString(3, "tabitha_lynn@brown.edu");
    prep.addBatch();

    prep.setInt(1, 2);
    prep.setString(2, "Calvin Eng");
    prep.setString(3, "calvin_eng@brown.edu");
    prep.addBatch();

    prep.setInt(1, 3);
    prep.setString(2, "Shravya Sompalli");
    prep.setString(3, "shravya_sompalli@brown.edu");
    prep.addBatch();

    prep.setInt(1, 4);
    prep.setString(2, "Aanya Hudda");
    prep.setString(3, "aanya_hudda@brown.edu");
    prep.addBatch();

    prep.setInt(1, 5);
    prep.setString(2, "Tanya Qu");
    prep.setString(3, "tanya_qu@brown.edu");
    prep.addBatch();

    prep.setInt(1, 6);
    prep.setString(2, "Klara Davidson-Schmich");
    prep.setString(3, "klara_davidson-schmich@brown.edu");
    prep.addBatch();

    prep.setInt(1, 7);
    prep.setString(2, "Cole Griscom");
    prep.setString(3, "cole_griscom@brown.edu");
    prep.addBatch();

    prep.setInt(1, 8);
    prep.setString(2, "Grace Samaha");
    prep.setString(3, "grace_samaha@brown.edu");
    prep.addBatch();

    prep.setInt(1, 9);
    prep.setString(2, "Jennifer Tran");
    prep.setString(3, "jennifer_tran1@brown.edu");
    prep.addBatch();

    prep.setInt(1, 10);
    prep.setString(2, "Alissa Simon");
    prep.setString(3, "alissa_simon@brown.edu");
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
    prep.setInt(3, 1);
    prep.addBatch();

    prep.setInt(1, 301);
    prep.setInt(2, 2);
    prep.setInt(3, 2);
    prep.addBatch();

    prep.setInt(1, 302);
    prep.setInt(2, 3);
    prep.setInt(3, 3);
    prep.addBatch();
    prep.executeBatch();
  }
}
