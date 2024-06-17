package com.minizin.travel.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    @Test
    @DisplayName("암호화 결과 출력")
    void stringEncryptor() {
        String text = "text";

        System.out.println("encrypted: " + jasyptEncoding(text));
    }

    private String jasyptEncoding(String value) {

        String key = "my_jasypt_key";

        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        pbeEnc.setPassword(key);
        pbeEnc.setIvGenerator(new RandomIvGenerator());

        return pbeEnc.encrypt(value);
    }
}