package com.exshipper.generalhandlers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.exshipper.listeners.ProgressBarUpdateListener;

import android.os.AsyncTask;
import android.util.Log;

public class DataExchangeHandler extends AsyncTask<String, Integer, String> {
	private ProgressBarUpdateListener progressBarUpdateListener = null;
	
	public DataExchangeHandler(ProgressBarUpdateListener p_progressbarUpdateListener){
		progressBarUpdateListener = p_progressbarUpdateListener;
	}

	@Override
	protected void onPostExecute(String p_result) {
		// TODO Auto-generated method stub
		progressBarUpdateListener.updateResult(p_result);
		super.onPostExecute(p_result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progressBarUpdateListener.setupProgressBar();
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		if(progressBarUpdateListener.isProgressCountable()){
			progressBarUpdateListener.updateProgressBar(values[0]);
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(params[0]);
		
		JSONObject jsonObj_response = null;
		String txt_response = null;
		String utf8_response = null;
		
		try{
			//authorization process
			if(progressBarUpdateListener.isAuthorizationNecessary() && progressBarUpdateListener.setAuthorizationInfo()!= null){
				httpPost.setEntity(new UrlEncodedFormEntity(progressBarUpdateListener.setAuthorizationInfo()));
			}
			
			HttpResponse serverResponse = httpClient.execute(httpPost, localContext);
			
			if(serverResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entityFromServer = serverResponse.getEntity();
				InputStream inputStream = entityFromServer.getContent();
				long total = entityFromServer.getContentLength();
				ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[2048];
				int count =0;
				int length = -1;
				while((length = inputStream.read(buffer)) != -1){
					byteOutStream.write(buffer, 0, length);
					count += length;

					if(progressBarUpdateListener.isProgressCountable()){
						publishProgress((int) (count/(float) total)*100);
						
						Thread.sleep(100); //slow down AsyncTask
					}
				}
				
				//utf8_response = EntityUtils.toString(entityFromServer, HTTP.UTF_8);
				utf8_response = new String(byteOutStream.toByteArray(),"utf-8");
				jsonObj_response = new JSONObject(utf8_response);
				txt_response = jsonObj_response.getString("response");
				
				return txt_response;
			}
		}
		catch(Exception e){
			Log.e("error", "Fail to get the response from server: "+e.getMessage());
		}
		
		return txt_response;
	}
}
