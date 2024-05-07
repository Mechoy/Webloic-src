package weblogic.ejb.container.deployer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.Map;
import weblogic.utils.classloaders.GenericClassLoader;

/** @deprecated */
@Deprecated
public final class RemoteBizIntfClassLoader extends GenericClassLoader {
   private GenericClassLoader ctxCl;
   private RemoteBizIntfClassPreProcessor rbzcpp = null;
   private String remoteBiName = null;
   private String biName = null;
   private Class<?> oldBiClass = null;
   private boolean isStubLoaded = false;
   private Map<String, Class> enhancedClasses = new HashMap();
   private static boolean debug = Boolean.getBoolean("weblogic.ejb.enhancement.debug");

   public RemoteBizIntfClassLoader(String var1, String var2, GenericClassLoader var3) {
      super(var3);
      this.ctxCl = var3;
      this.remoteBiName = var2;
      this.biName = var1;
      this.rbzcpp = new RemoteBizIntfClassPreProcessor(var2, var1);
      this.addInstanceClassPreProcessor(this.rbzcpp);
   }

   public Class<?> loadClass(String var1) throws ClassNotFoundException {
      Class var2;
      Class var3;
      if (var1.indexOf("_WLStub") > 0) {
         var2 = (Class)this.enhancedClasses.get(var1);
         if (var2 != null) {
            return var2;
         } else if (!this.isStubLoaded) {
            this.isStubLoaded = true;
            return this.ctxCl.loadClass(var1);
         } else {
            var3 = this.findClass(var1);
            this.enhancedClasses.put(var1, var3);
            this.isStubLoaded = false;
            return var3;
         }
      } else {
         var2 = this.ctxCl.loadClass(var1);
         if (var2.getName().equals(this.remoteBiName)) {
            this.debug(var2 + "is " + this.remoteBiName + ", return it");
            return var2;
         } else if (!Remote.class.isAssignableFrom(var2) && !Object.class.equals(var2)) {
            if (var1 != null && var1.startsWith("java.")) {
               return var2;
            } else if (!var2.isInterface()) {
               return var2;
            } else {
               if (var1.equals(this.biName)) {
                  this.oldBiClass = var2;
                  this.enhancedClasses.put(this.biName, this.oldBiClass);
               }

               if (this.oldBiClass != null && var2.isAssignableFrom(this.oldBiClass)) {
                  var3 = (Class)this.enhancedClasses.get(this.remoteBiName);
                  if (var3 != null) {
                     this.debug("got " + var3 + " from " + this.enhancedClasses);
                     return var3;
                  } else {
                     InputStream var5 = null;

                     byte[] var4;
                     try {
                        var5 = this.ctxCl.getResourceAsStream(var1.replace('.', '/') + ".class");
                        var4 = this.getClassBytes(var5, var1);
                     } finally {
                        try {
                           if (var5 != null) {
                              var5.close();
                           }
                        } catch (IOException var13) {
                        }

                     }

                     Class var6;
                     if (var1.equals(this.biName)) {
                        try {
                           var6 = this.ctxCl.loadClass(this.remoteBiName);
                           this.debug("loaded " + this.remoteBiName + " on " + this.ctxCl);
                           return var6;
                        } catch (ClassNotFoundException var15) {
                           var6 = this.enhanceClass(this.biName, this.remoteBiName, var4);
                        }
                     } else {
                        var6 = this.enhanceClass(var1, var1, var4);
                     }

                     return var6;
                  }
               } else {
                  this.debug(var2 + "is not super class of " + this.oldBiClass + ", return it");
                  return var2;
               }
            }
         } else {
            return var2;
         }
      }
   }

   private synchronized Class enhanceClass(String var1, String var2, byte[] var3) throws ClassNotFoundException {
      Class var4;
      byte[] var5;
      try {
         var5 = this.doPreProcess(var3, var1);
         if (!var1.equals(var2)) {
            String var6 = this.rbzcpp.getPseudoname(var2);
            var4 = (Class)this.enhancedClasses.get(var6);
            if (var4 == null) {
               var4 = this.defineClass(var6, var5, 0, var5.length);
               this.enhancedClasses.put(var6, var4);
            }
         } else {
            var4 = (Class)this.enhancedClasses.get(var2);
            if (var4 == null) {
               var4 = this.defineClass(var2, var5, 0, var5.length);
               this.enhancedClasses.put(var2, var4);
            }
         }

         var5 = this.rbzcpp.postProcess();
      } catch (Throwable var9) {
         if (debug) {
            var9.printStackTrace();
         }

         throw new ClassNotFoundException(var2);
      }

      try {
         if (!var1.equals(var2)) {
            synchronized(this.ctxCl) {
               Class var10000;
               try {
                  Class var7 = this.ctxCl.loadClass(var2);
                  this.debug("loaded " + var2 + " on " + this.ctxCl);
                  var10000 = var7;
               } catch (ClassNotFoundException var10) {
                  var4 = this.ctxCl.defineCodeGenClass(var2, var5, (URL)null);
                  this.debug("defined " + var2 + " on " + this.ctxCl);
                  this.enhancedClasses.put(var2, var4);
                  return var4;
               }

               return var10000;
            }
         } else {
            return var4;
         }
      } catch (Throwable var12) {
         if (debug) {
            var12.printStackTrace();
         }

         throw new ClassNotFoundException(var2);
      }
   }

   private byte[] getClassBytes(InputStream var1, String var2) throws ClassNotFoundException {
      try {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         byte[] var4 = new byte[4096];

         int var5;
         while((var5 = var1.read(var4)) != -1) {
            var3.write(var4, 0, var5);
         }

         var3.close();
         byte[] var7 = var3.toByteArray();
         return var7;
      } catch (Throwable var6) {
         if (debug) {
            var6.printStackTrace();
         }

         throw new ClassNotFoundException(var2);
      }
   }

   private void debug(String var1) {
      if (debug) {
         System.out.println("[" + this.getClass().getSimpleName() + "]" + "---" + Thread.currentThread() + "---" + "\n\t" + var1);
      }

   }
}
