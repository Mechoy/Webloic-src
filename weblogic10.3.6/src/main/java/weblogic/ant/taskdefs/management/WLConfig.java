package weblogic.ant.taskdefs.management;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import weblogic.management.MBeanCreationException;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commandline.tools.MBeanCommandLineInvoker;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.security.ProviderMBean;
import weblogic.management.security.RealmMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.security.authorization.AdjudicatorMBean;
import weblogic.management.security.authorization.AuthorizerMBean;
import weblogic.management.security.authorization.RoleMapperMBean;
import weblogic.management.security.credentials.CredentialMapperMBean;
import weblogic.management.security.pk.CertPathProviderMBean;
import weblogic.management.security.pk.KeyStoreMBean;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;
import weblogic.utils.StringUtils;
import weblogic.utils.TypeConversionUtils;

public class WLConfig extends Task {
   private static String JNDI = "/jndi/";
   private static String RUNTIME_MBEANSERVER = "weblogic.management.mbeanservers.runtime";
   private static String EDIT_SERVICE;
   private static String EDIT_MBEANSERVER;
   private static String[][] baseProviderTypes;
   private String adminurl;
   private String username;
   private String password;
   private String userConfigFile;
   private String userKeyFile;
   private ArrayList commands = new ArrayList();
   private boolean failOnError = true;
   private String domainName = null;

   public void setFailOnError(boolean var1) {
      this.failOnError = var1;
   }

   public void setUserName(String var1) {
      this.username = var1;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public void setUrl(String var1) {
      this.adminurl = var1;
   }

   public void addCreate(MBeanCreateCommand var1) {
      this.commands.add(var1);
   }

   public void addDelete(MBeanDeleteCommand var1) {
      this.commands.add(var1);
   }

   public void addSet(MBeanSetCommand var1) {
      this.commands.add(var1);
   }

   public void addGet(MBeanGetCommand var1) {
      this.commands.add(var1);
   }

   public void addQuery(MBeanQueryCommand var1) {
      this.commands.add(var1);
   }

   public void setUserConfigFile(String var1) {
      this.userConfigFile = var1;
   }

   public void setUserKeyFile(String var1) {
      this.userKeyFile = var1;
   }

   public void addInvoke(MBeanInvokeCommand var1) {
      this.commands.add(var1);
   }

   public void execute() throws BuildException {
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

      InitialContext var1;
      MBeanHome var2;
      try {
         var1 = this.getInitialContext();
         var2 = (MBeanHome)var1.lookup("weblogic.management.adminhome");
      } catch (NamingException var6) {
         try {
            var1 = this.getInitialContext();
            var2 = (MBeanHome)var1.lookup("weblogic.management.home.localhome");
         } catch (NamingException var5) {
            this.handleException("Failed to connect to the server", var5);
            return;
         }
      }

      Iterator var3 = this.commands.iterator();

      try {
         this.executeCommands(var2, var3, (WebLogicMBean)null);
      } catch (BuildException var7) {
         if (this.failOnError) {
            throw var7;
         }

         this.log(var7.toString());
      }

   }

   public void executeCommands(MBeanHome var1, Iterator var2, WebLogicMBean var3) throws BuildException {
      while(var2.hasNext()) {
         MBeanCommand var4 = (MBeanCommand)var2.next();
         switch (var4.getCommandType()) {
            case 1:
               this.invokeCreateCommand(var1, var3, (MBeanCreateCommand)var4);
               break;
            case 2:
               this.invokeDeleteCommand(var1, (MBeanDeleteCommand)var4);
               break;
            case 3:
               this.invokeSetCommand(var1, (MBeanSetCommand)var4);
               break;
            case 4:
               this.invokeGetCommand(var1, (MBeanGetCommand)var4);
               break;
            case 5:
               this.invokeQueryCommand(var1, (MBeanQueryCommand)var4);
            case 6:
            default:
               break;
            case 7:
               this.invokeInvokeCommand(var1, (MBeanInvokeCommand)var4);
         }
      }

   }

   private void invokeCreateCommand(MBeanHome var1, WebLogicMBean var2, MBeanCreateCommand var3) {
      WebLogicMBean var4 = null;
      String var5 = null;
      if (var3.getType() == null) {
         this.handleException("Type not specified for create command");
      } else {
         try {
            if (var3.getDomain() == null) {
               var3.setDomain(this.getDomainName(var1));
            }

            if (var2 != null && var2 instanceof ConfigurationMBean) {
               var4 = var1.createAdminMBean(var3.getName(), var3.getType(), var3.getDomain(), (ConfigurationMBean)var2);
            } else {
               var4 = var1.createAdminMBean(var3.getName(), var3.getType(), var3.getDomain());
            }

            var5 = var4.getObjectName().toString();
            this.log("Created MBEAN: " + var5, 3);
         } catch (MBeanCreationException var9) {
            this.invokeCreateSecurityBean(var3, var9);
            return;
         }

         if (var3.getProperty() != null) {
            this.getProject().setProperty(var3.getProperty(), var5);
         }

         Iterator var6 = var3.getSetCommands();

         while(var6.hasNext()) {
            MBeanSetCommand var7 = (MBeanSetCommand)var6.next();
            var7.setMBean(var5);
            this.invokeSetCommand(var1, var7);
         }

         Iterator var10 = var3.getInvokeCommands();

         while(var10.hasNext()) {
            MBeanInvokeCommand var8 = (MBeanInvokeCommand)var10.next();
            var8.setMBean(var5);
            this.invokeInvokeCommand(var1, var8);
         }

         Iterator var11 = var3.getCreateCommands();
         this.executeCommands(var1, var11, var4);
      }
   }

   private ObjectName invokeCreateCommand(MBeanServerConnection var1, ObjectName var2, MBeanCreateCommand var3) {
      if (var3.getType() == null) {
         throw new BuildException("Type not specified for create command");
      } else {
         String var4 = var3.getName();
         String var5 = var3.getType();

         try {
            Object[] var7 = new Object[]{var4};
            String[] var8 = new String[]{"java.lang.String"};
            ObjectName var6 = (ObjectName)var1.invoke(var2, "create" + var5, var7, var8);
            return var6;
         } catch (Throwable var9) {
            throw new BuildException("Error creating Bean " + var4, var9);
         }
      }
   }

   private void invokeCreateSecurityBean(MBeanCreateCommand var1, Exception var2) {
      String var3 = null;

      try {
         MBeanServerConnection var4 = this.getMBeanServerConnection(EDIT_MBEANSERVER);
         this.startEdit(var4);

         try {
            ObjectName var5 = this.createSecurityBean(var4, var1.getName(), var1.getType(), var1.getRealm());
            var3 = var5.toString();
            this.log("[CREATE] Created a new provider " + var3, 3);
            Iterator var6 = var1.getSetCommands();

            while(var6.hasNext()) {
               MBeanSetCommand var7 = (MBeanSetCommand)var6.next();
               var7.setMBean(var3);
               this.invokeSetCommand(var4, var7);
            }

            Iterator var12 = var1.getInvokeCommands();

            while(var12.hasNext()) {
               MBeanInvokeCommand var8 = (MBeanInvokeCommand)var12.next();
               var8.setMBean(var3);
               this.invokeInvokeCommand(var4, var8);
            }

            Iterator var13 = var1.getCreateCommands();

            while(true) {
               if (!var13.hasNext()) {
                  this.activate(var4);
                  break;
               }

               MBeanCreateCommand var9 = (MBeanCreateCommand)var13.next();
               this.invokeCreateCommand(var4, var5, var9);
            }
         } catch (Exception var10) {
            this.rollback(var4);
            throw var10;
         }
      } catch (Exception var11) {
         throw new BuildException("Unable to create mbean: " + var11, var2);
      }

      if (var1.getProperty() != null) {
         this.getProject().setProperty(var1.getProperty(), var3);
      }

   }

   private void invokeInvokeCommand(MBeanHome var1, MBeanInvokeCommand var2) throws BuildException {
      if (this.isProperty(var2.getMBean())) {
         var2.setMBean(this.getRequiredProperty(var2.getMBean()));
      }

      if (var2.getType() == null && var2.getMBean() == null) {
         this.handleException("MBean name or type not specified for invoke command");
      } else if (var2.getMethodName() == null) {
         this.handleException("Method name not specified for invoke command");
      } else {
         if (var2.getArguments() == null) {
            var2.setArguments("");
         }

         String[] var3 = this.getConnectParams();
         String var4 = StringUtils.join(var3, " ");
         var4 = var4 + " invoke ";
         if (var2.getMBean() != null) {
            var4 = var4 + "-mbean " + var2.getMBean() + " ";
         } else {
            var4 = var4 + "-type " + var2.getType() + " ";
         }

         var4 = var4 + "-method " + var2.getMethodName() + " ";
         String[] var5 = StringUtils.splitCompletely(var2.getArguments(), " ");

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var4 = var4 + var5[var6] + " ";
         }

         String[] var7 = StringUtils.splitCompletely(var4, " ");
         this.invokeCommand(var7);
      }
   }

   private void invokeInvokeCommand(MBeanServerConnection var1, MBeanInvokeCommand var2) throws BuildException {
      if (this.isProperty(var2.getMBean())) {
         var2.setMBean(this.getRequiredProperty(var2.getMBean()));
      }

      if (var2.getType() == null && var2.getMBean() == null) {
         this.handleException("MBean name or type not specified for invoke command");
      } else if (var2.getMethodName() == null) {
         this.handleException("Method name not specified for invoke command");
      } else {
         if (var2.getArguments() == null) {
            var2.setArguments("");
         }

         throw new BuildException("Nested invokes not supported in this context");
      }
   }

   private void invokeSetCommand(MBeanHome var1, MBeanSetCommand var2) throws BuildException {
      if (this.isProperty(var2.getValue())) {
         var2.setValue(this.getRequiredProperty(var2.getValue()));
      }

      if (this.isProperty(var2.getMBean())) {
         var2.setMBean(this.getRequiredProperty(var2.getMBean()));
      }

      if (var2.getMBean() == null) {
         this.handleException("MBean not specified for set command");
      } else if (var2.getAttribute() == null) {
         this.handleException("Attribute not specified for set command");
      } else if (var2.getValue() == null) {
         this.handleException("Value not specified for set command");
      } else {
         Object var3 = null;

         try {
            var3 = this.getMBean(var1, var2.getMBean());
         } catch (BuildException var11) {
            MBeanServerConnection var5 = null;

            try {
               var5 = this.getMBeanServerConnection(EDIT_MBEANSERVER);
               this.startEdit(var5);
               this.invokeSetCommand(var5, var2);
               this.log("[Set] New values have been set, not comitted yet");
               this.activate(var5);
               this.log("[Set] Changed values have been committed");
            } catch (Exception var8) {
               this.handleException((Throwable)var8);
               this.rollback(var5);
               this.log("[Set] Changed values have been rolled back");
            }

            return;
         }

         if (var3 instanceof ProviderMBean) {
            this.setOnProvider((ProviderMBean)var3, var2);
         } else if (var3 instanceof RealmMBean) {
            this.setOnRealm((RealmMBean)var3, var2);
         } else if (var2.getAttribute().equalsIgnoreCase("parent")) {
            try {
               WebLogicObjectName var4 = new WebLogicObjectName(var2.getMBean());
               WebLogicMBean var13 = (WebLogicMBean)var1.getProxy(var4);
               WebLogicObjectName var6 = new WebLogicObjectName(var2.getValue());
               WebLogicMBean var7 = (WebLogicMBean)var1.getProxy(var6);
               this.log("SET " + var2.getMBean() + " parent=" + var2.getValue(), 3);
               var13.setParent(var7);
            } catch (JMException var9) {
               this.handleException("Error setting parent", var9);
               return;
            } catch (ConfigurationException var10) {
               this.handleException("Error setting parent", var10);
               return;
            }
         } else {
            String[] var12 = this.getConnectParams();
            int var14 = var12.length;
            String[] var15 = new String[var14 + 6];
            System.arraycopy(var12, 0, var15, 0, var14);
            var15[var14++] = "SET";
            if (var2.getMBean() != null) {
               var15[var14++] = "-mbean";
               var15[var14++] = var2.getMBean();
            }

            var15[var14++] = "-property";
            var15[var14++] = var2.getAttribute();
            var15[var14++] = var2.getValue();
            this.invokeCommand(var15);
         }

      }
   }

   private void invokeSetCommand(MBeanServerConnection var1, MBeanSetCommand var2) throws BuildException {
      if (this.isProperty(var2.getValue())) {
         var2.setValue(this.getRequiredProperty(var2.getValue()));
      }

      if (this.isProperty(var2.getMBean())) {
         var2.setMBean(this.getRequiredProperty(var2.getMBean()));
      }

      if (var2.getMBean() == null) {
         this.handleException("MBean not specified for set command");
      } else if (var2.getAttribute() == null) {
         this.handleException("Attribute not specified for set command");
      } else if (var2.getValue() == null) {
         this.handleException("Value not specified for set command");
      } else {
         this.setOnBean(var1, var2.getMBean(), var2.getAttribute(), var2.getValue());
      }
   }

   private void setOnProvider(ProviderMBean var1, MBeanSetCommand var2) {
      String var3 = var1.getName();

      try {
         MBeanServerConnection var4 = this.getMBeanServerConnection(EDIT_MBEANSERVER);
         String var5 = var1.getRealm().getName();
         ObjectName var6 = this.getRealm(var4, var5);
         this.startEdit(var4);

         try {
            String var8 = this.getProviderMethod(var1, "lookup");
            ObjectName var7;
            if (var8.startsWith("get")) {
               var7 = (ObjectName)var4.getAttribute(var6, var8.substring(3));
               if (var7 == null) {
                  throw new BuildException("Could not find " + var3);
               }
            } else {
               Object[] var9 = new Object[]{var3};
               String[] var10 = new String[]{"java.lang.String"};
               var7 = (ObjectName)var4.invoke(var6, var8, var9, var10);
               if (var7 == null) {
                  throw new BuildException("Could not find " + var3);
               }
            }

            String var14 = var2.getAttribute();
            String var13 = var2.getValue();
            this.setOnBean(var4, var7, var14, var13);
            this.activate(var4);
         } catch (Exception var11) {
            this.rollback(var4);
            throw var11;
         }
      } catch (Exception var12) {
         this.handleException("Unable to modify mbean: " + var12, var12);
      }
   }

   private void setOnBean(MBeanServerConnection var1, String var2, String var3, String var4) throws BuildException {
      try {
         ObjectName var5 = new ObjectName(var2);
         this.setOnBean(var1, var5, var3, var4);
      } catch (MalformedObjectNameException var7) {
         throw new BuildException(var7);
      }
   }

   private void setOnBean(MBeanServerConnection var1, ObjectName var2, String var3, String var4) throws BuildException {
      try {
         MBeanInfo var5 = var1.getMBeanInfo(var2);
         MBeanAttributeInfo[] var6 = var5.getAttributes();
         MBeanAttributeInfo var7 = null;

         for(int var8 = 0; var8 < var6.length; ++var8) {
            if (var6[var8].getName().equals(var3)) {
               var7 = var6[var8];
               break;
            }
         }

         if (var7 == null) {
            throw new BuildException("No such attribute: " + var3);
         } else {
            String var11 = var7.getType();
            Attribute var9 = new Attribute(var3, this.convertObject(var4, var11));
            var1.setAttribute(var2, var9);
         }
      } catch (Exception var10) {
         throw new BuildException(var10);
      }
   }

   private void setOnRealm(RealmMBean var1, MBeanSetCommand var2) {
      try {
         MBeanServerConnection var3 = this.getMBeanServerConnection(EDIT_MBEANSERVER);
         String var4 = var2.getAttribute();
         String var5 = var2.getValue();
         String var6 = var1.getName();
         ObjectName var7 = this.getRealm(var3, var6);
         this.startEdit(var3);

         try {
            MBeanInfo var8 = var3.getMBeanInfo(var7);
            MBeanAttributeInfo[] var9 = var8.getAttributes();
            MBeanAttributeInfo var10 = null;

            for(int var11 = 0; var11 < var9.length; ++var11) {
               if (var9[var11].getName().equals(var4)) {
                  var10 = var9[var11];
                  break;
               }
            }

            if (var10 == null) {
               throw new BuildException("No such attribute: " + var4);
            } else {
               String var15 = var10.getType();
               Attribute var12 = new Attribute(var4, this.convertObject(var5, var15));
               var3.setAttribute(var7, var12);
               this.activate(var3);
            }
         } catch (Exception var13) {
            this.rollback(var3);
            throw var13;
         }
      } catch (Exception var14) {
         this.handleException("Unable to modify mbean: " + var14, var14);
      }
   }

   private void invokeGetCommand(MBeanHome var1, MBeanGetCommand var2) throws BuildException {
      if (var2.getAttribute() == null) {
         this.handleException("Attribute not specified in get command");
      } else if (var2.getProperty() == null) {
         this.handleException("Property not specified in get command");
      } else {
         if (this.isProperty(var2.getMBean())) {
            var2.setMBean(this.getRequiredProperty(var2.getMBean()));
         }

         try {
            Object var3 = this.getMBean(var1, var2.getMBean());
            Object var4 = null;
            if (var3 instanceof WebLogicMBean) {
               WebLogicMBean var5 = (WebLogicMBean)var3;
               var4 = var5.getAttribute(var2.getAttribute());
            } else if (var3 instanceof ObjectInstance) {
               ObjectInstance var13 = (ObjectInstance)var3;
               RemoteMBeanServer var6 = var1.getMBeanServer();
               var4 = var6.getAttribute(var13.getObjectName(), var2.getAttribute());
            } else {
               RemoteMBeanServer var14 = var1.getMBeanServer();
               var4 = var14.getAttribute(this.getObjectName(var3), var2.getAttribute());
            }

            if (var4 != null) {
               String var15 = "";
               if (var4 instanceof WebLogicObjectName[]) {
                  WebLogicObjectName[] var16 = (WebLogicObjectName[])((WebLogicObjectName[])var4);

                  for(int var7 = 0; var7 < var16.length; ++var7) {
                     var15 = var15 + var16[var7];
                     if (var7 + 1 < var16.length) {
                        var15 = var15 + ";";
                     }
                  }
               } else {
                  var15 = var4.toString();
               }

               this.getProject().setProperty(var2.getProperty(), var15);
            }
         } catch (InstanceNotFoundException var8) {
            this.handleException("No Such MBean", var8);
         } catch (AttributeNotFoundException var9) {
            this.handleException("No Such Attribute: " + var2.getAttribute(), var9);
         } catch (MBeanException var10) {
            this.handleException("Error retrieving attribute: " + var2.getAttribute(), var10);
         } catch (ReflectionException var11) {
            this.handleException("Error retrieving attribute: " + var2.getAttribute(), var11);
         } catch (BuildException var12) {
            this.handleException((Throwable)var12);
         }

      }
   }

   private ObjectName getObjectName(Object var1) {
      if (Proxy.isProxyClass(var1.getClass())) {
         InvocationHandler var2 = Proxy.getInvocationHandler(var1);
         if (var2 instanceof MBeanServerInvocationHandler) {
            return MBeanServerInvocationHandler.getObjectName(var1);
         }
      }

      if (var1 instanceof WebLogicMBean) {
         return ((WebLogicMBean)var1).getObjectName();
      } else {
         return var1 instanceof StandardInterface ? ((StandardInterface)var1).wls_getObjectName() : null;
      }
   }

   private void invokeDeleteCommand(MBeanHome var1, MBeanDeleteCommand var2) throws BuildException {
      if (this.isProperty(var2.getMBean())) {
         var2.setMBean(this.getRequiredProperty(var2.getMBean()));
      }

      try {
         Object var3 = null;

         try {
            var3 = this.getMBean(var1, var2.getMBean());
         } catch (BuildException var10) {
            MBeanServerConnection var5 = null;

            try {
               var5 = this.getMBeanServerConnection(EDIT_MBEANSERVER);
               this.startEdit(var5);
               var5.unregisterMBean(new ObjectName(var2.getMBean()));
               this.log("[Delete] MBean has been unregistered, uncomitted yet");
               this.activate(var5);
               this.log("[Delete] Changes have been committed");
            } catch (Exception var7) {
               this.handleException((Throwable)var7);
               this.rollback(var5);
               this.log("[Delete] Delete has been rolled back");
            }

            return;
         }

         if (var3 instanceof WebLogicMBean) {
            var1.deleteMBean((WebLogicMBean)var3);
         } else if (!(var3 instanceof RealmMBean) && !(var3 instanceof ProviderMBean)) {
            RemoteMBeanServer var14 = var1.getMBeanServer();
            var14.unregisterMBean(this.getObjectName(var3));
         } else {
            try {
               MBeanServerConnection var4 = this.getMBeanServerConnection(EDIT_MBEANSERVER);
               this.startEdit(var4);

               try {
                  if (var3 instanceof ProviderMBean) {
                     ProviderMBean var15 = (ProviderMBean)var3;
                     this.destroySecurityBean(var4, var15);
                  } else {
                     RealmMBean var16 = (RealmMBean)var3;
                     this.destroyRealmBean(var4, var16);
                  }

                  this.activate(var4);
               } catch (Exception var8) {
                  this.rollback(var4);
                  throw var8;
               }
            } catch (Exception var9) {
               throw new BuildException("Could not delete bean", var9);
            }
         }

         this.log("Deleted MBEAN: " + var2.getMBean(), 3);
      } catch (InstanceNotFoundException var11) {
      } catch (MBeanRegistrationException var12) {
      } catch (BuildException var13) {
         this.handleException((Throwable)var13);
      }

   }

   private void invokeQueryCommand(MBeanHome var1, MBeanQueryCommand var2) throws BuildException {
      ObjectName var3 = null;
      this.log("QUERY -pattern " + var2.getPattern(), 3);

      try {
         var3 = new ObjectName(var2.getPattern());
      } catch (MalformedObjectNameException var6) {
         this.handleException("Error in query pattern", var6);
         return;
      }

      Set var4 = var1.getMBeanServer().queryNames(var3, (QueryExp)null);
      this.log("Set size is " + var4.size(), 3);
      StringBuffer var5 = new StringBuffer();
      this.processNestedCommands(var1, var4, var2, var5);
      if (var2.getProperty() != null && !var5.toString().equals("")) {
         this.getProject().setProperty(var2.getProperty(), var5.toString());
      }

   }

   private void processNestedCommands(MBeanHome var1, Set var2, MBeanQueryCommand var3, StringBuffer var4) {
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         var4.append(var6.toString());
         if (var5.hasNext()) {
            var4.append(";");
         }

         Iterator var7 = var3.getGetCommands();

         while(var7.hasNext()) {
            MBeanGetCommand var8 = (MBeanGetCommand)var7.next();
            var8.setMBean(var6.toString());
            this.invokeGetCommand(var1, var8);
         }

         Iterator var13 = var3.getSetCommands();

         while(var13.hasNext()) {
            MBeanSetCommand var9 = (MBeanSetCommand)var13.next();
            var9.setMBean(var6.toString());
            this.invokeSetCommand(var1, var9);
         }

         Iterator var14 = var3.getCreateCommands();
         Object var10 = this.getMBean(var1, var6.toString());
         if (var10 instanceof WebLogicMBean) {
            this.executeCommands(var1, var14, (WebLogicMBean)var10);
         }

         MBeanDeleteCommand var11 = var3.getDeleteCommand();
         if (var11 != null) {
            var11.setMBean(var6.toString());
            this.invokeDeleteCommand(var1, var11);
         }

         MBeanInvokeCommand var12 = var3.getInvokeCommand();
         if (var12 != null) {
            var12.setMBean(var6.toString());
            this.invokeInvokeCommand(var1, var12);
         }
      }

   }

   private String[] getConnectParams() {
      String[] var1 = new String[6];
      int var2 = 0;
      var1[var2++] = "-url";
      var1[var2++] = this.adminurl;
      if (this.username != null) {
         var1[var2++] = "-username";
         var1[var2++] = this.username;
         var1[var2++] = "-password";
         var1[var2++] = this.password;
      } else {
         if (this.userConfigFile != null) {
            var1[var2++] = "-userconfigfile";
            var1[var2++] = this.userConfigFile;
         }

         if (this.userKeyFile != null) {
            var1[var2++] = "-userkeyfile";
            var1[var2++] = this.userKeyFile;
         }
      }

      return var1;
   }

   private void invokeCommand(String[] var1) throws BuildException {
      this.printCommand(var1);

      try {
         new MBeanCommandLineInvoker(var1, System.out);
      } catch (Exception var3) {
         this.handleException("Error invoking MBean command", var3);
      }
   }

   private void printCommand(String[] var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 6; var3 < var1.length; ++var3) {
         var2.append(var1[var3]);
         var2.append(" ");
      }

      this.log(var2.toString(), 3);
   }

   private InitialContext getInitialContext() throws NamingException, BuildException {
      if (this.adminurl != null && this.adminurl.trim().length() != 0) {
         Hashtable var1 = new Hashtable();
         var1.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         var1.put("java.naming.provider.url", this.adminurl);
         if (this.username == null && this.password == null) {
            UsernameAndPassword var2 = null;
            if (this.userConfigFile == null && this.userKeyFile == null) {
               var2 = UserConfigFileManager.getUsernameAndPassword("weblogic.management");
            } else {
               var2 = UserConfigFileManager.getUsernameAndPassword(this.userConfigFile, this.userKeyFile, "weblogic.management");
            }

            if (var2 != null) {
               this.username = var2.getUsername();
               this.password = new String(var2.getPassword());
            }
         }

         if (this.username != null) {
            var1.put("java.naming.security.principal", this.username);
         }

         if (this.password != null) {
            var1.put("java.naming.security.credentials", this.password);
         }

         return new InitialContext(var1);
      } else {
         throw new BuildException("No URL specified. Please specify valid URL");
      }
   }

   private MBeanServerConnection getMBeanServerConnection(String var1) throws NamingException, BuildException {
      if (this.adminurl != null && this.adminurl.trim().length() != 0) {
         String[] var2 = StringUtils.splitCompletely(this.adminurl, ":");
         if (var2.length != 3) {
            throw new BuildException("Invalid URL specified: " + var1);
         } else {
            String var3 = var2[0];
            String var4 = var2[1];
            String var5 = var2[2];
            if (var4.startsWith("//")) {
               var4 = var4.substring(2);
            }

            boolean var6 = true;

            int var13;
            try {
               var13 = Integer.parseInt(var5);
            } catch (Exception var12) {
               throw new BuildException("Invalid port  specified: " + var5);
            }

            try {
               JMXServiceURL var7 = new JMXServiceURL(var3, var4, var13, JNDI + var1);
               Hashtable var8 = new Hashtable();
               var8.put("java.naming.security.principal", this.username);
               var8.put("java.naming.security.credentials", this.password);
               var8.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
               JMXConnector var9 = null;
               var9 = JMXConnectorFactory.connect(var7, var8);
               MBeanServerConnection var10 = null;
               var10 = var9.getMBeanServerConnection();
               return var10;
            } catch (IOException var11) {
               this.handleException("Failed to connect", var11);
               return null;
            }
         }
      } else {
         throw new BuildException("No URL specified. Please specify valid URL");
      }
   }

   private String getDomainName(MBeanHome var1) {
      ServerRuntimeMBean var2 = null;
      if (this.domainName == null) {
         this.domainName = "mydomain";
         Set var3 = var1.getMBeansByType("ServerRuntime");
         Iterator var4 = var3.iterator();
         if (var4.hasNext()) {
            var2 = (ServerRuntimeMBean)var4.next();
            this.domainName = var2.getObjectName().getDomain();
         }
      }

      return this.domainName;
   }

   private Object getMBean(MBeanHome var1, String var2) throws BuildException {
      if (var2 == null) {
         throw new BuildException("MBean not specified in get command");
      } else {
         String var3 = var2;
         if (this.isProperty(var2)) {
            var3 = this.getRequiredProperty(var2);
            if (var3.indexOf(";") != -1) {
               throw new BuildException("Malformed Object Name: " + var3);
            }
         }

         try {
            WebLogicObjectName var4 = new WebLogicObjectName(var3);
            return var1.getProxy(var4);
         } catch (MalformedObjectNameException var5) {
            throw new BuildException("Malformed Object Name: " + var2, var5);
         } catch (InstanceNotFoundException var6) {
            return this.getCommoMBean(var1, var3);
         }
      }
   }

   private ObjectInstance getCommoMBean(MBeanHome var1, String var2) throws BuildException {
      try {
         ObjectName var3 = new ObjectName(var2);
         RemoteMBeanServer var4 = var1.getMBeanServer();
         if (var4.isRegistered(var3) && var4.isInstanceOf(var3, "weblogic.management.jmx.modelmbean.WLSModelMBean")) {
            Set var5 = var4.queryMBeans(var3, (QueryExp)null);
            Iterator var6 = var5.iterator();
            if (var6.hasNext()) {
               return (ObjectInstance)var6.next();
            }
         }
      } catch (MalformedObjectNameException var7) {
         throw new BuildException("Malformed Object Name: " + var2, var7);
      } catch (InstanceNotFoundException var8) {
         throw new BuildException("No Such Object: " + var2, var8);
      }

      throw new BuildException("Could not find provider: " + var2);
   }

   private boolean isProperty(String var1) {
      return var1 == null ? false : var1.startsWith("${");
   }

   private String getProperty(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.substring(2, var1.length() - 1);
         return this.getProject().getProperty(var2);
      }
   }

   private String getRequiredProperty(String var1) throws BuildException {
      if (var1.indexOf(";") != -1) {
         StringBuffer var2 = new StringBuffer();
         StringTokenizer var3 = new StringTokenizer(var1, ";");
         int var4 = var3.countTokens();
         int var5 = 0;

         while(var3.hasMoreTokens()) {
            ++var5;
            var2.append(this.getSingularRequiredProperty(var3.nextToken()));
            if (var5 != var4) {
               var2.append(";");
            }
         }

         return var2.toString();
      } else {
         return this.getSingularRequiredProperty(var1);
      }
   }

   private String getSingularRequiredProperty(String var1) throws BuildException {
      String var2 = this.getProperty(var1);
      if (var2 != null) {
         return var2;
      } else {
         throw new BuildException("Property not set: " + var1);
      }
   }

   private void handleException(String var1) {
      this.handleException(var1, (Throwable)null);
   }

   private void handleException(Throwable var1) {
      this.handleException(var1.toString(), (Throwable)null);
   }

   private void handleException(String var1, Throwable var2) {
      String var3 = var2 == null ? var1 : var1 + ": " + var2;
      if (this.failOnError) {
         throw new BuildException(var3, var2);
      } else {
         this.log(var3, 0);
      }
   }

   private ObjectName getDomain(MBeanServerConnection var1) throws BuildException {
      try {
         ObjectName var2 = new ObjectName("com.bea:Type=Domain,*");
         return this.getSingletonObject(var1, var2);
      } catch (Exception var3) {
         this.handleException("Error getting Domain", var3);
         return null;
      }
   }

   private ObjectName createSecurityBean(MBeanServerConnection var1, String var2, String var3, String var4) {
      if ("weblogic.management.security.Realm".equals(var3)) {
         ObjectName var5 = this.createRealm(var1, var2);
         return var5;
      } else {
         return this.createProvider(var1, var2, var3, var4);
      }
   }

   private ObjectName createProvider(MBeanServerConnection var1, String var2, String var3, String var4) {
      ObjectName var5 = this.getRealm(var1, var4);

      for(int var6 = 0; var6 < baseProviderTypes.length; ++var6) {
         String[] var7;
         try {
            var7 = (String[])((String[])var1.getAttribute(var5, baseProviderTypes[var6][0]));
         } catch (Exception var14) {
            throw new BuildException("Error determining provider types.", var14);
         }

         for(int var8 = 0; var8 < var7.length; ++var8) {
            if (var7[var8].equals(var3)) {
               String var9 = baseProviderTypes[var6][1];
               Object[] var10 = new Object[]{var2, var3};
               String[] var11 = new String[]{"java.lang.String", "java.lang.String"};

               try {
                  return (ObjectName)var1.invoke(var5, var9, var10, var11);
               } catch (Throwable var13) {
                  throw new BuildException("Error creating MBean " + var2, var13);
               }
            }
         }
      }

      throw new BuildException("Could not create provider " + var3);
   }

   private ObjectName createRealm(MBeanServerConnection var1, String var2) {
      try {
         ObjectName var3 = this.getDomain(var1);
         var3 = (ObjectName)var1.getAttribute(var3, "SecurityConfiguration");
         Object[] var4 = new Object[]{var2};
         String[] var5 = new String[]{"java.lang.String"};
         return (ObjectName)var1.invoke(var3, "createRealm", var4, var5);
      } catch (Throwable var6) {
         throw new BuildException("Error creating Realm " + var2, var6);
      }
   }

   private void destroySecurityBean(MBeanServerConnection var1, ProviderMBean var2) {
      ObjectName var3 = this.getRealm(var1, var2.getRealm().getName());
      String var4 = this.getProviderMethod(var2, "lookup");
      String var5 = this.getProviderMethod(var2, "destroy");

      try {
         Object[] var6 = new Object[]{var2.getName()};
         String[] var7 = new String[]{"java.lang.String"};
         ObjectName var8 = (ObjectName)var1.invoke(var3, var4, var6, var7);
         if (var8 == null) {
            throw new BuildException("Could not find " + var2.getName());
         } else {
            var6 = new Object[]{var8};
            var7 = new String[]{"javax.management.ObjectName"};
            var1.invoke(var3, var5, var6, var7);
         }
      } catch (Exception var9) {
         throw new BuildException("Error deleting MBean " + var2.getName(), var9);
      }
   }

   private void destroyRealmBean(MBeanServerConnection var1, RealmMBean var2) {
      ObjectName var3 = this.getDomain(var1);

      try {
         var3 = (ObjectName)var1.getAttribute(var3, "SecurityConfiguration");
         Object[] var4 = new Object[]{var2.getName()};
         String[] var5 = new String[]{"java.lang.String"};
         ObjectName var6 = (ObjectName)var1.invoke(var3, "lookupRealm", var4, var5);
         if (var6 == null) {
            throw new BuildException("Could not find " + var2.getName());
         } else {
            var4 = new Object[]{var6};
            var5 = new String[]{"javax.management.ObjectName"};
            var1.invoke(var3, "destroyRealm", var4, var5);
         }
      } catch (Exception var7) {
         throw new BuildException("Error deleting MBean " + var2.getName(), var7);
      }
   }

   private ObjectName getRealm(MBeanServerConnection var1, String var2) throws BuildException {
      try {
         ObjectName var3 = this.getDomain(var1);
         var3 = (ObjectName)var1.getAttribute(var3, "SecurityConfiguration");
         if (var2 == null) {
            var3 = (ObjectName)var1.getAttribute(var3, "DefaultRealm");
         } else {
            Object[] var4 = new Object[]{var2};
            String[] var5 = new String[]{"java.lang.String"};
            var3 = (ObjectName)var1.invoke(var3, "lookupRealm", var4, var5);
            if (var3 == null) {
               throw new BuildException("No such Realm: " + var2);
            }
         }

         return var3;
      } catch (Exception var6) {
         throw new BuildException("Error getting Realm", var6);
      }
   }

   protected ObjectName getSingletonObject(MBeanServerConnection var1, ObjectName var2) throws BuildException {
      ObjectName var3 = null;

      try {
         Set var4 = var1.queryNames(var2, (QueryExp)null);
         Iterator var5 = var4.iterator();
         if (var5.hasNext()) {
            var3 = (ObjectName)var5.next();
         }

         return var3;
      } catch (Exception var6) {
         this.handleException("Error getting object", var6);
         return null;
      }
   }

   private void startEdit(MBeanServerConnection var1) throws BuildException {
      try {
         this.getConfigurationManagerMBean(var1).startEdit(0, 3600000);
      } catch (Exception var3) {
         throw new BuildException("Failed to start edit: " + var3, var3);
      }
   }

   private void activate(MBeanServerConnection var1) throws BuildException {
      try {
         this.getConfigurationManagerMBean(var1).save();
         this.getConfigurationManagerMBean(var1).activate(-1L);
      } catch (Exception var3) {
         throw new BuildException("Failed to start edit: " + var3, var3);
      }
   }

   private void rollback(MBeanServerConnection var1) throws BuildException {
      try {
         this.getConfigurationManagerMBean(var1).undo();
         this.getConfigurationManagerMBean(var1).stopEdit();
      } catch (Exception var3) {
         throw new BuildException("Failed to start edit: " + var3, var3);
      }
   }

   private ConfigurationManagerMBean getConfigurationManagerMBean(MBeanServerConnection var1) throws BuildException {
      return this.getEditServiceMBean(var1).getConfigurationManager();
   }

   private EditServiceMBean getEditServiceMBean(MBeanServerConnection var1) throws BuildException {
      ObjectName var2 = this.getEditService(var1);
      return (EditServiceMBean)MBeanServerInvocationHandler.newProxyInstance(var1, var2);
   }

   private ObjectName getEditService(MBeanServerConnection var1) throws BuildException {
      try {
         ObjectName var2 = new ObjectName(EDIT_SERVICE);
         return this.getSingletonObject(var1, var2);
      } catch (Exception var3) {
         this.handleException("Error getting EditService: ", var3);
         return null;
      }
   }

   private String getProviderMethod(ProviderMBean var1, String var2) {
      if (var1 instanceof AuthenticationProviderMBean) {
         return var2 + "AuthenticationProvider";
      } else if (var1 instanceof AdjudicatorMBean) {
         if ("lookup".equals(var2)) {
            var2 = "get";
         }

         return var2 + "Adjudicator";
      } else if (var1 instanceof AuthorizerMBean) {
         return var2 + "Authorizer";
      } else if (var1 instanceof RoleMapperMBean) {
         return var2 + "RoleMapper";
      } else if (var1 instanceof CredentialMapperMBean) {
         return var2 + "CredentialMapper";
      } else if (var1 instanceof CertPathProviderMBean) {
         return var2 + "CertPathProvider";
      } else if (var1 instanceof KeyStoreMBean) {
         return var2 + "KeyStore";
      } else {
         throw new BuildException("Unknown provider type: " + var1);
      }
   }

   private Object convertObject(String var1, String var2) throws BuildException {
      StringTokenizer var4 = null;
      String var5 = null;
      Object var6 = null;
      Object var7 = null;
      boolean var3;
      if (var2.startsWith("[L") && var2.endsWith(";")) {
         var3 = true;
         var5 = var2.substring("[L".length(), var2.lastIndexOf(";"));
         var4 = new StringTokenizer(var1, ";");
         if (var5.startsWith("weblogic.management") && var5.endsWith("MBean")) {
            var6 = new WebLogicObjectName[var4.countTokens()];
         } else {
            try {
               Class var8 = Class.forName(var5);
               var6 = (Object[])((Object[])Array.newInstance(var8, var4.countTokens()));
            } catch (NegativeArraySizeException var9) {
            } catch (ClassNotFoundException var10) {
               throw new BuildException(var10);
            }
         }
      } else {
         var3 = false;
         var5 = var2;
         var4 = new StringTokenizer(var1, "");
      }

      for(int var11 = 0; var4.hasMoreTokens(); ((Object[])var6)[var11++] = var7) {
         var7 = this.getSingleObjectFromString(var5, (String)var4.nextElement());
         if (!var3) {
            return var7;
         }
      }

      return var6;
   }

   private Object getSingleObjectFromString(String var1, String var2) throws IllegalArgumentException {
      if (var1.equals("int")) {
         Integer var14 = new Integer(var2);
         return var14;
      } else if (!var1.equals("java.util.Properties") && !var1.equals("java.util.Map")) {
         if (var1.equals("boolean")) {
            Boolean var13 = new Boolean(var2);
            return var13;
         } else if (var1.equals("long")) {
            Long var12 = new Long(var2);
            return var12;
         } else if (var1.startsWith("weblogic.management") && var1.endsWith("MBean")) {
            try {
               WebLogicObjectName var11 = new WebLogicObjectName(var2);
               return var11;
            } catch (MalformedObjectNameException var8) {
               throw new IllegalArgumentException(var8.toString());
            }
         } else {
            try {
               Class var10 = Class.forName(var1);
               Class[] var4 = new Class[]{Class.forName("java.lang.String")};
               Constructor var5 = var10.getConstructor(var4);
               String[] var6 = new String[]{var2};
               Object var7 = var5.newInstance(var6);
               return var7;
            } catch (Exception var9) {
               throw new BuildException("Unable to convert the argument value " + var2 + " to class " + var1 + ". " + var9);
            }
         }
      } else {
         Properties var3 = new Properties();
         TypeConversionUtils.stringToDictionary(var2, var3, ";");
         return var3;
      }
   }

   static {
      EDIT_SERVICE = EditServiceMBean.OBJECT_NAME;
      EDIT_MBEANSERVER = "weblogic.management.mbeanservers.edit";
      baseProviderTypes = new String[][]{{"AuditorTypes", "createAuditor"}, {"AuthenticationProviderTypes", "createAuthenticationProvider"}, {"RoleMapperTypes", "createRoleMapper"}, {"AuthorizerTypes", "createAuthorizer"}, {"AdjudicatorTypes", "createAdjudicator"}, {"CredentialMapperTypes", "createCredentialMapper"}, {"CertPathProviderTypes", "createCertPathProvider"}, {"KeyStoreTypes", "createKeyStore"}};
   }
}
