package edu.neu.info6205.algo;

import java.util.Timer;
import java.util.TimerTask;

import edu.neu.info6205.dataStructure.DispatcherQueue;

public class Dispatcher {

	//Singleton
	static Dispatcher instance = null;
	public static DispatcherQueue queue = new DispatcherQueue();
	int dispatchPeriod = 200; //despatch period is 200ms
	Timer dispatchTimer = new Timer();
	
	private Dispatcher(){
		System.out.println("Dispatcher Initialised");
	}
	
	public void startDispatching(){

		dispatchTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!queue.isEmpty()){
					System.out.println("dispatched");
					dispatchRequest();
				}
			}
		}, 200, dispatchPeriod);
	
	}
	
	public void stopDispatchingRequests(){
		dispatchTimer.cancel();
		dispatchTimer = null;
		dispatchTimer = new Timer();
	}
	
	public static Dispatcher getInstance(){
		if(instance == null)
			instance = new Dispatcher();
		return instance;
	}
	
	public void queueRequest(Request request){
		queue.enque(request);
	}
	
	public void dispatchRequest(){
		if(!queue.isEmpty()){
			VirtualMachine vm = Service.getInstance().availableVM();
			if(vm == null){
				System.out.println("VM's are full");
			}else{
				Request temp = queue.deque();
				temp.setVm(vm);
				vm.getVmQueue().enque(temp);
				// send temp to service layer
			}
		}
	}
	
}
