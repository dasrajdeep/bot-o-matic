package impersonator;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author rajdeep
 *
 */
public class FQueue {
	
	private static Queue<String> queue=new PriorityQueue<String>();
	
	public static Facebook fb=new Facebook("723122917704615", "2a58136764663aad709ce2cf5efd1fa5", "http://wishemall.dnsd.me:7754/");
	
	public static void enqueue(String task) {
		queue.add(task);
	}
	
	public static void dequeue() {
		
		if(queue.isEmpty()) return;
		
		String task=queue.remove();
		
		if(task.equals("showAuthDialog")) {
			fb.showAuthDialog();
		} else if(task.equals("getAuthToken")) {
			fb.getAuthToken();
		}
	}
	
}
