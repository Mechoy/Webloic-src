package weblogic.connector.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader;
import weblogic.application.descriptor.BasicMunger;
import weblogic.application.descriptor.NamespaceURIMunger;
import weblogic.connector.common.JCAConnectionFactoryRegistry;
import weblogic.connector.exception.RAConfigurationException;
import weblogic.connector.exception.WLRAConfigurationException;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class ConnectorDescriptor {
   public static final String STANDARD_DD_FILENAME = "ra.xml";
   public static final String WEBLOGIC_DD_FILENAME = "weblogic-ra.xml";
   public static final String STANDARD_DD = "META-INF/ra.xml";
   public static final String WEBLOGIC_DD = "META-INF/weblogic-ra.xml";
   private MyConnectorDescriptor connectorDescriptor;
   private MyWlsConnectorDescriptor wlsConnectorDescriptor;
   private AtomicBoolean registeredConnectionFactory;

   public ConnectorDescriptor(File var1, VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5) {
      this.registeredConnectionFactory = new AtomicBoolean(false);
      if (var1 != null) {
         this.connectorDescriptor = new MyConnectorDescriptor(var1);
      } else {
         this.connectorDescriptor = new MyConnectorDescriptor(var2);
      }

      this.wlsConnectorDescriptor = new MyWlsConnectorDescriptor(var2, var3, var4, var5);
   }

   public ConnectorDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
      this.registeredConnectionFactory = new AtomicBoolean(false);
      if (var1 != null && !var1.getName().endsWith("weblogic-ra.xml")) {
         this.connectorDescriptor = new MyConnectorDescriptor(var1);
      } else {
         this.wlsConnectorDescriptor = new MyWlsConnectorDescriptor(var1, var2, var3, var4);
      }

   }

   public ConnectorDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      this.registeredConnectionFactory = new AtomicBoolean(false);
      this.connectorDescriptor = new MyConnectorDescriptor(var1, var2);
      this.wlsConnectorDescriptor = new MyWlsConnectorDescriptor(var1, var2, var3, var4, var5);
   }

   public ConnectorDescriptor(DescriptorManager var1, GenericClassLoader var2) {
      this(var1, var2, true);
   }

   public ConnectorDescriptor(DescriptorManager var1, GenericClassLoader var2, boolean var3) {
      this.registeredConnectionFactory = new AtomicBoolean(false);
      this.connectorDescriptor = new MyConnectorDescriptor(var1, var2);
      this.wlsConnectorDescriptor = new MyWlsConnectorDescriptor(var1, var2, var3);
   }

   public void mergeConnector(VirtualJarFile var1) throws IOException, XMLStreamException {
      MyConnectorDescriptor var2 = new MyConnectorDescriptor(var1);
      this.connectorDescriptor.getMergedDescriptorBean(var2);
      MyWlsConnectorDescriptor var3 = new MyWlsConnectorDescriptor(var1, (File)null, (DeploymentPlanBean)null, (String)null);
      this.wlsConnectorDescriptor.getMergedDescriptorBean(var3);
   }

   public ConnectorBean getConnectorBean() throws RAConfigurationException {
      try {
         ConnectorBean var1 = this.connectorDescriptor == null ? null : (ConnectorBean)this.connectorDescriptor.getRootDescriptorBean();
         if (this.registeredConnectionFactory.compareAndSet(false, true)) {
            JCAConnectionFactoryRegistry.getInstance().registerConnectionFactory(var1);
         }

         return var1;
      } catch (IOException var2) {
         throw new RAConfigurationException(var2);
      } catch (XMLStreamException var3) {
         throw new RAConfigurationException(var3);
      }
   }

   public WeblogicConnectorBean getWeblogicConnectorBean() throws WLRAConfigurationException {
      try {
         if (this.wlsConnectorDescriptor == null) {
            return null;
         } else {
            return this.wlsConnectorDescriptor.getDeploymentPlan() != null ? (WeblogicConnectorBean)this.wlsConnectorDescriptor.getPlanMergedDescriptorBean() : (WeblogicConnectorBean)this.wlsConnectorDescriptor.getRootDescriptorBean();
         }
      } catch (IOException var2) {
         throw new WLRAConfigurationException(var2);
      } catch (XMLStreamException var3) {
         throw new WLRAConfigurationException(var3);
      }
   }

   public AbstractDescriptorLoader getWlsRaDescriptorLoader() {
      return this.wlsConnectorDescriptor;
   }

   public AbstractDescriptorLoader getRaDescriptorLoader() {
      return this.connectorDescriptor;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      if (var0[0].lastIndexOf("create") > -1) {
         DescriptorManager var1 = new DescriptorManager();
         Descriptor var2 = var1.createDescriptorRoot(ConnectorBean.class);
         var2.toXML(System.out);
         System.out.println("\n\n\n");
         var2 = var1.createDescriptorRoot(WeblogicConnectorBean.class);
         var2.toXML(System.out);
         System.exit(0);
      }

      try {
         String var11 = var0[0];
         File var12 = new File(var11);
         WeblogicConnectorBean var7;
         if (var12.getName().endsWith(".rar")) {
            JarFile var3 = new JarFile(var11);
            VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(var3);
            System.out.println("\n\n... getting ConnectorBean:");
            ConnectorDescriptor var5 = new ConnectorDescriptor((File)null, var4, (File)null, (DeploymentPlanBean)null, (String)null);
            ConnectorBean var6 = var5.getConnectorBean();
            if (var6 != null) {
               ((DescriptorBean)var6).getDescriptor().toXML(System.out);
            } else {
               System.out.println("... connector bean was null");
            }

            System.out.println("\n\n... getting WeblogicConnectorBean:");
            var7 = (new ConnectorDescriptor((File)null, var4, (File)null, (DeploymentPlanBean)null, (String)null)).getWeblogicConnectorBean();
            ((DescriptorBean)var7).getDescriptor().toXML(System.out);
            if (var0.length > 1) {
               JarFile var8 = new JarFile(var0[1]);
               VirtualJarFile var9 = VirtualJarFactory.createVirtualJar(var8);
               System.out.println("\n\n... getting 2nd ConnectorBean:");
               var5.mergeConnector(var9);
               ((DescriptorBean)var5.getConnectorBean()).getDescriptor().toXML(System.out);
            }
         } else if (var12.getPath().endsWith("weblogic-ra.xml")) {
            System.out.println("\n\n... getting WeblogicConnectorBean from: " + var12);
            WeblogicConnectorBean var13 = (new ConnectorDescriptor(var12, (File)null, (DeploymentPlanBean)null, (String)null)).getWeblogicConnectorBean();
            ((DescriptorBean)var13).getDescriptor().toXML(System.out);
            if (var0.length > 1) {
               File var15 = new File(var0[1]);
               if (var15.getPath().endsWith(".xml")) {
                  System.out.println("\n\n... plan:");
                  DeploymentPlanDescriptorLoader var16 = new DeploymentPlanDescriptorLoader(var15);
                  DeploymentPlanBean var17 = var16.getDeploymentPlanBean();
                  ((DescriptorBean)var17).getDescriptor().toXML(System.out);
                  if (var0.length > 2) {
                     var7 = null;
                     File var19 = null;
                     if (var17.getConfigRoot() != null) {
                        var19 = new File(var17.getConfigRoot());
                     }

                     ConnectorDescriptor var18;
                     if (var11.startsWith("null")) {
                        var18 = new ConnectorDescriptor((File)null, var19, var17, var0[2]);
                     } else {
                        var18 = new ConnectorDescriptor(var12, var19, var17, var0[2]);
                     }

                     System.out.println("\n\n... plan merged:");
                     ((DescriptorBean)var18.getWeblogicConnectorBean()).getDescriptor().toXML(System.out);
                  } else {
                     System.out.println("\n\nNO MODULE NAME\n\nusage: java weblogic.connector.configuration.ConnectorDescriptor weblogic-ra.xml plan.xml module-name");
                  }
               } else {
                  System.out.println("\n\nPlan file '" + var15.getPath() + "' does not have .xml extension.  Will not attempt merge.");
               }
            }
         } else if (var12.getPath().endsWith("ra.xml")) {
            System.out.println("\n\n... getting ConnectorBean:");
            ConnectorBean var14 = (new ConnectorDescriptor(var12, (File)null, (DeploymentPlanBean)null, (String)null)).getConnectorBean();
            ((DescriptorBean)var14).getDescriptor().toXML(System.out);
         } else {
            System.out.println("\n\n... neither ra nor weblogic-rxa xml specified");
         }
      } catch (Exception var10) {
         System.out.println(var10.toString());
         System.out.println(var10.getMessage());
         System.out.println(var10.getCause());
         var10.printStackTrace();
      }

   }

   private static void usage() {
      System.err.println("usage: java weblogic.connector.configuration.ConnectorDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.connector.configuration.ConnectorDescriptor jar or altDD file name ");
      System.exit(0);
   }

   private class MyWlsConnectorDescriptor extends AbstractDescriptorLoader {
      private boolean createExtensionBean;

      MyWlsConnectorDescriptor(VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var2, var3, var4, var5);
         this.createExtensionBean = true;
      }

      MyWlsConnectorDescriptor(File var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var2, var3, var4, var5);
         this.createExtensionBean = true;
      }

      MyWlsConnectorDescriptor(DescriptorManager var2, GenericClassLoader var3, File var4, DeploymentPlanBean var5, String var6) {
         super(var2, var3, var4, var5, var6);
         this.createExtensionBean = true;
      }

      MyWlsConnectorDescriptor(DescriptorManager var2, GenericClassLoader var3) {
         super(var2, var3);
         this.createExtensionBean = true;
      }

      MyWlsConnectorDescriptor(DescriptorManager var2, GenericClassLoader var3, boolean var4) {
         super(var2, var3);
         this.createExtensionBean = var4;
      }

      public String getDocumentURI() {
         return "META-INF/weblogic-ra.xml";
      }

      protected BasicMunger createXMLStreamReader(InputStream var1) throws XMLStreamException {
         ConnectorBean var2 = null;

         try {
            var2 = ConnectorDescriptor.this.getConnectorBean();
         } catch (RAConfigurationException var4) {
         }

         return new WlsRAReader(var2, this.createXMLStreamReaderDelegate(var1), this, this.getDeploymentPlan(), this.getModuleName(), this.getDocumentURI(), this.createExtensionBean);
      }

      public XMLStreamReader createXMLStreamReaderDelegate(InputStream var1) throws XMLStreamException {
         String[] var2 = new String[]{"http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/weblogic-connector"};
         return new NamespaceURIMunger(var1, "http://xmlns.oracle.com/weblogic/weblogic-connector", var2);
      }
   }

   private class MyConnectorDescriptor extends AbstractDescriptorLoader {
      MyConnectorDescriptor(File var2) {
         super(var2);
      }

      MyConnectorDescriptor(VirtualJarFile var2) {
         super(var2);
      }

      MyConnectorDescriptor(DescriptorManager var2, GenericClassLoader var3) {
         super(var2, var3);
      }

      public String getDocumentURI() {
         return "META-INF/ra.xml";
      }

      protected BasicMunger createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new RAReader(this.createXMLStreamReaderDelegate(var1), this);
      }
   }
}
