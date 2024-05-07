package weblogic.deploy.api.spi.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.application.descriptor.NamespaceURIMunger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.ejb.spi.XMLConstants;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.management.ManagementException;
import weblogic.utils.io.StreamUtils;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public class DescriptorParser {
   private static final boolean ddebug = Debug.isDebug("utils");
   private static final String VERSION = "version";
   private Document doc = null;
   private final ByteArrayCache ddStreamCache;
   private String version = null;
   private boolean hasSchema = true;
   private static EditableDescriptorManager edm = null;
   private static final String DEFAULT_ENCODING = "UTF-8";
   private DescriptorSupport ds;
   private DeploymentPlanBean plan = null;
   private File configDir = null;
   File ddFile = null;
   private static Map map = new HashMap();

   public DescriptorSupport getDescriptorSupport() {
      return this.ds;
   }

   private DescriptorParser(InputStream var1, DeploymentPlanBean var2, DescriptorSupport var3) throws IOException {
      this.plan = var2;
      if (var1 == null) {
         throw new IOException("Valid Stream is required for descriptor");
      } else {
         this.ddStreamCache = new ByteArrayCache(var1);
         this.ds = var3;
         this.checkDocType();
         this.configDir = ConfigHelper.getConfigRootFile(var2);
      }
   }

   public final InputStream getDDStream() {
      return this.ddStreamCache.getInputStream();
   }

   private static void init() {
      if (edm == null) {
         edm = new EditableDescriptorManager();
      }
   }

   public static DescriptorParser getDescriptorParser(InputStream var0, DeploymentPlanBean var1, DescriptorSupport var2) throws IOException {
      return new DescriptorParser(var0, var1, var2);
   }

   public static DescriptorBean getWLSEditableDescriptorBean(Class var0) {
      return edm.createDescriptorRoot(var0, "UTF-8").getRootBean();
   }

   public static DeploymentPlanBean parseDeploymentPlan(InputStream var0) throws IOException {
      try {
         String[] var1 = new String[]{"http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/deployment-plan"};
         return (DeploymentPlanBean)edm.createDescriptor(new NamespaceURIMunger(var0, "http://xmlns.oracle.com/weblogic/deployment-plan", var1), false).getRootBean();
      } catch (XMLStreamException var2) {
         throw new IOException(var2.getMessage());
      }
   }

   public Document getDocument() throws IOException {
      if (this.doc != null) {
         return this.doc;
      } else {
         Object var1 = null;
         InputStream var2 = null;

         try {
            var2 = this.getDDStream();
            DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
            if (var3 != null) {
               InputSource var4 = new InputSource(var2);
               var3.setNamespaceAware(true);
               var3.setCoalescing(true);
               var3.setIgnoringElementContentWhitespace(true);
               var3.setValidating(false);
               DocumentBuilder var5 = var3.newDocumentBuilder();
               var5.setEntityResolver(new MyEntityResolver());
               this.doc = var5.parse(var4);
               this.setDocumentVersion(this.doc);
            }
         } catch (SAXException var17) {
            var1 = var17;
         } catch (ParserConfigurationException var18) {
            var1 = var18;
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var16) {
               }
            }

         }

         if (var1 != null) {
            if (ddebug) {
               ((Exception)var1).printStackTrace();
            }

            Throwable var20 = ManagementException.unWrapExceptions((Throwable)var1);
            IOException var21 = new IOException(var20.toString());
            var21.initCause(var20);
            throw var21;
         } else {
            return this.doc;
         }
      }
   }

   private void setDocumentVersion(Document var1) {
      DocumentType var2 = var1.getDoctype();
      if (var2 != null) {
         String var3 = var2.getPublicId();
         if (var3 != null) {
            int var4 = var3.lastIndexOf("//");

            int var5;
            for(var5 = var4; var3.charAt(var5) != ' ' && var5 != 0; --var5) {
            }

            if (var5 != 0) {
               this.version = var3.substring(var5 + 1, var4);
            }
         }
      } else {
         Element var6 = var1.getDocumentElement();
         this.version = var6.getAttribute("version");
      }

      if (ddebug) {
         Debug.say("doc version set to " + this.version + ". isSchema: " + this.hasSchema);
      }

   }

   public String getDocumentVersion() {
      return this.version;
   }

   public boolean isSchemaBased() {
      return this.hasSchema;
   }

   private void checkDocType() throws IOException {
      InputStream var1 = null;

      try {
         var1 = this.getDDStream();
         byte[] var2 = new byte[1024];
         var1.read(var2, 0, 1024);
         String var3 = new String(var2);
         if (var3.indexOf("DOCTYPE") != -1) {
            this.hasSchema = false;
         }
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var10) {
            }
         }

      }

   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.plan;
   }

   public File getConfigDir() {
      return this.configDir;
   }

   private static byte[] readStream(InputStream var0) throws IOException {
      if (var0 == null) {
         return null;
      } else {
         byte[] var3;
         try {
            ByteArrayOutputStream var1 = new ByteArrayOutputStream();
            StreamUtils.writeTo(var0, var1);
            byte[] var2 = var1.toByteArray();
            if (ddebug) {
               Debug.say(" +++ getBytes = " + new String(var2));
            }

            var3 = var2;
         } finally {
            try {
               if (var0 != null) {
                  var0.close();
               }
            } catch (IOException var10) {
            }

         }

         return var3;
      }
   }

   static {
      map.putAll(XMLConstants.locations);
      map.put("-//Sun Microsystems, Inc.//DTD Connector 1.0//EN", "/weblogic/connector/configuration/connector_1_0.dtd");
      map.put("-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.3//EN", "/weblogic/j2eeclient/application-client_1_3.dtd");
      map.put("-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.2//EN", "/weblogic/j2eeclient/application-client_1_2.dtd");
      map.put("-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN", "/weblogic/j2ee/dd/xml/application_1_2.dtd");
      map.put("-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN", "/weblogic/j2ee/dd/xml/application_1_3.dtd");
      map.put("-//Sun Microsystems, Inc.//DTD Web Application 1.2//EN", "/weblogic/servlet/internal/dd/web-jar.dtd");
      map.put("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", "/weblogic/servlet/internal/dd/web-jar.dtd");
      map.put("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", "/weblogic/servlet/internal/dd/web-jar-23.dtd");

      try {
         init();
      } catch (Exception var1) {
         if (ddebug) {
            var1.printStackTrace();
         }

         throw new RuntimeException(var1.toString(), var1);
      }
   }

   private static class ByteArrayCache {
      private final ByteBuffer byteBuffer;
      private final int size;
      private SoftReference cache;

      ByteArrayCache(byte[] var1) {
         this.size = var1.length;
         this.byteBuffer = ByteBuffer.allocateDirect(this.size);
         this.byteBuffer.put(var1);
         this.cache = new SoftReference(var1);
      }

      ByteArrayCache(InputStream var1) throws IOException {
         this(DescriptorParser.readStream(var1));
      }

      InputStream getInputStream() {
         return new UnsyncByteArrayInputStream(this.getBytes());
      }

      byte[] getBytes() {
         byte[] var1 = (byte[])((byte[])this.cache.get());
         if (var1 != null) {
            return var1;
         } else {
            var1 = new byte[this.size];
            this.byteBuffer.rewind();
            this.byteBuffer.get(var1, 0, this.size);
            this.cache = new SoftReference(var1);
            return var1;
         }
      }

      long length() {
         return (long)this.size;
      }
   }

   public class MyEntityResolver implements EntityResolver {
      public InputSource resolveEntity(String var1, String var2) {
         InputSource var3 = null;
         String var4 = (String)DescriptorParser.map.get(var1);
         if (DescriptorParser.ddebug) {
            Debug.say("local dtd for " + var1 + " is " + var4);
         }

         if (var4 != null) {
            InputStream var5 = this.getClass().getResourceAsStream(var4);
            if (var5 == null) {
               var4 = var4.substring(1);
               if (DescriptorParser.ddebug) {
                  Debug.say("now trying " + var4);
               }

               var5 = this.getClass().getResourceAsStream(var4);
            }

            if (var5 != null) {
               var3 = new InputSource(var5);
            }
         }

         return var3;
      }
   }
}
