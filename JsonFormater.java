package common;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormater {
	//获取空格数
	 private static String getSpaceSuffix(int level)
	 {
		 String ret = "";
		 for (int i = 0; i < level; i++) {
			ret +="	";
		}
		return ret;
	 }
	 
	 /**
	  * json的格式化,传入一个JsonObject，转换成string
	  * @param object
	  * @param level
	  * @param needKuoHao
	  * @return
	  */
	 public static String formatJson(Object object,int level,boolean needKuoHao)
	 {
		 if(object instanceof JSONObject)
		 {
			 JSONObject jsonObject = (JSONObject)object;
			 String content = "";
			 if(needKuoHao){
				 content =  getSpaceSuffix(level) + "{\n";
			 }
			 Iterator keys = jsonObject.keys();
	    	while(keys.hasNext()){
	    		String key = keys.next().toString();
	    		if(key.equals("3")){
	    			int test =1 ;
	    			test++;
	    		}
	    		try {
					Object subObject = jsonObject.get(key);
					if(subObject instanceof JSONObject){
						String realContent = formatJson(subObject, level+1, false);
						
						if(realContent.length() > 1 && !realContent.substring(realContent.length() - 1).equals("\n")){
							realContent +="\n";
						}
						content += (getSpaceSuffix(level+1)+formatJson(key,0,false)+":{\n"+realContent+getSpaceSuffix(level+1)+"},\n");
					}else if(subObject instanceof JSONArray){
						String realContent = formatJson(subObject, level+1, false);
						if(realContent.length() > 1 && !realContent.substring(realContent.length() - 1).equals("\n")){
							realContent +="\n";
						}
						content += (getSpaceSuffix(level+1)+formatJson(key,0,false)+":["+realContent+getSpaceSuffix(level+1)+"],\n");
					}else{
						content += (getSpaceSuffix(level+1)+formatJson(key,0,false)+":"+formatJson(subObject, level+1, false) + ",\n");
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	if(needKuoHao){
	    		if(content.length() > 1 && !content.substring(content.length() - 1).equals("\n")){
	    			content +="\n";
				}
	    		content += (getSpaceSuffix(level)+"}");
	    	}
	    	return  content;
		 }
		 else if(object instanceof JSONArray)
		 {
			 JSONArray arrayObject = (JSONArray)object;
			 String content = "";
			 if(needKuoHao){
				 content =getSpaceSuffix(level) + "[";
			 }
			 for (int i = 0; i < arrayObject.length(); i++) {
				 Object subObject;
				try {
					subObject = arrayObject.get(i);
					if(subObject instanceof JSONObject){
						String subStr = formatJson(subObject, level+1, true)+",";
						content += ("\n"+subStr);
					 }else if(subObject instanceof JSONArray){
						 content += ("\n"+formatJson(subObject, level+1, true)+",");
					 }else{
						 content += (formatJson(subObject, level, false)+",");
					 }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 if(needKuoHao){
				 content =getSpaceSuffix(level) + "]";
			 }
			 return  content;
		 }else if(object instanceof String){
			 return "\""+object.toString()+"\"";
		 }
		 return object.toString();
	 }

}
