package com.zfsoft.file.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.zfsoft.file.fonts.ChineseFont;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.docx4j.Docx4J;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

public class FreemarkerUtil {
	

	/**
	 * 生成pdf文件
	 * @param template 模版文件
	 * @param data 填充数据
	 * @param landscape true：横版，false 竖版
	 * @return
	 * @throws Exception
	 */
	public static File getPdfFile(File template,Map<String, Object> data,boolean landscape) {

		String html = null;
		FileOutputStream fops = null;
		// step 1
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		// step 2
		OutputStream osd = System.out;
		OutputStream os = null;
		File file = new File(genFilePath() + ".pdf");
		try {
			html = FreemarkerUtil.getContent(template, data);
			fops = new FileOutputStream("ftl/test.html");
			byte[] contentInBytes = html.getBytes();
			fops.write(contentInBytes);
			osd.write(html.getBytes());

			os = new FileOutputStream(file);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			// step 3
			document.open();
			BaseFont baseFont = BaseFont.createFont("/home/life/下载/toolbox-master/file-utils/src/main/resources/com/zfsoft/file/fonts/ARIALUNI.TTF",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
			MyFontProvider myFontProvider = new MyFontProvider(BaseColor.BLACK, "", "", false, false, 16, 1, baseFont);

			Font font = new Font(baseFont);
			document.add(new Paragraph("解决中文问题了！",font));
			// step 4
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
					new FileInputStream("ftl/test.html"),null, Charset.forName("UTF-8"),myFontProvider);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				document.close();
				System.out.println( "PDF Created!" );
				fops.flush();
				fops.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	
	/**
	 * 生成word文件
	 * @param template 模版文件
	 * @param data 填充数据
	 * @param landscape true：横版，false 竖版
	 * @return
	 * @throws Exception
	 */
	public static File getWordFile(File template,Map<String, Object> data,boolean landscape) throws Exception{
		
		String html = FreemarkerUtil.getContent(template, data);
		Document doc = Jsoup.parse(html);
		
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);  //转为 xhtml 格式

        File file = saveDocx(xhtml2word(doc,landscape));
		return file;
	}
	
	
	
	/*
	 * 将 {@link org.jsoup.nodes.Document} 对象转为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage}
	 * landscape true 横版，false 竖版
	 */
	private static WordprocessingMLPackage xhtml2word(Document doc,boolean landscape) throws Exception {

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape); //A4纸，

        configSimSunFont(wordMLPackage); //配置中文字体

        XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
		System.out.println("11111"+xhtmlImporter.convert(doc.html(), doc.baseUri()).toString());
		//导入 xhtml
        wordMLPackage.getMainDocumentPart().getContent().addAll(xhtmlImporter.convert(doc.html(), doc.baseUri()));
		System.out.println("22222"+wordMLPackage.getMainDocumentPart().getContent().toString());
        return wordMLPackage;
    }
	
	
	
	/*
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置中文字体
	 */
	private static void configSimSunFont(WordprocessingMLPackage wordMLPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        wordMLPackage.setFontMapper(fontMapper);
        
        ChineseFont[] fonts = ChineseFont.values();
        for (ChineseFont font : fonts){
        	//加载字体文件（解决linux环境下无中文字体问题）
            PhysicalFonts.addPhysicalFont(font.getFontName(), font.getFontUrl());
            PhysicalFont simsunFont = PhysicalFonts.get(font.getFontName());
            fontMapper.put(font.getFontName(), simsunFont);
        }
        
        
        
        //设置文件默认字体
        RFonts rfonts = Context.getWmlObjectFactory().createRFonts(); 
        rfonts.setAsciiTheme(null);
        rfonts.setAscii(ChineseFont.SIMSUM.getFontName());
        RPr rpr = wordMLPackage.getMainDocumentPart().getPropertyResolver().getDocumentDefaultRPr();
        rpr.setRFonts(rfonts);
        
    }
    
    
	/*
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 pdf
	 */
	private static File savePdf(WordprocessingMLPackage wordMLPackage) throws Exception {
		System.out.println(wordMLPackage.getMainDocumentPart().getContent().toString());
		File file = new File(genFilePath() + ".pdf");
        OutputStream os = null;
        
        try {
        	os = new FileOutputStream(file);
			Docx4J.toPDF(wordMLPackage, os);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (Docx4JException e) {
			throw e;
		} finally{
        	if (os != null){
                try {
                	os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }

        return file;
    }
	
	
	/*
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 docx
	 */
    public static File saveDocx(WordprocessingMLPackage wordMLPackage) throws Exception {
		System.out.println(wordMLPackage.getMainDocumentPart().getContent().toString());
        File file = new File(genFilePath() + ".docx");
        wordMLPackage.save(file); //保存到 docx 文件

        return file;
    }
    
    
    /*
     * 生成临时文件位置
     */
	private static String genFilePath() {
        return System.getProperty("user.dir") + "/" + System.currentTimeMillis();
    }
	
	private static String getContent(File templateFile, Map<String,Object> data)
			throws TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException {
		
		Configuration config = getFreemarkerConfig();
		config.setDefaultEncoding("UTF-8");
		config.setDirectoryForTemplateLoading(templateFile.getParentFile());
		//模版文件名
		Template template= config.getTemplate(templateFile.getName(),"UTF-8");
		template.setOutputEncoding("UTF-8");
		StringWriter writer = new StringWriter();
		template.process(data, writer);
		writer.flush();
		String html = writer.toString();
		return html;
	}

	
	private static Configuration getFreemarkerConfig()
			throws IOException {
		Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		config.setEncoding(Locale.CHINA, "utf-8");
		return config;
	}


	public static Font setChineseFont() {
		BaseFont bf = null;
		Font fontChinese = null;
		try {
			bf = BaseFont.createFont("resources/com/zfsoft/file/fonts/ARIALUNI.TTF",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			fontChinese = new Font(bf, 12, Font.NORMAL);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fontChinese;
	}

}
