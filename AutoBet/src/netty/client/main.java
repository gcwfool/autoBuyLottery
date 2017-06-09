package netty.client;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class main {
	public static void main(String [] args) throws IOException{
    	//重定向输出
		   String jsonStr = "{\"success\": 600,\"data\": {\"type\": \"put_money\",\"JeuValidate\": \"05011051368664\",\"index\": [\"10\", \"11\"],\"newpl\": [\"1.9851\", \"1.9852\"]},\"tipinfo\": \"賠率有變動,請確認後再提交!\"}";
		   JSONObject betRes = new JSONObject(jsonStr);
		   JSONObject data = betRes.getJSONObject("data");
		   JSONArray arr = data.getJSONArray("index");
		   JSONArray arr1 = data.getJSONArray("newpl");
		   int size = arr.length();
		   int [] indexs = new int[size];
		   String [] pls = new String[size];
		   for(int i = 0; i < size; i++) {
			   indexs[i] = arr.getInt(i);
			   pls[i] = arr1.getString(i);
		   }
		   
		   for(int j = 0; j < size; j++) {
			   System.out.println(indexs[j] + ":" + pls[j]);
		   }
    }

}
