package com.example.sadovod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class SiteScanner {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SiteClient siteClient;

   // @PostConstruct
    void run() {


        if (appConfig.getFirstRun()) {


            Thread worker = new Thread(this::task);//создали новый доп поток worker

            worker.start();//запустили новый потом

            //firs = false;//надо поменять значение
        }
    }

    void task() {

        String html = siteClient.getPage(5);

        Document htmlParsed = Jsoup.parse(html);

        //htmlParsed.getElementsByTag("a");берет теги
        //htmlParsed.getElementsByClass("col");берет классы
        //String value = htmlParsed.attr("href");берет href

        List<Element> rows = htmlParsed.getElementsByClass("list-products");

        List<Element> products = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            List<Element> tmp = rows.get(i).getElementsByClass("product");
            products.addAll(tmp);
        }

        List<Element> imgs = new ArrayList<>();
        List<String> srcs = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            List<Element> tmp = products.get(i).getElementsByTag("img");
            imgs.addAll(tmp);
        }

        for (int i = 0; i < imgs.size(); i++) {
            String value = imgs.get(i).attr("src");
            if (value.length() != 0){
                srcs.add(value);
                //System.out.println(value);
            }

        }

        try {
            //создать папку data
            File data = new File("./data");
            data.mkdirs();

            for (int i = 0; i < srcs.size(); i++) {
                String allpart = srcs.get(i);

                //allpart.substring(1, 2);
                //allpart.replaceAll("123", "456");
                //allpart.contains("123");
                allpart = allpart.substring(12, allpart.length());

                byte[] img = siteClient.getImg(allpart);
                allpart = allpart.replaceAll("/", "_");
                allpart = allpart.replaceAll(":", "_");
                allpart = allpart.replaceAll("&", "_");
                allpart = allpart.replaceAll("\\?", "_");

                //создаем файл
                File file = new File(data, allpart);
                file.createNewFile();

                //картинку записать в нутрь файла
                FileOutputStream output = new FileOutputStream(file);

                try (output){

                    output.write(img);

                    output.flush();//записываем на диск

                    output.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
