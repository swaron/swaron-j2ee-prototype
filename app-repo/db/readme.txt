flash database commands:

for default h2 database
mvn liquibase:update -Dliquibase.dropFirst=true

for postgresql
mvn liquibase:update -Dliquibase.dropFirst=true -Ppsql

for mysql
mvn liquibase:update -Dliquibase.dropFirst=true -Pmysql

custom database:
mvn liquibase:update -Dliquibase.dropFirst=true -Dliquibase.contexts=test -Djdbc.driverClassName=com.mysql.jdbc.Driver -Djdbc.url=jdbc:mysql://172.16.113.210:3306/ctn_dev -Djdbc.username=ctn_dev -Djdbc.password=ctn_dev

dump sql script:
mvn liquibase:updateSQL -Dliquibase.dropFirst=true -Dliquibase.contexts=test -Djdbc.driverClassName=com.mysql.jdbc.Driver -Djdbc.url=jdbc:mysql://172.16.113.210:3306/ctn_dev -Djdbc.username=ctn_dev -Djdbc.password=ctn_dev