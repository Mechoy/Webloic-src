package weblogic.xml.crypto.dsig.api.keyinfo;

import java.math.BigInteger;
import java.security.Provider;
import java.security.PublicKey;
import java.util.List;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;

public abstract class KeyInfoFactory {
   private String mechanismTyp;
   private Provider provider;

   protected KeyInfoFactory(String var1, Provider var2) {
   }

   public static KeyInfoFactory getInstance() throws XMLSignatureException {
      try {
         return (KeyInfoFactory)Class.forName(System.getProperty("weblogic.xml.crypto.dsig.api.KeyInfoFactory")).newInstance();
      } catch (Exception var1) {
         throw new XMLSignatureException("can't create signature factory");
      }
   }

   public static KeyInfoFactory getInstance(String var0) {
      return null;
   }

   public static KeyInfoFactory getInstance(String var0, Provider var1) {
      return null;
   }

   public static KeyInfoFactory getInstance(String var0, String var1) {
      return null;
   }

   public String getMechanismType() {
      return this.mechanismTyp;
   }

   public Provider getProvider() {
      return this.provider;
   }

   public abstract URIDereferencer getURIDereferencer();

   public abstract boolean isFeatureSupported(String var1);

   public abstract KeyInfo newKeyInfo(List var1);

   public abstract KeyInfo newKeyInfo(List var1, String var2);

   public abstract KeyName newKeyName(String var1);

   public abstract KeyValue newKeyValue(PublicKey var1);

   public abstract PGPData newPGPData(byte[] var1);

   public abstract PGPData newPGPData(byte[] var1, byte[] var2, List var3);

   public abstract PGPData newPGPData(byte[] var1, List var2);

   public abstract RetrievalMethod newRetrievalMethod(String var1);

   public abstract RetrievalMethod newRetrievalMethod(String var1, String var2, List var3);

   public abstract X509Data newX509Data(List var1);

   public abstract X509IssuerSerial newX509IssuerSerial(String var1, BigInteger var2);
}
