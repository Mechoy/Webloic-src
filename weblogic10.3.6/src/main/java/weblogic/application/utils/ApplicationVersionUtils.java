package weblogic.application.utils;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFileManager;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.J2EELogger;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.workarea.NoWorkContextException;
import weblogic.workarea.PrimitiveContextFactory;
import weblogic.workarea.PropertyReadOnlyException;
import weblogic.workarea.StringWorkContext;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextMap;

public final class ApplicationVersionUtils {
   private static final String DELIMITER = "#";
   private static final String LIB_DELIMITER = "@";
   public static final int MAX_VERSION_ID_LENGTH = 215;
   private static final String versionIdCharsRegExpr = "[\\w\\.\\-\\_]*";
   private static final String[] invalidVersionIdStrings = new String[]{".", ".."};
   private static final WorkContextMap workCtxMap = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final int defaultPropagationMode = 214;
   private static final int adminModePropagationMode = 150;
   private static final ApplicationAccess applicationAccess = ApplicationAccess.getApplicationAccess();
   private static final AppRuntimeStateManager appRTStateMgr = AppRuntimeStateManager.getManager();
   private static final String APP_CTX_BIND_APPLICATION_ID = "weblogic.BindApplicationId";
   private static final String APP_CTX_APPNAME_PREFIX = "weblogic.app.";
   private static final String APP_CTX_ADMIN_MODE = "weblogic.app.internal.AdminMode";
   private static final String APP_VERSION_MANIFEST_ATTR_NAME = "Weblogic-Application-Version";
   private static final String LIB_NAME_MANIFEST_ATTR_NAME = "Extension-Name";
   private static final String LIB_SPEC_VER_MANIFEST_ATTR_NAME = "Specification-Version";
   private static final String LIB_IMPL_VER_MANIFEST_ATTR_NAME = "Implementation-Version";
   private static final String ADMIN_MODE = "weblogic.app.adminMode";
   private static final boolean defaultAdminMode = false;
   private static final String IGNORE_SESSIONS = "weblogic.app.ignoreSessions";
   private static final boolean defaultIgnoreSessions = false;
   private static final String RMI_GRACE_PERIOD = "weblogic.app.rmiGracePeriod";
   private static final int defaultRMIGracePeriod = 30;
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");

   private static String getFirstToken(String var0, String var1) {
      if (var0 != null && var0.length() != 0) {
         int var2 = var0.indexOf(var1);
         if (var2 == -1) {
            return var0;
         } else {
            String var3 = var0.substring(0, var2);
            return var3.length() == 0 ? null : var3;
         }
      } else {
         return null;
      }
   }

   private static String getSecondToken(String var0, String var1) {
      if (var0 != null && var0.length() != 0) {
         int var2 = var0.indexOf(var1);
         if (var2 == -1) {
            return null;
         } else {
            String var3 = var0.substring(var2 + 1);
            return var3.length() == 0 ? null : var3;
         }
      } else {
         return null;
      }
   }

   public static String getNonVersionedName(String var0) {
      return getFirstToken(var0, "#");
   }

   public static String getApplicationName(String var0) {
      return getNonVersionedName(var0);
   }

   public static String getVersionId(String var0) {
      return getSecondToken(var0, "#");
   }

   public static String getArchiveVersion(String var0) {
      return getFirstToken(var0, "#");
   }

   public static String getArchiveVersion(AppDeploymentMBean var0) {
      return getArchiveVersion(var0.getVersionIdentifier());
   }

   public static String getPlanVersion(String var0) {
      return getSecondToken(var0, "#");
   }

   public static String getPlanVersion(AppDeploymentMBean var0) {
      return getPlanVersion(var0.getVersionIdentifier());
   }

   public static String getApplicationId(String var0, String var1) {
      if (var0 != null && var0.length() != 0) {
         return var1 != null && var1.length() != 0 ? var0 + "#" + var1 : var0;
      } else {
         return null;
      }
   }

   public static String getVersionId(String var0, String var1) {
      if (var0 != null && var0.length() != 0) {
         return var1 != null && var1.length() != 0 ? var0 + "#" + var1 : var0;
      } else {
         return null;
      }
   }

   public static String getLibVersionId(String var0, String var1) {
      if ((var0 == null || var0.length() == 0) && (var1 == null || var1.length() == 0)) {
         return null;
      } else {
         if (var0 == null) {
            var0 = "";
         }

         if (var1 == null) {
            var1 = "";
         }

         return var0 + "@" + var1;
      }
   }

   public static String getLibSpecVersion(String var0) {
      return getFirstToken(var0, "@");
   }

   public static String getLibImplVersion(String var0) {
      return getSecondToken(var0, "@");
   }

   public static boolean isLibraryVersion(String var0) {
      return var0 != null && var0.indexOf("@") != -1;
   }

   public static void setCurrentVersionId(String var0, String var1) {
      if (var0 != null && var0.length() != 0 && var1 != null && var1.length() != 0) {
         try {
            workCtxMap.put(getWorkCtxAppName(var0), PrimitiveContextFactory.create(var1), 214);
            if (DEBUG_APP_VERSION.isEnabled()) {
               Debug.say("*** setCurrentVersionId(appName=" + var0 + ", versionId=" + var1 + ")");
            }
         } catch (PropertyReadOnlyException var3) {
            Debug.assertion(false, "WorkContext property for '" + var0 + "' is read-only");
         }

      }
   }

   public static void setCurrentVersionId(String var0) {
      String var1 = getApplicationName(var0);
      String var2 = getVersionId(var0);
      setCurrentVersionId(var1, var2);
   }

   private static String getWorkCtxAppName(String var0) {
      return "weblogic.app." + var0;
   }

   public static void setCurrentAdminMode(boolean var0) {
      try {
         workCtxMap.put("weblogic.app.internal.AdminMode", PrimitiveContextFactory.create(Boolean.toString(var0)), 150);
         if (DEBUG_APP_VERSION.isEnabled()) {
            Debug.say("*** setAdminMode(adminMode=" + var0 + ")");
         }
      } catch (PropertyReadOnlyException var2) {
         Debug.assertion(false, "WorkContext property for AdminMode is read-only");
      }

   }

   public static void unsetCurrentVersionId(String var0) {
      String var1 = getApplicationName(var0);
      if (var1 != null && var1.length() != 0) {
         String var2 = getWorkCtxAppName(var1);
         if (!workCtxMap.isEmpty() && workCtxMap.get(var2) != null) {
            try {
               workCtxMap.remove(var2);
               if (DEBUG_APP_VERSION.isEnabled()) {
                  Debug.say("*** unsetCurrentVersionId(appName=" + var1 + ")");
               }
            } catch (PropertyReadOnlyException var4) {
               Debug.assertion(false, "WorkContext property for '" + var1 + "' is read-only");
            } catch (NoWorkContextException var5) {
               Debug.assertion(false, "No WorkContext is available");
            }

         }
      }
   }

   public static void unsetCurrentAdminMode() {
      if (!workCtxMap.isEmpty() && workCtxMap.get("weblogic.app.internal.AdminMode") != null) {
         try {
            workCtxMap.remove("weblogic.app.internal.AdminMode");
            if (DEBUG_APP_VERSION.isEnabled()) {
               Debug.say("*** unsetAdminMode");
            }
         } catch (PropertyReadOnlyException var1) {
            Debug.assertion(false, "WorkContext property for AdminMode is read-only");
         } catch (NoWorkContextException var2) {
            Debug.assertion(false, "No WorkContext is available");
         }

      }
   }

   public static String getCurrentVersionId(String var0) {
      if (var0 != null && var0.length() != 0) {
         String var2;
         if (!workCtxMap.isEmpty()) {
            WorkContext var1 = workCtxMap.get(getWorkCtxAppName(var0));
            if (var1 != null && var1 instanceof StringWorkContext) {
               var2 = ((StringWorkContext)var1).toString();
               if (var2.length() > 0) {
                  return var2;
               }
            }
         }

         String var3 = getCurrentApplicationId();
         if (var3 == null) {
            return null;
         } else {
            var2 = getApplicationName(var3);
            return var0.equals(var2) ? getVersionId(var3) : null;
         }
      } else {
         return null;
      }
   }

   public static String getCurrentVersionId() {
      String var0 = applicationAccess.getCurrentApplicationName();
      return getVersionId(var0);
   }

   public static String getCurrentApplicationId() {
      return applicationAccess.getCurrentApplicationName();
   }

   public static boolean getCurrentAdminMode() {
      if (workCtxMap.isEmpty()) {
         return false;
      } else {
         boolean var0 = false;
         WorkContext var1 = workCtxMap.get("weblogic.app.internal.AdminMode");
         if (var1 != null && var1 instanceof StringWorkContext) {
            var0 = Boolean.valueOf(((StringWorkContext)var1).toString());
         }

         return var0;
      }
   }

   public static boolean isAdminModeRequest() {
      boolean var0 = getCurrentAdminMode();
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** isAdminRequest(): isAdminModeSet : " + var0);
      }

      if (var0) {
         return true;
      } else {
         ServerChannel var1 = ServerHelper.getServerChannel();
         boolean var2 = var1 != null && ChannelHelper.isAdminChannel(var1);
         if (DEBUG_APP_VERSION.isEnabled()) {
            Debug.say("*** isAdminRequest(): isAdminChannel() : " + var2);
         }

         if (var2) {
            return true;
         } else {
            AuthenticatedSubject var3 = SecurityServiceManager.getCurrentSubject(kernelId);
            boolean var4 = SubjectUtils.doesUserHaveAnyAdminRoles(var3);
            if (DEBUG_APP_VERSION.isEnabled()) {
               Debug.say("*** isAdminRequest(): doesUserHaveAnyAdminRoles('" + var3 + "') : " + var4);
            }

            if (var4) {
               return true;
            } else {
               ApplicationRuntimeMBean var5 = getCurrentApplicationRuntime();
               boolean var6 = var5 != null && var5.getActiveVersionState() == 1;
               if (DEBUG_APP_VERSION.isEnabled()) {
                  Debug.say("*** isAdminRequest(): isRequestedAppAdminMode() : " + var6);
               }

               if (var6) {
                  return true;
               } else {
                  boolean var7 = getAdminModeAppCtxParam(applicationAccess.getCurrentApplicationContext());
                  if (DEBUG_APP_VERSION.isEnabled()) {
                     Debug.say("*** isAdminRequest(): hasAdminModeCtxParam() : " + var7);
                  }

                  return var7;
               }
            }
         }
      }
   }

   public static String getActiveVersionId(String var0, boolean var1) {
      AppDeploymentMBean var2 = getActiveAppDeployment(getDomain(), var0, var1);
      return var2 == null ? null : var2.getVersionIdentifier();
   }

   public static String getActiveVersionId(String var0) {
      return getActiveVersionId(var0, false);
   }

   public static String getActiveVersionId() {
      String var0 = applicationAccess.getCurrentApplicationName();
      return getActiveVersionId(getApplicationName(var0));
   }

   public static String getDisplayName(String var0, String var1) {
      if (var0 == null) {
         return "";
      } else {
         String var2 = var0;
         if (var1 != null) {
            var2 = var0 + " " + getDisplayVersionId(var1);
         }

         return var2;
      }
   }

   public static String getDisplayName(BasicDeploymentMBean var0) {
      if (var0 == null) {
         return "";
      } else {
         return var0 instanceof AppDeploymentMBean ? getAppDeploymentDisplayName((AppDeploymentMBean)var0) : getSystemResourceDisplayName((SystemResourceMBean)var0);
      }
   }

   private static String getSystemResourceDisplayName(SystemResourceMBean var0) {
      return getDisplayName(var0.getName(), (String)null);
   }

   private static String getAppDeploymentDisplayName(AppDeploymentMBean var0) {
      return getDisplayName(var0.getApplicationName(), var0.getVersionIdentifier());
   }

   public static String getDisplayName(String var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = getApplicationName(var0);
         String var2 = getVersionId(var0);
         return getDisplayName(var1, var2);
      }
   }

   public static String getDisplayVersionId(String var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = null;
         String var2 = getArchiveVersion(var0);
         String var3 = getPlanVersion(var0);
         if (isLibraryVersion(var2)) {
            String var4 = getLibSpecVersion(var2);
            String var5 = getLibImplVersion(var2);
            if (var4 != null) {
               var1 = "[LibSpecVersion=" + var4;
            }

            if (var5 != null) {
               if (var1 == null) {
                  var1 = "[";
               }

               var1 = var1 + ",LibImplVersion=" + var5;
            }
         } else if (var3 == null) {
            var1 = "[Version=" + var2;
         } else {
            var1 = "[ArchiveVersion=" + var2;
         }

         if (var3 != null) {
            var1 = var1 + ",PlanVersion=" + var3;
         }

         if (var1 != null) {
            var1 = var1 + "]";
         }

         return var1;
      }
   }

   public static String replaceDelimiter(String var0, char var1) {
      return var0 == null ? null : var0.replace("#".charAt(0), var1);
   }

   public static boolean isSameComponent(String var0, String var1, String var2, String var3) {
      if (var0 != null && var0.length() != 0) {
         if (var2 != null && var2.length() != 0) {
            String var4 = "#";
            String var5 = "#";
            if (var1 != null && var1.length() > 0) {
               var4 = var4 + var1;
            }

            if (var3 != null && var3.length() > 0) {
               var5 = var5 + var3;
            }

            return var0.replaceAll(var4, var5).equals(var2);
         } else {
            return false;
         }
      } else {
         return var2 == null || var2.length() == 0;
      }
   }

   public static void setBindApplicationId(String var0) {
      if (var0 != null && var0.length() != 0) {
         try {
            workCtxMap.put("weblogic.BindApplicationId", PrimitiveContextFactory.create(var0));
            if (DEBUG_APP_VERSION.isEnabled()) {
               Debug.say("*** setBindAppId(appId=" + var0 + ")");
            }
         } catch (PropertyReadOnlyException var2) {
            Debug.assertion(false, "WorkContext property for BindApplicationId is read-only");
         }

      }
   }

   public static void unsetBindApplicationId() {
      if (!workCtxMap.isEmpty() && workCtxMap.get("weblogic.BindApplicationId") != null) {
         try {
            workCtxMap.remove("weblogic.BindApplicationId");
            if (DEBUG_APP_VERSION.isEnabled()) {
               Debug.say("*** unsetBindAppId");
            }
         } catch (PropertyReadOnlyException var1) {
            Debug.assertion(false, "WorkContext property for BindApplicationId is read-only");
         } catch (NoWorkContextException var2) {
            Debug.assertion(false, "No WorkContext is available");
         }

      }
   }

   public static String getBindApplicationId() {
      if (workCtxMap.isEmpty()) {
         return null;
      } else {
         WorkContext var0 = workCtxMap.get("weblogic.BindApplicationId");
         String var1 = null;
         if (var0 != null && var0 instanceof StringWorkContext) {
            var1 = ((StringWorkContext)var0).toString();
            if (var1.length() > 0) {
               return var1;
            }
         }

         String var2 = applicationAccess.getCurrentApplicationName();
         return getVersionId(var2) != null ? var2 : null;
      }
   }

   public static String getManifestVersion(VirtualJarFile var0) {
      if (var0 == null) {
         return null;
      } else {
         Manifest var1 = null;

         try {
            var1 = var0.getManifest();
         } catch (IOException var5) {
            return null;
         }

         if (var1 != null) {
            Attributes var2 = var1.getMainAttributes();
            if (var2 != null) {
               try {
                  String var3 = var2.getValue("Weblogic-Application-Version");
                  if (var3 != null) {
                     var3 = var3.trim();
                  }

                  return var3;
               } catch (IllegalArgumentException var4) {
                  return null;
               }
            }
         }

         return null;
      }
   }

   public static String getManifestVersion(String var0) {
      if (var0 == null) {
         return null;
      } else {
         VirtualJarFile var1 = null;

         Object var3;
         try {
            var1 = ApplicationFileManager.newInstance(var0).getVirtualJarFile();
            String var2 = getManifestVersion(var1);
            return var2;
         } catch (IOException var13) {
            var3 = null;
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var12) {
               }
            }

         }

         return (String)var3;
      }
   }

   public static String getLibName(VirtualJarFile var0) {
      if (var0 == null) {
         return null;
      } else {
         Manifest var1 = null;

         try {
            var1 = var0.getManifest();
         } catch (IOException var5) {
            return null;
         }

         if (var1 != null) {
            Attributes var2 = var1.getMainAttributes();
            if (var2 != null) {
               try {
                  String var3 = var2.getValue("Extension-Name");
                  if (var3 != null) {
                     var3 = var3.trim();
                  }

                  return var3;
               } catch (IllegalArgumentException var4) {
                  return null;
               }
            }
         }

         return null;
      }
   }

   public static String getLibVersionId(VirtualJarFile var0) {
      if (var0 == null) {
         return null;
      } else {
         Manifest var1 = null;

         try {
            var1 = var0.getManifest();
         } catch (IOException var6) {
            return null;
         }

         if (var1 != null) {
            Attributes var2 = var1.getMainAttributes();
            if (var2 != null) {
               try {
                  String var3 = var2.getValue("Specification-Version");
                  if (var3 != null) {
                     var3 = var3.trim();
                  }

                  String var4 = var2.getValue("Implementation-Version");
                  if (var4 != null) {
                     var4 = var4.trim();
                  }

                  return getLibVersionId(var3, var4);
               } catch (IllegalArgumentException var5) {
                  return null;
               }
            }
         }

         return null;
      }
   }

   public static String getLibName(String var0) {
      if (var0 == null) {
         return null;
      } else {
         VirtualJarFile var1 = null;

         Object var3;
         try {
            var1 = ApplicationFileManager.newInstance(var0).getVirtualJarFile();
            String var2 = getLibName(var1);
            return var2;
         } catch (IOException var13) {
            var3 = null;
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var12) {
               }
            }

         }

         return (String)var3;
      }
   }

   public static String getLibVersionId(String var0) {
      if (var0 == null) {
         return null;
      } else {
         VirtualJarFile var1 = null;

         Object var3;
         try {
            var1 = ApplicationFileManager.newInstance(var0).getVirtualJarFile();
            String var2 = getLibVersionId(var1);
            return var2;
         } catch (IOException var13) {
            var3 = null;
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var12) {
               }
            }

         }

         return (String)var3;
      }
   }

   public static AppDeploymentMBean[] getAppDeployments(String var0) {
      return getAppDeployments(getDomain(), var0);
   }

   public static AppDeploymentMBean[] getAppDeployments(DomainMBean var0, String var1) {
      return getAppDeployments(var0, var1, false);
   }

   public static AppDeploymentMBean[] getAppDeployments(DomainMBean var0, String var1, boolean var2) {
      if (var0 != null && var1 != null) {
         AppDeploymentMBean[] var3;
         ArrayList var4;
         if (var2) {
            var4 = new ArrayList();
            var3 = var0.getInternalAppDeployments();
            if (var3 != null) {
               var4.addAll(Arrays.asList(var3));
            }

            var3 = var0.getAppDeployments();
            if (var3 != null) {
               var4.addAll(Arrays.asList(var3));
            }

            var3 = (AppDeploymentMBean[])((AppDeploymentMBean[])var4.toArray(new AppDeploymentMBean[var4.size()]));
         } else {
            var3 = AppDeploymentHelper.getAppsAndLibs(var0);
         }

         if (var3 == null) {
            return null;
         } else {
            var4 = new ArrayList();

            for(int var5 = 0; var5 < var3.length; ++var5) {
               if (var3[var5].getApplicationName().equals(var1)) {
                  var4.add(var3[var5]);
               }
            }

            if (var4.isEmpty()) {
               return null;
            } else {
               AppDeploymentMBean[] var6 = new AppDeploymentMBean[var4.size()];
               var4.toArray(var6);
               return var6;
            }
         }
      } else {
         return null;
      }
   }

   private static DomainMBean getDomain() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain();
   }

   public static AppDeploymentMBean getAppDeployment(String var0, String var1) {
      return getAppDeployment(getDomain(), var0, var1);
   }

   public static AppDeploymentMBean getAppDeployment(DomainMBean var0, String var1, String var2) {
      if (var0 != null && var1 != null) {
         String var3 = getApplicationId(var1, var2);
         AppDeploymentMBean var4 = AppDeploymentHelper.lookupAppOrLib(var3, var0);
         if (var4 != null) {
            return var4;
         } else {
            String var5 = getArchiveVersion(var2);
            String var6 = getPlanVersion(var2);
            if (var2 != null && var6 != null) {
               return null;
            } else {
               AppDeploymentMBean[] var7 = AppDeploymentHelper.getAppsAndLibs(var0);
               if (var7 == null) {
                  return null;
               } else {
                  AppDeploymentMBean var8 = null;

                  for(int var9 = 0; var9 < var7.length; ++var9) {
                     AppDeploymentMBean var10 = var7[var9];
                     if (var10.getApplicationName().equals(var1)) {
                        if (var2 == null) {
                           return var10;
                        }

                        String var11 = getArchiveVersion(var10.getVersionIdentifier());
                        if (var5.equals(var11)) {
                           if (isActiveVersion(var10)) {
                              return var10;
                           }

                           if (var8 == null) {
                              var8 = var10;
                           }
                        }
                     }
                  }

                  return var8;
               }
            }
         }
      } else {
         return null;
      }
   }

   public static boolean isActiveVersion(AppDeploymentMBean var0) {
      return isActiveVersion(var0.getName());
   }

   public static boolean isActiveVersion(String var0) {
      if (appRTStateMgr.isActiveVersion(var0)) {
         return true;
      } else {
         ApplicationRuntimeMBean var1 = getApplicationRuntime(getApplicationName(var0), getVersionId(var0));
         return var1 != null && (var1.getActiveVersionState() == 1 || var1.getActiveVersionState() == 2);
      }
   }

   public static long getRetireTimeMillis(AppDeploymentMBean var0) {
      return ManagementService.getRuntimeAccess(kernelId).isAdminServer() ? appRTStateMgr.getRetireTimeMillis(var0.getName()) : 0L;
   }

   public static boolean isAdminMode(AppDeploymentMBean var0) {
      return var0 == null ? false : isAdminMode(var0.getName());
   }

   public static boolean isAdminMode(String var0) {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         return appRTStateMgr.isAdminMode(var0);
      } else {
         ApplicationRuntimeMBean var1 = getApplicationRuntime(getApplicationName(var0), getVersionId(var0));
         return var1 != null && var1.getActiveVersionState() == 1;
      }
   }

   public static BasicDeploymentMBean getDeployment(DomainMBean var0, String var1) {
      AppDeploymentMBean var2 = AppDeploymentHelper.lookupAppOrLib(var1, var0);
      return (BasicDeploymentMBean)(var2 != null ? var2 : var0.lookupSystemResource(var1));
   }

   public static AppDeploymentMBean getActiveAppDeployment(DomainMBean var0, String var1, boolean var2) {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         return getActiveAppDeploymentAdmin(var0, var1, var2);
      } else {
         ApplicationRuntimeMBean var3 = getActiveApplicationRuntime(var1, var2);
         if (var3 == null) {
            return null;
         } else {
            String var4 = var3.getApplicationVersion();
            return getAppDeployment(var1, var4);
         }
      }
   }

   public static AppDeploymentMBean getActiveAppDeployment(String var0, boolean var1) {
      return getActiveAppDeployment(getDomain(), var0, var1);
   }

   public static AppDeploymentMBean getActiveAppDeployment(String var0) {
      return getActiveAppDeployment(var0, false);
   }

   public static AppDeploymentMBean getActiveAppDeployment(DomainMBean var0, String var1) {
      return getActiveAppDeployment(var0, var1, false);
   }

   private static AppDeploymentMBean getActiveAppDeploymentAdmin(DomainMBean var0, String var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         DomainMBean var3 = var0;
         if (var0 == null) {
            var3 = getDomain();
         }

         AppDeploymentMBean[] var4 = AppDeploymentHelper.getAppsAndLibs(var3);
         if (var4 == null) {
            return null;
         } else {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               AppDeploymentMBean var6 = var4[var5];
               if (var6.getApplicationName().equals(var1) && isAdminMode(var6) == var2 && (var6.getVersionIdentifier() == null || appRTStateMgr.isActiveVersion(var6))) {
                  return var6;
               }
            }

            return null;
         }
      }
   }

   public static ApplicationRuntimeMBean[] getApplicationRuntimes(String var0) {
      if (var0 == null) {
         return null;
      } else {
         ApplicationRuntimeMBean[] var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getApplicationRuntimes();
         if (var1 == null) {
            return null;
         } else {
            ArrayList var2 = new ArrayList();

            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var1[var3].getApplicationName().equals(var0)) {
                  var2.add(var1[var3]);
               }
            }

            if (var2.isEmpty()) {
               return null;
            } else {
               ApplicationRuntimeMBean[] var4 = new ApplicationRuntimeMBean[var2.size()];
               var2.toArray(var4);
               return var4;
            }
         }
      }
   }

   public static ApplicationRuntimeMBean getApplicationRuntime(String var0, String var1) {
      return getApplicationRuntime(var0, var1, false, 0);
   }

   public static ApplicationRuntimeMBean getActiveApplicationRuntime(String var0, boolean var1) {
      return getApplicationRuntime(var0, (String)null, true, var1 ? 1 : 2);
   }

   public static ApplicationRuntimeMBean getActiveApplicationRuntime(String var0) {
      return getActiveApplicationRuntime(var0, false);
   }

   private static ApplicationRuntimeMBean getApplicationRuntime(String var0, String var1, boolean var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         ApplicationRuntimeMBean[] var4 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getApplicationRuntimes();
         if (var4 == null) {
            return null;
         } else {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               ApplicationRuntimeMBean var6 = var4[var5];
               if (var0.equals(var6.getApplicationName())) {
                  if (var1 == null) {
                     if (!var2 || var6.getActiveVersionState() == var3) {
                        return var6;
                     }
                  } else if (var1.equals(var6.getApplicationVersion())) {
                     return var6;
                  }
               }
            }

            return null;
         }
      }
   }

   public static ApplicationRuntimeMBean getCurrentApplicationRuntime() {
      ApplicationContextInternal var0 = applicationAccess.getCurrentApplicationContext();
      return var0 == null ? null : var0.getRuntime();
   }

   public static void setAppContextParam(ApplicationContext var0, String var1, boolean var2) {
      if (var0 != null) {
         ApplicationContextInternal var3 = (ApplicationContextInternal)var0;
         Object var4 = var3.getApplicationParameters();
         if (var4 == null || var4 == Collections.EMPTY_MAP) {
            var4 = new HashMap();
            var3.setApplicationParameters((Map)var4);
         }

         ((Map)var4).put(var1, Boolean.toString(var2));
      }
   }

   public static void setAdminModeAppCtxParam(ApplicationContext var0, boolean var1) {
      setAppContextParam(var0, "weblogic.app.adminMode", var1);
   }

   public static void setIgnoreSessionsAppCtxParam(ApplicationContext var0, boolean var1) {
      setAppContextParam(var0, "weblogic.app.ignoreSessions", var1);
   }

   public static void setAppContextParam(ApplicationContext var0, String var1, int var2) {
      if (var0 != null) {
         ApplicationContextInternal var3 = (ApplicationContextInternal)var0;
         Object var4 = var3.getApplicationParameters();
         if (var4 == null || var4 == Collections.EMPTY_MAP) {
            var4 = new HashMap();
            var3.setApplicationParameters((Map)var4);
         }

         ((Map)var4).put(var1, Integer.toString(var2));
      }
   }

   public static void setRMIGracePeriodAppCtxParam(ApplicationContext var0, int var1) {
      setAppContextParam(var0, "weblogic.app.rmiGracePeriod", var1);
   }

   public static void setActiveVersionState(ApplicationContextInternal var0, int var1) throws DeploymentException {
      J2EEApplicationRuntimeMBeanImpl var2 = var0.getRuntime();
      ApplicationRuntimeMBean var3 = null;
      if (var2 != null) {
         try {
            if (var2.getApplicationVersion() != null && (var1 == 2 || var1 == 1)) {
               var3 = getActiveApplicationRuntime(var0.getApplicationName(), var1 == 1);
            }

            var2.setActiveVersionState(var1);
            if (var3 != null) {
               if (var0.isStaticDeploymentOperation()) {
                  if (var1 == 1) {
                     PropertyChangeListener[] var4 = ((RuntimeMBeanDelegate)var2).getPropertyChangeListeners();
                     if (var4 != null) {
                        for(int var7 = 0; var7 < var4.length; ++var7) {
                           if (var4[var7].getClass().getName().equals("weblogic.servlet.internal.ContextVersionManager$StateChangeListener")) {
                              ((RuntimeMBeanDelegate)var3).addPropertyChangeListener(var4[var7]);
                           }
                        }
                     }
                  }
               } else {
                  var3.setActiveVersionState(0);
               }
            }
         } catch (Exception var6) {
            Loggable var5 = J2EELogger.logCouldNotSetAppActiveVersionStateLoggable(getDisplayName(var0.getApplicationId()), var6);
            var5.log();
            throw new DeploymentException(var5.getMessage(), var6);
         }
      }

   }

   public static void unsetAppContextParam(ApplicationContext var0, String var1) {
      if (var0 != null) {
         ApplicationContextInternal var2 = (ApplicationContextInternal)var0;
         Map var3 = var2.getApplicationParameters();
         if (var3 != null && var3 != Collections.EMPTY_MAP && var3.remove(var1) != null && var3.size() == 0) {
            var2.setApplicationParameters((Map)null);
         }

      }
   }

   public static void unsetAdminModeAppCtxParam(ApplicationContext var0) {
      unsetAppContextParam(var0, "weblogic.app.adminMode");
   }

   public static void unsetIgnoreSessionsAppCtxParam(ApplicationContext var0) {
      unsetAppContextParam(var0, "weblogic.app.ignoreSessions");
   }

   public static void unsetRMIGracePeriodAppCtxParam(ApplicationContext var0) {
      unsetAppContextParam(var0, "weblogic.app.rmiGracePeriod");
   }

   public static boolean getAppContextParam(ApplicationContext var0, String var1, boolean var2) {
      if (var0 == null) {
         return false;
      } else {
         ApplicationContextInternal var3 = (ApplicationContextInternal)var0;
         String var4 = var3.getApplicationParameter(var1);
         return var4 == null ? var2 : Boolean.valueOf(var4);
      }
   }

   public static boolean getAdminModeAppCtxParam(ApplicationContext var0) {
      return getAppContextParam(var0, "weblogic.app.adminMode", false);
   }

   public static boolean getIgnoreSessionsAppCtxParam(ApplicationContext var0) {
      return getAppContextParam(var0, "weblogic.app.ignoreSessions", false);
   }

   public static int getAppContextParam(ApplicationContext var0, String var1, int var2) {
      if (var0 == null) {
         return var2;
      } else {
         ApplicationContextInternal var3 = (ApplicationContextInternal)var0;
         String var4 = var3.getApplicationParameter(var1);
         return var4 == null ? var2 : Integer.valueOf(var4);
      }
   }

   public static int getRMIGracePeriodAppCtxParam(ApplicationContext var0) {
      return getAppContextParam(var0, "weblogic.app.rmiGracePeriod", 30);
   }

   public static boolean isVersionIdValid(String var0) {
      if (var0 != null && var0.length() != 0) {
         if (var0.length() > 215) {
            return false;
         } else {
            String var1 = getArchiveVersion(var0);
            String var2 = getPlanVersion(var0);
            String var3 = null;
            String var4 = null;
            if (isLibraryVersion(var1)) {
               var3 = getLibSpecVersion(var1);
               var4 = getLibImplVersion(var1);
               var1 = null;
            }

            return isVersionIdComponentValid(var1) && isVersionIdComponentValid(var3) && isVersionIdComponentValid(var4) && isVersionIdComponentValid(var2);
         }
      } else {
         return true;
      }
   }

   private static boolean isVersionIdComponentValid(String var0) {
      if (var0 != null && var0.length() != 0) {
         for(int var1 = 0; var1 < invalidVersionIdStrings.length; ++var1) {
            if (var0.equals(invalidVersionIdStrings[var1])) {
               return false;
            }
         }

         return var0.matches("[\\w\\.\\-\\_]*");
      } else {
         return true;
      }
   }

   public static HashMap getDebugWorkContexts() {
      if (workCtxMap.isEmpty()) {
         return null;
      } else {
         HashMap var0 = new HashMap();

         String var2;
         Object var3;
         for(Iterator var1 = workCtxMap.keys(); var1.hasNext(); var0.put(var2, var3)) {
            var2 = (String)var1.next();
            var3 = workCtxMap.get(var2);
            if (var3 instanceof StringWorkContext) {
               var3 = ((StringWorkContext)var3).toString();
            }
         }

         return var0;
      }
   }

   public static void removeAppWorkContextEntries() {
      if (!workCtxMap.isEmpty()) {
         ArrayList var0 = new ArrayList();
         Iterator var1 = workCtxMap.keys();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            if (var2.startsWith("weblogic.app.")) {
               var0.add(var2);
            }
         }

         var1 = var0.iterator();

         while(var1.hasNext()) {
            try {
               workCtxMap.remove((String)var1.next());
            } catch (PropertyReadOnlyException var3) {
               Debug.assertion(false, "WorkContext property for is read-only");
            } catch (NoWorkContextException var4) {
               Debug.assertion(false, "No WorkContext is available");
            }
         }

      }
   }
}
