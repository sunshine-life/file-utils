package com.zfsoft.file.fonts;

import java.net.URL;

public enum ChineseFont {
	
	//仿宋体
	SIMFANG("SimFang","SIMFANG.TTF"),
	//黑体
	SIMHEI("SimHei","SIMHEI.TTF"),
	//楷体
	SIMKAI("SimKai","SIMKAI.TTF"),
	//宋体&新宋体
	SIMSUM("SimSun","SIMSUN.TTC"),
	//华文仿宋
	STFANGSO("StFangSo","STFANGSO.TTF"),
	;
	
	private String fontFileName;
	private String fontName;
	
	public URL getFontUrl() {
		return ChineseFont.class.getResource(this.fontFileName);
	}
	public String getFontName() {
		return fontName;
	}
	
	private ChineseFont(String v,String fontFileName) {
		this.fontName = v;
		this.fontFileName = fontFileName;
    }

	

}
