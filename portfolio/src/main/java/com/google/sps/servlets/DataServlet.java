// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //response.setContentType("text/html;");
    //response.getWriter().println("<h1>Hello Shawn!</h1>");

    /*
    ArrayList<String> comments = new ArrayList<String>();

    comments.add("You can tell the tree by the fruit it bears. ");
    comments.add("You see it through what the organization is delivering as far as a concrete program. ");
    comments.add("If the tree's fruit sours or grows brackish, then the time has come to chop it down - bury it and walk over it and plant new seeds. ");
    comments.add("- Huey P. Newton");

    Gson gson = new Gson();
    String json_text = gson.toJson(comments);

    response.setContentType("application/json;");
    response.getWriter().println(json_text);
    */

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Comment");

    PreparedQuery results = datastore.prepare(query);
    List<String> load_comments = new ArrayList<>();
    System.out.println(results);

    for (Entity survey : results.asIterable()) {
      System.out.println(survey);

      long id = survey.getKey().getId();
      String comment = (String) survey.getProperty("Question/Comment");
      //long timestamp = (long) survey.getProperty("timestamp");

      load_comments.add(comment);
    }
    //load_comments.add("comment");

    Gson gson = new Gson();

    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(load_comments));
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String name = getParameter(request, "name-input", "");
      String rb_response = request.getParameter("site-evaluation");
      String text = getParameter(request, "text-input", "");
      long timestamp = System.currentTimeMillis();
    
      Entity surveyEntity = new Entity("Comment");
      surveyEntity.setProperty("Name", name);
      surveyEntity.setProperty("Evaluation", rb_response);
      surveyEntity.setProperty("Question/Comment", text);
      surveyEntity.setProperty("TimeStamp", timestamp);
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(surveyEntity);
      

      String[] response_tokens = text.split(" ");

      
      response.setContentType("text/html");
      response.getWriter().println("Thanks for Your Evaluation " + name +": " + rb_response + "<br></br>");
      response.getWriter().println(Arrays.toString(response_tokens) + "<br></br>");
      response.getWriter().println("<a href='index.html'>Return to Home page</a>");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}



 