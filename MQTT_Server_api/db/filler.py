import mysql.connector as ms
from mysql.connector import errorcode

class Filler:

	def __init__(self, user, password, host, dbn):
		self.__user = user
		self.__password = password
		self.__host = host
		self.__db_name = dbn
		self.__db = None
		self.__cursor = None


	def run(self):
		try:
			self.__db = ms.connect(
				user=self.__user,
				password=self.__password,
				host=self.__host,
				database=self.__db_name
				)

			print(f"[*] Connection to database '{self.__db_name}' established")

			self.__cursor = self.__db.cursor()

			print("[*] Database cursor created")

		except ms.Error as mer:
			if mer.errno == errorcode.ER_ACCESS_DENIED_ERROR:
				print("Something is wrong with your user name or password")
			elif mer.errno == errorcode.ER_BAD_DB_ERROR:
				print("Database does not exist")
			else:
				print("[*] Maxed out connections to database")
	
				
		
	def end(self):
		self.__db.close()


	def get_id(self, sensor):
		query = f"SELECT sensor_id FROM sensor WHERE location = '{sensor}'"
		self.__cursor.execute(query)

		if result := self.__cursor.fetchall():
			sensor_id = int(result[0][0])
			return sensor_id
		else:
			return self.add_sensor(sensor)

		
	def add_reading(self, b_pressure, ambient_light, temperature, sensor):
		sensor_id = self.get_id(sensor)
		
		query_insert = f"INSERT INTO readings (b_pressure, ambient_light, temperature, sensor_id) values ({b_pressure}, {ambient_light}, {temperature}, {sensor_id});"
		self.__cursor.execute(query_insert)

		# query_insert_r = f"INSERT INTO readings (b_pressure, ambient_light, temperature) values ({b_pressure}, {ambient_light}, {temperature});"
		# self.__cursor.execute(query_insert_r)
		
		# reading_id = self.__cursor.lastrowid
		# query_insert_m = f"insert into sensor_to_readings(sensor_id, reading_id) values ({sensor_id}, {reading_id});"
		# self.__cursor.execute(query_insert_m)
		
		self.__db.commit()
		
		print(f"[*] Reading for {sensor} added")


	def add_sensor(self, sensor):
		query = f"insert into sensor(location) values ('{sensor}')";
		
		self.__cursor.execute(query)
		sensor_id = self.__cursor.lastrowid
		
		self.__db.commit()
		
		print(f"[*] New sensor '{sensor}' added")
		
		return int(sensor_id)


