package weblogic.security.SSL.jsseadapter;

import weblogic.security.SSL.SSLEngineFactory;
import weblogic.security.utils.SSLContextWrapper;
import weblogic.security.utils.SSLSetup;

public final class JaSSLEngineFactoryBuilder {
   public static final boolean isJSSEEnabled() {
      return SSLSetup.isJSSEEnabled();
   }

   public static final SSLEngineFactory getFactoryInstance(SSLContextWrapper var0) {
      if (!isJSSEEnabled()) {
         throw new UnsupportedOperationException("SSLEngine is only available when JSSE is enabled.");
      } else {
         return new JaSSLEngineFactoryImpl(var0);
      }
   }
}
