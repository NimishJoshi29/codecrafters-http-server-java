import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {

    System.out.println("Logs from your program will appear here!");

    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      // clientSocket = serverSocket.accept(); // Wait for connection from client.
      // System.out.println("accepted new connection");

      // OutputStream clientOutputStream = clientSocket.getOutputStream();
      // InputStream clientInputStream = clientSocket.getInputStream();

      // respond(clientOutputStream, clientInputStream);

    } catch (IOException e) {
      e.printStackTrace();
    }

    Thread[] threadPool = new Thread[10];
    for (int i = 0; i < threadPool.length; i++) {
      threadPool[i] = new Thread(new Server(serverSocket));
      threadPool[i].start();
    }

    while (true) {
      for (int i = 0; i < threadPool.length; i++) {
        if (!threadPool[i].isAlive()) {
          threadPool[i] = new Thread(new Server(serverSocket));
          threadPool[i].start();
        }
      }
    }
  }

  public static void respond(OutputStream clientOutputStream, InputStream clientInputStream) throws IOException {
    String _200OKresponseString = "HTTP/1.1 200 OK\r\n\r\n";
    String _404NOTFOUNDresponseString = "HTTP/1.1 404 Not Found\r\n\r\n";

    BufferedReader reader = new BufferedReader(new InputStreamReader(clientInputStream));

    String clientRequestString = reader.readLine();
    String requestPath = clientRequestString.split(" ")[1];

    System.out.println(requestPath);

    if (requestPath.length() == 1 && requestPath.equals("/")) {
      System.out.println("Responded 200 OK");
      clientOutputStream.write(_200OKresponseString.getBytes());
    } else if (requestPath.length() > 1) {
      String[] requestTokens = requestPath.split("/");

      if (requestTokens[1].equals("echo")) {
        String echoResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n" + //
            "Content-Length: " + requestTokens[2].length() + "\r\n" + //
            "\r\n" + //
            requestTokens[2];
        clientOutputStream.write(echoResponse.getBytes());
      } else if (requestTokens[1].equals("user-agent")) {

        List<Byte> inputBytesList = new ArrayList<Byte>();
        while (reader.ready())
          inputBytesList.add((byte) reader.read());

        byte[] inputBytesArray = new byte[inputBytesList.size()];
        int i = 0;
        for (byte b : inputBytesList)
          inputBytesArray[i++] = b;

        String fullRequestString = new String(inputBytesArray);

        String userAgent = fullRequestString.substring(fullRequestString.indexOf("User-Agent")).split("\r")[0]
            .split(":")[1].stripLeading();

        String userAgentEchoResponseString = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
            + userAgent.length() + "\r\n\r\n" + userAgent;

        clientOutputStream.write(userAgentEchoResponseString.getBytes());

        System.out.println("Responded 200 OK with user-agent info");
      } else {
        System.out.println("Responded 404 NOT FOUND");
        clientOutputStream.write(_404NOTFOUNDresponseString.getBytes());
      }
    }
    clientOutputStream.flush();
  }
}

class Server implements Runnable {

  private ServerSocket serverSocket;

  Server(ServerSocket socket) {
    serverSocket = socket;
  }

  @Override
  public void run() {
    try {
      Socket clientSocket = serverSocket.accept();
      System.out.println("accepted new connection");

      OutputStream clientOutputStream = clientSocket.getOutputStream();
      InputStream clientInputStream = clientSocket.getInputStream();

      Main.respond(clientOutputStream, clientInputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
