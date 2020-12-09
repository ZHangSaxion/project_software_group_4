# DOCUMENTATION ON MQTT BROKER

## Extractor 

### Functionality

    The extractor.py class provides the user with the
    functionality to open a connection with a MQTT server, 
    subscribe to numerous topics and resieve the json formated 
    payloads. The Extractor class is meant bo be used as a 
    background process. Thus, it has some thread managment 
    implementations: 

### Usage

    The Extractor class has only one constructor tha takes the
    host, port, topic and optionally username and passwords 
    arguments and sets them up as public variable.

```python

def __init__(self,  port,  topic, host, password=None, username=None):
	
	#connection needed variables
	self.__host = host
	self.__username = username
	self.__port = port
	self.__topic = topic
	self.__password = password
	
	#thread management variables
	self._running = True
	self._connections_count = 0
	self.__connection_thread = None
		
	#comunication variables
	self.__message = None
	self._client = None

```


    Above you can see three sets of varibales:
    1. Connection needed:
       These variable are indentifiers needed to establish      
       connection to the mqtt server through `paho-mqtt` 
       library
    2. Thread management:
       These variables ensure the succesfull start and end of the
       connection thread.
           - _running : is a flag used to stop the thread loop of 
           running
           - _connections_count: ensures that only one thread 
           could be alive 
           -__conection_thread: this is the thread  variable
    3. Comunication:
       - __message:  the last message that the broker have 
       received
       - __client: the paho-client that connects to the
       MQTT server.
    
The Extractor class makes use of some magic functions and it
should always be created with the contex manager `with`:

```
with Extractor(*login_data) as ex:
```

When creating the object the `__enter__` function is called:

```python
    def __enter__(self):
		self.__connection_thread = threading.Thread(target=self.connect_to_MQTT)
		self.__connection_thread.start()
		return self
```
  
  In this function a connection thread is being created and 
  started. The return should be `self` in order for the object to 
  be used within the context manager. The started thread is 
  responsible for the whole comunication with the MQTT server:
  
  ```python
    
  def connect_to_MQTT(self):
		while self._running:

			self._client = mqtt.Client('evgena')
			if self.__password != None:
				self._client.username_pw_set(self.__username, password=self.__password)
			self._client.on_connect = self.on_connect
			self._client.on_message = self.on_message
			self._client.on_subscribe = self.on_subscribe
			self._client.connect(self.__host, self.__port)
			self._client.subscribe(self.__topic)
			if not self._connections_count:	
				self._client.loop_start()
				self._connections_count += 1
  
  ```
  This function connects to the MQTT server as a client with
the given username, password.
the while loop is the way of stoping the thread which is set 
above in `__exit__`

client.subscribe([SOME TOPIC]) is used to subscribe to topic so 
that our `on_message` listens for messages 

with `client.on_connect = self.on_connect` and `client.on_message = self.on_message` 
pahon-mqtt @property functions are being overwritten 

`client.connect([HOST], [PORT])` is used to connect to the MQTT server

With the if statement and `_conections_count` it is ensured that 
only one paho-mqtt thread is being created and started



