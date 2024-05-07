package weblogic.servlet.internal.dd.glassfish;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.utils.jars.VirtualJarFile;

public class GlassFishWebAppParser {
   private static final String GLASSFISH_DD = "WEB-INF/glassfish-web.xml";
   private static final String SUNWEB_DD = "WEB-INF/sun-web.xml";
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("GlassFishWebAppParser");
   private InputStream descriptorInputStream;
   private Map<String, BaseGlassfishTagParser> parserMap = new HashMap();

   private GlassFishWebAppParser(InputStream var1) {
      this.descriptorInputStream = var1;
      this.parserMap.put("context-root", new ContextRootTagParser());
      this.parserMap.put("security-role-mapping", new SecurityRoleMappingTagParser());
      this.parserMap.put("session-config", new SessionConfigTagParser());
      this.parserMap.put("ejb-ref", new EjbRefTagParser());
      this.parserMap.put("resource-ref", new ResourceRefTagParser());
      this.parserMap.put("resource-env-ref", new ResourceEnvRefTagParser());
      this.parserMap.put("class-loader", new ClassLoaderTagParser());
      this.parserMap.put("jsp-config", new JspConfigTagParser());
      this.parserMap.put("default-tag-parser", new DefaultTagParser());
   }

   public static GlassFishWebAppParser getParser(VirtualJarFile var0) {
      InputStream var1 = null;
      if (var0 != null) {
         ZipEntry var2 = var0.getEntry("WEB-INF/glassfish-web.xml");
         if (var2 == null) {
            var2 = var0.getEntry("WEB-INF/sun-web.xml");
         }

         if (var2 != null) {
            try {
               var1 = var0.getInputStream(var2);
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }
      }

      return new GlassFishWebAppParser(var1);
   }

   public static GlassFishWebAppParser getParser(String var0, boolean var1) throws FileNotFoundException {
      FileInputStream var2 = new FileInputStream(new File(var0));
      return new GlassFishWebAppParser(var2);
   }

   public WeblogicWebAppBean getWeblogicWebAppBean() throws IOException, XMLStreamException {
      if (this.descriptorInputStream == null) {
         return null;
      } else {
         XMLInputFactory var1 = XMLInputFactory.newInstance();
         XMLStreamReader var2 = var1.createXMLStreamReader(this.descriptorInputStream);

         WeblogicWebAppBean var3;
         try {
            var3 = this.parseDescriptor(var2);
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }

         return var3;
      }
   }

   private WeblogicWebAppBean parseDescriptor(XMLStreamReader var1) throws IOException, XMLStreamException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Start Parse Glassfish Descriptor...");
      }

      WeblogicWebAppBean var2 = this.createWeblogicWebAppBean();

      do {
         int var3 = var1.next();
         if (var3 == 1) {
            this.getTagParser(var1.getLocalName()).parse(var1, var2);
         }
      } while(var1.hasNext());

      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Parse Glassfish Descriptor completed!");
      }

      return var2;
   }

   private BaseGlassfishTagParser getTagParser(String var1) {
      BaseGlassfishTagParser var2 = (BaseGlassfishTagParser)this.parserMap.get(var1);
      if (var2 == null) {
         var2 = (BaseGlassfishTagParser)this.parserMap.get("default-tag-parser");
      }

      return var2;
   }

   private WeblogicWebAppBean createWeblogicWebAppBean() {
      WeblogicWebAppBean var1 = (WeblogicWebAppBean)(new DescriptorManager()).createDescriptorRoot(WeblogicWebAppBean.class).getRootBean();
      return var1;
   }
}
