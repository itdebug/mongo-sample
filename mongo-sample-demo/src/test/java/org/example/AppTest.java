package org.example;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    private static final int MAX_POOL_SIZE = 1;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testCRUD()
    {
        MongoCredential credential =
                MongoCredential.createCredential("crm", "crm", "crm".toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(
                                Arrays.asList(new ServerAddress("172.16.0.126:27018"),
                                        new ServerAddress("172.16.0.126:27020"),
                                        new ServerAddress("172.16.0.126:27018"))))
                .build();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("crm");
            MongoCollection<Document> collection = database.getCollection("counter");
            Document document = new Document("name", "John Doe")
                    .append("age", 30)
                    .append("email", "johndoe@example.com");
            FindIterable<Document> documents = collection.find();
            Document first = documents.first();
            System.out.println(first);
        }
    }

    public void testCrudException() {
        MongoCredential credential =
                MongoCredential.createCredential("crm", "crm", "crm".toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToConnectionPoolSettings(builder -> builder.maxSize(MAX_POOL_SIZE))
                .applyToClusterSettings(builder ->
                        builder.hosts(
                                Arrays.asList(new ServerAddress("172.16.0.126:27018"),
                                        new ServerAddress("172.16.0.126:27020"),
                                        new ServerAddress("172.16.0.126:27018"))))
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("crm");
            MongoCollection<Document> collection = database.getCollection("counter");
            // Perform some operations using the connection pool
            for (int i = 0; i < 20; i++) {
                try (MongoCursor<Document> cursor = collection.find().iterator()) {
                    while (cursor.hasNext()) {
                        System.out.println(cursor.next());
                    }
                } catch (MongoTimeoutException e) {
                    System.err.println("Failed to get a connection from the pool: " + e.getMessage());
                }
            }
        }
    }

    public void testCrudException2() {

    }


}
