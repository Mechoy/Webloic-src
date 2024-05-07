package weblogic.management.provider.internal;

import com.bea.xml.XmlError;
import com.bea.xml.XmlException;
import com.bea.xml.XmlValidationError;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.nio.channels.FileLock;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXParseException;
import weblogic.common.internal.VersionInfo;
import weblogic.descriptor.BeanCreationInterceptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorCache;
import weblogic.descriptor.DescriptorCreationListener;
import weblogic.descriptor.DescriptorException;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.SpecialPropertiesProcessor;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.AdminServerMBeanProcessor;
import weblogic.management.internal.BootStrapStruct;
import weblogic.management.internal.ProductionModeHelper;
import weblogic.management.internal.Utils;
import weblogic.management.provider.AccessCallback;
import weblogic.management.provider.MSIService;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.provider.RuntimeAccessSettable;
import weblogic.management.provider.UpdateException;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.upgrade.ConfigFileHelper;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.Debug;

class RuntimeAccessImpl extends RegistrationManagerImpl implements RuntimeAccess, RuntimeAccessSettable, ConnectListener {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   private static final String ADMIN_HOST_PROP = "weblogic.management.server";
   private static final String OLD_ADMIN_HOST_PROP = "weblogic.admin.host";
   private static int ONE_MINUTE_TIMEOUT = 60000;
   private DomainMBean domain;
   private ServerMBean server;
   private ServerRuntimeMBean serverRuntime;
   private String adminHostProperty = System.getProperty("weblogic.management.server");
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private List<String> accessCallbackList = new ArrayList();
   public static final String SCHEMA_VALIDATION_ENABLED_PROP = "weblogic.configuration.schemaValidationEnabled";
   private static final boolean schemaValidationEnabled = getBooleanProperty("weblogic.configuration.schemaValidationEnabled", true);

   public static boolean getBooleanProperty(String var0, boolean var1) {
      String var2 = System.getProperty(var0);
      return var2 != null ? Boolean.parseBoolean(var2) : var1;
   }

   RuntimeAccessImpl() throws ManagementException {
      if (this.adminHostProperty == null) {
         this.adminHostProperty = System.getProperty("weblogic.admin.host");
      }

      try {
         long var1 = 0L;
         long var3 = 0L;
         if (debug.isDebugEnabled()) {
            var1 = System.currentTimeMillis();
         }

         this.domain = this.parseNewStyleConfig();
         if (debug.isDebugEnabled()) {
            var3 = System.currentTimeMillis();
            Debug.say("CONFIG PARSE TOOK " + (var3 - var1) + " milliseconds");
         }
      } catch (ManagementException var6) {
         throw var6;
      } catch (Throwable var7) {
         Loggable var2 = ManagementLogger.logConfigurationParseErrorLoggable("config.xml", this.getRootCauseMessage(var7));
         throw new ManagementException(var2.getMessage(), var7);
      }

      String var8 = Utils.findServerName(this.domain);
      if (var8 == null) {
         String var10 = "Unable to find a default server";
         throw new ManagementException(var10);
      } else {
         this.server = this.domain.lookupServer(var8);
         if (this.server == null) {
            ServerMBean[] var9 = this.domain.getServers();
            String var11 = "{";

            for(int var4 = 0; var4 < var9.length; ++var4) {
               if (var4 > 0) {
                  var11 = var11 + ",";
               }

               var11 = var11 + var9[var4].getName();
            }

            var11 = var11 + "}";
            Loggable var12 = ManagementLogger.logServerNameDoesNotExistLoggable(var8, var11);
            var12.log();
            throw new ManagementException(var12.getMessage());
         } else {
            if (this.isAdminServer()) {
               try {
                  if (this.domain.isConfigBackupEnabled()) {
                     if (debug.isDebugEnabled()) {
                        Debug.say("BACKUP");
                     }

                     ConfigBackup.saveOriginal();
                  }
               } catch (IOException var5) {
               }
            }

         }
      }
   }

   private String getRootCauseMessage(Throwable var1) {
      String var2 = var1.getMessage();

      for(var1 = var1.getCause(); var1 != null; var1 = var1.getCause()) {
         String var3 = var1.getMessage();
         if (var3 != null && var3.length() > 0) {
            var2 = var3;
         }
      }

      return var2;
   }

   private DomainMBean parseNewStyleConfig() throws ManagementException {
      Loggable var3;
      try {
         DescriptorHelper.setSkipSetProductionMode(true);
         File var1 = new File(DomainDir.getConfigDir());
         if (!var1.exists()) {
            String var35 = var1.getAbsolutePath();
            if (!this.isAdminServer() && !this.isAdminServerAvailable()) {
               var3 = ManagementLogger.logConfigurationDirMissingNoAdminLoggable(var35);
               throw new ManagementException(var3.getMessage());
            } else {
               var3 = ManagementLogger.logConfigurationDirMissingLoggable(var35);
               throw new ManagementException(var3.getMessage());
            }
         } else {
            File var34 = new File(var1, "config.xml");
            if (!var34.exists()) {
               String var37 = var1.getAbsolutePath();
               Loggable var38;
               if (!this.isAdminServer() && !this.isAdminServerAvailable()) {
                  var38 = ManagementLogger.logConfigFileMissingNoAdminLoggable(var37, "config.xml");
                  throw new ManagementException(var38.getMessage());
               } else {
                  var38 = ManagementLogger.logConfigFileMissingLoggable(var37, "config.xml");
                  throw new ManagementException(var38.getMessage());
               }
            } else {
               boolean var36 = ConfigFileHelper.getProductionModeEnabled();
               if (ProductionModeHelper.isProductionModePropertySet() && ManagementService.getPropertyService(kernelID).isAdminServer()) {
                  if (var36 && !ProductionModeHelper.getProductionModeProperty()) {
                     ManagementLogger.logDevelopmentModePropertyDiffersFromConfig();
                  } else if (!var36 && ProductionModeHelper.getProductionModeProperty()) {
                     ManagementLogger.logProductionModePropertyDiffersFromConfig();
                  }
               }

               DescriptorCache var4 = DescriptorCache.getInstance();
               IOHelperImpl var5 = new IOHelperImpl(var34);
               boolean var6 = var36 || Boolean.getBoolean("weblogic.ProductionModeEnabled");
               var5.setProductionModeEnabled(var6);
               File var7 = new File(var1 + File.separator + "configCache");
               FileLock var8 = Utils.getConfigFileLock(ONE_MINUTE_TIMEOUT);

               for(int var9 = 0; var8 == null && var9 < 14; ++var9) {
                  ManagementLogger.logCouldNotGetConfigFileLockRetry("" + ONE_MINUTE_TIMEOUT / 1000);
                  var8 = Utils.getConfigFileLock(ONE_MINUTE_TIMEOUT);
               }

               if (var8 == null) {
                  ManagementLogger.logCouldNotGetConfigFileLock();
               }

               ArrayList var39 = null;
               FileInputStream var10 = null;

               DomainMBean var15;
               try {
                  String var11 = VersionInfo.theOne().getReleaseVersion();
                  boolean var12 = var7.exists();
                  boolean var13 = var4.hasChanged(var7, var5);
                  if (var13 && !var12 && (new File(DomainDir.getInitInfoDir())).exists()) {
                     var13 = false;
                  }

                  var5.setValidate(var13);
                  if (!var13 && !var4.hasVersionChanged(var7, var11) || ConfigFileHelper.getConfigurationVersion() >= 11) {
                     var5.setNeedsTransformation(false);
                  }

                  var10 = new FileInputStream(var34);
                  DomainMBean var14 = (DomainMBean)var5.parseXML(var10);
                  var39 = var5.getErrs();
                  this.processSchemaErrors(var39, var34);
                  if (var5.isNeedsTransformation() && var39 != null && var39.size() == 0 && !var5.isTransformed()) {
                     var4.writeVersion(var7, var11);
                  }

                  DescriptorHelper.setSkipSetProductionMode(false);
                  var15 = var14;
               } finally {
                  if (var8 != null) {
                     try {
                        var8.release();
                        var8.channel().close();
                     } catch (IOException var28) {
                     }
                  }

                  if (var39 == null || var39.size() > 0) {
                     var4.removeCRC(var7);
                     var4.removeVersion(var7);
                  }

                  if (var10 != null) {
                     try {
                        var10.close();
                     } catch (Exception var27) {
                     }
                  }

               }

               return var15;
            }
         }
      } catch (XMLStreamException var30) {
         DescriptorHelper.setSkipSetProductionMode(false);
         throw this.convertXMLStreamException("config.xml", var30);
      } catch (DescriptorException var31) {
         DescriptorHelper.setSkipSetProductionMode(false);
         Throwable var33 = var31.getCause();
         if (var33 instanceof XmlException) {
            throw this.convertXmlException("config.xml", (XmlException)var33);
         } else {
            var3 = ManagementLogger.logConfigurationParseErrorLoggable("config.xml", this.getRootCauseMessage(var31));
            throw new ManagementException(var3.getMessage(), var31);
         }
      } catch (IOException var32) {
         DescriptorHelper.setSkipSetProductionMode(false);
         Loggable var2 = ManagementLogger.logConfigurationParseErrorLoggable("config.xml", this.getRootCauseMessage(var32));
         throw new ManagementException(var2.getMessage(), var32);
      }
   }

   private void processSchemaErrors(List var1, File var2) throws SchemaValidationException {
      if (var1.size() > 0) {
         int var3 = var1.size();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            if (var5 instanceof XmlValidationError) {
               XmlValidationError var6 = (XmlValidationError)var5;
               if (ConfigFileHelper.isAcceptableXmlValidationError(var6)) {
                  --var3;
               } else {
                  ManagementLogger.logConfigurationValidationProblem(var2.getAbsolutePath(), var6.getMessage());
               }
            } else {
               ManagementLogger.logConfigurationValidationProblem(var2.getAbsolutePath(), var5.toString());
            }
         }

         if (schemaValidationEnabled && this.isAdminServer() && var3 > 0) {
            String var8 = var2.getAbsolutePath();
            String var9 = "-Dweblogic.configuration.schemaValidationEnabled=false";
            Loggable var7 = ManagementLogger.logConfigurationSchemaFailureLoggable(var8, var9);
            throw new SchemaValidationException(var7.getMessage());
         }
      }

   }

   private ParseException convertXmlException(String var1, XmlException var2) {
      XmlError var3 = var2.getError();
      if (var3 != null) {
         int var4 = var3.getLine();
         int var5 = var3.getColumn();
         return this.getParseFailureException(var1, var4, var5, var2.getMessage(), var2);
      } else {
         return this.getParseFailureException(var1, -1, -1, var2.getMessage(), var2);
      }
   }

   private ParseException convertXMLStreamException(String var1, XMLStreamException var2) {
      int var3 = -1;
      int var4 = -1;
      Location var5 = var2.getLocation();
      if (var5 != null) {
         var3 = var5.getLineNumber();
         var4 = var5.getColumnNumber();
      } else {
         Throwable var6 = var2.getNestedException();
         if (var6 instanceof SAXParseException) {
            SAXParseException var7 = (SAXParseException)var6;
            var3 = var7.getLineNumber();
            var4 = var7.getColumnNumber();
         }
      }

      return this.getParseFailureException(var1, var3, var4, var2.getMessage(), var2);
   }

   private ParseException getParseFailureException(String var1, int var2, int var3, String var4, Throwable var5) {
      Loggable var6;
      if (var2 <= 0) {
         var6 = ManagementLogger.logConfigurationParseErrorLoggable(var1, var4);
         return new ParseException(var6.getMessage(), var5);
      } else if (var3 <= 0) {
         var6 = ManagementLogger.logConfigurationParseError2Loggable(var1, var2, var4);
         return new ParseException(var6.getMessage(), var5);
      } else {
         var6 = ManagementLogger.logConfigurationParseError3Loggable(var1, var2, var3, var4);
         return new ParseException(var6.getMessage(), var5);
      }
   }

   void initialize() throws ManagementException {
      try {
         SpecialPropertiesProcessor.updateConfiguration(this.domain);
      } catch (UpdateException var2) {
         throw new ManagementException(var2);
      }

      this.addAccessCallbackClass(AdminServerMBeanProcessor.class.getName());
      if (!ManagementService.getPropertyService(kernelID).isAdminServer()) {
         try {
            BootStrapHelper.getBootStrapStruct();
            if (ProductionModeHelper.isGlobalProductionModeSet()) {
               this.domain.setProductionModeEnabled(ProductionModeHelper.getGlobalProductionMode());
            } else if (this.domain.isProductionModeEnabled()) {
               DescriptorHelper.setDescriptorTreeProductionMode(this.domain.getDescriptor(), true);
            }
         } catch (ConfigurationException var3) {
            if (debug.isDebugEnabled()) {
               debug.debug("Error in configuration: " + var3, var3);
            }

            if (var3 instanceof BootStrapHelper.UnknownServerException) {
               throw new ManagementException(var3.getMessage());
            }

            ConnectMonitorFactory.getConnectMonitor().addConnectListener(this);
         }
      } else if (ProductionModeHelper.isProductionModePropertySet()) {
         this.domain.setProductionModeEnabled(ProductionModeHelper.getProductionModeProperty());
      } else if (this.domain.isProductionModeEnabled()) {
         DescriptorHelper.setDescriptorTreeProductionMode(this.domain.getDescriptor(), true);
      }

   }

   public void addAccessCallbackClass(String var1) {
      this.accessCallbackList.add(var1);
   }

   public AccessCallback[] initializeCallbacks(final DomainMBean var1) {
      AccessCallback[] var2 = (AccessCallback[])((AccessCallback[])SecurityServiceManager.runAs(kernelID, kernelID, new PrivilegedAction() {
         public Object run() {
            return RuntimeAccessImpl.this._initializeCallbacks(var1);
         }
      }));
      return var2;
   }

   private AccessCallback[] _initializeCallbacks(DomainMBean var1) {
      DescriptorImpl var2 = (DescriptorImpl)var1.getDescriptor();
      boolean var3 = var2.isModified();
      AccessCallback[] var4 = new AccessCallback[this.accessCallbackList.size()];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         String var6 = (String)this.accessCallbackList.get(var5);
         Class var7 = null;

         try {
            var7 = Class.forName(var6);
            Constructor var8 = var7.getConstructor((Class[])null);
            var4[var5] = (AccessCallback)var8.newInstance((Object[])null);
            var4[var5].accessed(var1);
         } catch (Exception var9) {
            throw new RuntimeException("Failure Initializing Access Callbacks", var9);
         }
      }

      if (!var3 && var2.isModified()) {
         var2.setModified(false);
      }

      return var4;
   }

   public DomainMBean getDomain() {
      return this.domain;
   }

   public ServerMBean getServer() {
      return this.server;
   }

   public String getServerName() {
      return this.server.getName();
   }

   public ServerRuntimeMBean getServerRuntime() {
      return this.serverRuntime;
   }

   public void setServerRuntime(ServerRuntimeMBean var1) {
      if (this.serverRuntime != null) {
         throw new AssertionError("ServerRuntimeMBean may only be set once.");
      } else {
         this.serverRuntime = var1;
      }
   }

   public boolean isAdminServer() {
      return this.adminHostProperty == null;
   }

   public boolean isAdminServerAvailable() {
      return MSIService.getMSIService().isAdminServerAvailable();
   }

   public String getDomainName() {
      return this.domain.getName();
   }

   public String getAdminServerName() {
      String var1 = this.domain.getAdminServerName();

      try {
         return var1 != null ? var1 : (ManagementService.getPropertyService(kernelID).isAdminServer() ? ManagementService.getPropertyService(kernelID).getServerName() : BootStrapHelper.getBootStrapStruct().getAdminServerName());
      } catch (ConfigurationException var3) {
         return var1;
      }
   }

   public void onConnect(ConnectEvent var1) {
      if (var1.getServerName().equals(this.getAdminServerName())) {
         try {
            ManagementService.getPropertyService(kernelID).waitForChannelServiceReady();
            if (MSIService.getMSIService().isAdminRequiredButNotSpecifiedOnBoot()) {
               return;
            }

            BootStrapStruct var2 = BootStrapHelper.getBootStrapStruct();
            ConnectMonitorFactory.getConnectMonitor().removeConnectListener(this);
         } catch (ConfigurationException var3) {
            if (debug.isDebugEnabled()) {
               debug.debug("Error in configuration: " + var3, var3);
            }
         }

      }
   }

   private class IOHelperImpl implements DescriptorCache.IOHelper {
      private File file;
      private ArrayList errs = new ArrayList();
      private boolean validate = true;
      private boolean needsTransformation = true;
      private boolean transformed = false;
      private boolean productionModeEnabled = false;

      public IOHelperImpl(File var2) {
         this.file = var2;
      }

      public InputStream openInputStream() throws IOException {
         FileInputStream var1 = new FileInputStream(this.file);
         return var1;
      }

      private DescriptorBean readCachedDescriptor(File var1) throws IOException {
         ObjectInputStream var2 = null;

         DescriptorBean var3;
         try {
            var2 = new ObjectInputStream(new FileInputStream(var1));
            var3 = (DescriptorBean)var2.readObject();
         } catch (ClassNotFoundException var12) {
            throw (IOException)(new IOException(var12.getMessage())).initCause(var12);
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var11) {
               }
            }

         }

         return var3;
      }

      public Object readCachedBean(File var1) throws IOException {
         DescriptorImpl var2 = DescriptorImpl.beginConstruction(false, RuntimeAccessImpl.READONLY_DESCRIPTOR_MANAGER_SINGLETON.instance, (DescriptorCreationListener)null, (BeanCreationInterceptor)null);
         DescriptorBean var3 = null;

         try {
            var3 = this.readCachedDescriptor(var1);
         } finally {
            DescriptorImpl.endConstruction(var3);
         }

         return var3;
      }

      public Object parseXML(InputStream var1) throws IOException, XMLStreamException {
         DescriptorManagerHelperContext var2 = new DescriptorManagerHelperContext();
         var2.setEditable(false);
         var2.setValidate(this.validate);
         var2.setTransform(this.needsTransformation);
         var2.setErrors(this.errs);
         boolean var3 = ConfigFileHelper.getProductionModeEnabled() || Boolean.getBoolean("weblogic.ProductionModeEnabled");
         if (var3) {
            var2.setRProductionModeEnabled(var3);
            var2.setEProductionModeEnabled(var3);
         }

         DescriptorBean var4 = DescriptorManagerHelper.loadDescriptor(var1, var2).getRootBean();
         this.transformed = var2.isTransformed();
         return var4;
      }

      protected ArrayList getErrs() {
         return this.errs;
      }

      public boolean useCaching() {
         return false;
      }

      void setValidate(boolean var1) {
         this.validate = var1;
      }

      void setNeedsTransformation(boolean var1) {
         this.needsTransformation = var1;
      }

      boolean isNeedsTransformation() {
         return this.needsTransformation;
      }

      boolean isTransformed() {
         return this.transformed;
      }

      void setProductionModeEnabled(boolean var1) {
         this.productionModeEnabled = var1;
      }

      boolean isProductionModeEnabled() {
         return this.productionModeEnabled;
      }
   }

   private static class READONLY_DESCRIPTOR_MANAGER_SINGLETON {
      static DescriptorManager instance = new DescriptorManager();
   }

   public class ParseException extends ManagementException {
      public ParseException(String var2) {
         super(var2);
      }

      public ParseException(String var2, Throwable var3) {
         super(var2, var3);
      }
   }

   public class SchemaValidationException extends ManagementException {
      public SchemaValidationException(String var2) {
         super(var2);
      }
   }
}
