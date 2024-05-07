package weblogic.jndi.internal;

import weblogic.jndi.WLContext;

public interface WLInternalContext extends WLContext {
   void enableLogoutOnClose();

   void disableThreadWarningOnClose();
}
