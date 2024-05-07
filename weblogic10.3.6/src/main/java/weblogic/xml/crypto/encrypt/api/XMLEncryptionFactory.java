package weblogic.xml.crypto.encrypt.api;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.spec.C14NMethodParameterSpec;
import weblogic.xml.crypto.encrypt.api.keyinfo.AgreementMethod;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;

public abstract class XMLEncryptionFactory {
   private final String mechanismType;
   private final Provider provider;
   private static final Map PROVIDERS = new HashMap();
   public static final String XMLNS = "http://www.w3.org/2001/04/xmlenc#";
   private static final String DEFAULT_CLASSNAME = "weblogic.xml.crypto.encrypt.WLXMLEncryptionFactory";
   public static final String FACTORY_PROPERTY = "weblogic.xml.crypto.encrypt.factory";
   public static final String FACTORY_PROPERTY_VALUE = System.getProperty("weblogic.xml.crypto.encrypt.factory");
   private static final String DEFAULT_FACTORY_CLASSNAME;

   protected XMLEncryptionFactory(String var1, Provider var2) {
      if (!"DOM".equals(var1)) {
         throw new IllegalArgumentException(var1 + " is unsupported; only \"DOM\" is supported.");
      } else {
         this.mechanismType = var1;
         this.provider = var2;
         synchronized(PROVIDERS) {
            PROVIDERS.put(var2, this);
         }
      }
   }

   public static XMLEncryptionFactory getInstance() throws XMLEncryptionException {
      try {
         return (XMLEncryptionFactory)PROVIDERS.values().iterator().next();
      } catch (Exception var1) {
         throw new XMLEncryptionException("can't create Encryption factory", var1);
      }
   }

   public static XMLEncryptionFactory getInstance(String var0) throws XMLEncryptionException {
      if (var0.equals("DOM")) {
         return getInstance();
      } else {
         throw new XMLEncryptionException("Unsupported mechanismType " + var0);
      }
   }

   public static XMLEncryptionFactory getInstance(String var0, Provider var1) throws XMLEncryptionException, NoSuchProviderException {
      if (var0.equals("DOM")) {
         return (XMLEncryptionFactory)PROVIDERS.get(var1);
      } else {
         throw new NoSuchProviderException("No such provider " + var1);
      }
   }

   public String getMechanismType() {
      return this.mechanismType;
   }

   public Provider getProvider() {
      return this.provider;
   }

   public boolean isFeaturedSupported() {
      return false;
   }

   public abstract AgreementMethod newAgreementMethod(String var1, byte[] var2, XMLStructure var3, XMLStructure var4, List var5) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;

   public abstract CanonicalizationMethod newCanonicalizationMethod(String var1, C14NMethodParameterSpec var2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

   public abstract CipherReference newCipherReference(String var1, List var2);

   public abstract DataReference newDataReference(String var1, List var2);

   public abstract EncryptedData newEncryptedData(TBE var1, EncryptionMethod var2, KeyInfo var3, EncryptionProperties var4, String var5, CipherReference var6);

   public abstract EncryptedKey newEncryptedKey(TBEKey var1, EncryptionMethod var2, KeyInfo var3, EncryptionProperties var4, List var5, String var6, String var7, String var8, CipherReference var9);

   public abstract EncryptionMethod newEncryptionMethod(String var1, Integer var2, EncryptionMethodParameterSpec var3) throws InvalidAlgorithmParameterException;

   public abstract EncryptionProperties newEncryptionProperties(List var1, String var2);

   public abstract EncryptionProperty newEncryptionProperty(List var1, String var2, String var3, Map var4);

   public abstract KeyReference newKeyReference(String var1, List var2);

   public abstract EncryptedType unmarshalEncryptedType(XMLDecryptContext var1) throws MarshalException;

   static {
      DEFAULT_FACTORY_CLASSNAME = FACTORY_PROPERTY_VALUE != null ? FACTORY_PROPERTY_VALUE : "weblogic.xml.crypto.encrypt.WLXMLEncryptionFactory";

      try {
         Class.forName(DEFAULT_FACTORY_CLASSNAME).newInstance();
      } catch (Exception var1) {
         throw new UnsupportedOperationException();
      }
   }
}
