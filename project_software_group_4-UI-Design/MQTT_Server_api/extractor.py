"""
	Evgeni Genchev
	4747103
	extractor class for retrieving the data from the
	MQTT server
	initial commit 
"""
import paho.mqtt.client as mqtt
import threading
import sys
import json


class Extractor:
	"""
	 	CONSTRUCTOR
	"""
	def __init__(self,  port,  topic, host, password=None, username=None):
		self.__host = host
		self.__username = username
		self.__port = port
		self.__topic = topic
		self.__password = password
		self.__connection_thread = None
		self.__message = None
		self._running = True

	"""
		The 'magic' function __enter__ is triggered when the class is opened 
		with a context manager it 
		returns self because of use inside the context manager
	""" 
	def __enter__(self):
		self.__connection_thread = threading.Thread(target=self.connect_to_MQTT)
		self.__connection_thread.start()
		return self
	"""
	the 'magic' function __exit__ executes every time when the conxter manager 
	finishes its work. Normally this function is used to close a conection, file or
	in this case a thread. 

	"""

	def __exit__(self, exc_type, exc_val, exc_tb):
		self._running = False

	"""
	
	these functions (on_connect, on_message) are created to be used as @property 
	functions internally by mqtt.Client class


		""
		on_connect prints a message regarding the connection
		""

		""
		on_message sets the  value of __message to the value of the received message
		""

	"""
	def on_connect(self, client, userdata, flags,rc):
		print("Bad connection! Return code: " + rc if rc else "Connection established")

	def on_message(self, client, userdata, msg):
		message = (msg.payload.decode('utf-8'))
		self.__message = self.pretify(message)

	"""
		This function connects to the MQTT server as a client with
		the given username, password.

		the while loop is the way of stoping the thread which is set above in __exit__

		client.subscribe([SOME TOPIC]) is used to subscribe to topic so that our on_message listens for
		messages 

		with client.on_connect = self.on_connect we set the @property functions
			 client.on_message = self.on_message

		client.connect([HOST], [PORT]) is used to connect to the MQTT server

		client.loop_forever() this is never ending loop


	"""

	def connect_to_MQTT(self):
		while self._running:
			client = mqtt.Client('evgena')
			if self.__password != None:
				client.username_pw_set(self.__username, password=self.__password)
			client.on_connect = self.on_connect
			client.on_message = self.on_message
			client.connect(self.__host, self.__port)
			client.subscribe(self.__topic)	
			client.loop_forever()

	"""
		this function is getter for __message
	"""

	def get_message(self):
		return self.__message;


	"""
		this function make ads the indentation on a json string
	"""

	def pretify(self, msg):
		return json.dumps(json.loads(msg), indent=4, sort_keys=True)

