import java.io.*;
import java.net.*;

public class WebServer {
    public static void main(String[] args) throws Exception {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("The web server is listening on port 8080");
            while(true) {
                Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                PrintWriter pw = new PrintWriter(os, true);
                String input = br.readLine();
                String fileName = input.split(" ")[1];

                Runnable server = () -> {
                    File file;
                    BufferedReader bReader;
                    String output;
                    try {
                        if(fileName.equals("/hello.html")) {
                            file = new File("www/hello.html");
                            bReader = new BufferedReader(new FileReader(file));
                            pw.print("HTTP/1.1 200 OK\n" + "Content-type: text/html\n" + "Content-length: 124\n\n");
                            output = bReader.readLine();

                            while(output != null) {
                                pw.print(output + "\n");
                                output = bReader.readLine();
                            }
                            System.out.println("Sent " + fileName + ".");
                            pw.close();
                            bReader.close();
                        } else {
                            file = new File("www/error404.html");
                            bReader = new BufferedReader(new FileReader(file));
                            pw.print("HTTP/1.1 404 Not Found\n" + "Content-type: text/html\n" + "Content-length: 126\n\n");
                            output = bReader.readLine();

                            while(output != null) {
                                pw.print(output + "\n");
                                output = bReader.readLine();
                            }
                            System.out.println(fileName + " not found.");
                            pw.close();
                            bReader.close();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                };
                Thread thread = new Thread(server);
                thread.start();
            }
        }
    }
}
