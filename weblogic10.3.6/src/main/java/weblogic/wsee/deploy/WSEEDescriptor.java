package weblogic.wsee.deploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.ReaderEvent2;
import weblogic.application.descriptor.ReaderEventInfo;
import weblogic.application.descriptor.VersionMunger;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class WSEEDescriptor {
   private static final String JAVAEE_NAMESPACE_URI = "http://java.sun.com/xml/ns/javaee";
   private static final String J2EE_NAMESPACE_URI = "http://java.sun.com/xml/ns/j2ee";
   public static final String WSEE_WAR_XML_URI = "WEB-INF/webservices.xml";
   public static final String WL_WSEE_WAR_XML_URI = "WEB-INF/weblogic-webservices.xml";
   public static final String WSEE_JAR_XML_URI = "META-INF/webservices.xml";
   public static final String WL_WSEE_JAR_XML_URI = "META-INF/weblogic-webservices.xml";
   private MyWSEEDescriptor wseeDescriptor;
   private MyWlsWSEEDescriptor wlsWseeDescriptor;

   public WSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, boolean var5) {
      this.wseeDescriptor = new MyWSEEDescriptor(var1, var2, var3, var4, var5);
      this.wlsWseeDescriptor = new MyWlsWSEEDescriptor(var1, var2, var3, var4, var5);
   }

   public WSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      this.wseeDescriptor = new MyWSEEDescriptor(var1, var2, var3, var4);
      this.wlsWseeDescriptor = new MyWlsWSEEDescriptor(var1, var2, var3, var4);
   }

   WSEEDescriptor(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4) throws IOException, XMLStreamException {
      this.wseeDescriptor = new MyWSEEDescriptor(var1, var2, var3, var4);
      this.wlsWseeDescriptor = new MyWlsWSEEDescriptor(var1, var2, var3, var4);
      this.mergeWebServicesDescriptors(var1.getResourceFinder("/"));
      this.mergeWeblogicWebServicesDescriptor(var1.getResourceFinder("/"));
   }

   public WSEEDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
      if (var1.getPath().endsWith("weblogic-webservices.xml")) {
         this.wlsWseeDescriptor = new MyWlsWSEEDescriptor(var1, var2, var3, var4);
      } else {
         this.wseeDescriptor = new MyWSEEDescriptor(var1, var2, var3, var4);
      }

   }

   public WSEEDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, boolean var6) {
      this.wseeDescriptor = new MyWSEEDescriptor(var1, var2, var6);
      this.wlsWseeDescriptor = new MyWlsWSEEDescriptor(var1, var2, var3, var4, var5, var6);
   }

   public void mergeWebServicesDescriptors(ClassFinder var1) throws IOException, XMLStreamException {
      String var2 = "/WEB-INF/webservices.xml";
      Enumeration var3 = var1.getSources(var2);
      Object[] var4 = Collections.list(var3).toArray();
      if (var4.length > 1) {
         this.wseeDescriptor.mergeDescriptors(var4);
      }

   }

   public void mergeWeblogicWebServicesDescriptor(ClassFinder var1) throws IOException, XMLStreamException {
      String var2 = "/WEB-INF/weblogic-webservices.xml";
      Enumeration var3 = var1.getSources(var2);
      Object[] var4 = Collections.list(var3).toArray();
      if (var4.length > 1) {
         this.wlsWseeDescriptor.mergeDescriptors(var4);
      }

   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.wseeDescriptor.getDeploymentPlan();
   }

   public WebservicesBean getWebservicesBean() throws IOException, XMLStreamException {
      return (WebservicesBean)this.wseeDescriptor.loadDescriptorBean();
   }

   public WeblogicWebservicesBean getWeblogicWebservicesBean() throws IOException, XMLStreamException {
      return (WeblogicWebservicesBean)this.wlsWseeDescriptor.loadDescriptorBean();
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      String var1 = var0[0];
      File var2 = new File(var1);
      if (!var2.getName().endsWith(".war") && !var2.getName().endsWith(".jar")) {
         File var14;
         DeploymentPlanDescriptorLoader var15;
         DeploymentPlanBean var16;
         WSEEDescriptor var17;
         if (var2.getPath().endsWith("weblogic-webservices.xml")) {
            System.out.println("\n\n... getting WeblogicWebservicesBean from: " + var2);
            WeblogicWebservicesBean var12 = (new WSEEDescriptor(var2, (File)null, (DeploymentPlanBean)null, (String)null)).getWeblogicWebservicesBean();
            ((DescriptorBean)var12).getDescriptor().toXML(System.out);
            if (var0.length > 1) {
               var14 = new File(var0[1]);
               if (var14.getPath().endsWith("plan.xml")) {
                  System.out.println("\n\n... plan:");
                  var15 = new DeploymentPlanDescriptorLoader(var14);
                  var16 = var15.getDeploymentPlanBean();
                  ((DescriptorBean)var16).getDescriptor().toXML(System.out);
                  var17 = new WSEEDescriptor(var2, new File(var16.getConfigRoot()), var16, var16.getApplicationName());
                  System.out.println("\n\n... plan merged WeblogicWebservicesBean with :");
                  ((DescriptorBean)var17.getWeblogicWebservicesBean()).getDescriptor().toXML(System.out);
               }
            }
         } else if (var2.getPath().endsWith("webservices.xml")) {
            System.out.println("\n\n... getting WebservicesBean:");
            WebservicesBean var13 = (new WSEEDescriptor(var2, (File)null, (DeploymentPlanBean)null, (String)null)).getWebservicesBean();
            ((DescriptorBean)var13).getDescriptor().toXML(System.out);
            if (var0.length > 1) {
               var14 = new File(var0[1]);
               if (var14.getPath().endsWith("plan.xml")) {
                  System.out.println("\n\n... plan:");
                  var15 = new DeploymentPlanDescriptorLoader(var14);
                  var16 = var15.getDeploymentPlanBean();
                  ((DescriptorBean)var16).getDescriptor().toXML(System.out);
                  var17 = new WSEEDescriptor(var2, new File(var16.getConfigRoot()), var16, var16.getApplicationName());
                  System.out.println("\n\n... plan merged WebservicesBean with :");
                  ((DescriptorBean)var17.getWebservicesBean()).getDescriptor().toXML(System.out);
               }
            }
         } else {
            System.out.println("\n\n... neither webservices.xml nor weblogic-webservices.xml specified");
         }
      } else {
         JarFile var3 = new JarFile(var1);
         VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(var3);
         System.out.println("\n\n... getting WebservicesBean:");
         WSEEDescriptor var5 = new WSEEDescriptor(var4, (File)null, (DeploymentPlanBean)null, (String)null);
         WebservicesBean var6 = var5.getWebservicesBean();
         ((DescriptorBean)var6).getDescriptor().toXML(System.out);
         System.out.println("\n\n... getting WeblogicWebservicesBean:");
         WeblogicWebservicesBean var7 = (new WSEEDescriptor(var4, (File)null, (DeploymentPlanBean)null, (String)null)).getWeblogicWebservicesBean();
         if (var7 != null) {
            ((DescriptorBean)var7).getDescriptor().toXML(System.out);
         } else {
            System.out.println("... WeblogicWebservicesBean is null");
         }

         if (var0.length > 1) {
            File var8 = new File(var0[1]);
            if (var8.getPath().endsWith("plan.xml")) {
               System.out.println("\n\n... plan:");
               DeploymentPlanDescriptorLoader var9 = new DeploymentPlanDescriptorLoader(var8);
               DeploymentPlanBean var10 = var9.getDeploymentPlanBean();
               ((DescriptorBean)var10).getDescriptor().toXML(System.out);
               System.out.println("\n\nConfig root = " + var10.getConfigRoot());
               System.out.println("\nApplication name = " + var10.getApplicationName());
               WSEEDescriptor var11 = new WSEEDescriptor(var4, new File(var10.getConfigRoot()), var10, var1);
               System.out.println("\n\n... plan merged WeblogicWebservicesBean with :");
               if (var11.getWeblogicWebservicesBean() != null) {
                  ((DescriptorBean)var11.getWeblogicWebservicesBean()).getDescriptor().toXML(System.out);
               } else {
                  System.out.println("... WeblogicWebservicesBean is null");
               }

               System.out.println("\n\n... plan merged WebservicesBean with :");
               ((DescriptorBean)var11.getWebservicesBean()).getDescriptor().toXML(System.out);
            }
         }
      }

   }

   private static void usage() {
      System.err.println("usage: java weblogic.wsee.deploy.WSEEDescriptor <descriptor file name>");
      System.err.println("\n\n example:\n java weblogic.wsee.deploy.WSEEDescriptor jar or altDD file name ");
      System.exit(0);
   }

   public static class MyWlsWSEEDescriptor extends MyAbstractDescriptorLoader {
      private MyWlsWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         super((VirtualJarFile)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      private MyWlsWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, boolean var5) {
         super((VirtualJarFile)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
         this.useWarPath = var5;
      }

      private MyWlsWSEEDescriptor(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4) {
         super((WebAppServletContext)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      private MyWlsWSEEDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         super((File)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      private MyWlsWSEEDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, boolean var6) {
         super(var1, var2, var3, var4, var5, null);
         this.useWarPath = var6;
      }

      public MyWlsWSEEDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super((File)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      public String getDocumentURI() {
         return this.useWarPath ? "WEB-INF/weblogic-webservices.xml" : "META-INF/weblogic-webservices.xml";
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         String var2 = "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanImpl$SchemaHelper2";
         return new VersionMunger(var1, this, var2, "http://xmlns.oracle.com/weblogic/weblogic-webservices");
      }

      // $FF: synthetic method
      MyWlsWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, boolean var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }

      // $FF: synthetic method
      MyWlsWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyWlsWSEEDescriptor(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyWlsWSEEDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyWlsWSEEDescriptor(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, boolean var6, Object var7) {
         this(var1, var2, var3, var4, var5, var6);
      }
   }

   public static class MyWSEEDescriptor extends MyAbstractDescriptorLoader {
      private MyWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         super((VirtualJarFile)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      private MyWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, boolean var5) {
         super((VirtualJarFile)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
         this.useWarPath = var5;
      }

      private MyWSEEDescriptor(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4) {
         super((WebAppServletContext)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      private MyWSEEDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4) {
         super((File)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      private MyWSEEDescriptor(DescriptorManager var1, GenericClassLoader var2, boolean var3) {
         super(var1, var2, null);
         this.useWarPath = var3;
      }

      public MyWSEEDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super((File)var1, (File)var2, (DeploymentPlanBean)var3, (String)var4, (<undefinedtype>)null);
      }

      public String getDocumentURI() {
         return this.useWarPath ? "WEB-INF/webservices.xml" : "META-INF/webservices.xml";
      }

      protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
         return new VersionMunger(var1, this, "weblogic.j2ee.descriptor.WebservicesBeanImpl$SchemaHelper2") {
            public String getDtdNamespaceURI() {
               return "http://java.sun.com/xml/ns/javaee";
            }

            protected boolean isOldSchema() {
               String var1 = this.getNamespaceURI();
               return var1 != null && var1.indexOf("j2ee") != -1;
            }

            private void transform(String var1, ReaderEvent2 var2) {
               ReaderEventInfo var3 = var2.getReaderEventInfo();
               int var4 = var3.getNamespaceCount();
               HashMap var5 = new HashMap();

               for(int var6 = 0; var6 < var4; ++var6) {
                  String var7 = var3.getNamespaceURI(var6);
                  if ("http://java.sun.com/xml/ns/j2ee".equals(var7)) {
                     var7 = var1;
                  }

                  String var8 = var3.getNamespacePrefix(var6);
                  var5.put(var8, var7);
               }

               this.setNamespaces(var2, var5);
            }

            private void setNamespaces(ReaderEvent2 var1, Map var2) {
               ReaderEventInfo var3 = var1.getReaderEventInfo();
               var3.clearNamespaces();
               var3.setNamespaceCount(var2.size());

               String var5;
               String var6;
               for(Iterator var4 = var2.keySet().iterator(); var4.hasNext(); var3.setNamespaceURI(var5, var6)) {
                  var5 = (String)var4.next();
                  var6 = (String)var2.get(var5);
                  if (var6.equals("http://java.sun.com/xml/ns/javaee") && var5 != null) {
                     var3.setPrefix(var5);
                  }
               }

               Iterator var7 = var1.getChildren().iterator();

               while(var7.hasNext()) {
                  ReaderEvent2 var8 = (ReaderEvent2)var7.next();
                  this.setNamespaces(var8, var2);
               }

            }

            protected void transformOldSchema() {
               if (this.currentEvent.getElementName().equals("webservices")) {
                  int var1 = this.currentEvent.getReaderEventInfo().getAttributeCount();

                  for(int var2 = 0; var2 < var1; ++var2) {
                     String var3 = this.currentEvent.getReaderEventInfo().getAttributeLocalName(var2);
                     if (var3 != null && var3.equals("version")) {
                        String var4 = this.currentEvent.getReaderEventInfo().getAttributeValue(var2);
                        if (var4.equals("1.1")) {
                           this.versionInfo = var4;
                           this.currentEvent.getReaderEventInfo().setAttributeValue("1.2", var2);
                        }
                     }
                  }

                  this.transform("http://java.sun.com/xml/ns/javaee", this.currentEvent);
               }

               this.tranformedNamespace = "http://java.sun.com/xml/ns/javaee";
            }
         };
      }

      // $FF: synthetic method
      MyWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, boolean var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }

      // $FF: synthetic method
      MyWSEEDescriptor(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyWSEEDescriptor(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyWSEEDescriptor(File var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyWSEEDescriptor(DescriptorManager var1, GenericClassLoader var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private abstract static class MyAbstractDescriptorLoader extends AbstractDescriptorLoader2 {
      boolean useWarPath;
      WebAppServletContext svltCtx;

      private MyAbstractDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
         super((VirtualJarFile)var1, var2, var3, var4, (String)null);
         this.useWarPath = false;
         if (var1.getName().endsWith(".war")) {
            this.useWarPath = true;
         }

      }

      private MyAbstractDescriptorLoader(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4) {
         super((VirtualJarFile)((VirtualJarFile)null), var2, var3, var4, (String)null);
         this.useWarPath = false;
         this.useWarPath = true;
         this.svltCtx = var1;
      }

      private MyAbstractDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4) {
         super((File)var1, var2, var3, var4, (String)null);
         this.useWarPath = false;
      }

      private MyAbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2) {
         super(var1, var2, (String)null);
         this.useWarPath = false;
      }

      private MyAbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
         super(var1, var2, var3, var4, var5, (String)null);
         this.useWarPath = false;
      }

      public InputStream getInputStream() throws IOException {
         return this.svltCtx != null ? this.svltCtx.getResourceAsStream(this.getDocumentURI()) : super.getInputStream();
      }

      // $FF: synthetic method
      MyAbstractDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyAbstractDescriptorLoader(WebAppServletContext var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyAbstractDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      // $FF: synthetic method
      MyAbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2, Object var3) {
         this(var1, var2);
      }

      // $FF: synthetic method
      MyAbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }
   }
}
