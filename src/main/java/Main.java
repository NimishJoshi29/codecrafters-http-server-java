import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible
    // when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");

      OutputStream clientOutputStream = clientSocket.getOutputStream();
      InputStream clientInputStream = clientSocket.getInputStream();

      respond(clientOutputStream, clientInputStream);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void respond(OutputStream clientOutputStream, InputStream clientInputStream) throws IOException {
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
      } else {
        System.out.println("Responded 404 NOT FOUND");
        clientOutputStream.write(_404NOTFOUNDresponseString.getBytes());
      }
    }
    clientOutputStream.flush();
  }
}
