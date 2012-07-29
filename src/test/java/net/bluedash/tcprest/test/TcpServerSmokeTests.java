package net.bluedash.tcprest.test;

import net.bluedash.tcprest.server.SimpleTcpRestServer;
import net.bluedash.tcprest.server.TcpRestServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

/**
 * User: weli
 * Date: 7/29/12
 */
public class TcpServerSmokeTests {

    private TcpRestServer tcpRestServer;
    private Socket clientSocket;


    @Before
    public void startTcpRestServer() throws IOException {
        tcpRestServer = new SimpleTcpRestServer(8001);
        tcpRestServer.up();
        clientSocket = new Socket("localhost", 8001);
    }

    @After
    public void stopTcpRestServer() throws IOException {
        tcpRestServer.down();
        clientSocket.close();
    }

    @Test
    public void testSimpleClient() throws IOException {
        tcpRestServer.addResource(HelloWorldRestlet.class);

        // Add HTTP Client code to access the port
        // net.bluedash.tcprest.test.unit.sandbox.HelloWorldRestlet/helloWorld
        // and the expected result should be 'Hello, world!'
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer.println("net.bluedash.tcprest.test.HelloWorldRestlet/helloWorld");
        writer.flush();

        String response = reader.readLine();
        assertEquals("Hello, world!", response);

        writer.println("net.bluedash.tcprest.test.HelloWorldRestlet/test2");
        writer.flush();
        assertEquals("test2", reader.readLine());
    }

}
