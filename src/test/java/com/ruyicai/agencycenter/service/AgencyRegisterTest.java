package com.ruyicai.agencycenter.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-jms.xml",
		"classpath:/META-INF/spring/applicationContext-memcache.xml" })
public class AgencyRegisterTest {

	@Test
	public void excute(){
		for(int i=1;i<=10;i++){
			String temp = new DecimalFormat("000").format(i);
			String param = "userName=1六律测试"+temp
					+"&password=123456"+"&state=1"+"&type=0"+"&channel=991";
			try {
//				post("http://192.168.99.6/lotteryprize/tuserinfoes/register",param);
				post("http://192.168.0.42:8080/lottery/tuserinfoes/register",param);
			} catch (Exception e1) {
				System.out.println("请求/tuserinfoes/register" + "失败" + e1.getMessage());
			}
		}
	}
	
	public static String post(String urlStr, String params) throws Exception {
		return new AgencyRegisterTest()._post(urlStr, params);
	}

	public String _post(String urlStr, String params) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);
		conn.setConnectTimeout(180000);
		conn.connect();
		OutputStream out = conn.getOutputStream();
		out.write(params.getBytes());
		out.close();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String result = reader.readLine();
		reader.close();
		return result;
	}
	
}




