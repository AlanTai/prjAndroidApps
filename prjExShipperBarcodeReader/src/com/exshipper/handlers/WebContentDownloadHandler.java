package com.exshipper.handlers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.exshipper.listeners.ProgressBarUpdateListener;
import com.exshipper.listeners.ViewsUpdateListener;

import android.os.AsyncTask;
import android.util.Log;

public class WebContentDownloadHandler extends AsyncTask<String, Integer, String> {
	private ProgressBarUpdateListener progressBarUpdateListener = null;
	private ViewsUpdateListener txtViewResultUpdateListener = null;
	
	public WebContentDownloadHandler(ProgressBarUpdateListener p_progressbarUpdateListener, ViewsUpdateListener p_txtViewUpdateListener){
		progressBarUpdateListener = p_progressbarUpdateListener;
		txtViewResultUpdateListener = p_txtViewUpdateListener;
	}

	@Override
	protected void onPostExecute(String p_result) {
		// TODO Auto-generated method stub
		if (p_result != null) {
			txtViewResultUpdateListener.updateView(p_result);
		}
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
		String txt_custom_entry_number = null;
		String utf8_response = null;
		
		try{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("account", "alantai"));
			nameValuePairs.add(new BasicNameValuePair("password", "1014"));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
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
						
						Thread.sleep(500); //slow down AsyncTask
					}
				}
				
				//utf8_response = EntityUtils.toString(entityFromServer, HTTP.UTF_8);
				utf8_response = new String(byteOutStream.toByteArray(),"utf-8");
				jsonObj_response = new JSONObject(utf8_response);
				txt_custom_entry_number = jsonObj_response.getString("custom_number");
				
				return txt_custom_entry_number;
			}
		}
		catch(Exception e){
			Log.e("error", "Fail to get the response from server: "+e.getMessage());
		}
		
		return null;
	}
}
