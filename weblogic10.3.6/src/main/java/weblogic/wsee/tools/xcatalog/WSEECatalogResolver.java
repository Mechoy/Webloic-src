package weblogic.wsee.tools.xcatalog;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.wsee.util.Verbose;

public class WSEECatalogResolver implements EntityResolver {
   private static final boolean verbose = Verbose.isVerbose(WSEECatalogResolver.class);
   private static final String WSEE_CATALOG_RESOLVER = "wsee.catalog.resolver";
   public static final boolean nonWseeCatalogResolver = Boolean.getBoolean("wsee.catalog.resolver");
   private Catalog catalog;
   private Catalog sysCatalog;

   public WSEECatalogResolver() {
      this.catalog = null;
      this.sysCatalog = null;
      this.catalog = CatalogManager.getStaticManager().getCatalog();
   }

   public WSEECatalogResolver(Catalog var1) {
      this.catalog = null;
      this.sysCatalog = null;
      this.catalog = var1;
      this.validate(var1);
   }

   private void validate(Catalog var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("paramater catalog can not be null!");
      } else if (var1.getCatalogManager() == null) {
         throw new IllegalArgumentException("paramater catalog getCatalogManager() can not be null!");
      }
   }

   public WSEECatalogResolver(Catalog var1, Catalog var2) {
      this(var1);
      if (var2 != null) {
         this.validate(var2);
         this.sysCatalog = var2;
      }

   }

   public static Catalog getPureCatalog(Enumeration<URL> var0) throws XCatalogException {
      Catalog var1 = new Catalog();
      CatalogManager var2 = new CatalogManager();

      try {
         var2.setIgnoreMissingProperties(true);
         var1.setCatalogManager(var2);
         var1.setupReaders();
         if (var0 != null) {
            while(var0.hasMoreElements()) {
               var1.parseCatalog((URL)var0.nextElement());
            }
         }

         return var1;
      } catch (Exception var4) {
         throw new XCatalogException("failed to init catalog", var4);
      }
   }

   public static Catalog getCatalog() throws XCatalogException {
      Catalog var0 = new Catalog();
      CatalogManager var1 = new CatalogManager();

      try {
         var1.setIgnoreMissingProperties(true);
         var0.setCatalogManager(var1);
         var0.setupReaders();
         var0.loadSystemCatalogs();
         return var0;
      } catch (Exception var3) {
         throw new XCatalogException("failed to init catalog", var3);
      }
   }

   public String getResolvedEntity(String var1, String var2) {
      String var3 = null;
      var3 = this.getResolvedEntity(this.catalog, var1, var2);
      if (var3 == null && this.sysCatalog != null) {
         var3 = this.getResolvedEntity(this.sysCatalog, var1, var2);
      }

      return var3;
   }

   private String getResolvedEntity(Catalog var1, String var2, String var3) {
      String var4 = null;
      if (var1 == null) {
         if (verbose) {
            Verbose.log((Object)"Catalog resolution attempted with null catalog; ignored");
         }

         return null;
      } else {
         if (var3 != null) {
            try {
               var4 = var1.resolveSystem(var3);
            } catch (MalformedURLException var8) {
               if (verbose) {
                  Verbose.log((Object)("Malformed URL exception trying to resolve " + var2));
               }

               var4 = null;
            } catch (IOException var9) {
               if (verbose) {
                  Verbose.log((Object)("I/O exception trying to resolve " + var2));
               }

               var4 = null;
            }
         }

         if (var4 == null) {
            if (var2 != null) {
               try {
                  var4 = var1.resolvePublic(var2, var3);
               } catch (MalformedURLException var6) {
                  if (verbose) {
                     Verbose.log((Object)("Malformed URL exception trying to resolve" + var2));
                  }
               } catch (IOException var7) {
                  if (verbose) {
                     Verbose.log((Object)("I/O exception trying to resolve" + var2));
                  }
               }
            }

            if (var4 != null && verbose) {
               Verbose.say("Resolved public " + var2 + " mapping " + var4);
            }
         } else if (verbose) {
            Verbose.say("Resolved system " + var3 + " mapping " + var4);
         }

         return var4;
      }
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      String var3 = this.getResolvedEntity(var1, var2);
      if (var3 != null) {
         try {
            InputSource var4 = new InputSource(var3);
            var4.setPublicId(var1);
            URL var5 = new URL(var3);
            InputStream var6 = var5.openStream();
            var4.setByteStream(var6);
            return var4;
         } catch (Exception var7) {
            if (verbose) {
               Verbose.log((Object)("Failed to create InputSource: " + var3));
            }
         }
      }

      return null;
   }
}
