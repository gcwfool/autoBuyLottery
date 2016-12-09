package dsn;
public class ReloginThread extends Thread{

	@Override
    public void run() {

		while(true){
			if(dsnHttp.isAllLotteryIdle()){
				if(dsnHttp.getIsisNeedChangeLine() == true){
					
					
					System.out.println("���ߣ����µ�¼\n");
					
					dsnHttp.isInRelogin = true;
					
					dsnHttp.setLinePriority();
					
					if(dsnHttp.reLogin() == false) {
						dsnHttp.connFailLogin();
					}
					
					dsnHttp.setisNeedChangeLine(false);
					dsnHttp.clearAvgRequest();
					
					dsnHttp.setIsNeedRelogin(false);
					
					dsnHttp.isInRelogin = false;
					
				}
			}
			
			
			if(dsnHttp.getIsNeedRelogin()){
				
				dsnHttp.isInRelogin = true;
				
				System.out.println("���µ�¼\n");
				
				dsnHttp.reLogin();
				dsnHttp.setIsNeedRelogin(false);
				
				dsnHttp.isInRelogin = false;
				
			}
		}

		
    }
	
}
