package weblogic.servlet.internal.session;

import java.util.HashMap;
import java.util.Iterator;
import weblogic.servlet.cluster.WANPersistenceManager;
import weblogic.servlet.cluster.wan.SessionDiff;

public class WANSessionData extends ReplicatedSessionData {
   private static final long serialVersionUID = -8758624332473471640L;
   private final SessionDiff diff;
   private static final String WAN_SESSION_VERSION = "wls_wan_session_version";

   public WANSessionData() {
      this.diff = new SessionDiff();
   }

   public WANSessionData(String var1, SessionContext var2) {
      this(var1, var2, true);
   }

   protected WANSessionData(String var1, SessionContext var2, boolean var3) {
      super(var1, var2, var3);
      this.diff = new SessionDiff();
   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      super.removeInternalAttribute(var1);
      this.diff.setAttribute(var1, (Object)null, false, true);
      super.setInternalAttribute("wls_wan_session_version", new Integer(this.diff.getVersionCount()));
   }

   public final void setInternalAttribute(String var1, Object var2) throws IllegalStateException {
      Object var3 = super.getInternalAttribute(var1);
      super.setInternalAttribute(var1, var2);
      this.diff.setAttribute(var1, var2, var3 == null, true);
      super.setInternalAttribute("wls_wan_session_version", new Integer(this.diff.getVersionCount()));
   }

   public final void removeAttribute(String var1) throws IllegalStateException {
      super.removeAttribute(var1);
      this.diff.setAttribute(var1, (Object)null, false, false);
      super.setInternalAttribute("wls_wan_session_version", new Integer(this.diff.getVersionCount()));
   }

   public final void setAttribute(String var1, Object var2) throws IllegalStateException {
      Object var3 = super.getAttribute(var1);
      super.setAttribute(var1, var2);
      this.diff.setAttribute(var1, var2, var3 == null, false);
      super.setInternalAttribute("wls_wan_session_version", new Integer(this.diff.getVersionCount()));
   }

   private String getIdWithoutServerInfo() {
      return RSID.getID(this.getIdWithServerInfo());
   }

   public void invalidate() {
      super.invalidate();
      WANPersistenceManager.getInstance().invalidate(this.getIdWithoutServerInfo(), this.getContextPath());
   }

   void remove(boolean var1) {
      super.remove(var1);
      WANPersistenceManager.getInstance().invalidate(this.getIdWithoutServerInfo(), this.getContextPath());
   }

   public SessionDiff getChange() {
      return this.diff;
   }

   public void setCreationTime(long var1) {
      this.creationTime = var1;
   }

   void syncSession() {
      super.syncSession();
      WANPersistenceManager.getInstance().update(this.getIdWithoutServerInfo(), this.getCreationTime(), this.getContextPath(), this.getMaxInactiveInterval(), this.getLAT(), this.diff);
   }

   SessionDiff getSessionDiff() {
      return this.diff;
   }

   public void setSessionCreationTime(long var1) {
      this.creationTime = var1;
   }

   public void applySessionDiff(SessionDiff var1) {
      HashMap var2 = var1.getAttributes();
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.setAttribute(var4, var2.get(var4));
      }

      HashMap var6 = var1.getInternalAttributes();
      var3 = var6.keySet().iterator();

      while(var3.hasNext()) {
         String var5 = (String)var3.next();
         this.setInternalAttribute(var5, var6.get(var5));
      }

      this.diff.setVersionCounter(var1.getVersionCount());
   }

   protected void applySessionChange(ReplicatedSessionChange var1) {
      HashMap var2 = var1.getInternalAttributeChanges();
      Integer var3 = (Integer)var2.remove("wls_wan_session_version");
      if (var3 != null) {
         this.diff.setVersionCounter(var3 + 1);
      }

      super.applySessionChange(var1);
   }
}
