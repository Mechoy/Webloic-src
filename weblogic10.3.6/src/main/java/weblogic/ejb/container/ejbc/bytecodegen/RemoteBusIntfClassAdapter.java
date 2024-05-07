package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.AnnotationVisitor;
import com.bea.objectweb.asm.ClassAdapter;
import com.bea.objectweb.asm.ClassReader;
import com.bea.objectweb.asm.ClassVisitor;
import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.FieldVisitor;
import com.bea.objectweb.asm.MethodVisitor;
import java.io.IOException;
import java.io.InputStream;

public final class RemoteBusIntfClassAdapter extends ClassAdapter {
   private final String rbiBinName;

   private RemoteBusIntfClassAdapter(ClassVisitor var1, String var2) {
      super(var1);
      this.rbiBinName = var2;
   }

   public static byte[] getRBIBytes(Class<?> var0, String var1) throws IOException {
      String var2 = getResourceName(var0);
      ClassReader var3 = getReader(var2, var0.getClassLoader());
      ClassWriter var4 = new ClassWriter(var3, 0);
      RemoteBusIntfClassAdapter var5 = new RemoteBusIntfClassAdapter(var4, var1);
      var3.accept(var5, 0);
      return var4.toByteArray();
   }

   private static ClassReader getReader(String var0, ClassLoader var1) throws IOException {
      InputStream var2 = null;

      ClassReader var3;
      try {
         var2 = var1.getResourceAsStream(var0);
         var3 = new ClassReader(var2);
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var10) {
            }
         }

      }

      return var3;
   }

   private static String getResourceName(Class<?> var0) {
      String var1 = "";

      Class var2;
      for(var2 = var0; var2.getEnclosingClass() != null; var2 = var2.getEnclosingClass()) {
         var1 = "$" + var2.getSimpleName() + var1;
      }

      return (var2.getName() + var1).replace('.', '/') + ".class";
   }

   public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
      String[] var7 = new String[var6.length + 1];

      for(int var8 = 0; var8 < var6.length; ++var8) {
         var7[var8] = var6[var8];
      }

      var7[var6.length] = "java/rmi/Remote";
      this.cv.visit(var1, var2, this.rbiBinName, var4, var5, var7);
   }

   public void visitSource(String var1, String var2) {
   }

   public void visitOuterClass(String var1, String var2, String var3) {
      throw new AssertionError("EnclosingMethod NOT allowed for interfaces");
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      return null;
   }

   public void visitInnerClass(String var1, String var2, String var3, int var4) {
   }

   public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
      return null;
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      return var2.equals("<clinit>") ? null : this.cv.visitMethod(var1, var2, var3, var4, var5);
   }

   public void visitEnd() {
      this.cv.visitEnd();
   }
}
