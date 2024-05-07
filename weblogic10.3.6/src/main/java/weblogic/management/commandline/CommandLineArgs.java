package weblogic.management.commandline;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MalformedObjectNameException;
import weblogic.kernel.Kernel;
import weblogic.management.MBeanHome;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commandline.tools.AdminToolHelper;
import weblogic.management.commandline.tools.MBeanCommandLineInvoker;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.provider.ManagementServiceClient;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;
import weblogic.utils.TypeConversionUtils;
import weblogic.utils.io.TerminalIO;

public final class CommandLineArgs {
   public static final int NONE = 1;
   public static final int PING = 2;
   public static final int STAT = 3;
   public static final int SHUT = 4;
   public static final int LICENCES = 5;
   public static final int VERSION = 6;
   public static final int CONNECT = 7;
   public static final int LOCK = 8;
   public static final int UNLOCK = 9;
   public static final int LIST = 10;
   public static final int CANCEL_SHUT = 11;
   public static final int RESET_POOL = 12;
   public static final int CREATE_POOL = 13;
   public static final int REMOVE_POOL = 14;
   public static final int ENABLE_POOL = 15;
   public static final int DISABLE_POOL = 16;
   public static final int EXISTS_POOL = 17;
   public static final int THREAD_DUMP = 18;
   public static final int LOG_OPERATION = 19;
   public static final int HELP_OPERATION = 20;
   public static final int START = 21;
   public static final int STARTINSTANDBY = 22;
   public static final int SUSPEND = 23;
   public static final int FORCESUSPEND = 24;
   public static final int RESUME = 25;
   public static final int SHUTDOWN = 26;
   public static final int FORCESHUTDOWN = 27;
   public static final int GETSTATE = 28;
   public static final int QUERY_OPERATION = 29;
   public static final int GET_OPERATION = 31;
   public static final int SET_OPERATION = 32;
   public static final int CREATE_OPERATION = 33;
   public static final int DELETE_OPERATION = 34;
   public static final int INVOKE_OPERATION = 35;
   public static final int BATCHUPDATE_OPERATION = 36;
   public static final int CLUSTERSTATE = 37;
   public static final int TEST_POOL = 38;
   public static final int STARTCLUSTER = 39;
   public static final int STOPCLUSTER = 40;
   public static final int VALIDATECLUSTERCONFIG = 41;
   public static final int CONVERTPROPS = 42;
   public static final int DISCOVERMANAGEDSERVER = 43;
   public static final int SUSPEND_POOL = 44;
   public static final int SHUTDOWN_POOL = 45;
   public static final int RESUME_POOL = 46;
   public static final int DELETE_POOL = 47;
   public static final int DESTROY_POOL = 48;
   public static final int MIGRATE_OPERATION = 50;
   public static final int STORE_USER_CONFIG = 51;
   public static final int MIGRATE_SERVER_OPERATION = 52;
   public static final int MIGRATE_ALL_OPERATION = 53;
   public static final int ALL = 100;
   public static final int SERVERINFO = 101;
   public static final int LIFECYCLE = 102;
   public static final int JDBC = 103;
   public static final int MBEAN = 104;
   public static final int CLUSTER = 105;
   public static final int ADMINCONFIG = 106;
   static final String DEFAULT_URL = "t3://localhost:7001";
   public static final String IGNORE_SESSIONS = "-ignoreExistingSessions";
   public static final String TIMEOUT = "-timeout";
   public boolean isIIOP;
   private static final int MBEAN_OPERATION_START = 30;
   static Hashtable cmds = new Hashtable();
   static Hashtable infoCmds = new Hashtable();
   static Hashtable serverSpecificCmds = new Hashtable();
   static Hashtable poolCmds = new Hashtable();
   static Hashtable mbeanCmds = new Hashtable();
   static Hashtable clusterCmds = new Hashtable();
   static Hashtable adminConfigCmds = new Hashtable();
   private static String operationString = null;
   private String url;
   private String username;
   private String password;
   private String userconfigfile;
   private String userkeyfile;
   private int operation;
   private String mBeanObjName;
   private String mbeanType;
   private boolean pretty;
   private boolean exclude;
   private boolean verbose;
   private boolean isURLset;
   private boolean isTimeoutSet;
   private AttributeList attribList;
   private String methodName;
   private String domainName;
   private Vector methodArguments;
   private String newMBeanName;
   private String migratableTargetName;
   private String singletonServiceName;
   private String migratableServerName;
   private String destinationServerName;
   private String migratableServerDestinationMachineName;
   private String sourceServer;
   private boolean migrateJTA;
   private boolean sourceDown;
   private boolean destinationDown;
   private boolean commoType;
   private String pattern;
   private String batchFile;
   private boolean continueOnErr;
   private boolean showNoMessages;
   private boolean batchCmd;
   private String clusterName;
   private String configPath;
   private String adminUrl;
   private boolean ignoreSessions;
   private int timeout;
   private boolean batchCmdVerbose;
   private String serverPropertiesDir;
   private String clusterPropertiesDir;
   private String outputDir;
   private String serverName;
   private String newDomainName;
   private String usernamePwdLocation;
   private String listenAdress;
   private String listenPort;
   private boolean listenPortSecure;
   private boolean usingBootProperties;
   public static boolean typeMunged = false;
   private String poolName;
   private boolean noExit;
   private Vector positionalArgs;
   private MBeanHome adminHome;
   private WebLogicMBean mbean;
   private MBeanInfo info;
   BeanInfoAccess beanInfoAccess;
   private static final String ADMIN_CONFIG_PKG_NAME = "weblogic.management.configuration";

   public CommandLineArgs(String[] var1) throws IllegalArgumentException {
      this((String)null, (String)null, (String)null, var1);
   }

   public CommandLineArgs(String var1, String var2, String var3, String[] var4) throws IllegalArgumentException {
      this.isIIOP = false;
      this.url = "t3://localhost:7001";
      this.username = null;
      this.password = null;
      this.userconfigfile = null;
      this.userkeyfile = null;
      this.operation = 0;
      this.pretty = false;
      this.exclude = true;
      this.verbose = false;
      this.isURLset = false;
      this.isTimeoutSet = false;
      this.attribList = new AttributeList();
      this.domainName = null;
      this.newMBeanName = null;
      this.commoType = false;
      this.batchFile = null;
      this.continueOnErr = false;
      this.showNoMessages = false;
      this.batchCmd = false;
      this.clusterName = null;
      this.configPath = null;
      this.adminUrl = null;
      this.ignoreSessions = false;
      this.timeout = 0;
      this.batchCmdVerbose = false;
      this.serverPropertiesDir = null;
      this.clusterPropertiesDir = null;
      this.outputDir = null;
      this.serverName = null;
      this.newDomainName = null;
      this.usernamePwdLocation = null;
      this.listenAdress = null;
      this.listenPort = null;
      this.listenPortSecure = false;
      this.usingBootProperties = false;
      this.poolName = null;
      this.noExit = false;
      this.positionalArgs = new Vector();
      this.adminHome = null;
      this.mbean = null;
      this.info = null;
      this.beanInfoAccess = null;
      this.username = var1;
      this.password = var2;
      if (var3 != null) {
         this.url = var3;
      }

      this.processArgs(var4);
      this.postProcessArgs();
   }

   public boolean continueOnError() {
      return this.continueOnErr;
   }

   public boolean isBatchCmdVerbose() {
      return this.batchCmdVerbose;
   }

   public boolean showNoMessages() {
      return this.showNoMessages;
   }

   public boolean exclude() {
      return this.exclude;
   }

   public boolean isIIOP() {
      return this.isIIOP;
   }

   public boolean isListenPortSecure() {
      return this.listenPortSecure;
   }

   public String getListenPort() {
      return this.listenPort;
   }

   public String getListenAddress() {
      return this.listenAdress;
   }

   public void setUrl(String var1) {
      this.url = var1;
   }

   public String getAdminUrl() {
      if (this.adminUrl != null) {
         int var1 = this.adminUrl.indexOf("//");
         if (var1 > 0 && this.adminUrl.charAt(var1 - 1) == ':') {
            return this.adminUrl;
         } else {
            return var1 == 0 ? "t3:" + this.adminUrl : "t3://" + this.adminUrl;
         }
      } else {
         return this.adminUrl;
      }
   }

   public String getUsernamePasswordLocation() {
      return this.usernamePwdLocation;
   }

   public CommandDescription getCommandDescription(String var1) {
      return (CommandDescription)cmds.get(var1.toUpperCase());
   }

   public String toString() {
      return "url: " + this.url + "\nusername: " + this.username + "\npassword: " + this.password + "\noperation: " + this.operation + "\nmBeanObjName: " + this.mBeanObjName + "\nmbeanType: " + this.mbeanType + "\nnewMBeanName: " + this.newMBeanName + "\npretty: " + this.pretty + "\nexclude: " + this.exclude + "\nattribList: " + this.getStringAttribList(this.attribList) + "\nmethodName: " + this.methodName + "\ndomainName: " + this.domainName + "\nmethodArguments: " + this.methodArguments;
   }

   static void addCmd(String var0, int var1, String var2, String var3, boolean var4, String var5, String var6) {
      CommandDescription var7 = new CommandDescription(var0, var1, var2, var3, var4, var5, var6, "");
      cmds.put(var0, var7);
   }

   static void addCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6) {
      CommandDescription var7 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6);
      cmds.put(var0, var7);
   }

   static void addCmd(String var0, int var1, String var2, String var3, String var4, String var5) {
      CommandDescription var6 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, "");
      cmds.put(var0, var6);
   }

   static void addInfoCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7) {
      CommandDescription var8 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7);
      infoCmds.put(var0, var8);
      cmds.put(var0, var8);
   }

   static void addInfoCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8) {
      CommandDescription var9 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7, var8);
      infoCmds.put(var0, var9);
      cmds.put(var0, var9);
   }

   static void addServerSpecificCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7) {
      CommandDescription var8 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7);
      serverSpecificCmds.put(var0, var8);
      cmds.put(var0, var8);
   }

   static void addServerSpecificCmd(String var0, int var1, String var2, String var3, boolean var4, String var5, String var6, String var7, int var8) {
      CommandDescription var9 = new CommandDescription(var0, var1, var2, var3, var4, var5, var6, var7, var8);
      serverSpecificCmds.put(var0, var9);
      cmds.put(var0, var9);
   }

   static void addServerSpecificCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8) {
      CommandDescription var9 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7, var8);
      serverSpecificCmds.put(var0, var9);
      cmds.put(var0, var9);
   }

   static void addPoolCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7) {
      CommandDescription var8 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7);
      poolCmds.put(var0, var8);
      cmds.put(var0, var8);
   }

   static void addPoolCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8) {
      CommandDescription var9 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7, var8);
      poolCmds.put(var0, var9);
      cmds.put(var0, var9);
   }

   static void addMBeanCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7) {
      CommandDescription var8 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7);
      mbeanCmds.put(var0, var8);
      cmds.put(var0, var8);
   }

   static void addMBeanCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8) {
      CommandDescription var9 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7, var8);
      mbeanCmds.put(var0, var9);
      cmds.put(var0, var9);
   }

   static void addClusterCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7) {
      CommandDescription var8 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7);
      clusterCmds.put(var0, var8);
      cmds.put(var0, var8);
   }

   static void addClusterCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8) {
      CommandDescription var9 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7, var8);
      clusterCmds.put(var0, var9);
      cmds.put(var0, var9);
   }

   static void addAdminConfigCmd(String var0, int var1, String var2, String var3, String var4, String var5, String var6, int var7) {
      CommandDescription var8 = new CommandDescription(var0, var1, var2, var3, true, var4, var5, var6, var7);
      adminConfigCmds.put(var0, var8);
      cmds.put(var0, var8);
   }

   boolean populateOperation(String[] var1, int var2, String var3) throws ANFE {
      String var4 = null;
      if (var1[var2].startsWith("-")) {
         var4 = var1[var2].substring(1);
      } else {
         var4 = var1[var2];
      }

      if (var4.equalsIgnoreCase(var3)) {
         this.operation = ((CommandDescription)cmds.get(var3.toUpperCase())).getCommandId();
         if (this.operation == 20 && var1.length > 1) {
            if (var1[1].startsWith("-")) {
               var4 = var1[1].substring(1);
            } else {
               var4 = var1[1];
            }
         }

         operationString = var4;
         return true;
      } else {
         throw new ANFE();
      }
   }

   void postProcessArgs() throws IllegalArgumentException {
      ManagementTextTextFormatter var1;
      if (33 != this.getOperation() && 34 != this.getOperation()) {
         if (null != this.getNewMBeanName()) {
            var1 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var1.getPostProcessArgsMBeanNameError(this.getNewMBeanName()));
         }

         if (null != this.getDomainName()) {
            var1 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var1.getPostProcessArgsDomainNameError(this.getDomainName()));
         }
      }

      if (this.getOperation() == 50) {
         if (this.getMigratableTargetName() == null && this.getSingletonServiceName() == null) {
            var1 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var1.getPostProcessArgsTargetError());
         }

         if (this.getDestinationServerName() == null) {
            var1 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var1.getPostProcessArgsDestinationError());
         }
      }

      if (this.getOperation() == 52) {
         if (this.getMigratableServerName() == null) {
            var1 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var1.getPostProcessArgsMigratableServerError());
         }

         if (this.getDestinationMachineName() == null) {
            var1 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var1.getPostProcessArgsDestinationMachineError());
         }
      }

   }

   void processArgs(String[] var1) throws IllegalArgumentException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].equalsIgnoreCase("commotype")) {
            this.commoType = true;
            break;
         }
      }

      int var3 = 0;
      Object var6 = null;

      while(var3 < var1.length) {
         try {
            this.url = this.getParameter(var1, var3, "-url");
            ++var3;
            ++var3;
            if (this.url.startsWith("iiop")) {
               this.isIIOP = true;
            }

            this.isURLset = true;
         } catch (ANFE var61) {
            try {
               this.username = this.getParameter(var1, var3, "-username");
               ++var3;
               ++var3;
            } catch (ANFE var60) {
               try {
                  this.password = this.getParameter(var1, var3, "-password");
                  ++var3;
                  ++var3;
               } catch (ANFE var59) {
                  try {
                     this.mbeanType = this.getParameter(var1, var3, "-type");
                     ++var3;
                     ++var3;
                     typeMunged = false;
                  } catch (ANFE var58) {
                     try {
                        this.migratableTargetName = this.getParameter(var1, var3, "-migratabletarget");
                        ++var3;
                        ++var3;
                     } catch (ANFE var57) {
                        try {
                           this.singletonServiceName = this.getParameter(var1, var3, "-singletonservice");
                           ++var3;
                           ++var3;
                        } catch (ANFE var56) {
                           try {
                              this.migratableServerName = this.getParameter(var1, var3, "-migratableserver");
                              ++var3;
                              ++var3;
                           } catch (ANFE var55) {
                              try {
                                 this.migratableServerDestinationMachineName = this.getParameter(var1, var3, "-destinationmachine");
                                 ++var3;
                                 ++var3;
                              } catch (ANFE var54) {
                                 try {
                                    this.destinationServerName = this.getParameter(var1, var3, "-destination");
                                    ++var3;
                                    ++var3;
                                 } catch (ANFE var53) {
                                    ManagementTextTextFormatter var65;
                                    try {
                                       this.mBeanObjName = this.getParameter(var1, var3, "-mbean");
                                       ++var3;
                                       ++var3;

                                       try {
                                          if (this.mbeanType == null) {
                                             typeMunged = true;
                                             this.mbeanType = this.getTypeFromObj(this.mBeanObjName);
                                          }
                                       } catch (MalformedObjectNameException var14) {
                                          var65 = new ManagementTextTextFormatter();
                                          throw new IllegalArgumentException(var65.getProcessArgsMBeanNameError(var1[var3 - 1], var3 + 1 - 1));
                                       }
                                    } catch (ANFE var52) {
                                       try {
                                          this.domainName = this.getParameter(var1, var3, "-domain");
                                          ++var3;
                                          ++var3;
                                       } catch (ANFE var51) {
                                          try {
                                             this.newMBeanName = this.getParameter(var1, var3, "-name");
                                             ++var3;
                                             ++var3;
                                          } catch (ANFE var50) {
                                             try {
                                                this.getFlag(var1, var3, "commotype");
                                                ++var3;
                                             } catch (ANFE var49) {
                                                try {
                                                   this.pretty = this.getFlag(var1, var3, "pretty");
                                                   ++var3;
                                                } catch (ANFE var48) {
                                                   try {
                                                      this.exclude = !this.getFlag(var1, var3, "include");
                                                      ++var3;
                                                   } catch (ANFE var47) {
                                                      try {
                                                         this.verbose = this.getFlag(var1, var3, "verbose");
                                                         ++var3;
                                                      } catch (ANFE var46) {
                                                         try {
                                                            this.migrateJTA = this.getFlag(var1, var3, "jta");
                                                            ++var3;
                                                         } catch (ANFE var45) {
                                                            try {
                                                               this.sourceDown = this.getFlag(var1, var3, "sourcedown");
                                                               ++var3;
                                                            } catch (ANFE var44) {
                                                               try {
                                                                  this.destinationDown = this.getFlag(var1, var3, "destinationdown");
                                                                  ++var3;
                                                               } catch (ANFE var43) {
                                                                  try {
                                                                     this.pattern = this.getParameter(var1, var3, "-pattern");
                                                                     ++var3;
                                                                     ++var3;
                                                                  } catch (ANFE var42) {
                                                                     try {
                                                                        this.batchFile = this.getParameter(var1, var3, "-batchFile");
                                                                        ++var3;
                                                                        ++var3;
                                                                     } catch (ANFE var41) {
                                                                        try {
                                                                           this.continueOnErr = this.getFlag(var1, var3, "continueOnError");
                                                                           ++var3;
                                                                        } catch (ANFE var40) {
                                                                           try {
                                                                              this.showNoMessages = this.getFlag(var1, var3, "showNoMessages");
                                                                              ++var3;
                                                                           } catch (ANFE var39) {
                                                                              try {
                                                                                 this.batchCmd = this.getFlag(var1, var3, "batchCmd");
                                                                                 ++var3;
                                                                              } catch (ANFE var38) {
                                                                                 try {
                                                                                    this.batchCmdVerbose = this.getFlag(var1, var3, "batchCmdVerbose");
                                                                                    ++var3;
                                                                                 } catch (ANFE var37) {
                                                                                    try {
                                                                                       this.listenPortSecure = this.getFlag(var1, var3, "listenPortSecure");
                                                                                       ++var3;
                                                                                    } catch (ANFE var36) {
                                                                                       try {
                                                                                          this.clusterName = this.getParameter(var1, var3, "-clusterName");
                                                                                          ++var3;
                                                                                          ++var3;
                                                                                       } catch (ANFE var35) {
                                                                                          try {
                                                                                             this.configPath = this.getParameter(var1, var3, "-configPath");
                                                                                             ++var3;
                                                                                             ++var3;
                                                                                          } catch (ANFE var34) {
                                                                                             try {
                                                                                                this.getFlag(var1, var3, "-ignoreExistingSessions".substring(1));
                                                                                                this.ignoreSessions = true;
                                                                                                ++var3;
                                                                                             } catch (ANFE var33) {
                                                                                                try {
                                                                                                   this.timeout = this.getInt(var1, var3, "-timeout");
                                                                                                   ++var3;
                                                                                                   ++var3;
                                                                                                   this.isTimeoutSet = true;
                                                                                                } catch (ANFE var32) {
                                                                                                   try {
                                                                                                      this.adminUrl = this.getParameter(var1, var3, "-adminUrl");
                                                                                                      if (!this.isURLset && this.adminUrl.startsWith("iiop")) {
                                                                                                         this.isIIOP = true;
                                                                                                      }

                                                                                                      ++var3;
                                                                                                      ++var3;
                                                                                                   } catch (ANFE var31) {
                                                                                                      try {
                                                                                                         this.serverPropertiesDir = this.getParameter(var1, var3, "-serverPropDir");
                                                                                                         ++var3;
                                                                                                         ++var3;
                                                                                                      } catch (ANFE var30) {
                                                                                                         try {
                                                                                                            this.clusterPropertiesDir = this.getParameter(var1, var3, "-clusterPropDir");
                                                                                                            ++var3;
                                                                                                            ++var3;
                                                                                                         } catch (ANFE var29) {
                                                                                                            try {
                                                                                                               this.newDomainName = this.getParameter(var1, var3, "-newDomainName");
                                                                                                               ++var3;
                                                                                                               ++var3;
                                                                                                            } catch (ANFE var28) {
                                                                                                               try {
                                                                                                                  this.poolName = this.getParameter(var1, var3, "-poolName");
                                                                                                                  ++var3;
                                                                                                                  ++var3;
                                                                                                               } catch (ANFE var27) {
                                                                                                                  try {
                                                                                                                     this.serverName = this.getParameter(var1, var3, "-serverName");
                                                                                                                     ++var3;
                                                                                                                     ++var3;
                                                                                                                  } catch (ANFE var26) {
                                                                                                                     try {
                                                                                                                        this.outputDir = this.getParameter(var1, var3, "-outputDir");
                                                                                                                        ++var3;
                                                                                                                        ++var3;
                                                                                                                     } catch (ANFE var25) {
                                                                                                                        try {
                                                                                                                           this.usernamePwdLocation = this.getParameter(var1, var3, "-usernamePwdLocation");
                                                                                                                           ++var3;
                                                                                                                           ++var3;
                                                                                                                        } catch (ANFE var24) {
                                                                                                                           try {
                                                                                                                              this.listenAdress = this.getParameter(var1, var3, "-listenAddress");
                                                                                                                              ++var3;
                                                                                                                              ++var3;
                                                                                                                           } catch (ANFE var23) {
                                                                                                                              try {
                                                                                                                                 this.listenPort = this.getParameter(var1, var3, "-listenPort");
                                                                                                                                 ++var3;
                                                                                                                                 ++var3;
                                                                                                                              } catch (ANFE var22) {
                                                                                                                                 try {
                                                                                                                                    this.noExit = this.getFlag(var1, var3, "noExit");
                                                                                                                                    ++var3;
                                                                                                                                 } catch (ANFE var21) {
                                                                                                                                    ManagementTextTextFormatter var62;
                                                                                                                                    try {
                                                                                                                                       String var4 = this.getParameter(var1, var3, "-property");
                                                                                                                                       ++var3;
                                                                                                                                       if (32 == this.operation) {
                                                                                                                                          if (null == this.mbeanType) {
                                                                                                                                             this.commoType = true;
                                                                                                                                          }

                                                                                                                                          ++var3;
                                                                                                                                          String var5 = null;
                                                                                                                                          if (var3 >= var1.length) {
                                                                                                                                             var62 = new ManagementTextTextFormatter();
                                                                                                                                             throw new IllegalArgumentException(var62.getProcessArgsPropError(var4));
                                                                                                                                          }

                                                                                                                                          var5 = var1[var3];

                                                                                                                                          for(int var63 = var3; var63 < var1.length - 1 && !var1[var63 + 1].equals("-property") && !var1[var63 + 1].equals("-pretty") && !var1[var63 + 1].equals("-commotype"); ++var63) {
                                                                                                                                             var5 = var5 + " " + var1[var63 + 1];
                                                                                                                                             ++var3;
                                                                                                                                          }

                                                                                                                                          if (var5 != null && var5.startsWith("\"") && var5.endsWith("\"")) {
                                                                                                                                             var5 = var5.substring(1, var5.length());
                                                                                                                                             StringBuffer var64 = new StringBuffer(var5);
                                                                                                                                             var64.deleteCharAt(var5.length() - 1);
                                                                                                                                             var5 = var64.toString();
                                                                                                                                          }

                                                                                                                                          try {
                                                                                                                                             if (!this.getCommoType()) {
                                                                                                                                                var6 = this.getValueObjectFromString(var4, var5, this.mbeanType);
                                                                                                                                             } else {
                                                                                                                                                var6 = var5;
                                                                                                                                             }
                                                                                                                                          } catch (InstanceNotFoundException var11) {
                                                                                                                                             new ManagementTextTextFormatter();
                                                                                                                                             throw new IllegalArgumentException("Could not find the instance " + var11);
                                                                                                                                          } catch (IllegalArgumentException var12) {
                                                                                                                                             var65 = new ManagementTextTextFormatter();
                                                                                                                                             throw new IllegalArgumentException(var65.getProcessArgsPropNameValError(var5, var4) + var12);
                                                                                                                                          } catch (ConfigurationError var13) {
                                                                                                                                             var6 = var5;
                                                                                                                                             this.commoType = true;
                                                                                                                                          }
                                                                                                                                       } else if (null == var6) {
                                                                                                                                          var6 = new Object();
                                                                                                                                       }

                                                                                                                                       Attribute var7 = new Attribute(var4, var6);
                                                                                                                                       this.attribList.add(var7);
                                                                                                                                       ++var3;
                                                                                                                                    } catch (ANFE var20) {
                                                                                                                                       try {
                                                                                                                                          this.methodName = this.getParameter(var1, var3, "-method");
                                                                                                                                          ++var3;
                                                                                                                                          this.methodArguments = new Vector();

                                                                                                                                          while(var3 < var1.length - 1) {
                                                                                                                                             ++var3;
                                                                                                                                             this.methodArguments.addElement(var1[var3]);
                                                                                                                                          }

                                                                                                                                          ++var3;
                                                                                                                                       } catch (ANFE var19) {
                                                                                                                                          try {
                                                                                                                                             this.userconfigfile = this.getParameter(var1, var3, "-userconfigfile");
                                                                                                                                             this.setUAndPFromConfigFile();
                                                                                                                                             ++var3;
                                                                                                                                             ++var3;
                                                                                                                                          } catch (ANFE var18) {
                                                                                                                                             try {
                                                                                                                                                this.userkeyfile = this.getParameter(var1, var3, "-userkeyfile");
                                                                                                                                                this.setUAndPFromConfigFile();
                                                                                                                                                ++var3;
                                                                                                                                                ++var3;
                                                                                                                                             } catch (ANFE var17) {
                                                                                                                                                try {
                                                                                                                                                   this.sourceServer = this.getParameter(var1, var3, "-server");
                                                                                                                                                   ++var3;
                                                                                                                                                   ++var3;
                                                                                                                                                } catch (ANFE var16) {
                                                                                                                                                   if (0 == this.operation) {
                                                                                                                                                      Enumeration var8 = cmds.keys();

                                                                                                                                                      while(var8.hasMoreElements()) {
                                                                                                                                                         String var9 = (String)var8.nextElement();

                                                                                                                                                         try {
                                                                                                                                                            this.populateOperation(var1, var3, var9);
                                                                                                                                                            ++var3;
                                                                                                                                                            break;
                                                                                                                                                         } catch (ANFE var15) {
                                                                                                                                                         }
                                                                                                                                                      }

                                                                                                                                                      if (0 != this.operation) {
                                                                                                                                                         continue;
                                                                                                                                                      }
                                                                                                                                                   }

                                                                                                                                                   if (0 != this.operation && 30 > this.operation) {
                                                                                                                                                      this.positionalArgs.add(var1[var3]);
                                                                                                                                                      ++var3;
                                                                                                                                                   } else {
                                                                                                                                                      if (0 == this.operation || 38 != this.operation && 48 != this.operation) {
                                                                                                                                                         var62 = new ManagementTextTextFormatter();
                                                                                                                                                         throw new IllegalArgumentException(var62.getProcessArgsCmdLineError(var1[var3], var3 + 1));
                                                                                                                                                      }

                                                                                                                                                      this.positionalArgs.add(var1[var3]);
                                                                                                                                                      ++var3;
                                                                                                                                                   }
                                                                                                                                                }
                                                                                                                                             }
                                                                                                                                          }
                                                                                                                                       }
                                                                                                                                    }
                                                                                                                                 }
                                                                                                                              }
                                                                                                                           }
                                                                                                                        }
                                                                                                                     }
                                                                                                                  }
                                                                                                               }
                                                                                                            }
                                                                                                         }
                                                                                                      }
                                                                                                   }
                                                                                                }
                                                                                             }
                                                                                          }
                                                                                       }
                                                                                    }
                                                                                 }
                                                                              }
                                                                           }
                                                                        }
                                                                     }
                                                                  }
                                                               }
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private static String replacePrefix(String var0, String var1, String var2) {
      return var0.toLowerCase().startsWith(var1) ? var2 + var0.substring(var1.length()) : var0;
   }

   public static String getOperationString() {
      return operationString;
   }

   public static String getUsageString() {
      return getUsageString(false);
   }

   public static String getUsageString(boolean var0) {
      return null != getOperationString() && !getOperationString().equalsIgnoreCase("help") ? getUsageString(getOperationString(), var0) : getGenericUsage();
   }

   public static String getGenericUsage() {
      ManagementTextTextFormatter var0 = new ManagementTextTextFormatter();
      System.out.println("\n" + var0.getAdminDescr());
      System.out.println("\n\t" + var0.getAdmin());
      System.out.println("\n\t" + var0.getInfo());
      System.out.println("\n\t" + var0.getJdbc());
      System.out.println("\n\t" + var0.getMbean());
      System.out.println("\n\t" + var0.getCluster());
      System.out.println("\n\t" + var0.getAdminConfig());
      System.out.println("\n\t" + var0.getAll());
      System.out.println("\n" + var0.getGenUsage1());
      System.out.println("\n" + var0.getHelpUrl());
      return "";
   }

   public static String getUsageString(String var0, boolean var1) {
      ManagementTextTextFormatter var2 = new ManagementTextTextFormatter();
      String var3 = null;
      if (var1) {
         var3 = "\n" + var2.getGenUserConfigUsage1();
      } else {
         var3 = "\n" + var2.getGenUsage1();
      }

      String var4 = "\n" + var2.getInfoStr();
      String var5 = "\n" + var2.getServerAdmin();
      String var6 = "\n" + var2.getPoolStr();
      String var7 = "\n" + var2.getMbeanStr();
      String var8 = "\n" + var2.getClusterStr();
      String var9 = "\n" + var2.getAdminConfigStr();
      String var10 = "";
      String var11 = "";
      String var12 = "";
      String var13 = "";
      String var14 = "";
      String var15 = "";
      CommandDescription var16 = null;
      String var17 = "\n\n********** SSL Trust Options **********";
      var17 = var17 + "\n\nIf the domain-wide administration port is enabled, or if you are";
      var17 = var17 + "\n connecting to a server through some other SSL port, you might need";
      var17 = var17 + "\nto include Java options to indicate which host the weblogic.Admin";
      var17 = var17 + "\nutility trusts. For example, if the server to which you are connecting";
      var17 = var17 + "\nis using the demonstration SSL keys and certificates, you must include";
      var17 = var17 + "\nthe TrustKeyStore option as follows:";
      var17 = var17 + "\njava -Dweblogic.security.TrustKeyStore=DemoTrust weblogic.Admin <...>";
      var17 = var17 + "\nFor more information, refer to the WebLogic Server security documentation.";
      String var18 = "\njava -Dweblogic.security.TrustKeyStore=DemoTrust ";
      var18 = var18 + "weblogic.Admin -url t3s://localhost:7002 -username weblogic -password weblogic ";
      String var19 = "\nConnecting through an SSL listen port on a server that is using the";
      var19 = var19 + "\ndemonstration SSL keys and certificates:";
      String var20;
      if (null != var0 && !var0.equals("")) {
         if (var0.startsWith("-")) {
            var20 = var0.substring(1).toUpperCase();
         } else {
            var20 = var0.toUpperCase();
         }

         var16 = (CommandDescription)cmds.get(var20);
      }

      String var21;
      if (null != var16 && var16.getCommandId() != 100) {
         var20 = var2.getGenUsage2();
         CommandDescription var22;
         String var23;
         Enumeration var25;
         if (var16.getCommandId() == 101) {
            var25 = infoCmds.elements();

            while(var25.hasMoreElements()) {
               var22 = (CommandDescription)var25.nextElement();
               if (var22.isExpose()) {
                  var23 = var22.getSyntax();
                  var10 = var10 + "\n\t" + var23;
               }
            }

            return "\n" + var4 + "\n" + var3 + "\n" + var20 + "\n" + var10 + var17 + var2.getgetHelp();
         } else if (var16.getCommandId() == 102) {
            var25 = serverSpecificCmds.elements();

            while(var25.hasMoreElements()) {
               var22 = (CommandDescription)var25.nextElement();
               if (var22.isExpose()) {
                  var23 = var22.getSyntax();
                  if (var23.length() != 0) {
                     var12 = var12 + "\n\t" + var23;
                  }
               }
            }

            return "\n" + var5 + "\n" + var3 + "\n" + var20 + "\n" + var12 + var17 + var2.getgetHelp();
         } else if (var16.getCommandId() == 103) {
            var25 = poolCmds.elements();

            while(var25.hasMoreElements()) {
               var22 = (CommandDescription)var25.nextElement();
               if (var22.isExpose()) {
                  var23 = var22.getSyntax();
                  var13 = var13 + "\n\t" + var23;
               }
            }

            return "\n" + var6 + "\n" + var3 + "\n" + var20 + "\n" + var13 + var17 + var2.getgetHelp();
         } else if (var16.getCommandId() == 104) {
            var25 = mbeanCmds.elements();

            while(var25.hasMoreElements()) {
               var22 = (CommandDescription)var25.nextElement();
               if (var22.isExpose()) {
                  var23 = var22.getSyntax();
                  var11 = var11 + "\n\t" + var23;
               }
            }

            return "\n" + var7 + "\n" + var3 + "\n" + var20 + "\n" + var11 + var17 + var2.getgetHelp();
         } else if (var16.getCommandId() == 105) {
            var25 = clusterCmds.elements();

            while(var25.hasMoreElements()) {
               var22 = (CommandDescription)var25.nextElement();
               if (var22.isExpose()) {
                  var23 = var22.getSyntax();
                  var14 = var14 + "\n\t" + var23;
               }
            }

            return "\n" + var8 + "\n" + var3 + "\n" + var20 + "\n" + var14 + var17 + var2.getgetHelp();
         } else if (var16.getCommandId() == 106) {
            var25 = adminConfigCmds.elements();

            while(var25.hasMoreElements()) {
               var22 = (CommandDescription)var25.nextElement();
               if (var22.isExpose()) {
                  var23 = var22.getSyntax();
                  var15 = var15 + "\n\t" + var23;
               }
            }

            return "\n" + var9 + "\n" + var3 + "\n" + var20 + "\n" + var15 + var17 + var2.getgetHelp();
         } else {
            var21 = var16.getInfoMessage();
            if (var21 != "") {
               var3 = "\n ***** " + var21 + " *****" + "\n\n" + var2.getDesr() + "\n" + var16.getDescription();
            } else {
               var3 = "\n" + var2.getDesr() + "\n" + var16.getDescription();
            }

            var3 = var3 + "\n\n" + (var1 ? var2.getCommUserConfigUsage() : var2.getCommUsage()) + "\t" + var16.getSyntax();
            if (var16.getArgumentDescription().length() != 0) {
               var3 = var3 + "\n\n" + var2.getWhere() + "\n" + var16.getArgumentDescription();
            }

            var3 = var3 + "\n\n" + var2.getExamples() + "\n" + var16.getExample();
            var3 = var3 + "\n" + var19 + var18 + var16.getSyntax();
            return var3;
         }
      } else {
         var3 = var3 + "\n\n" + var2.getGenUsage2();
         Enumeration var24 = infoCmds.elements();

         while(var24.hasMoreElements()) {
            var16 = (CommandDescription)var24.nextElement();
            if (var16.isExpose()) {
               var21 = var16.getSyntax();
               var10 = var10 + "\n\t" + var21;
            }
         }

         var24 = serverSpecificCmds.elements();

         while(var24.hasMoreElements()) {
            var16 = (CommandDescription)var24.nextElement();
            if (var16.isExpose()) {
               var21 = var16.getSyntax();
               if (var21.length() != 0) {
                  var12 = var12 + "\n\t" + var21;
               }
            }
         }

         var24 = poolCmds.elements();

         while(var24.hasMoreElements()) {
            var16 = (CommandDescription)var24.nextElement();
            if (var16.isExpose()) {
               var21 = var16.getSyntax();
               var13 = var13 + "\n\t" + var21;
            }
         }

         var24 = mbeanCmds.elements();

         while(var24.hasMoreElements()) {
            var16 = (CommandDescription)var24.nextElement();
            if (var16.isExpose()) {
               var21 = var16.getSyntax();
               var11 = var11 + "\n\t" + var21;
            }
         }

         var24 = clusterCmds.elements();

         while(var24.hasMoreElements()) {
            var16 = (CommandDescription)var24.nextElement();
            if (var16.isExpose()) {
               var21 = var16.getSyntax();
               var14 = var14 + "\n\t" + var21;
            }
         }

         var24 = adminConfigCmds.elements();

         while(var24.hasMoreElements()) {
            var16 = (CommandDescription)var24.nextElement();
            if (var16.isExpose()) {
               var21 = var16.getSyntax();
               var15 = var15 + "\n\t" + var21;
            }
         }

         var20 = var3 + "\n" + var5 + "\n" + var12 + "\n" + var4 + "\n" + var10 + "\n" + var6 + "\n" + var13 + "\n" + var7 + "\n" + var11 + "\n" + var8 + "\n" + var14 + "\n" + var9 + "\n" + var15;
         var20 = var20 + var17;
         var21 = var2.getgetHelp();
         return var20 + var21;
      }
   }

   public String getMBeanType() {
      return this.mbeanType;
   }

   public String getMBeanObjName() {
      return this.mBeanObjName;
   }

   public String getNewMBeanName() {
      return this.newMBeanName;
   }

   public String getDomainName() {
      return this.domainName;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public String getMigratableTargetName() {
      return this.migratableTargetName;
   }

   public String getSingletonServiceName() {
      return this.singletonServiceName;
   }

   public String getMigratableServerName() {
      return this.migratableServerName;
   }

   public String getDestinationMachineName() {
      return this.migratableServerDestinationMachineName;
   }

   public String getSourceServerName() {
      return this.sourceServer;
   }

   public String getDestinationServerName() {
      return this.destinationServerName;
   }

   public boolean getMigrateJTA() {
      return this.migrateJTA;
   }

   public boolean getIgnoreSessions() {
      return this.ignoreSessions;
   }

   public int getTimeout() {
      return this.timeout;
   }

   public boolean getSourceDown() {
      return this.sourceDown;
   }

   public boolean getDestinationDown() {
      return this.destinationDown;
   }

   public boolean getCommoType() {
      return this.commoType;
   }

   public String getObjectNamePattern() {
      return this.pattern;
   }

   public String getBatchFileName() {
      return this.batchFile;
   }

   public String getClusterName() {
      return this.clusterName;
   }

   public String getPoolName() {
      return this.poolName;
   }

   public String getConfigPath() {
      return this.configPath;
   }

   public boolean isBatchCmd() {
      return this.batchCmd;
   }

   public String getURL() {
      if (this.url != null) {
         int var1 = this.url.indexOf("//");
         if (var1 > 0 && this.url.charAt(var1 - 1) == ':') {
            return this.url;
         } else {
            return var1 == 0 ? "t3:" + this.url : "t3://" + this.url;
         }
      } else {
         return "t3://localhost:7001";
      }
   }

   public String getT3URL() {
      return replacePrefix(this.getURL(), "http", "t3");
   }

   public String getHttpURL() {
      return replacePrefix(this.getURL(), "t3", "http");
   }

   public String getUsername() {
      return Kernel.isServer() ? null : this.username;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public void setUsingBootProperties(boolean var1) {
      this.usingBootProperties = var1;
   }

   public boolean isUsingBootProperties() {
      return this.usingBootProperties;
   }

   public String getPassword() {
      if (Kernel.isServer()) {
         return null;
      } else {
         if (null == this.password) {
            if (this.username == null) {
               return null;
            }

            this.getPasswordInteractively();
         }

         return this.password;
      }
   }

   public String getUserConfig() {
      return Kernel.isServer() ? null : this.userconfigfile;
   }

   public String getUserKey() {
      return Kernel.isServer() ? null : this.userkeyfile;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public boolean isPretty() {
      return this.pretty;
   }

   public boolean isURLset() {
      return this.isURLset;
   }

   public boolean isTimeoutSet() {
      return this.isTimeoutSet;
   }

   public int getOperation() {
      return this.operation;
   }

   public String getServerPropertiesDirectory() {
      return this.serverPropertiesDir;
   }

   public String getClusterPropertiesDirectory() {
      return this.clusterPropertiesDir;
   }

   public String getOutputDirectory() {
      return this.outputDir;
   }

   public String getNewDomainName() {
      return this.newDomainName;
   }

   public String getServerName() {
      return this.serverName;
   }

   public boolean isNoExit() {
      return this.noExit;
   }

   public AttributeList getAttribList() {
      return this.attribList;
   }

   public Object[] getMethodArguments(String[] var1) throws IllegalArgumentException {
      Object[] var2 = new Object[var1.length];
      if (var1.length != this.methodArguments.size()) {
         ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
         throw new IllegalArgumentException(var4.getMethodArgumentsError(this.getMethodName(), var1.length, this.methodArguments.size()));
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = this.getObjectFromString(var1[var3], (String)this.methodArguments.elementAt(var3));
         }

         return var2;
      }
   }

   public String getPositionalArg(int var1) {
      return (String)this.positionalArgs.get(var1);
   }

   public int getPositionalArgCount() {
      return this.positionalArgs.size();
   }

   boolean getFlag(String[] var1, int var2, String var3) throws ANFE {
      String var4 = null;
      if (var1[var2].startsWith("-")) {
         var4 = var1[var2].substring(1);
      } else {
         var4 = var1[var2];
      }

      if (var4.equalsIgnoreCase(var3)) {
         return true;
      } else {
         throw new ANFE();
      }
   }

   String getParameter(String[] var1, int var2, String var3) throws IllegalArgumentException, ANFE {
      if (var1[var2].equalsIgnoreCase(var3)) {
         if (var2 < var1.length - 1) {
            return var1[var2 + 1];
         } else {
            ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var4.getParameterError(var2 + 1, var3));
         }
      } else {
         throw new ANFE();
      }
   }

   int getInt(String[] var1, int var2, String var3) throws IllegalArgumentException, ANFE {
      try {
         return Integer.parseInt(this.getParameter(var1, var2, var3));
      } catch (NumberFormatException var6) {
         ManagementTextTextFormatter var5 = new ManagementTextTextFormatter();
         throw new IllegalArgumentException(var5.getParameterError(var2 + 1, var3));
      }
   }

   String getStringAttribList(AttributeList var1) {
      Object[] var2 = var1.toArray();
      String var4 = "";

      for(int var5 = 0; var5 < var2.length; ++var5) {
         Attribute var3 = (Attribute)var2[var5];
         var4 = var4 + var3.getName() + "=" + var3.getValue() + "\n";
      }

      return var4;
   }

   String getTypeFromObj(String var1) throws MalformedObjectNameException {
      WebLogicObjectName var5 = new WebLogicObjectName(var1);
      String var3 = var5.getType();
      return var3 != null ? var3 : null;
   }

   void getPasswordInteractively() {
      ManagementTextTextFormatter var2;
      if (TerminalIO.isNoEchoAvailable()) {
         try {
            ManagementTextTextFormatter var1 = new ManagementTextTextFormatter();
            System.out.print(var1.getPwdInteractively(this.username));
            this.password = TerminalIO.readTerminalNoEcho();
            System.out.println("\n");
         } catch (Error var4) {
            var2 = new ManagementTextTextFormatter();
            System.err.println(var2.getPwdInteractivelyError());
         }
      } else {
         try {
            BufferedReader var5 = new BufferedReader(new InputStreamReader(System.in));
            var2 = new ManagementTextTextFormatter();
            System.out.print(var2.getPwdInteractively(this.username));
            this.password = var5.readLine();
         } catch (Exception var3) {
            var2 = new ManagementTextTextFormatter();
            System.err.println(var2.getPwdInteractivelyError());
         }
      }

   }

   private Object getValueObjectFromString(String var1, String var2, String var3) throws IllegalArgumentException, InstanceNotFoundException, ConfigurationError {
      BeanInfoAccess var4;
      try {
         if (this.mBeanObjName != null) {
            this.adminHome = AdminToolHelper.getAdminMBeanHome(this);
            this.mbean = this.adminHome.getMBean(new WebLogicObjectName(this.mBeanObjName));
            this.info = MBeanCommandLineInvoker.getMBeanInfo(this.mbean);
            this.adminHome = null;
         } else {
            var4 = ManagementServiceClient.getBeanInfoAccess();
            BeanInfo var5 = var4.getBeanInfoForInterface("weblogic.management.configuration." + var3 + "MBean", false, "9.0.0");
            PropertyDescriptor[] var6 = var5.getPropertyDescriptors();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               PropertyDescriptor var8 = var6[var7];
               if (var8.getName().equals(var1)) {
                  return this.getObjectFromString(var8.getPropertyType().getName(), var2);
               }
            }
         }
      } catch (InstanceNotFoundException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new ConfigurationError(var10);
      }

      var4 = null;
      MBeanAttributeInfo[] var11 = this.info.getAttributes();

      for(int var13 = 0; var13 < var11.length; ++var13) {
         if (var11[var13].getName().equals(var1)) {
            String var12 = var11[var13].getType();
            return this.getObjectFromString(var12, var2);
         }
      }

      ManagementTextTextFormatter var14 = new ManagementTextTextFormatter();
      throw new IllegalArgumentException(var14.getValueObjectFromStringError(var1, var3));
   }

   private Object getObjectFromString(String var1, String var2) throws IllegalArgumentException {
      Object var7 = null;
      Object var8 = null;
      boolean var3;
      String var4;
      StringTokenizer var5;
      if (var1.startsWith("[L") && var1.endsWith(";")) {
         var3 = true;
         var4 = var1.substring("[L".length(), var1.lastIndexOf(";"));
         var5 = new StringTokenizer(var2, ";");
         if (var4.startsWith("weblogic.management") && var4.endsWith("MBean")) {
            var8 = new WebLogicObjectName[var5.countTokens()];
         } else {
            try {
               Class var9 = Class.forName(var4);
               var8 = (Object[])((Object[])Array.newInstance(var9, var5.countTokens()));
            } catch (NegativeArraySizeException var11) {
            } catch (ClassNotFoundException var12) {
               ManagementTextTextFormatter var10 = new ManagementTextTextFormatter();
               throw new IllegalArgumentException(var10.getObjectFromStringError(var2, var1) + var12);
            }
         }
      } else {
         var3 = false;
         var4 = var1;
         var5 = new StringTokenizer(var2, "");
      }

      for(int var13 = 0; var5.hasMoreTokens(); ((Object[])var8)[var13++] = var7) {
         String var6 = (String)var5.nextElement();
         var7 = this.getOneObjectFromString(var4, var6);
         if (!var3) {
            return var7;
         }
      }

      return var8;
   }

   private Object getOneObjectFromString(String var1, String var2) throws IllegalArgumentException {
      if (var1.equals("int")) {
         Integer var19 = new Integer(var2);
         return var19;
      } else if (var1.equals("java.lang.String")) {
         return var2;
      } else if (!var1.equals("java.util.Properties") && !var1.equals("java.util.Map")) {
         if (var1.equals("boolean")) {
            Boolean var18 = new Boolean(var2);
            return var18;
         } else if (var1.equals("long")) {
            Long var17 = new Long(var2);
            return var17;
         } else if (var1.startsWith("weblogic.management") && var1.endsWith("MBean")) {
            try {
               WebLogicObjectName var16 = new WebLogicObjectName(var2);
               return var16;
            } catch (MalformedObjectNameException var8) {
               throw new IllegalArgumentException(var8.toString());
            }
         } else {
            ManagementTextTextFormatter var4;
            try {
               Class var15 = Class.forName(var1);
               Class[] var20 = new Class[]{Class.forName("java.lang.String")};
               Constructor var5 = var15.getConstructor(var20);
               String[] var6 = new String[]{var2};
               Object var7 = var5.newInstance((Object[])var6);
               return var7;
            } catch (InstantiationException var9) {
               var4 = new ManagementTextTextFormatter();
               throw new IllegalArgumentException(var4.getOneObjectFromString(var2, var1) + var9);
            } catch (IllegalAccessException var10) {
               var4 = new ManagementTextTextFormatter();
               throw new IllegalArgumentException(var4.getOneObjectFromString(var2, var1) + var10);
            } catch (InvocationTargetException var11) {
               var4 = new ManagementTextTextFormatter();
               throw new IllegalArgumentException(var4.getOneObjectFromString(var2, var1) + var11);
            } catch (ClassNotFoundException var12) {
               var4 = new ManagementTextTextFormatter();
               throw new IllegalArgumentException(var4.getOneObjectFromString(var2, var1) + var12);
            } catch (NoSuchMethodException var13) {
               var4 = new ManagementTextTextFormatter();
               throw new IllegalArgumentException(var4.getOneObjectFromString(var2, var1) + var13);
            } catch (SecurityException var14) {
               var4 = new ManagementTextTextFormatter();
               throw new IllegalArgumentException(var4.getOneObjectFromString(var2, var1) + var14);
            }
         }
      } else {
         Properties var3 = new Properties();
         if (this.mbeanType == null || !this.mbeanType.equals("JDBCConnectionPool") && !this.mbeanType.equals("JDBCDataSourceFactory")) {
            TypeConversionUtils.stringToDictionary(var2, var3);
         } else {
            TypeConversionUtils.stringToDictionary(var2, var3, ";");
         }

         return var3;
      }
   }

   public MBeanHome getAdminHome() {
      return this.adminHome;
   }

   public WebLogicMBean getWebLogicMBean() {
      return this.mbean;
   }

   private void setUAndPFromConfigFile() {
      if (this.username == null && this.password == null) {
         UsernameAndPassword var1 = null;
         if (this.getUserConfig() != null && this.getUserKey() != null) {
            var1 = UserConfigFileManager.getUsernameAndPassword(this.getUserConfig(), this.getUserKey(), "weblogic.management");
            if (var1 != null && var1.isUsernameSet() && var1.isPasswordSet()) {
               this.setUsername(var1.getUsername());
               this.setPassword(new String(var1.getPassword()));
            }
         }

      }
   }

   static {
      ManagementTextTextFormatter var0 = new ManagementTextTextFormatter();
      String var1 = var0.getCommonSample();
      String var2 = var0.getPingArgs3() + "\n" + var0.getPingArgs1() + "\n" + var0.getPingArgs2();
      addCmd("INFO", 101, "", "Retrieving Information About WebLogic Server", true, "", "");
      addInfoCmd("PING", 2, var0.getPingUsage(), var0.getPingDescription(), var1 + var0.getPingExample(), "", var2, 101);
      String var3 = var0.getConnectArgs();
      addInfoCmd("CONNECT", 7, var0.getConnectUsage(), var0.getConnectDescription(), var1 + var0.getConnectSample(), "", var3, 101);
      addInfoCmd("VERSION", 6, var0.getVersionUsage(), var0.getVersionDescription(), var1 + " " + var0.getVersionSample(), "", "", 101);
      addInfoCmd("LIST", 10, var0.getListUsage(), var0.getListDescription(), var1 + " " + var0.getListSample(), "", var0.getListArgs(), 101);
      addInfoCmd("THREAD_DUMP", 18, var0.getThreadDumpUsage(), var0.getThreadDumpDescription(), var1 + " " + var0.getThreadDumpSample(), "", "", 101);
      addInfoCmd("SERVERLOG", 19, var0.getLogOperationUsage(), var0.getLogDescription(), var1 + " " + var0.getLogSample(), "", var0.getLogArgs(), 101);
      addInfoCmd("GETSTATE", 28, var0.getGetStateUsage(), var0.getGetStateDescription(), var1 + " " + var0.getGetStateSample(), "", var0.getGetStateArgs(), 101);
      addCmd("JDBC", 103, "", "Managing JDBC Connection Pools", true, "", "");
      addPoolCmd("RESET_POOL", 12, var0.getResetPoolUsage(), var0.getResetDescription(), var1 + " " + var0.getResetSample(), "", "", 103);
      addPoolCmd("CREATE_POOL", 13, var0.getCreatePoolUsage(), var0.getCreatePoolDescription(), var1 + " " + var0.getCreatePoolSample(), "", var0.getCreatePoolArgs(), 103);
      addPoolCmd("DESTROY_POOL", 48, var0.getDestroyPoolUsage(), var0.getDestroyPoolDescription(), var1 + " " + var0.getDestroyPoolSample(), "", var0.getDestroyPoolArgs(), 103);
      addPoolCmd("ENABLE_POOL", 15, var0.getEnablePoolUsage(), var0.getEnablePoolDescription(), var1 + " " + var0.getEnablePoolSample(), "", var0.getEnablePoolArgs(), 103);
      addPoolCmd("DISABLE_POOL", 16, var0.getDisablePoolUsage(), var0.getDisablePoolDescription(), var1 + " " + var0.getDisablePoolSample(), "", var0.getDisablePoolArgs(), 103);
      addPoolCmd("EXISTS_POOL", 17, var0.getExistsPoolUsage(), var0.getExistsPoolDescription(), var1 + " " + var0.getExistsPoolSample(), "", var0.getExistsPoolArgs(), 103);
      addPoolCmd("TEST_POOL", 38, var0.getTestPoolUsage1(), var0.getTestPoolDescription(), var1 + " " + var0.getTestPoolSample(), "", var0.getTestPoolArgs(), 103);
      addPoolCmd("RESUME_POOL", 46, "RESUME_POOL -poolName <connection pool name>", "Resumes the connection pool", var1 + " " + "RESUME_POOL -poolName mypool", "", "", 103);
      addPoolCmd("SHUTDOWN_POOL", 45, "SHUTDOWN_POOL -poolName <connection pool name>", "Shutsdown the connection pool", var1 + " " + "SHUTDOWN_POOL -poolName mypool", "", "", 103);
      addPoolCmd("SUSPEND_POOL", 44, "SUSPEND_POOL -poolName <connection pool name>", "Suspends the connection pool", var1 + " " + "SUSPEND_POOL -poolName mypool", "", "", 103);
      addPoolCmd("DELETE_POOL", 47, "DELETE_POOL -poolName <connection pool name>", "Deletes the connection pool after undeploying from all targets", var1 + " " + "DELETE_POOL -poolName mypool", "", "", 103);
      addCmd("LIFECYCLE", 102, "", "Managing the Server Life Cycle", true, "", "");
      addServerSpecificCmd("START", 21, var0.getStartUsage(), var0.getStartDescription(), var1 + " " + var0.getStartSample(), "", var0.getStartArgs(), 102, var0.getNeedNodeManager());
      addServerSpecificCmd("STARTINSTANDBY", 22, var0.getSTARTINSTANDBYUsage(), var0.getStartInStandByDescription(), var1 + " " + var0.getStartInStandbySample(), "", var0.getStartInStandbyArgs(), 102);
      addServerSpecificCmd("LOCK", 8, var0.getLockUsage(), var0.getLockDescription(), var1 + " " + var0.getLockSample(), "", var0.getLockArgs(), 102, var0.getNotSupported());
      addServerSpecificCmd("UNLOCK", 9, var0.getUnlockUsage(), var0.getUnLockDescription(), var1 + " " + var0.getUnLockSample(), "", "", 102, var0.getNotSupported());
      addServerSpecificCmd("RESUME", 25, var0.getResumeUsage(), var0.getResumeDescription(), var1 + " " + var0.getResumeSample(), "", var0.getResumeArgs(), 102, var0.getNotSupported());
      addServerSpecificCmd("SHUTDOWN", 26, var0.getShutdownUsage(), var0.getShutDownDescription(), var1 + " " + var0.getShutDownSample(), "", var0.getShutDownArgs(), 102, var0.getshutNotSupported());
      addServerSpecificCmd("CANCEL_SHUTDOWN", 11, "", "", "", "", "", 102, var0.getNotSupported());
      addServerSpecificCmd("FORCESHUTDOWN", 27, var0.getForceSDUsage(), var0.getForceSDDescription(), var1 + " " + var0.getForceSDSample(), "", var0.getForceSDArgs(), 102);
      addServerSpecificCmd("CONVERTPROPS", 42, "CONVERTPROPS  -serverPropdir <server Dir> -clusterPropDir <clusterDir> -newdomainName <domainName> -servername <server name> -outputDir <outputDir>", "Converts the weblogic.properties to config.xml", false, var1 + " " + "CONVERTPROPS -serverPropdir c:\\wls_jb\\config\\karma -newdomainName ydomain -servername yserver -outputDir c:\\temp", "", "later dude", 102);
      addServerSpecificCmd("DISCOVERMANAGEDSERVER", 43, "DISCOVERMANAGEDSERVER [-serverName <targetServer> [-listenPort <listenport>] [-listenAddress <listen address>] [-listenPortSecure]]", "Causes the Administration Server to re-establish administrative control over Managed Servers.", var1 + " " + "DISCOVERMANAGEDSERVER -serverName managed1 -listenPort 7701 -listenAddress spiffy", "", "serverName = A Managed Server that is currently running. If you\n\tdo not specify a server, the Administration Server will\n\tdiscover and re-establish control over all the Managed\n\tServers that are known to be running but disconnected\n\tfrom administrative services. \nlistenPort = (optional)Listen port at which the managed server is running, if not specified, uses the listen port that is configured. \nlistenAddress = (optional) Listen address at which the managed server is running, if not specified uses the listen address that is configured. \nlistenPortSecure = (optional) Forces the Administration Server to use a secure protocol. Without this\noption, the Administration Server uses the t3 protocol. If you disable a \nManaged Server's non-SSL listen port, you must specify this option.", 102);
      addCmd("MBEAN", 104, "", "Working with WebLogic Server MBeans", true, "", "");
      String var4 = var1 + " " + var0.getCreateWithON();
      var4 = var4 + "\n" + var1 + " " + var0.getCreateWithName();
      addMBeanCmd("CREATE", 33, var0.getCreateUsage(), var0.getCreateDescription(), var4, "", var0.getCreateArgs(), 104);
      String var5 = var1 + " " + var0.getGetSampleForType();
      var5 = var5 + "\n" + var1 + " " + var0.getGetSampleForON();
      var5 = var5 + "\n" + var1 + " " + var0.getGetSampleForProperty();
      addMBeanCmd("GET", 31, var0.getGetUsage(), var0.getGetDescription(), var5, "", var0.getGetArgs(), 104);
      String var6 = var1 + " " + var0.getSetSampleForType();
      var6 = var6 + "\n" + var1 + " " + var0.getSetSampleForON();
      addMBeanCmd("SET", 32, var0.getSetUsage(), var0.getSetDescription(), var6, "", var0.getSetArgs(), 104);
      addMBeanCmd("INVOKE", 35, var0.getInvokeUsage(), var0.getInvokeDescription(), var1 + " " + var0.getInvokeSample(), "", var0.getInvokeArgs(), 104);
      addMBeanCmd("QUERY", 29, var0.getQueryUsage(), var0.getQueryDescription(), var1 + " " + var0.getQuerySample(), "", var0.getQueryArgs(), 104);
      String var7 = var1 + " " + var0.getDeleteWithON();
      var7 = var7 + "\n" + var1 + " " + var0.getDeleteType();
      addMBeanCmd("DELETE", 34, var0.getDeleteUsage(), var0.getDeleteDescription(), var7, "", var0.getDeleteArgs(), 104);
      addMBeanCmd("BATCHUPDATE", 36, var0.getBatchUsage(), var0.getBatchDescription(), var1 + " " + var0.getBatchSample(), "", var0.getBatchArgs(), 104);
      addCmd("CLUSTER", 105, "", "Working with Clusters", true, "", "");
      addClusterCmd("VALIDATECLUSTERCONFIG", 41, var0.getVCCUsage(), var0.getVCCDescription(), var1 + " " + var0.getVCCSample(), "", var0.getVCCArgs(), 105);
      addClusterCmd("STARTCLUSTER", 39, var0.getclusterStartUsage(), var0.getClusterStartDescription(), var1 + " " + var0.getClusterStartSample(), "", var0.getStartClusterArgs(), 105, var0.getNeedNodeManager());
      addClusterCmd("STOPCLUSTER", 40, var0.getClusterStopUsage(), var0.getClusterStopDescription(), var1 + " " + var0.getClusterStopSample(), "", var0.getStopClusterArgs(), 105, var0.getNeedNodeManager());
      addClusterCmd("CLUSTERSTATE", 37, var0.getPingClusterUsage(), var0.getpingclusDesr(), var1 + " " + var0.getpingclusterSample(), "", var0.getpingClusterArgs(), 105);
      String var8 = var1 + " " + var0.getMigrateSampleJTA();
      var8 = var8 + "\n" + var1 + " " + var0.getMigrateSampleJMS();
      addClusterCmd("MIGRATE", 50, var0.getMigrateUsage(), var0.getMigrateDescription(), var8, "", var0.getMigrateArgs(), 105);
      addClusterCmd("MIGRATESERVER", 52, var0.getMigrateServerUsage(), var0.getMigrateServerDescription(), var0.getMigrateServerSample(), "", var0.getMigrateServerArgs(), 105);
      String var9 = var1 + " MIGRATEALL -server server1 " + "-destination server2";
      addClusterCmd("MIGRATEALL", 53, var0.getMigrateAllUsage(), var0.getMigrateAllDescription(), var9, "", var0.getMigrateAllArgs(), 105);
      addCmd("ADMINCONFIG", 106, "", "Managing the weblogic commands", true, "", "");
      addAdminConfigCmd("STOREUSERCONFIG", 51, var0.getStoreUserConfigUsage(), var0.getStoreUserConfigDescription(), var1 + " " + var0.getStoreUserConfigSample(), "", var0.getStoreUserConfigArgs(), 106);
      addCmd("ALL", 100, "", "Help for all available commands", true, "", "");
      addInfoCmd("HELP", 20, var0.getHelpUsage(), "Help on a specific command", "", "", "", 0);
   }
}
