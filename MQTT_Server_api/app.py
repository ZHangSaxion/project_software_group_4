from extractor import Extractor
import json
import sys

HOST = 'eu.thethings.network'

PORT = 1883

USERNAME = 'project-software-engineering'

PASSWORD = 'ttn-account-v2.OC-mb7b1C5rDmos7-XTSoNE5T85V3c20jnrE8uN4jS0'

TOPIC = [('project-software-engineering/devices/pywierden/#',0),
		('project-software-engineering/devices/pysaxion/#',0),
		('project-software-engineering/devices/pygarage/#',0),
		('project-software-engineering/devices/pygronau/#',0)]
flag = True


def get_info(raw_json):
		sj = json.loads(raw_json)

		location = sj['dev_id']
		temperature = sj['payload_fields']['temperature']
		time = sj['metadata']['gateways'][0]['time']
		ambient_light = sj['payload_fields']['light']
		pressure = sj['payload_fields']['pressure']

		print('{',"\nlocation: ", location, 
				"\ntime:" ,time, 
				"\ntemperature:" ,temperature,
				"\nambient_light:" ,ambient_light,
				"\npressure:" ,pressure, 
				'\n}')



try:
	with Extractor(PORT, TOPIC, HOST, password=PASSWORD, username=USERNAME) as ex:
		while flag:
			if (msg := ex.get_message()) != None:
				get_info(msg)
except KeyboardInterrupt as er:
	ex.order_VI_VI()
	print("Fair well, Kenobi")
	


	