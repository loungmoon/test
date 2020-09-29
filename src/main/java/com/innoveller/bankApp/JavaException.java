package com.innoveller.bankApp;
public class JavaException {
    public static void main(String[] args) {
        int a = 0;
        int d = 10;
//        int b = d/ 1;
//        int f = d / a;
//        System.out.println(f+b);
        try {
            int b = d/ 1;
            int f = d / a;
            System.out.println(f+b);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
