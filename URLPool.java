import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * URLPool
 */
public class URLPool {
    //LinkedList URLDepthPair of all visited sites
    private LinkedList<URLDepthPair> visitedSites;
    //LinkedList URLDepthPair of all sites to visit
    private LinkedList<URLDepthPair> needToVisit;
    private int waitingThreads;
    private int maxDepth;
    private HashSet<String> alreadySeen;

    public URLPool(int mDepth){
        this.maxDepth = mDepth;
        visitedSites = new LinkedList<URLDepthPair>();
        needToVisit = new LinkedList<URLDepthPair>();
        alreadySeen = new HashSet<String>();
        this.waitingThreads = 0;
    }
    
    public synchronized URLDepthPair getPair(){
        if(needToVisit.size() == 0){
            waitingThreads++;
            try{
                this.wait();
            }catch(InterruptedException e){
                System.out.println("interrupted Exception - " + e.getMessage());
            }
            waitingThreads--;
            
        }
        URLDepthPair nextURLDepthPair = needToVisit.removeFirst();
        return nextURLDepthPair;
    }

    public synchronized void addPair(URLDepthPair addingPair){
        if(alreadySeen.contains(addingPair.getURLAddress())){
            System.out.println("this address has already been viewed: " + addingPair.getURLAddress());
            return;
        }
        
        visitedSites.add(addingPair);
        if(addingPair.getDepth() < maxDepth){
            needToVisit.add(addingPair);
            this.notify();
        }
        alreadySeen.add(addingPair.getURLAddress());
    }

    public synchronized int getCountOfWaitingThreads(){
        return waitingThreads;
    }

    public void printAllVisitedSitesWithDepth(){
        System.out.println("\nList of all visited sites with depth: ");
        for (URLDepthPair pair : visitedSites) {
            System.out.println(pair.toString());
        }
    }

    public void printAllVisitedSites(){
        System.out.println("\nList of all visited sites: ");
        alreadySeen.toString();
    }

    public boolean isNeedToVisitListEmpty(){
        return needToVisit.isEmpty();
    }
    
}