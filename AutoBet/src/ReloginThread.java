
public class ReloginThread extends Thread{

	@Override
    public void run() {

		while(true){
			if(dsnHttp.isAllLotteryIdle()){
				if(dsnHttp.getIsisNeedChangeLine() == true){
					
					
					System.out.println("换线，重新登录\n");
					
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
				
				System.out.println("重新登录\n");
				
				dsnHttp.reLogin();
				dsnHttp.setIsNeedRelogin(false);
				
				dsnHttp.isInRelogin = false;
				
			}
		}

		
    }
	
}
