from extractor import Extractor
import json
import sys
from db.filler import Filler 
import base64


args = sys.argv[1:]

####### constants MQTT login information ########
HOST = 'eu.thethings.network'

PORT = 1883

USERNAME = 'project-software-engineering'

PASSWORD = 'ttn-account-v2.OC-mb7b1C5rDmos7-XTSoNE5T85V3c20jnrE8uN4jS0'

TOPIC = 'project-software-engineering/devices/#'
####### end ########

####### constants DB login information ########
DB_USER = 'udwuiyqcaqjflruo'

DB_PASSWORD = 'AgBZZKgGOWLaS5gOeBLM'

DB_HOST = 'bd91kfdkf5spw0xzmzqv-mysql.services.clever-cloud.com'

DB = 'bd91kfdkf5spw0xzmzqv'
####### end ########

# creating connection to the mysql database
filler = Filler(DB_USER, DB_PASSWORD, DB_HOST, DB)
filler.run()

flag = True

# this function decodes the base64 decoded messaged and calculates the values 
def decoder(message):

	base64_bytes = message.encode('ascii')
	message = base64.b64decode(base64_bytes)
	
	temperature = ((message[2] - 20) * 10 + message[3]) / 10 				
	pressure = message[0] / 2 + 950
	light = message[1]

	return pressure, light, temperature


def get_info(raw_json):
	sj = json.loads(raw_json)

	payload = sj['payload_raw']

	location = sj['dev_id']
		
	pressure, ambient_light, temperature = decoder(payload)

	if args:
		if args[0] in ('-i', '--info'):
			print("\nlocation: ", location, 
					"\ntemperature:" ,temperature,
					"\nambient_light:" ,ambient_light,
					"\npressure:" ,pressure, 
					'\n')

	return pressure, ambient_light, temperature, location

try:
	with Extractor(PORT, TOPIC, HOST, password=PASSWORD, username=USERNAME) as ex:
		while flag:
			if (msg := ex.get_message()) != None:
				filler.add_reading(*get_info(msg))

except KeyboardInterrupt:
	flag = False
	ex.order_VI_VI()
	filler.end()

	

	


	