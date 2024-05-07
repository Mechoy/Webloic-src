package weblogic.security.acl;

import java.util.Vector;
import weblogic.t3.srvr.T3Srvr;

/** @deprecated */
public final class SSLUserInfo extends DefaultUserInfoImpl {
   private static final long serialVersionUID = 4109721614403507950L;
   private transient Vector sslCerts;

   public SSLUserInfo(DefaultUserInfoImpl var1) {
      super(var1.getName(), (Object)null);
      if (T3Srvr.getT3Srvr() == null) {
         throw new SecurityException("cannot find T3 server object");
      } else {
         if (var1.hasPassword()) {
            this.setCredential(var1.getPassword());
         }

         if (var1.hasCertificates()) {
            this.setCredential(var1.getCertificates());
         }

      }
   }

   public void setSSLCertificates(Vector var1) {
      this.sslCerts = var1;
   }

   public Vector getSSLCertificates() {
      return this.sslCerts;
   }

   public Vector getCertificates() {
      return this.sslCerts;
   }

   public boolean hasCertificates() {
      return this.sslCerts.size() > 0;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof SSLUserInfo)) {
         return false;
      } else {
         SSLUserInfo var2 = (SSLUserInfo)var1;
         if (this.hasCertificates() && !var2.hasCertificates()) {
            return false;
         } else if (!this.sslCerts.equals(var2.getSSLCertificates())) {
            return false;
         } else {
            boolean var10000;
            label49: {
               label34: {
                  String var3 = var2.getRealmName();
                  String var4 = var2.getPassword();
                  if (this.getRealmName() == null) {
                     if (var3 != null) {
                        break label34;
                     }
                  } else if (!this.getRealmName().equals(var3)) {
                     break label34;
                  }

                  if (this.getPassword() == null) {
                     if (var4 == null) {
                        break label49;
                     }
                  } else if (this.getPassword().equals(var4)) {
                     break label49;
                  }
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      }
   }
}
