# Comic information for ALBO

This is the challenge requested for ALBO.
## Requirements

+ Java 11
+ MongoDB ([How to install it](https://docs.mongodb.com/manual/installation/)?)


## Configuration

1. In the **src/main/resources/application.properties** you can edit your database configuration and the cron for the Task, now is configured to start the execution once a day.
2. Execute **assemble.sh** to build the project.
3. Execute **avengers.sh** to start the application.

The applications starts running on port 80.

## Usage

Retrieves Ironman secondary characters - **GET**: /marvel/characters/ironman.  
Retrieves Ironman collaborators - **GET**: /marvel/colaborators/ironman.   
Retrieves Captain America secondary characters - **GET**: /marvel/characters/capamerica.    
Retrieves Captain America collaborators - **GET**: /marvel/colaborators/capamerica