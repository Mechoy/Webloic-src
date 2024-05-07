package weblogic.deploy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Hashtable;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.kernel.Kernel;
import weblogic.management.MBeanHome;
import weblogic.management.deploy.utils.MBeanHomeToolTextFormatter;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;
import weblogic.utils.Getopt2;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.compiler.Tool;
import weblogic.utils.io.TerminalIO;

public abstract class MBeanHomeTool extends Tool {
   public static final String DEFAULT_ADMIN_URL = "t3://localhost:7001";
   public static final String DEFAULT_PROTOCOL = "t3://";
   public static final String DEFAULT_USER = null;
   public static final String DEFAULT_USER_VALUE = "installadministrator";
   public static final String OPTION_ADMIN_URL = "adminurl";
   public static final String OPTION_USER = "user";
   public static final String OPTION_PASSWORD = "password";
   public static final String HTTP_STRING = "http";
   public static final String HTTPS_STRING = "https";
   public static final String T3_STRING = "t3";
   public static final String T3S_STRING = "t3s";
   public static final String OPTION_USERNAME = "username";
   public static final String OPTION_URL = "url";
   public static final String IIOP_STRING = "iiop";
   public static final String OPTION_USERCONFIG = "userconfigfile";
   public static final String DEFAULT_USERCONFIG = null;
   public static final String OPTION_USERKEY = "userkeyfile";
   public static final String DEFAULT_USERKEY = null;
   private MBeanHomeToolTextFormatter textFormatter;
   private String password;
   private String user;
   private UsernameAndPassword UandP = null;
   private String userConfigFile = null;
   private String userKeyFile = null;
   private MBeanHome mbh = null;
   private Context ctx = null;
   private JMXConnector jmx = null;
   private MBeanServerConnection mbs = null;

   public MBeanHomeTool(MBeanHome var1, String[] var2) {
      super(var2);
      this.mbh = var1;
      this.textFormatter = new MBeanHomeToolTextFormatter();
   }

   public MBeanHomeTool(String[] var1) {
      super(var1);
      this.textFormatter = new MBeanHomeToolTextFormatter();
   }

   public Getopt2 getOpts() {
      return this.opts;
   }

   private InitialContext getInitialContext() throws NamingException {
      String var1 = "weblogic.jndi.WLInitialContextFactory";
      Hashtable var2 = new Hashtable();
      var2.put("java.naming.factory.initial", var1);
      return new InitialContext(var2);
   }

   public MBeanHome getMBeanHome() throws MBeanHomeToolException {
      if (this.mbh != null) {
         return this.mbh;
      } else {
         String var1 = this.opts.getOption("adminurl", "t3://localhost:7001");
         String var2 = null;

         try {
            if (Kernel.isServer()) {
               this.ctx = this.getInitialContext();
               this.mbh = (MBeanHome)this.ctx.lookup("weblogic.management.adminhome");
               return this.mbh;
            }

            this.processUsernameAndPassword();
            if (var1.startsWith("http")) {
               var2 = "t3" + var1.substring("http".length());
            } else {
               var2 = var1;
            }

            if (var2.startsWith("iiop")) {
               if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
                  System.setProperty("weblogic.system.iiop.enableClient", "false");
               }

               this.ctx = this.getIIOPContext(var2);
            } else {
               this.ctx = this.getContext(var2);
            }

            this.mbh = (MBeanHome)PortableRemoteObject.narrow(this.ctx.lookup("weblogic.management.home.localhome"), MBeanHome.class);
         } catch (Exception var4) {
            throw new MBeanHomeToolException(this.textFormatter.errorOnConnect(var2, this.user, StackTraceUtils.throwable2StackTrace(var4)));
         }

         return this.mbh;
      }
   }

   public MBeanServerConnection getMBeanServer() throws MBeanHomeToolException {
      if (this.mbs != null) {
         return this.mbs;
      } else {
         String var1 = this.opts.getOption("adminurl", "t3://localhost:7001");
         if (var1.indexOf("://") == -1) {
            var1 = "t3://" + var1;
         }

         try {
            if (!Kernel.isServer()) {
               this.processUsernameAndPassword();
            } else if (var1.startsWith("iiop") && System.getProperty("weblogic.system.iiop.enableClient") == null) {
               System.setProperty("weblogic.system.iiop.enableClient", "false");
            }

            URI var2 = new URI(var1);
            var1 = var1 + "/jndi/" + "weblogic.management.mbeanservers.domainruntime";
            Hashtable var3 = new Hashtable();
            if (!Kernel.isServer()) {
               var3.put("java.naming.security.principal", this.getUser());
               var3.put("java.naming.security.credentials", this.getPassword());
            }

            Environment var4 = new Environment();
            var4.setProviderUrl(var1);
            var4.setSecurityPrincipal(this.getUser());
            var4.setSecurityCredentials(this.getPassword());
            if (var1.startsWith("iiop")) {
               var4.setInitialContextFactory("weblogic.jndi.WLInitialContextFactory");
            }

            this.ctx = var4.getInitialContext();
            var3.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
            JMXConnector var5 = JMXConnectorFactory.connect(new JMXServiceURL(var2.getScheme(), var2.getHost(), var2.getPort(), "/jndi/weblogic.management.mbeanservers.domainruntime"), var3);
            this.mbs = var5.getMBeanServerConnection();
            return this.mbs;
         } catch (Exception var6) {
            throw new MBeanHomeToolException(this.textFormatter.errorOnConnect(var1, this.getUser(), StackTraceUtils.throwable2StackTrace(var6)));
         }
      }
   }

   public String getUser() {
      return this.user;
   }

   private String getUserFromHuman() {
      if (this.user != null) {
         return this.user;
      } else {
         this.user = this.opts.getOption("username");
         if (this.user == null || this.user.length() == 0) {
            System.out.print(this.textFormatter.promptUsername());
            System.out.flush();

            try {
               this.user = (new BufferedReader(new InputStreamReader(System.in))).readLine();
            } catch (IOException var2) {
               this.user = "installadministrator";
            }
         }

         if (this.user == null || this.user.length() == 0) {
            this.user = "installadministrator";
         }

         return this.user;
      }
   }

   public String getPassword() {
      return this.password;
   }

   private String getPasswordFromHuman() throws MBeanHomeToolException {
      if (this.password != null) {
         return this.password;
      } else {
         this.password = this.opts.getOption("password");
         if (this.password == null) {
            String var1 = this.textFormatter.promptPassword(this.getUser());
            int var2 = 0;

            while(this.password == null || this.password.length() == 0) {
               try {
                  System.out.print(var1);
                  System.out.flush();
                  if (TerminalIO.isNoEchoAvailable()) {
                     this.password = TerminalIO.readTerminalNoEcho();
                     System.out.println("");
                  } else {
                     this.password = (new BufferedReader(new InputStreamReader(System.in))).readLine();
                  }
               } catch (IOException var4) {
                  throw new MBeanHomeToolException(this.textFormatter.exceptionNoPassword());
               }

               if (var2++ > 3) {
                  throw new MBeanHomeToolException(this.textFormatter.exceptionNoPassword());
               }
            }
         }

         return this.password;
      }
   }

   public void prepare() {
      this.opts.addOption("adminurl", this.textFormatter.exampleAdminUrl(), this.textFormatter.usageAdminUrl("t3://localhost:7001"));
      this.opts.addOption("username", this.textFormatter.exampleUser(), this.textFormatter.usageUser());
      this.opts.addOption("password", this.textFormatter.examplePassword(), this.textFormatter.usagePassword());
      this.opts.addOption("userconfigfile", this.textFormatter.exampleUserConfig(), this.textFormatter.usageUserConfig());
      this.opts.addOption("userkeyfile", this.textFormatter.exampleUserKey(), this.textFormatter.usageUserkey());
      this.opts.addAlias("username", "user");
      this.opts.addAlias("user", "username");
      this.opts.addAlias("url", "adminurl");
   }

   public void processUsernameAndPassword() throws MBeanHomeToolException {
      this.user = this.opts.getOption("user", DEFAULT_USER);
      this.password = this.opts.getOption("password");
      if (this.user == null || this.password == null) {
         if (this.user != null && this.password == null) {
            this.password = this.getPasswordFromHuman();
         } else if (this.user == null && this.password == null) {
            this.userConfigFile = this.opts.getOption("userconfigfile", DEFAULT_USERCONFIG);
            this.userKeyFile = this.opts.getOption("userkeyfile", DEFAULT_USERKEY);
            UsernameAndPassword var1 = null;
            if (this.userConfigFile == null && this.userKeyFile == null) {
               var1 = UserConfigFileManager.getUsernameAndPassword("weblogic.management");
            } else {
               var1 = UserConfigFileManager.getUsernameAndPassword(this.userConfigFile, this.userKeyFile, "weblogic.management");
            }

            if (var1 != null) {
               this.user = var1.getUsername();
               this.password = new String(var1.getPassword());
            }

            if (this.user == null && this.password == null) {
               this.user = this.getUserFromHuman();
               this.password = this.getPasswordFromHuman();
            }
         }

      }
   }

   private Context getIIOPContext(String var1) throws NamingException {
      Hashtable var2 = new Hashtable();
      var2.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      var2.put("java.naming.provider.url", var1);
      var2.put("java.naming.security.principal", this.getUser());
      var2.put("java.naming.security.credentials", this.getPassword());
      return new InitialContext(var2);
   }

   private Context getContext(String var1) throws NamingException {
      Environment var2 = new Environment();
      new Hashtable();
      var2.setProviderUrl(var1);
      var2.setSecurityPrincipal(this.getUser());
      var2.setSecurityCredentials(this.getPassword());
      return var2.getInitialContext();
   }

   protected void reset() {
      try {
         if (this.ctx != null) {
            this.ctx.close();
         }
      } catch (NamingException var16) {
      } finally {
         this.ctx = null;
      }

      try {
         if (this.jmx != null) {
            this.jmx.close();
         }
      } catch (IOException var14) {
         var14.printStackTrace();
      } finally {
         this.jmx = null;
      }

   }
}
