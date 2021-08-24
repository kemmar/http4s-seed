package com.fiirb.util

import cats.effect.Sync

import java.net.{URLDecoder, URLEncoder}
import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.{Base64, UUID}
import javax.crypto.spec.{PBEKeySpec, SecretKeySpec}
import javax.crypto.{Cipher, SecretKeyFactory}
import scala.util.Try

object EncryptionUtil {

  private[this] val KeySalt = "0495c728-1614-41f6-8ac3-966c22b4a62d".getBytes(StandardCharsets.UTF_8)
  private[this] val AES = "AES"
  private[this] val Algorithm = AES + "/ECB/PKCS5Padding"
  private[this] val HashingIterations = 999999
  private[this] val KeySizeBits = 128

  private lazy val K = UUID.randomUUID().toString

  private lazy val prepareDefaultKey: SecretKeySpec = new SecretKeySpec(hashPassword(K.toCharArray, KeySalt), AES)

  private def hashPassword(password: Array[Char],
                           salt: Array[Byte],
                           iterations: Int = HashingIterations,
                           keyLength: Int = KeySizeBits,
                           hashingAlgorithm: String = "PBKDF2WithHmacSHA512"): Array[Byte] =
    try {
      val keyFactory = SecretKeyFactory.getInstance(hashingAlgorithm)
      val keySpec = new PBEKeySpec(password, salt, iterations, keyLength)
      val key = keyFactory.generateSecret(keySpec)
      key.getEncoded
    } catch {
      case e@(_: NoSuchAlgorithmException | _: InvalidKeySpecException) => throw new RuntimeException("Password hashing error", e)
    }

  def encrypt[F[_] : Sync](text: String): F[String] =
    Sync[F].fromTry {
      Try {
        val cipher = Cipher.getInstance(Algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, prepareDefaultKey)
        URLEncoder.encode(new String(Base64.getEncoder.encode(cipher.doFinal(text.getBytes()))), "UTF-8")
      }
    }

  def decrypt[F[_] : Sync](encryptedString: String): F[String] =
    Sync[F].fromTry {
      Try {
        val cipher: Cipher = Cipher.getInstance(Algorithm)
        cipher.init(Cipher.DECRYPT_MODE, prepareDefaultKey)

        new String(cipher.doFinal(Base64.getDecoder.decode(URLDecoder.decode(encryptedString, "UTF-8"))))
      }
    }
}