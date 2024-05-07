package weblogic.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.CachingDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.PersistenceBean;
import weblogic.j2ee.descriptor.PersistenceUnitBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.jars.VirtualJarFile;

public final class PersistenceDescriptorLoader extends CachingDescriptorLoader2 {
   private static boolean isDebug = Boolean.getBoolean("weblogic.deployment.PersistenceDescriptor");
   private final URL resourceURL;
   public static final String PERSISTENCE_RESOURCE_URI = "META-INF/persistence.xml";
   public static final String PERSISTENCE_CONFIG_RESOURCE_URI = "META-INF/persistence-configuration.xml";

   public PersistenceDescriptorLoader(URL var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super((VirtualJarFile)null, var2, var3, var4, var5);
      this.resourceURL = var1;
   }

   public PersistenceDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super(var1, var2, var3, var4, var5);
      this.resourceURL = null;
   }

   public InputStream getInputStream() throws IOException {
      return this.resourceURL != null ? this.resourceURL.openStream() : super.getInputStream();
   }

   protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
      if (this.isPersistenceConfigurationDescriptor()) {
         String var2 = "kodo.jdbc.conf.descriptor.PersistenceConfigurationBeanImpl$SchemaHelper2";
         return new VersionMunger(var1, this, var2, "http://xmlns.oracle.com/weblogic/persistence-configuration");
      } else {
         return new PersistenceReader(var1, this);
      }
   }

   public DescriptorBean loadDescriptorBean() throws IOException, XMLStreamException {
      DescriptorBean var1 = super.loadDescriptorBean();
      if (var1 != null && !this.isPersistenceConfigurationDescriptor()) {
         PersistenceBean var2 = (PersistenceBean)var1;
         PersistenceUnitBean var3 = var2.lookupPersistenceUnit("__ORACLE_WLS_INTERNAL_DUMMY_PERSISTENCE_UNIT");
         if (var3 != null) {
            var2.destroyPersistenceUnit(var3);
         }

         PersistenceReader var4 = (PersistenceReader)this.getMunger();
         var2.setOriginalVersion(var4.getOriginalVersion());
         return var1;
      } else {
         return var1;
      }
   }

   private boolean isPersistenceConfigurationDescriptor() {
      return this.getDocumentURI() != null && this.getDocumentURI().endsWith("META-INF/persistence-configuration.xml");
   }

   public static void debug(String var0, Exception var1) {
      if (isDebug) {
         System.out.println(var0);
         if (var1 != null) {
            var1.printStackTrace();
         }
      }

   }

   public static void debug(String var0) {
      debug(var0, (Exception)null);
   }

   public static URI getRelativeURI(URI var0, URI var1) {
      URI var2 = var0.relativize(var1);
      debug("Relativizing " + var1 + " to " + var0);
      if (var2.equals(var1)) {
         debug("No initial match, schemes are " + var1.getScheme() + " and " + var0.getScheme());
         if ((var1.getScheme().equals("jar") || var1.getScheme().equals("zip")) && var0.getScheme().equals("file")) {
            debug("Schema mismatch detected");
            String var3 = var1.toString();
            String var4 = var3.substring(var3.indexOf("!") + 1);
            debug("Descriptor URI is " + var4);
            String var5 = null;
            if (var1.getScheme().equals("zip")) {
               var5 = "file:/" + var3.substring(4, var3.indexOf("!"));
            } else {
               var5 = var3.substring(4, var3.indexOf("!"));
            }

            debug("Jar URI is " + var5);

            try {
               URI var6 = new URI(var5);
               URI var7 = var0.relativize(var6);
               debug("Relative jar URI is " + var7.toString());
               return new URI(var7 + "!" + var4);
            } catch (URISyntaxException var8) {
               debug("Unable to relativize URI", var8);
               return var2;
            }
         }
      }

      return var2;
   }

   public static URI getRelativeURI(File[] var0, URI var1) {
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            URI var3 = getRelativeURI(var0[var2].toURI(), var1);
            if (!var3.equals(var1)) {
               return var3;
            }
         }
      }

      return var1;
   }

   public static URI getResourceURI(URL var0) throws IOException {
      String var1 = var0.getFile();
      if ("jar".equals(var0.getProtocol())) {
         var1 = var1.substring(5);
      }

      return (new File(var1)).getCanonicalFile().toURI();
   }

   public static void main(String[] var0) throws IOException, XMLStreamException, URISyntaxException {
      if (var0.length < 1) {
         System.out.println("Usage: java weblogic.deployment.PersistenceDescriptorLoader [descriptor-uri] [plan path] [module] [root-uri]");
      } else {
         String var1 = var0[0];
         DeploymentPlanBean var2 = null;
         String var3 = null;
         ClassLoader var4 = Thread.currentThread().getContextClassLoader();
         URI var5 = (new File(".")).toURI();
         if (var0.length > 1) {
            DescriptorManager var6 = new DescriptorManager();
            var2 = (DeploymentPlanBean)var6.createDescriptor(new FileInputStream(var0[1])).getRootBean();
            if (var0.length > 2) {
               var3 = var0[2];
               if (var0.length > 3) {
                  var5 = (new File(var0[3])).toURI();
               }
            }
         }

         Enumeration var13 = var4.getResources(var1);

         while(var13.hasMoreElements()) {
            URL var7 = (URL)var13.nextElement();
            URI var8 = var7.toURI();
            String var9 = var7.toString();
            String var10 = getRelativeURI(var5, var8).toString();
            System.out.println("Trying to load URL " + var9 + " aka " + var10);
            PersistenceDescriptorLoader var11 = new PersistenceDescriptorLoader(var7, (File)null, var2, var3, var10);
            DescriptorBean var12 = var11.loadDescriptorBean();
            DescriptorUtils.writeAsXML(var12);
         }
      }

   }
}
