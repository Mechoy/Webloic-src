package weblogic.messaging.interception.module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.InterceptionBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.classloaders.GenericClassLoader;

public class InterceptionParser {
   private AbstractDescriptorLoader2 interceptionDescriptor = null;
   private String interceptionFileName;
   private final boolean debug = false;

   public InterceptionBean createInterceptionDescriptor(String var1) throws ModuleException {
      if (var1 == null) {
         throw new ModuleException("Null URI specified");
      } else {
         if (this.interceptionDescriptor == null) {
            this.interceptionDescriptor = createDescriptorLoader(new File(var1), (File)null, (DeploymentPlanBean)null, (String)null, (String)null);
         }

         try {
            return this.getInterceptionBean();
         } catch (Exception var3) {
            throw new ModuleException(var3.toString(), var3);
         }
      }
   }

   public InterceptionBean createInterceptionDescriptor(ApplicationContextInternal var1, String var2) throws ModuleException {
      if (var2 == null) {
         throw new ModuleException("Null URI specified");
      } else {
         AppDeploymentMBean var3 = var1.getAppDeploymentMBean();
         DeploymentPlanBean var4 = null;
         File var5 = null;
         String var6 = null;
         String var7 = null;
         if (var3 != null) {
            var4 = var3.getDeploymentPlanDescriptor();
            var5 = null;
            if (var3.getPlanDir() != null) {
               var5 = new File(var3.getLocalPlanDir());
            }

            var6 = this.getModuleName(var3, var2);
            var7 = this.getModuleUri(var3, var2);
         }

         if (this.interceptionDescriptor == null) {
            this.interceptionDescriptor = createDescriptorLoader(new File(this.getCanonicalPath(var1, var2)), var5, var4, var6, var7);
         }

         try {
            return this.getInterceptionBean();
         } catch (Exception var9) {
            throw new ModuleException(var9.toString());
         }
      }
   }

   private String getModuleUri(AppDeploymentMBean var1, String var2) {
      return var1.getSourcePath() != null && var1.getSourcePath().endsWith(".xml") ? "." : var2;
   }

   private String getModuleName(AppDeploymentMBean var1, String var2) {
      return var1.getSourcePath() != null ? (new File(var1.getSourcePath())).getName() : var2;
   }

   public InterceptionBean createInterceptionDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, String var6) throws IOException, XMLStreamException {
      this.interceptionDescriptor = createDescriptorLoader(var1, var2, var3, var4, var5, var6);
      return this.getInterceptionBean();
   }

   public String getCanonicalPath(ApplicationContextInternal var1, String var2) {
      File[] var3 = var1.getApplicationPaths();
      var2 = var3[0] + "/" + var2;
      return (new File(var2)).getAbsolutePath().replace(File.separatorChar, '/');
   }

   public InterceptionBean getInterceptionBean() throws IOException, XMLStreamException {
      return (InterceptionBean)this.interceptionDescriptor.loadDescriptorBean();
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(DescriptorManager var0, GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3, var4, var5) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return InterceptionParser.createVersionMunger(var1, this);
         }
      };
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(File var0, File var1, DeploymentPlanBean var2, String var3, String var4) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3, var4) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return InterceptionParser.createVersionMunger(var1, this);
         }
      };
   }

   private static VersionMunger createVersionMunger(InputStream var0, AbstractDescriptorLoader2 var1) throws XMLStreamException {
      String var2 = "weblogic.j2ee.descriptor.wl.InterceptionBeanImpl$SchemaHelper2";
      return new VersionMunger(var0, var1, var2, "http://xmlns.oracle.com/weblogic/weblogic-interception");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      System.out.println("\n\n... getting InterceptionBean:");
      ((DescriptorBean)(new InterceptionParser()).createInterceptionDescriptor(var0[0])).getDescriptor().toXML(System.out);
   }

   private static void usage() {
      System.err.println("usage: java weblogic.messaging.interception.module.InterceptionParser <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.messaging.interception.module.InterceptionParser sample.xml");
      System.exit(0);
   }
}
