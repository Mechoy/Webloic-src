package weblogic.wsee.tools.xcatalog;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.ContainerResolver;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.WebServiceException;
import org.apache.tools.ant.types.XMLCatalog;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.wsee.util.IOUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.dom.DOMParser;
import weblogic.xml.domimpl.Saver;
import weblogic.xml.domimpl.SaverOptions;

public class XCatalogUtil {
   private static final boolean verbose = Verbose.isVerbose(XCatalogUtil.class);
   private static final String FILE_SEPRATOR = "/";
   public static final int TYPE_RUNTIME_WAR = 0;
   public static final int TYPE_RUNTIME_NO_WAR = 1;
   public static final int TYPE_RUNTIME_UNKNOWN = 2;
   private static final String SYS_XCATALOG_FILE = "META-INF/jax-ws-catalog.xml";
   private static volatile Catalog SYS_XCATALOG;
   private static volatile Vector<URL> SYS_XURLS_Vector = new Vector();

   public static Document getDocument(InputSource var0) throws XCatalogException {
      Document var1 = null;

      try {
         if (var0.getSystemId() != null && !"".equals(var0.getSystemId())) {
            var1 = DOMParser.getDefaultDocumentImpl(var0.getSystemId());
         } else {
            var1 = DOMParser.getDefaultDocumentImpl(var0);
         }

         return var1;
      } catch (IOException var5) {
         String var3 = "";
         if (var0 != null) {
            var3 = var0.getSystemId();
         }

         StringBuffer var4 = new StringBuffer();
         var4.append("Failed to parse the URI ");
         var4.append("or refernce ID : [" + var3 + "]");
         var4.append(", please check if it is correctly defined.");
         if (verbose) {
            Verbose.say(var4.toString());
            var5.printStackTrace();
         }

         throw new XCatalogException(var4.toString(), var5);
      }
   }

   private static EntityResolver getEntityResolver(String var0) throws IOException {
      CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
      CatalogManager.getStaticManager().setUseStaticCatalog(false);
      CatalogResolver var1 = new CatalogResolver(true);

      try {
         ((CatalogResolver)var1).getCatalog().parseCatalog(var0);
         return var1;
      } catch (MalformedURLException var3) {
         throw new IOException(var3);
      }
   }

   public static LREntityResolver createEntityResolver(CatalogOptions var0) throws XCatalogException {
      LREntityResolver var1 = null;
      if (var0 == null) {
         return var1;
      } else {
         XMLCatalog var2 = var0.getXmlCatalog();
         File var3 = var0.getCatalog();
         String var4 = null;
         if (var3 != null) {
            var4 = var3.getPath();
         }

         try {
            XMLCatalog var5 = null;
            EntityResolver var6 = null;
            if (var4 != null && var4.length() > 0) {
               var6 = getEntityResolver(var4);
            }

            if (var2 != null) {
               var5 = var2;
            }

            var1 = new LREntityResolver(var5, var6);
            return var1;
         } catch (IOException var7) {
            throw new XCatalogException("bad catalog or entity definition:" + var7.toString());
         }
      }
   }

   public static String getBaseDir(String var0) {
      int var1 = var0.lastIndexOf("/");
      return var1 < 1 ? "." : var0.substring(0, var1);
   }

   public static String uniqueFile(String var0) {
      if (var0 == null) {
         return null;
      } else {
         try {
            if (var0.startsWith("file:")) {
               for(var0 = var0.substring("file:".length()); var0.startsWith("/") || var0.startsWith("\\"); var0 = var0.substring(1)) {
               }

               if (var0.indexOf(":") < 0) {
                  var0 = "/" + var0;
               }
            }

            File var1 = new File(var0);
            var0 = var1.getCanonicalPath();
            var0 = var0.replace("\\", "/");
            return var0;
         } catch (IOException var2) {
            return var0;
         }
      }
   }

   public static String getRelativeFile(File var0, File var1) throws IOException {
      if (var0 != null && var1 != null) {
         String var2 = var0.getName();
         String var3 = var0.getPath();
         var3 = uniqueFile(var3);
         if (var3.length() == var2.length()) {
            return var2;
         } else {
            var3 = var3.substring(0, var3.length() - var2.length());
            String var4 = var1.getCanonicalPath();
            var4 = uniqueFile(var4);
            if (var3.endsWith("/")) {
               var3 = var3.substring(0, var3.length() - 1);
            }

            if (var4.endsWith("/")) {
               var4 = var4.substring(0, var3.length() - 1);
            }

            if (verbose) {
               Verbose.say("fromFilePath=" + var3);
               Verbose.say("toDir=" + var4);
            }

            int var5 = var3.indexOf(var4);
            int var6;
            String var7;
            if (var5 >= 0) {
               var6 = var3.length() > var4.length() ? 1 : 0;
               var7 = var6 == 0 ? "" : "/";
               return var3.substring(var4.length() + var6) + var7 + var2;
            } else {
               var5 = var4.indexOf(var3);
               if (var5 != 0) {
                  return var3 + "/" + var2;
               } else {
                  var4 = var4.substring(var3.length());

                  for(var6 = 0; var4.indexOf("/") > 0; ++var6) {
                     var4 = var4.replace("/", "_+_");
                  }

                  var7 = "";

                  for(int var8 = 0; var8 < var6; ++var8) {
                     var7 = var7 + "../";
                  }

                  return var7 + var2;
               }
            }
         }
      } else {
         return null;
      }
   }

   public static EntityResolver createRuntimeCatalogResolver(int var0) {
      CatalogManager var1 = null;
      Catalog var2 = null;
      boolean var3 = true;
      if (!WSEECatalogResolver.nonWseeCatalogResolver) {
         try {
            var2 = WSEECatalogResolver.getCatalog();
            var3 = false;
         } catch (XCatalogException var16) {
            if (verbose) {
               Verbose.log("Goto traditional XML Catalogs processing due to:", var16);
            }
         }
      }

      if (var3) {
         var1 = new CatalogManager();
         var1.setIgnoreMissingProperties(true);
         var2 = var1.getCatalog();
      }

      ClassLoader var4 = Thread.currentThread().getContextClassLoader();
      Object var5 = null;

      try {
         if (var0 == 0 || var0 == 2) {
            if (var4 == null) {
               var5 = ClassLoader.getSystemResources("WEB-INF/jax-ws-catalog.xml");
            } else if (var0 == 2) {
               Container var6 = ContainerResolver.getInstance().getContainer();

               try {
                  Class var7 = Class.forName("javax.servlet.ServletContext");
                  Object var8 = var6 != null ? var6.getSPI(var7) : null;
                  if (var8 != null) {
                     Class var9 = Class.forName("weblogic.servlet.internal.WebAppServletContext");
                     if (var9.isAssignableFrom(var8.getClass())) {
                        Method var10 = var9.getMethod("getResources", String.class);
                        var5 = new IteratorEnumerator(Arrays.asList((URL[])((URL[])var10.invoke(var8, "/WEB-INF/jax-ws-catalog.xml"))).iterator());
                     }
                  }
               } catch (ClassNotFoundException var11) {
               } catch (NoSuchMethodException var12) {
               } catch (IllegalAccessException var13) {
               } catch (InvocationTargetException var14) {
               }

               if (var5 == null || !((Enumeration)var5).hasMoreElements()) {
                  var5 = getWebRunTimeCatalogs(var4);
               }
            } else {
               var5 = getWebRunTimeCatalogs(var4);
            }
         }

         Enumeration var18 = trimXURLs((Enumeration)var5);
         if (var0 == 1 || var0 == 2 && (var18 == null || !var18.hasMoreElements())) {
            if (var4 == null) {
               var18 = ClassLoader.getSystemResources("META-INF/jax-ws-catalog.xml");
            } else {
               var18 = var4.getResources("META-INF/jax-ws-catalog.xml");
            }
         }

         URL var17;
         for(var18 = trimXURLs(var18); var18 != null && var18.hasMoreElements(); var2.parseCatalog(var17)) {
            var17 = (URL)var18.nextElement();
            if (verbose) {
               Verbose.log((Object)("catalog file:" + var17.toExternalForm()));
            }
         }
      } catch (IOException var15) {
         throw new WebServiceException(var15);
      }

      return new WSEECatalogResolver(var2, SYS_XCATALOG);
   }

   private static Enumeration<URL> trimXURLs(Enumeration<URL> var0) {
      if (var0 != null && var0.hasMoreElements()) {
         Vector var1 = new Vector();

         while(var0.hasMoreElements()) {
            URL var2 = (URL)var0.nextElement();
            if (!SYS_XURLS_Vector.contains(var2)) {
               var1.add(var2);
            }
         }

         return var1.elements();
      } else {
         return var0;
      }
   }

   private static Enumeration<URL> getWebRunTimeCatalogs(ClassLoader var0) {
      Enumeration var1 = null;
      URL var2 = var0.getResource("/");
      if (var2 == null) {
         return null;
      } else {
         String var3 = var2.getPath();
         if (var3.indexOf(":") >= 0 && var3.startsWith("/")) {
            var3 = var3.substring(1);
         }

         File var4 = new File(var3, "../jax-ws-catalog.xml");
         if (var4.exists()) {
            try {
               var2 = var4.toURL();
            } catch (MalformedURLException var6) {
               return null;
            }

            Vector var5 = new Vector();
            var5.add(var2);
            var1 = var5.elements();
         }

         return var1;
      }
   }

   public static String doc2String(Document var0) {
      try {
         TransformerFactory var1 = TransformerFactory.newInstance();
         Transformer var2 = var1.newTransformer();
         DOMSource var3 = new DOMSource(var0);
         StringWriter var4 = new StringWriter();
         StreamResult var5 = new StreamResult(var4);
         var2.transform(var3, var5);
         return var4.toString();
      } catch (TransformerException var6) {
         return null;
      }
   }

   public static void writeDoc2File(Document var0, File var1, String var2) throws IOException {
      if (var1.getParentFile() != null) {
         var1.getParentFile().mkdirs();
      }

      OutputStream var3 = IOUtil.createEncodedFileOutputStream(var1, var2);
      SaverOptions var4 = SaverOptions.getDefaults();
      var4.setPrettyPrint(true);
      var4.setWriteXmlDeclaration(true);
      if (var2 != null) {
         var4.setEncoding(var2);
      }

      Saver.save(var3, var0, var4);
      var3.close();
   }

   public static URL toURL(String var0) {
      URL var1 = null;

      try {
         try {
            var1 = new URL(var0);
         } catch (Exception var3) {
         }

         if (var1 == null) {
            File var2 = new File(var0);
            var2 = var2.getCanonicalFile();
            var1 = var2.toURI().toURL();
         }
      } catch (Exception var4) {
         var1 = null;
      }

      return var1;
   }

   static {
      ClassLoader var0 = XCatalogUtil.class.getClassLoader();

      try {
         Enumeration var1;
         if (var0 == null) {
            var1 = ClassLoader.getSystemResources("META-INF/jax-ws-catalog.xml");
         } else {
            var1 = var0.getResources("META-INF/jax-ws-catalog.xml");
         }

         try {
            if (var1 != null) {
               while(var1.hasMoreElements()) {
                  SYS_XURLS_Vector.add(var1.nextElement());
               }

               SYS_XCATALOG = WSEECatalogResolver.getPureCatalog(SYS_XURLS_Vector.elements());
            }
         } catch (XCatalogException var3) {
            if (verbose) {
               Verbose.log("Failed to parse system catalog:META-INF/jax-ws-catalog.xml", var3);
            }
         }
      } catch (IOException var4) {
         if (verbose) {
            Verbose.log("Failed to parse system catalog:META-INF/jax-ws-catalog.xml", var4);
         }
      }

   }
}
