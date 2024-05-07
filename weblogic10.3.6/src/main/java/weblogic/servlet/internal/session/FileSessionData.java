package weblogic.servlet.internal.session;

import java.io.Externalizable;

public final class FileSessionData extends SessionData implements Externalizable {
   static final long serialVersionUID = -1312849241262491947L;

   public FileSessionData() {
   }

   public FileSessionData(String var1, SessionContext var2, boolean var3) {
      super(var1, var2, var3);
      if (var3) {
         this.getWebAppServletContext().getEventsManager().notifySessionLifetimeEvent(this, true);
      }

   }

   protected void logTransientAttributeError(String var1) {
      HTTPSessionLogger.logTransientFileAttributeError(this.getWebAppServletContext().getLogContext(), var1, this.getId());
   }
}
