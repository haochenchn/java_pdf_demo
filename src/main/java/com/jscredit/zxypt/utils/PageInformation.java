package com.jscredit.zxypt.utils;


import java.io.IOException;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
          

public class PageInformation {
          
    public static final String filename = "target\\1533521204672.pdf";
          
    public static void main(String[] args){
        try {
            inspect(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
          
    public static void inspect(String filename) throws IOException {
        PdfReader reader = new PdfReader(filename);
        System.out.println(filename);
        System.out.println("Number of pages: " + reader.getNumberOfPages());
        Rectangle mediabox = reader.getPageSize(1);
        System.out.print("Size of page 1: [");
        System.out.print(mediabox.getLeft());
        System.out.print(',');
        System.out.print(mediabox.getBottom());
        System.out.print(',');
        System.out.print(mediabox.getRight());
        System.out.print(',');
        System.out.print(mediabox.getTop());
         System.out.println("]");
         System.out.print("Rotation of page 1: ");
         System.out.println(reader.getPageRotation(1));
         System.out.print("Page size with rotation of page 1: ");
         System.out.println(reader.getPageSizeWithRotation(1));
         System.out.print("Is rebuilt? ");
         System.out.println(reader.isRebuilt());
         System.out.print("Is encrypted? ");
         System.out.println(reader.isEncrypted());
         System.out.println();
    }
}