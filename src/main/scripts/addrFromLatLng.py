import requests
import csv
import os

LAT_IDX = 0
LNG_IDX = 1

# Initial file setup, put new csv in same location as csv with just coordinates.
csv_location = '../webapp/WEB-INF/Hikes_Location.csv'
log_file = open('log.txt', 'w+')
new_csv = open('../webapp/WEB-INF/Hikes_Locations_Names.csv', 'w+')
with open(csv_location) as csv_file:
  csv_reader = csv.reader(csv_file, delimiter=',')
  sensor = 'true'
  num_entries = 0
  base = 'https://maps.googleapis.com/maps/api/geocode/json?'
  # If using the script, make an api key and don't push to github to not expose it (or
  # export environment variable GOOGLE_API_KEY I think, not sure).
  api_key = 'REPLACE W/ API KEY'
  for row in csv_reader:
    print('Entry {} in progress'.format(num_entries))
    params = 'latlng={},{}&sensor={}&key={}'.format(row[LAT_IDX], row[LNG_IDX], sensor, api_key)
    url = '{}{}'.format(base, params)
    response = requests.get(url).json()
    try:
      # The following is the info we need from the api for address from latlng
      formatted_addr = response['results'][0]['formatted_address']
      addr_list = formatted_addr.split(',')
      if len(addr_list) != 4:
        formatted_addr = 'N/A, ' + formatted_addr
      # New csv formatted as
      # [LAT], [LNG], [ADDR] if available or [N/A], [State zip], [Country]
      print(','.join(row)+', '+formatted_addr, file=new_csv)
    except:
      print('Failed to get address from google api at {}'.format(num_entries))
      print(','.join(row), file=new_csv)
    # Log what was the output of results to debug in case couldn't parse response. Entry
    # number in log should correspond to entry number in csv.
    print(response['results'], file=log_file)
    num_entries += 1

new_csv.close()
log_file.close()