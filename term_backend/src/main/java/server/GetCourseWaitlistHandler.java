package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetCourseWaitlistHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    List<List<String>> courseWaitlist = new ArrayList<List<String>>();
    return courseWaitlist;
  }

}
