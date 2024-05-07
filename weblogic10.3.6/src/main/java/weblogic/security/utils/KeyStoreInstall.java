package weblogic.security.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Properties;
import utils.CertGen;
import weblogic.management.bootstrap.BootStrap;

public final class KeyStoreInstall {
   public static void initDefaultKeyStore() throws Exception {
      initDefaultKeyStore((String)null);
   }

   public static void initDefaultKeyStore(String var0) throws Exception {
      if (var0 == null) {
         var0 = InetAddress.getLocalHost().getHostName();
      }

      Properties var1 = new Properties();
      var1.put("x500name.commonname", var0);
      initDefaultKeyStore("DemoIdentity.jks", "DemoIdentityKeyStorePassPhrase", "DemoIdentity", "DemoIdentityPassPhrase", var1);
   }

   public static void initDefaultKeyStore(String var0, String var1, String var2, String var3, Properties var4) throws Exception {
      CertGen var5 = new CertGen(true);
      var5.generateCertificate(var4);
      Certificate[] var6 = new Certificate[]{var5.getSubjectCertificate()};
      PrivateKey var7 = var5.getSubjectPrivateKey();
      KeyStore var8 = KeyStore.getInstance("jks");
      var8.load((InputStream)null, var1.toCharArray());
      var8.setKeyEntry(var2, var7, var3.toCharArray(), var6);
      File var9 = new File(new File(BootStrap.getWebLogicHome(), "lib"), var0);
      FileOutputStream var10 = new FileOutputStream(var9);
      var8.store(var10, var1.toCharArray());
      var10.close();
   }

   public static void main(String[] var0) throws Exception {
      String var1 = var0.length > 0 ? var0[0] : null;
      initDefaultKeyStore(var1);
   }
}
