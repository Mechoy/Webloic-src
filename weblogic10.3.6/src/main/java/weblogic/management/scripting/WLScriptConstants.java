package weblogic.management.scripting;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import javax.management.Descriptor;
import javax.management.MBeanServerConnection;
import javax.naming.NamingException;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.mbeanservers.MBeanTypeService;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.ActivationTaskMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.scripting.utils.WLSTUtilHelper;

public class WLScriptConstants extends WLSTScriptVariables {
   public WLScriptConstants() {
      this.home = null;
      this.adminHome = null;
      this.wlInstanceObjName = null;
      this.wlInstanceObjName_name = null;
      this.prompt = "";
      this.connected = "false";
      this.domainName = "";
      this.domainType = "serverConfig";
      this.username_bytes = (new String("")).getBytes();
      this.password_bytes = (new String("")).getBytes();
      this.url = "";
      this.atDomainLevel = false;
      this.inMBeanType = false;
      this.inMBeanTypes = false;
      this.atBeanLevel = true;
      this.debug = false;
      this.isAdminServer = true;
      this.serverName = "";
      this.wlInstanceObjNames = null;
      this.wlInstanceObjNames_names = null;
      this.prompts = new Stack();
      this.beans = new Stack();
      this.stackTrace = null;
      this.timeAtError = "";
      this.errorInfo = null;
      this.version = "";
      this.browseHandler = null;
      this.infoHandler = null;
      this.editHandler = null;
      this.lifeCycleHandler = null;
      this.exceptionHandler = null;
      this.jsr88Handler = null;
      this.clusterHandler = null;
      this.domainRuntimeHandler = null;
      this.wlstHelper = null;
      this.findUtil = null;
      this.watchUtil = null;
      this.editService = null;
      this.nmService = null;
      this.interp = null;
      this.shutdownSuccessful = false;
      this.commandType = "";
      this.recording = false;
      this.errorMsg = null;
      this.lastPlaceInRuntime = "";
      this.lastPlaceInConfig = "";
      this.lastPlaceInAdminConfig = "";
      this.iContext = null;
      this.stdOutputMedium = null;
      this.errOutputMedium = null;
      this.skipSingletons = this.getBoolean(System.getProperty("wlst.skipSingletonCd"));
   }

   boolean getBoolean(String value) {
      if (value == null) {
         return false;
      } else {
         return value.toLowerCase(Locale.US).equals("true");
      }
   }

   static List getLoggersList() {
      return loggersList;
   }

   public void setHome(MBeanHome home1) {
      this.home = home1;
   }

   public void setCmo(Object bean) {
      this.wlcmo = bean;
   }

   public MBeanHome getHome() {
      return this.home;
   }

   public MBeanHome getAdminHome() {
      return this.adminHome;
   }

   public byte[] getUsername_bytes() {
      return this.username_bytes;
   }

   public byte[] getPassword_bytes() {
      return this.password_bytes;
   }

   public Throwable getStackTrace() {
      return this.stackTrace;
   }

   public Object getCmo() {
      return this.wlcmo;
   }

   public ConfigurationManagerMBean getConfigManager() {
      return this.configurationManager;
   }

   public ActivationTaskMBean getActivationTask() {
      return this.activationTask;
   }

   public MBeanServerConnection getMBeanServer() {
      return this.mbs;
   }

   public String getPrompt() {
      return this.prompt;
   }

   public String getDomainType() {
      return this.domainType;
   }

   public String getVersion() {
      return this.version;
   }

   public String getConnected() {
      return this.connected;
   }

   public boolean isConnected() {
      return this.connected.equalsIgnoreCase("true");
   }

   public String getDomainName() {
      return this.domainName;
   }

   public String getServerName() {
      return this.serverName;
   }

   public boolean isShutdownSuccessful() {
      return this.shutdownSuccessful;
   }

   public EditService getEditService() {
      return this.editService;
   }

   public NodeManagerService getNodeManagerService() {
      return this.nmService;
   }

   public String getScriptMode() {
      return WLSTUtilHelper.scriptMode ? "true" : "false";
   }

   public void println(String s) {
      try {
         if (this.stdOutputMedium == null && this.logToStandardOut) {
            System.out.println(s);
            return;
         }

         if (this.logToStandardOut) {
            System.out.println(s);
         }

         if (this.stdOutputMedium instanceof OutputStream) {
            ((OutputStream)this.stdOutputMedium).write(s.getBytes());
            ((OutputStream)this.stdOutputMedium).write("\n".getBytes());
            ((OutputStream)this.stdOutputMedium).flush();
         } else if (this.stdOutputMedium instanceof Writer) {
            if (this.stdOutputMedium instanceof PrintWriter) {
               ((PrintWriter)this.stdOutputMedium).println(s);
               ((PrintWriter)this.stdOutputMedium).flush();
            } else {
               ((Writer)this.stdOutputMedium).write(s);
               ((Writer)this.stdOutputMedium).write("\n");
               ((Writer)this.stdOutputMedium).flush();
            }
         }
      } catch (IOException var12) {
         var12.printStackTrace();
      } finally {
         try {
            if (this.stdOutputMedium != null) {
               if (this.stdOutputMedium instanceof OutputStream) {
                  ((OutputStream)this.stdOutputMedium).flush();
               } else if (this.stdOutputMedium instanceof Writer) {
                  if (this.stdOutputMedium instanceof PrintWriter) {
                     ((PrintWriter)this.stdOutputMedium).flush();
                  } else {
                     ((Writer)this.stdOutputMedium).flush();
                  }
               }
            }
         } catch (IOException var11) {
            var11.printStackTrace();
         }

      }

   }

   Object getStandardOutputMedium() {
      return this.stdOutputMedium;
   }

   void printError(String s) {
      try {
         if (this.errOutputMedium == null) {
            System.err.println(s);
            return;
         }

         if (this.errOutputMedium instanceof OutputStream) {
            ((OutputStream)this.errOutputMedium).write(s.getBytes());
            ((OutputStream)this.errOutputMedium).flush();
         } else if (this.errOutputMedium instanceof Writer) {
            if (this.errOutputMedium instanceof PrintWriter) {
               ((PrintWriter)this.errOutputMedium).println(s);
               ((PrintWriter)this.errOutputMedium).flush();
            } else {
               ((Writer)this.errOutputMedium).write(s);
               ((Writer)this.errOutputMedium).flush();
            }
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public void printDebug(String s) {
      if (this.debug) {
         System.out.println("<wlst-debug> " + this.commandType + " : " + s);
      }

   }

   public void print(String s) {
      try {
         if (this.stdOutputMedium == null && this.logToStandardOut) {
            System.out.print(s);
            return;
         }

         if (this.logToStandardOut) {
            System.out.print(s);
         }

         if (this.stdOutputMedium instanceof OutputStream) {
            ((OutputStream)this.stdOutputMedium).write(s.getBytes());
            ((OutputStream)this.stdOutputMedium).flush();
         } else if (this.stdOutputMedium instanceof Writer) {
            if (this.stdOutputMedium instanceof PrintWriter) {
               ((PrintWriter)this.stdOutputMedium).print(s);
               ((PrintWriter)this.stdOutputMedium).flush();
            } else {
               ((Writer)this.stdOutputMedium).write(s);
               ((Writer)this.stdOutputMedium).flush();
            }
         }
      } catch (IOException var12) {
         var12.printStackTrace();
      } finally {
         try {
            if (this.stdOutputMedium != null) {
               if (this.stdOutputMedium instanceof OutputStream) {
                  ((OutputStream)this.stdOutputMedium).flush();
               } else if (this.stdOutputMedium instanceof Writer) {
                  if (this.stdOutputMedium instanceof PrintWriter) {
                     ((PrintWriter)this.stdOutputMedium).flush();
                  } else {
                     ((Writer)this.stdOutputMedium).flush();
                  }
               }
            }
         } catch (IOException var11) {
            var11.printStackTrace();
         }

      }

   }

   boolean booleanValue(Descriptor descr, String descriptorName) {
      String valueAsString = (String)descr.getFieldValue(descriptorName);
      return valueAsString != null && valueAsString.toLowerCase(Locale.US).startsWith("t");
   }

   void initAll() {
      this.home = null;
      this.adminHome = null;
      this.wlcmo = null;
      this.wlInstanceObjName = null;
      this.wlInstanceObjName_name = null;
      this.mbs = null;
      this.prompt = "";
      this.connected = "false";
      this.domainName = "";
      this.domainType = "";
      this.username_bytes = (new String("")).getBytes();
      this.password_bytes = (new String("")).getBytes();
      this.url = "";
      this.atDomainLevel = false;
      this.inMBeanType = false;
      this.inMBeanTypes = false;
      this.atBeanLevel = true;
      this.isAdminServer = true;
      this.serverName = "";
      this.wlInstanceObjNames = null;
      this.wlInstanceObjNames_names = null;
      this.prompts = new Stack();
      this.beans = new Stack();
      this.stackTrace = null;
      this.timeAtError = "";
      this.errorInfo = null;
      this.version = "";
      this.interp = null;
      this.shutdownSuccessful = false;
      helpSet = new HashMap();
      this.commandType = "";
      this.errorMsg = null;
      this.lastPlaceInConfig = "";
      this.lastPlaceInRuntime = "";
      this.lastPlaceInConfigRuntime = "";
      this.lastPlaceInRuntimeRuntime = "";
      this.lastPlaceInConfigDomainRuntime = "";
      this.lastPlaceInRuntimeDomainRuntime = "";
      this.lastPlaceInJNDI = "";
      this.lastPlaceInCustom = "";
      this.lastPlaceInEdit = "";
      this.lastPlaceInJSR77 = "";
      this.skipSingletons = false;

      try {
         if (Thread.currentThread().getName().equals("main") && this.iContext != null) {
            this.iContext.close();
         }
      } catch (NamingException var2) {
      }

      this.runtimeMSC = null;
      this.domainRTMSC = null;
      this.editMSC = null;
      this.jsr77MSC = null;
      this.isEditSessionInProgress = false;
      this.isEditSessionExclusive = false;
      this.isRestartRequired = false;
   }

   public String isAdminServer() {
      Boolean bool = new Boolean(this.isAdminServer);
      return bool.toString();
   }

   public void setStdOutputMedium(Object obj) {
      this.stdOutputMedium = obj;
   }

   public void setErrOutputMedium(Object obj) {
      this.errOutputMedium = obj;
   }

   DomainRuntimeMBean getDomainRuntimeDomainRuntimeMBean() {
      return this.runtimeDomainRuntimeDRMBean;
   }

   DomainMBean getDomainRuntimeDomainMBean() {
      return this.configDomainRuntimeDRMBean;
   }

   DomainMBean getServerRuntimeDomainMBean() {
      return this.runtimeDomainMBean;
   }

   ServerRuntimeMBean getServerRuntimeServerRuntimeMBean() {
      return this.runtimeServerRuntimeMBean;
   }

   DomainRuntimeMBean getCompatabilityDomainRuntimeMBean() {
      return this.compatDomainRuntimeMBean;
   }

   DomainMBean getCompatabilityDomainMBean() {
      return this.compatDomainMBean;
   }

   ServerRuntimeMBean getCompatabilityServerRuntimeMBean() {
      return this.compatServerRuntimeMBean;
   }

   DomainMBean getEditServerDomainMBean() {
      return this.editDomainMBean;
   }

   public EditServiceMBean getEditServiceMBean() {
      return this.editServiceMBean;
   }

   public RuntimeServiceMBean getRuntimeServiceMBean() {
      return this.runtimeServiceMBean;
   }

   public DomainRuntimeServiceMBean getDomainRuntimeServiceMBean() {
      return this.domainRuntimeServiceMBean;
   }

   public MBeanTypeService getMBeanTypeService() {
      return this.mbeanTypeService;
   }

   String calculateThreadDumpFileName() {
      return "Thread_Dump";
   }

   public boolean isEditSessionInProgress() {
      return this.isEditSessionInProgress;
   }

   public boolean inMBeanType() {
      return this.inMBeanType;
   }

   public boolean inMBeanTypes() {
      return this.inMBeanTypes;
   }

   public boolean atBeanLevel() {
      return this.atBeanLevel;
   }

   public String getErrorMessage() {
      return this.theErrorMessage;
   }

   public Object getSTDOutputMedium() {
      return this.stdOutputMedium;
   }

   public void setSTDOutputMedium(Object o) {
      this.stdOutputMedium = o;
   }

   public void setlogToStandardOut(boolean bool) {
      this.logToStandardOut = bool;
   }

   public void setCommandType(String cmdType) {
      this.commandType = cmdType;
   }

   static {
      loggersList.add("javax.management.remote.misc");
      loggersList.add("javax.management.remote.rmi");
   }
}
