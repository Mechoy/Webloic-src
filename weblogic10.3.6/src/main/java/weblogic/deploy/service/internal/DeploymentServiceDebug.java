package weblogic.deploy.service.internal;

import weblogic.logging.LogOutputStream;

public final class DeploymentServiceDebug {
   public static final boolean DEBUG = false;
   private static final LogOutputStream LOG = new LogOutputStream("DeploymentService");

   public static final void log(String var0) {
      LOG.debug(var0);
   }

   public static final void error(String var0) {
      LOG.error(var0);
   }
}
