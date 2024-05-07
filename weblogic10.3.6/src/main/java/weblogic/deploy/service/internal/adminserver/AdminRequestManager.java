package weblogic.deploy.service.internal.adminserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentServiceCallbackHandler;
import weblogic.deploy.service.DeploymentServiceCallbackHandlerV2;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.RequestManager;
import weblogic.deploy.service.internal.transport.CommonMessageSender;
import weblogic.deploy.service.internal.transport.ServerDisconnectListener;
import weblogic.deploy.service.internal.transport.ServerDisconnectManager;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.utils.StackTraceUtils;

public final class AdminRequestManager extends RequestManager {
   private final HashSet pendingCancels;
   private final HashMap requestsMap;
   private final AdminDeploymentsManager adminDeploymentsManager;
   private static final int DEPLOYMENT_SUCCEEDED = 1;
   private static final int COMMIT_FAILURE = 2;
   private static final int CANCEL_SUCCESS = 3;
   private static final int COMMIT_SUCCESS = 4;

   private AdminRequestManager() {
      super("AdminRequestManager");
      this.pendingCancels = new HashSet();
      this.requestsMap = new HashMap();
      this.adminDeploymentsManager = AdminDeploymentsManager.getInstance();
      ServerDisconnectManager.getInstance().initialize();
   }

   public static AdminRequestManager getInstance() {
      return AdminRequestManager.Maker.SINGLETON;
   }

   public ArrayList getDeployments(DomainVersion var1, String var2, long var3, boolean var5, String var6, Set var7) {
      boolean var8 = this.isDebugEnabled();
      ArrayList var9 = new ArrayList();
      Map var10 = var1.getDeploymentsVersionMap();
      DomainVersion var11 = this.adminDeploymentsManager.getCurrentDomainVersion();
      if (var6 != null && var6.length() != 0) {
         var9.addAll(this.getDeploymentsFor(var6, var1, var2, var3));
      } else if (!var11.equals(var1)) {
         if (var8) {
            this.debug("AdminRequestManager: getDeployments: fromVersion '" + var1 + "' is not equal to currentVersion '" + var11 + "' - need to generate deployments to get " + " them in sync");
         }

         Map var12 = var11.getDeploymentsVersionMap();
         Iterator var13 = var12.keySet().iterator();

         label67:
         while(true) {
            Deployment[] var19;
            do {
               String var14;
               Version var15;
               Version var17;
               do {
                  do {
                     do {
                        while(true) {
                           if (!var13.hasNext()) {
                              break label67;
                           }

                           var14 = (String)var13.next();
                           if (var7 != null && var7.contains(var14)) {
                              var15 = (Version)var10.get(var14);
                              var17 = (Version)var12.get(var14);
                              break;
                           }

                           if (var8) {
                              this.debug("Skipping " + var14 + " passed in collection does NOT contain it for server - " + var2);
                           }
                        }
                     } while(var17 == null);
                  } while(var17.equals(var15));
               } while(var15 == null);

               DeploymentServiceCallbackHandler var18 = this.adminDeploymentsManager.getCallbackHandler(var14);
               var19 = var18.getDeployments(var15, var17, var2);
            } while(var19 == null);

            for(int var20 = 0; var20 < var19.length; ++var20) {
               if (var8) {
                  this.debug("adding deployment '" + var19[var20] + "' to getDeploymentsResponse");
               }

               var9.add(var19[var20]);
            }
         }
      }

      if (!var5) {
         CommonMessageSender var21 = CommonMessageSender.getInstance();
         var21.sendGetDeploymentsResponse(var9, var2, var11, var3);
      }

      return var9;
   }

   public synchronized void addToRequestTable(AdminRequestImpl var1) {
      if (this.isDebugEnabled()) {
         this.debug("adding request  '" + var1.getId() + "' to admin request table");
      }

      this.requestsMap.put(new Long(var1.getId()), var1);
   }

   public final synchronized Set getRequests() {
      return this.requestsMap.entrySet();
   }

   public final synchronized AdminRequestImpl getRequest(long var1) {
      return (AdminRequestImpl)this.requestsMap.get(new Long(var1));
   }

   public final synchronized void removeRequest(long var1) {
      if (this.isDebugEnabled()) {
         this.debug("removing request '" + var1 + "' from admin request table");
      }

      this.requestsMap.remove(new Long(var1));
   }

   public final synchronized void addPendingCancel(long var1) {
      Long var3 = new Long(var1);
      if (!this.pendingCancels.contains(var3)) {
         this.pendingCancels.add(var3);
      }

      if (this.isDebugEnabled()) {
         this.debug("adding '" + var1 + "' to list of pending cancels on admin");
      }

   }

   public final synchronized boolean isCancelPending(long var1) {
      Long var3 = new Long(var1);
      return this.pendingCancels.contains(var3);
   }

   public final synchronized void removePendingCancel(long var1) {
      this.pendingCancels.remove(new Long(var1));
   }

   public final void deliverDeploySucceededCallback(AdminRequestImpl var1, Map var2, List var3) {
      long var4 = var1.getId();
      LinkedHashMap var6 = new LinkedHashMap();
      this.setUpCallbacksToFailuresTable(var1, var6, var2);
      this.deliverCallbacks(var6, var4, 1, var3);
   }

   private boolean isDeferredDeploymentTargetInDeploymentTargetCluster(Set var1, String var2) {
      ClusterMBean var3 = TargetHelper.getTargetCluster(var2);
      if (var3 == null) {
         return false;
      } else {
         ServerMBean[] var4 = var3.getServers();
         if (var4 == null) {
            return false;
         } else {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (var1.contains(var4[var5])) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public ArrayList getDeploymentsFor(String var1, DomainVersion var2, String var3, long var4) {
      boolean var6 = this.isDebugEnabled();
      ArrayList var7 = new ArrayList();
      if (var1 != null && var1.length() != 0) {
         Map var8 = var2.getDeploymentsVersionMap();
         DomainVersion var9 = this.adminDeploymentsManager.getCurrentDomainVersion();
         if (var6) {
            this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : current DomainVersion : " + var9);
         }

         if (var9.equals(var2)) {
            if (var6) {
               this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : managed " + "server is at the same version as admin server.");
            }

            return var7;
         } else {
            if (var6) {
               this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "' : managed " + "server is *NOT* at the same version as admin server.");
            }

            Map var10 = var9.getDeploymentsVersionMap();
            Version var11 = (Version)var8.get(var1);
            Version var13 = (Version)var10.get(var1);
            if (var6) {
               this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : " + " fromVersion : '" + var11);
               this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : " + " toVersion : '" + var13);
            }

            if (var13 != null && !var13.equals(var11)) {
               if (var6) {
                  this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : " + " From-Version is *NOT* equal to To-Version");
               }

               DeploymentServiceCallbackHandler var14 = this.adminDeploymentsManager.getCallbackHandler(var1);
               Deployment[] var15 = var14.getDeployments(var11, var13, var3);
               if (var15 != null) {
                  List var16 = Arrays.asList(var15);
                  if (var6) {
                     this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : " + " foundDeployments : " + var16);
                  }

                  var7.addAll(var16);
               } else if (var6) {
                  this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : " + " did *NOT* find Deployments");
               }
            } else if (var6) {
               this.debug(" AdminRequestManager: getDeploymentsFor('" + var1 + "', '" + var3 + "') : " + " From-Version is equal to To-Version");
            }

            if (var6) {
               this.debug("AdminRequestManager.getDeploymentsFor() : returning deployments : " + var7);
            }

            return var7;
         }
      } else {
         return var7;
      }
   }

   private void updateDeferredDeploymentsSubset(Map var1, Set var2, Deployment var3) {
      if (var1 != null && var1.size() > 0) {
         String[] var4 = var3.getTargets();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var1.keySet().contains(var4[var5])) {
               this.addServerToDeferredSet(var2, var4[var5], var3, var1);
            } else if (this.isDeferredDeploymentTargetInDeploymentTargetCluster(var1.keySet(), var4[var5])) {
               ClusterMBean var6 = TargetHelper.getTargetCluster(var4[var5]);
               ServerMBean[] var7 = var6.getServers();

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  String var9 = var7[var8].getName();
                  if (var1.keySet().contains(var9)) {
                     this.addServerToDeferredSet(var2, var9, var3, var1);
                  }
               }
            }
         }
      }

   }

   private void addServerToDeferredSet(Set var1, String var2, Deployment var3, Map var4) {
      if (!var1.contains(var2)) {
         if (this.isDebugEnabled()) {
            this.debug("AdminRequestManager: updateDeferredDeploymentsSubset adding " + var2 + " to deferred set of " + var3.getCallbackHandlerId());
         }

         var1.add(var4.get(var2));
      }

   }

   public final void deliverDeployFailedCallback(AdminRequestImpl var1, AdminDeploymentException var2) {
      long var3 = var1.getId();
      Iterator var5 = var1.getDeployments();
      HashSet var6 = new HashSet();
      boolean var7 = this.isDebugEnabled();

      while(true) {
         while(true) {
            String var9;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               Deployment var8 = (Deployment)var5.next();
               var9 = var8.getCallbackHandlerId();
            } while(var6.contains(var9));

            var6.add(var9);
            DeploymentServiceCallbackHandler var10 = this.adminDeploymentsManager.getCallbackHandler(var9);
            if (var10 == null) {
               if (var7) {
                  this.debug("No DeploymentServiceCallbackHandler to dispatch prepare delivery failure for deployment id '" + var3 + "' for '" + var9 + "'");
               }
            } else {
               try {
                  var10.deployFailed(var3, var2);
               } catch (Throwable var12) {
                  if (var7) {
                     this.debug("DeploymentServiceCallbackHandler deployFailed callback failed for deployment id '" + var3 + "' and deployment type '" + var9 + "' due to '" + var12.getMessage() + "'");
                  }
               }
            }
         }
      }
   }

   public final void deliverCommitFailureCallback(AdminRequestImpl var1, Map var2) {
      long var3 = var1.getId();
      LinkedHashMap var5 = new LinkedHashMap();
      this.setUpCallbacksToFailuresTable(var1, var5, var2);
      this.deliverCallbacks(var5, var3, 2, new ArrayList());
   }

   public final void deliverCommitSucceededCallback(AdminRequestImpl var1) {
      long var2 = var1.getId();
      LinkedHashMap var4 = new LinkedHashMap();
      this.setUpCallbacksToFailuresTable(var1, var4, (Map)null);
      this.deliverCallbacks(var4, var2, 4, new ArrayList());
   }

   public final void deliverDeployCancelSucceededCallback(AdminRequestImpl var1, Map var2) {
      long var3 = var1.getId();
      LinkedHashMap var5 = new LinkedHashMap();
      this.setUpCallbacksToFailuresTable(var1, var5, var2);
      this.deliverCallbacks(var5, var3, 3, new ArrayList());
   }

   private void deliverCallbacks(HashMap var1, long var2, int var4, List var5) {
      if (var1.size() > 0) {
         Iterator var6 = var1.keySet().iterator();
         FailureDescription[] var7 = new FailureDescription[0];
         boolean var8 = this.isDebugEnabled();

         while(true) {
            while(var6.hasNext()) {
               String var9 = (String)var6.next();
               Set var10 = (Set)var1.get(var9);
               DeploymentServiceCallbackHandler var11 = this.adminDeploymentsManager.getCallbackHandler(var9);
               if (var11 == null) {
                  if (var8) {
                     this.debug("No DeploymentServiceCallbackHandler to deliver '" + var4 + "' for deployment id '" + var2 + "' for '" + var9 + "'");
                  }
               } else {
                  try {
                     switch (var4) {
                        case 1:
                           ArrayList var12 = new ArrayList();
                           if (var5 != null) {
                              var12.addAll(var5);
                           }

                           if (var10 != null) {
                              var12.addAll(var10);
                           }

                           var11.deploySucceeded(var2, (FailureDescription[])((FailureDescription[])var12.toArray(var7)));
                           break;
                        case 2:
                           var11.commitFailed(var2, (FailureDescription[])((FailureDescription[])var10.toArray(var7)));
                           break;
                        case 3:
                           var11.cancelSucceeded(var2, (FailureDescription[])((FailureDescription[])var10.toArray(var7)));
                           break;
                        case 4:
                           var11.commitSucceeded(var2);
                     }
                  } catch (Throwable var13) {
                     if (var8) {
                        this.debug("DeploymentServiceCallbackHandler '" + this.getOperationString(var4) + "' callback failed for request '" + var2 + "' and deployment type '" + var9 + "' due to '" + StackTraceUtils.throwable2StackTrace(var13) + "'");
                     }
                  }
               }
            }

            return;
         }
      }
   }

   private void setUpCallbacksToFailuresTable(AdminRequestImpl var1, HashMap var2, Map var3) {
      Iterator var4 = var1.getDeployments();

      while(var4.hasNext()) {
         Deployment var5 = (Deployment)var4.next();
         String var6 = var5.getCallbackHandlerId();
         Object var7 = (Set)var2.get(var6);
         if (var7 == null) {
            var7 = new HashSet();
            var2.put(var6, var7);
         }

         if (var3 != null) {
            this.updateDeferredDeploymentsSubset(var3, (Set)var7, var5);
         }
      }

   }

   public final void deliverDeployCancelFailedCallback(AdminRequestImpl var1, AdminDeploymentException var2) {
      long var3 = var1.getId();
      Iterator var5 = var1.getDeployments();
      boolean var6 = this.isDebugEnabled();

      while(true) {
         while(var5.hasNext()) {
            Deployment var7 = (Deployment)var5.next();
            String var8 = var7.getCallbackHandlerId();
            DeploymentServiceCallbackHandler var9 = this.adminDeploymentsManager.getCallbackHandler(var8);
            if (var9 == null) {
               if (var6) {
                  this.debug("No DeploymentServiceCallbackHandler to deliver failure of deployment id '" + var3 + "' for '" + var8 + "'");
               }
            } else {
               try {
                  var9.cancelFailed(var3, var2);
               } catch (Throwable var11) {
                  if (var6) {
                     this.debug("DeploymentServiceCallbackHandler cancelFailed callback failed for deployment id " + var3 + "' and deployment type '" + var8 + "' due to '" + var11.getMessage() + "'");
                  }
               }
            }
         }

         return;
      }
   }

   public void addPrepareDisconnectListener(String var1, AdminRequestImpl var2) throws RemoteException {
      if (var2 != null) {
         AdminRequestStatus var3 = var2.getStatus();
         DisconnectListener var4 = var3.getPrepareDisconnectListener(var1);
         this.addDisconnectListener(var1, var4);
      }
   }

   public void addCommitDisconnectListener(String var1, AdminRequestImpl var2) throws RemoteException {
      if (var2 != null) {
         AdminRequestStatus var3 = var2.getStatus();
         DisconnectListener var4 = var3.getCommitDisconnectListener(var1);
         this.addDisconnectListener(var1, var4);
      }
   }

   public void addCancelDisconnectListener(String var1, AdminRequestImpl var2) throws RemoteException {
      if (var2 != null) {
         AdminRequestStatus var3 = var2.getStatus();
         DisconnectListener var4 = var3.getCancelDisconnectListener(var1);
         this.addDisconnectListener(var1, var4);
      }
   }

   public void removeDisconnectListener(String var1, DisconnectListener var2) {
      ServerDisconnectManager var3 = ServerDisconnectManager.getInstance();
      ServerDisconnectListener var4 = var3.findDisconnectListener(var1);
      if (var4 != null) {
         var4.unregisterListener(var2);
         if (this.isDebugEnabled()) {
            this.debug(" +++ Removed listener='" + var2 + "' : from : " + var4);
         }

      }
   }

   public final void deliverRequestStatusUpdateCallback(AdminRequestImpl var1, String var2, String var3) {
      long var4 = var1.getId();
      Iterator var6 = var1.getDeployments();

      while(true) {
         while(var6.hasNext()) {
            Deployment var7 = (Deployment)var6.next();
            String var8 = var7.getCallbackHandlerId();
            DeploymentServiceCallbackHandler var9 = this.adminDeploymentsManager.getCallbackHandler(var8);
            if (var9 == null) {
               if (this.isDebugEnabled()) {
                  this.debug("No DeploymentServiceCallbackHandler to deliver '" + var2 + "' for deployment id '" + var4 + "' for '" + var8 + "'");
               }
            } else if (!(var9 instanceof DeploymentServiceCallbackHandlerV2)) {
               if (this.isDebugEnabled()) {
                  this.debug("Callback handler is not V2 to deliver '" + var2 + "' for deployment id '" + var4 + "' for '" + var8 + "'");
               }
            } else {
               DeploymentServiceCallbackHandlerV2 var10 = (DeploymentServiceCallbackHandlerV2)var9;

               try {
                  var10.requestStatusUpdated(var4, var2, var3);
               } catch (Throwable var12) {
                  if (this.isDebugEnabled()) {
                     this.debug("DeploymentServiceCallbackHandlerV2 requestStatusUpdated callback failed for deployment id " + var4 + "' and deployment type '" + var8 + "' due to '" + var12.getMessage() + "'");
                  }
               }
            }
         }

         return;
      }
   }

   private void addDisconnectListener(String var1, DisconnectListener var2) throws RemoteException {
      ServerDisconnectManager var3 = ServerDisconnectManager.getInstance();
      ServerDisconnectListener var4 = var3.findOrCreateDisconnectListener(var1);
      if (var4 == null) {
         String var5 = DeployerRuntimeLogger.serverUnreachable(var1);
         throw new RemoteException(var5);
      } else {
         var4.registerListener(var2);
         if (this.isDebugEnabled()) {
            this.debug(" +++ Added listener='" + var2 + "' : to : " + var4);
         }

      }
   }

   private String getOperationString(int var1) {
      String var2 = null;
      switch (var1) {
         case 1:
            var2 = "DEPLOYMENT_SUCCEEDED";
            break;
         case 2:
            var2 = "COMMIT_FAILURE";
            break;
         case 3:
            var2 = "CANCEL_SUCCESS";
            break;
         case 4:
            var2 = "COMMIT_SUCCESS";
      }

      return var2;
   }

   // $FF: synthetic method
   AdminRequestManager(Object var1) {
      this();
   }

   static class Maker {
      static final AdminRequestManager SINGLETON = new AdminRequestManager();
   }
}
