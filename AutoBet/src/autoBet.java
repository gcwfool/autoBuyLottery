

public class autoBet {
	public static void main(String[] args) throws Exception {
		
		//String content = zzLogin.doGet("http://a1.dio168.net/indexs2.php?p=QWERTYUNzIzOTc2NDY0MT14ZWRuaV84ODFvaWQmbmM9WEdOQUwmODgxb2lkPWxwdCZGMiVtb2MuODg4Nzc3YUYyJUYyJUEzJXB0dGg9aHI%253D");
		//dsnHttp.loginToDsn();   
		//����  5���һ������
		String data = DsnProxyGrab.doLogin(); //��¼����
		
		boolean res = dsnHttp.loginToDsn();	//��¼��Ա
		
		boolean autoBetSuccess = false;
		
		if (res == true){
			while(true){
				long remainTime = dsnHttp.timeToBet();
				
					System.out.println("�������ʱ��Ϊ:");
					System.out.println(remainTime/1000);
				
					if(remainTime <= 0){//�Ѿ�����,���ķ��Ӻ��ٴβ�ѯ
						Thread.sleep(4*60*1000);//���ķ���
					}
					else{
						if(remainTime < 3*1000){//���������ȥ��ע
							data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
							System.out.println("�µ����ݣ�");
							System.out.println(data);
							if(data != null){
								autoBetSuccess = dsnHttp.doBetCQSSC(data);
								Thread.sleep(4*1000);
							}
						}
						else{
							long sleepTime = remainTime - 3*1000;
							Thread.sleep(sleepTime);
						}
					}

		}
		}
		
		/*if(data != null) {
			for(int i = 0; i < 3; i++) {
					//data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					//data = DsnProxyGrab.grabCQSSCdata("DH", "XZ", "");
					//data = DsnProxyGrab.grabCQSSCdata("QZHS", "XZ", "");
				Thread.sleep(10000);
			}
		}*/
	}
}
