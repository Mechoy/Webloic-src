package weblogic.xml.crypto.dsig.keyinfo;

import java.math.BigInteger;
import java.security.Provider;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfoFactory;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyName;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyValue;
import weblogic.xml.crypto.dsig.api.keyinfo.PGPData;
import weblogic.xml.crypto.dsig.api.keyinfo.RetrievalMethod;
import weblogic.xml.crypto.dsig.api.keyinfo.X509Data;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;

public class KeyInfoFactoryImpl extends KeyInfoFactory {
   public KeyInfoFactoryImpl() {
      super((String)null, (Provider)null);
   }

   public URIDereferencer getURIDereferencer() {
      return null;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public KeyInfo newKeyInfo(List var1) {
      return new KeyInfoImpl(var1);
   }

   public KeyInfo newKeyInfo(List var1, String var2) {
      return new KeyInfoImpl(var1, var2);
   }

   public KeyName newKeyName(String var1) {
      return new KeyNameImpl(var1);
   }

   public KeyValue newKeyValue(PublicKey var1) {
      if (var1 instanceof DSAPublicKey) {
         return new DSAKeyValue((DSAPublicKey)var1);
      } else if (var1 instanceof RSAPublicKey) {
         return new RSAKeyValue((RSAPublicKey)var1);
      } else {
         throw new IllegalArgumentException("Unknown PublicKey type: " + var1);
      }
   }

   public PGPData newPGPData(byte[] var1) {
      return null;
   }

   public PGPData newPGPData(byte[] var1, byte[] var2, List var3) {
      return null;
   }

   public PGPData newPGPData(byte[] var1, List var2) {
      return null;
   }

   public RetrievalMethod newRetrievalMethod(String var1) {
      return null;
   }

   public RetrievalMethod newRetrievalMethod(String var1, String var2, List var3) {
      return new RetrievalMethodImpl(var1, var2, var3);
   }

   public X509Data newX509Data(List var1) {
      return new X509DataImpl(var1);
   }

   public X509IssuerSerial newX509IssuerSerial(String var1, BigInteger var2) {
      return new X509IssuerSerialImpl(var1, var2);
   }
}
