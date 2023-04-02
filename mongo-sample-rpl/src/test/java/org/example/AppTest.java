package org.example;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.bson.Document;
import org.example.util.SymmetricUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
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
    public void testApp() throws Exception
    {
        String decode = SymmetricUtil.decode("Mp9MNqjjPtHyz2tZnW7YHcILXJC+rcWVPDPny4dKlKnQryxx51m0b/nW/os65Df6zMb0KsYuGBgFHBm546Elr+fXICnH4rt+tZy1eSwEVliSTH5zKKJNh7jmPjr//RqzJOr0B2a3TFg4ON1hro/jLA==");
        final MongoClientURI uri = new MongoClientURI(decode +
                        "&readPreference=secondaryPreferred" +
                        "&connectTimeoutMS=300000" +
                        "&minpoolsize=1" +
                        "&waitqueuemultiple=1" +
                        "&waitqueuetimeoutms=1" +
                        "&connecttimeoutms=1" +
                        "&maxpoolsize=1");

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    final MongoClient newClient = new MongoClient(uri);
                    MongoDatabase database = newClient.getDatabase("crm");
                    MongoCollection<Document> collection = database.getCollection("counter");
                    // Perform some operations using the connection pool
                    for (int i = 0; i < 2000000; i++) {
                        FindIterable<Document> documents = collection.find();
//                        System.out.println(documents);
                    }
                    newClient.close();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
    }
}
