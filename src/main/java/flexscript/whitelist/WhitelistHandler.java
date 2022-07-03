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
        URL u = new URL("https://gist.githubusercontent.com/Can-Sami/a20c4222ed5d6442efbf93ee26d95c41/raw/a27c30d16e0fc08048b128985c06d201ad4674e8/gistfile1.txt");
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
