# user-management
This repo contains spring-boot micro-service to manage users for an application.

#### Docker Commands
##### Start MySql Container (downloads image if not found)
``
docker run --detach --name user-mysql -p 6604:3306 -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=user -e MYSQL_USER=admin -e MYSQL_PASSWORD=admin -d mysql
``

##### Interact with Database (link to user-mysql container) with mysql client
``
docker run -it --link user-mysql:mysql --rm mysql sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"'
``