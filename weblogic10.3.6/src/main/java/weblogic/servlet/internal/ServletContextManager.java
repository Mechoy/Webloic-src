package weblogic.servlet.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Hashtable;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.session.SessionContext;
import weblogic.servlet.utils.ServletMapping;
import weblogic.utils.http.HttpParsing;

public final class ServletContextManager implements PropertyChangeListener {
   private final Hashtable preservedState = new Hashtable();
   private final WebServerMBean svrMBean;
   private ServletMapping contextTable;

   ServletContextManager(WebServerMBean var1) {
      this.svrMBean = var1;
      this.contextTable = new ServletMapping();
      if (!HttpServer.isProductionModeEnabled()) {
         this.svrMBean.addPropertyChangeListener(this);
      }

   }

   public WebAppServletContext getContext(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = HttpParsing.ensureStartingSlash(var1);
         return this.getContextForURI(var1);
      }
   }

   ContextVersionManager resolveVersionManagerForURI(String var1) {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("ServletContextManager: resolving VersionManager for URI : " + var1);
      }

      ContextVersionManager var2 = this.lookupVersionManager(var1);
      if (HTTPDebugLogger.isEnabled()) {
         if (var2 == null) {
            HTTPDebugLogger.debug("Context not found for uri " + var1);
         } else {
            HTTPDebugLogger.debug("Found context: " + var2.toString() + " for: " + var1);
         }
      }

      return var2;
   }

   ContextVersionManager lookupVersionManager(String var1) {
      return (ContextVersionManager)this.contextTable.get(var1);
   }

   public WebAppServletContext getContextForContextPath(String var1) {
      ContextVersionManager var2 = this.lookupVersionManagerForContextPath(var1);
      return var2 == null ? null : var2.getContext((String)null);
   }

   public WebAppServletContext getContextForContextPath(String var1, String var2) {
      ContextVersionManager var3 = this.lookupVersionManagerForContextPath(var1);
      return var3 == null ? null : var3.getContext(var2);
   }

   private WebAppServletContext getContextForURI(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = HttpParsing.ensureStartingSlash(var1);
         ContextVersionManager var2 = this.lookupVersionManager(var1);
         return var2 == null ? null : var2.getContext(false);
      }
   }

   ContextVersionManager lookupVersionManagerForContextPath(String var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = var1.length();
         if (var2 == 1 && var1.charAt(0) == '/') {
            var1 = "";
         } else if (var2 > 0) {
            var1 = HttpParsing.ensureStartingSlash(var1);
            if (var1.endsWith("/")) {
               var1 = var1.substring(0, var2 - 1);
            }
         }

         ContextVersionManager var3 = this.lookupVersionManager(var1);
         if (var3 == null) {
            return null;
         } else {
            return !var3.getContextPath().equals(var1) ? null : var3;
         }
      }
   }

   public WebAppServletContext getDefaultContext() {
      ContextVersionManager var1 = (ContextVersionManager)this.contextTable.getDefault();
      return var1 == null ? null : var1.getActiveContext(false);
   }

   public WebAppServletContext[] getAllContexts() {
      ArrayList var1 = new ArrayList();
      Object[] var2 = this.contextTable.values();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.addAll(((ContextVersionManager)var2[var3]).getServletContexts());
      }

      WebAppServletContext[] var4 = new WebAppServletContext[var1.size()];
      return (WebAppServletContext[])((WebAppServletContext[])var1.toArray(var4));
   }

   public WebAppServletContext getServletContextFor(WebAppComponentMBean var1) {
      WebAppServletContext[] var2 = this.getAllContexts();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         WebAppServletContext var4 = var2[var3];
         if (var4 != null) {
            WebAppComponentMBean var5 = var4.getMBean();
            if (var5 != null && var5.getName().equals(var1.getName())) {
               return var4;
            }
         }
      }

      return null;
   }

   synchronized void registerContext(WebAppServletContext var1) throws DeploymentException {
      String var2 = var1.getContextPath();
      this.registerContext(var1, var2);
   }

   synchronized void registerContext(WebAppServletContext var1, String var2) throws DeploymentException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("Registering ServletContext with context-root: '" + var2 + "'");
      }

      this.reinstateSessionState(var1);
      synchronized(this.contextTable) {
         ContextVersionManager var4 = (ContextVersionManager)this.contextTable.get(var2);
         ContextVersionManager var5 = null;
         if (var4 != null && var4.getContextPath().equals(var2) && (var4 != this.contextTable.getDefault() || toPattern(var2).equals("/"))) {
            if (!var4.getAppName().equals(var1.getAppName())) {
               Loggable var12 = HTTPLogger.logContextAlreadyRegisteredLoggable(var1.toString(), var1.getDocroot(), this.svrMBean.getName(), var4.getContext().toString(), var4.getContext().getDocroot(), var2);
               var12.log();
               throw new DeploymentException(var12.getMessage());
            }

            WebAppServletContext var6 = var4.getContext(var1.getVersionId());
            if (var6 == null && var1.getVersionId() != null) {
               WebAppServletContext var7 = var4.getContext();
               if (var7 != null && var7.getVersionId() == null) {
                  Loggable var8 = HTTPLogger.logNonVersionedContextAlreadyRegisteredLoggable(var1.toString(), var1.getDocroot(), this.svrMBean.getName(), var7.toString(), var7.getDocroot(), var2);
                  var8.log();
                  throw new DeploymentException(var8.getMessage());
               }
            }

            var5 = var4;
         }

         ContextVersionManager var11 = new ContextVersionManager(var5, var1.getApplicationName(), var1.getContextPath());
         var11.putContext(var1.getVersionId(), var1);
         ServletMapping var13 = (ServletMapping)this.contextTable.clone();
         var13.put(toPattern(var2), var11);
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("WASC registered: " + toPattern(var2) + " with versionId=" + var1.getVersionId() + ", ctx=" + var1 + ", newCtxManager=" + var11);
         }

         this.contextTable = var13;
      }
   }

   synchronized void removeContext(String var1) {
      this.contextTable.remove(var1);
   }

   private static String toPattern(String var0) {
      if (!var0.equals("") && !var0.equals("/")) {
         return var0.endsWith("/") ? var0 + "*" : var0 + "/*";
      } else {
         return "/";
      }
   }

   void destroyContext(WebAppServletContext var1, String var2) {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("Destroying ServletContext with context-root: '" + var1.getContextPath() + "'");
      }

      this.preserveSessionState(var1);
      String var3 = var1.getContextPath();
      synchronized(this.contextTable) {
         ContextVersionManager var5 = (ContextVersionManager)this.contextTable.get(var3);
         if (var5 == null) {
            return;
         }

         ServletMapping var6 = (ServletMapping)this.contextTable.clone();
         if (var5.getServletContexts().size() == 1) {
            var6.remove(toPattern(var3));
         } else {
            var5.removeContext(var2);
         }

         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("WASC destroyed: " + toPattern(var3) + " with versionId=" + var1.getVersionId() + ", ctx=" + var1 + ", oldCtxManager=" + var5);
         }

         this.contextTable = var6;
      }

      var1.destroy();
   }

   private void preserveSessionState(WebAppServletContext var1) {
      WebAppModule var2 = var1.getWebAppModule();
      if (var2 != null && var1.getSessionContext().getConfigMgr().isSaveSessionsOnRedeployEnabled()) {
         SessionContext var3 = var1.getSessionContext();
         var3.storeAttributesInBytes();
         this.preservedState.put(var1.getContextPath(), new PreservedState(var3.getSessionsMap(), var3.getCurrOpenSessionsCount(), var3.getTotalOpenSessionsCount(), var3.getMaxOpenSessionsCount(), var3.getPersistentStoreType()));
      }
   }

   private void reinstateSessionState(WebAppServletContext var1) {
      WebAppModule var2 = var1.getWebAppModule();
      if (var2 != null) {
         String var3 = var1.getContextPath();
         PreservedState var4 = (PreservedState)this.preservedState.get(var3);
         if (var4 != null) {
            SessionContext var5 = var1.getSessionContext();
            if (var4.storeType.equals(var5.getPersistentStoreType())) {
               var5.setSessionsMap(var4.sessions);
               var5.setCurrOpenSessionsCount(var4.curSessCnt);
               var5.setTotalOpenSessionsCount(var4.totalSessCnt);
               var5.setMaxOpenSessionsCount(var4.maxSessCnt);
            }

            this.preservedState.remove(var3);
         }
      }
   }

   void removeSavedSessionState(String var1) {
      this.preservedState.remove(var1);
   }

   public String toString() {
      return "ServletContextManager(" + this.svrMBean.getName() + ")";
   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      if (var1.getPropertyName().equalsIgnoreCase("DefaultWebAppContextRoot")) {
         Object var2 = var1.getOldValue();
         Object var3 = var1.getNewValue();
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Received a PropertyChangeEvent with oldvalue=" + var2 + " and newValue=" + var3);
         }

         if (var3 != null) {
            if (this.contextTable.getDefault() != null) {
               if (HTTPDebugLogger.isEnabled()) {
                  HTTPDebugLogger.debug("We already have a default webapp, cannot assign a new default webapp");
               }

            } else {
               String var4 = (String)var3;
               if (var4.length() >= 2) {
                  if (var2 != null) {
                     String var5 = (String)var2;
                     if (var4.equals(var5)) {
                        return;
                     }
                  }

                  WebAppServletContext var7 = this.getContextForContextPath(var4);
                  if (var7 == null) {
                     if (HTTPDebugLogger.isEnabled()) {
                        HTTPDebugLogger.debug("Didn't find a ServletContext with context-root=" + var4);
                     }

                  } else {
                     var7.setDefaultContext();
                     this.contextTable.remove(toPattern(var4));
                     ContextVersionManager var6 = var7.getContextManager();
                     if (var6 == null) {
                        var6 = new ContextVersionManager((ContextVersionManager)null, var7.getApplicationName(), var7.getContextPath());
                     }

                     this.contextTable.put(toPattern(var7.getContextPath()), var6);
                  }
               }
            }
         }
      }
   }

   private static final class PreservedState {
      Hashtable sessions = null;
      int curSessCnt = 0;
      int totalSessCnt = 0;
      int maxSessCnt = 0;
      String storeType = null;

      PreservedState(Hashtable var1, int var2, int var3, int var4, String var5) {
         this.sessions = var1;
         this.curSessCnt = var2;
         this.totalSessCnt = var3;
         this.maxSessCnt = var4;
         this.storeType = var5;
      }
   }
}
