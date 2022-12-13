package database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class dbJ {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    // This line loads the driver manager class, and must be
    // present for everything else to work properly
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + "waitlist.sqlite3";
    Connection conn = DriverManager.getConnection(urlToDB);
    // These two lines tell the database to enforce foreign keys during operations, and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");

    PreparedStatement prep;

    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS classes("
        + "class_id INTEGER,"
        + "title TEXT,"
        + "instructorName TEXT,"
        + "instructorEmail TEXT,"
        + "description TEXT,"
        + "PRIMARY KEY (class_id))");
    //+ "FOREIGN KEY (instructorName) REFERENCES instructor(instr_id)"
    //+ "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();




    prep = conn.prepareStatement(
        "INSERT INTO classes VALUES (?, ?, ?,?,?);");
    prep.setInt(1, 1);
    prep.setString(2, "CSCI 0320: Introduction to Software Engineering");
    prep.setString(3, "Tim Nelson");
    prep.setString(4, "tn@cs.brown.edu");
    prep.setString(5,"Focuses on designing, building, testing, and maintaining systems collaboratively.");
    prep.addBatch();

    prep.setInt(1, 2);
    prep.setString(2, "CSCI 0330: Fundamentals of Computer Systems");
    prep.setString(3, "Malte Schwarzkopf");
    prep.setString(4, "malte_schwarzkopf@brown.edu");
    prep.setString(5,"Covers fundamental concepts, principles, and abstractions that underlie the design and engineering of computer systems.");
    prep.addBatch();

    prep.setInt(1, 3);
    prep.setString(2, "CSCI 0190: Accelerated Introduction to CS");
    prep.setString(3, "Tim Nelson");
    prep.setString(4, "tn@cs.brown.edu");
    prep.setString(5,"A one-semester introduction to CS covering programming integrated with core data structures, algorithms, and analysis techniques.");
    prep.addBatch();

    prep.setInt(1, 4);
    prep.setString(2, "CSCI 1470: Deep Learning");
    prep.setString(3, "Ritambhara Singh");
    prep.setString(4, "ritambhara_singh@brown.edu");
    prep.setString(5,"In this course, you will get an overview of the prominent techniques of deep learning and their applications.");
    prep.addBatch();

    prep.executeBatch();


    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS students("
        + "student_id INTEGER,"
        + "name TEXT,"
        + "email TEXT,"
        + "PRIMARY KEY (student_id))");
    //+ "FOREIGN KEY (instructorName) REFERENCES instructor(instr_id)"
    //+ "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();




    prep = conn.prepareStatement(
        "INSERT INTO students VALUES (?, ?, ?);");

    prep.setInt(1, 1);
    prep.setString(2, "Tabitha Lynn");
    prep.setString(3, "tlynn1@brown.edu");
    //prep.setString(4, "Sophomore");
    prep.addBatch();

    prep.setInt(1, 2);
    prep.setString(2, "Calvin Eng");
    prep.setString(3, "ceng4@brown.edu");
   // prep.setString(4, "Sophomore");
    prep.addBatch();

    prep.setInt(1, 3);
    prep.setString(2, "Shravya Sompalli");
    prep.setString(3, "shravya_sompalli@brown.edu");
    //prep.setString(4, "Sophomore");
    prep.addBatch();

    prep.setInt(1, 4);
    prep.setString(2, "Aanya Hudda");
    prep.setString(3, "ahudda1@brown.edu");
   //prep.setString(4, "Sophomore");
    prep.addBatch();

    prep.executeBatch();





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




/**

    PreparedStatement instructorPrep;
    instructorPrep = conn.prepareStatement("DROP TABLE instructor");
    instructorPrep.executeUpdate();
    instructorPrep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS instructor("
        + "instr_id INTEGER,"
        + "name TEXT,"
        + "PRIMARY KEY (instr_id))");
    instructorPrep.executeUpdate();

    instructorPrep = conn.prepareStatement(
        "INSERT INTO instructor VALUES (?, ?);");
    instructorPrep.setInt(1, 1);
    instructorPrep.setString(2, "Tim Nelson");
    instructorPrep.addBatch();

    instructorPrep.setInt(1, 2);
    instructorPrep.setString(2, "Matte Schwarzkpof");
    instructorPrep.addBatch();

    instructorPrep.setInt(1, 3);
    instructorPrep.setString(2, "Tim");
    instructorPrep.addBatch();

    instructorPrep.executeBatch();



    //PreparedStatement prep;
    prep = conn.prepareStatement("DROP TABLE cs_classes");
    prep.executeUpdate();
    //prep = conn.prepareStatement("DROP TABLE instructor");
    //prep.executeUpdate();
;    prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS cs_classes("
        + "number INTEGER,"
        + "title TEXT,"
        + "semester TEXT,"
        + "instructorName TEXT,"
        + "PRIMARY KEY (number))");
        //+ "FOREIGN KEY (instructorName) REFERENCES instructor(instr_id)"
        //+ "ON DELETE CASCADE ON UPDATE CASCADE);");
    prep.executeUpdate();


    prep = conn.prepareStatement(
        "INSERT INTO cs_classes VALUES (?, ?, ?, ?);");
    prep.setInt(1, 10);
    prep.setString(2, "Software Engineering");
    prep.setString(3, "Fall21");
    prep.setString(4, "Tim Nelson");
    prep.addBatch();

    prep.setInt(1, 20);
    prep.setString(2, "Computer Systems");
    prep.setString(3, "Fall21");
    prep.setString(4, "Andy van Dam");
    prep.addBatch();

    prep.setInt(1, 30);
    prep.setString(2, "Operating Systems");
    prep.setString(3, "Spring22");
    prep.setString(4, "Kathi Fisler");
    prep.addBatch();

    prep.executeBatch();
     */


  }
}
