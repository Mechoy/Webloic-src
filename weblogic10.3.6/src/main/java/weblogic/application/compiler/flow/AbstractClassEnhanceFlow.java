package weblogic.application.compiler.flow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.enhance.PCEnhancer;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.meta.ClassAnnotationMetaDataFilter;
import org.apache.openjpa.lib.meta.XMLMetaDataParser;
import org.apache.openjpa.lib.util.Options;
import org.apache.openjpa.persistence.PersistenceUnitInfoImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import serp.bytecode.Project;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.utils.VirtualJarFileMetaDataIterator;
import weblogic.utils.FileUtils;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public abstract class AbstractClassEnhanceFlow extends CompilerFlow {
   protected static final String JPA_CONFIGURATION_NAME = "META-INF/persistence.xml";
   protected static final String OPJPA_PROVIDER = "org.apache.openjpa.persistence.PersistenceProviderImpl";
   protected static final String KODO_PROVIDER = "kodo.persistence.PersistenceProviderImpl";
   public static final String WEBINF_CLASSES;
   public static final String WEBINF_LIB;
   public static final String APP_CLASSES;
   public static final String PERSISTENCE_XML = "<?xml version=\"1.0\"?> <persistence xmlns=\"http://java.sun.com/xml/ns/persistence\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\" version=\"1.0\"> <persistence-unit name=\"default\"> </persistence-unit> </persistence> ";
   protected Collection jarFiles = new HashSet();
   private Class[] enhanceClasses = new Class[]{Entity.class, Embeddable.class, MappedSuperclass.class};

   public AbstractClassEnhanceFlow(CompilerCtx var1) {
      super(var1);
   }

   public void cleanup() throws ToolFailureException {
   }

   protected File createConfigFile() throws IOException {
      File var1 = new File(System.getProperty("java.io.tmpdir"), "persistence.xml");
      FileWriter var2 = new FileWriter(var1);
      var2.write("<?xml version=\"1.0\"?> <persistence xmlns=\"http://java.sun.com/xml/ns/persistence\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\" version=\"1.0\"> <persistence-unit name=\"default\"> </persistence-unit> </persistence> ");
      var2.close();
      return var1;
   }

   protected void processJars(Collection var1, File var2, Collection var3, ClassLoader var4, boolean var5) throws ToolFailureException {
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         this.processJar(new File(var7), var2, var3, var4, var5);
      }

   }

   protected void processJar(File var1, File var2, Collection var3, ClassLoader var4, boolean var5) throws ToolFailureException {
      String var6 = this.ctx.getOutputDir().getPath() + File.separatorChar + "tempjar" + System.currentTimeMillis();

      try {
         JarFileUtils.extract(var1, new File(var6));
         this.processDir(var6, var2, var3, var4, var5);
         JarFileUtils.createJarFileFromDirectory(var1, new File(var6));
      } catch (IOException var12) {
         throw new ToolFailureException(var12.getMessage(), var12);
      } finally {
         FileUtils.remove(new File(var6));
      }

   }

   protected void processDir(String var1, File var2, Collection var3, ClassLoader var4, boolean var5) throws ToolFailureException {
      VirtualJarFile var6 = null;

      try {
         File var7 = new File(var1);
         if (var7.exists()) {
            var6 = VirtualJarFactory.createVirtualJar(var7);
            OpenJPAConfigurationImpl var8 = new OpenJPAConfigurationImpl();
            Options var9 = new Options();
            var9.put("p", var2.getCanonicalPath());
            Configurations.populateConfiguration(var8, var9);
            VirtualJarFileMetaDataIterator var10 = new VirtualJarFileMetaDataIterator(var6, new ClassAnnotationMetaDataFilter(this.enhanceClasses));
            Project var11 = new Project();

            while(var10.hasNext()) {
               String var13 = (String)var10.next();
               String var14 = var13.replace('/', '.');
               var14 = var14.substring(0, var14.length() - 6);
               if (var3.contains(var14) || !var5) {
                  Class var15 = Class.forName(var14, false, var4);
                  PCEnhancer var12 = new PCEnhancer(var8, var15);
                  var12.setDirectory(var7);
                  var12.run();
                  var12.record();
                  var11.clear();
               }
            }

            return;
         }
      } catch (IOException var26) {
         throw new ToolFailureException(var26.getMessage(), var26);
      } catch (ClassNotFoundException var27) {
         throw new ToolFailureException(var27.getMessage(), var27);
      } finally {
         if (var6 != null) {
            try {
               var6.close();
            } catch (IOException var25) {
            }
         }

      }

   }

   protected Collection getEntityClasses(ClassLoader var1) throws ToolFailureException {
      HashSet var2 = new HashSet();
      Thread var3 = Thread.currentThread();
      ClassLoader var4 = var3.getContextClassLoader();
      var3.setContextClassLoader(var1);

      try {
         HashSet var5 = new HashSet(Collections.list(var1.getResources("META-INF/persistence.xml")));
         ConfigurationParser var6 = new ConfigurationParser((Map)null);
         Iterator var7 = var5.iterator();

         label84:
         while(var7.hasNext()) {
            URL var8 = (URL)var7.next();
            var6.parse(var8);
            Iterator var9 = var6.getResults().iterator();

            while(true) {
               PersistenceUnitInfoImpl var10;
               String var11;
               do {
                  if (!var9.hasNext()) {
                     var6.clear();
                     continue label84;
                  }

                  var10 = (PersistenceUnitInfoImpl)var9.next();
                  this.jarFiles.addAll(var10.getJarFileUrls());
                  var11 = var10.getPersistenceProviderClassName();
               } while(var11 != null && !var11.equals("org.apache.openjpa.persistence.PersistenceProviderImpl") && !var11.equals("kodo.persistence.PersistenceProviderImpl"));

               var2.addAll(var10.getManagedClassNames());
            }
         }
      } catch (IOException var16) {
         throw new ToolFailureException(var16.getMessage(), var16);
      } finally {
         var3.setContextClassLoader(var4);
      }

      return var2;
   }

   static {
      WEBINF_CLASSES = File.separatorChar + "WEB-INF" + File.separatorChar + "classes";
      WEBINF_LIB = File.separatorChar + "WEB-INF" + File.separatorChar + "lib" + File.separatorChar;
      APP_CLASSES = File.separatorChar + "APP-INF" + File.separatorChar + "classes";
   }

   class ConfigurationParser extends XMLMetaDataParser {
      private final Map _map;
      private PersistenceUnitInfoImpl _info = null;
      private URL _source = null;

      public ConfigurationParser(Map var2) {
         this._map = var2;
         this.setCaching(false);
         this.setValidating(true);
         this.setParseText(true);
      }

      public void parse(URL var1) throws IOException {
         this._source = var1;
         super.parse(var1);
      }

      public void parse(File var1) throws IOException {
         this._source = var1.toURL();
         super.parse(var1);
      }

      protected Object getSchemaSource() {
         return this.getClass().getResourceAsStream("persistence-xsd.rsrc");
      }

      protected void reset() {
         super.reset();
         this._info = null;
         this._source = null;
      }

      protected boolean startElement(String var1, Attributes var2) throws SAXException {
         if (this.currentDepth() == 1) {
            this.startPersistenceUnit(var2);
         } else if (this.currentDepth() == 3 && "property".equals(var1)) {
            this._info.setProperty(var2.getValue("name"), var2.getValue("value"));
         }

         return true;
      }

      protected void endElement(String var1) throws SAXException {
         if (this.currentDepth() == 1) {
            this._info.fromUserProperties(this._map);
            this.addResult(this._info);
         }

         if (this.currentDepth() == 2) {
            switch (var1.charAt(0)) {
               case 'c':
                  this._info.addManagedClassName(this.currentText());
               case 'e':
                  this._info.setExcludeUnlistedClasses("true".equalsIgnoreCase(this.currentText()));
               case 'd':
               case 'f':
               case 'g':
               case 'h':
               case 'i':
               case 'k':
               case 'l':
               case 'o':
               default:
                  break;
               case 'j':
                  if ("jta-data-source".equals(var1)) {
                     this._info.setJtaDataSourceName(this.currentText());
                  } else {
                     try {
                        this._info.addJarFileName(AbstractClassEnhanceFlow.this.ctx.getEar().getURI() + "#" + this.currentText());
                     } catch (IllegalArgumentException var3) {
                        throw this.getException(var3.getMessage());
                     }
                  }
                  break;
               case 'm':
                  this._info.addMappingFileName(this.currentText());
                  break;
               case 'n':
                  this._info.setNonJtaDataSourceName(this.currentText());
                  break;
               case 'p':
                  if ("provider".equals(var1)) {
                     this._info.setPersistenceProviderClassName(this.currentText());
                  }
            }

         }
      }

      private void startPersistenceUnit(Attributes var1) throws SAXException {
         this._info = new PersistenceUnitInfoImpl();
         this._info.setPersistenceUnitName(var1.getValue("name"));
         String var2 = var1.getValue("transaction-type");
         if (this._source != null) {
            this._info.setPersistenceXmlFileUrl(this._source);
         }

      }
   }
}
