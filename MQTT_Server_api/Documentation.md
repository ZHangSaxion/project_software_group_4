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
that our `on_message` listens for messages and it also provides
messeges concerning subscribtion:

```python
def on_subscribe(self, client, userdata, mid, granted_qos):
		try:
			for t, i in self.__topic:
				print(f"[*] Broker successfully subscribed to {t}")
		except Exception as e:
			print(f"[*] Broker successfully subscribed to all the topics")

``` 




with `client.on_connect = self.on_connect` and `client.on_message = self.on_message` 
pahon-mqtt @property functions are being overwritten

`on_messege` is used to set the value of `self.__message` to the last recieved message decoded in utf-8:

```python

def on_message(self, client, userdata, msg):
		message = (msg.payload.decode('utf-8'))
		self.__message = self.pretify(message)
```  

`on_connect` is printing either success or fail message on the connection status:

```python
def on_connect(self, client, userdata, flags,rc):
	print("[*] Bad connection! Return code: " + rc if rc else f"[*] Connection to MQTT server {self.__host} established")

```


`client.connect([HOST], [PORT])` is used to connect to the MQTT server

With the if statement and `_conections_count` it is ensured that 
only one paho-mqtt thread is being created and started

In the class Extrractor there is a getter function that sends the current
last received message and then sets the value of `self.__message` to None 
in order to have no way of messege to be read twice:

```python
def get_message(self):
		msg = self.__message
		self.__message = None
		return msg;
	
```

In order to have the functionality of using contex manger the class has
to have a way of stoping all the threads and loops that may be running
in a function called `__exit__`:


```python
def __exit__(self, exc_type, exc_val, exc_tb):
		self.order_VI_VI()

```  

This function uses another class function called `order_VI_VI`. The stopping
procces inside the magic function beacause this will ensure that if the contex
manager routine fail without properly exiting the thread could still be stopped.
Inside `order_VI_VI` you can see two things. First the paho-mqtt internal 
loop/thread is being stopped and then the loop inside the connection thread which
will force the thread to stop.

```python

def order_VI_VI(self):
		self._client.loop_stop()
		self._running = False

```

## Filler


### Functionality

The only functionality of the Filler class is to connect database that can be 
defined in the initialisation of the class and retrieving and inserting 	
data using predifined queries that suit the WeatherStation project
	
### Usage
	
In the intitialisation of the class all the parameters that are needed in order
to start the connection are being defined. On top of that there are `__db` and 
`__cursor` variables that will be assigned a value after the class is being run
	
```python
def __init__(self, user, password, host, dbn):
		self.__user = user
		self.__password = password
		self.__host = host
		self.__db_name = dbn
		self.__db = None
		self.__cursor = None
```

After initialisation the server will not start untill the `run` function is called.
Than a connection to the database is established and a cursor is being created and
respectively the values of `__db` and `__cursor` are set: 

```python
def run(self):
		try:
			self.__db = ms.connect(
				user=self.__user,
				password=self.__password,
				host=self.__host,
				database=self.__db_name
				)

			print(f"[*] Connection to database '{self.__host}.{self.__db_name}' established")

			self.__cursor = self.__db.cursor()

			print("[*] Database cursor created")

		except ms.Error as mer:
			if mer.errno == errorcode.ER_ACCESS_DENIED_ERROR:
				print("Something is wrong with your user name or password")
			elif mer.errno == errorcode.ER_BAD_DB_ERROR:
				print("Database does not exist")
			else:
				print("[*] Maxed out connections to database")
```

After the establishment of the connection there are 3 options available.

`get_id` is a function that returns the `sensor_id` and it takes as
arguments the sensor location:

```python

def get_id(self, sensor):
		query = f"SELECT sensor_id FROM sensor WHERE location = '{sensor}'"
		self.__cursor.execute(query)

		if result := self.__cursor.fetchall():
			sensor_id = int(result[0][0])
			return sensor_id
		else:
			return self.add_sensor(sensor)
```

If the sensor could not be find in the database `get_id` returns a call to add_sensor
an insertion query is being executed:

```python
def add_sensor(self, sensor):
		query = f"insert into sensor(location) values ('{sensor}')";
		
		self.__cursor.execute(query)
		sensor_id = self.__cursor.lastrowid
		
		self.__db.commit()
		
		print(f"[*] New sensor '{sensor}' added")
		
		return int(sensor_id)

```

Finally the most important part of the filler is the `add_readings` is used to insert reading
the database:


```python

def add_reading(self, b_pressure, ambient_light, temperature, sensor):
		sensor_id = self.get_id(sensor)
		query_insert = f"INSERT INTO readings (b_pressure, ambient_light, temperature, sensor_id) values ({b_pressure}, {ambient_light}, {temperature}, {sensor_id});"
		self.__cursor.execute(query_insert)
		
		self.__db.commit()
		
		print(f"[*] Reading for {sensor} added")
``` 


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



