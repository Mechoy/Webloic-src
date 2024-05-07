package weblogic.management.commandline.tools;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.singleton.ServerMigrationCoordinator;
import weblogic.cluster.singleton.ServerMigrationException;
import weblogic.common.T3Client;
import weblogic.management.MBeanHome;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.commandline.OutputFormatter;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.management.runtime.MigrationException;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleTaskRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.StringUtils;

public final class ClusterAdminCommandLineInvoker {
   static final String OK_STRING = "Ok";
   static CommandLineArgs params = null;
   static PrintWriter printLog = null;
   static T3Client t3 = null;
   static AdminToolHelper toolHelper = null;
   MBeanHome adminHome;
   Context ctx;
   OutputFormatter out;
   private static PrintStream printStream;

   public ClusterAdminCommandLineInvoker(CommandLineArgs var1, PrintStream var2) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;

      try {
         params = var1;
         if (var2 != null) {
            printStream = var2;
         }

         toolHelper = new AdminToolHelper(var1);
         this.doCommandline();
      } catch (Exception var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            AdminToolHelper.printException(var4);
         }

         throw var4;
      }
   }

   public ClusterAdminCommandLineInvoker(String[] var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      params = new CommandLineArgs(var1);
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      toolHelper = new AdminToolHelper(params);
      this.doCommandline();
   }

   public ClusterAdminCommandLineInvoker(CommandLineArgs var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      params = var1;
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      toolHelper = new AdminToolHelper(params);
      this.doCommandline();
   }

   public ClusterAdminCommandLineInvoker(String[] var1, PrintStream var2) throws Exception {
      this(new CommandLineArgs(var1), var2);
   }

   public static void main(String[] var0) throws Exception {
      new ClusterAdminCommandLineInvoker(var0, System.out);
   }

   static void executeClusterStart(CommandLineArgs var0) throws Exception {
      Context var1 = null;
      MBeanHome var2 = null;
      ClusterMBean var3 = null;
      ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
      String var5 = var0.getClusterName();

      try {
         var1 = getInitialContext(var0);
      } catch (Exception var20) {
         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getFailedConnect(var0.getURL()) + "[" + var20 + "]");
         }

         throw var20;
      }

      var2 = AdminToolHelper.getAdminMBeanHome(var0);
      Iterator var6 = var2.getMBeansByType("ServerRuntime").iterator();
      ServerRuntimeMBean var7 = (ServerRuntimeMBean)var6.next();
      if (!var7.isAdminServer()) {
         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getAdminServerUrlRequired(var0.getURL()));
         }

         throw new Exception();
      } else {
         Set var8 = var2.getMBeansByType("Cluster");
         boolean var9 = false;
         if (var5 != null) {
            try {
               var3 = (ClusterMBean)var2.getAdminMBean(var5, "Cluster");
               var9 = true;
            } catch (Exception var19) {
               System.out.println(var4.getNoclusterDefined(var5));
               AdminToolHelper.printDone = true;
               throw var19;
            }

            if (var8.size() == 0) {
               System.out.println(var4.getNoClustersDefined());
               throw new Exception();
            } else {
               HashMap var10 = new HashMap();
               ServerMBean[] var11;
               if (var9) {
                  var11 = var3.getServers();

                  try {
                     for(int var12 = 0; var12 < var11.length; ++var12) {
                        ServerMBean var23 = var11[var12];
                        var10.put(var23.getName(), ((ServerLifeCycleRuntimeMBean)var2.getMBean(var23.getName(), "ServerLifeCycleRuntime", var2.getActiveDomain().getName())).start());

                        try {
                           Thread.currentThread();
                           Thread.sleep(1000L);
                        } catch (Exception var18) {
                        }
                     }
                  } catch (Exception var21) {
                     RuntimeException var13 = new RuntimeException(var21);
                     throw new RuntimeOperationsException(var13);
                  }
               }

               var11 = var3.getServers();
               String[] var22 = new String[var11.length];

               for(int var24 = 0; var24 < var11.length; ++var24) {
                  var22[var24] = var11[var24].getName();
               }

               System.out.println("\n" + var4.getstartingServersInCluster() + " " + var5 + ": " + StringUtils.join(var22, ","));
               boolean var25 = false;

               for(int var14 = 0; var14 < var11.length; ++var14) {
                  ServerLifeCycleTaskRuntimeMBean var15 = (ServerLifeCycleTaskRuntimeMBean)var10.get(var11[var14].getName());
                  if (var15 != null && var15.isRunning()) {
                     do {
                        Thread.sleep(1000L);
                     } while(var15.isRunning());

                     var25 = true;
                  }
               }

               if (var25) {
                  boolean var26 = true;

                  for(int var27 = 0; var27 < var11.length; ++var27) {
                     try {
                        if (!((ServerLifeCycleRuntimeMBean)var2.getMBean(var22[var27], "ServerLifeCycleRuntime", var2.getActiveDomain().getName())).getState().equals("RUNNING")) {
                           System.out.println("Unable to start Managed server " + var22[var27]);
                           var26 = false;
                        }
                     } catch (Exception var17) {
                        System.out.println("Unable to start Managed server " + var22[var27]);
                        var26 = false;
                     }
                  }

                  if (var26) {
                     System.out.println("\nAll servers in the cluster " + var5 + " are started successfully.");
                  } else {
                     System.out.println("\nUnable to start some of the servers in the cluster " + var5 + ". Please check if the Node Manager is up and running.");
                     AdminToolHelper.printDone = true;
                     throw new Exception("Cluster could not be started");
                  }
               } else {
                  System.out.println("\nNone of the servers in the cluster " + var5 + " could be started. Please check if the Node Manager is up and running.");
                  AdminToolHelper.printDone = true;
                  throw new Exception("Cluster could not be started");
               }
            }
         } else {
            System.out.println("\n" + var4.getSpecCN());
            System.out.println(CommandLineArgs.getUsageString());
            AdminToolHelper.printDone = true;
            throw new Exception();
         }
      }
   }

   static void executeClusterStop(CommandLineArgs var0) throws Exception {
      Context var1 = null;
      MBeanHome var2 = var0.getAdminHome();
      ClusterMBean var3 = null;
      ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
      String var5 = var0.getClusterName();

      try {
         var1 = getInitialContext(var0);
      } catch (Exception var17) {
         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getFailedConnect(var0.getURL()) + "[" + var17 + "]");
         }

         throw var17;
      }

      var2 = (MBeanHome)var1.lookup("weblogic.management.home.localhome");
      Iterator var6 = var2.getMBeansByType("ServerRuntime").iterator();
      ServerRuntimeMBean var7 = (ServerRuntimeMBean)var6.next();
      if (!var7.isAdminServer()) {
         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getAdminServerUrlRequired(var0.getURL()));
         }

         throw new Exception();
      } else {
         Set var8 = var2.getMBeansByType("Cluster");
         boolean var9 = false;
         if (var5 != null) {
            try {
               var3 = (ClusterMBean)var2.getAdminMBean(var5, "Cluster");
               var9 = true;
            } catch (Exception var16) {
               System.out.println(var4.getNoclusterDefined(var5));
               AdminToolHelper.printDone = true;
               throw var16;
            }

            if (var8.size() == 0) {
               System.out.println(var4.getNoClustersDefined());
               throw new Exception();
            } else {
               HashMap var10 = new HashMap();
               ServerMBean[] var11;
               if (var9) {
                  var11 = var3.getServers();

                  try {
                     for(int var12 = 0; var12 < var11.length; ++var12) {
                        ServerMBean var20 = var11[var12];
                        var10.put(var20.getName(), ((ServerLifeCycleRuntimeMBean)var2.getMBean(var20.getName(), "ServerLifeCycleRuntime", var2.getActiveDomain().getName())).forceShutdown());

                        try {
                           Thread.currentThread();
                           Thread.sleep(1000L);
                        } catch (Exception var15) {
                        }
                     }
                  } catch (Exception var18) {
                     RuntimeException var13 = new RuntimeException(var18);
                     throw new RuntimeOperationsException(var13);
                  }
               }

               var11 = var3.getServers();
               String[] var19 = new String[var11.length];

               int var21;
               for(var21 = 0; var21 < var11.length; ++var21) {
                  var19[var21] = var11[var21].getName();
               }

               System.out.println("\n" + var4.getSDC() + " " + var5 + ": " + StringUtils.join(var19, ","));

               for(var21 = 0; var21 < var11.length; ++var21) {
                  ServerLifeCycleTaskRuntimeMBean var14 = (ServerLifeCycleTaskRuntimeMBean)var10.get(var11[var21].getName());
                  if (var14 != null && var14.isRunning()) {
                     do {
                        Thread.sleep(1000L);
                     } while(var14.isRunning());
                  }
               }

               System.out.println("\n" + var4.getSDCS(var5));
            }
         } else {
            System.out.println("\n" + var4.getspecCNS());
            System.out.println(CommandLineArgs.getUsageString());
            AdminToolHelper.printDone = true;
            throw new Exception();
         }
      }
   }

   static void executeClusterPing(CommandLineArgs var0) throws Exception {
      Context var1 = null;
      MBeanHome var2 = null;
      ClusterMBean var3 = null;
      ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
      String var5 = var0.getClusterName();

      try {
         var1 = getInitialContext(var0);
         var2 = (MBeanHome)var1.lookup("weblogic.management.adminhome");
      } catch (Exception var9) {
         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getFailedConnect(var0.getURL()) + "[" + var9 + "]");
         }

         throw var9;
      }

      if (var5 != null) {
         try {
            var3 = (ClusterMBean)var2.getAdminMBean(var5, "Cluster");
         } catch (Exception var8) {
            System.out.println(var4.getNoclusterDefined(var5));
            AdminToolHelper.printDone = true;
            throw var8;
         }
      }

      try {
         if (var3 != null) {
            getClusterInfo(var2, var3);
         } else {
            Set var6 = var2.getMBeansByType("Cluster");
            Iterator var7 = var6.iterator();
            if (var6.size() == 0) {
               System.out.println(var4.getNoClustersDefined());
            } else {
               while(var7.hasNext()) {
                  var3 = (ClusterMBean)var7.next();
                  getClusterInfo(var2, var3);
               }
            }
         }

      } catch (RemoteRuntimeException var10) {
         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getFailedConnect(var0.getURL()) + "[" + var10 + "]");
         }

         throw var10;
      }
   }

   static void getClusterInfo(MBeanHome var0, ClusterMBean var1) {
      ManagementTextTextFormatter var2 = new ManagementTextTextFormatter();
      String var3 = var1.getName();
      ServerMBean[] var4 = var1.getServers();
      String[] var5 = new String[var4.length];
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      HashMap var8 = new HashMap();
      System.out.println("\nThere are " + var1.getServers().length + " server(s) in cluster: " + var3);
      Set var9 = var0.getMBeansByType("ServerLifeCycleRuntime");

      for(int var10 = 0; var10 < var4.length; ++var10) {
         var5[var10] = var4[var10].getName();
         var6.add(var4[var10].getName());
         Iterator var11 = var9.iterator();

         while(var11.hasNext()) {
            ServerLifeCycleRuntimeMBean var12 = (ServerLifeCycleRuntimeMBean)var11.next();
            String var13 = var12.getName();
            String var14 = "";
            if (var13.equals(var5[var10])) {
               var14 = var12.getState();
               var8.put(var5[var10], var14);
               var7.add(var5[var10]);
               var6.remove(var5[var10]);
            }
         }
      }

      String[] var15 = new String[var7.size()];

      for(int var16 = 0; var16 < var7.size(); ++var16) {
         var15[var16] = (String)var7.get(var16);
      }

      int var18;
      if (var15.length != 0) {
         System.out.println("\n" + var2.getaliveServersStates());
         String[] var17 = new String[var7.size()];

         for(var18 = 0; var18 < var7.size(); ++var18) {
            var17[var18] = (String)var7.get(var18);
            System.out.println(var17[var18] + "---" + var8.get(var17[var18]));
         }
      }

      if (!var6.isEmpty()) {
         String var19 = "";

         for(var18 = 0; var18 < var6.size(); ++var18) {
            var19 = var19 + "\n" + (String)var6.get(var18);
         }

         System.out.println("\nThe other server(s) in the cluster that are not active are: \n" + var19);
      }

   }

   private void doMigrate() throws Exception {
      ManagementTextTextFormatter var1 = ManagementTextTextFormatter.getInstance();

      try {
         MBeanServerConnection var2 = this.getRuntimeMBeanServerConnection(params);
         MigratableServiceCoordinatorRuntimeMBean var3 = null;
         DomainRuntimeServiceMBean var4 = null;
         DomainMBean var5 = null;

         try {
            var4 = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var2, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));
            var3 = var4.getDomainRuntime().getMigratableServiceCoordinatorRuntime();
            var5 = var4.getDomainConfiguration();
         } catch (Throwable var14) {
         }

         if (var4 == null) {
            throw new AssertionError("Failed to locate domain runtime service");
         } else if (var3 == null) {
            throw new AssertionError("coordinator must not be null");
         } else {
            Object var6 = null;
            SingletonServiceMBean var7 = null;
            String var8 = params.getMigratableTargetName();
            String var9 = params.getSingletonServiceName();
            if (var8 != null) {
               if (params.getMigrateJTA()) {
                  ServerMBean[] var10 = var5.getServers();

                  for(int var11 = 0; var11 < var10.length; ++var11) {
                     if (var10[var11].getName().equals(var8)) {
                        var6 = var10[var11].getJTAMigratableTarget();
                     }
                  }
               } else {
                  var6 = getMigratableTarget(var5, var8);
               }

               var7 = getSingletonService(var5, var8);
            } else if (var9 != null) {
               var7 = getSingletonService(var5, var9);
            }

            ServerMBean var17 = getServer(var5, params.getDestinationServerName());
            if (var6 == null && var7 == null && !params.showNoMessages() && !AdminToolHelper.printDone) {
               printStream.println(var1.getMigrationUnknownMigratableTarget(params.getMigratableTargetName()));
               AdminToolHelper.printDone = true;
            } else if (var17 == null && !params.showNoMessages() && !AdminToolHelper.printDone) {
               printStream.println(var1.getMigrationUnknownDestinationServer(params.getDestinationServerName()));
               AdminToolHelper.printDone = true;
            } else {
               boolean var18 = false;
               String var12 = null;
               int var13;
               if (var6 != null) {
                  for(var13 = 0; var13 < ((MigratableTargetMBean)var6).getAllCandidateServers().length && !var18; ++var13) {
                     var18 = ((MigratableTargetMBean)var6).getAllCandidateServers()[var13].getName().equals(var17.getName());
                  }

                  var12 = ((MigratableTargetMBean)var6).getName();
               } else {
                  for(var13 = 0; var13 < var7.getAllCandidateServers().length && !var18; ++var13) {
                     var18 = var7.getAllCandidateServers()[var13].getName().equals(var17.getName());
                  }

                  var12 = var7.getName();
               }

               if (!var18) {
                  if (var6 != null && ((MigratableTargetMBean)var6).getConstrainedCandidateServers().length > 0) {
                     if (!params.showNoMessages()) {
                        printStream.println(var1.getMigrationErrorDestinationNotAmongCandidateServers(params.getDestinationServerName(), params.getMigratableTargetName()));
                     }
                  } else if (!params.showNoMessages()) {
                     printStream.println(var1.getMigrationErrorDestinationNotAmongClusterMembers(params.getDestinationServerName(), params.getMigratableTargetName()));
                  }
               } else {
                  try {
                     if (!params.showNoMessages()) {
                        printStream.println(var1.getMigrationStarted(params.getMigrateJTA() ? var1.getMigrationJTAPrefix() : "", var12, params.getDestinationServerName()));
                     }

                     if (var7 != null) {
                        var3.migrateSingleton(var7, var17);
                     } else if (params.getMigrateJTA()) {
                        var3.migrateJTA((MigratableTargetMBean)var6, var17, !params.getSourceDown(), !params.getDestinationDown());
                     } else {
                        var3.migrate((MigratableTargetMBean)var6, var17, !params.getSourceDown(), !params.getDestinationDown());
                     }

                     if (!params.showNoMessages()) {
                        printStream.println(var1.getMigrationSucceeded(params.getMigrateJTA() ? var1.getMigrationJTAPrefix() : ""));
                     }

                     if (!params.showNoMessages()) {
                        this.out.println("Ok");
                     }
                  } catch (MigrationException var15) {
                     if (!AdminToolHelper.printDone) {
                        printStream.println(var1.getMigrationFailed(params.getMigrateJTA() ? var1.getMigrationJTAPrefix() : "", var15.getMessage()));
                        AdminToolHelper.printDone = true;
                     }

                     throw var15;
                  }
               }

            }
         }
      } catch (Exception var16) {
         if (!params.showNoMessages() && !AdminToolHelper.printDone) {
            printStream.println("Internal problem: " + var16);
            printStream.println(StackTraceUtils.throwable2StackTrace(var16));
            AdminToolHelper.printDone = true;
         }

         throw var16;
      }
   }

   private void doMigrateAll() throws Exception {
      ManagementTextTextFormatter var1 = ManagementTextTextFormatter.getInstance();
      MBeanServerConnection var2 = this.getRuntimeMBeanServerConnection(params);
      MigratableServiceCoordinatorRuntimeMBean var3 = null;
      DomainRuntimeServiceMBean var4 = null;
      DomainMBean var5 = null;

      try {
         var4 = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var2, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));
         var3 = var4.getDomainRuntime().getMigratableServiceCoordinatorRuntime();
         var5 = var4.getDomainConfiguration();
      } catch (Throwable var15) {
      }

      if (var3 == null) {
         throw new AssertionError("coordinator must not be null");
      } else {
         ServerMBean var6 = var5.lookupServer(params.getSourceServerName());
         if (var6 == null && !params.showNoMessages() && !AdminToolHelper.printDone) {
            printStream.println("Couldn't locate '" + params.getSourceServerName() + "' server in the domain");
            AdminToolHelper.printDone = true;
         } else if (var6.getCluster() == null && !params.showNoMessages() && !AdminToolHelper.printDone) {
            printStream.println("Server '" + params.getSourceServerName() + "' does not belong to a cluster. To migrate servers should" + " be part of a cluster");
            AdminToolHelper.printDone = true;
         } else {
            String var7 = params.getDestinationServerName();
            ServerMBean var8 = getServer(var5, var7);
            if (var8 == null && !params.showNoMessages() && !AdminToolHelper.printDone) {
               printStream.println(var1.getMigrationUnknownDestinationServer(params.getDestinationServerName()));
               AdminToolHelper.printDone = true;
            } else {
               JTAMigratableTargetMBean var9 = var6.getJTAMigratableTarget();
               if (this.verifyServiceMigration(var9, var8, var1)) {
                  MigratableTargetMBean[] var10 = var5.getMigratableTargets();
                  ArrayList var11 = new ArrayList();
                  String var12 = params.getSourceServerName();

                  int var13;
                  for(var13 = 0; var13 < var10.length; ++var13) {
                     if (var10[var13].getUserPreferredServer().getName().equals(var12)) {
                        if (!this.verifyServiceMigration(var10[var13], var8, var1)) {
                           return;
                        }

                        var11.add(var10[var13]);
                     }
                  }

                  var13 = var11.size();

                  try {
                     var3.migrateJTA(var9, var8, !params.getSourceDown(), !params.getDestinationDown());

                     for(int var14 = 0; var14 < var13; ++var14) {
                        var3.migrate((MigratableTargetMBean)var11.get(var14), var8, !params.getSourceDown(), !params.getDestinationDown());
                     }

                  } catch (MigrationException var16) {
                     if (!AdminToolHelper.printDone) {
                        printStream.println(var1.getMigrationFailed(params.getMigrateJTA() ? var1.getMigrationJTAPrefix() : "", var16.getMessage()));
                        AdminToolHelper.printDone = true;
                     }

                     throw var16;
                  }
               }
            }
         }
      }
   }

   private boolean verifyServiceMigration(MigratableTargetMBean var1, ServerMBean var2, ManagementTextTextFormatter var3) {
      boolean var4 = false;

      for(int var5 = 0; var5 < var1.getAllCandidateServers().length && !var4; ++var5) {
         var4 = var1.getAllCandidateServers()[var5].getName().equals(var2.getName());
      }

      if (!var4) {
         if (var1.getConstrainedCandidateServers().length > 0) {
            if (!params.showNoMessages()) {
               printStream.println(var3.getMigrationErrorDestinationNotAmongCandidateServers(params.getDestinationServerName(), var1.getName()));
            }
         } else if (!params.showNoMessages()) {
            printStream.println(var3.getMigrationErrorDestinationNotAmongClusterMembers(params.getDestinationServerName(), var1.getName()));
         }
      }

      return true;
   }

   private static MigratableTargetMBean getMigratableTarget(DomainMBean var0, String var1) {
      MigratableTargetMBean[] var2 = var0.getMigratableTargets();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getName().equals(var1)) {
               return var2[var3];
            }
         }
      }

      return null;
   }

   private static SingletonServiceMBean getSingletonService(DomainMBean var0, String var1) {
      SingletonServiceMBean[] var2 = var0.getSingletonServices();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getName().equals(var1)) {
               return var2[var3];
            }
         }
      }

      return null;
   }

   private static ServerMBean getServer(DomainMBean var0, String var1) {
      ServerMBean[] var2 = var0.getServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   static void executeValidateCluster(CommandLineArgs var0) throws Exception {
      String var1 = var0.getConfigPath();
      if (var1 == null) {
         System.out.println("Please specify a valid path.");
         System.out.println(CommandLineArgs.getUsageString());
         throw new Exception();
      } else {
         ClusterValidatorInvoker var2 = new ClusterValidatorInvoker();
         String[] var3 = new String[]{var1};
         ClusterValidatorInvoker.main(var3);
      }
   }

   void doOperation() throws Exception {
      String var1 = "";
      switch (params.getOperation()) {
         case 37:
            executeClusterPing(params);
         case 38:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 51:
         default:
            break;
         case 39:
            executeClusterStart(params);
            break;
         case 40:
            executeClusterStop(params);
            break;
         case 41:
            executeValidateCluster(params);
            break;
         case 50:
            this.doMigrate();
            break;
         case 52:
            this.doManualMigration(params);
            break;
         case 53:
            this.doMigrateAll();
      }

      System.out.println(var1);
   }

   private void doCommandline() throws Exception {
      ManagementTextTextFormatter var10;
      try {
         if (params.getOperation() != 41) {
            if (this.adminHome == null) {
               if (params.getAdminUrl() != null) {
                  params.setUrl(params.getAdminUrl());
                  this.adminHome = AdminToolHelper.getAdminMBeanHome(params);
               } else {
                  this.adminHome = AdminToolHelper.getMBeanHome(params);
               }
            } else {
               this.adminHome = AdminToolHelper.getMBeanHome(params);
            }
         }

         this.out = new OutputFormatter(printStream, params.isPretty());
         this.doOperation();
      } catch (IllegalArgumentException var3) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var3);
            AdminToolHelper.printDone = true;
         }

         throw var3;
      } catch (MalformedObjectNameException var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var4, true);
            AdminToolHelper.printDone = true;
         }

         printStream.println("Usage:\n" + CommandLineArgs.getUsageString());
         throw var4;
      } catch (InstanceNotFoundException var5) {
         var10 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            printStream.println(var10.getCouldNotFindInstance(params.getMBeanObjName()));
            AdminToolHelper.printDone = true;
         }

         throw var5;
      } catch (RemoteRuntimeException var6) {
         Throwable var11 = var6.getNestedException();
         if (var11 instanceof PeerGoneException) {
            System.out.println("Lost connectivity to the adminstration server");
            return;
         }

         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var6, true);
            AdminToolHelper.printDone = true;
         }

         throw var6;
      } catch (ConnectException var7) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException("java.net.ConnectException", var7);
            AdminToolHelper.printDone = true;
         }

         throw var7;
      } catch (IOException var8) {
         var10 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var10.getErrorWriting(), var8);
            AdminToolHelper.printDone = true;
         }

         throw var8;
      } catch (Exception var9) {
         if (!(var9 instanceof RuntimeOperationsException)) {
            if (var9 instanceof ClassCastException) {
               System.out.println("Lost connectivity to the adminstration server..");
               return;
            }

            if (!params.showNoMessages() && !AdminToolHelper.printDone) {
               AdminToolHelper.printException(var9);
               AdminToolHelper.printDone = true;
            }

            throw var9;
         }

         RuntimeOperationsException var2 = (RuntimeOperationsException)var9;
         if (var2.getTargetException() instanceof RemoteRuntimeException) {
            System.out.println("Lost connectivity to the adminstration server.");
         }
      }

   }

   private static Context getInitialContext(CommandLineArgs var0) throws Exception {
      return AdminToolHelper.getInitialContext(var0);
   }

   private void doManualMigration(CommandLineArgs var1) throws Exception {
      String var2 = var1.getMigratableServerName();
      String var3 = var1.getDestinationMachineName();
      DomainMBean var4 = null;
      Object var5 = null;
      Context var6 = null;
      ServerMigrationCoordinator var7 = null;

      ManagementTextTextFormatter var9;
      label140: {
         try {
            MBeanServerConnection var8 = this.getRuntimeMBeanServerConnection(var1);
            DomainRuntimeServiceMBean var25 = (DomainRuntimeServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var8, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));
            var4 = var25.getDomainConfiguration();
            var6 = getInitialContext(var1);
            var7 = (ServerMigrationCoordinator)var6.lookup("weblogic/cluster/singleton/ServerMigrationCoordinator");
            break label140;
         } catch (NamingException var21) {
            var9 = ManagementTextTextFormatter.getInstance();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var9.getFailedConnect(var1.getURL()) + "[" + var21 + "]");
            }

            throw var21;
         } catch (Throwable var22) {
            var22.printStackTrace();
         } finally {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (NamingException var19) {
               }
            }

         }

         return;
      }

      ServerMBean var24 = var4.lookupServer(var2);
      if (var24 == null || !var24.isAutoMigrationEnabled()) {
         var9 = ManagementTextTextFormatter.getInstance();
         if (!var1.showNoMessages()) {
            printStream.println(var9.IncorrectMigratableServerName(var2));
            return;
         }
      }

      if (var24.getCluster() == null && !var1.showNoMessages()) {
         var9 = ManagementTextTextFormatter.getInstance();
         printStream.println(var9.MigratableServerIsNotInCluster(var2));
      } else {
         MachineMBean var26 = var4.lookupMachine(var3);
         if (var26 == null && !var1.showNoMessages()) {
            ManagementTextTextFormatter var10 = ManagementTextTextFormatter.getInstance();
            printStream.println(var10.IncorrectDestinationMachine(var3));
         } else {
            try {
               var7.migrate(var24.getName(), var24.getMachine().getName(), var26.getName(), var1.getSourceDown(), var1.getDestinationDown());
            } catch (ServerMigrationException var20) {
               this.migrationFailed(var24.getMachine().getName(), var3, var2, var20);
               return;
            }

            this.migrationSucceeded(var2);
         }
      }
   }

   private void migrationSucceeded(String var1) {
      ManagementTextTextFormatter var2 = ManagementTextTextFormatter.getInstance();
      printStream.println(var2.getMigrationSucceeded(var1));
   }

   private void migrationFailed(String var1, String var2, String var3, ServerMigrationException var4) {
      ManagementTextTextFormatter var5 = ManagementTextTextFormatter.getInstance();
      switch (var4.getStatus()) {
         case -2:
            printStream.println(var5.getMigrationInProgress(var3));
            break;
         case -1:
         case 0:
         default:
            printStream.println(var5.getMigrationFailed(var3, var4.toString()));
            break;
         case 1:
            printStream.println(var5.getSourceMachineDown(var1, var3));
            break;
         case 2:
            printStream.println(var5.getDestinationMachineDown(var2));
      }

   }

   private MBeanServerConnection getRuntimeMBeanServerConnection(CommandLineArgs var1) throws IOException {
      String var2 = var1.getUsername();
      String var3 = var1.getPassword();
      JMXServiceURL var4 = new JMXServiceURL("service:jmx:" + var1.getURL() + "/jndi/" + "weblogic.management.mbeanservers.domainruntime");
      HashMap var5 = new HashMap();
      var5.put("java.naming.security.principal", var2);
      var5.put("java.naming.security.credentials", var3);
      var5.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
      JMXConnector var6 = JMXConnectorFactory.connect(var4, var5);
      return var6.getMBeanServerConnection();
   }

   private ConfigurationManagerMBean lookupMBeanServerConnection(CommandLineArgs var1) throws Throwable {
      String var2 = var1.getUsername();
      String var3 = var1.getPassword();
      JMXServiceURL var4 = new JMXServiceURL("service:jmx:" + var1.getURL() + "/jndi/" + "weblogic.management.mbeanservers.edit");
      HashMap var5 = new HashMap();
      var5.put("java.naming.security.principal", var2);
      var5.put("java.naming.security.credentials", var3);
      var5.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
      JMXConnector var6 = JMXConnectorFactory.connect(var4, var5);
      ObjectName var7 = new ObjectName(EditServiceMBean.OBJECT_NAME);
      EditServiceMBean var8 = (EditServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var6.getMBeanServerConnection(), var7);
      return var8.getConfigurationManager();
   }

   static {
      printStream = System.out;
   }
}
