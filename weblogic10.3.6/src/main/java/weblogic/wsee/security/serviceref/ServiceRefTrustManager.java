package weblogic.wsee.security.serviceref;

import java.io.Serializable;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import weblogic.security.SSL.TrustManager;
import weblogic.xml.crypto.utils.CertUtils;

public class ServiceRefTrustManager implements TrustManager, Serializable {
   static final long serialVersionUID = 4818528773669203889L;
   private static ServiceRefTrustManager tm = new ServiceRefTrustManager();

   private ServiceRefTrustManager() {
   }

   public static TrustManager getInstance() {
      return tm;
   }

   public boolean certificateCallback(X509Certificate[] var1, int var2) {
      try {
         return CertUtils.validateCertPath(this.getCertPath(var1));
      } catch (CertificateException var4) {
         return false;
      }
   }

   private CertPath getCertPath(X509Certificate[] var1) throws CertificateException {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         X509Certificate var4 = var1[var3];
         var2.add(var4);
      }

      return CertificateFactory.getInstance("X509").generateCertPath(var2);
   }
}
