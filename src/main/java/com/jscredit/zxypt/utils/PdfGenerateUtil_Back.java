package com.jscredit.zxypt.utils;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.itextpdf.text.Image;

/**
 *
 */
public class PdfGenerateUtil_Back {


    public static void generatePdf(String htmlStr, OutputStream out) throws Exception {
        //final ServletContext servletContext = getServletContext();
        BaseFont fontChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//设置中文字体
        Font chinese = new Font(fontChinese, 10, Font.NORMAL);

        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        document.setMargins(30, 30, 30, 30);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        /**
         * HeaderFooter的第2个参数为非false时代表打印页码
         * 页眉页脚中也可以加入图片，并非只能是文字
         */
        HeaderFooter header=new HeaderFooter(new Phrase("这仅仅是个页眉，页码在页脚处",chinese),false);
        //设置是否有边框等
        //header.setBorder(Rectangle.NO_BORDER);
        header.setBorder(Rectangle.BOTTOM);
        header.setAlignment(1);
        header.setBorderColor(Color.red);
        document.setHeader(header);

        document.open();

        // html内容解析
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(
                new CssAppliersImpl(new XMLWorkerFontProvider() {
                    @Override
                    public com.itextpdf.text.Font getFont(String fontname, String encoding, float size, final int style) {
                        com.itextpdf.text.Font font = null;
                        if (fontname == null) {
                            //字体
                            String fontCn = FreemarkerUtil.getChineseFont();
                            com.itextpdf.text.pdf.BaseFont bf;
                            try {
                                //注意这里有一个,1
                                bf = com.itextpdf.text.pdf.BaseFont.createFont(fontCn+",1",
                                        com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.NOT_EMBEDDED);
                                font = new com.itextpdf.text.Font(bf, size, style);
                            } catch (com.itextpdf.text.DocumentException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        return font;
                    }
                })) {
            @Override
            public HtmlPipelineContext clone()
                    throws CloneNotSupportedException {
                HtmlPipelineContext context = super.clone();
                try {
                    ImageProvider imageProvider = this.getImageProvider();
                    context.setImageProvider(imageProvider);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return context;
            }
        };

        // 图片解析
        htmlContext.setImageProvider(new AbstractImageProvider() {

            // String rootPath = servletContext.getRealPath("/");

            @Override
            public String getImageRootPath() {
                return "";
            }

            @Override
            public Image retrieve(String src) {
                if (StringUtils.isEmpty(src)) {
                    return null;
                }
                try {
                    // String imageFilePath = new File(rootPath, src).toURI().toString();
                    Image image = Image.getInstance(src);
                    image.setAbsolutePosition(400, 400);
                    if (image != null) {
                        store(src, image);
                        return image;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return super.retrieve(src);
            }
        });
        htmlContext.setAcceptUnknown(true).autoBookmark(true)
                .setTagFactory(Tags.getHtmlTagProcessorFactory());

        // css解析
        CSSResolver cssResolver = XMLWorkerHelper.getInstance()
                .getDefaultCssResolver(true);
        cssResolver.setFileRetrieve(new FileRetrieve() {
            @Override
            public void processFromStream(InputStream in,
                                          ReadingProcessor processor) {
                try (InputStreamReader reader = new InputStreamReader(in,
                        FreemarkerUtil.CHARSET_NAME)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Throwable e) {
                }
            }

            // 解析href
            @Override
            public void processFromHref(String href, ReadingProcessor processor)
                    throws IOException {
                // InputStream is = servletContext.getResourceAsStream(href);
                URL url = new URL(href);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream is = conn.getInputStream();

                try (InputStreamReader reader = new InputStreamReader(is,
                        FreemarkerUtil.CHARSET_NAME)) {
                    int i = -1;
                    while (-1 != (i = reader.read())) {
                        processor.process(i);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

        /*HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext,
                new PdfWriterPipeline(document, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
                htmlPipeline);
        XMLWorker worker = null;
        worker = new XMLWorker(pipeline, true);
        XMLParser parser = new XMLParser(true, worker,
                Charset.forName(FreemarkerUtil.CHARSET_NAME));
        try (InputStream inputStream = new ByteArrayInputStream(
                htmlStr.getBytes())) {
            parser.parse(inputStream, Charset.forName(FreemarkerUtil.CHARSET_NAME));
        }*/
        document.close();
    }
}
