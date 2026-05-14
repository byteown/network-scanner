package ru.denis.networkscanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Введите ваше имя: ");
            String userName = sc.nextLine();
            if (userName.isBlank()) {
                System.out.println("Привет, незнакомец!");
            } else {
                System.out.println("Привет, " + userName + "!");
            }
        }
    }
}
