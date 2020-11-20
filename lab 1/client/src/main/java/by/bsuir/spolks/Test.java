package by.bsuir.spolks;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {


        byte[] array=new byte[30];
        byte[] array2={1,2,3,4};

        System.out.println(Arrays.toString(array));
        System.out.println(Arrays.toString(array2));

        setArrayToAnotherArray(array, array2, 3, 4);

        System.out.println(Arrays.toString(array));
        System.out.println(Arrays.toString(array2));
    }

    public static void setArrayToAnotherArray(byte[] array, byte[] insertArray, int offset, int length){

        for(int i=0; i<length; ++i){
            array[offset+i]=insertArray[i];
        }
    }
}
