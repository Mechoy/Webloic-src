package weblogic.ejb.container.deployer;

import com.bea.objectweb.asm.ClassAdapter;
import com.bea.objectweb.asm.ClassReader;
import com.bea.objectweb.asm.ClassVisitor;
import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.TreeSet;

/** @deprecated */
@Deprecated
public final class DownloadRemoteBizIntfClassLoader extends ClassLoader {
   private String newName;
   private boolean IsBizIntf = false;

   public DownloadRemoteBizIntfClassLoader(String var1, ClassLoader var2) {
      super(var2);
      this.newName = var1;
   }

   public Class<?> loadClass(String var1) throws ClassNotFoundException {
      Class var2 = super.loadClass(var1);
      if (!Remote.class.isAssignableFrom(var2) && !Object.class.equals(var2)) {
         if (var1 != null && var1.startsWith("java.")) {
            return var2;
         } else {
            Object var3 = null;
            InputStream var4 = null;

            byte[] var13;
            try {
               var4 = this.getResourceAsStream(var1.replace('.', '/') + ".class");
               var13 = this.getClassBytes(var4, var1);
            } finally {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (IOException var11) {
                  }
               }

            }

            byte[] var5 = this.preProcess(var1, var13);
            if (!this.IsBizIntf) {
               this.IsBizIntf = true;
               return this.defineClass(this.newName, var5, 0, var5.length);
            } else {
               return this.defineClass(var1, var5, 0, var5.length);
            }
         }
      } else {
         return var2;
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
         throw new ClassNotFoundException(var2);
      }
   }

   private byte[] preProcess(String var1, byte[] var2) {
      ClassReader var3 = new ClassReader(var2);
      ClassWriter var4 = new ClassWriter(var3, 0);
      Local2RemoteTransformer var5;
      if (!this.IsBizIntf) {
         var5 = new Local2RemoteTransformer(var4, false, this.newName);
      } else {
         var5 = new Local2RemoteTransformer(var4, true, this.newName);
      }

      var3.accept(var5, 0);
      byte[] var6 = var4.toByteArray();
      if (!this.IsBizIntf) {
         this.writeEnhancedClassBack(this.newName, var6);
      } else {
         this.writeEnhancedClassBack(var1, var6);
      }

      return var6;
   }

   private void writeEnhancedClassBack(String var1, byte[] var2) {
      if (var2 != null) {
         try {
            if (System.getProperty("weblogic.ejb.enhancement.debug") == null) {
               return;
            }
         } catch (Exception var21) {
            return;
         }

         File var3 = new File(System.getProperty("user.home") + File.separatorChar + "enhanced_classes", var1.replace('.', File.separatorChar) + ".class");
         var3.getParentFile().mkdirs();
         FileOutputStream var4 = null;

         try {
            if (!var3.exists()) {
               var3.createNewFile();
            }

            var4 = new FileOutputStream(var3);
            var4.write(var2);
         } catch (FileNotFoundException var18) {
            var18.printStackTrace();
         } catch (IOException var19) {
            var19.printStackTrace();
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var17) {
                  var17.printStackTrace();
               }
            }

         }

      }
   }

   public class Local2RemoteTransformer extends ClassAdapter {
      private static final String REMOTE = "java/rmi/Remote";
      private static final String REMOTE_EXCEPTION = "java/rmi/RemoteException";
      private final String[] EXCEPTIONS = new String[]{"java/rmi/RemoteException"};
      private final ClassVisitor classvisitor;
      private final boolean onlyTransferMethod;
      private final String generatedIntfName;

      public Local2RemoteTransformer(ClassVisitor var2, boolean var3, String var4) {
         super(var2);
         this.classvisitor = var2;
         this.onlyTransferMethod = var3;
         this.generatedIntfName = var4;
      }

      public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
         if (!this.onlyTransferMethod) {
            ArrayList var7 = new ArrayList(var6.length + 1);
            String[] var8 = var6;
            int var9 = var6.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String var11 = var8[var10];
               var7.add(var11);
            }

            if (!var7.contains("java/rmi/Remote")) {
               var7.add("java/rmi/Remote");
            }

            var8 = new String[var7.size()];
            var8 = (String[])var7.toArray(var8);
            this.classvisitor.visit(var1, var2, this.generatedIntfName.replace('.', '/'), var4, var5, var8);
         } else {
            this.classvisitor.visit(var1, var2, var3, var4, var5, var6);
         }

      }

      public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
         return this.classvisitor.visitMethod(var1, var2, var3, var4, this.getExceptions(var5));
      }

      private final String[] getExceptions(String[] var1) {
         if (var1 != null && var1.length > 0) {
            TreeSet var2 = new TreeSet();
            String[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               var2.add(var6);
            }

            var2.add("java/rmi/RemoteException");
            var3 = new String[var2.size()];
            var2.toArray(var3);
            return var3;
         } else {
            return this.EXCEPTIONS;
         }
      }
   }
}
