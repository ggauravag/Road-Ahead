package com.source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.Log;

public class ServerUtility 
{
	public static final String REGISTER_URL = "http://www.webservice.dreambit.in/AndroidRegister.aspx";
	

	
	
	public static final String TAG = "GCM";
	
	public static final String MESSAGE_ID = "id";
	
	public static final String ACKNOWLEDGE_URL = "http://www.webservice.dreambit.in/AndroidPush.aspx";
	
	public static final String TITLE_ID = "title";
	
	public static final String MESSAGE_TEXT = "message";
	
	public static final String MESSAGE_TIME = "datetime";
	
	public static final String LOGIN_URL = "http://www.webservice.dreambit.in/AndroidLogin.aspx";
	
	public static final String DATA_URL = "http://www.webservice.dreambit.in/AndroidData.aspx";
	
	public static final String GOOGLE_ID = "1008548012532";
	
	
	
	
	public static void acknowledgeReceipt(int id,String regid)
	{
		URL url = null;
	       try {
			 url = new URL(ACKNOWLEDGE_URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG,"SU : Unable to register - Incorrect URL");
			
		}
	       
	       
	       try {
	    	   
				String data = URLEncoder.encode("id","UTF-8");
				data += "="+URLEncoder.encode(id+"","UTF-8");
				data += "&"+URLEncoder.encode("regid","UTF-8")+"=";
				data += URLEncoder.encode(regid,"UTF-8")+"&";

				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
				writer.write(data);
				writer.flush();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer sb = new StringBuffer("");
		        String line="";
		        while ((line = reader.readLine()) != null) {
		           sb.append(line);
		           break;
		         }
		        
		        Log.i(TAG,"Response : "+sb);
		        reader.close();
		       
		        
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG,"SU : "+e.getMessage());
				
		       
			}
	       
	       
	}
	
	
	public static String login(String credential,String password,String id)
	{
		URL url = null;
	       try {
			 url = new URL(LOGIN_URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG,"SU : Unable to register - Incorrect URL");
			return "Error ! No Internet Connection.";
		}
	       
	       try {
	    	   
			String data = URLEncoder.encode("id","UTF-8");
			data += "="+URLEncoder.encode(id,"UTF-8");
			data += "&"+URLEncoder.encode("cred","UTF-8")+"=";
			data += URLEncoder.encode(credential,"UTF-8")+"&";
			data += URLEncoder.encode("password", "UTF-8")+"=";
			data += URLEncoder.encode(password,"UTF-8");
			
			
			
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer("");
	        String line="";
	        while ((line = reader.readLine()) != null) {
	           sb.append(line);
	           break;
	         }
	        
	        Log.i(TAG,"Response : "+sb);
	        JSONObject obj = new JSONObject(sb.toString());
	        String op = obj.getString("Status");
	        Log.i(TAG,op);
	        if(op.equals("Valid"))
	        {
	        	return "Logged In Successfully !";
	        }
	        if(op.equals("Error"))
	        	return "Unexpected Error ! Try Again Later.";
	        else if(op.equals("Invalid"))
	        {
	        	return "Username/Password Invalid !";
	        }
	        
	        reader.close();
	       
	        
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG,"SU : "+e.getMessage());
			return "Error while processing request ! Try Again.";
		}
	       

	        return "Error while processing request ! Try Again.";
	    
		
	}
	
	
	
	public static int registerAccount(String androidId,String name,String mobile,String email,String password)
    {
		URL url = null;
       try {
		 url = new URL(REGISTER_URL);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.i(TAG,"SU : Unable to register - Incorrect URL");
		return 0;
	}
       
       try {
    	   
		String data = URLEncoder.encode("ID","UTF-8");
		data += "="+URLEncoder.encode(androidId,"UTF-8");
		data += "&"+URLEncoder.encode("name","UTF-8")+"=";
		data += URLEncoder.encode(name,"UTF-8")+"&";
		data += URLEncoder.encode("mobile", "UTF-8")+"=";
		data += URLEncoder.encode(mobile,"UTF-8");
		data += "&"+URLEncoder.encode("email","UTF-8")+"=";
		data += URLEncoder.encode(email,"UTF-8");
		data += "&"+URLEncoder.encode("password","UTF-8")+"=";
		data += URLEncoder.encode(password,"UTF-8");
		data += "&"+URLEncoder.encode("resp","UTF-8")+"=";
		data += URLEncoder.encode("json","UTF-8");
		
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(data);
		writer.flush();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer("");
        String line="";
        while ((line = reader.readLine()) != null) {
           sb.append(line);
           break;
         }
        
        Log.i(TAG,"Response : "+sb);
        JSONObject obj = new JSONObject(sb.toString());
        String op = obj.getString("Status");
        Log.i(TAG,op);
        if(op.equals("Success"))
        {
        	return 1;
        }
        if(obj.getString("Report") != null && !obj.getString("Report").equals("") )
        	return 0;
        else if(op.equals("Duplicate"))
        {
        	return 2;
        }
        
        reader.close();
       
        
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.i(TAG,"SU : "+e.getMessage());
		return 0;
	}
       

        return 0;
    }
	
	
}
