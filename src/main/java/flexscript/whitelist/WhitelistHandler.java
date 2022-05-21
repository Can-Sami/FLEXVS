package flexscript.whitelist;

import flexscript.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WhitelistHandler implements Runnable{

    public volatile static String Raw;

    public static void getPastes() throws IOException {
        URL u = new URL("https://gist.githubusercontent.com/Can-Sami/210c34128f9fc13f5aabe819d30491ad/raw/09930bd0081f6ed4d29f71a83dc6b603f1fffff4/gistfile1.txt");
        URLConnection conn = u.openConnection();
        conn.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        conn.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            buffer.append(inputLine);
        in.close();
        System.out.println(buffer.toString());
        Raw = buffer.toString();
        Main.tested = true;
    }

    @Override
    public void run() {
        try {
            getPastes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
