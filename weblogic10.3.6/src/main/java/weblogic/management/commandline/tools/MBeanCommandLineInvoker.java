package weblogic.management.commandline.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.URL;
import java.rmi.MarshalException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.kernel.Kernel;
import weblogic.management.MBeanCreationException;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementError;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commandline.CommandDescription;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.commandline.OutputFormatter;
import weblogic.management.commo.CommoAdminTool;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.utils.StringUtils;

public final class MBeanCommandLineInvoker {
   static final String OK_STRING = "Ok";
   private static final boolean debug = false;
   private CommandLineArgs params;
   private MBeanHome adminHome;
   private Set matchedMBeans;
   private OutputFormatter out;
   private boolean EXIT;
   private boolean batchMode;
   private PrintStream printStream;
   private boolean commoMbeanType;
   private static final String USERNAME = "username";
   private static final String PASSWORD = "password";
   private static final String URL = "url";
   private static final String INPUTFILE = "inputfile";
   private static final String DEFAULT_URL = "http://localhost:7001";
   private static final String COMMAND = "cmd";
   private static final Set NON_COMMAND_ADMIN_OPTIONS = new HashSet(Arrays.asList((Object[])(new String[]{"username", "password", "url", "inputfile"})));

   public MBeanCommandLineInvoker(CommandLineArgs var1, PrintStream var2) throws Exception {
      this.params = null;
      this.adminHome = null;
      this.matchedMBeans = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;
      this.printStream = System.out;
      this.commoMbeanType = false;

      try {
         this.params = var1;
         if (var2 != null) {
            this.printStream = var2;
         }

         if (var2 != null) {
            AdminToolHelper.printStream = var2;
         }

         this.doCommandline();
      } catch (Exception var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var4);
         }

         throw var4;
      }
   }

   public MBeanCommandLineInvoker(String[] var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.params = null;
      this.adminHome = null;
      this.matchedMBeans = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;
      this.printStream = System.out;
      this.commoMbeanType = false;
      this.params = new CommandLineArgs(var1);
      if (var2 != null) {
         this.printStream = var2;
      }

      this.adminHome = var3;
      this.doCommandline();
   }

   public MBeanCommandLineInvoker(CommandLineArgs var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.params = null;
      this.adminHome = null;
      this.matchedMBeans = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;
      this.printStream = System.out;
      this.commoMbeanType = false;
      this.params = var1;
      if (var2 != null) {
         this.printStream = var2;
      }

      this.adminHome = var3;
      this.doCommandline();
   }

   public MBeanCommandLineInvoker(String[] var1, PrintStream var2) throws Exception {
      this(new CommandLineArgs(var1), var2);
   }

   public static void main(String[] var0) throws Exception {
      new MBeanCommandLineInvoker(var0, System.out);
   }

   private void processCommoMBeans() throws JMException, Exception {
      if (!Kernel.isServer()) {
         RemoteMBeanServer var1 = null;

         try {
            var1 = this.adminHome.getMBeanServer();
         } catch (Exception var3) {
            AdminToolHelper.printException(var3, true);
            throw var3;
         }

         CommoAdminTool.doIt(this.params, var1, var1.getDefaultDomain(), this.adminHome);
      }
   }

   private void doCommandline() throws Exception {
      ManagementTextTextFormatter var12;
      try {
         this.adminHome = this.params.getAdminHome();
         if (this.adminHome == null) {
            if (this.params.getAdminUrl() != null) {
               this.adminHome = AdminToolHelper.getAdminMBeanHome(this.params);
               this.params.setUrl(this.params.getAdminUrl());
            } else {
               this.adminHome = AdminToolHelper.getMBeanHome(this.params);
            }
         }

         if (this.params.getCommoType()) {
            this.processCommoMBeans();
            this.commoMbeanType = true;
            return;
         }

         if (this.params.getOperation() != 33 && this.params.getMBeanObjName() != null) {
            try {
               RemoteMBeanServer var1 = this.adminHome.getMBeanServer();
               if (var1.isInstanceOf(new ObjectName(this.params.getMBeanObjName()), "weblogic.management.commo.StandardInterface")) {
                  this.processCommoMBeans();
                  this.commoMbeanType = true;
                  return;
               }
            } catch (InstanceNotFoundException var3) {
            }
         }

         if (this.params.getOperation() != 50 && this.params.getOperation() != 29 && this.params.getOperation() != 36 && this.params.getOperation() != 52) {
            this.listMatchedMBeans();
         }

         this.out = new OutputFormatter(this.printStream, this.params.isPretty());
         if (!this.commoMbeanType) {
            this.doOperation();
         }
      } catch (IllegalArgumentException var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var4);
            AdminToolHelper.printDone = true;
         }

         throw var4;
      } catch (MalformedObjectNameException var5) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var5, true);
            AdminToolHelper.printDone = true;
         }

         this.printStream.println("Usage:\n" + CommandLineArgs.getUsageString());
         throw var5;
      } catch (InstanceNotFoundException var6) {
         var12 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            this.printStream.println(var12.getCouldNotFindInstance(this.params.getMBeanObjName()));
            AdminToolHelper.printDone = true;
         }

         throw var6;
      } catch (RemoteRuntimeException var7) {
         Throwable var13 = var7.getNestedException();
         if (var13 instanceof PeerGoneException) {
            return;
         }

         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var7, true);
            AdminToolHelper.printDone = true;
         }

         throw var7;
      } catch (ConnectException var8) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException("java.net.ConnectException", var8);
            AdminToolHelper.printDone = true;
         }

         throw var8;
      } catch (IOException var9) {
         var12 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var12.getErrorWriting(), var9);
            AdminToolHelper.printDone = true;
         }

         throw var9;
      } catch (MBeanException var10) {
         new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var10.getTargetException().getClass().getName(), var10.getTargetException());
            AdminToolHelper.printDone = true;
         }

         throw var10;
      } catch (Exception var11) {
         if (var11 instanceof RuntimeOperationsException) {
            RuntimeOperationsException var2 = (RuntimeOperationsException)var11;
            if (var2.getTargetException() instanceof RemoteRuntimeException) {
            }
         } else {
            if (!(var11 instanceof AttributeNotFoundException)) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var11);
                  AdminToolHelper.printDone = true;
               }

               throw var11;
            }

            if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
               AdminToolHelper.printException("Attribute not found", var11);
               AdminToolHelper.printDone = true;
            }
         }
      }

   }

   private void doOperation() throws Exception {
      switch (this.params.getOperation()) {
         case 29:
            this.doQuery();
         case 30:
         default:
            break;
         case 31:
            this.doGet();
            break;
         case 32:
            this.doSet();
            break;
         case 33:
            this.doCreate();
            break;
         case 34:
            this.doDelete();
            break;
         case 35:
            this.doInvoke();
            break;
         case 36:
            this.doBatchOperation();
      }

   }

   private void doBatchOperation() throws Exception {
      this.batchMode = this.params.showNoMessages();
      new ManagementTextTextFormatter();
      String[] var2 = this.getCommands();
      if (this.adminHome == null) {
         try {
            if (this.params.getAdminUrl() != null) {
               this.adminHome = AdminToolHelper.getAdminMBeanHome(this.params);
            } else {
               this.adminHome = AdminToolHelper.getMBeanHome(this.params);
            }
         } catch (Exception var17) {
            AdminToolHelper.printException(var17, false);
         }
      }

      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      String[] var6 = null;
      boolean var7 = this.params.isBatchCmdVerbose();
      int var8 = 0;

      for(int var9 = var2 != null ? var2.length : 0; var8 < var9; ++var8) {
         String var10 = var2[var8];
         if (var10.trim().length() >= 1 && !var10.trim().startsWith("#")) {
            var10 = var10.replaceAll("\"\"", "_WLS_EMPTYSTR_");
            var10 = "-batchCmd " + var10;
            boolean var11 = false;
            int var12;
            if (this.params.getAdminUrl() != null) {
               var12 = 0;

               for(int var13 = var6 != null ? var6.length : 0; var12 < var13; ++var12) {
                  Object var14 = var6[var12];
                  if (((String)var14).startsWith("-adminurl")) {
                     var11 = true;
                  }
               }

               if (!var11) {
                  var10 = " -adminurl " + this.params.getAdminUrl() + " " + var10;
               }
            }

            if (this.batchMode) {
               var10 = var10 + " -showNoMessages";
            }

            if (var10.indexOf("\"") <= -1 && var10.indexOf("'") <= -1) {
               var6 = StringUtils.splitCompletely(var10);
            } else {
               Vector var19 = new Vector();
               String[] var20 = StringUtils.splitCompletely(var10, "\"");

               int var23;
               for(var23 = 0; var23 < var20.length; ++var23) {
                  var20[var23].trim();
                  String[] var15 = StringUtils.splitCompletely(var20[var23]);

                  for(int var16 = 0; var16 < var15.length; ++var16) {
                     var19.add(var15[var16]);
                  }

                  ++var23;
                  if (var23 < var20.length) {
                     var19.add(var20[var23]);
                  }
               }

               var6 = new String[var19.size()];

               for(var23 = 0; var23 < var19.size(); ++var23) {
                  String var24 = (String)var19.get(var23);
                  if (var24.equals("_WLS_EMPTYSTR_")) {
                     var24 = "";
                  }

                  var6[var23] = var24;
               }
            }

            for(var12 = 0; var12 < var6.length; ++var12) {
               if (var6[var12].equals("_WLS_EMPTYSTR_")) {
                  var6[var12] = "";
               }
            }

            try {
               if (var7) {
                  System.out.println("\nExecuting command: " + StringUtils.join(StringUtils.splitCompletely(var2[var8]), " ") + "\n");
               }

               CommandLineArgs var21 = new CommandLineArgs(this.params.getUsername(), this.params.getPassword(), this.params.getURL(), var6);
               CommandDescription var22 = var21.getCommandDescription(CommandLineArgs.getOperationString());
               ++var3;
               if (var22.getGenericType() == 104) {
                  new MBeanCommandLineInvoker(var21, System.out, this.adminHome);
               } else if (var22.getGenericType() == 103) {
                  new JDBCCommandLineInvoker(var21, System.out, this.adminHome);
               } else if (var22.getGenericType() == 101) {
                  new ServerInfoCommandLineInvoker(var21, System.out, this.adminHome);
               } else if (var22.getGenericType() == 102) {
                  new ServerAdminCommandLineInvoker(var21, System.out, this.adminHome);
               } else if (var22.getGenericType() == 105) {
                  new ClusterAdminCommandLineInvoker(var21, System.out, this.adminHome);
               }

               ++var5;
            } catch (Exception var18) {
               if (!this.params.continueOnError()) {
                  System.out.println("\nError: " + this.params.getBatchFileName() + " at line number: " + (var8 + 1));
                  ++var4;
                  break;
               }

               System.out.println("\nError executing command in batch file " + this.params.getBatchFileName() + " at line number: " + (var8 + 1));
               ++var4;
            }
         }
      }

      if (var7) {
         System.out.println("\n---------------------------------------------------------------------------------------------");
         System.out.println("---------------------------------------------------------------------------------------------");
         System.out.println("Batch Command Results: ");
         System.out.println("Total Commands Executed: " + var3);
         System.out.println("Commands Successful: " + var5);
         System.out.println("Commands Failed: " + var4);
      }

   }

   private void doQuery() throws Exception {
      try {
         ObjectName var1 = new ObjectName(this.params.getObjectNamePattern());
         Set var21 = this.adminHome.getMBeanServer().queryNames(var1, (QueryExp)null);
         Iterator var3 = var21.iterator();
         boolean var4 = false;
         new AttributeList();
         if (!var3.hasNext()) {
            ManagementTextTextFormatter var22 = new ManagementTextTextFormatter();
            this.printStream.println(var22.getNoMBeansFound());
         } else {
            while(true) {
               ObjectName var6;
               Object var7;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  var6 = null;
                  var7 = null;
                  var7 = var3.next();
               } while(var7 == null);

               String[] var8 = null;

               AttributeList var5;
               try {
                  var6 = (ObjectName)var7;
                  var8 = this.getAllAttribute(var6);
                  var5 = this.adminHome.getMBeanServer().getAttributes(var6, var8);
                  MBeanInfo var9 = this.adminHome.getMBeanServer().getMBeanInfo(var6);
                  MBeanAttributeInfo[] var10 = var9.getAttributes();
                  ModelMBeanAttributeInfo[] var11 = null;
                  if (null != var10 && ModelMBeanAttributeInfo[].class.isAssignableFrom(var10.getClass())) {
                     var11 = (ModelMBeanAttributeInfo[])((ModelMBeanAttributeInfo[])var10);
                  }

                  for(int var12 = 0; var12 < var5.size(); ++var12) {
                     Attribute var13 = (Attribute)var5.get(var12);
                     String var14 = var13.getName();

                     for(int var15 = 0; var15 < var11.length; ++var15) {
                        if (var11[var15].getName().equals(var14) && this.isEncrypted(var11[var15]) && var13.getValue() != null) {
                           var5.set(var12, new Attribute(var14, "******"));
                           break;
                        }
                     }
                  }
               } catch (InstanceNotFoundException var16) {
                  AdminToolHelper.printException(var16);
                  throw var16;
               } catch (ReflectionException var17) {
                  AdminToolHelper.printException(var17);
                  throw var17;
               }

               this.out.mbeanBegin(var6.toString());
               this.out.printAttribs(var5, var8);
               this.out.mbeanEnd();
            }
         }
      } catch (MalformedObjectNameException var18) {
         ManagementTextTextFormatter var2 = new ManagementTextTextFormatter();
         AdminToolHelper.printException(var2.getPatternNotUnderstood(), var18, false);
         throw var18;
      } catch (InstanceNotFoundException var19) {
         AdminToolHelper.printException("", var19, true);
         throw var19;
      } catch (JMException var20) {
         AdminToolHelper.printException("", var20, true);
         throw var20;
      }
   }

   private boolean isEncrypted(ModelMBeanAttributeInfo var1) {
      Boolean var2 = (Boolean)var1.getDescriptor().getFieldValue("com.bea.encrypted");
      return var2 != null ? var2 : false;
   }

   private boolean isExcluded(ModelMBeanAttributeInfo var1) {
      Boolean var2 = (Boolean)var1.getDescriptor().getFieldValue("com.bea.exclude");
      return var2 != null ? var2 : false;
   }

   private void doGet() throws Exception {
      AttributeList var1 = new AttributeList();
      Iterator var2 = this.matchedMBeans.iterator();
      if (!var2.hasNext()) {
         ManagementTextTextFormatter var20 = new ManagementTextTextFormatter();
         this.printStream.println(var20.getNoMBeansFound());
      } else {
         while(var2.hasNext()) {
            WebLogicMBean var3 = (WebLogicMBean)var2.next();
            String[] var4 = null;

            try {
               var4 = this.getAllAttribute(var3);

               int var22;
               Object var24;
               try {
                  if (var4.length == 1) {
                     var1.add(new Attribute(var4[0], var3.getAttribute(var4[0])));
                  } else {
                     var1 = var3.getAttributes(var4);
                  }
               } catch (RemoteRuntimeException var14) {
                  label104: {
                     if (var14.getNestedException() instanceof MarshalException) {
                        var22 = 0;

                        while(true) {
                           if (var22 >= var4.length) {
                              break label104;
                           }

                           try {
                              var24 = var3.getAttribute(var4[var22]);
                              var1.add(new Attribute(var4[var22], var24));
                           } catch (RemoteRuntimeException var13) {
                              if (!(var13.getNestedException() instanceof MarshalException)) {
                                 throw var13;
                              }
                           }

                           ++var22;
                        }
                     }

                     throw var14;
                  }
               } catch (RuntimeErrorException var15) {
                  for(var22 = 0; var22 < var4.length; ++var22) {
                     try {
                        var24 = var3.getAttribute(var4[var22]);
                        var1.add(new Attribute(var4[var22], var24));
                     } catch (RuntimeErrorException var12) {
                     }
                  }
               }

               MBeanInfo var5 = getMBeanInfo(var3);
               MBeanAttributeInfo[] var25 = var5.getAttributes();
               ModelMBeanAttributeInfo[] var26 = null;
               if (null != var25 && ModelMBeanAttributeInfo[].class.isAssignableFrom(var25.getClass())) {
                  var26 = (ModelMBeanAttributeInfo[])((ModelMBeanAttributeInfo[])var25);
               }

               for(int var8 = 0; var8 < var1.size(); ++var8) {
                  Attribute var9 = (Attribute)var1.get(var8);
                  String var10 = var9.getName();

                  for(int var11 = 0; var11 < var26.length; ++var11) {
                     if (var26[var11].getName().equals(var10) && this.isEncrypted(var26[var11]) && var9.getValue() != null) {
                        var1.set(var8, new Attribute(var10, "******"));
                        break;
                     }
                  }
               }
            } catch (InstanceNotFoundException var16) {
               AdminToolHelper.printException(var16);
               AdminToolHelper.printDone = true;
               throw var16;
            } catch (ReflectionException var17) {
               AdminToolHelper.printException(var17);
               AdminToolHelper.printDone = true;
               throw var17;
            } catch (RemoteRuntimeException var18) {
               Throwable var21 = var18.getNestedException();
               if (var21 instanceof PeerGoneException) {
                  return;
               }

               AdminToolHelper.printException(var18);
               AdminToolHelper.printDone = true;
               throw var18;
            } catch (RuntimeErrorException var19) {
               if (!(var19.getTargetError() instanceof ManagementError)) {
                  AdminToolHelper.printException(var19);
                  AdminToolHelper.printDone = true;
                  throw var19;
               }

               if (((ManagementError)var19.getTargetError()).getNestedError() instanceof AttributeNotFoundException) {
                  String var6 = "";

                  for(int var7 = 0; var7 < var4.length; ++var7) {
                     var6 = var6 + " " + var4[var7];
                  }

                  ManagementTextTextFormatter var23 = new ManagementTextTextFormatter();
                  this.printStream.println(var23.getInvalidParameterError(var6));
                  AdminToolHelper.printDone = true;
                  throw var19;
               }
            }

            this.out.mbeanBegin(var3.getObjectName().toString());
            this.out.printAttribs(var1, var4);
            this.out.mbeanEnd();
            var1.clear();
         }

      }
   }

   private void doSet() throws Exception {
      Iterator var1 = this.matchedMBeans.iterator();
      if (!var1.hasNext()) {
         ManagementTextTextFormatter var6 = new ManagementTextTextFormatter();
         this.printStream.println(var6.getNoMBeansFound());
      } else {
         AttributeList var2 = this.params.getAttribList();
         if (0 == var2.size()) {
            ManagementTextTextFormatter var7 = new ManagementTextTextFormatter();
            this.printStream.println(var7.getNoProp());
         } else {
            WebLogicMBean var3 = null;

            while(var1.hasNext()) {
               var3 = (WebLogicMBean)var1.next();
               Iterator var4 = var2.iterator();

               while(var4.hasNext()) {
                  Attribute var5 = (Attribute)var4.next();
                  var3.setAttribute(var5);
               }
            }

            if (!this.params.showNoMessages()) {
               this.out.println("Ok");
            }

         }
      }
   }

   private void doCreate() throws Exception {
      WebLogicObjectName var1 = null;
      HashMap var2 = new HashMap();
      Vector var3 = new Vector();

      ManagementTextTextFormatter var5;
      try {
         String var4;
         String var6;
         StringTokenizer var10;
         if (null != this.params.getNewMBeanName()) {
            var4 = this.params.getDomainName();
            if (null == var4) {
               var4 = this.getDefaultDomain();
            }

            var1 = new WebLogicObjectName(this.params.getNewMBeanName(), this.params.getMBeanType(), var4);
         } else if (null != this.params.getMBeanObjName()) {
            var4 = this.params.getMBeanObjName();
            int var21 = var4.indexOf(58);
            var6 = var4.substring(var21 + 1, var4.length());
            StringTokenizer var7 = new StringTokenizer(var6, ",");
            int var8 = 0;

            while(true) {
               if (!var7.hasMoreElements()) {
                  var1 = new WebLogicObjectName(this.params.getMBeanObjName());
                  break;
               }

               String var9 = var7.nextToken();
               var10 = new StringTokenizer(var9, "=");
               String var11 = null;
               String var12 = null;

               while(var10.hasMoreTokens()) {
                  var11 = var10.nextToken();
                  var12 = var10.nextToken();
                  if (!var11.equals("Name") && !var11.equals("Type") && !var11.equals("Location")) {
                     var3.add(var8, var11);
                     ++var8;
                     var2.put(var11, var12);
                  }
               }
            }
         }

         var4 = var1.getType();
         String var22 = var1.getName();
         var6 = var1.getDomain();
         ConfigurationMBean var23 = null;
         Hashtable var24 = var1.getKeyPropertyList();
         Iterator var25 = var2.keySet().iterator();
         if (var23 == null && !var2.isEmpty()) {
            ConfigurationMBean var26 = null;
            Iterator var27 = var2.keySet().iterator();
            int var29 = 0;

            while(var27.hasNext()) {
               var27.next();
               String var13 = (String)var3.get(var29);
               ++var29;
               String var14 = (String)var2.get(var13);
               if (var26 == null) {
                  try {
                     var26 = this.adminHome.getAdminMBean(var14, var13, var6);
                  } catch (Exception var16) {
                  }

                  if (var26 == null) {
                     var26 = (ConfigurationMBean)this.adminHome.createAdminMBean(var14, var13, var6);
                  }
               } else {
                  var26 = (ConfigurationMBean)this.adminHome.createAdminMBean(var14, var13, var6, var26);
               }
            }

            var23 = var26;
         }

         var10 = null;
         if (var4 == null) {
            throw new MBeanCreationException("Type is null");
         }

         ManagementTextTextFormatter var28;
         if (var4.endsWith("Config")) {
            var28 = new ManagementTextTextFormatter();
            throw new MBeanCreationException(var28.getConfigCreateError());
         }

         if (var4.endsWith("Runtime")) {
            var28 = new ManagementTextTextFormatter();
            throw new MBeanCreationException(var28.getRuntimeCreateError());
         }

         if (var23 != null) {
            this.adminHome.createAdminMBean(var22, var4, var6, var23);
         } else {
            this.adminHome.createAdminMBean(var22, var4, var6);
         }
      } catch (MBeanCreationException var18) {
         if (var18.getCause() instanceof BeanAlreadyExistsException) {
            this.completeOnDuplicate(var1);
            if (!this.params.isVerbose()) {
               return;
            }

            var18.getCause().printStackTrace();
         }

         if (var18.getNested() instanceof BeanAlreadyExistsException || var18.getNestedException() instanceof BeanAlreadyExistsException) {
            this.completeOnDuplicate(var1);
            if (!this.params.isVerbose()) {
               return;
            }

            var18.getCause().printStackTrace();
         }

         if (this.params.getMBeanObjName() != null && this.adminHome.getMBeanServer().isRegistered(new ObjectName(this.params.getMBeanObjName()))) {
            this.completeOnDuplicate(var1);
            throw var18;
         }

         if (this.params.getMBeanObjName() != null && this.params.getMBeanType() != null) {
            try {
               this.processCommoMBeans();
               this.commoMbeanType = true;
               return;
            } catch (JMException var17) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var17);
                  AdminToolHelper.printDone = true;
               }

               throw var17;
            }
         }

         if (!this.params.isVerbose()) {
            if (this.hasAlreadyExists(var18)) {
               var5 = new ManagementTextTextFormatter();
               System.out.println("\n" + var5.getInstanceAlreadyExists(var1.toString()));
               AdminToolHelper.printDone = true;
               throw var18;
            }

            if (!(var18.getNestedException() instanceof ClassNotFoundException) && !(var18.getNestedException() instanceof ConfigurationError)) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var18, false);
                  AdminToolHelper.printDone = true;
               }

               throw var18;
            }

            var5 = new ManagementTextTextFormatter();
            System.out.println("\n" + var5.getInvalidMBeanType(var1.getType()));
            AdminToolHelper.printDone = true;
            throw var18;
         }

         AdminToolHelper.printException(var18);
      } catch (MalformedObjectNameException var19) {
         if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
            AdminToolHelper.printException(var19);
            AdminToolHelper.printDone = true;
         }

         throw var19;
      } catch (Exception var20) {
         if (this.hasAlreadyExists(var20)) {
            var5 = new ManagementTextTextFormatter();
            System.out.println("\n" + var5.getInstanceAlreadyExists(var1.toString()));
            AdminToolHelper.printDone = true;
         }

         throw var20;
      }

      if (!this.params.showNoMessages()) {
         this.out.println("Ok");
      }

   }

   private boolean hasAlreadyExists(Throwable var1) {
      Throwable var2 = var1.getCause();
      if (var2 == null) {
         return false;
      } else {
         return !(var2 instanceof InstanceAlreadyExistsException) && !(var2 instanceof BeanAlreadyExistsException) ? this.hasAlreadyExists(var2) : true;
      }
   }

   private void completeOnDuplicate(WebLogicObjectName var1) {
      ManagementTextTextFormatter var2 = new ManagementTextTextFormatter();
      System.out.println("\n" + var2.getInstanceAlreadyExists(var1.toString()));
      AdminToolHelper.printDone = true;
   }

   private void doDelete() throws Exception {
      Object var1 = null;
      if (null != this.params.getNewMBeanName() && null != this.params.getMBeanType()) {
         Set var8 = this.adminHome.getMBeansByType(this.params.getMBeanType());
         Iterator var10 = var8.iterator();
         if (!var10.hasNext()) {
            ManagementTextTextFormatter var11 = new ManagementTextTextFormatter();
            if (!this.params.showNoMessages()) {
               this.printStream.println(var11.getNoMBeansFound());
            }

            return;
         }

         boolean var4 = false;

         while(var10.hasNext()) {
            WebLogicMBean var5 = (WebLogicMBean)var10.next();
            if (var5.getName().equals(this.params.getNewMBeanName())) {
               this.adminHome.deleteMBean(var5);
               System.out.println("\nDeleted MBean with Object Name: " + var5.getObjectName());
               var4 = true;
            }
         }

         if (!var4) {
            ManagementTextTextFormatter var12 = new ManagementTextTextFormatter();
            if (!this.params.showNoMessages()) {
               this.printStream.println(var12.getNoMBeansFound());
            }
         }
      } else {
         Iterator var2 = this.matchedMBeans.iterator();
         if (!var2.hasNext()) {
            ManagementTextTextFormatter var9 = new ManagementTextTextFormatter();
            if (!this.params.showNoMessages()) {
               this.printStream.println(var9.getNoMBeansFound());
            }

            return;
         }

         while(var2.hasNext()) {
            try {
               WebLogicMBean var3 = (WebLogicMBean)var2.next();
               this.adminHome.deleteMBean(var3);
            } catch (InstanceNotFoundException var6) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var6);
                  AdminToolHelper.printDone = true;
               }

               throw var6;
            } catch (MBeanRegistrationException var7) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var7);
                  AdminToolHelper.printDone = true;
               }

               throw var7;
            }
         }

         if (!this.params.showNoMessages()) {
            this.out.println("Ok");
         }
      }

   }

   private void doInvoke() throws Exception {
      Iterator var1 = this.matchedMBeans.iterator();
      if (!var1.hasNext()) {
         ManagementTextTextFormatter var13 = new ManagementTextTextFormatter();
         if (!this.params.showNoMessages()) {
            this.printStream.println(var13.getNoMBeansFound());
         }

      } else {
         while(var1.hasNext()) {
            Object var2 = null;
            WebLogicMBean var3 = (WebLogicMBean)var1.next();

            ManagementTextTextFormatter var5;
            try {
               String[] var4 = this.getMethodSignature(var3, this.params.getMethodName());
               var2 = var3.invoke(this.params.getMethodName(), this.params.getMethodArguments(var4), var4);
            } catch (IllegalArgumentException var6) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var6);
                  AdminToolHelper.printDone = true;
               }

               throw var6;
            } catch (InstanceNotFoundException var7) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var7);
                  AdminToolHelper.printDone = true;
               }

               throw var7;
            } catch (MBeanException var8) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var8);
                  AdminToolHelper.printDone = true;
               }

               throw var8;
            } catch (ReflectionException var9) {
               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  if (var9.getCause() instanceof NoSuchMethodException) {
                     this.printStream.println("Method " + this.params.getMethodName() + " Not available on MBean " + var3.getObjectName());
                     AdminToolHelper.printDone = true;
                  } else {
                     var9.printStackTrace();
                     AdminToolHelper.printDone = true;
                  }
               }

               throw var9;
            } catch (RemoteRuntimeException var10) {
               Throwable var14 = var10.getNestedException();
               if (var14 != null) {
                  if (var14 instanceof PeerGoneException) {
                     return;
                  }

                  if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                     AdminToolHelper.printException((Exception)var14, true);
                     AdminToolHelper.printDone = true;
                  }

                  throw var10;
               }

               if (!this.params.showNoMessages() && !AdminToolHelper.printDone) {
                  AdminToolHelper.printException(var10, true);
                  AdminToolHelper.printDone = true;
               }

               throw var10;
            } catch (Exception var11) {
               var5 = new ManagementTextTextFormatter();
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printException("\n" + var5.getCouldNotInvoke() + " method " + this.params.getMethodName() + " on MBean: " + var3.getObjectName().toString() + " due to ", var11, false);
                  AdminToolHelper.printDone = true;
               }

               throw var11;
            } catch (Throwable var12) {
               var5 = new ManagementTextTextFormatter();
               if (!this.params.showNoMessages()) {
                  this.printStream.println(var5.getCouldNotInvoke() + var3.getObjectName() + ", " + var12);
               }

               return;
            }

            this.out.mbeanBegin(var3.getObjectName().toString());
            this.out.printReturnValue(this.params.getMethodName(), var2);
            this.out.mbeanEnd();
         }

         if (!this.params.showNoMessages()) {
            this.out.println("Ok");
         }

      }
   }

   private void listMatchedMBeans() throws Exception {
      WebLogicObjectName var1 = null;
      if (33 != this.params.getOperation()) {
         this.matchedMBeans = new HashSet();
         if (null != this.params.getMBeanObjName()) {
            var1 = new WebLogicObjectName(this.params.getMBeanObjName());

            try {
               this.adminHome.getMBean(var1);
               this.matchedMBeans.add(this.adminHome.getMBean(var1));
            } catch (InstanceNotFoundException var5) {
               if (this.adminHome.getMBeanServer().isRegistered(var1)) {
                  this.processCommoMBeans();
                  this.commoMbeanType = true;
                  return;
               }

               throw var5;
            } catch (ClassCastException var6) {
               if (this.adminHome.getMBeanServer().isRegistered(var1)) {
                  this.processCommoMBeans();
                  this.commoMbeanType = true;
                  return;
               }

               throw var6;
            }
         } else {
            if (null == this.params.getMBeanType()) {
               ManagementTextTextFormatter var7 = new ManagementTextTextFormatter();
               AdminToolHelper.printErrorMessage(var7.getNoMBeanNameOrType(), false);
               AdminToolHelper.printDone = true;
               throw new Exception();
            }

            if (this.isTypeMalformed(this.params.getMBeanType())) {
               return;
            }

            Set var2 = null;
            if (this.params.getAdminUrl() != null) {
               var2 = this.adminHome.getMBeansByType(this.params.getMBeanType());
            } else {
               var2 = AdminToolHelper.getLocalMBeanHome(this.params).getMBeansByType(this.params.getMBeanType());
            }

            if (var2.size() == 0) {
               try {
                  this.processCommoMBeans();
                  this.commoMbeanType = true;
                  return;
               } catch (JMException var4) {
                  AdminToolHelper.printException(var4, false);
                  throw var4;
               }
            }

            this.matchedMBeans.addAll(var2);
         }

      }
   }

   private boolean isTypeMalformed(String var1) {
      return var1.indexOf(":") != -1 || var1.indexOf(",") != -1 || var1.indexOf("=") != -1;
   }

   private String[] getCommands() throws IOException {
      Map var1 = parseInputURL((new File(this.params.getBatchFileName())).toURL().toExternalForm());
      List var2 = (List)var1.get("cmd");
      String[] var3 = new String[var2.size()];
      var2.toArray(var3);
      return var3;
   }

   private static Map parseInputURL(String var0) throws IOException {
      LineNumberReader var1 = new LineNumberReader(new InputStreamReader((new URL(var0)).openStream()));
      HashMap var2 = new HashMap();
      Vector var3 = new Vector();

      String var4;
      while((var4 = var1.readLine()) != null) {
         int var5 = var4.indexOf("=");
         if (var5 != -1) {
            String var6 = var4.substring(0, var5);
            String var7 = var4.substring(var5 + 1);
            if (NON_COMMAND_ADMIN_OPTIONS.contains(var6)) {
               var2.put(var6, var7);
            } else {
               var3.add(var4);
            }
         } else {
            var3.add(var4);
         }
      }

      var2.put("cmd", var3);
      return var2;
   }

   private String[] getAllAttribute(Object var1) throws InstanceNotFoundException, ReflectionException, ConfigurationException, IntrospectionException {
      WebLogicMBean var2 = null;
      ObjectName var3 = null;
      if (var1 instanceof WebLogicMBean) {
         var2 = (WebLogicMBean)var1;
      } else {
         var3 = (ObjectName)var1;
      }

      AttributeList var4 = this.params.getAttribList();
      int var5 = var4.size();
      String[] var6 = null;
      ArrayList var7 = new ArrayList();
      MBeanInfo var8 = null;
      if (var2 != null) {
         var8 = getMBeanInfo(var2);
      } else {
         var8 = this.adminHome.getMBeanServer().getMBeanInfo(var3);
      }

      MBeanAttributeInfo[] var9 = var8.getAttributes();
      ModelMBeanAttributeInfo[] var10 = null;
      if (null != var9 && ModelMBeanAttributeInfo[].class.isAssignableFrom(var9.getClass())) {
         var10 = (ModelMBeanAttributeInfo[])((ModelMBeanAttributeInfo[])var9);
      }

      if (0 != var5) {
         for(int var12 = 0; var12 < var5; ++var12) {
            Attribute var11 = (Attribute)var4.get(var12);
            if (!var11.getName().equals("MBeanInfo")) {
               boolean var13 = false;
               if (null != var10) {
                  for(int var14 = 0; var14 < var10.length; ++var14) {
                     if (var10[var14].getName().equals(var11.getName()) && this.isExcluded(var10[var14]) && this.params.exclude()) {
                        var13 = true;
                        break;
                     }
                  }
               }

               if (!var13) {
                  var7.add(var11.getName());
               }
            }
         }
      } else {
         for(int var15 = 0; var15 < var9.length; ++var15) {
            if (!var9[var15].getName().equals("MBeanInfo") && null != var10 && (!this.isExcluded(var10[var15]) || !this.params.exclude())) {
               var7.add(var9[var15].getName());
            }
         }
      }

      var6 = new String[var7.size()];
      var7.toArray(var6);
      return var6;
   }

   private String getDefaultDomain() throws Exception {
      ServerRuntimeMBean var1 = null;
      String var2 = "mydomain";

      try {
         Set var3 = this.adminHome.getMBeansByType("ServerRuntime");
         Iterator var7 = var3.iterator();
         if (var7.hasNext()) {
            var1 = (ServerRuntimeMBean)var7.next();
            var2 = var1.getObjectName().getDomain();
         }

         return var2;
      } catch (RemoteRuntimeException var6) {
         Throwable var4 = var6.getNestedException();
         if (var4 instanceof PeerGoneException) {
            return var2;
         } else {
            ManagementTextTextFormatter var5 = new ManagementTextTextFormatter();
            if (!this.params.showNoMessages()) {
               AdminToolHelper.printException(var5.getRemExpServerRuntime(), var6);
            }

            throw var6;
         }
      }
   }

   private String[] getMethodSignature(WebLogicMBean var1, String var2) throws InstanceNotFoundException, ConfigurationException {
      MBeanInfo var3 = getMBeanInfo(var1);
      String[] var4 = null;
      IllegalArgumentException var5 = null;
      MBeanOperationInfo[] var6 = var3.getOperations();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         if (var6[var7].getName().equalsIgnoreCase(var2)) {
            MBeanParameterInfo[] var8 = var6[var7].getSignature();
            var4 = new String[var8.length];

            for(int var9 = 0; var9 < var8.length; ++var9) {
               var4[var9] = var8[var9].getType();
            }

            try {
               this.params.getMethodArguments(var4);
               return var4;
            } catch (IllegalArgumentException var10) {
               var5 = var10;
            }
         }
      }

      if (null != var5) {
         throw var5;
      } else {
         ManagementTextTextFormatter var11 = new ManagementTextTextFormatter();
         throw new InstanceNotFoundException(var11.getInstanceMethNotFound(var2, var1.getObjectName().toString()));
      }
   }

   public static MBeanInfo getMBeanInfo(WebLogicMBean var0) throws ConfigurationException {
      return var0.getMBeanInfo();
   }
}
