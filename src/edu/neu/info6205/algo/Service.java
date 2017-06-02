package edu.neu.info6205.algo;

public class Service 
{
	private static Service service;
	private static VirtualMachine[] machines = new VirtualMachine[5];
	private static Thread[] threads = new Thread[machines.length];
	private static int[] serverSizes = {0,0,0,0,0};
	private int inProcCount = 0;
	private int processedCount = 0;
	private int serversInProccess = 0;
	private int proccessTime = 0;
	private long AvgProccessTimeStorage = 0;//holds a total time of previous 100 requests that got proccessed 
	private long AvgProccessTime = 0;
	
	
	public long getAvgProccessTimeStorage() {
		return AvgProccessTimeStorage;
	}

	public void setAvgProccessTimeStorage(long avgProccessTimeStorage) {
		AvgProccessTimeStorage = avgProccessTimeStorage;
	}

	public long getAvgProccessTime() {
		return AvgProccessTime;
	}

	public void setAvgProccessTime(long avgProccessTime) {
		AvgProccessTime = avgProccessTime;
	}

	public int getProccessTime() {
		return proccessTime;
	}

	public void setProccessTime(int proccessTime) 
	{
		this.proccessTime = proccessTime;
	}

	public int getServersInProccess() 
	{
		return serversInProccess;
	}

	public void setServersInProccess(int serversInProccess) 
	{
		this.serversInProccess = serversInProccess;
	}

	public int getProcessedCount() 
	{
		return processedCount;
	}

	public void setProcessedCount(int processedCount) 
	{
		this.processedCount = processedCount;
	}

	public int getInProcCount() 
	{
		return inProcCount;
	}

	public void setInProcCount(int inProcCount) 
	{
		this.inProcCount = inProcCount;
	}
	
	public int[] getServerSizes() 
	{
		return serverSizes;
	}

	private Service() 
	{	
		for (int i = 0; i < machines.length; i++)
			{
				machines[i] = new VirtualMachine();
			}
	}
	
	public static Service getInstance() 
	{
		if(service == null)
			service = new Service();
		return service;
	}

	public VirtualMachine availableVM()
	{
		if(!machines[0].isEmployed())
		{
			System.out.println(">>>>>>>>>>first machine employed");
			inProcCount++;
			employVM(machines[0]);
			threads[0] = new Thread(machines[0]);
			threads[0].start();
			serverSizes[0]++;
			return machines[0];
		}
		else if(!(machines[0].getVmQueue()).isFull())
		{
			inProcCount++;
			serverSizes[0]++;
			return machines[0];
		}
		for(int i = 1; i < machines.length; i++)
		{
			if(machines[i].isEmployed())
				{
					if(!((machines[i].getVmQueue()).isFull()))
					{
						inProcCount++;
						serverSizes[i]++;
						return machines[i];
					}
				}
			else 
				{
					System.out.println(">>>>>>>>>>new machine employed");
					inProcCount++;
					employVM(machines[i]);
					threads[i] = new Thread(machines[i]);
					threads[i].start();
					serverSizes[i]++;
					return machines[i];
				}					
		}
		
		return null;
	}
	
	public void employVM(VirtualMachine vm)
	{
		vm.setEmployed(true);
		serversInProccess++;
	}
	
	public void cleareQueues(){
		for(int i= 0; i<machines.length;i++){
			VirtualMachine m = machines[i];
			m.getVmQueue().cleare();
			m = null;
		}
	}
	
	public void releaseVM(VirtualMachine vm)
	{
		if(serversInProccess > 1)
		{
			serversInProccess--;
			vm.setEmployed(false);
		}
	}
}