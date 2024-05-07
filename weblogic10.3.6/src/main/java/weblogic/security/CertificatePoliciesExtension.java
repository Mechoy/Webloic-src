package weblogic.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Set;

public class CertificatePoliciesExtension {
   private static final String EXTN_ID = "2.5.29.32";
   private static final String className = "com.bea.sslplus.extensions.CertificatePoliciesImpl";
   private CertificatePolicy[] policies;
   private boolean critical;

   public CertificatePoliciesExtension(boolean var1, CertificatePolicy[] var2) {
      this.critical = var1;
      this.policies = var2;
   }

   public static CertificatePoliciesExtension getCertificatePoliciesExtension(X509Certificate var0) throws CertificateParsingException {
      Object var1 = null;
      boolean var2 = false;
      Set var3 = var0.getCriticalExtensionOIDs();
      if (var3 != null && !var3.isEmpty()) {
         var2 = var3.contains("2.5.29.32");
      }

      try {
         Class var4 = Class.forName("com.bea.sslplus.extensions.CertificatePoliciesImpl");
         Method var9 = var4.getMethod("getPolicies", X509Certificate.class);
         CertificatePolicy[] var6 = (CertificatePolicy[])((CertificatePolicy[])var9.invoke((Object)null, var0));
         return new CertificatePoliciesExtension(var2, var6);
      } catch (InvocationTargetException var7) {
         CertificateParsingException var5 = new CertificateParsingException("Cannot Parse the Certificate Policies Extension");
         var5.initCause(var7.getCause());
         throw var5;
      } catch (Exception var8) {
         throw new RuntimeException("Unexpected exception", var8);
      }
   }

   public boolean isCritical() {
      return this.critical;
   }

   public CertificatePolicy[] getPolicies() {
      CertificatePolicy[] var1 = new CertificatePolicy[this.policies.length];
      System.arraycopy(this.policies, 0, var1, 0, this.policies.length);
      return var1;
   }
}
