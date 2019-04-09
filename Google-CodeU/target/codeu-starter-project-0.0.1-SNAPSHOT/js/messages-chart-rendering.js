google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(fetchMessageData);

function fetchMessageData() {
  fetch('/messageschart')
    .then((response) => {
      return response.json();
    })
    .then((msgJson) => {
      // turn message json into DataTable
      var msgData = new google.visualization.DataTable();

      // define columns for DataTable instance
      msgData.addColumn('date', 'Date');
      msgData.addColumn('number', 'Message Count');

      // go through all messages saved in json
      for (i = 0; i < msgJson.length; i++) {
        // array to hold row in msgData DataTable
        msgRow = [];

        // get the correctly formatted row information
        var timestampAsDate = new Date(msgJson[i].timestamp);
        var totalMessages = i + 1;

        console.log(timestampAsDate);

        msgRow.push(timestampAsDate);
        msgRow.push(totalMessages);
        msgData.addRow(msgRow);
      }

      drawMessagesChart(msgData);
    });
}

// draws a google chart using the data from 'data' (a DataTable instance)
function drawMessagesChart(data) {
  // create instance of specific chart to use
  var messageChart = new google.visualization.LineChart(document.getElementById('messages_chart'));

  // set options/styling of chart
  var messageChartOptions = {
    width: 1000,
    height: 1000,
    title: "Number of Messages Sent Per Day",
    hAxis: {
      title: "Date",
    },
    vAxis: {
      title: 'Number of Messages',
      gridlines: {
        count: -1,
        units: {
          days: {format: ['MMM dd']},
          hours: {format: ['HH:mm', 'ha']},
        }
      },
      minorGridlines: {
        units: {
          hours: {format: ['hh:mm:ss a', 'ha']},
          minutes: {format: ['HH:mm a Z', ':mm']}
        }
      }
    },
    legend: {
      position: 'none'
    }
  };

  messageChart.draw(data, messageChartOptions);
}
