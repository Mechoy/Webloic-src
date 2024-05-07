package weblogic.security.pki.revocation.common;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

final class DownloadCrlFromDpRunnable implements Runnable {
   private volatile boolean running = true;
   private volatile boolean crlCacheUpdated = false;
   private final CrlCacheAccessor crlCacheAccessor;
   private final Object waitingObject;
   private final X509Certificate certWithDp;
   private final LogListener logger;
   private final URI alternateUri;
   private final AbstractCertRevocContext.AttributeUsage alternateUriUsage;
   private final long dpDownloadTimeout;

   DownloadCrlFromDpRunnable(CrlCacheAccessor var1, Object var2, X509Certificate var3, LogListener var4, URI var5, AbstractCertRevocContext.AttributeUsage var6, long var7) {
      Util.checkNotNull("crlCacheAccessor", var1);
      Util.checkNotNull("certWithDp", var3);
      Util.checkNotNull("waitingObject", var2);
      this.crlCacheAccessor = var1;
      this.waitingObject = var2;
      this.certWithDp = var3;
      this.logger = var4;
      this.alternateUri = var5;
      this.alternateUriUsage = var6;
      this.dpDownloadTimeout = var7;
   }

   public boolean isRunning() {
      return this.running;
   }

   public boolean isCrlCacheUpdated() {
      return this.crlCacheUpdated;
   }

   public void run() {
      synchronized(this.waitingObject) {
         X500Principal var2 = this.certWithDp.getSubjectX500Principal();
         String var3 = null == var2 ? "" : var2.getName();

         try {
            long var4 = this.dpDownloadTimeout;
            long var6 = this.dpDownloadTimeout;
            CrlDpFetcher var8 = CrlDpFetcher.getInstance();
            this.crlCacheUpdated = var8.updateCrls(this.certWithDp, this.crlCacheAccessor, this.alternateUri, this.alternateUriUsage, var4, var6, this.logger);
         } catch (Exception var14) {
            if (null != this.logger && this.logger.isLoggable(Level.FINE)) {
               this.logger.log(Level.FINE, var14, "Trying to download CRLs from Distribution Point for cert subject \"{0}\".", var3);
            }
         } finally {
            this.running = false;
            if (null != this.waitingObject) {
               this.waitingObject.notifyAll();
            }

         }

      }
   }
}
