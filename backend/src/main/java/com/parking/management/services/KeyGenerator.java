import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // Створення секретного ключа для HMAC-SHA256
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // Кодування ключа у формат Base64
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        // Виведення згенерованого ключа на консоль
        System.out.println("Generated Key: " + encodedKey);
    }
}
