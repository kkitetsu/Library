package com.example.todo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ハッシュ生成を行うクラス
 */
public class HashGenerator {
    // ハッシュ化を行うメソッド
    public static String generateHash(String val) throws NoSuchAlgorithmException {
        // MessageDigestは、Javaで暗号化およびハッシュ化の機能を提供するクラス
        // SHA-256というハッシュアルゴリズムを利用することを指定してインスタンス化
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        // 文字列をバイト配列に変換
        byte[] inputBytes = val.getBytes();
        
        // バイト配列をハッシュ化
        byte[] hashedBytes = md.digest(inputBytes);
        
        // バイト配列を16進数文字列に変換
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            // 各バイトを16進数文字列に変換
            // 0xff & b は、バイト値を符号なし整数に変換し、16進数文字列に変換する
            String hex = Integer.toHexString(0xff & b);
            // 16進数文字列が1桁の場合0を付与して2桁にする
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        // 最終的な16進数のハッシュ値を文字列にする
        return hexString.toString();
    }
}
