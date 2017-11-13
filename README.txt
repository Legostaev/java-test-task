The program uses the apache derby database and stores the data in RAM. You can change this behavior by changing the data source.

<!-- this is the JDBC data source which uses an in-memory only Apache Derby database -->
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="org.apache.derby.jdbc.EmbeddedDriver"/>
    <property name="url" value="jdbc:derby:memory:security;create=true"/>
    <property name="username" value=""/>
    <property name="password" value=""/>
</bean>

In the file client.hbm.xml you can see entity configuration. They are created in the database when the application is started.

In this bean creates a connection pool to the external service. (tls-test.scnetservices.ru). 
10 - maximum number of open connections. Connections are shared between clients.
 

<bean id="accountDetailsManager"
      class="rest.service.foreign.manager.AccountDetailsManagerImpl">
    <constructor-arg value="tls-test.scnetservices.ru" />
    <constructor-arg value="9000" />
    <constructor-arg value="10" />
</bean>

Error handling is made in a separate controller.

rest.service.resource.exception.AccountExceptionHandlerController


The system is password protected. When you sign in, you must enter

login    - admin
password - admin

The field for adding a new user can be logged on by a new user.

rest.service.resource.AccountController - this class contains CRUD operations for working with accounts.
Entry point /accounts

When you using method POST you should be send 
Content-Type: application/x-www-form-urlencoded

In order to add a new account, you need to perform an operation
POST /accounts/add 

login    - user login
password - user password

return Account

In order to change the password, you need to perform an operation
POST /accounts/update

accountId - account id
password  - new password

In order to delete accounts, you need to perform an operation
GET /accounts/delete

accountId - list of account id

In order to get the list of accounts, you need to perform an operation
GET /accounts/ 

from  - start offset
count - count of account

