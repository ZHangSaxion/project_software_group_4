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
It is using the path with parameter **\<req_type\>**, **\<day_from\>**, **\<day_to\>**, **\<id\>**, and **\<key\>**:  
``` https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=<req_type>&day_from=<day_from>&day_to=<day_to>&id=<id>&key=<key> ```  
The **req_type** can be the following:  
> 0 for full detail,  
1 for ambient light only,  
2 for temperature only,  
3 for pressure only 

The **day_from** is:  
> an integer of the number of days between the beginning date and today  

The **day_to** is:  
> an integer of the number of days between the last date and today

And the **id** can be:
 > -1 for all sensors  
 an integer of a specific sensor id
  
**For example**:
> https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=0&day_from=30&day_to=25&id=-1&key=saxion_group_4
 
The information will looks like this:  
``` 
{ 
    { 
    "sensor_id": "1", 
    "date": "2020-12-22 08:17:19", 
    "temperature": 20.4, 
    "ambient_light": 3.0, 
    "b_pressure": 987.0 
    }
}
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
> https://https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=1&day_from=30&day_to=25&id=-1&key=saxion_group_4
 
The information will looks like this:  
``` 
[
    { 
    "sensor_id": "1", 
    "date": "2020-12-22 08:17:19", 
    "ambient_light": 3.0 
    }
]
```  
  
**For example**: 
> https://https://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=2&day_from=30&day_to=25&id=-1&key=saxion_group_4
 
The information will looks like this:  
``` 
[
    { 
    "sensor_id": "1", 
    "date": "2020-12-22 08:17:19", 
    "temperature": 20.4 
    }
]
```  
  
**For example**: 
> https://http://wsgroup4.herokuapp.com/weather_station_java_api/readings?req_type=3&day_from=30&day_to=25&id=-1&key=saxion_group_4

The information will looks like this:  
``` 
[
    { 
    "sensor_id": "1", 
    "date": "2020-12-22 08:17:19", 
    "b_pressure": 987.0 
    }
]
```  
   
---  
  
### 3. Get locations of all supported sensors.
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
  
### 4. Get newest records of readings for all the sensors
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
    
---  
  
### 5. Get averages readings
This function will list the newsst records of readings in database with their full details for each sensor.  
It is using the path with parameter  **\<day_from\>**, **\<day_to\>**, **\<id\>**, and **\<key\>**:   
``` https://wsgroup4.herokuapp.com/weather_station_java_api/average?day_from=<day_from>&day_to=<day_to>&id=<id>key=<key> ```  
The **day_from** is:  
> an integer of the number of days between the beginning date and today  

The **day_to** is:  
> an integer of the number of days between the last date and today

And the **id** can be:
 > -1 for all sensors  
 an integer of a specific sensor id
  
For example:
> https://wsgroup4.herokuapp.com/weather_station_java_api/average?day_from=30&day_to=25&id=-1&key=saxion_group_4
 
The information will looks like this:  
``` 
[
    {
    "sensor_id": 1, 
    "ambient_light": 44.75905118601748, 
    "temperature": 18.330836454431964, 
    "b_pressure": 1016.5539950062422
    },
    {
    "sensor_id": 2, 
    "ambient_light": 8.950700570835496, 
    "temperature": 5.3892579138557375, 
    "b_pressure": 1015.4815775817333
    },
    {
    "sensor_id": 3, "
    ambient_light": 6.57034632034632, 
    "temperature": -3.2022727272727343, 
    "b_pressure": 1012.881764069264
    },
    {"sensor_id": 4, 
    "ambient_light": 43.081740976645435, 
    "temperature": 23.772452229299283, 
    "b_pressure": 1013.1066878980891
    } 
]
```  
   
For example:
> https://wsgroup4.herokuapp.com/weather_station_java_api/average?day_from=30&day_to=25&id=1&key=saxion_group_4
 
The information will looks like this:  
``` 
[
    {
    "sensor_id": 1, 
    "ambient_light": 44.75905118601748, 
    "temperature": 18.330836454431964, 
    "b_pressure": 1016.5539950062422
    } 
]
```  
      