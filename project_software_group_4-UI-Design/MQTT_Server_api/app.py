from extractor import Extractor
import json 

HOST = 'eu.thethings.network'

PORT = 1883

USERNAME = 'project-software-engineering'

PASSWORD = 'ttn-account-v2.OC-mb7b1C5rDmos7-XTSoNE5T85V3c20jnrE8uN4jS0'

TOPIC = 'project-software-engineering/devices/pygronau/#'

with Extractor(PORT, TOPIC, HOST, password=PASSWORD, username=USERNAME) as ex:
	while True:
		if (message:= ex.get_message()) != None:
			print(message)
			break
			


	