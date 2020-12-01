from extractor import Extractor
import json
import sys
from db.filler import Filler 
import getpass 

args = sys.argv[1:]

HOST = 'eu.thethings.network'

PORT = 1883

USERNAME = 'project-software-engineering'

PASSWORD = 'ttn-account-v2.OC-mb7b1C5rDmos7-XTSoNE5T85V3c20jnrE8uN4jS0'

# TOPIC = [('project-software-engineering/devices/pywierden/#',0),
# 		('project-software-engineering/devices/pysaxion/#',0),
# 		('project-software-engineering/devices/pygarage/#',0),
# 		('project-software-engineering/devices/pygronau/#',0)]

TOPIC = 'project-software-engineering/devices/#'



# DB_PASSWORD = getpass.getpass("Enter db password:")

DB_USER = 'udwuiyqcaqjflruo'

DB_PASSWORD = 'by3sjyvuabMAXgxMi1vz'

DB_HOST = 'bd91kfdkf5spw0xzmzqv-mysql.services.clever-cloud.com'

DB = 'bd91kfdkf5spw0xzmzqv'

filler = Filler(DB_USER, DB_PASSWORD, DB_HOST, DB)

filler.run()

flag = True

def decoder():
	pass

def get_info(raw_json):
		sj = json.loads(raw_json)

		location = sj['dev_id']
		temperature = sj['payload_fields']['temperature']
		time = sj['metadata']['gateways'][0]['time']
		ambient_light = sj['payload_fields']['light']
		pressure = sj['payload_fields']['pressure']

		if args:
			if args[0] in ('-i', '--info'):
				print('{',"\nlocation: ", location, 
						"\ntime:" ,time, 
						"\ntemperature:" ,temperature,
						"\nambient_light:" ,ambient_light,
						"\npressure:" ,pressure, 
						'\n}\n')

		return pressure, ambient_light, temperature, location

try:
	with Extractor(PORT, TOPIC, HOST, password=PASSWORD, username=USERNAME) as ex:
		while flag:
			if (msg := ex.get_message()) != None:
				filler.add_reading(*get_info(msg))

except KeyboardInterrupt:
	ex.order_VI_VI()
	filler.end()

	

	


	