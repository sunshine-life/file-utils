package com.zfsoft.file.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerUtilTest {

	@Test
	public void testGetPdfFile() throws IOException {
		try {Map<String, Object> o = new HashMap<String, Object>();
			o.put("name","李飞");
			File file = new File("ftl/test.ftl");
			File pdf = FreemarkerUtil.getPdfFile(file, o , false);
			System.out.println(String.format("生成pdf文件：%s", pdf.getAbsolutePath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetWordFile(){
		try {
			Map<String, Object> o = new HashMap<String, Object>();
			o.put("name","李飞");
			File file = new File("ftl/test.ftl");
			File docx = FreemarkerUtil.getWordFile(file, o,false);
			System.out.println(String.format("生成docx文件：%s", docx.getAbsolutePath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
