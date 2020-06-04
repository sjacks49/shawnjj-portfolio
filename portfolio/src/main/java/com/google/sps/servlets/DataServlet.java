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
import com.google.sps.data.Comment;



/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Comment").addSort("TimeStamp", SortDirection.ASCENDING);

    PreparedQuery results = datastore.prepare(query);
    List<Comment> load_comments = new ArrayList<>();
    
    for (Entity survey : results.asIterable()) {
      
      long id = survey.getKey().getId();
      String name = (String) survey.getProperty("Name");
      String evaluation = (String) survey.getProperty("Evaluation");
      String comment = (String) survey.getProperty("Question/Comment");
      long timestamp = (long) survey.getProperty("TimeStamp");

      Comment commentObject = new Comment(id, name, evaluation, comment, timestamp);

      load_comments.add(commentObject);
    }


    Gson gson = new Gson();

    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(load_comments));
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String name = getParameter(request, "name-input", "");
      String rb_response = request.getParameter("site-evaluation");
      String comment = getParameter(request, "text-input", "");
      long timestamp = System.currentTimeMillis();

    
      Entity surveyEntity = new Entity("Comment");
      surveyEntity.setProperty("Name", name);
      surveyEntity.setProperty("Evaluation", rb_response);
      surveyEntity.setProperty("Question/Comment", comment);
      surveyEntity.setProperty("TimeStamp", timestamp);
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(surveyEntity);
      
      response.sendRedirect("/index.html");

  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}



 