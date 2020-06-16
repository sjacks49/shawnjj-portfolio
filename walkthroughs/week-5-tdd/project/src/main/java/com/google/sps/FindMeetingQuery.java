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
        ArrayList<TimeRange> invalid_times = new ArrayList<>();


        // Returns appropriate values for no attendees and invalid meeting length
        int full_day = TimeRange.WHOLE_DAY.duration();

        if (attendees.size() == 0) attendees = request.getOptionalAttendees();
        if (request.getDuration() > full_day) return Arrays.asList();
        
        

        //Generates a list of invalid timeslots for madatory
        invalid_times = generateInvalidTimes(events, attendees);

        invalid_times = resolveOverlaps(invalid_times);

        ArrayList<TimeRange> valid_times = generateValidTimes(invalid_times, request.getDuration());

        ArrayList<TimeRange> invalid_optional = generateInvalidTimes(events, request.getOptionalAttendees());

        for (TimeRange invalid : invalid_optional){ 
            for (TimeRange valid : valid_times) {
                if (invalid.equals(valid)) valid_times.remove(invalid);
            }
        }
        
        TimeRange[] ret = new TimeRange[valid_times.size()];
        ret = valid_times.toArray(ret);

        return Arrays.asList(ret);
    }

    public ArrayList<TimeRange> resolveOverlaps(ArrayList<TimeRange> ranges) {
        ListIterator<TimeRange> it = ranges.listIterator();    
        if (it.hasNext()) {  
            TimeRange range1 = it.next();

            if (it.hasNext()) {
                TimeRange range2 = it.next();
                
                if (range1.equals(range2)){}

                else {
                    if (range1.overlaps(range2)) {
                        int start = Math.min(range1.start(), range2.start());
                        int end  = Math.max(range1.end(), range2.end());
                        int replace_index = ranges.indexOf(range1);

                        TimeRange combined_range = TimeRange.fromStartEnd(start, end, false);
                        ranges.set(replace_index, combined_range);
                        ranges.remove(range2);
                    }
                }

            }
        }  

        return ranges;
    }

    public ArrayList<TimeRange> generateValidTimes(ArrayList<TimeRange> ranges, long duration) {
        int start_time = TimeRange.START_OF_DAY;
        ArrayList<TimeRange> valid_times = new ArrayList<>();

        for (TimeRange range : ranges) {
            TimeRange valid = TimeRange.fromStartEnd(start_time, range.start(), false);
            if ( (long) valid.duration() >= duration) {
                valid_times.add(valid);
            }
            start_time = range.end();
        }

        TimeRange final_range = TimeRange.fromStartEnd(start_time, TimeRange.END_OF_DAY, true);
        if (final_range.duration() != 0) {
            valid_times.add(final_range);
        }

        return valid_times;
    }

    public ArrayList<TimeRange> generateInvalidTimes(Collection<Event> events, Collection<String> attendees) {
        ArrayList<TimeRange> times = new ArrayList<>();

        for (String name : attendees) {
            for (Event event : events) {
                if (event.getAttendees().contains(name)) {
                    times.add(event.getWhen());
                }
            }
        }

        return times;
    }



}
