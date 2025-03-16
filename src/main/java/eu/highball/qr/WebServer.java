package eu.highball.qr;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer {

    protected WebServer(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new FileHandler());
            server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static class FileHandler implements HttpHandler {

        private Map<String, String> getParams(String query) {
            Map<String, String> result = new HashMap<>();
            String[] q = new String[0];
            if (query != null) {
                if (query.contains("&")) q = query.split("&");
                else if (query.contains("=")) q = new String[]{query};
            }
            for (String vp : q) {
                String[] par = vp.split("=");
                String key = par[0];
                String val = par.length < 2 ? "" : par[1];
                result.put(key, val);
            }
            return result;
        }

        @Override
        public void handle(HttpExchange t) {
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                URI uri = t.getRequestURI();
                Map<String, String> params = getParams(uri.getQuery());
                String code = params.get("code");
                String qrsize = params.get("qrsize");
                String svg = "";
                if (code != null) {
                    QrGenerator qrGenerator = new QrGenerator();
                    svg = qrGenerator.svg(code,
                            qrsize == null ? 100 : Integer.parseInt(qrsize)
                    );
                }
                InputStream is = classloader.getResourceAsStream("public/index.html");
                byte[] theBytes;
                theBytes = new byte[is.available()];
                is.read(theBytes, 0, is.available());
                String html = new String(theBytes);
                html = html.replace("<!--svg-->", svg);
                if (code != null) {
                    html = html.replace("<input name=\"code\" value=\"\">", "<input name=\"code\" value=\"" + code + "\">");
                }
                if (qrsize != null) {
                    html = html.replace("<input name=\"qrsize\" value=\"100\">", "<input name=\"qrsize\" value=\"" + qrsize + "\">");
                }
                theBytes = html.getBytes();
                Headers h = t.getResponseHeaders();
                h.set("Content-Type", "text/html");
                t.sendResponseHeaders(200, theBytes.length);
                try (OutputStream os = t.getResponseBody()) {
                    os.write(theBytes);
                }
            } catch (Exception ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
