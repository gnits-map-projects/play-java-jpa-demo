Sample play java jpa demo app with mysql as database


The mysql connection strings are configured in application.conf file. Please replace the password and username in the conf file along with database name. 

Install mysql 8.0* version or above and also use workbench for easy access to database through UI.

# Running application 
Run the application just by "sbt run" in the root folder of the project. And then access localhost:9000 from browser to access UI. You can update the person city by visitng  localhost:9000/persons/update/:name (where replace :name with correct name to update)
