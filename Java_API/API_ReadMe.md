# Readme for Java APIs  
## Supported functions  
### 1. Get all details for all the sensors  
This function will list all the details for the supported sensors.  
It is using the path:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/all_sensor  ```  
The information will looks like this:  
``` 
[
    {
    "id":1,
    "location":"pywierden",
    "date":"2020-12-22T20:17:09.000+00:00"
    }
]
```
  
To check the newest sensors from this [link](https://wsgroup4.herokuapp.com/weather_station_java_api/all_sensor).  
The reason why this function is not requiring key verify is because user should have right to know whether this resful api can offer records in the location they want.
  
---
    
### 2. Get all records of readings
This function will list all the records of readings in database with their full details.  
It is using the path with parameter \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/all_readings?key=<key> ```  
For example:
> https://wsgroup4.herokuapp.com/weather_station_java_api/all_readings?key=saxion_group_4
 
The information will looks like this:  
``` 
[
    {
    "id":1,
    "sensor_id":1,
    "ambient_light":3.0,
    "temperature":20.4,
    "date":"2020-12-22T20:17:19.000+00:00",
    "a_pressure":987.0
    }
]
```  

To check the newest sensors from this [link](https://wsgroup4.herokuapp.com/weather_station_java_api/all_readings?key=saxion_group_4).    
  
---  
  
### 3. Get newest records of readings for all the sensors
This function will list the newsst records of readings in database with their full details for each sensor.  
It is using the path with parameter \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/now_sensor?key=<key> ```  
For example:
> https://wsgroup4.herokuapp.com/weather_station_java_api/now_sensor?key=saxion_group_4
 
The information will looks like this:  
``` 
{ 
    "time_requested":"Thu Jan 07 12:31:05 UTC 2021", 
    "list": 
    [ 
        { 
        "location": "pywierden", 
        "time": "2020-12-22 08:17:19", 
        "temperature": 20.4, 
        "ambient_light": 3.0, 
        "b_pressure": 987.0 
        },
        
        { 
        "location": "pygarage", 
        "time": "2020-12-22 08:17:19", 
        "temperature": 12.8, 
        "ambient_light": 0.0, 
        "b_pressure": 984.5 
        },
        
        { 
        "location": "pygronau", 
        "time": "2020-12-22 08:17:19", 
        "temperature": -3.3, 
        "ambient_light": 0.0, 
        "b_pressure": 983.0 
        },
        
        { "location": "pysaxion", 
        "time": "2020-12-22 08:17:19", 
        "temperature": 22.1, 
        "ambient_light": 0.0, 
        "b_pressure": 982.5 
        } 
    ] 
}
```  
  
To check the newest sensors from this [link](https://wsgroup4.herokuapp.com/weather_station_java_api/now_sensor?key=saxion_group_4)      
  
---  
  
### 4. Get all records of readings for one sensor which id number is known.  
This function will list all the records of readings in database with their full details.  
It is using the path with parameter \<id\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_full?id=<id>&key=<key>  ```  
For example: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_full?id=1&key=saxion_group_4 

The information will looks like this:  
``` 
{ 
    "sensor":"pywierden", 
    "list": 
    [ 
        { 
        "time": "2020-12-22 08:17:19", 
        "temperature": 20.4, 
        "ambient_light": 3.0, 
        "b_pressure": 987.0 
        }
    ]
}
```  

---  
    
### 5. Get ambient light records of readings for one sensor which id number is known.  
This function will list all the records of readings in database with their ambient light value only.  
It is using the path with parameter \<id\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_ambient_light?id=<id>&key=<key>  ```  
For example: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_ambient_light?id=1&key=saxion_group_4
 
The information will looks like this:  
``` 
{ 
"sensor":"pywierden", 
"list": 
    [ 
        { 
        "time": "2020-12-22 08:17:19", 
        "ambient_light": 3.0 
        }
    ]
}
```  

---  
  
### 6. Get temperature records of readings for one sensor which id number is known.  
This function will list all the records of readings in database with their temperature value only.  
It is using the path with parameter \<id\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_temperature?id=<id>&key=<key>  ```  
For example: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_temperature?id=1&key=saxion_group_4 
 
The information will looks like this:  
``` 
{ 
"sensor":"pywierden", 
"list": 
    [ 
        { 
        "time": "2020-12-22 08:17:19", 
        "temperature": 20.4 
        }
    ]
}
```  

---  
  
### 7. Get pressure records of readings for one sensor which id number is known.  
This function will list all the records of readings in database with their pressure value only.  
It is using the path with parameter \<id\> and \<key\>:  
 ``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_pressure?id=<id>&key=<key>  ```  
For example: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_pressure?id=1&key=saxion_group_4

The information will looks like this:  
``` 
 { 
 "sensor":"pywierden", 
 "list": 
     [ 
         { 
         "time": "2020-12-22 08:17:19", 
         "b_pressure": 987.0 
         }
     ]
 }
```  
 
---  
     
### 8. Get all records of readings for one sensor which location is known.  
This function will list all the records of readings in database with their full details.  
It is using the path with parameter \<place\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_place_full?place=<place>&key=<key>  ```  
For example: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_place_full?place=pygarage&key=saxion_group_4  
  
The information will looks like this:  
``` 
 { 
 "sensor":"pygarage", 
 "list": 
    [ 
        { 
        "time": "2020-12-22 08:17:19", 
        "temperature": 12.8, 
        "ambient_light": 0.0, 
        "b_pressure": 984.5 
        }
    ]
 }
```  
   
---  
     
### 9. Get ambient light records of readings for one sensor which location is known.
This function will list all the records of readings in database with their ambient light value only.  
It is using the path with parameter \<place\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_place_ambient_light?place=<place>&key=<key>  ```  
This function works same as the one using id number as function 5.  
  
---  
  
### 10. Get temperature records of readings for one sensor which location is known.
This function will list all the records of readings in database with their temperature value only.  
It is using the path with parameter \<place\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_id_ambient_light?place=<place>&key=<key>  ```  
This function works same as the one using id number as function 6.  
  
---  
  
### 11. Get pressure records of readings for one sensor which location is known.
This function will list all the records of readings in database with their pressure value only.  
It is using the path with parameter \<place\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/reading_from_place_pressure?place=<place>&key=<key>  ```  
This function works same as the one using id number as function 7.  
  
---  
  
### 12. Get newest records of readings for one sensor which id number is known in last given days.
This function will list the newest records of readings of required one sensor with their all details limited by given number of days.  
It is using the path with parameter \<day\>, \<id\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/recent_readings_1sensor_id?day=<day>&id=<id>&key=<key>  ```  
For example: 
> http://wsgroup4.herokuapp.com/weather_station_java_api/recent_readings_1sensor_id?day=30&id=1&key=saxion_group_4 
  
The information will looks like this:  
``` 
{ 
"sensor":"pywierden", 
"list": 
    [ 
        { 
        "time": "2020-12-22 08:17:19", 
        "temperature": 20.4, 
        "ambient_light": 3.0, 
        "b_pressure": 987.0 
        }
    ]
 }
```  
  
---  
  
### 13. Get newest records of readings for one sensor which location is known in last given days.
This function will list the newest records of readings of required one sensor with their all details limited by given number of days.  
It is using the path with parameter \<day\>, \<place\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/recent_readings_1sensor?day=<day>&place=<place>&key=<key>  ```  
This function works same as the one using id number as function 12.   
  
---  
  
### 14. Get newest records of readings for all supported sensors in last given days.
This function will list the newest records of readings of required one sensor with their all details limited by given number of days.  
It is using the path with parameter \<day\> and \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/recent_readings?day=<day>&key=<key>  ```  
This function works similar as function 12.   
  
---  
  
### 15. Get locations of all supported sensors.
This function will list the location of all sensors stored in the database.  
It is using the path with parameter \<key\>:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/all_sensor_location?key=<key>  ```  
For example:
> https://wsgroup4.herokuapp.com/weather_station_java_api/all_sensor_location?key=saxion_group_4  

The information will looks like this:  
``` 
{
"list":
    [ 
        { 
        "sensor_id": "1", 
        "location": "pywierden" 
        }, 
        
        { 
        "sensor_id": "2", 
        "location": "pygarage" 
        }, 
        
        { 
        "sensor_id": "3", 
        "location": "pygronau" 
        }, 
        
        { 
        "sensor_id": "4", 
        "location": "pysaxion" 
        } 
    ] 
}
```
To check the newest sensors from this [link](https://wsgroup4.herokuapp.com/weather_station_java_api/all_sensor_location?key=saxion_group_4).  
