package lanyang;




public class BetBJSCManager {
	
	
    public static String getGDKLSFoddsData(){
    	
    	String res = "";
    	
    	
/*    	String checkGameUrl = LanyangHttp.lineuri + "z/user-CheckGameAjax";
    	
        List<NameValuePair> Params = new ArrayList<NameValuePair>();
        Params.add(new BasicNameValuePair("cn", "11"));
        Params.add(new BasicNameValuePair("ty", "0"));

        res = LanyangHttp.doPost(checkGameUrl, Params, LanyangHttp.strCookies);
        
        
        System.out.println("check game");
        System.out.println(res);*/
        
    	
    	String url = LanyangHttp.lineuri + "z/gPKT-pkt7?gi=11&bt=1";
    	
    	System.out.println(LanyangHttp.strCookies);
    	
    	
    	res = LanyangHttp.doGet(url, LanyangHttp.strCookies, "");
    	
    	
    	
    	if(res == null){
    		res = LanyangHttp.doGet(url, LanyangHttp.strCookies, "");
    	}
    	
    	System.out.println(res);
    	
    	return  res;
   	
    }
    
    
}
