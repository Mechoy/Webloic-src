package weblogic.aspects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;
import weblogic.utils.Getopt2;
import weblogic.utils.classloaders.ClassPreProcessor;
import weblogic.utils.classloaders.ClasspathClassLoader;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class AspectClassPreProcessor implements ClassPreProcessor {
   public static final Logger logger = Logger.getLogger("weblogic.aspects");
   private Map classLoaderMap = new WeakHashMap();
   private boolean creating = false;
   private static AspectBundle NULL_BUNDLE = new AspectBundle();

   public void initialize(Hashtable var1) {
   }

   public byte[] preProcess(String var1, byte[] var2) {
      ClassLoader var3;
      for(var3 = Thread.currentThread().getContextClassLoader(); var3 != null && !(var3 instanceof GenericClassLoader); var3 = var3.getParent()) {
      }

      if (var3 instanceof GenericClassLoader) {
         GenericClassLoader var4 = (GenericClassLoader)var3;
         AspectBundle var5 = this.getAspectSystem(var4);
         if (var5 != null && (var5.aspectIncludePattern == null || var5.aspectIncludePattern.matcher(var1).find()) && (var5.aspectExcludePattern == null || !var5.aspectExcludePattern.matcher(var1).find())) {
            try {
               byte[] var6 = var5.aspectSystem.weaveClass(var1, var2);
               return var6 == null ? var2 : var6;
            } catch (IOException var7) {
               logger.log(Level.WARNING, "Could not weave class " + var1, var7);
            }
         }
      }

      return var2;
   }

   private synchronized AspectBundle getAspectSystem(GenericClassLoader var1) {
      if (this.creating) {
         return null;
      } else {
         AspectBundle var2 = (AspectBundle)this.classLoaderMap.get(var1);
         if (var2 == NULL_BUNDLE) {
            return null;
         } else {
            if (var2 == null) {
               this.creating = true;

               try {
                  var2 = createAspectSystem(var1);
                  if (var2 == null) {
                     this.classLoaderMap.put(var1, NULL_BUNDLE);
                     Object var3 = null;
                     return (AspectBundle)var3;
                  }

                  this.classLoaderMap.put(var1, var2);
               } finally {
                  this.creating = false;
               }
            }

            return var2;
         }
      }
   }

   private static AspectBundle createAspectSystem(GenericClassLoader var0) {
      try {
         String var1 = null;
         String var2 = null;
         String var3 = null;
         Properties var4 = new Properties();
         Object var5 = var0;
         InputStream var6 = getSourceAsStream("wlaspect.properties", var0);

         while(var6 != null) {
            Properties var7 = new Properties();
            var7.load(var6);
            var6.close();
            String var8 = (String)var7.remove("aspect.system");
            if (var8 != null && (var3 == null || var8.equals(var3)) && !"false".equals(var7.remove("aspect.enable"))) {
               var2 = addToRegex((String)var7.remove("aspect.exclude"), var2);
               var1 = addToRegex((String)var7.remove("aspect.include"), var1);
               var3 = var8;
               Iterator var9 = var7.keySet().iterator();

               while(var9.hasNext()) {
                  String var10 = (String)var9.next();
                  if (!var4.contains(var10)) {
                     var4.setProperty(var10, var7.getProperty(var10));
                  }
               }
            }

            var5 = ((ClassLoader)var5).getParent();
            if (var5 instanceof GenericClassLoader) {
               var6 = getSourceAsStream("wlaspect.properties", (GenericClassLoader)var5);
            } else if (var5 != null) {
               var6 = ((ClassLoader)var5).getResourceAsStream("wlaspect.properties");
            } else {
               var6 = null;
            }
         }

         if (var3 == null) {
            logger.info("Could not find aspect.system property");
            return null;
         }

         AspectBundle var15 = new AspectBundle();
         if (var1 != null) {
            var15.aspectIncludePattern = Pattern.compile(var1);
         }

         if (var2 != null) {
            var15.aspectExcludePattern = Pattern.compile(var2);
         }

         try {
            var15.aspectSystem = (AspectSystem)Class.forName(var3).newInstance();
            var15.aspectSystem.init(var0, var4);
            var0.addClassFinder(new AspectSystemClassFinder(var15.aspectSystem));
            return var15;
         } catch (InstantiationException var11) {
            logger.log(Level.WARNING, "Could not create AspectSystem: " + var3, var11);
         } catch (IllegalAccessException var12) {
            logger.log(Level.WARNING, "Could not access AspectSystem: " + var3, var12);
         } catch (ClassNotFoundException var13) {
            logger.log(Level.WARNING, "Could not find AspectSystem: " + var3, var13);
         }
      } catch (IOException var14) {
         logger.log(Level.WARNING, "Could not initialize aspect subsystem", var14);
      }

      return null;
   }

   private static String addToRegex(String var0, String var1) {
      if (var0 == null) {
         return var1;
      } else {
         String[] var2 = var0.split(",");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var1 == null) {
               var1 = var2[var3];
            } else {
               var1 = var1 + "|" + var2[var3];
            }
         }

         return var1;
      }
   }

   public static InputStream getSourceAsStream(String var0, GenericClassLoader var1) throws IOException {
      Source var2 = var1.getClassFinder().getSource(var0);
      if (var2 != null) {
         return var2.getInputStream();
      } else {
         ClassLoader var3 = var1.getParent();
         if (var3 instanceof GenericClassLoader) {
            GenericClassLoader var5 = (GenericClassLoader)var3;
            return getSourceAsStream(var0, var5);
         } else if (var3 != null) {
            URL var4 = var3.getResource(var0);
            return var4 == null ? null : var4.openStream();
         } else {
            return null;
         }
      }
   }

   public static void main(String[] var0) throws ZipException, IOException {
      Getopt2 var1 = new Getopt2();
      var1.addOption("src", "jar or classes directory file", "This is the jar file of classes to process");
      var1.addOption("outjar", "jar file", "This is the output of the jar needed to modify the src jar");
      var1.addOption("aspects", "jar file", "These are the aspects you want to apply");
      var1.grok(var0);
      if (!var1.hasOption("src") || !var1.hasOption("outjar") || !var1.hasOption("aspects")) {
         var1.usageAndExit("Apply");
      }

      String var2 = var1.getOption("src");
      String var3 = var1.getOption("outjar");
      String var4 = var1.getOption("aspects");
      ClasspathClassLoader var5 = new ClasspathClassLoader(var4, new ClassLoader() {
         public Class findClass(String var1) throws ClassNotFoundException {
            return super.findClass(var1);
         }
      });
      AspectBundle var6 = createAspectSystem(var5);
      ZipOutputStream var7 = new ZipOutputStream(new FileOutputStream(var3));
      VirtualJarFile var8 = VirtualJarFactory.createVirtualJar(new File(var2));
      Iterator var9 = var8.entries();

      while(true) {
         ZipEntry var10;
         String var11;
         String var12;
         do {
            do {
               do {
                  if (!var9.hasNext()) {
                     Map var19 = var6.aspectSystem.getAllSources();
                     Iterator var20 = var19.keySet().iterator();

                     while(var20.hasNext()) {
                        var11 = (String)var20.next();
                        byte[] var21 = (byte[])((byte[])var19.get(var11));
                        String var22 = var11.replace('.', '/') + ".class";
                        ZipEntry var23 = new ZipEntry(var22);
                        var7.putNextEntry(var23);
                        var7.write(var21);
                        var7.closeEntry();
                     }

                     var7.finish();
                     var7.close();
                     return;
                  }

                  var10 = (ZipEntry)var9.next();
                  var11 = var10.getName();
               } while(!var11.endsWith(".class"));

               var12 = var11.substring(0, var11.length() - 6);
               var12 = var12.replace('/', '.');
            } while(var6.aspectIncludePattern != null && !var6.aspectIncludePattern.matcher(var12).find());
         } while(var6.aspectExcludePattern != null && var6.aspectExcludePattern.matcher(var12).find());

         InputStream var13 = var8.getInputStream(var10);
         ByteArrayOutputStream var14 = new ByteArrayOutputStream();

         int var15;
         while((var15 = var13.read()) != -1) {
            var14.write(var15);
         }

         byte[] var16 = var14.toByteArray();
         byte[] var17 = var6.aspectSystem.weaveClass(var12, var16);
         if (var17 != null && var16 != var17) {
            ZipEntry var18 = new ZipEntry(var11);
            var7.putNextEntry(var18);
            var7.write(var17);
            var7.closeEntry();
         }
      }
   }

   private static class AspectBundle {
      AspectSystem aspectSystem;
      Pattern aspectIncludePattern;
      Pattern aspectExcludePattern;

      private AspectBundle() {
      }

      // $FF: synthetic method
      AspectBundle(Object var1) {
         this();
      }
   }
}
