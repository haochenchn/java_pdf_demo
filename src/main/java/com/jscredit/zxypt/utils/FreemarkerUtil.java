package com.jscredit.zxypt.utils;

import java.io.*;
import java.util.*;

import com.jscredit.zxypt.freemaker.FreemarkerConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 */
public class FreemarkerUtil {
    public static String CHARSET_NAME = "UTF-8";

    public static Template getTemplate(String name) {
        try {
            //通过Freemarker的Configuration读取相应的ftl
            Configuration configuration = FreemarkerConfiguration.getConfiguation();//这里是对应的你使用jar包的版本号：<version>2.3.23</version>

            //configuration.setDirectoryForTemplateLoading(new File("/ftl")); //如果是maven项目可以使用这种方式
            //第二个参数 为你对应存放.ftl文件的包名
            //configuration.setClassForTemplateLoading(this.getClass(), "/ftl");

            Template template = configuration.getTemplate(name);

            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传入模版路径 和数据生成html 返回html 的内容
     * template 路径template/下的模版名 variables数据
     */
    public static String generateHtmlStr(String template, Map<String, Object> variables) throws IOException, TemplateException {
        Template tp = getTemplate(template);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        tp.setEncoding(CHARSET_NAME);
        tp.process(variables, writer);
        String htmlStr = stringWriter.toString();
        writer.flush();
        writer.close();
        return htmlStr;
    }

    /**
     * 获取中文字体位置
     * @return
     */
    public static String getChineseFont() {

        String chineseFont = null;
        chineseFont = ResourceLoader.getPath("font/")+"SIMSUN.TTC";
        if(!new File(chineseFont).exists()){
            throw new RuntimeException("字体文件不存在,影响导出pdf中文显示！"+chineseFont);
        }
        return chineseFont;
    }

    public void print(String name, Map<String, Object> root) {
        //通过Template可以将模版文件输出到相应的文件流
        Template template = this.getTemplate(name);
        try {
            template.process(root, new PrintWriter(System.out));//在控制台输出内容
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 输出HTML文件
     *
     * @param name
     * @param root
     * @param outFile
     */
    public void fprint(String name, Map<String, Object> root, String outFile) {
        FileWriter out = null;
        try {
            // 通过一个文件输出流，就可以写到相应的文件中，此处用的是绝对路径
            out = new FileWriter(new File(ResourceLoader.getPath("") + outFile));
            Template temp = this.getTemplate(name);
            temp.process(root, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String fileRead(String filePath) throws Exception {
        File file = new File(filePath);//定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
            System.out.println(s);
        }
        bReader.close();
        String str = sb.toString();
        System.out.println(str );
        return str;
    }

    /***
     * 将对象转换为List>
     * @param object
     * @return
     */
    // 返回非实体类型(Map)的List
    public static List<Map<String, Object>> toList(Object object)
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = JSONArray.fromObject(object);
        for (Object obj : jsonArray)
        {
            JSONObject jsonObject = (JSONObject) obj;
            Map<String, Object> map = new HashMap<String, Object>();
            Iterator it = jsonObject.keys();
            while (it.hasNext())
            {
                String key = (String) it.next();
                Object value = jsonObject.get(key);
                map.put((String) key, value);
            }
            list.add(map);
        }
        return list;
    }

}