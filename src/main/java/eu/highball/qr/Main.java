package eu.highball.qr;

public class Main {

	public static void main(String[] args) {
        int port = 8080;
        new WebServer(port);
        System.out.println("Server started on port " + port);
	}
}



