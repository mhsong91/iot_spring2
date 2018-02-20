package com.iot.spring.dao.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;

import com.iot.spring.dao.NaverTransDAO;
import com.iot.spring.vo.NaverMsg;

public class NaverTransDAOImpl implements NaverTransDAO{
	private String url;
	private String contentType;
	private String clientId;
	private String clientSecret;
	private String source;
	private String target;

	public void setUrl(String url) {
		this.url = url;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getText(String text) throws IOException {
		HttpURLConnection con =null;
		BufferedReader br =null;
		try {
			text = URLEncoder.encode(text, "UTF-8");
			URL url = new URL(this.url);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			con.setDefaultRequestProperty("Content-Type", this.contentType);
			// post request
			
			String postParams = "source="+this.source+"&target="+this.target+"&text=" + text.substring(0, 30);  // 내가받은 영어에러메시지를 한글로 바꿀꺼야
			
			con.setDoOutput(true); 
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush(); //번역실행
			wr.close(); //번역되면 전화 끊고
			int responseCode = con.getResponseCode();
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				
				response.append(inputLine);
			}
			br.close();
			
			
			ObjectMapper om = new ObjectMapper();
			NaverMsg json = om.readValue(response.toString(),NaverMsg.class);
			return json.getMessage().getResult().getTranslatedText();
			
			
		} catch (Exception e) {
			System.out.println(e);
		}finally {
			if(br!=null) {
				br.close();
			}
			if(con!=null) {
				con.disconnect();
			}
		}
		return "";
	}

}
