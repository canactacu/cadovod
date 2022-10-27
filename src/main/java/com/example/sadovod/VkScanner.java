package com.example.sadovod;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VkScanner {

    // @PostConstruct
    public ArrayList<String> read() {//достает ссылки из фаила

        ArrayList<String> str = new ArrayList<>();

        try {
            //создаем путь к файлу
            File oneFile = new File("input.txt");

            //вытаскиваем данные
            FileInputStream input = new FileInputStream(oneFile);

            try (input) {

                long size = oneFile.length();//размер пути к файлу

                //создаем массив
                byte[] data = new byte[(int) size];

                int total = input.read(data);
                input.close();

                String test = new String(data, StandardCharsets.UTF_8);

                String[] lines = test.split("\n");


                for (int i = 0; i < lines.length; i++) {
                    lines[i] = lines[i].trim();//откидываем пустоты
                    if (lines[i].length() > 14) {

                        String partLine = lines[i].substring(0, 5);

                        if (partLine.equals("https")) {
                            str.add(lines[i]);
                        }
                    }

                }

                // System.out.println(str);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Autowired
    private VkClient vkClient;
    //вызвать вк и распечать запись со стены

    @Value("${vk.secret}")
    private String vkSecret;

   // @PostConstruct
    public void importdata() {
        //скачать картинки по ссылкам

        ArrayList<String> srt = this.read();
        for (int i = 0; i < 1 /*srt.size()*/; i++) {
            String data = srt.get(i);
            data = data.substring(15, data.length());
            System.out.println();
            System.out.println("..........работает.........");
//            System.out.println(data);
            //offset = сколько нужно отстипить в выборе стены
            String json = vkClient.detPage(data, 0, vkSecret);
//            SaveToFile(json);


            Gson gson = new Gson();
            //с помошью библиотеки gson строку json  мы(комп сам)..
            //..перевели в обьект типа VkStructure
            VkStructure page = gson.fromJson(json, VkStructure.class);//

            parsePage(page);
        }


    }

    //находит все картинки и распечатывает размер и URL
    public void parsePage(VkStructure page) {
        if ((page.response != null) && (page.response.items != null)) {

            for (int i = 0; i < page.response.items.size(); i++) {

                if (page.response.items.get(i).attachments != null) {
                    for (int j = 0; j < page.response.items.get(i).attachments.size(); j++) {

                        if (page.response.items.get(i).attachments.get(j).photo != null) {

                            List<PhotoSizes> sizes = page.response.items.get(i).attachments.get(j).photo.sizes;

                            int maxWidth = 0;
                            String maxUrl = null;
                            for (int k = 0; k < sizes.size(); k++) {
                                String url = sizes.get(k).url;
                                int width = sizes.get(k).width;
//                                System.out.println(page.response.items.get(i).attachments.get(j).photo.id + " ширина = " + width + "   url = " + url);

                                if (width > maxWidth) {
                                    maxUrl = url;
                                    maxWidth = width;
                                }
                            }
                            System.out.println(
                                    " MaX ширина = " + maxWidth + "   url = " + maxUrl);
                            try {
                                URL url = new URL(maxUrl);//класс URL для работы со ссылками
                                InputStream stream = url.openStream();//позволяет скачать файл по указанному адресу и получить его в виде потока

                                try (stream) {//закрывам stream, если возникает ошибка

                                    int index = maxUrl.indexOf("?");
                                    maxUrl = maxUrl.substring(0, index);
                                    index = maxUrl.lastIndexOf(".");
                                    String ext = maxUrl.substring(index + 1, maxUrl.length());
                                    String name = "Vk/photo." + ext;
                                    File file = new File(name);

                                    if (!file.exists()) {
                                        file.getParentFile().mkdirs();
                                        file.createNewFile();
                                    }

                                    FileOutputStream output = new FileOutputStream(file);
                                    try (output) {

                                        byte[] data = new byte[1024];//блок данных = 1 кбайт
                                        //будем считывать пачками по 1 кбайту
                                        while (true) {

                                            int size = stream.read(data);//из stream пишем в data. размер

                                            if (size < 0) {//если данных не осталось,метод stream выдаст -1
                                                break;
                                            }

                                            output.write(data, 0, size);
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        } else {
            System.out.println(" Ошибка ");
        }

    }

    //функция для записи строки line в файл
    public void SaveToFile(String line) {
        try {
            //создаем путь к файлу
            File oneFile = new File("./Vk/dataVk.json");
            File folder = oneFile.getParentFile();
            //создаем необходимые папки
            folder.mkdirs();

            //создаем файл
            oneFile.createNewFile();

            //открыли на запись файл someFile
            FileOutputStream output = new FileOutputStream(oneFile);

            try (output) {

                //getBytes = преобразует символы в массив байтов
                byte[] data = line.getBytes(StandardCharsets.UTF_8);

                output.write(data);

                output.flush();//записываем на диск
            }
        }//обработка ошибок
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
