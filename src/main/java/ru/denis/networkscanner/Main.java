package ru.denis.networkscanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите ваше имя: ");
        String user_prompt = sc.nextLine();
        if (user_prompt.isBlank()) {
            System.out.println("Привет, незнакомец!");
        } else {
            System.out.println("Привет, " + user_prompt + "!");
        }
    }
}
