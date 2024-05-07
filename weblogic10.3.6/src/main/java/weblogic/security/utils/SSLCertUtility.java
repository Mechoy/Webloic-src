package weblogic.security.utils;

import com.rsa.certj.cert.AttributeValueAssertion;
import com.rsa.certj.cert.NameException;
import com.rsa.certj.cert.X500Name;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.security.cert.CertificateEncodingException;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import weblogic.security.MessageDigest;
import weblogic.security.WLMessageDigest;

public final class SSLCertUtility {
   public static X509Certificate toJavaX509(Certificate var0) {
      try {
         return X509Certificate.getInstance(var0.getEncoded());
      } catch (CertificateEncodingException var2) {
      } catch (CertificateException var3) {
      } catch (java.security.cert.CertificateEncodingException var4) {
      }

      return null;
   }

   public static X509Certificate[] toJavaX509(Certificate[] var0) {
      if (var0 == null) {
         return null;
      } else {
         X509Certificate[] var1 = new X509Certificate[var0.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = toJavaX509(var0[var2]);
            if (var1[var2] == null) {
               return null;
            }
         }

         return var1;
      }
   }

   public static java.security.cert.X509Certificate[] toJavaX5092(Certificate[] var0) throws IOException {
      if (var0 != null) {
         if (var0 instanceof java.security.cert.X509Certificate[]) {
            return (java.security.cert.X509Certificate[])((java.security.cert.X509Certificate[])var0);
         }

         try {
            java.security.cert.X509Certificate[] var1 = new java.security.cert.X509Certificate[var0.length];
            CertificateFactory var2 = CertificateFactory.getInstance("X.509");

            for(int var3 = 0; var3 < var0.length; ++var3) {
               var1[var3] = (java.security.cert.X509Certificate)(var0[var3] instanceof java.security.cert.X509Certificate ? var0[var3] : var2.generateCertificate(new ByteArrayInputStream(var0[var3].getEncoded())));
            }

            return var1;
         } catch (java.security.cert.CertificateException var4) {
            SSLSetup.info(var4, "Exception processing certificates: " + var4.getMessage());
         }
      }

      return null;
   }

   public static java.security.cert.X509Certificate toX509(Certificate var0) throws java.security.cert.CertificateException {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof java.security.cert.X509Certificate) {
         return (java.security.cert.X509Certificate)var0;
      } else {
         CertificateFactory var1 = CertificateFactory.getInstance("X.509");
         return (java.security.cert.X509Certificate)var1.generateCertificate(new ByteArrayInputStream(var0.getEncoded()));
      }
   }

   public static java.security.cert.X509Certificate toJavaX509(X509Certificate var0) throws java.security.cert.CertificateException, IOException, CertificateEncodingException {
      CertificateFactory var1 = CertificateFactory.getInstance("X.509");
      return (java.security.cert.X509Certificate)var1.generateCertificate(new ByteArrayInputStream(var0.getEncoded()));
   }

   public static java.security.cert.X509Certificate[] toJavaX509(X509Certificate[] var0) throws java.security.cert.CertificateException, IOException, CertificateEncodingException {
      java.security.cert.X509Certificate[] var1 = new java.security.cert.X509Certificate[var0.length];
      CertificateFactory var2 = CertificateFactory.getInstance("X.509");

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var1[var3] = (java.security.cert.X509Certificate)var2.generateCertificate(new ByteArrayInputStream(var0[var3].getEncoded()));
      }

      return var1;
   }

   public static java.security.cert.X509Certificate getPeerLeafCert(SSLSocket var0) {
      return getPeerLeafCert(var0.getSession());
   }

   public static java.security.cert.X509Certificate getPeerLeafCert(SSLSession var0) {
      try {
         Certificate[] var1 = var0.getPeerCertificates();
         return var1 != null && var1.length != 0 ? toX509(var1[0]) : null;
      } catch (java.security.cert.CertificateException var2) {
      } catch (SSLPeerUnverifiedException var3) {
      }

      return null;
   }

   public static java.security.cert.X509Certificate[] getPeerCertChain(SSLSocket var0) {
      try {
         return toJavaX5092(var0.getSession().getPeerCertificates());
      } catch (SSLPeerUnverifiedException var2) {
      } catch (IOException var3) {
      }

      return null;
   }

   public static com.rsa.certj.cert.X509Certificate toCertJ(java.security.cert.X509Certificate var0) throws java.security.cert.CertificateEncodingException, com.rsa.certj.cert.CertificateException {
      return new com.rsa.certj.cert.X509Certificate(var0.getEncoded(), 0, 0);
   }

   public static X500Name getSubjectX500Name(java.security.cert.X509Certificate var0) throws java.security.cert.CertificateEncodingException, com.rsa.certj.cert.CertificateException {
      return toCertJ(var0).getSubjectName();
   }

   public static String getSubjectDNValue(X500Name var0, int var1) throws java.security.cert.CertificateEncodingException, com.rsa.certj.cert.CertificateException, NameException {
      AttributeValueAssertion var2 = var0.getAttribute(var1);
      return var2 == null ? null : var2.getStringAttribute();
   }

   public static String getSubjectDNValue(java.security.cert.X509Certificate var0, int var1) throws java.security.cert.CertificateEncodingException, com.rsa.certj.cert.CertificateException, NameException {
      return getSubjectDNValue(getSubjectX500Name(var0), var1);
   }

   public static String getSubjectDNCommonName(java.security.cert.X509Certificate var0) throws java.security.cert.CertificateEncodingException, com.rsa.certj.cert.CertificateException, NameException {
      return getSubjectDNValue((java.security.cert.X509Certificate)var0, 0);
   }

   public static java.security.cert.X509Certificate[] inputCertificateChain(SSLContextWrapper var0, InputStream var1) throws IOException, KeyManagementException {
      InputStreamCloner var2 = new InputStreamCloner(var1);

      try {
         return var0.inputCertChain(var2.cloneStream());
      } catch (IOException var9) {
         throw new KeyManagementException(var9.getMessage());
      } catch (KeyManagementException var10) {
         try {
            CertificateFactory var4 = CertificateFactory.getInstance("X.509");
            java.security.cert.X509Certificate[] var5 = new java.security.cert.X509Certificate[]{(java.security.cert.X509Certificate)var4.generateCertificate(var2.cloneStream())};
            return var5;
         } catch (java.security.cert.CertificateEncodingException var6) {
         } catch (java.security.cert.CertificateException var7) {
         } catch (IOException var8) {
         }

         throw var10;
      }
   }

   public static Collection getX509Certificates(KeyStore var0) throws KeyStoreException {
      ArrayList var1 = new ArrayList();
      Enumeration var2 = var0.aliases();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         if (var0.isCertificateEntry(var3)) {
            Certificate var4 = var0.getCertificate(var3);
            if (var4 instanceof java.security.cert.X509Certificate) {
               var1.add(var4);
            }
         }
      }

      return var1;
   }

   public static byte[] getFingerprint(Certificate var0) throws java.security.cert.CertificateEncodingException {
      MessageDigest var1 = WLMessageDigest.getInstance("MD5");
      var1.update(var0.getEncoded());
      return var1.digest();
   }

   public static String getCommonName(java.security.cert.X509Certificate var0) {
      String var1 = null;
      if (var0 != null) {
         String var2 = var0.getSubjectX500Principal().getName();
         int var3 = var2.indexOf("CN=");
         if (var3 >= 0) {
            boolean var4 = false;
            int var5 = var3 + 3;

            int var6;
            for(var6 = var2.indexOf(44, var5); var6 > 0 && var2.charAt(var6 - 1) == '\\'; var6 = var2.indexOf(",", var6 + 1)) {
               var4 = true;
            }

            if (var6 < 0) {
               var6 = var2.length();
            }

            var1 = var2.substring(var5, var6);
            if (var4) {
               int var7 = var1.length();
               StringBuffer var8 = new StringBuffer(var7);

               for(int var9 = 0; var9 < var7; ++var9) {
                  char var10 = var1.charAt(var9);
                  if (var10 == '\\') {
                     ++var9;
                     if (var9 == var7) {
                        break;
                     }

                     var10 = var1.charAt(var9);
                  }

                  var8.append(var10);
               }

               var1 = var8.toString();
            }
         }
      }

      return var1;
   }

   public static String getCommonName(SSLSession var0) {
      return getCommonName(getPeerLeafCert(var0));
   }

   public static Collection getDNSSubjAltNames(SSLSession var0) {
      java.security.cert.X509Certificate var1 = getPeerLeafCert(var0);
      Collection var2 = null;

      try {
         var2 = var1.getSubjectAlternativeNames();
      } catch (CertificateParsingException var10) {
         return var2;
      }

      if (var2 == null) {
         return var2;
      } else {
         Vector var3 = new Vector();
         Iterator var4 = var2.iterator();
         List var5 = null;
         ListIterator var6 = null;
         Object var7 = null;
         Integer var8 = null;
         String var9 = null;

         while(var4.hasNext()) {
            var5 = (List)var4.next();
            var6 = var5.listIterator();

            while(var6.hasNext()) {
               var7 = var6.next();
               if (var7 instanceof Integer) {
                  var8 = (Integer)var7;
                  if (var8 == 2 && var6.hasNext()) {
                     var9 = (String)var6.next();
                     var3.add(var9);
                  }
               }
            }
         }

         return var3;
      }
   }
}
