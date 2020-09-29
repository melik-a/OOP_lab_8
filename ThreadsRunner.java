/**
 * ThreadsRunner
 */
public class ThreadsRunner {

    public static void main(String[] args) throws Exception {
        if(args.length != 4){
            System.out.println("usage: java ThreadsRunner <URL> <depth> <threads> <thWaitTime>");
            return;
        }
        String ur = args[0];
        int deep = Integer.parseInt(args[1]);
        int threads;
        int serverTimeout;
        try{
            threads = Integer.parseInt(args[2]);
            serverTimeout = Integer.parseInt(args[3]);
        }catch(Exception e){
            System.out.println("illegal arguments value " + e.getMessage());
            System.out.println("count of threads and server timeout must be positive integer number.");
            return;
        }


        URLPool pool = new URLPool(deep);
		URLDepthPair firstPair = new URLDepthPair(ur, 0);
		pool.addPair(firstPair);
		
		for (int i = 0; i < threads; i++) {
			CrawlerTask task = new CrawlerTask(pool, serverTimeout);
			Thread taskThread = new Thread(task);
			taskThread.start();
		}
		
		while (pool.getCountOfWaitingThreads() != threads) {
			try {
				Thread.sleep(100); // 0.1 second
			} catch (InterruptedException e) {
				System.out.println("interrupted Exception - " + e.getMessage());
			}
		}
        // Print out all found urls
        pool.printAllVisitedSitesWithDepth();
		// Exit the program
		System.exit(0);      
    }
}
