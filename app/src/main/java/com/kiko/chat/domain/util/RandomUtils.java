package com.kiko.chat.domain.util;

import java.util.Random;

public class RandomUtils {
    public static String getRandomNameOfFile(int len) {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345674890";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < len; i++) {
            builder.append(lexicon.charAt(random.nextInt(lexicon.length())));
        }

        return builder.toString();
    }
}
