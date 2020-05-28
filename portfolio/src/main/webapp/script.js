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
      ['Hello there, General Kenobi.', 'English motherf*****! Do you speak it!?', "C'est tragique!", 'My middle name is James.'];

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
    let quoteFile = "<img src='Dune_Quotes/" + quote +"' width='480' height='600' alt='Image of a Dune quote.'>";

    //Adds picture to the page
    const quoteContainer = document.getElementById('quote_container');
    quoteContainer.innerHTML = quoteFile;

}

var random_number;
var guess_attempts;
var first_random_number = true;
function generateRandomNumber(){
    random_number = Math.floor(Math.random() * 11)
    guess_attempts  = 1;
}

function guessTheNumber() {
    if (first_random_number){
        generateRandomNumber();
        first_random_number = false;
    }
    
    var guessString = document.getElementById("numberGuess").value;
    var guess = parseInt(guessString, 10);

    console.log(guess + typeof guess);
    console.log(random_number+ typeof random_number);

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