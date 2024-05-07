package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.DescriptorCache;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.collections.ArraySet;

public class TldCacheHelper {
   private static final String IMPLICIT_TLD = "implicit.tld";
   private static final String TLD_CACHE_DIR = ".tld_cache";
   public static final String LISTENER_CLASS = "listener-class";
   public static final String TAG_CLASS = "tag-class";

   public static Map parseTagLibraries(Collection var0, String var1, String var2) {
      return parseTagLibraries(var0, new File(var1), var2);
   }

   public static Map parseTagLibraries(Collection var0, File var1, String var2) {
      if (var0 != null && var0.size() != 0) {
         Object var3 = null;
         Object var4 = null;
         long var5 = System.currentTimeMillis();
         File var7 = new File(var1, ".tld_cache");
         var7.mkdirs();
         Iterator var8 = var0.iterator();

         while(true) {
            War.ResourceLocation var9;
            do {
               if (!var8.hasNext()) {
                  HashMap var18 = new HashMap(2);
                  if (var3 == null) {
                     var3 = Collections.EMPTY_SET;
                  }

                  if (var4 == null) {
                     var4 = Collections.EMPTY_SET;
                  }

                  var18.put("listener-class", var3);
                  var18.put("tag-class", var4);
                  if (HTTPDebugLogger.isEnabled()) {
                     HTTPDebugLogger.debug("[TldCacheHelper] parseTagLibraries() took : " + (System.currentTimeMillis() - var5));
                  }

                  return var18;
               }

               var9 = (War.ResourceLocation)var8.next();
            } while(var9.getURI().endsWith("implicit.tld"));

            TldIOHelper var10 = new TldIOHelper(var9);

            try {
               File var11 = new File(var7, var9.getURI().replace('\\', '/'));
               Map var12 = (Map)DescriptorCache.getInstance().parseXML(var11, var10);
               Collection var13 = (Collection)var12.get("listener-class");
               if (var13 != null && !var13.isEmpty()) {
                  if (var3 == null) {
                     var3 = new ArraySet();
                  }

                  ((Set)var3).addAll(var13);
                  if (HTTPDebugLogger.isEnabled()) {
                     Iterator var14 = var13.iterator();

                     while(var14.hasNext()) {
                        HTTPDebugLogger.debug("[TldCacheHelper] Found listener '" + var14.next() + "' in tld at " + var9.getLocation());
                     }
                  }
               }

               Collection var19 = (Collection)var12.get("tag-class");
               if (var19 != null && !var19.isEmpty()) {
                  if (var4 == null) {
                     var4 = new ArraySet();
                  }

                  ((Set)var4).addAll(var19);
                  if (HTTPDebugLogger.isEnabled()) {
                     Iterator var15 = var19.iterator();

                     while(var15.hasNext()) {
                        HTTPDebugLogger.debug("[TldCacheHelper] Found tag handler '" + var15.next() + "' in tld at " + var9.getLocation());
                     }
                  }
               }
            } catch (XMLStreamException var16) {
               if (var2 != null) {
                  HTTPLogger.logListenerParseException(var2, var9.getLocation(), var16);
               }
            } catch (IOException var17) {
               if (var2 != null) {
                  HTTPLogger.logListenerParseException(var2, var9.getLocation(), var17);
               }
            }
         }
      } else {
         return Collections.EMPTY_MAP;
      }
   }

   private static class TldIOHelper extends WebAppIOHelper {
      TldIOHelper(War.ResourceLocation var1) {
         super(var1);
      }

      protected Object parseXMLInternal(XMLStreamReader var1) throws IOException, XMLStreamException {
         ArraySet var2 = new ArraySet();
         ArraySet var3 = new ArraySet();
         String var4 = null;
         boolean var5 = false;
         boolean var6 = false;

         for(int var7 = var1.next(); var7 != 8; var7 = var1.next()) {
            switch (var7) {
               case 1:
                  if ("listener-class".equals(var1.getLocalName())) {
                     var5 = true;
                  } else if ("tag-class".equals(var1.getLocalName())) {
                     var6 = true;
                  }
                  break;
               case 2:
                  if ("listener-class".equals(var1.getLocalName())) {
                     var5 = false;
                  } else if ("tag-class".equals(var1.getLocalName())) {
                     var6 = false;
                  }
                  break;
               case 4:
               case 12:
                  var4 = var1.getText().trim();
                  if (var4.length() != 0) {
                     if (var5) {
                        var2.add(var4);
                     }

                     if (var6) {
                        var3.add(var4);
                     }
                  }
            }
         }

         HashMap var8 = new HashMap(2);
         var8.put("listener-class", var2);
         var8.put("tag-class", var3);
         return var8;
      }
   }
}
