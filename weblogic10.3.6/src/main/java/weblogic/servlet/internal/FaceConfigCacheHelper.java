package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.DescriptorCache;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.collections.ArraySet;

public class FaceConfigCacheHelper {
   private static final String FACES_CACHE_DIR = ".faces_cache";

   public static Set parseFacesConfigs(Collection var0, String var1, String var2) {
      return parseFacesConfigs(var0, new File(var1), var2);
   }

   public static Set parseFacesConfigs(Collection var0, File var1, String var2) {
      if (var0 != null && var0.size() != 0) {
         HashSet var3 = new HashSet();
         long var4 = System.currentTimeMillis();
         File var6 = new File(var1, ".faces_cache");
         var6.mkdirs();
         Iterator var7 = var0.iterator();

         while(var7.hasNext()) {
            War.ResourceLocation var8 = (War.ResourceLocation)var7.next();
            FacesConfigsIOHelper var9 = new FacesConfigsIOHelper(var8);

            try {
               File var10 = new File(var6, var8.getURI().replace('\\', '/'));
               var3.addAll((Set)DescriptorCache.getInstance().parseXML(var10, var9));
               if (HTTPDebugLogger.isEnabled()) {
                  Iterator var11 = var3.iterator();

                  while(var11.hasNext()) {
                     HTTPDebugLogger.debug("[FaceConfigCacheHelper] Found managed bean class '" + var11.next() + "' in faces config at " + var8.getLocation());
                  }
               }
            } catch (XMLStreamException var12) {
               if (var2 != null) {
                  HTTPLogger.logFacesConfigParseException(var2, var8.getLocation(), var12);
               }
            } catch (IOException var13) {
               if (var2 != null) {
                  HTTPLogger.logFacesConfigParseException(var2, var8.getLocation(), var13);
               }
            }
         }

         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("[FaceConfigCacheHelper] parseFacesConfigs() took : " + (System.currentTimeMillis() - var4));
         }

         return var3;
      } else {
         return Collections.EMPTY_SET;
      }
   }

   private static class FacesConfigsIOHelper extends WebAppIOHelper {
      FacesConfigsIOHelper(War.ResourceLocation var1) {
         super(var1);
      }

      protected Object parseXMLInternal(XMLStreamReader var1) throws IOException, XMLStreamException {
         ArraySet var2 = new ArraySet();
         boolean var3 = false;
         boolean var4 = false;
         String var5 = null;
         String var6 = null;

         for(int var7 = var1.next(); var7 != 8; var7 = var1.next()) {
            switch (var7) {
               case 1:
                  if ("managed-bean".equals(var1.getLocalName())) {
                     var5 = null;
                     var6 = null;
                  } else if ("managed-bean-class".equals(var1.getLocalName())) {
                     var3 = true;
                  } else if ("managed-bean-scope".equals(var1.getLocalName())) {
                     var4 = true;
                  }
                  break;
               case 2:
                  if ("managed-bean".equals(var1.getLocalName())) {
                     if (var5 != null && var6 != null && !"none".equals(var6)) {
                        var2.add(var5);
                     }
                  } else if ("managed-bean-class".equals(var1.getLocalName())) {
                     var3 = false;
                  } else if ("managed-bean-scope".equals(var1.getLocalName())) {
                     var4 = false;
                  }
                  break;
               case 4:
               case 12:
                  String var8;
                  if (var4) {
                     var8 = var1.getText().trim();
                     if (var8.length() != 0) {
                        var6 = var8;
                     }
                  }

                  if (var3) {
                     var8 = var1.getText().trim();
                     if (var8.length() != 0) {
                        var5 = var8;
                     }
                  }
            }
         }

         return var2;
      }
   }
}
