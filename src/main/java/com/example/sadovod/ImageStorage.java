package com.example.sadovod;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//будем сравнивать картики
@Component
public class ImageStorage {

    //список хешей 256
    private List<byte[]> allSha256 = new CopyOnWriteArrayList<>();

    //список хешей ph
    private List<byte[]> allPh = new CopyOnWriteArrayList<>();


    //функция, которая будет тестировать хеш байтов,черно белый и цв хеш.
    @PostConstruct
    public void test(){

            //создаем путь к файлу
            File data = new File("./data");

            data.listFiles();//список файлов в папке

        for (int i = 0; i < data.listFiles().length; i++) {
            File file = data.listFiles()[i];
            check(file);

            System.out.println(file);
        }


    }

    //функция для сохраненния файла в базе и проверке ее на похожие
    public void check(File file){
        byte[] hash = calculateSHA256(file);
        boolean match = checkSha256(hash); //allSha256
        if (match){return 1;}
        else{
            byte[] ph = calculatePh(match, file);
            boolean similar = checkPH(ph); //allPh

            if (similar){
             //нашли похожие. хеш sha сохраняем. хеш ph сохраняем

            }
            else {
                //не нашли похожие.оба хеша сохраняем
            }
        }

    }

    //функция подсчета хеша
    public byte[] calculateSHA256(File file){
        //file -> byte[] data (1)
        try {
            //вытаскиваем данные
            FileInputStream input = new FileInputStream(file);

            try(input){

                byte[] data = new  byte[(int)file.length()];

                input.read(data);
                input.close();


                //byte[] data -> byte[] sha (2)
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(data);

                return hash;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    //сравнения хешей или записи
    public boolean checkSha256(byte[] hash){

        for (int i = 0; i < allSha256.size(); i++) {
            boolean e = Arrays.equals(hash, allSha256.get(i));//сравниваем

            if (e){
                return true;
            }
        }
        return false;
    }

    //считаем хеш перцентивный для картинки
    public byte[] calculatePh(boolean match, File file){
        try {
            BufferedImage image = ImageIO.read(file);//возвращает значение обьекта загруженной картинки
            Image image1 = image.getScaledInstance(32, 32, Image.SCALE_DEFAULT);//сжимаем картинку

            BufferedImage result = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);//пустой,но нужноо формата
            result.getGraphics().drawImage(image1, 0, 0, null);//записали в пустой(уже нужного формата),наше изображение

            int[] pixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();//достали массив int

            int[] greys = new int[pixels.length];
            int mid = 0;//среднее арифметическое
            for (int i = 0; i < pixels.length; i++) {
                Color color = new Color(pixels[i]);
                int grey = (color.getRed() + color.getBlue() + color.getGreen())/3;
                greys[i] = grey;
                mid = mid + grey;
            }

            mid = mid / pixels.length;

            //наш хеш
            byte[] mask = new byte[pixels.length/8];//32*32 кратно 8, поэтому нет + 1
            for (int i = 0; i < pixels.length; i++) {
                if (greys[i] > mid){//темная часть картины

                    //
                    ByteUtils.set1(mask, i);

                }
            }

            return mask;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //сравниваем хеш PH с другими хешами
    public boolean checkPH(byte[] ph){
        int a = (32 * 32)/10;//посчитали максимальное расстояние для пхожих картинок

        for (int i = 0; i < allPh.size(); i++) {
            int d = ByteUtils.distance(ph, allPh.get(i));
            if (d < a){
                return true;//значт картинки похожи
            }
        }
        if (allPh.size() == 0){
            return false;//картинка первая
        }
        return false;//картинки не похожи
    }

    //сохраняем в бд похожие хеши
    public void save(byte[] a1){

    }
}

