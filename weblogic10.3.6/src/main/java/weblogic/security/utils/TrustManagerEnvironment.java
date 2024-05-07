package weblogic.security.utils;

import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSocket;

public final class TrustManagerEnvironment {
   private static ThreadLocal threadInstance = new ThreadLocal();
   private TrustManagerEnvironment parentEnv;
   private X509Certificate[] trustedCAs;
   private SSLSocket sslSocket;

   public static void push(X509Certificate[] var0, SSLSocket var1) {
      TrustManagerEnvironment var2 = new TrustManagerEnvironment(getInstance(), var0, var1);
      threadInstance.set(var2);
   }

   public static void pop() {
      TrustManagerEnvironment var0 = getInstance();
      if (var0 != null) {
         threadInstance.set(var0._getParentEnv());
      }
   }

   public static X509Certificate[] getTrustedCAs() {
      TrustManagerEnvironment var0 = getInstance();
      return var0 == null ? null : var0._getTrustedCAs();
   }

   public static SSLSocket getSSLSocket() {
      TrustManagerEnvironment var0 = getInstance();
      return var0 == null ? null : var0._getSSLSocket();
   }

   private static TrustManagerEnvironment getInstance() {
      return (TrustManagerEnvironment)threadInstance.get();
   }

   private TrustManagerEnvironment(TrustManagerEnvironment var1, X509Certificate[] var2, SSLSocket var3) {
      this.parentEnv = var1;
      this.trustedCAs = var2;
      this.sslSocket = var3;
   }

   private TrustManagerEnvironment _getParentEnv() {
      return this.parentEnv;
   }

   private X509Certificate[] _getTrustedCAs() {
      return this.trustedCAs;
   }

   private SSLSocket _getSSLSocket() {
      return this.sslSocket;
   }
}
