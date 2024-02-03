package com.therainbowville.minegasm;

public class Simulator {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new Simulator().getGreeting());
    }
}
