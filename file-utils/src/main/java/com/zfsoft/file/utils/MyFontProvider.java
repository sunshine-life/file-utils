package com.zfsoft.file.utils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;


public class MyFontProvider implements FontProvider {
    private BaseColor bc;
    private String fontname;
    private String encoding;
    private boolean embedded;
    private boolean cached;
    private float size;
    private int style;
    private BaseFont baseFont;

    public MyFontProvider(){}


    public BaseColor getBc() {
        return bc;
    }


    public void setBc(BaseColor bc) {
        this.bc = bc;
    }


    public String getFontname() {
        return fontname;
    }


    public void setFontname(String fontname) {
        this.fontname = fontname;
    }


    public String getEncoding() {
        return encoding;
    }


    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


    public boolean isEmbedded() {
        return embedded;
    }


    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }


    public boolean isCached() {
        return cached;
    }


    public void setCached(boolean cached) {
        this.cached = cached;
    }


    public float getSize() {
        return size;
    }


    public void setSize(float size) {
        this.size = size;
    }


    public int getStyle() {
        return style;
    }


    public void setStyle(int style) {
        this.style = style;
    }


    public BaseFont getBaseFont() {
        return baseFont;
    }


    public void setBaseFont(BaseFont baseFont) {
        this.baseFont = baseFont;
    }


    public MyFontProvider(BaseColor bc, String fontname, String encoding,
                          boolean embedded, boolean cached, float size, int style,
                          BaseFont baseFont) {
        super();
        this.bc = bc;
        this.fontname = fontname;
        this.encoding = encoding;
        this.embedded = embedded;
        this.cached = cached;
        this.size = size;
        this.style = style;
        this.baseFont = baseFont;
    }


    public Font getFont(String arg0, String arg1, boolean arg2, float arg3,
                        int arg4, BaseColor arg5) {
        Font font = null;
        if(baseFont==null){
            font = new Font();
        }else{
            font = new Font(baseFont);
        }
        font.setColor(arg5);
        font.setFamily(fontname);
        font.setSize(size);
        font.setStyle(arg4);
        return font;
    }


    public boolean isRegistered(String arg0) {
// TODO Auto-generated method stub
        return true;
    }
}