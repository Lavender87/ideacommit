package com.gupaoedu.vip.spring.formwork.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPView {

    public final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    private File viewFile;

    public GPView(File viewFile) {
        this.viewFile = viewFile;
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception{

        StringBuilder builder = new StringBuilder();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile,"r");
        String line = null;
        while(null!=(line= ra.readLine())){
            line = new String(line.getBytes("utf-8")); //"ISO-8859-1",
            Pattern pattern = Pattern.compile("￥\\{ ^\\}+\\}",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while(matcher.find()){
                String paramName = matcher.group();
                paramName =paramName.replaceAll("￥\\{|\\}}","");
                Object paramValue = model.get(paramName);
                if(null==paramValue)continue;
                line = matcher.replaceFirst(paramValue.toString());
                matcher =pattern.matcher(line);
            }
            builder.append(line);

        }

        response.setCharacterEncoding("utf-8");
        response.getWriter().write(builder.toString());
    }
}
