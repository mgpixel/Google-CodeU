google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(drawBestHikesChart);

function drawBestHikesChart(){
  // create the data table instance (to pass data to render)
  var bestHikesData = new google.visualization.DataTable();

  // define the columns of the DataTable
  bestHikesData.addColumn('string', 'Hike Name');
  bestHikesData.addColumn('string', 'Location');
  bestHikesData.addColumn('number', 'Distance (Miles)');
  bestHikesData.addColumn({type: 'string', role: 'style'});
  bestHikesData.addColumn({type: 'string', role: 'annotation'});

  // add data as rows
  bestHikesData.addRows([
    ["Stillaguamish River Station", "Arlington, Washington", 11, 'color: #7BBF6A', "Arlington, Washington"],
    ["Breakneck Ridge", "West Point, New York", 3.6, 'color: #7BBF6A', "West Point, New York"],
    ["Angel's Landing", "Zion National Park, Utah", 2.5, 'color: #7BBF6A', "Zion National Park, Utah"],
    ["Camelback Mountain Summit Trail", "Paradise Valley, Arizona", 2.4, 'color: #7BBF6A', "Paradise Valley, Arizona"],
    ["Half Dome Trail", "Yosemite National Park, California", 16, 'color: #7BBF6A', "Yosemite National Park, California"],
    ["Bright Angel Trail", "Grand Canyon National Park, Arizona", 7.8, 'color: #7BBF6A', "Grand Canyon National Park, Arizona"],
    ["Eagle Rock Loop", "Glenwood, Arkansas", 26.8, 'color: #7BBF6A', "Glenwood, Arkansas"],
    ["Alder Glen Recreation Site Bike Trail", "Beaverton, Oregon", 13, 'color: #7BBF6A', "Beaverton, Oregon"],
    ["Linville Falls Trail", "Blue Ridge Parkway, North Carolina", 0.9, 'color: #7BBF6A', "Blue Ridge Parkway, North Carolina"],
    ["Old Rag Fire Road", "Shenandoah National Park, Virginia", 4.3, 'color: #7BBF6A', "Shenandoah National Park, Virginia"]
  ]);

  var viewHikes = new google.visualization.DataView(bestHikesData);
  viewHikes.setColumns([0, 2, 3, 4]);

  // create instance of specific chart to use
  var bestHikesChart = new google.visualization.BarChart(document.getElementById('best_hike_chart'));

  // set options/styling of chart
  var bestHikesOptions = {
    width: 1000,
    height: 800,
    title: "Top 10 Hikes in the United States and Trail Length",
    hAxis: {
      title: "Length of Hike (Miles)",
    },
    vAxis: {
      title: 'Trail Name'
    },
    legend: {
      position: 'none'
    },
    annotations: {
      alwaysOutside: true
    }
  };

  bestHikesChart.draw(viewHikes, bestHikesOptions);
}
