package weblogic.management.commandline.tools;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.RuntimeOperationsException;
import javax.naming.Context;
import weblogic.common.ResourceException;
import weblogic.common.T3ExecuteException;
import weblogic.management.MBeanHome;
import weblogic.management.WebLogicMBean;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.commandline.OutputFormatter;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.utils.TypeConversionUtils;

public final class JDBCCommandLineInvoker {
   static final String OK_STRING = "Ok";
   private static final boolean debug = false;
   static CommandLineArgs params = null;
   MBeanHome adminHome;
   Context ctx;
   Set matchedMBeans;
   OutputFormatter out;
   private static boolean CONTINUE = true;
   private static PrintStream printStream;
   private boolean EXIT;
   private boolean batchMode;

   public JDBCCommandLineInvoker(CommandLineArgs var1, PrintStream var2) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.matchedMBeans = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;

      try {
         params = var1;
         if (var2 != null) {
            printStream = var2;
         }

         this.doCommandline();
      } catch (Exception var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            AdminToolHelper.printException(var4);
         }

         throw var4;
      }
   }

   public JDBCCommandLineInvoker(String[] var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.matchedMBeans = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;
      params = new CommandLineArgs(var1);
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      this.doCommandline();
   }

   public JDBCCommandLineInvoker(CommandLineArgs var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.matchedMBeans = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;
      params = var1;
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      this.doCommandline();
   }

   public JDBCCommandLineInvoker(String[] var1, PrintStream var2) throws Exception {
      this(new CommandLineArgs(var1), var2);
   }

   public static void main(String[] var0) throws Exception {
      new JDBCCommandLineInvoker(var0, System.out);
   }

   static void executeResetPoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var2.nextArg("", 0);

      try {
         Context var3 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var11 = (MBeanHome)var3.lookup("weblogic.management.home.localhome");
         boolean var5 = false;
         Set var6 = var11.getMBeansByType("JDBCConnectionPoolRuntime");
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            JDBCConnectionPoolRuntimeMBean var8 = (JDBCConnectionPoolRuntimeMBean)var7.next();
            if (var1.equals(var8.getName())) {
               var5 = true;
               var8.reset();
               ManagementTextTextFormatter var9 = new ManagementTextTextFormatter();
               System.out.println(var9.getResetSuccess(var1));
            }
         }

         if (!var5) {
            ManagementTextTextFormatter var12 = new ManagementTextTextFormatter();
            throw new Exception(var12.getNoSuchPool(var1));
         } else {
            System.out.println("Ok");
         }
      } catch (T3ExecuteException var10) {
         ManagementTextTextFormatter var4;
         if (var10.getNestedException().toString().indexOf("SecurityException") != -1) {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getResetPoolSecException(var1));
            }

            throw var10;
         } else {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getFailResetPool(var1) + var10.getNestedException().toString());
            }

            throw var10;
         }
      }
   }

   static void executeEnablePoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var2.nextArg("", 0);

      try {
         Context var3 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var10 = (MBeanHome)var3.lookup("weblogic.management.home.localhome");
         boolean var5 = false;
         Set var6 = var10.getMBeansByType("JDBCConnectionPoolRuntime");
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            JDBCConnectionPoolRuntimeMBean var8 = (JDBCConnectionPoolRuntimeMBean)var7.next();
            if (var1.equals(var8.getName())) {
               var5 = true;
               var8.enable();
            }
         }

         if (!var5) {
            ManagementTextTextFormatter var11 = new ManagementTextTextFormatter();
            throw new Exception(var11.getNoSuchPool(var1));
         }
      } catch (T3ExecuteException var9) {
         ManagementTextTextFormatter var4;
         if (var9.getNestedException().toString().indexOf("SecurityException") != -1) {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getEnablePoolSecException(var1));
            }

            throw var9;
         }

         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var4.getFailEnablePool(var1) + var9.getNestedException().toString());
         }
      }

   }

   static void executeResumePoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var0.getPoolName();

      try {
         Context var3 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var10 = (MBeanHome)var3.lookup("weblogic.management.home.localhome");
         boolean var5 = false;
         Set var6 = var10.getMBeansByType("JDBCConnectionPoolRuntime");
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            JDBCConnectionPoolRuntimeMBean var8 = (JDBCConnectionPoolRuntimeMBean)var7.next();
            if (var1.equals(var8.getName())) {
               var5 = true;
               var8.resume();
            }
         }

         if (!var5) {
            ManagementTextTextFormatter var11 = new ManagementTextTextFormatter();
            throw new Exception(var11.getNoSuchPool(var1));
         }

         System.out.println("\nConnection pool " + var1 + " is resumed successfully.");
      } catch (T3ExecuteException var9) {
         ManagementTextTextFormatter var4;
         if (var9.getNestedException().toString().indexOf("SecurityException") != -1) {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getEnablePoolSecException(var1));
            }

            throw var9;
         }

         var4 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var4.getFailEnablePool(var1) + var9.getNestedException().toString());
         }
      }

   }

   static void executeDisablePoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      boolean var2 = false;
      AdminToolHelper var3 = new AdminToolHelper(var0);
      var1 = var3.nextArg("", 0);
      var2 = var3.nextArg(true, 1);

      try {
         Context var4 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var11 = (MBeanHome)var4.lookup("weblogic.management.home.localhome");
         boolean var6 = false;
         Set var7 = var11.getMBeansByType("JDBCConnectionPoolRuntime");
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            JDBCConnectionPoolRuntimeMBean var9 = (JDBCConnectionPoolRuntimeMBean)var8.next();
            if (var1.equals(var9.getName())) {
               var6 = true;
               if (var2) {
                  var9.disableDroppingUsers();
               } else {
                  var9.disableFreezingUsers();
               }
            }
         }

         if (!var6) {
            ManagementTextTextFormatter var12 = new ManagementTextTextFormatter();
            throw new Exception(var12.getNoSuchPool(var1));
         }
      } catch (T3ExecuteException var10) {
         ManagementTextTextFormatter var5;
         if (var10.getNestedException().toString().indexOf("SecurityException") != -1) {
            var5 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var5.getDisablePoolSecException(var1));
            }

            throw var10;
         } else {
            var5 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var5.getFailDisablePool(var1) + var10.getNestedException().toString());
            }

            throw var10;
         }
      }
   }

   static void executeSuspendPoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var0.getPoolName();

      try {
         Context var3 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var10 = (MBeanHome)var3.lookup("weblogic.management.home.localhome");
         boolean var5 = false;
         Set var6 = var10.getMBeansByType("JDBCConnectionPoolRuntime");
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            JDBCConnectionPoolRuntimeMBean var8 = (JDBCConnectionPoolRuntimeMBean)var7.next();
            if (var1.equals(var8.getName())) {
               var5 = true;
               var8.suspend();
            }
         }

         if (!var5) {
            ManagementTextTextFormatter var11 = new ManagementTextTextFormatter();
            throw new Exception(var11.getNoSuchPool(var1));
         } else {
            System.out.println("\nConnection pool " + var1 + " is suspended successfully.");
         }
      } catch (T3ExecuteException var9) {
         ManagementTextTextFormatter var4;
         if (var9.getNestedException().toString().indexOf("SecurityException") != -1) {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getDisablePoolSecException(var1));
            }

            throw var9;
         } else {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getFailDisablePool(var1) + var9.getNestedException().toString());
            }

            throw var9;
         }
      }
   }

   static void executeShutdownPoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var0.getPoolName();

      try {
         Context var3 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var10 = (MBeanHome)var3.lookup("weblogic.management.home.localhome");
         boolean var5 = false;
         Set var6 = var10.getMBeansByType("JDBCConnectionPoolRuntime");
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            JDBCConnectionPoolRuntimeMBean var8 = (JDBCConnectionPoolRuntimeMBean)var7.next();
            if (var1.equals(var8.getName())) {
               var5 = true;
               var8.shutdown();
            }
         }

         if (!var5) {
            ManagementTextTextFormatter var11 = new ManagementTextTextFormatter();
            throw new Exception(var11.getNoSuchPool(var1));
         } else {
            System.out.println("\nConnection pool " + var1 + " is shutdown successfully.");
         }
      } catch (T3ExecuteException var9) {
         ManagementTextTextFormatter var4;
         if (var9.getNestedException().toString().indexOf("SecurityException") != -1) {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getDisablePoolSecException(var1));
            }

            throw var9;
         } else {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getFailDisablePool(var1) + var9.getNestedException().toString());
            }

            throw var9;
         }
      }
   }

   static void executeExistsPoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var2.nextArg("", 0);

      try {
         Context var3 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var10 = (MBeanHome)var3.lookup("weblogic.management.adminhome");
         boolean var5 = false;
         Set var6 = var10.getMBeansByType("JDBCConnectionPool");
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            JDBCConnectionPoolMBean var8 = (JDBCConnectionPoolMBean)var7.next();
            if (var8.getName().equals(var1)) {
               var5 = true;
            }
         }

         ManagementTextTextFormatter var11;
         if (var5) {
            var11 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var11.getPoolExists(var1));
            }

         } else {
            var11 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var11.getPoolNotExists(var1));
            }

            throw new Exception(var11.getNoSuchPool(var1));
         }
      } catch (T3ExecuteException var9) {
         ManagementTextTextFormatter var4;
         if (var9.getNestedException().toString().indexOf("SecurityException") != -1) {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getCheckExistSecException(var1));
            }

            throw var9;
         } else {
            var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var4.getFailCheckExists(var1) + var9.getNestedException().toString());
            }

            throw var9;
         }
      }
   }

   static void executeTestPoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      JDBCConnectionPoolRuntimeMBean var3 = null;
      var1 = var2.nextArg("", 0);
      String var4 = "";

      try {
         Context var5 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var12 = (MBeanHome)var5.lookup("weblogic.management.home.localhome");
         boolean var7 = false;
         Set var8 = var12.getMBeansByType("JDBCConnectionPoolRuntime");
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            var3 = (JDBCConnectionPoolRuntimeMBean)var9.next();
            if (var1.equals(var3.getName())) {
               var7 = true;
               break;
            }
         }

         ManagementTextTextFormatter var10;
         if (var7) {
            var10 = new ManagementTextTextFormatter();
            var4 = var3.testPool();
            if (var4 == null) {
               System.out.println("\n" + var10.getjdbcConTestSuc(var1));
            } else {
               System.out.println("\n" + var10.getjdbcTestUnsuc(var1) + " : " + var4);
               AdminToolHelper.printDone = true;
               throw new Exception(var10.getjdbcTestUnsuc(var1));
            }
         } else {
            var10 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var10.getPoolNotExists(var1));
            }

            throw new Exception(var10.getNoSuchPool(var1));
         }
      } catch (T3ExecuteException var11) {
         ManagementTextTextFormatter var6;
         if (var11.getNestedException().toString().indexOf("SecurityException") != -1) {
            var6 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var6.getCheckExistSecException(var1));
            }

            throw var11;
         } else {
            var6 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var6.getFailCheckExists(var1) + var11.getNestedException().toString());
            }

            throw var11;
         }
      }
   }

   static void executeCreatePoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var2.nextArg("", 0);
      String var3 = var2.nextArg("", 1);
      String var4 = "poolName";
      String var5 = "aclName";
      String var6 = "props";
      String var7 = "maxCapacity";
      String var8 = "initialCapacity";
      String var9 = "capacityIncrement";
      String var10 = "allowShrinking";
      String var11 = "shrinkPeriodMins";
      String var12 = "driver";
      String var13 = "url";
      String var14 = "refreshPeriod";
      String var15 = "testConnsOnReserve";
      String var16 = "testConnsOnRelease";
      String var17 = "testTableName";
      String var18 = "loginDelaySecs";
      String var19 = "secondsToTrustAnIdlePoolConnection";
      String var20 = "password";
      ManagementTextTextFormatter var21 = new ManagementTextTextFormatter();
      if (var3.equals("")) {
         ManagementTextTextFormatter var53 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var53.getCRPool());
         }

         throw new Exception();
      } else {
         try {
            Context var22 = AdminToolHelper.getInitialContext(var0);
            MBeanHome var54 = (MBeanHome)var22.lookup("weblogic.management.adminhome");
            Iterator var24 = var54.getMBeansByType("ServerRuntime").iterator();
            String var25 = null;

            while(var24.hasNext()) {
               ServerRuntimeMBean var26 = (ServerRuntimeMBean)var24.next();
               if (var26.isAdminServer()) {
                  var25 = var26.getName();
                  break;
               }
            }

            if (var25 == null) {
               throw new Exception("Could not find the admin server for creating connection pool" + var1);
            } else {
               ServerMBean var55 = (ServerMBean)var54.getAdminMBean(var25, "Server");
               boolean var27 = false;
               Set var28 = var54.getMBeansByType("JDBCConnectionPoolRuntime");
               Iterator var29 = var28.iterator();

               while(var29.hasNext()) {
                  JDBCConnectionPoolRuntimeMBean var30 = (JDBCConnectionPoolRuntimeMBean)var29.next();
                  if (var1.equals(var30.getName())) {
                     var27 = true;
                  }
               }

               if (var27) {
                  ManagementTextTextFormatter var57 = new ManagementTextTextFormatter();
                  throw new Exception(var57.getPoolExists(var1));
               } else {
                  Properties var56 = new Properties();
                  TypeConversionUtils.stringToDictionary(var3, var56);
                  if (var1 != null && !var1.equals("")) {
                     String var31 = var56.getProperty(var5);
                     String var32 = null;
                     if ((var32 = var56.getProperty(var6)) == null) {
                        throw new ResourceException(var21.getmissStartupValue(var6, var1));
                     } else {
                        Properties var33 = new Properties();
                        TypeConversionUtils.stringToDictionary(var32, var33, ";");
                        int var34 = 1;
                        if (var56.getProperty(var7) != null) {
                           var34 = Integer.parseInt(var56.getProperty(var7));
                           if (var34 < 1) {
                              throw new ResourceException(var21.getPoolLessThanOne(var1));
                           }
                        }

                        int var35 = 0;
                        if (var56.getProperty(var8) != null) {
                           var35 = Integer.parseInt(var56.getProperty(var8));
                           if (var35 < 0) {
                              throw new ResourceException(var21.getPoolInLessThanZero(var1, var35));
                           }

                           if (var35 > var34) {
                              throw new ResourceException("Connection pool " + var1 + ": initial size (" + var35 + ") is greater than maximum size (" + var34 + ")");
                           }
                        }

                        int var36 = 0;
                        if (var56.getProperty(var9) != null) {
                           var36 = Integer.parseInt(var56.getProperty(var9));
                           if (var36 > var34) {
                              throw new ResourceException("Connection pool " + var1 + ": increment size (" + var36 + ") is greater than maximum size (" + var34 + ")");
                           }
                        }

                        boolean var37 = true;
                        int var38 = 0;
                        if (var56.getProperty(var10) != null && var56.getProperty(var10).equalsIgnoreCase("false")) {
                           var37 = false;
                        } else {
                           var37 = true;
                        }

                        if (var56.getProperty(var11) != null) {
                           var38 = Integer.parseInt(var56.getProperty(var11));
                           if (var38 < 1) {
                              throw new ResourceException("Connection pool " + var1 + ": shrink period is less than 1 minute");
                           }
                        }

                        if (var37 && var38 == 0) {
                           var38 = 15;
                        }

                        if (var36 < 1) {
                           var36 = 1;
                        }

                        String var39 = null;
                        String var40 = "weblogic.jdbc.oci.xa.XADataSource";
                        if ((var39 = var56.getProperty(var13)) == null) {
                           throw new ResourceException("Missing startup value \"" + var13 + "\" in JDBC Connection Pool " + var1);
                        } else {
                           String var41 = null;
                           if ((var41 = var56.getProperty(var12)) == null) {
                              throw new ResourceException("Missing startup value \"" + var12 + "\" in JDBC Connection Pool " + var1);
                           } else {
                              String var42 = null;
                              var42 = var56.getProperty(var17);
                              boolean var43 = false;
                              boolean var44 = false;
                              if (var56.getProperty(var15) != null) {
                                 var43 = true;
                              }

                              if (var56.getProperty(var16) != null) {
                                 var44 = true;
                              }

                              int var45 = 0;
                              String var46 = null;
                              if ((var46 = var56.getProperty(var14)) != null) {
                                 var45 = Integer.parseInt(var46);
                                 if (var45 < 1) {
                                    throw new ResourceException("The refresh period must be one minute or more");
                                 }
                              }

                              if ((var44 || var43) && var42 == null) {
                                 throw new ResourceException("Missing startup value \"" + var17 + "\" in JDBC Connection Pool " + var1);
                              } else if (var45 > 0 && var42 == null) {
                                 throw new ResourceException("Missing startup value \"" + var17 + "\" in JDBC Connection Pool " + var1);
                              } else {
                                 int var47 = 0;
                                 if (var56.getProperty(var18) != null) {
                                    var47 = Integer.parseInt(var56.getProperty(var18));
                                    if (var47 < 0) {
                                       throw new ResourceException("Connection pool " + var1 + " : loginDelaySecs must be positive");
                                    }
                                 }

                                 int var48 = 0;
                                 if (var56.getProperty(var19) != null) {
                                    var48 = Integer.parseInt(var56.getProperty(var19));
                                    if (var48 < 0) {
                                       throw new ResourceException("Connection pool " + var1 + " : secondsToTrustAnIdlePoolConnection must be positive");
                                    }
                                 }

                                 boolean var49 = false;
                                 String var50 = null;
                                 if (var56.getProperty(var20) != null) {
                                    var50 = var56.getProperty(var20);
                                    var49 = true;
                                 }

                                 JDBCConnectionPoolMBean var51 = (JDBCConnectionPoolMBean)var54.createAdminMBean(var1, "JDBCConnectionPool", var54.getDomainName());
                                 if (var31 != null) {
                                    var51.setACLName(var31);
                                 }

                                 var51.setURL(var39);
                                 var51.setDriverName(var41);
                                 var51.setProperties(var33);
                                 var51.setLoginDelaySeconds(var47);
                                 var51.setSecondsToTrustAnIdlePoolConnection(var48);
                                 var51.setInitialCapacity(var35);
                                 var51.setMaxCapacity(var34);
                                 var51.setCapacityIncrement(var36);
                                 var51.setShrinkingEnabled(var37);
                                 if (var38 != 0) {
                                    var51.setShrinkFrequencySeconds(var38 * 60);
                                 }

                                 var51.setTestFrequencySeconds(var45 * 60);
                                 var51.setTestTableName(var42);
                                 var51.setTestConnectionsOnReserve(var43);
                                 var51.setTestConnectionsOnRelease(var44);
                                 if (var49) {
                                    var51.setPassword(var50);
                                    var49 = true;
                                 }

                                 var51.addTarget(var55);
                                 System.out.println(var21.getconnPoolSuc(var1));
                              }
                           }
                        }
                     }
                  } else {
                     throw new ResourceException(var21.getMissPool());
                  }
               }
            }
         } catch (T3ExecuteException var52) {
            ManagementTextTextFormatter var23;
            if (var52.getNestedException().toString().indexOf("SecurityException") != -1) {
               var23 = new ManagementTextTextFormatter();
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.out.println(var23.getCreatePoolSecException(var1));
               }

               throw var52;
            } else {
               var23 = new ManagementTextTextFormatter();
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.out.println(var23.getFailCreatePool(var1) + var52.getNestedException().toString());
               }

               throw var52;
            }
         }
      }
   }

   static void executeDeletePoolCommand(CommandLineArgs var0) throws Exception {
      String var1 = null;
      AdminToolHelper var2 = new AdminToolHelper(var0);
      var1 = var2.nextArg("", 0);
      ManagementTextTextFormatter var3 = new ManagementTextTextFormatter();

      try {
         Context var4 = AdminToolHelper.getInitialContext(var0);
         MBeanHome var5 = (MBeanHome)var4.lookup("weblogic.management.adminhome");
         JDBCConnectionPoolMBean var6 = (JDBCConnectionPoolMBean)var5.getAdminMBean(var1, "JDBCConnectionPool", var5.getDomainName());

         try {
            var6.setTargets(new TargetMBean[0]);
         } catch (Exception var8) {
            System.out.println(var3.getErrorUndeployingPool(var1, var8));
         }

         var5.deleteMBean((WebLogicMBean)var6);
         System.out.println(var3.getPoolDeleted(var1));
      } catch (Exception var9) {
         System.out.println(var3.getErrorDeletingPool(var1, var9));
      }

   }

   static void executeDestroyPoolCommand(CommandLineArgs var0) throws Exception {
      try {
         executeDisablePoolCommand(var0);
      } catch (Exception var2) {
      }

      executeDeletePoolCommand(var0);
   }

   void doOperation() throws Exception {
      switch (params.getOperation()) {
         case 12:
            executeResetPoolCommand(params);
            break;
         case 13:
            executeCreatePoolCommand(params);
            break;
         case 14:
            executeDisablePoolCommand(params);
            break;
         case 15:
            executeEnablePoolCommand(params);
            break;
         case 16:
            executeDisablePoolCommand(params);
            break;
         case 17:
            executeExistsPoolCommand(params);
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         default:
            break;
         case 38:
            executeTestPoolCommand(params);
            break;
         case 44:
            executeSuspendPoolCommand(params);
            break;
         case 45:
            executeShutdownPoolCommand(params);
            break;
         case 46:
            executeResumePoolCommand(params);
            break;
         case 47:
            executeDeletePoolCommand(params);
            break;
         case 48:
            executeDestroyPoolCommand(params);
      }

   }

   private void doCommandline() throws Exception {
      ManagementTextTextFormatter var10;
      try {
         if (this.adminHome == null) {
            if (params.getAdminUrl() != null) {
               params.setUrl(params.getAdminUrl());
               this.adminHome = AdminToolHelper.getAdminMBeanHome(params);
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
            if (!params.showNoMessages() && !AdminToolHelper.printDone) {
               AdminToolHelper.printException(var9);
               AdminToolHelper.printDone = true;
            }

            throw var9;
         }

         RuntimeOperationsException var2 = (RuntimeOperationsException)var9;
         if (var2.getTargetException() instanceof RemoteRuntimeException) {
         }
      }

   }

   static {
      printStream = System.out;
   }
}
