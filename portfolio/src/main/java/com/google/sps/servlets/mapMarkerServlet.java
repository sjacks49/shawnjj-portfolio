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

import com.google.sps.Data.place;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Returns a json object with coordinates, name, and description of the place
@WebServlet("/place-data")
public class mapMarkerServlet extends HttpServlet {
    private ArrayList<place> places;

    @Override
    public void init() {
        places = new ArrayList<>();

        Scanner scan = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/places.csv"));
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] tokens = line.split(",");

            double lat = Double.parseDouble(tokens[0]);
            double lng = Double.parseDouble(tokens[1]);
            String name = tokens[2];
            String description = tokens[3];

            places.add(new place(lat, lng, name, description));
        }
        scan.close();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(places);
        response.getWriter().println(json);
    }
}