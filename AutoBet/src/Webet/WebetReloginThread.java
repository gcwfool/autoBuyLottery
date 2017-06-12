package Webet;

import dsn.dsnHttp;

public class WebetReloginThread extends Thread{

	@Override
    public void run() {

		while(true){
			
			
			if(WebetHttp.getIsNeedRelogin()){
				
				WebetHttp.isInRelogin = true;
				
				WebetHttp.strCookies = "";
				
				System.out.println("重新登录\n");
				
				try{
					Thread.currentThread().sleep(10*1000);
				}catch(Exception e){
					
				}
				
				if(WebetHttp.reLogin() == false) {
					
					for(int i = 0; i < 5; i++){
						try{
							Thread.currentThread().sleep(5*1000);
						}catch(Exception e){
							
						}
						
						WebetHttp.strCookies = "";
						
						if(WebetHttp.reLogin()== true){
							
							break;
						}
					}

				}
				
				BetBJSCManager.changeToBJgame();
				
				WebetHttp.setIsNeedRelogin(false);
				
				WebetHttp.isInRelogin = false;
				
			}
		}

		
    }
	
}
