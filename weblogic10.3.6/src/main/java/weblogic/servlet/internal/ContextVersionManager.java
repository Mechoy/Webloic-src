package weblogic.servlet.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class ContextVersionManager {
   private static final String DEFAULT_VERSION_ID = "weblogic.default_version";
   private final String appName;
   private String contextPath;
   private WebAppServletContext activeServletContext;
   private WebAppServletContext activeAdminModeServletContext;
   private Map versionIdWASCMap;
   private final Map sessionIdWASCMap;
   private boolean isOld = false;
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");

   ContextVersionManager(ContextVersionManager var1, String var2, String var3) {
      this.contextPath = var3;
      if (var1 == null) {
         this.versionIdWASCMap = new HashMap();
         this.sessionIdWASCMap = new Hashtable();
         this.appName = var2;
      } else {
         this.versionIdWASCMap = new HashMap(var1.versionIdWASCMap);
         this.sessionIdWASCMap = var1.sessionIdWASCMap;
         this.activeServletContext = var1.activeServletContext;
         this.activeAdminModeServletContext = var1.activeAdminModeServletContext;
         this.appName = var1.appName;
         var1.isOld = true;
      }

   }

   public String toString() {
      return "CVM[appName=" + this.appName + ", contextPath=" + this.contextPath + ", activeServletContext={" + this.activeServletContext + "}, activeAdminServletContext={" + this.activeAdminModeServletContext + "}, versionIdWASCMap.size=" + this.versionIdWASCMap.size() + ", sessionIdWASCMap.size=" + this.sessionIdWASCMap.size() + "]";
   }

   public boolean isVersioned() {
      if (this.activeServletContext != null) {
         return this.activeServletContext.getVersionId() != null;
      } else if (this.activeAdminModeServletContext != null) {
         return this.activeAdminModeServletContext.getVersionId() != null;
      } else if (!this.versionIdWASCMap.isEmpty()) {
         WebAppServletContext var1 = (WebAppServletContext)this.versionIdWASCMap.values().iterator().next();
         return var1.getVersionId() != null;
      } else {
         return false;
      }
   }

   String getAppName() {
      return this.appName;
   }

   String getContextPath() {
      return this.contextPath;
   }

   void setDefaultContext() {
      this.contextPath = "";
   }

   public void putContext(String var1, WebAppServletContext var2) {
      if (var1 == null) {
         this.versionIdWASCMap.put("weblogic.default_version", var2);
      } else {
         this.versionIdWASCMap.put(var1, var2);
      }

      this.delaySetActive(var2);
   }

   public void removeContext(String var1) {
      WebAppServletContext var2 = null;
      HashMap var3 = new HashMap(this.versionIdWASCMap);
      if (var1 == null) {
         var2 = (WebAppServletContext)var3.remove("weblogic.default_version");
      } else {
         var2 = (WebAppServletContext)var3.remove(var1);
      }

      if (DEBUG_APP_VERSION.isEnabled()) {
         HTTPLogger.logDebug("CVM.removeContext: version=" + var1 + ", CVM=" + this);
      }

      this.versionIdWASCMap = var3;
      this.unsetActive(var2);
   }

   public WebAppServletContext getContext(String var1) {
      if (var1 == null) {
         return this.activeServletContext != null ? this.activeServletContext : (WebAppServletContext)this.versionIdWASCMap.get("weblogic.default_version");
      } else {
         return (WebAppServletContext)this.versionIdWASCMap.get(var1);
      }
   }

   public WebAppServletContext getActiveContext(boolean var1) {
      return var1 ? this.activeAdminModeServletContext : this.activeServletContext;
   }

   public WebAppServletContext getCurrentOrActiveContext(boolean var1) {
      return this.getContext(ApplicationVersionUtils.getCurrentVersionId(this.appName));
   }

   public WebAppServletContext getContext() {
      Iterator var1 = this.getServletContexts().iterator();
      return var1.hasNext() ? (WebAppServletContext)var1.next() : null;
   }

   public WebAppServletContext getContext(boolean var1) {
      Iterator var2 = this.getServletContexts(var1);
      if (var2.hasNext()) {
         return (WebAppServletContext)var2.next();
      } else {
         WebAppServletContext var3 = this.getContext();
         return var3 == null || !var3.isInternalApp() && !var3.getConfigManager().isRequireAdminTraffic() ? null : var3;
      }
   }

   public Collection getServletContexts() {
      return this.versionIdWASCMap.values();
   }

   public Iterator getServletContexts(boolean var1) {
      return new AdminModeAwareIterator(var1);
   }

   public void putContextForSession(String var1, WebAppServletContext var2) {
      this.sessionIdWASCMap.put(var1, var2);
      if (DEBUG_APP_VERSION.isEnabled()) {
         HTTPLogger.logDebug("CVM.putServletContextForSession(sessionID=" + var1 + ", WASC=" + var2 + ")");
      }

   }

   public void removeContextForSession(String var1) {
      this.sessionIdWASCMap.remove(var1);
      if (DEBUG_APP_VERSION.isEnabled()) {
         HTTPLogger.logDebug("CVM.removeServletContextForSession(sessionID=" + var1 + ")");
      }

   }

   public WebAppServletContext getContextForSession(String var1) {
      return (WebAppServletContext)this.sessionIdWASCMap.get(var1);
   }

   public WorkManager getWorkManager(boolean var1) {
      WebAppServletContext var2 = this.getActiveContext(var1);
      return var2 != null ? var2.getConfigManager().getWorkManager() : WorkManagerFactory.getInstance().getDefault();
   }

   private void setActive(String var1, boolean var2) {
      this.setActive(this.getContext(var1), var2);
   }

   private void setActive(WebAppServletContext var1, boolean var2) {
      if (var1 != null) {
         if (var2) {
            this.activeAdminModeServletContext = var1;
         } else {
            this.activeServletContext = var1;
         }

         var1.setAdminMode(var2);
         if (DEBUG_APP_VERSION.isEnabled()) {
            HTTPLogger.logDebug("CVM.setActive: version=" + (var1 == null ? null : var1.getVersionId()) + ", admin=" + var2 + ", CVM=" + this);
         }

      }
   }

   private void delaySetActive(WebAppServletContext var1) {
      ApplicationRuntimeMBean var2 = ApplicationVersionUtils.getCurrentApplicationRuntime();
      if (var2 == null) {
         this.setActive(var1, false);
      } else {
         var2.addPropertyChangeListener(new StateChangeListener(var1));
         if (DEBUG_APP_VERSION.isEnabled()) {
            HTTPLogger.logDebug("CVM.delaySetActive: version=" + var1.getVersionId() + ", CVM=" + this);
         }
      }

   }

   private void unsetActive(WebAppServletContext var1) {
      if (var1 != null) {
         if (var1 == this.activeServletContext) {
            this.activeServletContext = null;
         } else if (var1 == this.activeAdminModeServletContext) {
            this.activeAdminModeServletContext = null;
            var1.setAdminMode(false);
         }

         if (DEBUG_APP_VERSION.isEnabled()) {
            HTTPLogger.logDebug("CVM.unsetActive: app=" + this.appName + ", version=" + (var1 == null ? null : var1.getVersionId()) + ", CVM=" + this);
         }

      }
   }

   private void adminTransition(WebAppServletContext var1, boolean var2, boolean var3) {
      if (var2 && !var3) {
         if (var1 == this.activeAdminModeServletContext) {
            this.activeAdminModeServletContext = null;
            this.activeServletContext = var1;
         }
      } else if (!var2 && var3 && var1 == this.activeServletContext) {
         this.activeServletContext = null;
         this.activeAdminModeServletContext = var1;
      }

      var1.setAdminMode(var3);
      if (DEBUG_APP_VERSION.isEnabled()) {
         HTTPLogger.logDebug("CVM.adminTransition: app=" + this.appName + ", version=" + var1.getVersionId() + ", admin=" + var3);
      }

   }

   boolean isOld() {
      return this.isOld;
   }

   private final class AdminModeAwareIterator implements Iterator {
      Iterator iter;
      boolean isAdminMode;
      WebAppServletContext nextContext;

      private AdminModeAwareIterator(boolean var2) {
         this.isAdminMode = var2;
         this.iter = ContextVersionManager.this.versionIdWASCMap.values().iterator();
      }

      public void remove() {
         this.iter.remove();
      }

      public boolean hasNext() {
         while(true) {
            if (this.iter.hasNext()) {
               this.nextContext = (WebAppServletContext)this.iter.next();
               if (this.nextContext.isAdminMode() != this.isAdminMode) {
                  continue;
               }

               return true;
            }

            return false;
         }
      }

      public Object next() {
         return this.nextContext;
      }

      // $FF: synthetic method
      AdminModeAwareIterator(boolean var2, Object var3) {
         this(var2);
      }
   }

   private final class StateChangeListener implements PropertyChangeListener {
      private WebAppServletContext ctx;

      private StateChangeListener(WebAppServletContext var2) {
         this.ctx = var2;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equalsIgnoreCase("ActiveVersionState")) {
            Object var2 = var1.getOldValue();
            Object var3 = var1.getNewValue();
            if (var2 instanceof Integer && var3 instanceof Integer) {
               int var4 = (Integer)var2;
               int var5 = (Integer)var3;
               if (ContextVersionManager.DEBUG_APP_VERSION.isEnabled()) {
                  HTTPLogger.logDebug("+++ StateChange oldState=" + var4 + ", newState=" + var5);
               }

               ApplicationRuntimeMBean var6 = (ApplicationRuntimeMBean)var1.getSource();
               String var7 = var6.getApplicationVersion();
               if (var5 == 2 && var6.getApplicationName().equals(ContextVersionManager.this.appName) && var7 != null && !var7.equals(this.ctx.getVersionId())) {
                  ContextVersionManager.this.setActive(var6.getApplicationVersion(), var5 == 1);

                  try {
                     ((RuntimeMBeanDelegate)var6).removePropertyChangeListener(this);
                  } catch (Throwable var9) {
                     if (ContextVersionManager.DEBUG_APP_VERSION.isEnabled()) {
                        HTTPLogger.logDebug(StackTraceUtils.throwable2StackTrace(var9));
                     }
                  }

                  return;
               }

               if (var4 == var5) {
                  return;
               }

               if (this.isActiveState(var5)) {
                  if (var4 == 0) {
                     ContextVersionManager.this.setActive(this.ctx, var5 == 1);
                  } else if (this.isActiveState(var4)) {
                     ContextVersionManager.this.adminTransition(this.ctx, var4 == 1, var5 == 1);
                  }
               } else if (this.isActiveState(var4)) {
                  ContextVersionManager.this.unsetActive(this.ctx);

                  try {
                     ((RuntimeMBeanDelegate)var6).removePropertyChangeListener(this);
                  } catch (Throwable var10) {
                     if (ContextVersionManager.DEBUG_APP_VERSION.isEnabled()) {
                        HTTPLogger.logDebug(StackTraceUtils.throwable2StackTrace(var10));
                     }
                  }
               }
            }
         }

      }

      private boolean isActiveState(int var1) {
         return var1 == 2 || var1 == 1;
      }

      // $FF: synthetic method
      StateChangeListener(WebAppServletContext var2, Object var3) {
         this(var2);
      }
   }
}
