package weblogic.jndi.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.jndi.JNDILogger;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;

final class VersionHandler extends AbstractAdminModeHandler {
   private ActiveVersionInfo activeVersionInfo = null;
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");
   private static final boolean debug;
   private static final Class[] globalResources;

   VersionHandler(ServerNamingNode var1) {
      super(var1);
   }

   boolean isVersioned() {
      return this.getActiveVersionInfo() != null;
   }

   private ActiveVersionInfo getActiveVersionInfo() {
      return this.activeVersionInfo;
   }

   private ActiveVersionInfo getOrCreateActiveVersionInfo(String var1, String var2) {
      if (this.activeVersionInfo == null) {
         this.activeVersionInfo = new ActiveVersionInfo(var1, var2);
      }

      return this.activeVersionInfo;
   }

   Object getCurrentVersion(ServerNamingNode var1, Hashtable var2) throws NoPermissionException, NamingException {
      ActiveVersionInfo var3 = var1.getVersionHandler().getActiveVersionInfo();
      if (var3 == null) {
         return var1;
      } else {
         String var4 = ApplicationVersionUtils.getCurrentVersionId(var3.appName);
         if (var4 == null) {
            if (this.allowExternalAppLookup(var2)) {
               return this.getActiveVersionObjectAndInit(var3, var1, var2);
            } else {
               var4 = this.getCurrentVersionIdFromEnv(var3.appName, var2);
               if (var4 != null) {
                  return this.getActiveVersionObject(var3, var1, var2);
               } else {
                  if (debug) {
                     NamingDebugLogger.debug("+++ allowExternalAppLookup check failed: ActiveVersionInfo=" + var3 + ", CurrentApp=" + ApplicationVersionUtils.getCurrentApplicationId() + ", CurrentWorkContext=" + ApplicationVersionUtils.getDebugWorkContexts() + ", JNDIEnv=" + (var2 == null ? "" : var2.toString()) + "\n" + StackTraceUtils.throwable2StackTrace(new Exception()));
                  }

                  JNDILogger.logExternalAppLookupWarning(var1.getNameInNamespace(""));
                  return this.getActiveVersionObjectAndInit(var3, var1, var2);
               }
            }
         } else {
            try {
               Object var5 = var1.lookupHere(var4, var2, "");
               if (debug) {
                  NamingDebugLogger.debug("+++ lookupHere(" + var1.getNameInNamespace(var4) + ") returns " + var5);
               }

               return var5;
            } catch (NameNotFoundException var6) {
               if (this.relaxVersionLookup(var2)) {
                  return this.getActiveVersionObjectAndInit(var3, var1, var2);
               } else {
                  throw this.node.newNameNotFoundException(var6.getMessage() + "  Possibly version '" + var4 + "' of " + "application '" + var3.appName + "' was retired.  To relax " + "lookup to return the active version, set context environment " + "property defined by weblogic.jndi.WLContext.RELAX_VERSION_LOOKUP " + "to \"true\".", "", var2);
               }
            }
         }
      }
   }

   private boolean allowExternalAppLookup(Hashtable var1) {
      return var1 != null && "true".equalsIgnoreCase(this.node.getProperty(var1, "weblogic.jndi.allowExternalAppLookup"));
   }

   private boolean relaxVersionLookup(Hashtable var1) {
      return var1 != null && "true".equalsIgnoreCase(this.node.getProperty(var1, "weblogic.jndi.relaxVersionLookup"));
   }

   private String getCurrentVersionIdFromEnv(String var1, Hashtable var2) {
      if (var2 == null) {
         return null;
      } else {
         String var3 = this.node.getProperty(var2, "weblogic.jndi.lookupApplicationId");
         String var4 = ApplicationVersionUtils.getApplicationName(var3);
         return var1 != null && var1.equals(var4) ? ApplicationVersionUtils.getVersionId(var3) : null;
      }
   }

   private Object getActiveVersionObject(ActiveVersionInfo var1, ServerNamingNode var2, Hashtable var3) throws NameNotFoundException, NamingException {
      return this.getActiveVersionObject(var1, var2, var3, false);
   }

   private Object getActiveVersionObjectAndInit(ActiveVersionInfo var1, ServerNamingNode var2, Hashtable var3) throws NameNotFoundException, NamingException {
      return this.getActiveVersionObject(var1, var2, var3, true);
   }

   private Object getActiveVersionObject(ActiveVersionInfo var1, ServerNamingNode var2, Hashtable var3, boolean var4) throws NameNotFoundException, NamingException {
      boolean var5 = this.isAdminModeRequest(var1);
      String var6 = var5 ? var1.adminModeVersionId : var1.versionId;
      if (var6 == null) {
         if (debug) {
            NamingDebugLogger.debug("+++ getActiveVersion failed, info=" + var1 + ", isAdmin=" + this.isAdminModeRequest(var1));
         }

         throw this.node.newNameNotFoundException("Unable to resolve '" + var2.getNameInNamespace("") + "'.  " + "Possibly previously active version was already unbound.", "", var3);
      } else {
         Object var7;
         if (var5) {
            var7 = var1.adminModeObject;
         } else {
            var7 = var1.object;
         }

         if (var4) {
            ApplicationVersionUtils.setCurrentVersionId(var1.appName, var6);
            if (debug) {
               NamingDebugLogger.debug("+++ setCurrentVersionId(appName=" + var1.appName + ", versionId=" + var6 + ", admin=" + var5 + ")");
            }
         }

         return var7;
      }
   }

   private boolean isAdminModeRequest(ActiveVersionInfo var1) {
      return var1.adminModeVersionId == null ? false : ApplicationVersionUtils.isAdminModeRequest();
   }

   boolean isBindVersioned() {
      return ApplicationVersionUtils.getBindApplicationId() != null;
   }

   void bindHere(String var1, Object var2, Hashtable var3) throws NoPermissionException, NamingException {
      if (var2 instanceof Context) {
         throw new NamingException("Context cannot be versioned");
      } else {
         ServerNamingNode var4 = null;
         String var5 = ApplicationVersionUtils.getBindApplicationId();
         String var6 = ApplicationVersionUtils.getApplicationName(var5);

         try {
            Object var7 = this.node.superLookupHere(var1, var3, "");
            if (!(var7 instanceof ServerNamingNode)) {
               throw this.node.fillInException(new NameAlreadyBoundException(var1 + " is already bound"), var1, var7, "");
            }

            var4 = (ServerNamingNode)var7;
            this.checkApp(var4, var6, var1);
         } catch (NameNotFoundException var8) {
            var4 = (ServerNamingNode)this.node.createSubnodeHere(var1, this.getNoReplicateBindingsEnv(var3));
         }

         String var9 = ApplicationVersionUtils.getVersionId(var5);
         if (debug) {
            NamingDebugLogger.debug("+++ >>> bindVersionHere(" + var1 + ", " + var4.getNameInNamespace(var9) + ", " + var2.getClass().getName() + ")");
         }

         var4.bindHere(var9, var2, var3, false);
         if (debug) {
            NamingDebugLogger.debug("+++ <<< bindVersionHere(" + var1 + ", " + var4.getNameInNamespace(var9) + ", " + var2.getClass().getName() + ") succeeded");
         }

         this.updateActiveVersionInfo(var4, var6, var9, var2);
      }
   }

   void rebindHere(String var1, Object var2, Hashtable var3) throws NoPermissionException, NamingException {
      if (var2 instanceof Context) {
         throw new NamingException("Context cannot be versioned");
      } else {
         ServerNamingNode var4 = null;
         String var5 = ApplicationVersionUtils.getBindApplicationId();
         String var6 = ApplicationVersionUtils.getApplicationName(var5);
         String var7 = ApplicationVersionUtils.getVersionId(var5);

         try {
            Object var8 = this.node.superLookupHere(var1, var3, "");
            if (!(var8 instanceof ServerNamingNode)) {
               throw this.node.fillInException(new NamingException(var1 + " was bound without version previously." + "  Cannot rebind with version '" + var7 + "'."), var1, var8, "");
            }

            var4 = (ServerNamingNode)var8;
            this.checkApp(var4, var6, var1);
         } catch (NameNotFoundException var9) {
            var4 = (ServerNamingNode)this.node.createSubnodeHere(var1, this.getNoReplicateBindingsEnv(var3));
         }

         if (debug) {
            NamingDebugLogger.debug("+++ >>> rebindVersionHere(" + var4.getNameInNamespace(var7) + ", " + var2.getClass().getName() + ")");
         }

         var4.rebindHere(var7, var2, var3, false);
         if (debug) {
            NamingDebugLogger.debug("+++ <<< rebindVersionHere(" + var4.getNameInNamespace(var7) + ", " + var2.getClass().getName() + ") succeeded");
         }

         this.updateActiveVersionInfo(var4, var6, var7, var2);
      }
   }

   void unbindHere(String var1, Object var2, Hashtable var3) throws NoPermissionException, NamingException {
      Object var4 = this.node.superLookupHere(var1, var3, "");
      String var5 = ApplicationVersionUtils.getBindApplicationId();
      String var6 = ApplicationVersionUtils.getApplicationName(var5);
      String var7 = ApplicationVersionUtils.getVersionId(var5);
      if (!(var4 instanceof ServerNamingNode)) {
         throw this.node.fillInException(new NamingException(var1 + " is bound without version previously.  Cannot unbind " + "with version '" + var7 + "'."), var1, var4, "");
      } else {
         ServerNamingNode var8 = (ServerNamingNode)var4;
         this.checkApp(var8, var6, var1);
         if (debug) {
            NamingDebugLogger.debug("+++ >>> unbindVersionHere(" + var8.getNameInNamespace(var7) + ")");
         }

         var8.unbindHere(var7, var2, var3, false);
         if (debug) {
            NamingDebugLogger.debug("+++ <<< unbindVersionHere(" + var8.getNameInNamespace(var7) + ") succeeded");
         }

         ActiveVersionInfo var9 = var8.getVersionHandler().getActiveVersionInfo();
         if (var9 != null && var8.isUnbound(var7)) {
            var9.unsetActive(var7);
         }

      }
   }

   void checkUnbind(String var1, Hashtable var2) throws NamingException {
      if (this.isVersioned() && this.node.getNumOfBindings() == 0) {
         if (debug) {
            NamingDebugLogger.debug("+++ No more versions after unbinding " + var1 + ", unbindHere(" + this.node.getNameInNamespace() + ")");
         }

         String var3 = this.node.getRelativeName();
         ServerNamingNode var4 = (ServerNamingNode)this.node.getParent();
         var4.destroySubnodeHere(var3, this.getNoReplicateBindingsEnv(var2));
      }

   }

   private void checkApp(ServerNamingNode var1, String var2, String var3) throws NamingException {
      if (var1.isVersioned()) {
         ActiveVersionInfo var4 = var1.getVersionHandler().getActiveVersionInfo();
         if (var4 != null && var4.name != null && !var4.appName.equals(var2) && var1.getNumOfBindings() > 0) {
            throw this.node.fillInException(new NamingException(var3 + " was previously bound from another application '" + var4.appName + "'"), var3, (Object)null, "");
         }
      }
   }

   private Hashtable getNoReplicateBindingsEnv(Hashtable var1) {
      if (!this.node.replicateBindings(var1)) {
         return var1;
      } else {
         Hashtable var2 = new Hashtable(var1);
         var2.put("weblogic.jndi.replicateBindings", "false");
         return var2;
      }
   }

   private void updateActiveVersionInfo(ServerNamingNode var1, String var2, String var3, Object var4) throws NoPermissionException, NamingException {
      ActiveVersionInfo var5 = var1.getVersionHandler().getOrCreateActiveVersionInfo(var1.getNameInNamespace(), var2);
      var5.delaySetActive(var3, var4);
   }

   private boolean allowGlobalResourceLookup(Hashtable var1) {
      return var1 != null && "true".equalsIgnoreCase(this.node.getProperty(var1, "weblogic.jndi.allowGlobalResourceLookup"));
   }

   void checkGlobalResource(Object var1, Hashtable var2) throws NamingException {
      if (ApplicationVersionUtils.getCurrentVersionId() != null) {
         int var3 = 0;

         for(int var4 = globalResources.length; var3 < var4; ++var3) {
            if (globalResources[var3].isInstance(var1)) {
               if (this.allowGlobalResourceLookup(var2)) {
                  return;
               }

               if (debug) {
                  NamingDebugLogger.debug("+++ checkGlobalResource failed: CurrentApp=" + ApplicationVersionUtils.getCurrentApplicationId() + ", CurrentWorkContext=" + ApplicationVersionUtils.getDebugWorkContexts() + ", JNDIEnv=" + (var2 == null ? "" : var2.toString()) + ", object=" + var1 + ", class=" + var1.getClass().getName() + "\n" + StackTraceUtils.throwable2StackTrace(new Exception()));
               }

               String var5 = ApplicationVersionUtils.getDisplayName(ApplicationVersionUtils.getCurrentApplicationId());
               JNDILogger.logGlobalResourceLookupWarning(this.node.getNameInNamespace(""), var5);
            }
         }

      }
   }

   static {
      debug = DEBUG_APP_VERSION.isEnabled() || NamingDebugLogger.isDebugEnabled();
      globalResources = new Class[]{DataSource.class, Destination.class, XADataSource.class};
   }

   private final class ActiveVersionInfo {
      private String name;
      private String appName;
      private String versionId;
      private Object object;
      private String adminModeVersionId;
      private Object adminModeObject;
      private Hashtable listeners;

      private ActiveVersionInfo(String var2, String var3) {
         this.listeners = new Hashtable();
         this.name = var2;
         this.appName = var3;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("ActiveVersionInfo[name=").append(this.name).append(",appName=").append(this.appName).append(",version=").append(this.versionId).append(",adminVersion=").append(this.adminModeVersionId).append("]");
         return var1.toString();
      }

      private void setActive(String var1, Object var2, boolean var3) {
         if (var3) {
            this.adminModeVersionId = var1;
            this.adminModeObject = var2;
            VersionHandler.this.setAdminMode(var1);
         } else {
            this.versionId = var1;
            this.object = var2;
         }

         if (VersionHandler.debug) {
            NamingDebugLogger.debug("+++ ActiveVersionInfo.setActive(name=" + this.name + ", appName=" + this.appName + ", versionId=" + var1 + ", admin=" + var3 + "), info=" + this);
         }

      }

      private void delaySetActive(String var1, Object var2) {
         ApplicationRuntimeMBean var3 = ApplicationVersionUtils.getCurrentApplicationRuntime();
         if (var3 == null) {
            this.setActive(var1, var2, false);
         } else {
            if (VersionHandler.debug) {
               NamingDebugLogger.debug("+++ ActiveVersionInfo.delaySetActive(name=" + this.name + ", appName=" + this.appName + ", versionId=" + var1 + ")");
            }

            StateChangeListener var4 = new StateChangeListener(this, var1, var2);
            var3.addPropertyChangeListener(var4);
            this.listeners.put(var1, var4);
            int var5 = var3.getActiveVersionState();
            if (this.isActiveState(var5)) {
               this.setActive(var1, var2, var5 == 1);
            }
         }

      }

      private void unsetActive(String var1) {
         if (VersionHandler.debug) {
            NamingDebugLogger.debug("+++ ActiveVersionInfo.unsetActive(name=" + this.name + ", appName=" + this.appName + ", versionId=" + var1 + ", info=" + this);
         }

         if (var1.equals(this.versionId)) {
            this.versionId = null;
            this.object = null;
         } else if (var1.equals(this.adminModeVersionId)) {
            this.adminModeVersionId = null;
            this.adminModeObject = null;
         }

         VersionHandler.this.unsetAdminMode(var1);
         StateChangeListener var2 = (StateChangeListener)this.listeners.remove(var1);
         if (var2 != null) {
            ApplicationRuntimeMBean var3 = ApplicationVersionUtils.getCurrentApplicationRuntime();
            if (var3 != null) {
               try {
                  var3.removePropertyChangeListener(var2);
               } catch (Throwable var5) {
               }

            }
         }
      }

      private void adminTransition(String var1, Object var2, boolean var3, boolean var4) {
         if (var3 && !var4) {
            if (this.adminModeVersionId != null && this.adminModeVersionId.equals(var1)) {
               this.adminModeVersionId = null;
               this.adminModeObject = null;
            }

            this.versionId = var1;
            this.object = var2;
            VersionHandler.this.unsetAdminMode(var1);
         } else if (!var3 && var4) {
            if (this.versionId != null && this.versionId.equals(var1)) {
               this.versionId = null;
               this.object = null;
            }

            this.adminModeVersionId = var1;
            this.adminModeObject = var2;
            VersionHandler.this.setAdminMode(var1);
         }

         if (VersionHandler.debug) {
            NamingDebugLogger.debug("+++ ActiveVersionInfo.adminTransition(name=" + this.name + ", appName=" + this.appName + ", versionId=" + var1 + ", admin=" + var4 + "), info=" + this);
         }

      }

      private boolean isActiveState(int var1) {
         return var1 == 2 || var1 == 1;
      }

      // $FF: synthetic method
      ActiveVersionInfo(String var2, String var3, Object var4) {
         this(var2, var3);
      }

      private final class StateChangeListener implements PropertyChangeListener {
         ActiveVersionInfo info;
         String versionId;
         Object object;

         private StateChangeListener(ActiveVersionInfo var2, String var3, Object var4) {
            this.info = var2;
            this.versionId = var3;
            this.object = var4;
         }

         public void propertyChange(PropertyChangeEvent var1) {
            if (var1.getPropertyName().equalsIgnoreCase("ActiveVersionState")) {
               Object var2 = var1.getOldValue();
               Object var3 = var1.getNewValue();
               if (var2 instanceof Integer && var3 instanceof Integer) {
                  int var4 = (Integer)var2;
                  int var5 = (Integer)var3;
                  if (VersionHandler.debug) {
                     NamingDebugLogger.debug("+++ StateChange for name=" + ActiveVersionInfo.this.name + ", appName=" + ActiveVersionInfo.this.appName + ", versionId=" + this.versionId + ", oldState=" + var4 + ", newState=" + var5);
                  }

                  if (var4 == var5) {
                     return;
                  }

                  if (ActiveVersionInfo.this.isActiveState(var5)) {
                     if (var4 == 0) {
                        ActiveVersionInfo.this.setActive(this.versionId, this.object, var5 == 1);
                     } else if (ActiveVersionInfo.this.isActiveState(var4)) {
                        ActiveVersionInfo.this.adminTransition(this.versionId, this.object, var4 == 1, var5 == 1);
                     }
                  } else if (ActiveVersionInfo.this.isActiveState(var4)) {
                     ActiveVersionInfo.this.unsetActive(this.versionId);
                  }
               }
            }

         }

         // $FF: synthetic method
         StateChangeListener(ActiveVersionInfo var2, String var3, Object var4, Object var5) {
            this(var2, var3, var4);
         }
      }
   }
}
