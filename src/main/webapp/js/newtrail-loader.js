function addNewTrail() {
  // get the form elements
  let trailName = document.getElementById('trail-name').value;
  let stateName = document.getElementById('state-name').value;
  let cityName = document.getElementById('city-name').value;
  let startLat = document.getElementById('lat-start').value;
  let startLon = document.getElementById('lon-start').value;

  const url = '/new-trails?trailName=' + trailName + "&state=" +
    stateName + "&city=" + cityName + "&startLat=" + startLat + "&startLon=" + startLon;


    fetch(url)
        .then((response) => {
          return response.json();
        })
        .then((messages) => {
          const messagesContainer = document.getElementById('message-container');
          if (messages.length == 0) {
            messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
          } else {
            messagesContainer.innerHTML = '';
          }
          messages.forEach((message) => {
            const messageDiv = buildMessageDiv(message);
            messagesContainer.appendChild(messageDiv);
          });
        });

  fetch(url).then(function(response) {
    return response.json();
  }).then(())



    function createUfoSightingsMap() {
        fetch('/MapsInfo').then(function(response) {
            return response.json();
        }).then((ufoSightings) => {

            const map = new google.maps.Map(document.getElementById('map'), {
                center: {lat: 48.012, lng: -120.623},
                zoom:10
            });

        ufoSightings.forEach((ufoSighting) => {
            new google.maps.Marker({
                position: {lat: ufoSighting.lat, lng: ufoSighting.lng},
                map: map
            });
        });
      });
    }
}
