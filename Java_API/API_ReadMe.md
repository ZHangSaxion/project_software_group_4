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
    "date":"2020-12-22 08:17:19"
    }
]
```
  
To check the newest sensors from this [link](https://wsgroup4.herokuapp.com/weather_station_java_api/all_sensor).  
The reason why this function is not requiring key verify is because user should have right to know whether this resful api can offer records in the location they want.
  
---
    
### 2. Get records of readings
This function will list all the records of readings in database with their full details.  
It is using the path with parameter **\<req_type\>**, **\<id\>**, and **\<key\>**:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=<req_type>&id=<id>&key=<key> ```  
The **req_type** can be the following:  
> 0 for full detail,  
1 for ambient light only,  
2 for temperature only,  
3 for pressure only 

And the "id" can be:
 > -1 for all sensors  
 an integer of a specific sensor id
 
**For example**:
> https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=0&id=-1&key=saxion_group_4
 
The information will looks like this:  
``` 
[
    {
    "id":1,
    "sensor_id":1,
    "ambient_light":3.0,
    "temperature":20.4,
    "date":"2020-12-22 08:17:19",
    "a_pressure":987.0
    }
]
```  
  
**For example**: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=0&id=1&key=saxion_group_4 

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
  
**For example**: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=1&id=1&key=saxion_group_4
 
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
  
**For example**: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=2&id=1&key=saxion_group_4
 
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
  
**For example**: 
> https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=3&id=1&key=saxion_group_4

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
  
### 3. Get recently records
This function will list the newest records of readings in database with their full details for required sensor.  
It is using the path with parameter  **\<day\>**, **\<id\>**, and **\<key\>**:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/recent?day=<day>&id=<id>&key=<key> ```  

**For example**:
> https://wsgroup4.herokuapp.com/weather_station_java_api/recent?day=30&id=-1&key=saxion_group_4
 
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
  
**For example**: 
> http://wsgroup4.herokuapp.com/weather_station_java_api/recent?day=30&id=1&key=saxion_group_4 
  
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
  
### 4. Get locations of all supported sensors.
This function will list the location of all sensors stored in the database.  
It is using the path with parameter **\<key\>**:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/all_location?key=<key>  ```  
For example:
> https://wsgroup4.herokuapp.com/weather_station_java_api/all_location?key=saxion_group_4  

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
  
---  
  
### 5. Get newest records of readings for all the sensors
This function will list the newsst records of readings in database with their full details for each sensor.  
It is using the path with parameter **\<key\>**:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/newest?key=<key> ```  
For example:
> https://wsgroup4.herokuapp.com/weather_station_java_api/newest?key=saxion_group_4
 
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
  