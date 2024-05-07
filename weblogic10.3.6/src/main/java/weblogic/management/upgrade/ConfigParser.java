package weblogic.management.upgrade;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.management.InvalidAttributeValueException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import weblogic.version;
import weblogic.common.internal.VersionInfo;
import weblogic.descriptor.BasicDescriptorManager;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.internal.mbean.MBeanBinderFactoryHelper;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.provider.ManagementServiceClient;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.management.security.internal.compatibility.SecurityDataInUserConfigMigrationProcessor;
import weblogic.utils.codegen.AttributeBinder;
import weblogic.utils.codegen.AttributeBinderFactory;
import weblogic.utils.codegen.AttributeBinderFactoryHelper;

public class ConfigParser {
   private static Class BINDER_FACTORY_HELPER_CLASS = MBeanBinderFactoryHelper.class;
   private AttributeBinder root;
   private File config;
   private InputStream is;
   private AttributeBinderFactoryHelper helper;
   private String rootTag;
   private AttributeBinder top;
   private Stack stack = new Stack();
   private LogHandler logHandler;
   public static final String ALLOW_90_ATTRIBUTES = "weblogic.configuration.upgrade.Allow90Attributes";
   private static final boolean allow90Attributes = getBooleanProperty("weblogic.configuration.upgrade.Allow90Attributes", false);
   private static final String[] transformScripts;
   private static String[] ignoredElements;
   private static final VersionInfo lastEight;
   String[][] skipList = new String[][]{{"SecurityConfiguration", "EnforceStrictURLPattern"}, {"SecurityConfiguration", "CompatibilityConnectionFiltersEnabled"}, {"SecurityConfiguration", "ConnectionFilter"}, {"SecurityConfiguration", "ConnectionFilterRules"}, {"SecurityConfiguration", "ConnectionLoggerEnabled"}, {"weblogic.management.security.Realm", "CombinedRoleMappingEnabled"}};
   private static final boolean isTransformEnabled = true;

   public static boolean getBooleanProperty(String var0, boolean var1) {
      String var2 = System.getProperty(var0);
      return var2 != null ? Boolean.parseBoolean(var2) : var1;
   }

   public ConfigParser(File var1) {
      this.config = var1;
   }

   public ConfigParser(File var1, LogHandler var2) {
      this.config = var1;
      this.logHandler = var2;
   }

   public ConfigParser(InputStream var1) {
      this.is = var1;
   }

   private boolean is70Domain() {
      if (this.config == null) {
         return false;
      } else {
         File var1 = this.config.getParentFile();
         File var2 = new File(var1 + "/userConfig");
         return var2.exists();
      }
   }

   private InputStream insert70SecurityData(InputStream var1) throws IOException {
      if (this.config != null && this.is70Domain()) {
         File var2 = this.config.getParentFile();
         SecurityDataInUserConfigMigrationProcessor var3 = new SecurityDataInUserConfigMigrationProcessor();
         var1 = var3.updateConfiguration(var1, var2);
      }

      return var1;
   }

   public void parse() throws ManagementException {
      this.parse(false);
   }

   public void parse(boolean var1) throws ManagementException {
      XMLReader var2 = null;

      try {
         Class var3 = Class.forName("com.sun.org.apache.xerces.internal.parsers.SAXParser");
         if (var3 != null) {
            var2 = (XMLReader)var3.newInstance();
         }
      } catch (InstantiationException var19) {
         throw new AssertionError("Unexpected exception: " + var19);
      } catch (IllegalAccessException var20) {
         throw new AssertionError("Unexpected exception: " + var20);
      } catch (ClassNotFoundException var21) {
         try {
            var2 = XMLReaderFactory.createXMLReader();
         } catch (SAXException var18) {
            throw new AssertionError("Unable to create SAX parser " + var18);
         }
      }

      try {
         this.helper = (AttributeBinderFactoryHelper)BINDER_FACTORY_HELPER_CLASS.newInstance();
      } catch (InstantiationException var16) {
         throw new AssertionError("Unexpected exception: " + var16);
      } catch (IllegalAccessException var17) {
         throw new AssertionError("Unexpected exception: " + var17);
      }

      var2.setContentHandler(new DefaultHandler() {
         public void startElement(String var1, String var2, String var3, Attributes var4) {
            if (!ConfigParser.isIgnoredElement(var2)) {
               try {
                  AttributeBinderFactory var5 = ConfigParser.this.helper.getAttributeBinderFactory(var2);
                  AttributeBinder var6 = var5.getAttributeBinder();
                  ConfigParser.this.stack.push(ConfigParser.this.top);
                  if (ConfigParser.this.top == null) {
                     if (var6 == null) {
                        throw new ManagementException("Unable to get binder for " + var2);
                     }

                     ConfigParser.this.root = var6;
                     ConfigParser.this.rootTag = var2;
                     if ("Domain".equals(var2)) {
                        DescriptorBean var7 = ((ReadOnlyMBeanBinder)var6).getBean();
                        if (var7 instanceof DomainMBean) {
                           DomainMBean var8 = (DomainMBean)var7;
                           var8.setConfigurationVersion("8.1.0.0");
                           String var9 = version.getReleaseBuildVersion();
                           if (var9 != null) {
                              var8.setDomainVersion(var9);
                           } else {
                              var8.setDomainVersion("9.0.0.0");
                           }
                        }
                     }
                  }

                  ReadOnlyMBeanBinder var12 = (ReadOnlyMBeanBinder)ConfigParser.this.top;
                  if (var12 != null) {
                     var6.bindAttribute("Parent", var12.getBean());
                  }

                  ConfigParser.this.top = var6;

                  for(int var13 = var4.getLength() - 1; var13 >= 0; --var13) {
                     ConfigParser.this.logIf90((ReadOnlyMBeanBinder)var6, var2, var4.getQName(var13));
                     var6.bindAttribute(var4.getQName(var13), var4.getValue(var13));
                  }

                  this.logDeprecationInfo((ReadOnlyMBeanBinder)var6);
               } catch (ClassNotFoundException var10) {
                  ManagementLogger.logOldSecurityProvidersFound();
                  throw new AssertionError(var10);
               } catch (Throwable var11) {
                  throw new AssertionError(var11);
               }
            }
         }

         public void endElement(String var1, String var2, String var3) {
            if (!ConfigParser.isIgnoredElement(var2)) {
               try {
                  AttributeBinder var4 = (AttributeBinder)ConfigParser.this.stack.pop();
                  if (var4 != null) {
                     ConfigParser.this.logIf90((ReadOnlyMBeanBinder)var4, var2, var3);
                     var4.bindAttribute(var2, ConfigParser.this.top);
                  }

                  ConfigParser.this.top = var4;
               } catch (Throwable var5) {
                  throw new AssertionError(var5);
               }
            }
         }

         public void characters(char[] var1, int var2, int var3) {
            String var4 = (new String(var1, var2, var3)).trim();
            if (var4.length() > 0) {
               ManagementLogger.logExtraneousConfigText(var4);
            }

         }

         private void logDeprecationInfo(ReadOnlyMBeanBinder var1) {
            List var2 = var1.getDeprecationInfo();
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ReadOnlyMBeanBinder.DeprecationInfo var4 = (ReadOnlyMBeanBinder.DeprecationInfo)var3.next();
               ManagementLogger.logDeprecatedConfigurationProperty(var4.getName(), var4.getType(), var4.getVersion(), var4.getMessage());
            }

         }
      });

      String var4;
      try {
         Object var24 = var1 ? ConfigParser.EDITABLE_DESCRIPTOR_MANAGER_SINGLETON.instance : ConfigParser.READONLY_DESCRIPTOR_MANAGER_SINGLETON.instance;
         DescriptorImpl.beginConstruction(var1, (BasicDescriptorManager)var24);
         var4 = null;
         if (this.config != null) {
            this.is = new FileInputStream(this.config);
            this.is = this.insert70SecurityData(this.is);
         }

         this.is = this.transform(this.is);

         DescriptorImpl var25;
         try {
            var2.parse(new InputSource(this.is));
         } finally {
            var25 = (DescriptorImpl)DescriptorImpl.endConstruction(this.getRoot());
         }

         this.fixupRoot();
         var25.validate();
         var25.setModified(false);
      } catch (FileNotFoundException var22) {
         var4 = (new File(".")).getAbsolutePath();
         throw new ManagementException("The configuration file " + this.config + " does not exist. (pwd=" + var4 + ")", var22);
      } catch (Throwable var23) {
         if (var23 instanceof ManagementException) {
            throw (ManagementException)var23;
         } else if (var23 instanceof AssertionError && ((AssertionError)var23).getCause() instanceof ClassNotFoundException) {
            throw new ManagementException(ManagementLogger.logOldSecurityProvidersFoundLoggable().getMessage(), var23);
         } else {
            var4 = "Error parsing the configuration file " + this.config + ": " + var23.getMessage();
            throw new ManagementException(var4, var23);
         }
      }
   }

   private void logIf90(ReadOnlyMBeanBinder var1, String var2, String var3) throws ManagementException {
      BeanInfoAccess var4 = ManagementServiceClient.getBeanInfoAccess();
      BeanInfo var5 = null;
      if (var4 != null) {
         var5 = var4.getBeanInfoForDescriptorBean(var1.getBean());
      }

      if (var5 != null) {
         PropertyDescriptor var6 = var4.getPropertyDescriptor(var5, var3);
         if (var6 == null) {
            var3 = this.pluralize(var3);
            var6 = var4.getPropertyDescriptor(var5, var3);
         }

         if (var6 != null) {
            String var7 = (String)var6.getValue("since");
            this.checkVersion(var7, var2, var3);
         }

      }
   }

   private String pluralize(String var1) {
      if (var1 == null) {
         return var1;
      } else {
         if (var1.endsWith("s")) {
            var1 = var1 + "es";
         } else if (var1.endsWith("y")) {
            var1 = var1.substring(0, var1.length() - 1);
            var1 = var1 + "ies";
         } else {
            var1 = var1 + "s";
         }

         return var1;
      }
   }

   private void checkVersion(String var1, String var2, String var3) throws ManagementException {
      if (var1 != null) {
         if (var1 != null) {
            VersionInfo var4 = new VersionInfo(var1);
            if (var4.laterThan(lastEight) && !this.skipVersionCheck(var2, var3)) {
               String var5 = ManagementLogger.logAttributeNotValidBefore90Loggable(var2, var3).getMessage();
               if (this.logHandler == null) {
                  ManagementLogger.logAttributeNotValidBefore90(var2, var3);
               } else {
                  this.logHandler.log(var5);
               }

               if (!allow90Attributes) {
                  throw new ManagementException(var5);
               }
            }
         }

      }
   }

   private boolean skipVersionCheck(String var1, String var2) {
      for(int var3 = 0; var3 < this.skipList.length; ++var3) {
         if (this.skipList[var3][0].equals(var1) && this.skipList[var3][1].equals(var2)) {
            return true;
         }
      }

      return false;
   }

   private void fixupRoot() {
      DomainMBean var1 = this.getRoot();
      if (var1 != null) {
         if (var1.getName() == null) {
            try {
               String var2 = BootStrap.getDomainName();
               if (var2 == null) {
                  var2 = "mydomain";
               }

               var1.setName(var2);
            } catch (InvalidAttributeValueException var3) {
            } catch (ManagementException var4) {
            }
         }

      }
   }

   private InputStream transform(InputStream var1) throws ManagementException {
      ConfigTransformer var2 = new ConfigTransformer(transformScripts);
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      var2.transform(var1, var3);
      return new ByteArrayInputStream(var3.toByteArray());
   }

   public DomainMBean getRoot() {
      return this.root == null ? null : (DomainMBean)((ReadOnlyMBeanBinder)this.root).getBean();
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.out.println("java " + ConfigParser.class.getName() + " <filename>");
         System.exit(1);
      }

      File var1 = new File(var0[0]);
      ConfigParser var2 = new ConfigParser(var1);
      var2.parse(false);
   }

   private static boolean isIgnoredElement(String var0) {
      if (var0 != null && ignoredElements != null) {
         for(int var1 = 0; var1 < ignoredElements.length; ++var1) {
            if (var0.equals(ignoredElements[var1])) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   static {
      transformScripts = ConfigFileHelperConstants.UPGRADE_XSLT_SCRIPTS;
      ignoredElements = new String[]{"Administrator", "ApplicationConfiguration", "JTARecoveryService", "ApplicationManager"};
      lastEight = new VersionInfo("8.9.9.9");
   }

   private class InputStreamWrapper extends InputStream {
      InputStream is;

      public InputStreamWrapper(InputStream var2) {
         this.is = var2;
      }

      public int read() throws IOException {
         int var1 = this.is.read();
         System.err.print(",");
         if (var1 > 127 || var1 <= 0) {
            System.err.print("<" + var1 + ">");
         }

         System.err.write(var1);
         return var1;
      }

      public void close() throws IOException {
         this.is.close();
         super.close();
      }
   }

   private static class EDITABLE_DESCRIPTOR_MANAGER_SINGLETON {
      static EditableDescriptorManager instance = new EditableDescriptorManager();
   }

   private static class READONLY_DESCRIPTOR_MANAGER_SINGLETON {
      static DescriptorManager instance = new DescriptorManager();
   }
}
