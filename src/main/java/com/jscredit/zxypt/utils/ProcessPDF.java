package com.jscredit.zxypt.utils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import sun.misc.IOUtils;

import javax.swing.*;

public class ProcessPDF {

    public static void main(String[] args) {
        // 生成pdf路径
        String DESC = "target/";
        String outputFile = DESC + System.currentTimeMillis() + "_process.pdf";
        try {
//            List<InputStream> pdfs = new ArrayList<InputStream>();
//            pdfs.add(new FileInputStream("e:\\123.pdf"));
//            pdfs.add(new FileInputStream("e:\\456.pdf"));
//            OutputStream output = new FileOutputStream("e:\\789.pdf");
//            concatPDFs(pdfs, output, true);

            /*ProcessPDF.addPdfImgMark("E:\\file2\\1502349579058.pdf",
                    "E:\\file2" + File.separator + "mark" + File.separator + "mark1111upGRAY.pdf",
                    "C:\\Users\\Administrator\\Desktop\\1127.png",
                    300,350);*/
            ProcessPDF.addPdfTextMark("E:\\file2\\1502349579058.pdf",
                    "E:\\file2" + File.separator + "mark" + File.separator + "mark2222upGRAY.pdf", "水印文字", 300,
                    350);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 给pdf文件添加水印
     * @param InPdfFile 要加水印的原pdf文件路径
     * @param outPdfFile 加了水印后要输出的路径
     * @param markImagePath 水印图片路径
     * @param imgWidth 图片横坐标
     * @param imgHeight 图片纵坐标
     * @throws Exception
     */
    public static void addPdfImgMark(String InPdfFile, String outPdfFile, String markImagePath,int imgWidth, int imgHeight) throws Exception {
        PdfReader reader = new PdfReader(InPdfFile, "PDF".getBytes());
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(new File(outPdfFile)));

        PdfContentByte under;

        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.3f);// 透明度设置

        Image img = Image.getInstance(markImagePath);// 插入图片水印

        img.setAbsolutePosition(imgWidth, imgHeight); // 坐标
        img.setRotation(-20);// 旋转 弧度
        img.setRotationDegrees(45);// 旋转 角度
        // img.scaleAbsolute(200,100);//自定义大小
        // img.scalePercent(50);//依照比例缩放

        int pageSize = reader.getNumberOfPages();// 原pdf文件的总页数
        for(int i = 1; i <= pageSize; i++) {
            under = stamp.getUnderContent(i);// 水印在之前文本下
            // under = stamp.getOverContent(i);//水印在之前文本上
            under.setGState(gs1);// 图片水印 透明度
            under.addImage(img);// 图片水印
        }

        stamp.close();// 关闭
        // // 删除不带水印文件
//		File tempfile = new File(InPdfFile);
//		if(tempfile.exists()) {
//			tempfile.delete();
//		}

    }

    /**
     *
     * <br>
     * <p>
     * Description: 给pdf文件添加水印<br>
     *
     * @param InPdfFile
     *            要加水印的原pdf文件路径
     * @param outPdfFile
     *            加了水印后要输出的路径
     * @param textMark
     *            水印文字
     * @param textWidth
     *            文字横坐标
     * @param textHeight
     *            文字纵坐标
     * @throws Exception
     */
    public static void addPdfTextMark(String InPdfFile, String outPdfFile, String textMark,int textWidth,
                                      int textHeight) throws Exception {
        PdfReader reader = new PdfReader(InPdfFile, "PDF".getBytes());
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(new File(outPdfFile)));

        PdfContentByte under;
        //字体
        String fontCn = FreemarkerUtil.getChineseFont();
        BaseFont font = BaseFont.createFont(fontCn+",1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        //BaseFont font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);// 使用系统字体

        int pageSize = reader.getNumberOfPages();// 原pdf文件的总页数
        for(int i = 1; i <= pageSize; i++) {
            under = stamp.getUnderContent(i);// 水印在之前文本下
            // under = stamp.getOverContent(i);//水印在之前文本上
            under.beginText();
            under.setColorFill(BaseColor.GRAY);// 文字水印 颜色
            under.setFontAndSize(font, 38);// 文字水印 字体及字号
            under.setTextMatrix(textWidth, textHeight);// 文字水印 起始位置
            under.showTextAligned(Element.ALIGN_CENTER, textMark, textWidth, textHeight, 45);
            under.endText();
        }

        stamp.close();// 关闭
    }

    /**
     * 倾斜文字水印
     */
    public static void setWaterMark(String InPdfFile, String outPdfFile, String waterMarkName) throws Exception {
        int interval = 0;
        /*ByteArrayOutputStream out = null;*/
        PdfReader pdfReader = null;
        PdfStamper pdfStamper = null;
        try {
            pdfReader = new PdfReader(InPdfFile, "PDF".getBytes());
            pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(new File(outPdfFile)));

            int total = pdfReader.getNumberOfPages();//获取PDF的总页数
            Rectangle pageRect = null;
            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();//字符串的高,   只和字体有关
            textW = metrics.stringWidth(label.getText());//字符串的宽

            PdfContentByte waterContent = null;
            // 设置字体
            String fontCn = FreemarkerUtil.getChineseFont();
            BaseFont font = BaseFont.createFont(fontCn+",1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            PdfGState gs = new PdfGState();
            //透明度
            gs.setFillOpacity(0.5f);// 设置透明度为0.2
            for (int i = 1; i <= total; i++) {
                pageRect=pdfReader.getPageSizeWithRotation(i);//获取页
                waterContent = pdfStamper.getUnderContent(i);//在内容下方加水印
                //under = stamp.getOverContent(i);//水印在之前文本上
                waterContent.setGState(gs);
                //开始写入文本
                waterContent.beginText();
                waterContent.setColorFill(BaseColor.GRAY);
                waterContent.setFontAndSize(font, 13);
                waterContent.setTextMatrix(0, 0);
                // 计算水印X,Y坐标
                float x = pageRect.getWidth() / 2;
                float y = pageRect.getHeight() / 2;
                for (int height = interval + textH; height < pageRect.getHeight();
                     height = height + textH*8) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW;
                         width = width + textW) {
                        waterContent.showTextAligned(Element.ALIGN_LEFT
                                , waterMarkName, width - textW,
                                height - textH, 45);
                    }
                }
                waterContent.endText();
            }
            pdfStamper.close();
        } finally {
            if (pdfReader != null)
                pdfReader.close();
        }

    }

    /**
     * 图片水印
     */
    public  byte[] imageWatermark(byte[] pdfBty, String imagePath) throws Exception {
        PdfReader reader = null;
        PdfStamper stamp = null;
        try {

            reader = new PdfReader(pdfBty);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            stamp = new PdfStamper(reader, out);
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(1f);

            Image image = Image.getInstance(getContent(imagePath));
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                PdfContentByte pdfContentByte = stamp.getUnderContent(i);
                pdfContentByte.setGState(gs1);
                /* 设置图片的位置 */
                image.setAbsolutePosition(0, 0);
                /* 设置图片的大小 （A4）*/
                image.scaleAbsolute(595, 842);

                pdfContentByte.addImage(image);
            }

            stamp.close();
            return out.toByteArray();
        } finally {
            reader.close();
        }
    }

    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
