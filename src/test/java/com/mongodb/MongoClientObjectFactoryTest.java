package com.mongodb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;




public class MongoClientObjectFactoryTest {

	private MongoClientObjectFactory mongoClientObjectFactory;
	private Reference referenceObj;	
	private Reference misconfiguredRefObj;	
	

	@Before
	public void setup() {
		mongoClientObjectFactory = new MongoClientObjectFactory();
		referenceObj = new Reference(
				"com.mongodb.MongoClient",
				new StringRefAddr("mongoClientURI",
						"mongodb://127.0.0.1:27017,127.0.0.1:37017,127.0.0.1:47017/jndiTest?safe=true"));
		misconfiguredRefObj  = new Reference(
				"com.mongodb.MongoClient",
				//Note the incorrect property name
				new StringRefAddr("mongoURIForClients",
						"mongodb://127.0.0.1:27017,127.0.0.1:37017,127.0.0.1:47017/jndiTest?safe=true"));
	}

	@After
	public void tearDown() {
		mongoClientObjectFactory = null;
	}

	
	@Test
	public void testCreationUsingValidUri() throws Exception {
		Object retObj = mongoClientObjectFactory.getObjectInstance(referenceObj, null, null, null);
		Assert.assertNotNull(retObj);
		Assert.assertTrue(retObj instanceof MongoClient);
		MongoClient client = (MongoClient) retObj;		
		Assert.assertTrue(client.getAllAddress().size() == 3);
		DB db = client.getDB("jndiTest");
		assertNotNull(db);
	}
	
	@Test
	public void testCreationUsingInvalidUri() throws Exception {
		try{
			mongoClientObjectFactory.getObjectInstance(misconfiguredRefObj, null, null, null);
			fail("Should fail, as the ref obj was misconfigured");
		}
		catch(MongoException mongoException){
			//All is well, an exception is welcome and expected !
		}
	}

}
