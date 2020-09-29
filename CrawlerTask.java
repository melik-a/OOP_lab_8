import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.LinkedList;

/**
 * WebCrawler
 */

public class CrawlerTask implements Runnable {
    
    private URLPool pool;
    private int serverTimeOut;
    private static final String HREF_TAG = "<a href=\"";

    public CrawlerTask(URLPool specifiedPool, int timeout){
        this.pool = specifiedPool;
        this.serverTimeOut = timeout;
    }

    // private void getSites(String initURL, int maxDepth) throws Exception{
        
    // }

    @Override
    public void run() {
        int webPort = 80;
        URLDepthPair URLDepthPair;
        int currDepth;
        //while(!pool.isNeedToVisitListEmpty()){
        while(true){
            URLDepthPair = pool.getPair(); 
            currDepth = URLDepthPair.getDepth();
            try{
                Socket sock = new Socket(URLDepthPair.getHost(), webPort);
                sock.setSoTimeout(serverTimeOut); // Time-out
                OutputStream os = sock.getOutputStream();
                PrintWriter writer = new PrintWriter(os, true);
                System.out.println("Connected to: " + URLDepthPair.getURLAddress());   
                writer.println("GET " + URLDepthPair.getPath() + " HTTP/1.1");
                writer.println("Host: " + URLDepthPair.getHost());
                writer.println("Connection: close");
                writer.println();
                InputStream is = sock.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while((line = br.readLine()) != null){
                    //System.out.println(line);
                    if(line.indexOf(HREF_TAG) != -1){
                        int startIndURL = line.indexOf(HREF_TAG) + HREF_TAG.length();
                        if(line.indexOf('"', startIndURL) != -1){
                            int endIndURL = line.indexOf('"',startIndURL);
                            String newURL = line.substring(startIndURL, endIndURL);
                            //System.out.println(newURL);
                            if(newURL.startsWith(URLDepthPair.URL_PREFIX))
                                pool.addPair(new URLDepthPair(newURL, currDepth + 1));
                        }
                    }
                }
                sock.close();
                System.out.println("Disconnected"); 
            }catch(Exception e){
                System.out.println("Something wrong with client server interconnections. Exception - " + e.getMessage());
                System.out.println(URLDepthPair.toString() + " - problem with connection");
                e.printStackTrace();
            }
            
        }        
    }
}
