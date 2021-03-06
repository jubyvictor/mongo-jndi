mongo-jndi
==========
This is a simple plugin that allows you to configure a MongoClient as a JNDI resource. This plugin
fulfills the javax.naming.spi.ObjectFactory contract, thereby allowing the
mongodb driver to be used for configuring JNDI resources at the container
level. Typically this would be copied over to the /lib directory of your application server.

Please note that I have only tested this on a Tomcat 7 server.
 
Please refer to <a href="http://api.mongodb.org/java/2.11.2/com/mongodb/MongoClientURI.html">MongoClientURI documentation</a> 
for the various options that can be passed in via the URI.
  
For e.g. In an Apache Tomcat environment, you maybe able to define a Resource element  in the context.xml as shown below.
  

    <Resource name="mongodb/MongoClient" 
  	    auth="Container"
  		type="com.mongodb.MongoClient"
  	    factory="com.mongodb.MongoClientObjectFactory" 
  	    mongoClientURI="mongodb://localhost:27017,some.other.host:28017,.../dbName?safe=true;option1=someValue"/>

  
In case you are using Spring for your application, you can refer to the
mongoclient resource using the jee namespace support. e.g


    <jee:jndi-lookup id="mongoClient" jndi-name="mongodb/MongoClient"/>

  
and either use autowiring or setter/constructor injection to provide a
reference to the mongoClient

    <bean id="mongoClientDependentBean" class="com.foo.bar.MongoRepo">
  	    <property name="mongoClient" ref="mongoClient"/>
         ...
         ...
	</bean>	 	 
