FROM tomcat:latest
ADD BloggingApp/target/BloggingApp*.war /usr/local/tomcat/webapps/BloggingApp.war
ADD setenv.sh /usr/local/tomcat/bin/setenv.sh
