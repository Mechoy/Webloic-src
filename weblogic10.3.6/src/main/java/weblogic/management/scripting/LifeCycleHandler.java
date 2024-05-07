package weblogic.management.scripting;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.management.InstanceNotFoundException;
import javax.management.RuntimeOperationsException;
import javax.naming.CommunicationException;
import javax.naming.InitialContext;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.jmx.RemoteRuntimeException;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleTaskRuntimeMBean;
import weblogic.management.scripting.utils.ErrorInformation;
import weblogic.management.scripting.utils.WLSTMsgTextFormatter;
import weblogic.management.scripting.utils.WLSTUtil;
import weblogic.rjvm.PeerGoneException;
import weblogic.server.ServerLifecycleException;
import weblogic.utils.FileUtils;
import weblogic.utils.JavaExec;
import weblogic.utils.StringUtils;

public class LifeCycleHandler implements Serializable {
   WLScriptContext ctx = null;
   private static WLSTMsgTextFormatter txtFmt = new WLSTMsgTextFormatter();
   static final int ATTEMPT_SUCCESS = 0;
   static final int ATTEMPT_FAILED = 1;
   static final int ATTEMPT_UNKNOWN = 2;

   public LifeCycleHandler(WLScriptContext ctx) {
      this.ctx = ctx;
   }

   private boolean getBooleanFromString(String s) {
      return s.toLowerCase(Locale.US).equals("true");
   }

   public boolean shutdown(String name, String entityType, String ignoreSessions, int timeOut, String force, String block) throws ScriptException {
      boolean sdCurrentServer = false;
      ServerLifeCycleTaskRuntimeMBean taskBean = null;
      if (WLSTUtil.runningWLSTAsModule()) {
         block = "true";
      }

      boolean ignoreSes = this.getBooleanFromString(ignoreSessions);
      boolean blockCall = this.getBooleanFromString(block);

      try {
         ServerLifeCycleRuntimeMBean slrbean = null;
         if (!entityType.equals("Server")) {
            if (!entityType.equals("Cluster")) {
               this.setShutdownFailure(txtFmt.getInvalidShutdownEntity(entityType), (Throwable)null);
               return sdCurrentServer;
            }

            this.shutdownCluster(name, force, timeOut, ignoreSes);
         } else if (this.ctx.isAdminServer) {
            this.ctx.println(txtFmt.getShutdownServer(this.ctx.serverName, name, force));
            if (this.ctx.serverName.equals(name)) {
               sdCurrentServer = true;
            }

            try {
               slrbean = this.getServerLifecycleRuntimeMBean(name);
               if (slrbean == null) {
                  this.setShutdownFailure("The server " + name + " is not running.", (Throwable)null);
                  return sdCurrentServer;
               }

               if (force.equals("true")) {
                  taskBean = slrbean.forceShutdown();
               } else {
                  taskBean = slrbean.shutdown(timeOut, ignoreSes);
               }
            } catch (InstanceNotFoundException var14) {
               this.setShutdownFailure("Cannot find the instance of Server " + name, var14);
               return sdCurrentServer;
            }

            try {
               this.handleShutdownTask(taskBean, name, blockCall);
               if (taskBean.getStatus().equals("FAILED")) {
                  this.setShutdownFailure((String)null, taskBean.getError());
               }
            } catch (RemoteRuntimeException var13) {
            }
         } else if (this.ctx.serverName.equals(name)) {
            sdCurrentServer = true;
            this.ctx.println(txtFmt.getShutdownServer(this.ctx.serverName, name, force));
            if (force.equals("true")) {
               this.ctx.runtimeServiceMBean.getServerRuntime().forceShutdown();
            } else {
               this.ctx.runtimeServiceMBean.getServerRuntime().shutdown(timeOut, ignoreSes);
            }

            this.ctx.dc("true");
         } else {
            this.ctx.println(txtFmt.getCannotShutdownFromManaged());
         }

         this.setShutdownSuccess(sdCurrentServer);
      } catch (ServerLifecycleException var15) {
         this.setShutdownFailure("ServerLifeCycle Exception occured.", var15);
      } catch (InstanceNotFoundException var16) {
         this.setShutdownFailure("Cannot find the Instance of ClusterMBean", var16);
      } catch (NoSuchObjectException var17) {
         this.setShutdownSuccessIfCurrent(sdCurrentServer, (String)null, var17);
      } catch (weblogic.rmi.extensions.RemoteRuntimeException var18) {
         this.setShutdownSuccessIfCurrent(sdCurrentServer, (String)null, var18);
      } catch (RemoteRuntimeException var19) {
         this.setShutdownSuccessIfCurrent(sdCurrentServer, (String)null, var19);
      } catch (UndeclaredThrowableException var20) {
         if (var20.getCause() instanceof PeerGoneException) {
            this.setShutdownSuccessIfCurrent(sdCurrentServer, (String)null, var20);
         } else {
            this.setShutdownFailure("Cannot find the Instance of ClusterMBean", var20);
         }
      } catch (RuntimeException var21) {
         Throwable cause = var21.getCause();
         if (cause instanceof NoSuchObjectException) {
            this.setShutdownSuccessIfCurrent(sdCurrentServer, (String)null, var21);
         } else {
            this.setShutdownFailure((String)null, var21);
         }
      } catch (Throwable var22) {
         this.setShutdownFailure((String)null, var22);
      }

      return sdCurrentServer;
   }

   private ServerLifeCycleRuntimeMBean getServerLifecycleRuntimeMBean(String serverName) throws InstanceNotFoundException {
      if (this.ctx.isDomainRuntimeServerEnabled) {
         return this.ctx.domainRuntimeServiceMBean.getDomainRuntime().lookupServerLifeCycleRuntime(serverName);
      } else {
         return this.ctx.isCompatabilityServerEnabled ? (ServerLifeCycleRuntimeMBean)this.ctx.home.getMBean(serverName, "ServerLifeCycleRuntime") : null;
      }
   }

   private void handleShutdownTask(ServerLifeCycleTaskRuntimeMBean taskBean, String name, boolean blockCall) throws RemoteRuntimeException {
      if (taskBean != null && taskBean.isRunning()) {
         if (!blockCall) {
            String taskName = name + "Task";
            WLSTUtil.getWLSTInterpreter().set(taskName, taskBean);
            this.ctx.println(txtFmt.getServerShutdownTaskAvailable(name, taskName));
         } else {
            if (!WLSTUtil.runningWLSTAsModule()) {
               WLSTUtil.getWLSTInterpreter().set(name + "Task", taskBean);
            }

            this.blockOnTask(taskBean);
         }
      }

   }

   private void blockOnTask(ServerLifeCycleTaskRuntimeMBean taskBean) throws RemoteRuntimeException {
      while(true) {
         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var3) {
         }

         if (!taskBean.isRunning()) {
            return;
         }

         System.out.print(".");
      }
   }

   private void setShutdownFailure(String msg, Throwable e) throws ScriptException {
      if (msg == null) {
         msg = "Error shutting down the server";
      }

      this.ctx.shutdownSuccessful = false;
      if (e != null) {
         this.ctx.throwWLSTException(msg, e);
      } else {
         this.ctx.throwWLSTException(msg);
      }

   }

   private void setShutdownSuccessIfCurrent(boolean sdCurrentServer, String msg, Throwable e) throws ScriptException {
      if (sdCurrentServer) {
         this.setShutdownSuccess(sdCurrentServer);
      } else {
         this.setShutdownFailure(msg, e);
      }

   }

   private void setShutdownSuccess(boolean sdCurrentServer) throws ScriptException {
      this.ctx.shutdownSuccessful = true;
      if (sdCurrentServer) {
         try {
            this.ctx.dc("true");
         } catch (Throwable var3) {
            this.ctx.throwWLSTException("Error shutting down connection", var3);
         }
      }

   }

   public void shutdownCluster(String clusterName, String force, int timeOut, boolean ignoreSes) throws Throwable {
      try {
         if (clusterName == null || "".equals(clusterName)) {
            this.ctx.println(txtFmt.getShutdownClusterRequiresName());
         }

         this.ctx.println(txtFmt.getShutdownCluster(clusterName));
         ClusterMBean cMBean = null;
         if (this.ctx.isAdminServer) {
            boolean clusterExists = false;
            if (clusterName != null) {
               cMBean = this.ctx.getDomainRuntimeDomainMBean().lookupCluster(clusterName);
               if (cMBean != null) {
                  clusterExists = true;
               }
            }

            if (!clusterExists) {
               this.ctx.throwWLSTException("There is no cluster defined with name " + clusterName);
            }

            HashMap results = new HashMap();
            ServerMBean[] count;
            if (clusterExists) {
               count = cMBean.getServers();

               try {
                  for(int i = 0; i < count.length; ++i) {
                     ServerMBean server = count[i];
                     ServerLifeCycleRuntimeMBean slr = server.lookupServerLifeCycleRuntime();
                     results.put(server.getName(), force.equals("true") ? slr.forceShutdown() : slr.shutdown(timeOut, ignoreSes));

                     try {
                        Thread.currentThread();
                        Thread.sleep(1000L);
                     } catch (Exception var13) {
                     }
                  }
               } catch (Exception var14) {
                  RuntimeException re = new RuntimeException(var14);
                  throw new RuntimeOperationsException(re);
               }
            }

            count = cMBean.getServers();
            String[] serverNames = new String[count.length];

            int i;
            for(i = 0; i < count.length; ++i) {
               serverNames[i] = count[i].getName();
            }

            for(i = 0; i < count.length; ++i) {
               ServerLifeCycleTaskRuntimeMBean bean = (ServerLifeCycleTaskRuntimeMBean)results.get(count[i].getName());
               if (bean != null && bean.isRunning()) {
                  do {
                     Thread.sleep(1000L);
                  } while(bean.isRunning());
               }
            }

            this.ctx.println(txtFmt.getClusterShutdownIssued(clusterName));
         }
      } catch (Throwable var15) {
         this.ctx.throwWLSTException("Error while shutting down the cluster", var15);
      }

   }

   public Object startCluster(String clusterName, String block) throws Throwable {
      ServerLifeCycleTaskRuntimeMBean[] tbeans = null;
      if (WLSTUtil.runningWLSTAsModule()) {
         block = "true";
      }

      try {
         ClusterMBean cMBean = null;
         if (!this.ctx.isAdminServer) {
            this.ctx.throwWLSTException("You should be connected to admin server to start a cluster.");
            return tbeans;
         }

         boolean clusterExists = false;
         if (clusterName != null) {
            cMBean = this.ctx.getDomainRuntimeDomainMBean().lookupCluster(clusterName);
            if (cMBean != null) {
               clusterExists = true;
            }
         }

         if (!clusterExists) {
            this.ctx.throwWLSTException("There is no cluster defined with name " + clusterName);
         }

         HashMap results = new HashMap();
         ServerMBean[] count = cMBean.getServers();
         String[] serverNames = new String[count.length];

         for(int i = 0; i < count.length; ++i) {
            serverNames[i] = count[i].getName();
         }

         int j;
         if (clusterExists) {
            this.ctx.println("\nStarting the following servers in Cluster, " + clusterName + ": " + StringUtils.join(serverNames, ","));
            ServerMBean[] _servers = cMBean.getServers();
            tbeans = new ServerLifeCycleTaskRuntimeMBean[count.length];

            try {
               for(j = 0; j < _servers.length; ++j) {
                  ServerMBean server = _servers[j];
                  tbeans[j] = this.getServerLifecycleRuntimeMBean(server.getName()).start();
                  results.put(server.getName(), tbeans[j]);

                  try {
                     Thread.currentThread();
                     Thread.sleep(1000L);
                  } catch (Exception var15) {
                  }
               }
            } catch (Exception var17) {
               this.ctx.throwWLSTException("Problem starting cluster " + clusterName, var17);
            }
         }

         ServerLifeCycleTaskRuntimeMBean bean;
         if (block.equals("true")) {
            boolean success = false;

            for(j = 0; j < count.length; ++j) {
               bean = (ServerLifeCycleTaskRuntimeMBean)results.get(count[j].getName());
               if (bean != null && bean.getStatus().equals("TASK IN PROGRESS")) {
                  try {
                     Integer in = new Integer(180000);
                     long quitTime = System.currentTimeMillis() + in.longValue();

                     do {
                        this.ctx.print(".");
                        Thread.sleep(1000L);
                     } while(bean.getStatus().equals("TASK IN PROGRESS"));

                     success = true;
                  } catch (Exception var16) {
                     this.ctx.throwWLSTException("Error getting the status from the lifecycle bean", var16);
                  }
               }
            }

            if (success) {
               boolean suc = true;

               for(int k = 0; k < count.length; ++k) {
                  ServerLifeCycleRuntimeMBean urlServerRuntimeMBean1 = this.ctx.getDomainRuntimeDomainRuntimeMBean().lookupServerLifeCycleRuntime(serverNames[k]);
                  if (!urlServerRuntimeMBean1.getState().equals("RUNNING")) {
                     this.ctx.throwWLSTException("Could not start server " + serverNames[k]);
                     suc = false;
                  }
               }

               if (suc) {
                  this.ctx.println("\nAll servers in the cluster " + clusterName + " are started successfully.");
               } else {
                  this.ctx.throwWLSTException("Unable to start some of the servers in the cluster " + clusterName + ". Please check if the Node Manager is up and running.");
               }
            } else {
               this.ctx.throwWLSTException("None of the servers in the cluster " + clusterName + " could be started. Please check if the Node Manager is up and running. " + "It could also be possible that the servers are already running. " + "Check the state of the servers or cluster by using the state command.");
            }
         } else {
            Iterator resIter = results.keySet().iterator();

            while(resIter.hasNext()) {
               String svrnm = (String)resIter.next();
               bean = (ServerLifeCycleTaskRuntimeMBean)results.get(svrnm);
               WLSTUtil.getWLSTInterpreter().set(svrnm + "Task", bean);
               this.ctx.println("\nThe server start status task for server " + svrnm + " is assigned to variable " + svrnm + "Task");
            }

            this.ctx.println("\nYou can call the getStatus(), getError(), getDescription() or isRunning()\nmethods on these variables to determine the status of your cluster start\n");
         }
      } catch (Throwable var18) {
         this.ctx.throwWLSTException("Error starting the cluster", var18);
      }

      return tbeans;
   }

   public Object startServer(String targetServerName, String listenAddress, int listenPort, String block) throws Throwable {
      ServerLifeCycleTaskRuntimeMBean taskMBean = null;

      try {
         if (WLSTUtil.runningWLSTAsModule()) {
            block = "true";
         }

         try {
            if (!this.ctx.isAdminServer || !this.ctx.isConnected()) {
               if (this.ctx.nmService.isConnectedToNM()) {
                  this.ctx.nmService.nmStart(targetServerName, (String)null, (Properties)null, (Writer)null);
                  return taskMBean;
               }

               this.ctx.throwWLSTException("You should be connected to an admin server or a NM to start a server");
            }

            if (this.ctx.serverName.equals(targetServerName)) {
               this.ctx.errorMsg = "Server with name " + targetServerName + " is already running";
               return taskMBean;
            }

            boolean found = false;
            ServerMBean targetServerMBean = null;
            this.ctx.print("\nStarting server " + targetServerName + " ...");
            ServerLifeCycleRuntimeMBean targetServerLifeCycleRuntimeMBean = this.ctx.domainRuntimeServiceMBean.getDomainRuntime().lookupServerLifeCycleRuntime(targetServerName);
            if (targetServerLifeCycleRuntimeMBean != null) {
               taskMBean = targetServerLifeCycleRuntimeMBean.start();
            } else {
               ServerLifeCycleRuntimeMBean targetServerLifeCycleRuntimeMBean1 = null;
               if (this.ctx.compatDomainRuntimeMBean != null) {
                  targetServerLifeCycleRuntimeMBean1 = this.ctx.compatDomainRuntimeMBean.lookupServerLifeCycleRuntime(((ServerMBean)targetServerMBean).getName());
               }

               if (targetServerLifeCycleRuntimeMBean1 == null) {
                  this.ctx.throwWLSTException("Unable to lookup the ServerLifeCycleRuntimeMBean for server " + targetServerName);
               }

               taskMBean = targetServerLifeCycleRuntimeMBean1.start();
            }

            if (block.equals("true")) {
               if (taskMBean != null && taskMBean.isRunning()) {
                  do {
                     this.ctx.print(".");
                     Thread.sleep(1000L);
                  } while(taskMBean.isRunning());
               }

               if (taskMBean.getStatus().equals("TASK COMPLETED")) {
                  this.ctx.println("\nServer with name " + targetServerName + " started successfully");
                  if (!WLSTUtil.runningWLSTAsModule()) {
                     WLSTUtil.getWLSTInterpreter().set(targetServerName + "Task", taskMBean);
                  }
               } else {
                  if (taskMBean.getError() != null) {
                     System.err.println(taskMBean.getError().getMessage());
                  }

                  this.ctx.errorMsg = "Server with name " + targetServerName + " failed to be started ";
                  this.ctx.errorInfo = new ErrorInformation(this.ctx.errorMsg);
                  this.ctx.exceptionHandler.handleException(this.ctx.errorInfo);
               }
            } else {
               WLSTUtil.getWLSTInterpreter().set(targetServerName + "Task", taskMBean);
               this.ctx.println("\nThe server start status task for server " + targetServerName + " is assigned to variable " + targetServerName + "Task");
               this.ctx.println("\nYou can call the getStatus(), getError(), getDescription() or isRunning() \nmethods on this variable to determine the status of your server start\n");
            }
         } catch (SecurityException var10) {
            this.ctx.throwWLSTException("Security Exception occured.", var10);
         } catch (ServerLifecycleException var11) {
            this.ctx.throwWLSTException("ServerLifeCycle Exception occured.", var11);
         }
      } catch (Throwable var12) {
         this.ctx.throwWLSTException("Error starting the server", var12);
      }

      return taskMBean;
   }

   public HashMap state(String name, String type) throws Throwable {
      if (type.equals("Server")) {
         return this.serverState(name);
      } else if (type.equals("Cluster")) {
         return this.clusterState(name);
      } else {
         this.ctx.throwWLSTException("Please specify the right entity type Server or Cluster.");
         return null;
      }
   }

   public HashMap serverState(String urlServerName) throws Throwable {
      if (!this.ctx.isAdminServer) {
         this.ctx.throwWLSTException("You should be connected to an Administration Server to perform this action.");
      }

      HashMap hmap = new HashMap();

      try {
         ServerLifeCycleRuntimeMBean slcBean = this.ctx.domainRuntimeServiceMBean.getDomainRuntime().lookupServerLifeCycleRuntime(urlServerName);
         if (slcBean != null) {
            hmap.put(slcBean.getName(), slcBean.getState());
            this.ctx.println("Current state of '" + urlServerName + "' : " + slcBean.getState());
            return hmap;
         }
      } catch (Throwable var4) {
         this.ctx.throwWLSTException("Error getting the server state", var4);
      }

      this.ctx.throwWLSTException("No Server with name \"" + urlServerName + "\" configured in the domain");
      return null;
   }

   private HashMap clusterState(String name) throws Throwable {
      ClusterMBean cMBean = null;
      if (!this.ctx.isAdminServer) {
         this.ctx.throwWLSTException("You should be connected to an Administration Server to perform this action.");
         return null;
      } else {
         cMBean = this.ctx.runtimeDomainMBean.lookupCluster(name);
         if (cMBean == null) {
            this.ctx.throwWLSTException("No Cluster with name \"" + name + "\" configured in the domain");
            return null;
         } else {
            ServerMBean[] servers = cMBean.getServers();
            String[] serverNames = new String[servers.length];
            List allServers = new ArrayList();
            List activeServers = new ArrayList();
            HashMap map = new HashMap();
            this.ctx.println("\nThere are " + cMBean.getServers().length + " server(s) in cluster: " + name);
            ServerLifeCycleRuntimeMBean[] sruntimes = this.ctx.getDomainRuntimeDomainRuntimeMBean().getServerLifeCycleRuntimes();

            int i;
            for(int j = 0; j < servers.length; ++j) {
               serverNames[j] = servers[j].getName();
               allServers.add(servers[j].getName());

               for(i = 0; i < sruntimes.length; ++i) {
                  ServerLifeCycleRuntimeMBean bean = sruntimes[i];
                  String srname = bean.getName();
                  String state = "";
                  if (srname.equals(serverNames[j])) {
                     state = bean.getState();
                     map.put(serverNames[j], state);
                     activeServers.add(serverNames[j]);
                     allServers.remove(serverNames[j]);
                  }
               }
            }

            String[] stt = new String[activeServers.size()];

            for(i = 0; i < activeServers.size(); ++i) {
               stt[i] = (String)activeServers.get(i);
            }

            int k;
            if (stt.length != 0) {
               this.ctx.println("\nStates of the servers are ");
               String[] stt1 = new String[activeServers.size()];

               for(k = 0; k < activeServers.size(); ++k) {
                  stt1[k] = (String)activeServers.get(k);
                  this.ctx.println(stt1[k] + "---" + map.get(stt1[k]));
               }
            }

            if (!allServers.isEmpty()) {
               String names = "";

               for(k = 0; k < allServers.size(); ++k) {
                  names = names + "\n" + (String)allServers.get(k);
               }

               this.ctx.println("\nThe other server(s) in the cluster that are not active are: \n" + names);
            }

            return map;
         }
      }
   }

   public Object resume(String serverName, String block) throws ScriptException {
      ServerLifeCycleTaskRuntimeMBean taskBean = null;

      try {
         if (serverName == null) {
            serverName = this.ctx.serverName;
         }

         if (this.ctx.domainRuntimeServiceMBean == null) {
            if (serverName.equals(this.ctx.serverName)) {
               this.ctx.printDebug("Trying to resume the managed server you are currently connected to");
               this.ctx.getServerRuntimeServerRuntimeMBean().resume();
               return taskBean;
            }

            this.ctx.throwWLSTException("You should be connected to an admin server to resume another server.");
            return taskBean;
         }

         if (WLSTUtil.runningWLSTAsModule()) {
            block = "true";
         }

         ServerLifeCycleRuntimeMBean srBean = this.ctx.domainRuntimeServiceMBean.getDomainRuntime().lookupServerLifeCycleRuntime(serverName);
         taskBean = srBean.resume();
         if (this.ctx.getBoolean(block)) {
            if (taskBean != null && taskBean.isRunning()) {
               while(true) {
                  try {
                     Thread.sleep(1000L);
                  } catch (InterruptedException var6) {
                  }

                  if (!taskBean.isRunning()) {
                     break;
                  }

                  this.ctx.print(".");
               }
            }

            if (!taskBean.getStatus().equals("FAILED")) {
               this.ctx.println("Server " + serverName + " resumed successfully.\n");
            } else {
               this.ctx.println("Failed to resume the server " + serverName + " due to:\n" + taskBean.getError());
            }

            if (!WLSTUtil.runningWLSTAsModule()) {
               WLSTUtil.getWLSTInterpreter().set(serverName + "_resume_Task", taskBean);
            }
         } else {
            WLSTUtil.getWLSTInterpreter().set(serverName + "_resume_Task", taskBean);
            this.ctx.println("\nThe server resume task for server " + serverName + "\nis assigned to variable " + serverName + "_resume_Task");
            this.ctx.println("\nYou can call the getStatus() or isRunning() or getError()\nmethods on this variable to determine the status \nof your server resume\n");
         }
      } catch (ServerLifecycleException var7) {
         this.ctx.throwWLSTException("Serverlifecycle exception occured while resuming server " + serverName, var7);
      }

      return taskBean;
   }

   public Object suspend(String serverName, String ignoreSessions, int timeOut, String force, String block) throws ScriptException {
      ServerLifeCycleTaskRuntimeMBean taskBean = null;

      try {
         if (serverName == null) {
            serverName = this.ctx.serverName;
         }

         if (this.ctx.domainRuntimeServiceMBean == null) {
            if (serverName.equals(this.ctx.serverName)) {
               this.ctx.printDebug("Trying to suspend the managed server you are currently connected to");
               this.ctx.getServerRuntimeServerRuntimeMBean().suspend(timeOut, this.ctx.getBoolean(ignoreSessions));
               return taskBean;
            } else {
               this.ctx.throwWLSTException("You should be connected to an admin server to suspend another server.");
               return taskBean;
            }
         } else {
            if (WLSTUtil.runningWLSTAsModule()) {
               block = "true";
            }

            ServerLifeCycleRuntimeMBean srBean = this.ctx.domainRuntimeServiceMBean.getDomainRuntime().lookupServerLifeCycleRuntime(serverName);
            if (this.ctx.getBoolean(force)) {
               taskBean = srBean.forceSuspend();
            } else {
               taskBean = srBean.suspend(timeOut, this.ctx.getBoolean(ignoreSessions));
            }

            if (this.ctx.getBoolean(block)) {
               if (taskBean != null && taskBean.isRunning()) {
                  while(true) {
                     try {
                        Thread.sleep(1000L);
                     } catch (InterruptedException var9) {
                     }

                     if (!taskBean.isRunning()) {
                        break;
                     }

                     this.ctx.print(".");
                  }
               }

               if (!taskBean.getStatus().equals("FAILED")) {
                  this.ctx.println("Server " + serverName + " suspended successfully.\n");
               } else {
                  this.ctx.println("Failed to suspend the server " + serverName + " due to:\n" + taskBean.getError());
               }

               if (!WLSTUtil.runningWLSTAsModule()) {
                  WLSTUtil.getWLSTInterpreter().set(serverName + "_suspend_Task", taskBean);
               }
            } else {
               WLSTUtil.getWLSTInterpreter().set(serverName + "_suspend_Task", taskBean);
               this.ctx.println("\nThe server suspend task for server " + serverName + "\nis assigned to variable " + serverName + "_suspend_Task");
               this.ctx.println("\nYou can call the getStatus(), getError(), getDescription()\nor isRunning() methods on this variable to determine \nthe status of your server suspend\n");
            }

            return taskBean;
         }
      } catch (ServerLifecycleException var10) {
         this.ctx.throwWLSTException("Serverlifecycle exception occured while suspending server " + serverName, var10);
         return taskBean;
      }
   }

   public String startSvr(String serverName, String domainName, String url, String username, String password, String rootDirectory, String generateDefaultConfig, String overWriteDomainDir, String block, int timeout, String useNM, String serverLog, String sysProps, String jvmArgs, String spaceAsJvmArgsDelimiter) throws Throwable {
      Properties props = System.getProperties();
      if (props == null) {
         props = new Properties();
      }

      boolean usingDefaults = false;
      if (username != null && password != null && username.length() > 0 && password.length() > 0) {
         usingDefaults = true;
      }

      File domDir = new File(rootDirectory);
      if (!domDir.exists()) {
         domDir.mkdirs();
      }

      File configFile = new File(domDir.getAbsolutePath() + "/config/config.xml");
      if (!configFile.exists()) {
         props.setProperty("weblogic.Domain", domainName);
         props.setProperty("weblogic.Name", serverName);
      }

      String listenAddress = "localhost";
      String listenPort = "7001";
      String protocol = "t3";
      if (url == null) {
         url = "t3://localhost:7001";
      }

      if (!url.equals("t3://localhost:7001")) {
         listenPort = this.getListenPort(this.getURL(url));
         listenAddress = this.getListenAddress(this.getURL(url));
         protocol = this.ctx.getProtocol(url);
         if (props.getProperty("weblogic.EnableListenPortOverride") != null) {
            props.setProperty("weblogic.ListenPort", listenPort);
            props.setProperty("weblogic.ListenAddress", listenAddress);
            props.setProperty("weblogic.Protocol", protocol);
         }
      }

      if (!configFile.exists()) {
         props.setProperty("weblogic.ListenPort", listenPort);
         props.setProperty("weblogic.ListenAddress", listenAddress);
         props.setProperty("weblogic.Protocol", protocol);
      }

      File bootProps = new File(domDir.getAbsolutePath() + File.separator + "boot.properties");
      if (!bootProps.exists()) {
         bootProps = new File(domDir.getAbsolutePath() + File.separator + "servers" + File.separator + serverName + File.separator + "security" + File.separator + "boot.properties");
      }

      if (!bootProps.exists() && !configFile.exists() && !usingDefaults) {
         if (username == null || username.length() == 0) {
            username = "weblogic";
         }

         if (password == null || password.length() == 0) {
            password = "welcome1";
         }

         usingDefaults = true;
      }

      if (usingDefaults) {
         props.setProperty("weblogic.management.username", username);
         props.setProperty("weblogic.management.password", password);
      }

      String[] _props;
      if (generateDefaultConfig.equals("true")) {
         if (overWriteDomainDir.equals("true")) {
            if (domDir.exists()) {
               FileUtils.remove(domDir);
               if (username == null || username.length() == 0) {
                  username = "weblogic";
               }

               if (password == null || password.length() == 0) {
                  password = "welcome1";
               }

               props.setProperty("weblogic.management.username", username);
               props.setProperty("weblogic.management.password", password);
            }

            domDir.mkdirs();
         } else if (domDir.isDirectory() || domDir.exists()) {
            _props = domDir.list();
            if (_props.length > 0) {
               this.ctx.throwWLSTException("The root directory specified " + domDir + " is not empty, " + "if you would like to overwrite the " + "directory contents, specify overWriteDir='true'");
               return null;
            }

            props.setProperty("weblogic.RootDirectory", domDir.getAbsolutePath());
            props.setProperty("weblogic.management.GenerateDefaultConfig", generateDefaultConfig);
            if (this.ctx.nmService.isConnectedToNM() && useNM.toLowerCase(Locale.US).equals("true")) {
               this.ctx.nmService.nmStart(serverName, rootDirectory, props, (Writer)null);
               return null;
            }

            boolean spaceAsDelimiter = false;
            if ("true".equalsIgnoreCase(spaceAsJvmArgsDelimiter)) {
               spaceAsDelimiter = true;
            }

            return this.nowStartServer(props, block, timeout, serverName, serverLog, usingDefaults, jvmArgs, spaceAsDelimiter, url);
         }
      }

      props.setProperty("weblogic.RootDirectory", domDir.getAbsolutePath());
      props.setProperty("weblogic.management.GenerateDefaultConfig", generateDefaultConfig);
      if (sysProps != null) {
         _props = StringUtils.splitCompletely(sysProps);

         for(int i = 0; i < _props.length; ++i) {
            String s = _props[i];
            String[] _s = StringUtils.splitCompletely(s, "=");
            if (_s.length == 2) {
               props.setProperty(_s[0], _s[1]);
            }
         }
      }

      if (this.ctx.nmService.isConnectedToNM() && useNM.toLowerCase(Locale.US).equals("true")) {
         this.ctx.nmService.nmStart(serverName, rootDirectory, props, (Writer)null);
         return null;
      } else {
         boolean spaceAsDelimiter = false;
         if ("true".equalsIgnoreCase(spaceAsJvmArgsDelimiter)) {
            spaceAsDelimiter = true;
         }

         return this.nowStartServer(props, block, timeout, serverName, serverLog, usingDefaults, jvmArgs, spaceAsDelimiter, url);
      }
   }

   String nowStartServer(Properties props, String block, int timeout, String serverName, String serverLog, boolean usingDefaults, String jvmArgs, boolean spaceAsDelimiter, String jndiUrl) throws ScriptException {
      Process myChild = null;
      String processName = null;

      try {
         JavaExec jx = JavaExec.createCommand("weblogic.Server");
         if (jvmArgs != null) {
            String delim = spaceAsDelimiter ? " " : ",";
            if (!spaceAsDelimiter && jvmArgs.indexOf(",") == -1 && jvmArgs.indexOf(" ") != -1) {
               delim = " ";
            }

            String[] _jargs = StringUtils.splitCompletely(jvmArgs, delim);

            for(int i = 0; i < _jargs.length; ++i) {
               jx.addJvmArg(_jargs[i].trim());
            }
         }

         Enumeration e = props.keys();

         while(e.hasMoreElements()) {
            String k = (String)e.nextElement();
            if (!"sun.boot.class.path".equals(k)) {
               String v = (String)props.get(k);
               jx.addSystemProp(k, v);
            }
         }

         jx.addDefaultClassPath();
         this.ctx.printDebug("Command being used to start the server is ");
         this.ctx.printDebug(jx.getCommand());
         myChild = jx.getProcess();
         this.ctx.println("Starting weblogic server ... ");
         processName = this.calculateProcessName();
         WLSTUtil.startProcess(myChild, processName, true, serverLog);

         int attemptStatus;
         try {
            this.ctx.printDebug("Sleeping for 3 seconds for the new JVM to start");
            Thread.sleep(3000L);
            attemptStatus = myChild.exitValue();
            if (attemptStatus == 1) {
               this.ctx.printDebug("An exit value 1 reported which indicates that WLST was not able to create a new JVM.");
            }

            this.ctx.throwWLSTException("Error starting the server. WLST could not start a new JVM for the server process, this might happen if you have provided illegal jvmArgs or the JVM picked up any illegal JVM_OPTIONS from your path.Please look for the correct error in your logs and try again.");
         } catch (IllegalThreadStateException var16) {
            this.ctx.printDebug("Child process is running");
         } catch (InterruptedException var17) {
         }

         if ("true".equals(block)) {
            attemptStatus = this.attemptConnection(props, jndiUrl, timeout, serverName, usingDefaults);
            if (attemptStatus == 0) {
               this.ctx.println("Server started successfully.");
               return processName;
            } else if (attemptStatus == 1) {
               myChild.destroy();
               this.ctx.throwWLSTException("Could not start the server, the process might have timed out or there is an Error starting the server. Please refer to the log files for more details.");
               return processName;
            } else {
               this.ctx.println("Check server output to see if it started successfully.");
               return processName;
            }
         } else {
            return processName;
         }
      } catch (IOException var18) {
         this.ctx.throwWLSTException("Could not start the server.", var18);
         myChild.destroy();
         return processName;
      }
   }

   private String calculateProcessName() {
      StringBuffer buf = new StringBuffer();
      Date dte = new Date();
      buf.append("WLST-WLS-");
      buf.append(Long.toString(dte.getTime()));
      return buf.toString();
   }

   private int attemptConnection(Properties props, String jndiUrl, int timeout, String serverName, boolean usingDefaults) {
      Integer in = new Integer(timeout);
      long quitTime = System.currentTimeMillis() + in.longValue();
      String username = null;
      String pwd = null;
      File lastException;
      if (usingDefaults) {
         username = props.getProperty("weblogic.management.username");
         pwd = props.getProperty("weblogic.management.password");
      } else {
         String domainDir = props.getProperty("weblogic.RootDirectory");
         lastException = new File(domainDir + File.separator + "boot.properties");
         if (!lastException.exists()) {
            lastException = new File(domainDir + File.separator + "servers" + File.separator + serverName + File.separator + "security" + File.separator + "boot.properties");
         }

         if (lastException.exists()) {
            HashMap map = WLSTHelper.loadUsernameAndPasswordFromBootProperties(lastException, domainDir);
            if (map != null) {
               username = (String)map.get("username");
               pwd = (String)map.get("password");
            }
         }

         if (username == null || pwd == null) {
            this.ctx.println("Username or password has not been specified. Without that information, WLST can not connect");
            this.ctx.println("to the server to check for success. Instead, WLST will just block until the configured timeout of " + timeout / '\uea60' + " minutes ...");

            do {
               try {
                  this.ctx.print(".");
                  Thread.sleep(2000L);
               } catch (InterruptedException var16) {
               }
            } while(timeout == 0 || quitTime > System.currentTimeMillis());

            this.ctx.println("");
            return 2;
         }
      }

      Hashtable env = new Hashtable();
      env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      env.put("java.naming.provider.url", jndiUrl);
      if (username != null) {
         env.put("java.naming.security.principal", username);
      }

      if (pwd != null) {
         env.put("java.naming.security.credentials", pwd);
      }

      lastException = null;

      while(true) {
         try {
            new InitialContext(env);
            return 0;
         } catch (Exception var17) {
            if (!(var17 instanceof CommunicationException)) {
               System.out.println("Unexpected Exception, retrying in 2 seconds");
               var17.printStackTrace();
            }

            try {
               this.ctx.print(".");
               Thread.sleep(2000L);
            } catch (InterruptedException var15) {
            }

            if (timeout != 0 && quitTime <= System.currentTimeMillis()) {
               this.ctx.println("Could not connect to the server to verify that it has started. The error returned is: " + var17);
               return 1;
            }
         }
      }
   }

   private String getListenAddress(String url) {
      int i = url.indexOf("//");
      int j = url.lastIndexOf(":");
      String addr = url.substring(i + 2, j);
      return addr;
   }

   private String getListenPort(String url) {
      int j = url.lastIndexOf(":");
      String port = url.substring(j + 1, url.length());
      return port;
   }

   private String getURL(String url) {
      int i = url.indexOf("//");
      if (i > 0 && url.charAt(i - 1) == ':') {
         return url;
      } else {
         return i == 0 ? "t3:" + url : "t3://" + url;
      }
   }

   public boolean startServerNM(String serverName, String domainName, String domainDir, String url, String username, String password, String NMType, String NMHost, String NMPort, String NMUsername, String NMPassword) throws Throwable {
      String nmArgs = "";
      return false;
   }
}
