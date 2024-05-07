package weblogic.jdbc.module;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.configuration.JDBCDataSourceMBean;
import weblogic.management.configuration.JDBCMultiPoolMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JDBCTxDataSourceMBean;
import weblogic.utils.classloaders.GenericClassLoader;

public class JDBCDeploymentHelper {
   private String uri = null;
   private AbstractDescriptorLoader2 jdbcDescriptor = null;
   private String appName = null;
   private String jdbcFileName;
   private final boolean debug = false;

   public JDBCDataSourceBean createJDBCDataSourceDescriptor(InputStream var1, DescriptorManager var2, List var3, boolean var4) throws ModuleException {
      try {
         this.jdbcDescriptor = createDescriptorLoader(var1, var2, var3, var4);
         return this.getJDBCDataSourceBean();
      } catch (Exception var6) {
         throw new ModuleException(var6);
      }
   }

   public JDBCDataSourceBean createJDBCDataSourceDescriptor(String var1) throws ModuleException {
      if (var1 == null) {
         throw new ModuleException("Null URI specified");
      } else {
         Object var2 = null;

         JDBCDataSourceBean var3;
         try {
            this.jdbcDescriptor = createDescriptorLoader(new File(var1), (File)null, (DeploymentPlanBean)null, (String)null, var1);
            var3 = this.getJDBCDataSourceBean();
         } catch (Exception var12) {
            throw new ModuleException(var12);
         } finally {
            try {
               ((FileInputStream)var2).close();
            } catch (Exception var11) {
            }

         }

         return var3;
      }
   }

   public JDBCDataSourceBean createJDBCDataSourceDescriptor(ApplicationContextInternal var1, String var2) throws ModuleException {
      this.uri = var2;
      DeploymentPlanBean var3 = null;
      File var4 = null;
      if (var2 == null) {
         throw new ModuleException("Null URI specified");
      } else {
         AppDeploymentMBean var5 = var1.getAppDeploymentMBean();
         String var6 = null;
         String var7 = null;
         if (var5 != null) {
            var3 = var5.getDeploymentPlanDescriptor();
            if (var5.getPlanDir() != null) {
               var4 = new File(var5.getLocalPlanDir());
            }

            this.appName = var5.getName();
            var6 = this.getModuleName(var5, var2);
            var7 = this.getModuleUri(var5, var2);
         }

         File var8 = null;
         String var9 = this.getCanonicalPath(var1, var2);
         if (var9 != null) {
            var8 = new File(var9);
         }

         this.jdbcDescriptor = createDescriptorLoader(var8, var4, var3, var6, var7);

         try {
            return this.getJDBCDataSourceBean();
         } catch (Exception var11) {
            throw new ModuleException(var11);
         }
      }
   }

   private String getModuleUri(AppDeploymentMBean var1, String var2) {
      return var1.getSourcePath() != null && var1.getSourcePath().endsWith(".xml") ? "." : var2;
   }

   private String getModuleName(AppDeploymentMBean var1, String var2) {
      return var1.getSourcePath() != null ? (new File(var1.getSourcePath())).getName() : var2;
   }

   public JDBCDataSourceBean createJDBCDataSourceDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, String var6) throws IOException, XMLStreamException {
      this.jdbcDescriptor = createDescriptorLoader(var1, var2, var3, var4, var5, var6);
      return this.getJDBCDataSourceBean();
   }

   public JDBCDataSourceBean createJDBCDataSourceDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) throws IOException, XMLStreamException {
      this.jdbcDescriptor = createDescriptorLoader(var1, var2, var3, var4, var5);
      return this.getJDBCDataSourceBean();
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(File var0, File var1, DeploymentPlanBean var2, String var3, String var4) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3, var4) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return JDBCDeploymentHelper.createVersionMunger(var1, this);
         }
      };
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(DescriptorManager var0, GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3, var4, var5) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return JDBCDeploymentHelper.createVersionMunger(var1, this);
         }
      };
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(InputStream var0, DescriptorManager var1, List var2, boolean var3) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return JDBCDeploymentHelper.createVersionMunger(var1, this);
         }
      };
   }

   private static VersionMunger createVersionMunger(InputStream var0, AbstractDescriptorLoader2 var1) throws XMLStreamException {
      String var2 = "weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanImpl$SchemaHelper2";
      return new VersionMunger(var0, var1, var2, "http://xmlns.oracle.com/weblogic/jdbc-data-source");
   }

   public String getCanonicalPath(ApplicationContextInternal var1, String var2) throws ModuleException {
      if (var1.isEar()) {
         File[] var3 = var1.getEar().getModuleRoots(var2);
         if (var3.length == 0) {
            return null;
         } else {
            File var4 = var3[0];
            return var4.getAbsolutePath().replace(File.separatorChar, '/');
         }
      } else {
         return var1.getStagingPath();
      }
   }

   public JDBCDataSourceBean getJDBCDataSourceBean() throws IOException, XMLStreamException {
      return (JDBCDataSourceBean)this.jdbcDescriptor.loadDescriptorBean();
   }

   public JDBCSystemResourceMBean createJDBCSystemResource(DeploymentMBean var1, int var2, DomainMBean var3) throws Exception {
      String var4 = getSystemResourceName(var1.getName(), var2);
      if (var3.lookupJDBCSystemResource(var4) != null) {
         return null;
      } else {
         JDBCSystemResourceMBean var5 = var3.createJDBCSystemResource(var4);
         var5.setDeploymentOrder(var2);
         JDBCDataSourceBean var6 = var5.getJDBCResource();
         if (var2 == 1) {
            JDBCMBeanConverter.getJDBCDataSourceDescriptor((JDBCConnectionPoolMBean)var1, var6);
         } else if (var2 == 2) {
            JDBCMBeanConverter.getJDBCDataSourceDescriptor((JDBCMultiPoolMBean)var1, var6);
         } else if (var2 == 3) {
            JDBCMBeanConverter.getJDBCDataSourceDescriptor((JDBCDataSourceMBean)var1, var6);
         } else if (var2 == 4) {
            JDBCMBeanConverter.getJDBCDataSourceDescriptor((JDBCTxDataSourceMBean)var1, var6, var3);
         }

         return var5;
      }
   }

   public static String getSystemResourceName(String var0, int var1) {
      if (var1 == 1) {
         return new String("CP-" + var0);
      } else if (var1 == 2) {
         return new String("MP-" + var0);
      } else if (var1 == 3) {
         return new String("DS-" + var0);
      } else {
         return var1 == 4 ? new String("TxDS-" + var0) : new String("");
      }
   }

   public static void writeModuleAsXML(DescriptorBean var0) {
      Descriptor var1 = var0.getDescriptor();

      try {
         EditableDescriptorManager var2 = new EditableDescriptorManager();
         var2.writeDescriptorAsXML(var1, new BufferedOutputStream(System.out) {
            public void close() {
            }
         });
      } catch (IOException var3) {
      }

   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      System.out.println("\n\n... getting InterceptionBean:");
      ((DescriptorBean)(new JDBCDeploymentHelper()).createJDBCDataSourceDescriptor(var0[0])).getDescriptor().toXML(System.out);
   }

   private static void usage() {
      System.err.println("usage: java weblogic.jdbc.module.JDBCDeploymentHelper <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.jdbc.module.JDBCDeploymentHelper sample.xml");
      System.exit(0);
   }
}
