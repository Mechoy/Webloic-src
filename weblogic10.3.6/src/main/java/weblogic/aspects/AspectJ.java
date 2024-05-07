package weblogic.aspects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.bcel.BcelObjectType;
import org.aspectj.weaver.bcel.BcelWeaver;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.bcel.LazyClassGen;
import org.aspectj.weaver.bcel.UnwovenClassFile;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.JarClassFinder;

public class AspectJ implements AspectSystem {
   private static final int BUFFER_SIZE = 8192;
   private BcelWorld world;
   private BcelWeaver weaver;
   private boolean aspects;
   private WeakReference gcl;
   private Properties properties;
   private String classPath;
   private static final Logger logger;
   private Map classBytes = new HashMap();

   public void init(GenericClassLoader var1, Properties var2) {
      logger.info("Initializing: " + var2);
      this.gcl = new WeakReference(var1);
      this.classPath = var1.getClassPath();
      this.properties = var2;
      String[] var3 = var1.getClassPath().split(File.pathSeparator);
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4.add(var3[var5]);
      }

      MessageHandler var9 = new MessageHandler();
      var9.ignore(IMessage.INFO);
      this.world = new BcelWorld(var4, var9);
      this.weaver = new BcelWeaver(this.world);
      Iterator var6 = var2.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         String var8 = var2.getProperty(var7);
         this.addAspectJar(var1, var7, var8);
      }

   }

   private void addAspectJar(GenericClassLoader var1, String var2, String var3) {
      try {
         InputStream var4 = AspectClassPreProcessor.getSourceAsStream(var3, var1);
         if (var4 == null) {
            logger.info("Configured aspects " + var2 + " not found as " + var3);
            return;
         }

         File var5 = File.createTempFile("aspects", ".jar");
         streamToFile(var4, var5);
         this.weaver.addLibraryJarFile(var5);
         var1.addClassFinderFirst(new JarClassFinder(var5));
         logger.info("Aspects configured from " + var3);
         this.aspects = true;
         this.weaver.prepareForWeave();
      } catch (IOException var6) {
         logger.log(Level.WARNING, "Failed to copy aspect jar", var6);
      }

   }

   private static void streamToFile(InputStream var0, File var1) throws IOException {
      byte[] var3 = new byte[8192];
      FileOutputStream var4 = new FileOutputStream(var1);

      int var2;
      while((var2 = var0.read(var3)) != -1) {
         var4.write(var3, 0, var2);
      }

      var4.close();
      var0.close();
   }

   public byte[] weaveClass(String var1, byte[] var2) throws IOException {
      byte[] var3 = var2;
      LazyClassGen var4 = null;
      if (this.aspects) {
         String var7;
         try {
            try {
               UnwovenClassFile var5 = new UnwovenClassFile(var1, var2);
               this.weaver.addClassFile(var5);
               String var12 = var5.getClassName();
               ResolvedTypeX var14 = this.world.resolve(var12);
               BcelObjectType var8 = BcelWorld.getBcelObjectType(var14);
               var8.resetState();
               this.weaver.weave(var14);
               var4 = this.weaver.weaveWithoutDump(var5, var8);
            } catch (ClassCastException var9) {
               GenericClassLoader var6 = (GenericClassLoader)this.gcl.get();
               if (var6 != null) {
                  var7 = var6.getClassPath();
                  if (!var7.equals(this.classPath)) {
                     this.init(var6, this.properties);
                     return this.weaveClass(var1, var2);
                  }
               }

               throw var9;
            }
         } catch (RuntimeException var10) {
            logger.log(Level.WARNING, "Failed to weave: " + var1, var10);
         }

         if (var4 == null) {
            var3 = var2;
         } else {
            logger.info("Weaved: " + var1);
            var3 = var4.getJavaClass().getBytes();
            Iterator var11 = var4.getChildClasses().iterator();

            while(var11.hasNext()) {
               UnwovenClassFile.ChildClass var13 = (UnwovenClassFile.ChildClass)var11.next();
               var7 = var1 + "$" + var13.name;
               this.classBytes.put(var7, var13.bytes);
               logger.info("Added: " + var7);
            }
         }
      }

      return var3;
   }

   public Map getAllSources() {
      return this.classBytes;
   }

   static {
      logger = AspectClassPreProcessor.logger;
   }
}
