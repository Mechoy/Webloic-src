package weblogic.deploy.api.spi.deploy.mbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTargetModuleID;
import weblogic.deploy.api.spi.deploy.TargetModuleIDImpl;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.utils.application.WarDetector;

public class ModuleCache extends MBeanCache {
   private static final long serialVersionUID = 1678014076382735164L;
   private static final boolean ddebug = Debug.isDebug("internal");
   transient List moduleTargetCache;
   transient List TMIDCache;
   transient Map tmidMap;
   transient Map beanTargetCache;
   private AppRuntimeStateRuntimeMBean artMBean = null;

   public ModuleCache(DomainMBean var1, WebLogicDeploymentManager var2) throws ServerConnectionException {
      super(var2);
      this.currDomain = var1;
      this.moduleTargetCache = null;
      this.TMIDCache = null;
      this.tmidMap = new HashMap();
      this.listenType = new String[]{"AppDeployments", "Libraries"};
      this.beanTargetCache = null;
      this.addNotificationListener();
   }

   public synchronized ConfigurationMBean[] getTypedMBeans() {
      ArrayList var1 = new ArrayList();
      AppDeploymentMBean[] var2 = this.currDomain.getAppDeployments();
      if (var2 != null) {
         this.add(var2, var1);
      }

      LibraryMBean[] var3 = this.currDomain.getLibraries();
      if (var3 != null) {
         this.add(var3, var1);
      }

      return (ConfigurationMBean[])((ConfigurationMBean[])var1.toArray(new ConfigurationMBean[0]));
   }

   private void add(AppDeploymentMBean[] var1, List var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         AppDeploymentMBean var4 = var1[var3];
         var2.add(var4);
      }

   }

   public synchronized List getModules(Target var1) throws ServerConnectionException {
      if (this.isStale()) {
         this.reset();
      }

      if (!this.moduleTargetCache.contains(var1)) {
         if (ddebug) {
            Debug.say("Populating tmid cache for target " + var1);
         }

         this.moduleTargetCache.add(var1);
         Iterator var3 = this.getMBeans().iterator();

         label115:
         while(true) {
            boolean var2;
            AppDeploymentMBean var4;
            WebLogicTargetModuleID var5;
            TargetInfoMBean[] var8;
            do {
               do {
                  if (!var3.hasNext()) {
                     this.addUnconfiguredModules(var1);
                     break label115;
                  }

                  var4 = (AppDeploymentMBean)var3.next();
               } while(var4 == null);

               if (ddebug) {
                  Debug.say("checking for tmid for " + var4.getName());
               }

               var2 = this.targeted(var4, var1);
               var5 = null;
               if (var2) {
                  var5 = this.getTMID(var4, var1, (TargetInfoMBean)null);
                  if (var5 != null) {
                     if (this.TMIDCache.add(var5)) {
                        if (ddebug) {
                           Debug.say("Added " + var5.toString() + " to cache");
                        }
                     } else if (ddebug) {
                        Debug.say(var5.toString() + " already in cache");
                     }
                  }
               }

               var8 = this.getChildModules(var4);
            } while(var8 == null);

            for(int var12 = 0; var12 < var8.length; ++var12) {
               TargetInfoMBean var10 = var8[var12];
               if (var10 != null) {
                  var2 = this.targeted(var10, var1);
                  WebLogicTargetModuleID var6 = null;
                  if (var2) {
                     if (var5 == null) {
                        var5 = this.addImpliedParent(var4, var1, (TargetInfoMBean)null);
                     }

                     var6 = this.getTMID(var10, var1, var4);
                     if (var6 != null) {
                        this.TMIDCache.add(var6);
                     }
                  }

                  TargetInfoMBean[] var9 = this.getChildModules(var10);
                  if (var9 != null) {
                     for(int var13 = 0; var13 < var9.length; ++var13) {
                        TargetInfoMBean var11 = var9[var13];
                        if (var11 != null) {
                           var2 = this.targeted(var11, var1);
                           if (var2) {
                              if (var6 == null) {
                                 if (var5 == null) {
                                    var5 = this.addImpliedParent(var4, var1, (TargetInfoMBean)null);
                                 }

                                 var6 = this.addImpliedParent(var10, var1, var4);
                              }

                              WebLogicTargetModuleID var7 = this.getTMID(var11, var1, var10);
                              if (var7 != null) {
                                 this.TMIDCache.add(var7);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if (ddebug) {
         this.dump();
      }

      ArrayList var14 = new ArrayList();
      Iterator var15 = this.TMIDCache.iterator();

      while(var15.hasNext()) {
         TargetModuleID var16 = (TargetModuleID)var15.next();
         if (var16.getTarget().getName().equals(var1.getName())) {
            var14.add(var16);
         }
      }

      return var14;
   }

   private AppRuntimeStateRuntimeMBean getAppRuntimeStateRuntimeMBean() {
      if (this.artMBean == null) {
         try {
            this.artMBean = this.dm.getHelper().getAppRuntimeStateMBean();
         } catch (Exception var2) {
            if (ddebug) {
               Debug.say("Can't get app runtime state: " + var2.toString());
            }
         }
      }

      return this.artMBean;
   }

   private void addUnconfiguredModules(Target var1) {
      if (this.getAppRuntimeStateRuntimeMBean() != null) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.TMIDCache.iterator();

         while(var3.hasNext()) {
            TargetModuleID var4 = (TargetModuleID)var3.next();
            if (var4.getParentTargetModuleID() == null && var4.getTarget().getName().equals(var1.getName())) {
               var2.add(var4);
            }
         }

         for(int var9 = 0; var9 < var2.size(); ++var9) {
            WebLogicTargetModuleID var5 = (WebLogicTargetModuleID)var2.get(var9);
            if (var5.getValue() == ModuleType.EAR.getValue()) {
               String[] var6 = this.getAppRuntimeStateRuntimeMBean().getModuleIds(var5.getModuleID());
               if (var6 == null) {
                  continue;
               }

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  String var8 = var6[var7];
                  this.addMissingModule(var8, var5);
               }
            }

            this.addUnconfiguredSubmodules(var5, var5.getModuleID());
         }

      }
   }

   private void addMissingModule(String var1, WebLogicTargetModuleID var2) {
      if (!this.isChild(var1, var2.getChildTargetModuleID())) {
         this.addImpliedChild(var2, var1, WebLogicModuleType.getTypeFromString(this.getAppRuntimeStateRuntimeMBean().getModuleType(var2.getModuleID(), var1)));
      }

   }

   private void addUnconfiguredSubmodules(WebLogicTargetModuleID var1, String var2) {
      int var4;
      if (var1.getValue() == WebLogicModuleType.JMS.getValue()) {
         String[] var3 = this.getAppRuntimeStateRuntimeMBean().getSubmoduleIds(var2, var1.getModuleID());
         if (var3 == null) {
            return;
         }

         for(var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            this.addMissingModule(var5, var1);
         }
      } else {
         TargetModuleID[] var6 = var1.getChildTargetModuleID();
         if (var6 == null) {
            return;
         }

         for(var4 = 0; var4 < var6.length; ++var4) {
            WebLogicTargetModuleID var7 = (WebLogicTargetModuleID)var6[var4];
            this.addUnconfiguredSubmodules(var7, var2);
         }
      }

   }

   private void addImpliedChild(TargetModuleID var1, String var2, ModuleType var3) {
      TargetModuleIDImpl var4 = new TargetModuleIDImpl(var2, var1.getTarget(), var1, var3, this.getDM());
      var4.setTargeted(false);
      if (var3 == ModuleType.WAR) {
         var4.setWebURL(var2);
      }

      this.TMIDCache.add(var4);
   }

   private boolean isChild(String var1, TargetModuleID[] var2) {
      if (var2 == null) {
         return false;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            TargetModuleID var4 = var2[var3];
            if (var1.equals(var4.getModuleID())) {
               return true;
            }
         }

         return false;
      }
   }

   private WebLogicTargetModuleID addImpliedParent(TargetInfoMBean var1, Target var2, TargetInfoMBean var3) {
      WebLogicTargetModuleID var4 = this.getTMID(var1, var2, var3);
      if (var4 != null) {
         var4.setTargeted(false);
         this.TMIDCache.add(var4);
      }

      return var4;
   }

   public TargetInfoMBean[] getChildModules(TargetInfoMBean var1) {
      Object var2 = null;
      if (var1 instanceof AppDeploymentMBean) {
         var2 = ((AppDeploymentMBean)var1).getSubDeployments();
      } else if (var1 instanceof SubDeploymentMBean) {
         var2 = ((SubDeploymentMBean)var1).getSubDeployments();
      }

      if (var2 == null) {
         var2 = new TargetInfoMBean[0];
      }

      return (TargetInfoMBean[])var2;
   }

   public TargetInfoMBean getTargetInfoMBean(TargetModuleIDImpl var1) {
      this.getModules(var1.getTarget());
      return (TargetInfoMBean)this.tmidMap.get(var1);
   }

   public boolean targeted(TargetInfoMBean var1, Target var2) {
      boolean var3 = false;
      String[] var4;
      if (!this.beanTargetCache.containsKey(var1)) {
         var4 = new String[0];
         TargetMBean[] var5 = var1.getTargets();
         if (var5 != null) {
            var4 = new String[var5.length];

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6] == null) {
                  var4[var6] = null;
               } else {
                  var4[var6] = var5[var6].getObjectName().getName();
               }
            }
         }

         this.beanTargetCache.put(var1, var4);
      }

      var4 = (String[])((String[])this.beanTargetCache.get(var1));
      if (var4 != null) {
         for(int var7 = 0; var7 < var4.length; ++var7) {
            if (var4[var7] != null && var4[var7].equals(var2.getName())) {
               var3 = true;
               break;
            }
         }
      }

      if (ddebug && !var3) {
         Debug.say(var1.getName() + " is not targeted to " + var2.getName());
      }

      return var3;
   }

   public TargetModuleID getTMID(AppDeploymentMBean var1, Target var2) {
      if (this.targeted(var1, var2)) {
         Iterator var3 = this.getModules(var2).iterator();

         TargetModuleID var4;
         do {
            if (!var3.hasNext()) {
               return this.getTMID(var1, var2, (TargetInfoMBean)null);
            }

            var4 = (TargetModuleID)var3.next();
         } while(!var4.getModuleID().equals(var1.getName()));

         return var4;
      } else {
         return null;
      }
   }

   public TargetModuleID getResultTmids(String var1, Target var2) {
      Iterator var3 = this.getModules(var2).iterator();

      TargetModuleID var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (TargetModuleID)var3.next();
      } while(!var4.getModuleID().equals(var1));

      return var4;
   }

   private WebLogicTargetModuleID getTMID(TargetInfoMBean var1, Target var2, TargetInfoMBean var3) {
      if (ddebug) {
         Debug.say("Getting TMID for " + var1.getName() + ", target=(" + var2.toString() + ") " + var2.getName());
      }

      ModuleType var4 = this.getModuleType(var1);
      return var4 == null ? null : this.getNewTMID(var1, var4, var2, var3);
   }

   private WebLogicTargetModuleID getNewTMID(TargetInfoMBean var1, ModuleType var2, Target var3, TargetInfoMBean var4) {
      if (ddebug) {
         Debug.say("Creating TMID for " + var1.getName() + ": type=" + var2.toString() + ": value=" + var2.getValue() + ", target=(" + var3.toString() + ") " + var3.getName());
      }

      TargetModuleID var5 = this.getParentTmid(var3, var4);
      TargetModuleIDImpl var6 = new TargetModuleIDImpl(var1.getObjectName().getName(), var3, var5, var2, this.getDM());
      this.tmidMap.put(var6, var1);
      return var6;
   }

   private TargetModuleID getParentTmid(Target var1, TargetInfoMBean var2) {
      if (var2 == null) {
         return null;
      } else {
         Iterator var3 = this.TMIDCache.iterator();

         TargetModuleIDImpl var4;
         do {
            if (!var3.hasNext()) {
               return this.getTMID(var2, var1, var2.getParent() instanceof TargetInfoMBean ? (TargetInfoMBean)var2.getParent() : null);
            }

            var4 = (TargetModuleIDImpl)var3.next();
         } while(!var4.getTarget().getName().equals(var1.getName()) || !var2.getName().equals(var4.getModuleID()) || !this.compareParents(var4, var2));

         return var4;
      }
   }

   private boolean compareParents(TargetModuleID var1, WebLogicMBean var2) {
      TargetModuleID var3 = var1.getParentTargetModuleID();
      WebLogicMBean var4 = var2.getParent();
      String var5 = var3 == null ? "" : var3.getModuleID();
      String var6 = var4 instanceof TargetInfoMBean ? var4.getName() : "";
      if (var5.equals(var6)) {
         return var3 != null ? this.compareParents(var3, var4) : true;
      } else {
         return false;
      }
   }

   private ModuleType getModuleType(TargetInfoMBean var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.getModuleType();
         if (ddebug) {
            Debug.say("Checking type for " + var1.getName() + ", with type attr = " + var2);
         }

         if (var2 != null) {
            return this.getTypeFromString(var2);
         } else {
            if (var1 instanceof AppDeploymentMBean) {
               String var3 = ((AppDeploymentMBean)var1).getSourcePath();
               if (var3 != null) {
                  if (var3.endsWith(".ear")) {
                     return ModuleType.EAR;
                  }

                  if (WarDetector.instance.suffixed(var3)) {
                     return ModuleType.WAR;
                  }

                  if (var3.endsWith(".jar")) {
                     return ModuleType.EJB;
                  }

                  if (var3.endsWith(".rar")) {
                     return ModuleType.RAR;
                  }

                  if (var3.endsWith("-jms.xml")) {
                     return WebLogicModuleType.JMS;
                  }

                  if (var3.endsWith("-jdbc.xml")) {
                     return WebLogicModuleType.JDBC;
                  }

                  if (var3.endsWith("-interception.xml")) {
                     return WebLogicModuleType.INTERCEPT;
                  }
               }
            }

            if (var1 instanceof SubDeploymentMBean && var1.getParent() instanceof SubDeploymentMBean) {
               return WebLogicModuleType.SUBMODULE;
            } else {
               return (ModuleType)(var1 instanceof SubDeploymentMBean && ((AppDeploymentMBean)var1.getParent()).getSourcePath().endsWith("-jms.xml") ? WebLogicModuleType.SUBMODULE : this.getModuleTypeFromState(var1));
            }
         }
      }
   }

   private ModuleType getModuleTypeFromState(TargetInfoMBean var1) {
      try {
         AppRuntimeStateRuntimeMBean var3 = this.dm.getHelper().getAppRuntimeStateMBean();
         String var2;
         if (var1 instanceof AppDeploymentMBean) {
            SubDeploymentMBean[] var4 = ((AppDeploymentMBean)var1).getSubDeployments();
            if (var4 != null && var4.length > 0) {
               return ModuleType.EAR;
            } else {
               var2 = var1.getName();
               return this.getTypeFromString(var3.getModuleType(var2, var2));
            }
         } else {
            var2 = var1.getParent().getName();
            return this.getTypeFromString(var3.getModuleType(var2, var1.getName()));
         }
      } catch (Exception var5) {
         return WebLogicModuleType.UNKNOWN;
      }
   }

   private ModuleType getTypeFromString(String var1) {
      if (ModuleType.EAR.toString().equals(var1)) {
         return ModuleType.EAR;
      } else if (ModuleType.WAR.toString().equals(var1)) {
         return ModuleType.WAR;
      } else if (ModuleType.EJB.toString().equals(var1)) {
         return ModuleType.EJB;
      } else if (ModuleType.RAR.toString().equals(var1)) {
         return ModuleType.RAR;
      } else if (ModuleType.CAR.toString().equals(var1)) {
         return ModuleType.CAR;
      } else if (WebLogicModuleType.JMS.toString().equals(var1)) {
         return WebLogicModuleType.JMS;
      } else if (WebLogicModuleType.JDBC.toString().equals(var1)) {
         return WebLogicModuleType.JDBC;
      } else if (WebLogicModuleType.INTERCEPT.toString().equals(var1)) {
         return WebLogicModuleType.INTERCEPT;
      } else {
         return WebLogicModuleType.SUBMODULE.toString().equals(var1) ? WebLogicModuleType.SUBMODULE : WebLogicModuleType.UNKNOWN;
      }
   }

   public synchronized void reset() {
      super.reset();
      this.moduleTargetCache = new ArrayList();
      this.TMIDCache = new ArrayList();
      this.tmidMap = new HashMap();
      this.beanTargetCache = new HashMap();
   }

   private void dump() {
      Debug.say("Current cache of TMIDs:");
      Iterator var1 = this.TMIDCache.iterator();

      while(var1.hasNext()) {
         Debug.say("  " + ((TargetModuleID)var1.next()).toString());
      }

   }
}
