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

package com.google.sps;

import java.lang.Math;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.*;

import com.google.sps.Event;
import com.google.sps.Events;
import com.google.sps.MeetingRequest;
import com.google.sps.TimeRange;



public final class FindMeetingQuery {
  
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        
        Collection<String> attendees = request.getAttendees();
        TimeRange[] valid_times = new TimeRange[0];
        ArrayList<TimeRange> invalid_times = new ArrayList<>();


        // Returns appropriate values for no attendees and invalid meeting length
        if (attendees.size() == 0) return Arrays.asList(TimeRange.WHOLE_DAY);
        if (request.getDuration() > 24*60) return Arrays.asList();
        

        //Generates a list of invalid timeslots
        for (String name : attendees) {
            for (Event event : events) {
                if (event.getAttendees().contains(name)) {
                    invalid_times.add(event.getWhen());
                }
            }
        }

        invalid_times = resolveContains(invalid_times);

        
        TimeRange[] ret = new TimeRange[invalid_times.size()];
        ret = invalid_times.toArray(ret);

        return Arrays.asList(ret);
    }

    public ArrayList<TimeRange> resolveContains(ArrayList<TimeRange> ranges) {

        ListIterator<TimeRange> it = ranges.listIterator();    
        if (it.hasNext()) {  
            TimeRange range1 = it.next();

            if (it.hasNext()) {
                TimeRange range2 = it.next();
                if (range1.equals(range2)){}

                else {
                    if (range1.contains(range2)) ranges.remove(range2);
                    else if (range2.contains(range1)) ranges.remove(range1);
                }

            }
        }  

        return ranges;
    }

    public ArrayList



}
