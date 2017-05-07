FROM tomcat:latest
ADD *.war /usr/local/tomcat/webapps/cmad.war
ADD setenv.sh /usr/local/tomcat/bin/setenv.sh
