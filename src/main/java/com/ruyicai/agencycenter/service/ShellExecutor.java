package com.ruyicai.agencycenter.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShellExecutor {

	private Logger logger = LoggerFactory.getLogger(ShellExecutor.class);

	/**
	 * @param batname
	 *            文件全路径
	 * @return
	 */
	public boolean executeFile(String batname) {
		Process process;
		String line = null;
		try {
			process = Runtime.getRuntime().exec(batname);
			InputStream fis = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			while ((line = br.readLine()) != null) {
				logger.info(line);
			}
			if (process.waitFor() != 0) {
				return false;
			}
			logger.info(batname + " run successful!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			ShellExecutor df = new ShellExecutor();
			System.out.println(df.executeFile("D:\\1.bat"));// /home/appusr/renhongyu/1.sh
															// chmod 777
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
