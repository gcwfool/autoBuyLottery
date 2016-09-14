
public class GrabXYNCthread extends Thread{
	
	GrabXYNCwindow gwXYNC = null;
	boolean grabXYNC      = false;
	boolean isClose       = false;
	boolean requsetTime   = true;
	long almostTime       = 30 * 1000;
	
	public GrabXYNCthread() {
		gwXYNC = new GrabXYNCwindow();
	}
	
	@Override
    public void run() {
		try{
			while(true) {
				if(grabXYNC) {
					String[] XYNCtime = {"0", "0", "0"};
					
					long XYNCremainTime = 21*60*1000;
					for(int i = 0; i < 4; i++) {
						XYNCtime = DsnProxyGrab.getXYNCTime();
						XYNCremainTime = Long.parseLong(XYNCtime[0]);
						if(XYNCremainTime < 20*60*1000) {
							break;
						}
					}
					
					if(XYNCremainTime > 20*60*1000) {
						Thread.currentThread().sleep(5000);
						continue;
					}
						
					if(XYNCremainTime >= 0){
						System.out.println("距离幸运农场封盘时间：" + XYNCremainTime/1000);
						gwXYNC.setRemainTime(XYNCremainTime);
					} else {
						System.out.println("距离幸运农场开盘时间：" + Long.parseLong(XYNCtime[2])/1000);
						gwXYNC.setRemainTime(Long.parseLong(XYNCtime[2]));
					}
					
					gwXYNC.setDrawNumber(XYNCtime[1]);
					if(XYNCremainTime > 0) {
						if(isClose) {
							gwXYNC.setCloseText(false);
							gwXYNC.resetData();
							isClose = false;
							DsnProxyGrab.disableXYNCData();
						}					
					} else if(XYNCremainTime <= 0){
						if(!isClose) {
							gwXYNC.setCloseText(true);
							isClose = true;
							continue;
						}
					}
					
					boolean res = DsnProxyGrab.grabXYNCdata();
					
					if(res) {
						gwXYNC.setData(DsnProxyGrab.getXYNCshowData());
						if(XYNCremainTime <= 11 * 1000) {
							DsnProxyGrab.setXYNCdata(XYNCtime[1], Long.toString(gwXYNC.getRemainTime()/1000));
						}
					}
				}
						
				Thread.currentThread().sleep(5000);
			}
		}catch (InterruptedException e) {
	        // TODO: handle exception
	    }
		
	}
	
	public  void startGrabXYNC() {
    	grabXYNC = true;
    	gwXYNC.setVisible(true);
    }
    
    public  void stopGrabXYNC() {
    	grabXYNC = false;
    	gwXYNC.setVisible(false);
    }

}
