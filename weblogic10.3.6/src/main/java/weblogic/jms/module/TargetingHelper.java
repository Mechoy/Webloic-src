package weblogic.jms.module;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.DeploymentManager;
import weblogic.application.ModuleException;
import weblogic.application.ModuleListener;
import weblogic.application.ModuleListenerCtx;
import weblogic.application.SubModuleListenerCtx;
import weblogic.application.SubModuleListenerCtxImpl;
import weblogic.cluster.migration.MigrationManager;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.j2ee.descriptor.wl.TargetableBean;
import weblogic.jms.JMSLogger;
import weblogic.jms.common.JMSDebug;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class TargetingHelper {
   static final int UNCHANGED = 0;
   static final int ADDED = 1;
   static final int REMOVED = 2;
   static final int CHANGED = 3;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String serverName;
   private String clusterName;
   private boolean hasUpdate = false;
   private TargetMBean updatedTargetMBean = null;
   private int updateAction = -1;
   private HashMap acceptedLocalGroups;
   private HashMap acceptedGroups;
   String EARModuleName;
   private String appId;
   private String moduleUri;
   private ModuleListener.State state;
   public HashMap subModuleListenerContextMap;
   private final DeploymentManager deploymentManager;

   TargetingHelper(JMSModule var1, DomainMBean var2, String var3, String var4) {
      this.state = ModuleListener.STATE_NEW;
      this.deploymentManager = DeploymentManager.getDeploymentManager();
      this.acceptedLocalGroups = new HashMap();
      this.acceptedGroups = new HashMap();
      this.subModuleListenerContextMap = new HashMap();
      this.moduleUri = var1.getId();
      this.appId = var4;
      ServerMBean var5 = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.serverName = var5.getName();
      ClusterMBean var6 = var5.getCluster();
      if (var6 != null) {
         this.clusterName = var6.getName();
      }

      this.EARModuleName = var3;
      BasicDeploymentMBean var7 = var1.getBasicDeployment(var2);
      this.analyzeTargets(var7, (UpdateInformation)null, var1.getAppCtx());
   }

   public String getServerName() {
      return this.serverName;
   }

   public void setHasUpdate(boolean var1) {
      this.hasUpdate = var1;
   }

   public boolean isHasUpdate() {
      return this.hasUpdate;
   }

   public void setUpdatedTargetMBean(TargetMBean var1) {
      this.updatedTargetMBean = var1;
   }

   public TargetMBean getUpdatedTargetMBean() {
      return this.updatedTargetMBean;
   }

   public void setUpdateAction(int var1) {
      this.updateAction = var1;
   }

   public int getUpdateAction() {
      return this.updateAction;
   }

   private void fixupList(Map var1, DomainMBean var2) {
      Iterator var3 = var1.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         List var5 = (List)var1.get(var4);
         LinkedList var6 = new LinkedList();
         SubModuleListenerCtxImpl var7 = (SubModuleListenerCtxImpl)this.subModuleListenerContextMap.get(var4);
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("TargetingHelper:activate(): Got " + var7 + " for " + var4);
            this.dumpSubModuleListenerContext((SubModuleListenerCtxImpl)var7);
         }

         this.begin(var7, ModuleListener.STATE_ADMIN);
         Iterator var8 = var5.iterator();

         while(var8.hasNext()) {
            TargetMBean var9 = (TargetMBean)var8.next();
            String var10 = var9.getName();
            Object var11 = null;
            if (var9 instanceof ClusterMBean) {
               var11 = var2.lookupCluster(var10);
            } else if (var9 instanceof ServerMBean) {
               var11 = var2.lookupServer(var10);
            } else if (var9 instanceof MigratableTargetMBean) {
               var11 = var2.lookupMigratableTarget(var10);
            } else if (var9 instanceof JMSServerMBean) {
               var11 = var2.lookupJMSServer(var10);
            } else if (var9 instanceof SAFAgentMBean) {
               var11 = var2.lookupSAFAgent(var10);
            }

            if (var11 == null) {
               this.failed(var7, ModuleListener.STATE_ADMIN);
               throw new AssertionError("Could not find a target of name " + var10 + " in module=" + this.appId + " URI=" + this.moduleUri);
            }

            var6.add(var11);
         }

         this.end(var7, ModuleListener.STATE_ADMIN);
         var1.put(var4, var6);
      }

   }

   void activate() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      Iterator var2 = this.subModuleListenerContextMap.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         SubModuleListenerCtxImpl var4 = (SubModuleListenerCtxImpl)this.subModuleListenerContextMap.get(var3);
         TargetMBean var5 = ((ModuleListenerCtx)var4).getTarget();
         Object var6 = null;
         if (var5 != null) {
            String var7 = var5.getName();
            if (var5 instanceof ClusterMBean) {
               var6 = var1.lookupCluster(var7);
            } else if (var5 instanceof ServerMBean) {
               var6 = var1.lookupServer(var7);
            } else if (var5 instanceof MigratableTargetMBean) {
               var6 = var1.lookupMigratableTarget(var7);
            } else if (var5 instanceof JMSServerMBean) {
               var6 = var1.lookupJMSServer(var7);
            } else if (var5 instanceof SAFAgentMBean) {
               var6 = var1.lookupSAFAgent(var7);
            }

            SubModuleListenerCtxImpl var8 = new SubModuleListenerCtxImpl(((ModuleListenerCtx)var4).getApplicationId(), ((ModuleListenerCtx)var4).getModuleUri(), ((ModuleListenerCtx)var4).getType(), (TargetMBean)var6, var4.getSubModuleName(), var4.getSubModuleTargets());
            this.subModuleListenerContextMap.put(var3, var8);
         }
      }

      this.fixupList(this.acceptedLocalGroups, var1);
      this.fixupList(this.acceptedGroups, var1);
      this.state = ModuleListener.STATE_ADMIN;
   }

   void adminToProduction() {
      if (this.acceptedGroups != null) {
         Iterator var1 = this.acceptedGroups.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            SubModuleListenerCtx var3 = (SubModuleListenerCtx)this.subModuleListenerContextMap.get(var2);
            this.begin(var3, ModuleListener.STATE_ACTIVE);
            this.end(var3, ModuleListener.STATE_ACTIVE);
         }
      }

      this.state = ModuleListener.STATE_PREPARED;
   }

   void productionToAdmin() {
      if (this.acceptedGroups != null) {
         Iterator var1 = this.acceptedGroups.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            SubModuleListenerCtx var3 = (SubModuleListenerCtx)this.subModuleListenerContextMap.get(var2);
            this.begin(var3, ModuleListener.STATE_ADMIN);
            this.end(var3, ModuleListener.STATE_ADMIN);
         }
      }

      this.state = ModuleListener.STATE_PREPARED;
   }

   void unprepare() {
      if (this.acceptedGroups != null) {
         Iterator var1 = this.acceptedGroups.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            SubModuleListenerCtx var3 = (SubModuleListenerCtx)this.subModuleListenerContextMap.get(var2);
            this.begin(var3, ModuleListener.STATE_NEW);
            this.end(var3, ModuleListener.STATE_NEW);
         }
      }

      this.state = ModuleListener.STATE_NEW;
   }

   void deactivate() {
      if (this.acceptedGroups != null) {
         Iterator var1 = this.acceptedGroups.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            SubModuleListenerCtx var3 = (SubModuleListenerCtx)this.subModuleListenerContextMap.get(var2);
            this.begin(var3, ModuleListener.STATE_PREPARED);
            this.end(var3, ModuleListener.STATE_PREPARED);
         }
      }

      this.state = ModuleListener.STATE_PREPARED;
   }

   void prepareUpdate(BasicDeploymentMBean var1, UpdateInformation var2, ApplicationContextInternal var3) throws ModuleException {
      HashMap var4 = new HashMap();
      var2.setAddedGroups(var4);
      HashMap var5 = new HashMap();
      var2.setAddedLocalGroups(var5);
      HashMap var6 = new HashMap();
      var2.setChangedGroups(var6);
      HashMap var7 = new HashMap();
      var2.setChangedLocalGroups(var7);
      if (var1 == null) {
         var2.setRemovedLocalGroups(new HashMap());
         var2.setRemovedGroups(new HashMap());
      } else {
         this.analyzeTargets(var1, var2, var3);
         removeAll(var2.getChangedGroups(), var2.getRemovedGroups());
         removeAll(var2.getChangedLocalGroups(), var2.getRemovedLocalGroups());
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            Iterator var8 = var7.keySet().iterator();

            String var9;
            while(var8.hasNext()) {
               var9 = (String)var8.next();
               JMSDebug.JMSModule.debug("Group " + var9 + " is changed locally in an update");
            }

            var8 = var6.keySet().iterator();

            while(var8.hasNext()) {
               var9 = (String)var8.next();
               JMSDebug.JMSModule.debug("Group " + var9 + " is changed in an update");
            }
         }

      }
   }

   private static void removeAll(HashMap var0, HashMap var1) {
      Set var2 = var1.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var0.remove(var4);
      }

   }

   void activateUpdate(UpdateInformation var1) {
      if (var1 != null) {
         this.acceptedLocalGroups.putAll(var1.getAddedLocalGroups());
         this.acceptedGroups.putAll(var1.getAddedGroups());
         this.acceptedLocalGroups.putAll(var1.getChangedLocalGroups());
         this.acceptedGroups.putAll(var1.getChangedGroups());
         removeAll(this.acceptedLocalGroups, var1.getRemovedLocalGroups());
         removeAll(this.acceptedGroups, var1.getRemovedGroups());
         var1.clearTargetUpdates();
         this.activate();
      }
   }

   void rollbackUpdate(UpdateInformation var1) {
      var1.clearTargetUpdates();
   }

   private boolean isLocallyTargeted(TargetMBean var1) {
      return isLocallyTargeted(var1, this.clusterName, this.serverName, this.hasUpdate, this.updatedTargetMBean, this.updateAction);
   }

   public static boolean isLocallyTargeted(TargetMBean var0, String var1, String var2) {
      return isLocallyTargeted(var0, var1, var2, false, (TargetMBean)null, -1);
   }

   public static boolean isLocallyTargeted(TargetMBean var0, String var1, String var2, boolean var3, TargetMBean var4, int var5) {
      if (var0 == null) {
         return false;
      } else {
         TargetMBean[] var7;
         if (var0 instanceof SAFAgentMBean) {
            SAFAgentMBean var6 = (SAFAgentMBean)var0;
            var7 = var6.getTargets();
            if (var7.length <= 0) {
               return false;
            }

            if (var7.length != 1 || !(var7[0] instanceof MigratableTargetMBean)) {
               for(int var16 = 0; var16 < var7.length; ++var16) {
                  TargetMBean var10 = var7[var16];
                  if (var10 != null) {
                     String var19 = var10.getName();
                     if (var19.equals(var2) || var19.equals(var1)) {
                        return true;
                     }
                  }
               }

               return false;
            }

            var0 = var7[0];
         }

         if (var0 instanceof JMSServerMBean) {
            JMSServerMBean var11 = (JMSServerMBean)var0;
            var7 = var11.getTargets();
            if (var7.length <= 0) {
               return false;
            }

            var0 = var7[0];
            if (var0 == null) {
               return false;
            }
         }

         if (var0 instanceof MigratableTargetMBean) {
            MigratableTargetMBean var12 = (MigratableTargetMBean)var0;
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("TargetingHelper.isLocallyTargeted(): hasUpdate=" + var3 + ", lvUpdatedTargetMBean=" + (var4 != null ? var4.getName() : "") + ", lvUpdateAction=" + var5 + ", clusterName=" + var1 + ", serverName=" + var2 + ", mig(current targetBean)=" + var12.getName() + ", mig.getHostingServer()=" + (var12.getHostingServer() != null ? var12.getHostingServer().getName() : "null") + ", ups=" + (var12.getUserPreferredServer() != null ? var12.getUserPreferredServer().getName() : "null"));
            }

            if (var3 && var5 != -1 && var4 != null) {
               if (var4.getType().equals("JMSServer")) {
                  if (((JMSServerMBean)var4).getTargets().length != 0 && ((JMSServerMBean)var4).getTargets()[0].equals(var12)) {
                     if (var5 != 1 && var5 != 2) {
                        if (JMSDebug.JMSModule.isDebugEnabled()) {
                           JMSDebug.JMSModule.debug("TargetingHelper.isLocallyTargeted(): JMSServer: updatedTargetMBean" + var4 + " got removed from serverName=" + var2);
                        }

                        return false;
                     }

                     if (JMSDebug.JMSModule.isDebugEnabled()) {
                        JMSDebug.JMSModule.debug("TargetingHelper.isLocallyTargeted(): JMSServer: updatedTargetMBean" + var4 + " got added to serverName=" + var2);
                     }

                     return true;
                  }

                  if (var12.getMigrationPolicy().equals("manual")) {
                     int var17 = MigrationManager.singleton().getMigratableState(var12.getName());
                     if (var17 != 2 && var17 != 1) {
                        return false;
                     }

                     return true;
                  }

                  ServerMBean var15 = var12.getHostingServer();
                  if (var15 != null && var15.getName().equals(var2)) {
                     return true;
                  }

                  return false;
               }

               if (var4.getType().equals("SAFAgent")) {
                  SAFAgentMBean var14 = (SAFAgentMBean)var4;
                  TargetMBean[] var8 = var14.getTargets();
                  if (var8.length == 1 && var8[0].equals(var12)) {
                     if (var5 != 1 && var5 != 2) {
                        if (JMSDebug.JMSModule.isDebugEnabled()) {
                           JMSDebug.JMSModule.debug("TargetingHelper.isLocallyTargeted(): SAFAgent: updatedTargetMBean" + var4 + " got remvoed from serverName=" + var2);
                        }

                        return false;
                     }

                     if (JMSDebug.JMSModule.isDebugEnabled()) {
                        JMSDebug.JMSModule.debug("TargetingHelper.isLocallyTargeted(): SAFAgent: updatedTargetMBean" + var4 + " got added to serverName=" + var2);
                     }

                     return true;
                  }

                  if (var12.getMigrationPolicy().equals("manual")) {
                     int var18 = MigrationManager.singleton().getMigratableState(var12.getName());
                     if (var18 != 2 && var18 != 1) {
                        return false;
                     }

                     return true;
                  }

                  ServerMBean var9 = var12.getHostingServer();
                  if (var9 != null && var9.getName().equals(var2)) {
                     return true;
                  }

                  return false;
               }
            }

            var0 = var12.getHostingServer();
            if (var0 == null) {
               return false;
            }
         }

         String var13 = ((TargetMBean)var0).getName();
         return var13.equals(var2) || var13.equals(var1);
      }
   }

   private static void compareToBaseline(String var0, UpdateInformation var1, HashMap var2, HashMap var3, HashMap var4, HashMap var5, TargetMBean var6, boolean var7) {
      List var8 = (List)var2.get(var0);
      if (var8 == null) {
         if (var7) {
            Object var12;
            if (var3.containsKey(var0)) {
               var12 = (List)var3.get(var0);
            } else {
               var12 = new LinkedList();
               var3.put(var0, var12);
            }

            ((List)var12).add(var6);
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Group " + var0 + " added in an update");
            }

         }
      } else if (var7) {
         var5.remove(var0);
         Iterator var9 = var8.iterator();

         TargetMBean var10;
         do {
            if (!var9.hasNext()) {
               Object var11;
               if (!var4.containsKey(var0)) {
                  var11 = new LinkedList(var8);
                  var4.put(var0, var11);
               } else {
                  var11 = (List)var4.get(var0);
               }

               ((List)var11).add(var6);
               return;
            }

            var10 = (TargetMBean)var9.next();
         } while(!var10.getClass().equals(var6.getClass()) || !var10.getName().equals(var6.getName()));

      }
   }

   private static void addToGroupMap(Map var0, String var1, Object var2) {
      Object var3;
      if (var0.containsKey(var1)) {
         var3 = (List)var0.get(var1);
      } else {
         var3 = new LinkedList();
         var0.put(var1, var3);
      }

      ((List)var3).add(var2);
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Group " + var1 + " added");
      }

   }

   private void updateMaps(String var1, TargetMBean var2, UpdateInformation var3, TargetMBean var4) {
      boolean var5 = this.isLocallyTargeted(var2);
      if (var3 != null) {
         compareToBaseline(var1, var3, this.acceptedLocalGroups, var3.getAddedLocalGroups(), var3.getChangedLocalGroups(), var3.getRemovedLocalGroups(), var4, var5);
         compareToBaseline(var1, var3, this.acceptedGroups, var3.getAddedGroups(), var3.getChangedGroups(), var3.getRemovedGroups(), var4, true);
      } else {
         if (var5) {
            addToGroupMap(this.acceptedLocalGroups, var1, var4);
         }

         addToGroupMap(this.acceptedGroups, var1, var4);
      }

   }

   private SubDeploymentMBean[] getSubModules(BasicDeploymentMBean var1, SubDeploymentMBean[] var2) {
      SubDeploymentMBean[] var3 = var1.getSubDeployments();
      if (var3 == null) {
         return null;
      } else if (this.EARModuleName == null) {
         return var3;
      } else {
         int var4;
         for(var4 = 0; var4 < var3.length; ++var4) {
            SubDeploymentMBean var5 = var3[var4];
            if (this.EARModuleName.equals(var5.getName())) {
               return var5.getSubDeployments();
            }
         }

         if (var2 != null && var2.length > 0) {
            for(var4 = 0; var4 < var2.length; ++var4) {
               if (this.EARModuleName.equals(var2[var4].getName())) {
                  return var2[var4].getSubDeployments();
               }
            }
         }

         JMSLogger.logNoEARSubDeployment(this.EARModuleName, var1.getName());
         return null;
      }
   }

   private void analyzeTargets(BasicDeploymentMBean var1, UpdateInformation var2, ApplicationContextInternal var3) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Analyzing targets in " + var1.getName());
      }

      if (var2 != null) {
         HashMap var4 = (HashMap)this.acceptedGroups.clone();
         HashMap var5 = (HashMap)this.acceptedLocalGroups.clone();
         var2.setRemovedGroups(var4);
         var2.setRemovedLocalGroups(var5);

         assert var2.getChangedGroups().size() == 0;

         assert var2.getChangedLocalGroups().size() == 0;
      }

      SubDeploymentMBean[] var16 = this.getSubModules(var1, var3.getLibrarySubDeployments());
      if (var16 != null) {
         for(int var17 = 0; var17 < var16.length; ++var17) {
            SubDeploymentMBean var6 = var16[var17];
            String var7 = var6.getName();
            TargetMBean[] var8 = var6.getTargets();
            Object var9 = (SubModuleListenerCtx)this.subModuleListenerContextMap.get(var7);
            TargetMBean var11;
            if (var9 == null) {
               TargetMBean[] var10 = var1.getTargets();
               var11 = var10.length > 0 ? var10[0] : null;
               var9 = new SubModuleListenerCtxImpl(this.appId, this.moduleUri, WebLogicModuleType.MODULETYPE_SUBMODULE, var11, var7, var8);
               if (JMSDebug.JMSModule.isDebugEnabled()) {
                  JMSDebug.JMSModule.debug("TargetingHelper:analyzeTargets(): Created a new " + var9 + " for " + var7);
               }

               this.subModuleListenerContextMap.put(var7, var9);
            }

            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("TargetingHelper:analyzeTargets(): Got " + var9 + " for " + var7);
               this.dumpSubModuleListenerContext((SubModuleListenerCtxImpl)var9);
            }

            this.begin((SubModuleListenerCtx)var9, ModuleListener.STATE_PREPARED);

            for(int var18 = 0; var18 < var8.length; ++var18) {
               var11 = var8[var18];
               TargetMBean[] var13;
               int var14;
               TargetMBean var15;
               if (var11 instanceof JMSServerMBean) {
                  JMSServerMBean var19 = (JMSServerMBean)var11;
                  var13 = var19.getTargets();

                  for(var14 = 0; var14 < var13.length; ++var14) {
                     var15 = var13[var14];
                     this.updateMaps(var7, var15, var2, var11);
                  }
               } else if (var11 instanceof SAFAgentMBean) {
                  SAFAgentMBean var12 = (SAFAgentMBean)var11;
                  var13 = var12.getTargets();

                  for(var14 = 0; var14 < var13.length; ++var14) {
                     var15 = var13[var14];
                     this.updateMaps(var7, var15, var2, var11);
                  }
               } else {
                  this.updateMaps(var7, var11, var2, var11);
               }

               this.end((SubModuleListenerCtx)var9, ModuleListener.STATE_PREPARED);
            }
         }

         this.state = ModuleListener.STATE_PREPARED;
      }
   }

   private static boolean hasTargetingChanged(Map var0, Map var1, Map var2) {
      return var1 != null && var1.size() > 0 || var0 != null && var0.size() > 0 || var2 != null && var2.size() > 0;
   }

   static boolean hasTargetingChanged(UpdateInformation var0) {
      return hasTargetingChanged(var0.getAddedGroups(), var0.getRemovedGroups(), var0.getChangedGroups()) || hasTargetingChanged(var0.getAddedLocalGroups(), var0.getRemovedLocalGroups(), var0.getChangedLocalGroups());
   }

   int getGroupTargetChangeStatus(TargetableBean var1, String var2, UpdateInformation var3, boolean var4) {
      HashMap var5;
      if (var4) {
         var5 = this.acceptedGroups;
      } else {
         var5 = this.acceptedLocalGroups;
      }

      if (var3 == null) {
         return !var5.containsKey(var2) && !var1.isDefaultTargetingEnabled() ? 0 : 1;
      } else if (var1.isDefaultTargetingEnabled()) {
         return var3.hasDefaultTargetsChanged() ? 3 : 0;
      } else {
         HashMap var6;
         HashMap var7;
         HashMap var8;
         if (var4) {
            var6 = var3.getAddedGroups();
            var7 = var3.getChangedGroups();
            var8 = var3.getRemovedGroups();
         } else {
            var6 = var3.getAddedLocalGroups();
            var7 = var3.getChangedLocalGroups();
            var8 = var3.getRemovedLocalGroups();
         }

         if (var6.containsKey(var2)) {
            return 1;
         } else if (var8.containsKey(var2)) {
            return 2;
         } else {
            return var7.containsKey(var2) ? 3 : 0;
         }
      }
   }

   List getTarget(TargetInfoMBean var1, TargetableBean var2, String var3, UpdateInformation var4, boolean var5) {
      LinkedList var6 = null;
      if (var2 != null && var1 != null && var2.isDefaultTargetingEnabled()) {
         TargetMBean[] var11 = var1.getTargets();
         if (var11 != null) {
            var6 = new LinkedList();

            for(int var12 = 0; var12 < var11.length; ++var12) {
               if (var5 || this.isLocallyTargeted(var11[var12])) {
                  var6.add(var11[var12]);
               }
            }
         }

         return var6;
      } else if (var3 == null) {
         return null;
      } else {
         List var7;
         if (var5) {
            var7 = (List)this.acceptedGroups.get(var3);
         } else {
            var7 = (List)this.acceptedLocalGroups.get(var3);
         }

         if (var4 == null) {
            return var7;
         } else {
            HashMap var8;
            HashMap var9;
            HashMap var10;
            if (var5) {
               var8 = var4.getAddedGroups();
               var9 = var4.getRemovedGroups();
               var10 = var4.getChangedGroups();
            } else {
               var8 = var4.getAddedLocalGroups();
               var9 = var4.getRemovedLocalGroups();
               var10 = var4.getChangedLocalGroups();
            }

            if (var7 != null) {
               if (var9 == null) {
                  return var7;
               } else if (var9.containsKey(var3)) {
                  return null;
               } else {
                  return var10 != null && var10.containsKey(var3) ? (List)var10.get(var3) : var7;
               }
            } else {
               return var8 == null ? null : (List)var8.get(var3);
            }
         }
      }
   }

   private void dumpSubModuleListenerContext(SubModuleListenerCtxImpl var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("SubModuleListenerContxext " + var1);
      var2.append("\n  Application Id     : " + var1.getApplicationId());
      var2.append("\n  Module URI         : " + var1.getModuleUri());
      var2.append("\n  Module Target      : " + (var1.getTarget() != null ? var1.getTarget().getName() : ""));
      var2.append("\n  Sub Module Name    : " + var1.getSubModuleName());
      StringBuffer var3 = new StringBuffer();
      TargetMBean[] var4 = var1.getSubModuleTargets();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3.append(var4[var5].getName());
         if (var5 < var4.length - 1) {
            var3.append(",");
         }
      }

      var2.append("\n  Sub Module Targets : " + var3.toString());
      System.out.println(var2.toString());
   }

   private void begin(SubModuleListenerCtx var1, ModuleListener.State var2) {
      Iterator var3 = this.deploymentManager.getModuleListeners();

      while(var3.hasNext()) {
         ((ModuleListener)var3.next()).beginTransition((ModuleListenerCtx)var1, this.state, var2);
      }

   }

   private void end(SubModuleListenerCtx var1, ModuleListener.State var2) {
      Iterator var3 = this.deploymentManager.getModuleListeners();

      while(var3.hasNext()) {
         ((ModuleListener)var3.next()).endTransition((ModuleListenerCtx)var1, this.state, var2);
      }

   }

   private void failed(SubModuleListenerCtx var1, ModuleListener.State var2) {
      Iterator var3 = this.deploymentManager.getModuleListeners();

      while(var3.hasNext()) {
         ((ModuleListener)var3.next()).failedTransition((ModuleListenerCtx)var1, this.state, var2);
      }

   }
}
