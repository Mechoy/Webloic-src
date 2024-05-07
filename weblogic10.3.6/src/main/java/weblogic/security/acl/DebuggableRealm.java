package weblogic.security.acl;

import weblogic.logging.LogOutputStream;

/** @deprecated */
public interface DebuggableRealm {
   void setDebug(boolean var1);

   LogOutputStream getDebugLog();
}
