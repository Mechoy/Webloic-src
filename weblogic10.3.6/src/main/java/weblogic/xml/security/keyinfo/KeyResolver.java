package weblogic.xml.security.keyinfo;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.xml.security.wsse.KeyIdentifier;
import weblogic.xml.security.wsse.SecurityTokenReference;

public class KeyResolver {
   private final List keyProviders;
   protected static Accessor BY_ALG_AND_PURPOSE = new Accessor() {
      public KeyResult getKey(Object var1, String var2, KeyPurpose var3, KeyProvider var4) {
         return var4.getKey(var2, var3);
      }
   };
   protected static Accessor BY_TOKEN_REFERENCE = new Accessor() {
      public KeyResult getKey(Object var1, String var2, KeyPurpose var3, KeyProvider var4) {
         SecurityTokenReference var5 = (SecurityTokenReference)var1;
         KeyResult var6 = null;
         KeyIdentifier var7 = var5.getKeyIdentifier();
         if (var7 != null) {
            var6 = var4.getKeyByIdentifier(var7.getIdentifier(), var2, var3);
         } else {
            X509IssuerSerial var8 = var5.getX509IssuerSerial();
            if (var8 != null) {
               var6 = var4.getKeyByIssuerSerial(var8.getIssuerName(), var8.getIssuerSerialNumber(), var2, var3);
            }
         }

         if (var6 != null) {
            return var6;
         } else {
            String var9 = var5.getReference();
            return var4.getKeyByURI(var9, var2, var3);
         }
      }
   };
   protected static Accessor BY_X509_DATA = new Accessor() {
      public KeyResult getKey(Object var1, String var2, KeyPurpose var3, KeyProvider var4) {
         X509Data var5 = (X509Data)var1;
         Iterator var6 = var5.getSubjectNames();

         KeyResult var7;
         String var8;
         for(var7 = null; var6.hasNext() && var7 == null; var7 = var4.getKeyBySubjectName(var8, var2, var3)) {
            var8 = (String)var6.next();
         }

         X509IssuerSerial var9;
         if (var7 == null) {
            for(Iterator var10 = var5.getIssuerSerials(); var10.hasNext() && var7 == null; var7 = var4.getKeyByIssuerSerial(var9.getIssuerName(), var9.getIssuerSerialNumber(), var2, var3)) {
               var9 = (X509IssuerSerial)var10.next();
            }
         }

         return var7;
      }
   };
   protected static Accessor BY_KEY_NAME = new Accessor() {
      public KeyResult getKey(Object var1, String var2, KeyPurpose var3, KeyProvider var4) {
         String var5 = (String)var1;
         return var4.getKeyByName(var5, var2, var3);
      }
   };

   private KeyResolver(List var1) {
      this.keyProviders = var1;
   }

   public KeyResolver() {
      this((List)(new ArrayList()));
   }

   public KeyResolver(KeyProvider[] var1) {
      this((List)(new ArrayList()));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         KeyProvider var3 = var1[var2];
         this.keyProviders.add(var3);
      }

   }

   public KeyResolver copy() {
      ArrayList var1 = new ArrayList(this.keyProviders);
      return new KeyResolver(var1);
   }

   public void addKeyProvider(KeyProvider var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Provider cannot be null");
      } else {
         this.keyProviders.add(var1);
      }
   }

   public boolean removeKeyProvider(KeyProvider var1) {
      return this.keyProviders.remove(var1);
   }

   public KeyProvider[] getKeyProviders() {
      KeyProvider[] var1 = new KeyProvider[this.keyProviders.size()];
      this.keyProviders.toArray(var1);
      return var1;
   }

   public KeyResult resolveKey(KeyPurpose var1, String var2, KeyInfo var3) throws KeyResolverException {
      KeyResult var4 = null;
      KeyProvider[] var5 = this.getKeyProviders();
      if (var3 != null) {
         Iterator var6 = var3.getSecurityTokenReferences();
         var4 = this.getKey(var6, BY_TOKEN_REFERENCE, var2, var1, var5);
         if (var4 != null) {
            return var4;
         }

         var6 = var3.getKeyNames();
         var4 = this.getKey(var6, BY_KEY_NAME, var2, var1, var5);
         if (var4 != null) {
            return var4;
         }

         var6 = var3.getX509Data();
         var4 = this.getKey(var6, BY_X509_DATA, var2, var1, var5);
         if (var4 != null) {
            return var4;
         }

         try {
            var6 = var3.getCertificates();

            while(var6.hasNext()) {
               X509Certificate var7 = (X509Certificate)var6.next();
               PublicKey var8 = var7.getPublicKey();
               if (KeyPurpose.serves(KeyPurpose.getPurposes(var8), var1) && Utils.supports(Utils.getAlgorithms(var8), var2)) {
                  return new X509KeyResult(var8, var7);
               }
            }

            var6 = var3.getPublicKeys();

            while(var6.hasNext()) {
               PublicKey var10 = (PublicKey)var6.next();
               if (KeyPurpose.serves(KeyPurpose.getPurposes(var10), var1) && Utils.supports(Utils.getAlgorithms(var10), var2)) {
                  return new KeyResult(var10);
               }
            }
         } catch (KeyInfoValidationException var9) {
            throw new KeyResolverException("Unable to resolveKey key", var9, var3);
         }
      }

      var4 = this.getKey((Object)null, BY_ALG_AND_PURPOSE, var2, var1, var5);
      if (var4 != null) {
         return var4;
      } else {
         throw new KeyResolverException("Failed to resolve key from providers " + this.providersToString(), var3);
      }
   }

   private String providersToString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[");
      Iterator var2 = this.keyProviders.iterator();

      while(var2.hasNext()) {
         KeyProvider var3 = (KeyProvider)var2.next();
         var1.append(var3).append(" ");
      }

      var1.append("]");
      return var1.toString();
   }

   private KeyResult getKey(Iterator var1, Accessor var2, String var3, KeyPurpose var4, KeyProvider[] var5) {
      KeyResult var6;
      Object var7;
      for(var6 = null; var1.hasNext() && var6 == null; var6 = this.getKey(var7, var2, var3, var4, var5)) {
         var7 = var1.next();
      }

      return var6;
   }

   private KeyResult getKey(Object var1, Accessor var2, String var3, KeyPurpose var4, KeyProvider[] var5) {
      KeyResult var6 = null;

      for(int var7 = 0; var6 == null && var7 < var5.length; ++var7) {
         KeyProvider var8 = var5[var7];
         var6 = var2.getKey(var1, var3, var4, var8);
      }

      return var6;
   }

   protected interface Accessor {
      KeyResult getKey(Object var1, String var2, KeyPurpose var3, KeyProvider var4);
   }
}
