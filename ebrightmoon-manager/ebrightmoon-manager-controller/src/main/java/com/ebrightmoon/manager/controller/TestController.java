package com.ebrightmoon.manager.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ebrightmoon.common.FileUtil;


@Controller
public class TestController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/hello")
	public String hello() {
		return "hello";
	}

	@RequestMapping("/upload")
	public String upload() {
		return "upload";
	}

	@RequestMapping("/uploadFile")
	public String upload(@RequestParam MultipartFile[] myfiles,
			HttpServletRequest request) throws IOException {
		if (myfiles == null || myfiles.length == 0) {
			return "upload";
		}
		for (MultipartFile file : myfiles) {
			// 姝ゅMultipartFile[]琛ㄦ槑鏄鏂囦欢,濡傛灉鏄崟鏂囦欢MultipartFile灏辫浜�
			if (file.isEmpty()) {
				System.out.println("鏂囦欢鏈笂浼�!");
			} else {
				// 寰楀埌涓婁紶鐨勬枃浠跺悕
				String fileName = file.getOriginalFilename();
				boolean isImage = FileUtil.isImage(fileName);
				// 寰楀埌鏈嶅姟鍣ㄩ」鐩彂甯冭繍琛屾墍鍦ㄥ湴鍧�
				String sourcePath = "";
				if (isImage) {
					sourcePath = request.getSession().getServletContext()
							.getRealPath("image")
							+ File.separator;
				} else {
					sourcePath = request.getSession().getServletContext()
							.getRealPath("tempFile")
							+ File.separator;
				}
				// 姝ゅ鏈娇鐢║UID鏉ョ敓鎴愬敮涓�鏍囪瘑,鐢ㄦ棩鏈熷仛涓烘爣璇�
				String path = sourcePath
						+ new SimpleDateFormat("yyyyMMddHHmmss")
								.format(new Date()) + fileName;
				// 鏌ョ湅鏂囦欢涓婁紶璺緞,鏂逛究鏌ユ壘
				System.out.println(path);
				// 鎶婃枃浠朵笂浼犺嚦path鐨勮矾寰�
				File localFile = new File(path);
				file.transferTo(localFile);
			}
		}
		return "success";
	}

	@RequestMapping("/download")
	public void download(String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="
				+ fileName);
		try {
			String path = request.getSession().getServletContext()
					.getRealPath("image")
					+ File.separator;
			InputStream inputStream = new FileInputStream(new File(path
					+ fileName));

			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}

			// 杩欓噷涓昏鍏抽棴銆�
			os.flush();
			os.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 杩斿洖鍊艰娉ㄦ剰锛岃涓嶇劧灏卞嚭鐜颁笅闈㈣繖鍙ラ敊璇紒
		// java+getOutputStream() has already been called for this response
	}
}
