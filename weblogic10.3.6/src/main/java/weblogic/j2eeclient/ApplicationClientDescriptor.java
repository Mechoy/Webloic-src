package weblogic.j2eeclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ApplicationClientBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationClientBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class ApplicationClientDescriptor {
   private final MyApplicationClientDescriptor appClientDescriptor;
   private final MyWlsApplicationClientDescriptor wlsAppClientDescriptor;
   private static final Map wlNameChanges = new HashMap();

   public ApplicationClientDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      this.appClientDescriptor = new MyApplicationClientDescriptor(var1);
      this.wlsAppClientDescriptor = new MyWlsApplicationClientDescriptor(var1, var2, var3, var4);
   }

   public ApplicationClientDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
      this.appClientDescriptor = new MyApplicationClientDescriptor(var1);
      this.wlsAppClientDescriptor = new MyWlsApplicationClientDescriptor(var1, var2, var3, var4);
   }

   public ApplicationClientDescriptor(GenericClassLoader var1) {
      this.appClientDescriptor = new MyApplicationClientDescriptor(var1);
      this.wlsAppClientDescriptor = new MyWlsApplicationClientDescriptor(var1);
   }

   public ApplicationClientDescriptor(GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4) {
      this.appClientDescriptor = new MyApplicationClientDescriptor(var1);
      this.wlsAppClientDescriptor = new MyWlsApplicationClientDescriptor(var1, var2, var3, var4);
   }

   public ApplicationClientDescriptor(DescriptorManager var1, GenericClassLoader var2) {
      this.appClientDescriptor = new MyApplicationClientDescriptor(var1, var2);
      this.wlsAppClientDescriptor = new MyWlsApplicationClientDescriptor(var1, var2);
   }

   public ApplicationClientDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      this.appClientDescriptor = new MyApplicationClientDescriptor(var1, var2);
      this.wlsAppClientDescriptor = new MyWlsApplicationClientDescriptor(var1, var2, var3, var4, var5);
   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.appClientDescriptor.getDeploymentPlan();
   }

   public ApplicationClientBean getApplicationClientBean() throws IOException, XMLStreamException {
      ApplicationClientBean var1 = (ApplicationClientBean)this.appClientDescriptor.loadDescriptorBean();
      if (var1 == null) {
         var1 = (ApplicationClientBean)(new DescriptorManager()).createDescriptorRoot(ApplicationClientBean.class).getRootBean();
      }

      return var1;
   }

   public WeblogicApplicationClientBean getWeblogicApplicationClientBean() throws IOException, XMLStreamException {
      WeblogicApplicationClientBean var1 = (WeblogicApplicationClientBean)this.wlsAppClientDescriptor.loadDescriptorBean();
      return var1 == null ? (WeblogicApplicationClientBean)(new EditableDescriptorManager()).createDescriptorRoot(WeblogicApplicationClientBean.class).getRootBean() : var1;
   }

   public AbstractDescriptorLoader2 getApplicationClientDescriptorLoader() {
      return this.appClientDescriptor;
   }

   public AbstractDescriptorLoader2 getWlsApplicationClientDescriptorLoader() {
      return this.wlsAppClientDescriptor;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      String var1 = var0[0];
      File var2 = new File(var1);
      if (var2.getName().endsWith(".jar")) {
         JarFile var3 = new JarFile(var1);
         VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(var3);
         System.out.println("\n\n... getting ApplicationClientBean:");
         ApplicationClientDescriptor var5 = new ApplicationClientDescriptor(var4, (File)null, (DeploymentPlanBean)null, (String)null);
         ApplicationClientBean var6 = var5.getApplicationClientBean();
         ((DescriptorBean)var6).getDescriptor().toXML(System.out);
         System.out.println("\n\n... getting WeblogicApplicationClientBean:");
         WeblogicApplicationClientBean var7 = (new ApplicationClientDescriptor(var4, (File)null, (DeploymentPlanBean)null, (String)null)).getWeblogicApplicationClientBean();
         ((DescriptorBean)var7).getDescriptor().toXML(System.out);
      } else if (var2.getPath().endsWith("weblogic-application-client.xml")) {
         System.out.println("\n\n... getting WeblogicApplicationClientBean from: " + var2);
         WeblogicApplicationClientBean var8 = (new ApplicationClientDescriptor(var2, (File)null, (DeploymentPlanBean)null, (String)null)).getWeblogicApplicationClientBean();
         ((DescriptorBean)var8).getDescriptor().toXML(System.out);
      } else if (var2.getPath().endsWith("application-client.xml")) {
         System.out.println("\n\n... getting ApplicationClientBean:");
         ApplicationClientBean var9 = (new ApplicationClientDescriptor(var2, (File)null, (DeploymentPlanBean)null, (String)null)).getApplicationClientBean();
         ((DescriptorBean)var9).getDescriptor().toXML(System.out);
      } else {
         System.out.println("\n\n... neither application-client.xml nor weblogic-application-client.xml specified");
      }

   }

   private static void usage() {
      System.err.println("usage: java weblogic.j2eeclient.ApplicationClientDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.j2eeclient.ApplicationClientDescriptor jar or altDD file name ");
      System.exit(0);
   }

   static {
      wlNameChanges.put("application-client", "weblogic-application-client");
      wlNameChanges.put("ejb-ref", "ejb-reference-description");
      wlNameChanges.put("resource-ref", "resource-description");
      wlNameChanges.put("resource-env-ref", "resource-env-description");
   }

   private class MyWlsApplicationClientDescriptor extends MyAbstractDescriptorLoader {
      MyWlsApplicationClientDescriptor(VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5) {
         super((VirtualJarFile)var2, var3, var4, var5, "META-INF/weblogic-application-client.xml");
      }

      MyWlsApplicationClientDescriptor(File var2, File var3, DeploymentPlanBean var4, String var5) {
         super((File)var2, var3, var4, var5, "META-INF/weblogic-application-client.xml");
      }

      MyWlsApplicationClientDescriptor(GenericClassLoader var2) {
         super((GenericClassLoader)var2, "META-INF/weblogic-application-client.xml");
      }

      MyWlsApplicationClientDescriptor(DescriptorManager var2, GenericClassLoader var3) {
         super(var2, var3, "META-INF/weblogic-application-client.xml");
      }

      MyWlsApplicationClientDescriptor(GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
         this(var2);
      }

      MyWlsApplicationClientDescriptor(DescriptorManager var2, GenericClassLoader var3, File var4, DeploymentPlanBean var5, String var6) {
         this(var3);
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new VersionMunger(var1, this, "weblogic.j2ee.descriptor.wl.WeblogicApplicationClientBeanImpl$SchemaHelper2", ApplicationClientDescriptor.wlNameChanges, "http://xmlns.oracle.com/weblogic/weblogic-application-client") {
            public boolean hasDTD() {
               return true;
            }

            protected boolean lookForDTD(AbstractDescriptorLoader2 var1) throws XMLStreamException {
               return true;
            }
         };
      }
   }

   private class MyApplicationClientDescriptor extends MyAbstractDescriptorLoader {
      MyApplicationClientDescriptor(VirtualJarFile var2) {
         super((VirtualJarFile)var2, "META-INF/application-client.xml");
      }

      MyApplicationClientDescriptor(File var2) {
         super((File)var2, "META-INF/application-client.xml");
      }

      MyApplicationClientDescriptor(GenericClassLoader var2) {
         super((GenericClassLoader)var2, "META-INF/application-client.xml");
      }

      MyApplicationClientDescriptor(DescriptorManager var2, GenericClassLoader var3) {
         super(var2, var3, "META-INF/application-client.xml");
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new VersionMunger(var1, this, "weblogic.j2ee.descriptor.ApplicationClientBeanImpl$SchemaHelper2") {
            private boolean inEjbLink = false;

            public String getDtdNamespaceURI() {
               return "http://java.sun.com/xml/ns/javaee";
            }

            protected boolean isOldSchema() {
               String var1 = this.getNamespaceURI();
               return var1 != null && var1.indexOf("j2ee") != -1;
            }

            protected void transformOldSchema() {
               if (this.currentEvent.getElementName().equals("application-client")) {
                  int var1 = this.currentEvent.getReaderEventInfo().getAttributeCount();

                  for(int var2 = 0; var2 < var1; ++var2) {
                     String var3 = this.currentEvent.getReaderEventInfo().getAttributeLocalName(var2);
                     if (var3 != null && var3.equals("version")) {
                        String var4 = this.currentEvent.getReaderEventInfo().getAttributeValue(var2);
                        if (var4.equals("1.4")) {
                           this.versionInfo = var4;
                           this.currentEvent.getReaderEventInfo().setAttributeValue("5", var2);
                        }
                     }
                  }

                  this.transformNamespace("http://java.sun.com/xml/ns/javaee", this.currentEvent, "http://java.sun.com/xml/ns/j2ee");
               }

               this.tranformedNamespace = "http://java.sun.com/xml/ns/javaee";
            }

            public VersionMunger.Continuation onStartElement(String var1) {
               if ("ejb-link".equals(var1)) {
                  this.inEjbLink = true;
               }

               return CONTINUE;
            }

            protected VersionMunger.Continuation onCharacters(String var1) {
               if (this.inEjbLink) {
                  this.replaceSlashWithPeriod(this.inEjbLink);
               }

               return CONTINUE;
            }

            public VersionMunger.Continuation onEndElement(String var1) {
               if ("ejb-link".equals(var1)) {
                  this.inEjbLink = false;
               }

               return CONTINUE;
            }
         };
      }
   }

   private class MyAbstractDescriptorLoader extends AbstractDescriptorLoader2 {
      MyAbstractDescriptorLoader(VirtualJarFile var2, String var3) {
         super(var2, var3);
      }

      MyAbstractDescriptorLoader(File var2, String var3) {
         super(var2, var3);
      }

      MyAbstractDescriptorLoader(GenericClassLoader var2, String var3) {
         super(var2, var3);
      }

      MyAbstractDescriptorLoader(DescriptorManager var2, GenericClassLoader var3, String var4) {
         super(var2, var3, var4);
      }

      MyAbstractDescriptorLoader(VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
         super(var2, var3, var4, var5, var6);
      }

      MyAbstractDescriptorLoader(File var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
         super(var2, var3, var4, var5, var6);
      }

      MyAbstractDescriptorLoader(GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
         this((GenericClassLoader)var2, var6);
      }

      MyAbstractDescriptorLoader(DescriptorManager var2, GenericClassLoader var3, File var4, DeploymentPlanBean var5, String var6, String var7) {
         this((GenericClassLoader)var3, var7);
      }

      protected DescriptorBean createDescriptorBean(InputStream var1) throws IOException, XMLStreamException {
         this.munger = (VersionMunger)this.createXMLStreamReader(var1);
         return !this.munger.hasDTD() && !(this.munger instanceof VersionMunger) ? this.getDescriptorManager().createDescriptor(this.munger).getRootBean() : this.getDescriptorManager().createDescriptor(this.munger.getPlaybackReader(), false).getRootBean();
      }
   }
}
