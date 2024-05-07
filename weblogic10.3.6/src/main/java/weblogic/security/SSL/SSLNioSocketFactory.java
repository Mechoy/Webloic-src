package weblogic.security.SSL;

import java.net.SocketException;
import javax.net.SocketFactory;
import weblogic.security.utils.SSLSetup;

public class SSLNioSocketFactory extends SSLSocketFactory {
   public SSLNioSocketFactory() {
   }

   private SSLNioSocketFactory(SSLClientInfo var1) {
      this.setSSLClientInfo(var1);
   }

   protected SSLNioSocketFactory(javax.net.ssl.SSLSocketFactory var1) {
      super(var1);
   }

   public static SocketFactory getDefault() {
      if (defFactory == null) {
         Class var0 = SSLNioSocketFactory.class;
         synchronized(SSLNioSocketFactory.class) {
            if (defFactory == null) {
               defFactory = new SSLNioSocketFactory();
            }
         }
      }

      return defFactory;
   }

   public static SSLSocketFactory getInstance(SSLClientInfo var0) {
      return new SSLNioSocketFactory(var0);
   }

   /** @deprecated */
   public static SSLSocketFactory getJSSE(SSLClientInfo var0) {
      return new SSLNioSocketFactory(var0);
   }

   public void setSSLClientInfo(SSLClientInfo var1) {
      try {
         if (var1 != null && !var1.isNioSet()) {
            var1.setNio(true);
         }

         this.jsseFactory = var1 == null ? SSLSetup.getSSLContext(var1).getSSLNioSocketFactory() : var1.getSSLSocketFactory();
      } catch (SocketException var3) {
         SSLSetup.debug(3, var3, "Failed to create context");
         throw new RuntimeException("Failed to update factory: " + var3.getMessage());
      }
   }
}
