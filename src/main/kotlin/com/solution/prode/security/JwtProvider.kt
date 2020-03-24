package com.solution.prode.security

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

@Component
class JwtProvider {

    @Value("\${app.tokenSecretKey}")
    private val secretKey: String? = null

    @Value("\${app.tokenExpiration}")
    private val tokenExpiration = 0

    fun generateToken(authentication: Authentication): String {

        val userDetail: UserDetailsImpl = authentication.getPrincipal() as UserDetailsImpl
        val now = Date()
        val expiryDate = Date(now.getTime() + tokenExpiration)
        // To generate new valid secretkeys:
        //SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS51
        //String base64Key = Encoders.BASE64.encode(key.getEncoded());
        val apiKeySecretBytes: ByteArray = DatatypeConverter.parseBase64Binary(secretKey)
        val signingKey: Key = SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName())
        return Jwts.builder()
                .setSubject((userDetail.id ?: 0).toString())
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact()
    }

    fun getUserIdFromJWT(token: String?): Long {

        val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body
        return claims.subject.toLong()
    }

    fun validateToken(authToken: String?): Boolean {

        try {

            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken)
            return true
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported token")
        } catch (ex: IllegalArgumentException) {
            logger.error("Token claims string is empty.")
        } catch (ex: Exception) {
            logger.error("Token error occurred.")
        }
        return false
    }

    companion object {

        private val logger: Logger = LoggerFactory.getLogger(JwtProvider::class.java)
    }
}