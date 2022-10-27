package com.example.sadovod;

public class ByteUtils {
    public static void set1(byte[] bytes, int index){

        int number = index / 8; //получаем номер байта
        int index1 = index % 8;//остаток от деления = нужный нам индекс

        byte bytes1 = bytes[number];

        bytes[number] = set1(bytes1, index1);

    }

    public static void set0(byte[] bytes, int index){

        int number = index / 8; //получаем номер байта
        int index1 = index % 8;//остаток от деления = нужный нам индекс

        byte bytes1 = bytes[number];

        bytes[number] = set0(bytes1, index1);
    }

    public static boolean get(byte[] bytes, int index){
        int number = index / 8; //получаем номер байта
        int index1 = index % 8;//остаток от деления = нужный нам индекс

        byte bytes1 = bytes[number];
        return get(bytes1, index1);

    }

    public static byte set1(byte bytes, int index){

        bytes = (byte) ( bytes | (1 << index));

        return bytes;
    }

    public static byte set0(byte bytes, int index){

        bytes = (byte) (bytes & ~(1 << index));

        return bytes;
    }


    public static boolean get(byte bytes, int index){

        boolean result = (byte) (bytes & (1 << index)) != 0;

        return result;
    }

    //функция считает расстояние между хешами
    public static int distance(byte[] a, byte[] b){

        int resuit = 0;
        for (int i = 0; i < a.length * 8; i++) {

            if (get(a, i) != get(b, i)){
                resuit = resuit + 1;
            }
        }
        return resuit;
    }
}
