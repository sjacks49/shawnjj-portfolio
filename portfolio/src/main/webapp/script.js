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


/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      [
        "You may not control all the events that happen to you, but you can decide not to be reduced by them. -Maya Angelou",
        "The drums of Africa still beat in my heart. They will not let me rest while there is a single Negro boy or girl without a chance to prove his worth. -Mary McLeod Bethune",
        "The willow submits to the wind and prospers until one day it is many willows - a wall against the wind. -Frank Herbert", 
        "Do. OR do not. There is no try. -Yoda",
        "Any man's death diminishes me, because I am involved in mankind, and therefore never send to know for whom the bells tolls; it tolls for thee. -John Donne",
        "To be a Negro in this country and to be relatively conscious is to be in a rage almost all the time. - James Baldwin",
        "Tough times never last. Tough people do. -Robert Schuller"
      ];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function randomDuneQuote() {
    const filenames = [
        "quote_repeat.jpg",
        'quote_fear.jpg',
        'quote_achieve.jpg',
        'quote_change.jpg',
    ];
    // Picks random filename
    const quote = filenames[Math.floor(Math.random() * filenames.length)];
    let quoteFile = "<img src='Dune_Quotes/" + quote +"' width='480' height='600' alt='Image of a Dune quote.' class='center'>";

    //Adds picture to the page
    const quoteContainer = document.getElementById('quote_container');
    quoteContainer.innerHTML = quoteFile;

}

var random_number;
var guess_attempts;
var first_random_number = true;
function generateRandomNumber(){
    random_number = Math.floor(Math.random() * 101)
    guess_attempts  = 1;
}

function guessTheNumber() {
    if (first_random_number){
        generateRandomNumber();
        first_random_number = false;
    }
    
    var guessString = document.getElementById("numberGuess").value;
    var guess = parseInt(guessString, 10);

    console.log("Guess: " + guess + typeof guess);
    console.log("Actual Number: " + random_number + typeof random_number);

    if (guess == random_number){
        console.log("Original number: " + random_number);
        alert("Congrats! You guessed the right number in " + guess_attempts + " attemps! A new number has been generated, so feel free to play again!");
        generateRandomNumber();
        console.log("New number: " + random_number);

    }
    else if (guess < random_number){
        alert("Sorry! Try guessing a bigger number...");
        guess_attempts++;
    }
    else {
        alert("Sorry! Try guessing a smaller number...");
        guess_attempts++;
    }
}

async function fetchFromServer(max_comments) {
  const response = await fetch('/data');
  const json_surveys = await response.json();
  console.log(json_surveys);

  var max = Math.min(parseInt(max_comments), json_surveys.length);

  const commentListElement = document.getElementById("server-quote");

  while (commentListElement.hasChildNodes()) {  
    commentListElement.removeChild(commentListElement.firstChild);
  }
  
  var output = "";
  for(var ndx = 0; ndx < max; ndx++){
    commentListElement.appendChild(createCommentElement(json_surveys[ndx]));

  }
  
  
}

async function deleteComment(survey) {
    const params = new URLSearchParams();
    params.append('id', survey.id);
    await fetch('/delete-data', {method:'POST', body: params});
}

function createCommentElement(survey) {
  const listElement = document.createElement('li');
  listElement.className = 'comments';

  const commentElement = document.createElement('span');
  commentElement.insertAdjacentText("beforeend", "");
  commentElement.innerText = survey.comment;

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
    deleteComment(comment);

    // Remove the comment from the DOM.
    listElement.remove();
  });

  listElement.appendChild(commentElement);
  listElement.appendChild(deleteButtonElement);
  return listElement;
}

async function deleteAll() {
    const response = await fetch('/data');
    const json_surveys = await response.json();

    json_surveys.forEach((survey) => {
        deleteComment(survey);
    });
}



async function siteEvaluationData() {
    const response = await fetch('/data');
    const commentObjects = await response.json();

    var perfectData = 0;
    var servicabledata = 0;
    var improvementData = 0;

    commentObjects.forEach((survey) => {
        switch(survey.evaluation) {
            case 'perfect':
                perfectData++;
                break;
            case 'servicable':
                servicabledata++;
                break;
            case 'needs-improvements':
                improvementData++;
                break;

        }
    })

    return [perfectData, servicabledata, improvementData];
}

function createMap() {
    // New Orleans Coordinates
    var latitude_default = 29.9511;
    var longitude_default = -90.0715;
    //Map Default Settings
    var zoom_default = 13;
    var mapType_default = "hybird";
    var dark_mode = [
            {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
            {
              featureType: 'administrative.locality',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'poi',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'poi.park',
              elementType: 'geometry',
              stylers: [{color: '#263c3f'}]
            },
            {
              featureType: 'poi.park',
              elementType: 'labels.text.fill',
              stylers: [{color: '#6b9a76'}]
            },
            {
              featureType: 'road',
              elementType: 'geometry',
              stylers: [{color: '#38414e'}]
            },
            {
              featureType: 'road',
              elementType: 'geometry.stroke',
              stylers: [{color: '#212a37'}]
            },
            {
              featureType: 'road',
              elementType: 'labels.text.fill',
              stylers: [{color: '#9ca5b3'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'geometry',
              stylers: [{color: '#746855'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'geometry.stroke',
              stylers: [{color: '#1f2835'}]
            },
            {
              featureType: 'road.highway',
              elementType: 'labels.text.fill',
              stylers: [{color: '#f3d19c'}]
            },
            {
              featureType: 'transit',
              elementType: 'geometry',
              stylers: [{color: '#2f3948'}]
            },
            {
              featureType: 'transit.station',
              elementType: 'labels.text.fill',
              stylers: [{color: '#d59563'}]
            },
            {
              featureType: 'water',
              elementType: 'geometry',
              stylers: [{color: '#17263c'}]
            },
            {
              featureType: 'water',
              elementType: 'labels.text.fill',
              stylers: [{color: '#515c6d'}]
            },
            {
              featureType: 'water',
              elementType: 'labels.text.stroke',
              stylers: [{color: '#17263c'}]
            }
          ]

    const map = new google.maps.Map(
        document.getElementById('map'),
        {
            center: {lat: latitude_default, lng: longitude_default}, 
            zoom: zoom_default,
            mapTypeID: mapType_default,
            styles: dark_mode
        }
    );

    //Add Search Bar to Map
    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(input);

    //Search Bar Functionality
    var markers = [];
    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();

        if (places.length == 0) {
            return;
        }

        //Clear old markers.
        markers.forEach(function(marker) {
            marker.setMap(null);
        });
        markers = [];

        // Get the icon, name, and location.
        var bounds = new google.maps.LatLngBounds();
        places.forEach(function(place) {
            if (!place.geometry) {
                console.log("Returned place contains no geometry");
                return;
            }
            var icon = {
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(25, 25)
            };

            // Create a marker
            markers.push(new google.maps.Marker({
                map: map,
                icon: icon,
                title: place.name,
                position: place.geometry.location
            }));

            if (place.geometry.viewport) {
                // Only geocodes have viewport.
                bounds.union(place.geometry.viewport);} 
            else {
                bounds.extend(place.geometry.location);
            }
        });
        map.fitBounds(bounds);
    });
        
    //Generate markers for places in NOLA
    
    var bayouHW_Coords = {lat: 29.946270, lng: -90.112960};
    var camelliaG_Coords = {lat: 29.943760, lng: -90.133812};
    var mikimoto_Coords = {lat: 29.962280, lng: -90.113220};
    var juansFB_Coords = {lat: 29.973780, lng: -90.100740};
    var citypark_Coords = {lat: 29.991908, lng:-90.097360};
    var chilis_Coords = {lat: 30.005930, lng: -90.178660};
    var lighthouse_Coords = {lat: 30.030910, lng: -90.059390};
    var BN_Coords = {lat: 29.905310, lng: -90.068480};
    var GamingC_Coords = {lat: 29.948310, lng: -90.131360};

    const bayouHW = createMarker(bayouHW_Coords, map, "Bayou Hot Wings");
    const camelliaG =  createMarker(camelliaG_Coords, map, "Camellia Grill");
    const mikimoto = createMarker(mikimoto_Coords, map, "Mikimotos");
    const juansFB = createMarker(juansFB_Coords, map, "Juan's Flying Burrito");
    const citypark = createMarker(citypark_Coords, map, "City Park Track");
    const chilis = createMarker(chilis_Coords, map, "Chili's Bar and Grill");
    const lighthouse = createMarker(lighthouse_Coords, map, "Lighthouse");
    const barnesAndNobels = createMarker(BN_Coords, map, "Barnes and Nobels");
    const gamingCafe = createMarker(GamingC_Coords, map, "d4 Gaming Cafe");

}



//Generates a marker for the given map
function createMarker(coordinates, map, title) {

    var marker = new google.maps.Marker({
        position: coordinates,
        map: map,
        title: title,
    });

    //OnClick Listener
    marker.addListener('click', function() {
        map.setZoom(18);
        map.setCenter(marker.getPosition());
    });

    return marker;
}


