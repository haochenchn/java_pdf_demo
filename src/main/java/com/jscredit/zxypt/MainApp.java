package com.jscredit.zxypt;

import com.jscredit.zxypt.model.User;
import com.jscredit.zxypt.utils.FreemarkerUtil;
import com.jscredit.zxypt.utils.PdfGenerateUtil;
import com.jscredit.zxypt.utils.ProcessPDF;
import com.jscredit.zxypt.utils.ResourceLoader;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主应用
 */
public class MainApp {
    public static void main(String[] args) throws Exception {
        String DESC = "target/";
        // classpath 中模板路径
        //String temp = "fileTemplate.html";
        String temp = "freemakerTest.html";

        String s = FreemarkerUtil.fileRead(ResourceLoader.getPath("")+"data.json");
        JSONObject jsonObject = new JSONObject().fromObject(s);
        List<Map<String, Object>> values = FreemarkerUtil.toList(jsonObject.get("data"));
        Map<String, Object> map = new HashMap<>();
        map.put("json", values);
        //String ss = new File(outputFileClass).getParentFile().getParent();
        //System.out.println(ss);
        /*Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name","hello 转PDF");*/

        //Map<String, Object> variables = getContent();
        String htmlStr = FreemarkerUtil.generateHtmlStr(temp, map);
        System.out.println(htmlStr);

        // 生成pdf路径
        String outputFile = DESC + System.currentTimeMillis() + ".pdf";

        PdfGenerateUtil.createPdf(htmlStr, outputFile);
        // 生成pdf路径
        //OutputStream out = new FileOutputStream(outputFile);

        //PdfGenerateUtil.generatePdf(htmlStr, out);
        //PdfDocumentGenerator.pdfgenerate(htmlStr, outputFile);

       /* ProcessPDF.addPdfTextMark(outputFile, outputFile.substring(0,outputFile.lastIndexOf(".")) + "_mark.pdf", "水印文字", 300,350);*/
        //ProcessPDF.waterMark(outputFile,outputFile.substring(0,outputFile.lastIndexOf(".")) + "_mark.pdf", "水印文字",16);
        ProcessPDF.setWaterMark(outputFile,outputFile.substring(0,outputFile.lastIndexOf(".")) + "_mark.pdf","水印文字111");

    }


    public static Map<String, Object> getContent() {
        // 从数据库中获取数据， 出于演示目的， 这里数据不从数据库获取， 而是直接写死
        Map<String, Object> variables = new HashMap<>();
        List<User> userList = new ArrayList<>();

        User tom = new User("Tom", 19, 1);
        User amy = new User("Amy", 28, 0);
        User leo = new User("Leo", 23, 1);

        userList.add(tom);
        userList.add(amy);
        userList.add(leo);
        String path1 = ResourceLoader.getPath("css/") + "pdf.css";
        String path2 = ResourceLoader.getPath("images/") + "aloner.jpg";
        variables.put("path1", path1);
        variables.put("path2", path2);
        variables.put("title", "用户列表");
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("values2", map);
        map.put("userList", userList);
        variables.put("values", map2);

        return variables;
    }
}
