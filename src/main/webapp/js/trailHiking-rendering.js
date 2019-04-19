function createUfoSightingsMap(){
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