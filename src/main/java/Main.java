import java.io.IOException;
import java.io.InputStream;
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
    String _200OKresponseString = "HTTP/1.1 200 OK\r\n\r\n";
    String _404NOTFOUNDresponseString = "HTTP/1.1 404 Not Found\r\n\r\n";

    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");
      OutputStream clientOutputStream = clientSocket.getOutputStream();
      InputStream clientInputStream = clientSocket.getInputStream();

      String clientRequestString = new String(clientInputStream.readAllBytes());
      System.out.println(clientRequestString);
      String requestPath = clientRequestString.split("\r\n\r\n")[0].split(" ")[1];

      System.out.println(requestPath);

      if (requestPath.equals("/")) {
        System.out.println("Responded 200 OK");
        clientOutputStream.write(_200OKresponseString.getBytes());
      } else {
        System.out.println("Responded 404 NOT FOUND");
        clientOutputStream.write(_404NOTFOUNDresponseString.getBytes());
      }
      serverSocket.close();
      clientSocket.close();
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
      return;
    }

  }
}
