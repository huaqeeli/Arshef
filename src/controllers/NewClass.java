package controllers;

import java.io.IOException;

public class NewClass {
    
    static Config config = new Config();
    
    public static void main(String[] args) throws IOException {
        System.out.println(config.getAppURL()+"\\fonts\\URW-DIN-Arabic.ttf");      
    }
}
