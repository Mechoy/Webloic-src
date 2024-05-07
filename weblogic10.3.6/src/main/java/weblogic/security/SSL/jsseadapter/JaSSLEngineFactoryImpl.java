package weblogic.security.SSL.jsseadapter;

import javax.net.ssl.SSLException;
import weblogic.security.SSL.SSLEngineFactory;
import weblogic.security.SSL.WeblogicSSLEngine;
import weblogic.security.utils.SSLContextWrapper;

final class JaSSLEngineFactoryImpl implements SSLEngineFactory {
   private final SSLContextWrapper context;
   private final JaSSLContext jaContext;

   JaSSLEngineFactoryImpl(SSLContextWrapper var1) {
      if (!JaSSLEngineFactoryBuilder.isJSSEEnabled()) {
         throw new UnsupportedOperationException("SSLEngine is only available when JSSE is enabled.");
      } else if (null == var1) {
         throw new IllegalArgumentException("Non-null SSLContextWrapper expected.");
      } else {
         this.context = var1;
         this.jaContext = null;
      }
   }

   JaSSLEngineFactoryImpl(JaSSLContext var1) {
      if (!JaSSLEngineFactoryBuilder.isJSSEEnabled()) {
         throw new UnsupportedOperationException("SSLEngine is only available when JSSE is enabled.");
      } else if (null == var1) {
         throw new IllegalArgumentException("Non-null JaSSLContext expected.");
      } else {
         this.context = null;
         this.jaContext = var1;
      }
   }

   public String[] getDefaultCipherSuites() {
      return null != this.jaContext ? this.jaContext.getDefaultCipherSuites() : this.context.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return null != this.jaContext ? this.jaContext.getSupportedCipherSuites() : this.context.getSupportedCipherSuites();
   }

   public WeblogicSSLEngine createSSLEngine() throws SSLException {
      return null != this.jaContext ? this.jaContext.createSSLEngine() : this.context.createSSLEngine();
   }

   public WeblogicSSLEngine createSSLEngine(String var1, int var2) throws SSLException {
      return null != this.jaContext ? this.jaContext.createSSLEngine(var1, var2) : this.context.createSSLEngine(var1, var2);
   }
}
