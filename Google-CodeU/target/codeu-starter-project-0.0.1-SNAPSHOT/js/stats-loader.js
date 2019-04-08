/**
 * Gets all of the data from the /stats url and shows it in a more readable
 * format.
 */
function fetchStats() {
  const url = '/stats';
  fetch(url).then((response) => {
    return response.json();
  }).then((stats) => {
    const statsContainer = document.getElementById('stats-container');
    statsContainer.innerHTML = '';
    // Build each element and append to stats-container to show data
    const messageCountElement = buildStatElement('Message count: ' + stats.messageCount);
    const averageMessageLengthElement = buildStatElement('Average message length: ' + stats.averageMessageLength);
    const userCountElement = buildStatElement('User count: ' + stats.userCount);
    const activeUserElement = buildStatElement('Most active user: ' + stats.mostActiveUser);
    statsContainer.appendChild(messageCountElement);
    statsContainer.appendChild(averageMessageLengthElement);
    statsContainer.appendChild(userCountElement);
    statsContainer.appendChild(activeUserElement);
  })
}

/**
 * Builds the string passed in into an HTML element to display
 * @param {string} statString 
 */
function buildStatElement(statString) {
  const statElement = document.createElement('p');
  statElement.appendChild(document.createTextNode(statString));
  return statElement;
}

/** Fetches data and populates UI of page */
function buildUI() {
  fetchStats()
}