package lk.ijse.Salone.config;

import java.util.Random;

public class VerificationCodeGenerator {
    public static String generateCode(int length){
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i<length; i++){
            code.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return code.toString();
    }
}
