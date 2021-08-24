package io.github.churunfa.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Strings;
import org.apache.tomcat.util.codec.binary.Base64;


import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;

/**
 * @author crf
 */
public class JwtUtils {
    public static String getSecret() {
        return getSecret(40);
    }

    public static String getSecret(int len) {
        byte[] salt = new byte[len];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(System.currentTimeMillis());
        secureRandom.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

    public static String getUsername(String jwt) {
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        char[] var7 = jwt.toCharArray();
        int var8 = var7.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            char c = var7[var9];
            if (c == '.') {
                CharSequence tokenSeq = Strings.clean(sb);
                String token = tokenSeq != null ? tokenSeq.toString() : null;
                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                ++delimiterCount;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        String payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);

        Claims claims = null;
        if (payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') {
            Map<String, Object> claimsMap = readValue(payload);
            claims = new DefaultClaims(claimsMap);
        }

        if (claims == null) {
            throw new SignatureException("无效的jwt");
        }

        String username = claims.getSubject();
        return username;
    }

    private static Map<String, Object> readValue(String val) {
        try {
            return (Map)new ObjectMapper().readValue(val, Map.class);
        } catch (IOException var3) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, var3);
        }
    }

}
