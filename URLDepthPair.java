import java.net.MalformedURLException;
import java.net.URL;

/**
 * URLDepth
 */
public class URLDepthPair {
    private String URLaddress;
    private int Depth;
    public static final String URL_PREFIX = "http://";


    public URLDepthPair(String url, int depth) throws Exception {
        if(!url.startsWith(URL_PREFIX)){
            throw new MalformedURLException("Something wrong with URL prefix. URL must be starts with 'http://'.");
        }
        if(depth < 0)
            throw new Exception("Depth must be more than 0.");
        this.URLaddress = url;
        this.Depth = depth;
    }

    public String getPath() throws MalformedURLException{
        return new URL(URLaddress).getPath();
    }

    public String getHost() throws MalformedURLException{
        return new URL(URLaddress).getHost();
    }

    public int getDepth(){
        return Depth;
    }

    public String getURLAddress(){
        return URLaddress;
    }

    public String toString() {
        return URLaddress + " - " + Depth;
    }
}