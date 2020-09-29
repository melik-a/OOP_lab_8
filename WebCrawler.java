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

public class WebCrawler {
    //LinkedList URLDepthPair of all visited sites
    private LinkedList<URLDepthPair> visitedSites;
    //LinkedList URLDepthPair of all sites to visit
    private LinkedList<URLDepthPair> needToVisit;
    static final String HREF_TAG = "<a href=\"";

    public WebCrawler(){
        visitedSites = new LinkedList<URLDepthPair>();
        needToVisit = new LinkedList<URLDepthPair>();
    }

    private void getSites(String initURL, int maxDepth) throws Exception{
        needToVisit.add(new URLDepthPair(initURL, 0));
        int webPort = 80;
        URLDepthPair URLDepthPair;
        int currDepth;
        while(!needToVisit.isEmpty()){
            try{
                URLDepthPair = needToVisit.removeFirst(); 
                currDepth = URLDepthPair.getDepth();
                Socket sock = new Socket(URLDepthPair.getHost(), webPort);
                sock.setSoTimeout(3000); // Time-out after 3 seconds
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
                        if((line.indexOf('"', startIndURL) != -1) && (currDepth < maxDepth)){
                            int endIndURL = line.indexOf('"',startIndURL);
                            String newURL = line.substring(startIndURL, endIndURL);
                            //System.out.println(newURL);
                            if(newURL.startsWith(URLDepthPair.URL_PREFIX))
                                needToVisit.add(new URLDepthPair(newURL, currDepth + 1));
                        }
                    }
                }
                sock.close();
                visitedSites.add(URLDepthPair);
                System.out.println("Disconnected"); 
            }catch(Exception e){
                System.out.println("Something wrong with client server interconnections. Exception - " + e.getMessage());
            }
            
        }
        System.out.println("\nList of all visited sites: ");
        for (URLDepthPair pair : visitedSites) {
            System.out.println(pair.toString());
        }
    }


    public static void main(String[] args) throws Exception {
        if(args.length != 2){
            System.out.println("usage: java Crawler <URL><depth>");
            return;
        }
        String ur = args[0];
        int deep = Integer.parseInt(args[1]);
        WebCrawler cr = new WebCrawler();
        cr.getSites(ur,deep);        
    }
}
