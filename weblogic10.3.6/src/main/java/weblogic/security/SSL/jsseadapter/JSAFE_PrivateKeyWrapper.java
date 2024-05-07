package weblogic.security.SSL.jsseadapter;

import com.rsa.jsafe.JSAFE_InvalidKeyException;
import com.rsa.jsafe.JSAFE_InvalidParameterException;
import com.rsa.jsafe.JSAFE_PrivateKey;
import com.rsa.jsafe.JSAFE_UnimplementedException;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.logging.Level;

class JSAFE_PrivateKeyWrapper implements RSAPrivateKey, RSAPrivateCrtKey {
   private JSAFE_PrivateKey key;
   BigInteger crtCoefficient;
   BigInteger primeExponentP;
   BigInteger primeExponentQ;
   BigInteger primeP;
   BigInteger primeQ;
   BigInteger publicExponent;
   BigInteger modulus;

   public BigInteger getCrtCoefficient() {
      return this.crtCoefficient;
   }

   public BigInteger getPrimeExponentP() {
      return this.primeExponentP;
   }

   public BigInteger getPrimeExponentQ() {
      return this.primeExponentQ;
   }

   public BigInteger getPrimeP() {
      return this.primeP;
   }

   public BigInteger getPrimeQ() {
      return this.primeQ;
   }

   public BigInteger getPublicExponent() {
      return this.publicExponent;
   }

   private byte[] ensurePositiveUnsigned(byte[] var1) {
      if ((var1[0] & 128) == 0) {
         return var1;
      } else {
         byte[] var2 = new byte[var1.length + 1];
         var2[0] = 0;
         System.arraycopy(var1, 0, var2, 1, var1.length);
         return var2;
      }
   }

   private void initFromKey() throws JSAFE_UnimplementedException {
      byte[][] var1 = this.key.getKeyData("RSAPrivateKeyCRT");
      this.modulus = new BigInteger(this.ensurePositiveUnsigned(var1[0]));
      this.publicExponent = new BigInteger(this.ensurePositiveUnsigned(var1[1]));
      this.primeP = new BigInteger(this.ensurePositiveUnsigned(var1[3]));
      this.primeQ = new BigInteger(this.ensurePositiveUnsigned(var1[4]));
      this.primeExponentP = new BigInteger(this.ensurePositiveUnsigned(var1[5]));
      this.primeExponentQ = new BigInteger(this.ensurePositiveUnsigned(var1[6]));
      this.crtCoefficient = new BigInteger(this.ensurePositiveUnsigned(var1[7]));
   }

   public boolean equals(Object var1) {
      return var1 == this || var1 instanceof JSAFE_PrivateKeyWrapper && this.key.equals(((JSAFE_PrivateKeyWrapper)var1).key);
   }

   public int hashCode() {
      int var1 = this.crtCoefficient.hashCode();
      var1 = 31 * var1 + this.primeExponentP.hashCode();
      var1 = 31 * var1 + this.primeExponentQ.hashCode();
      var1 = 31 * var1 + this.primeP.hashCode();
      var1 = 31 * var1 + this.primeQ.hashCode();
      var1 = 31 * var1 + this.publicExponent.hashCode();
      var1 = 31 * var1 + this.modulus.hashCode();
      return var1;
   }

   public JSAFE_PrivateKeyWrapper(byte[] var1) {
      try {
         this.key = JSAFE_PrivateKey.getInstance(var1, 0, "Java");
         this.initFromKey();
      } catch (JSAFE_UnimplementedException var3) {
         if (JaLogger.isLoggable(Level.SEVERE)) {
            JaLogger.log(Level.SEVERE, JaLogger.Component.SSLCONTEXT, "Error instantiating PrivateKey object: ", var3.getMessage());
         }

         throw new IllegalStateException(var3.getMessage());
      }
   }

   public JSAFE_PrivateKeyWrapper(BigInteger var1, BigInteger var2, BigInteger var3, BigInteger var4, BigInteger var5, BigInteger var6, BigInteger var7, BigInteger var8) {
      try {
         this.key = JSAFE_PrivateKey.getInstance("RSA", "Java");
         this.key.setKeyData(new byte[][]{var1.toByteArray(), var2.toByteArray(), var3.toByteArray(), var4.toByteArray(), var5.toByteArray(), var6.toByteArray(), var7.toByteArray(), var8.toByteArray()});
         this.initFromKey();
      } catch (JSAFE_UnimplementedException var10) {
         throw new IllegalStateException(var10.getMessage());
      } catch (JSAFE_InvalidKeyException var11) {
         throw new IllegalStateException(var11.getMessage());
      } catch (JSAFE_InvalidParameterException var12) {
         throw new IllegalStateException(var12.getMessage());
      }
   }

   public JSAFE_PrivateKeyWrapper(JSAFE_PrivateKey var1) {
      this.key = var1;

      try {
         this.initFromKey();
      } catch (JSAFE_UnimplementedException var3) {
         if (JaLogger.isLoggable(Level.SEVERE)) {
            JaLogger.log(Level.SEVERE, JaLogger.Component.SSLCONTEXT, "Error setting PrivateKey object: ", var3.getMessage());
         }

         throw new IllegalStateException(var3.getMessage());
      }
   }

   public BigInteger getModulus() {
      return this.modulus;
   }

   public BigInteger getPrivateExponent() {
      byte[][] var1 = this.key.getKeyData();
      return new BigInteger(var1[1]);
   }

   public JSAFE_PrivateKey getKey() {
      return this.key;
   }

   public String getAlgorithm() {
      return "RSA";
   }

   public String getFormat() {
      return "PKCS#8";
   }

   public byte[] getEncoded() {
      try {
         return this.key.getKeyData("RSAPrivateKeyBER")[0];
      } catch (JSAFE_UnimplementedException var2) {
         if (JaLogger.isLoggable(Level.SEVERE)) {
            JaLogger.log(Level.SEVERE, JaLogger.Component.SSLCONTEXT, "Error getting encoded value of the PrivateKey object: ", var2.getMessage());
         }

         throw new IllegalStateException();
      }
   }
}
