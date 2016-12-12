package lanyang;

import dsn.dsnHttp;

public class LanyangReloginThread extends Thread{

	@Override
    public void run() {

		while(true){
			if(LanyangHttp.isAllLotteryIdle()){
				if(LanyangHttp.getIsisNeedChangeLine() == true){
					
					
					
					
					System.out.println("换线登录\n");
					
					LanyangHttp.isInRelogin = true;
					
					LanyangHttp.strCookies = "";
					
					if(LanyangHttp.reLogin() == false) {
						LanyangHttp.connFailLogin();
					}
					
					LanyangHttp.setisNeedChangeLine(false);
					LanyangHttp.clearAvgRequest();
					
					LanyangHttp.setIsNeedRelogin(false);
					
					LanyangHttp.isInRelogin = false;
					
				}
			}
			
			
			if(LanyangHttp.getIsNeedRelogin()){
				
				LanyangHttp.isInRelogin = true;
				
				LanyangHttp.strCookies = "";
				
				System.out.println("重新登录\n");
				
				try{
					Thread.currentThread().sleep(10*1000);
				}catch(Exception e){
					
				}
				
				if(LanyangHttp.reLogin() == false) {
					
					for(int i = 0; i < 5; i++){
						try{
							Thread.currentThread().sleep(5*1000);
						}catch(Exception e){
							
						}
						
						LanyangHttp.strCookies = "";
						
						if(LanyangHttp.reLogin()== true){
							break;
						}
					}

				}
				LanyangHttp.setIsNeedRelogin(false);
				
				LanyangHttp.isInRelogin = false;
				
			}
		}

		
    }
	
}
