package weblogic.servlet.utils.annotation;

import com.bea.objectweb.asm.AnnotationVisitor;
import com.bea.objectweb.asm.ClassAdapter;
import com.bea.objectweb.asm.ClassReader;
import com.bea.objectweb.asm.ClassVisitor;
import com.bea.objectweb.asm.FieldVisitor;
import com.bea.objectweb.asm.MethodVisitor;
import java.io.IOException;
import java.io.InputStream;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.Source;

public class ClassAnnotationDetector extends ClassAdapter {
   private boolean isClassV5 = false;
   private AnnotationDetectContext context;
   private ClassFinder finder;
   private MethodVisitor mv;
   private FieldVisitor fv;

   public ClassAnnotationDetector(ClassVisitor var1, AnnotationDetectContext var2, ClassFinder var3) {
      super(var1);
      this.context = var2;
      this.finder = var3;
      if (!var2.isClassLevelOnly()) {
         this.mv = new MethodAnnotationDetector(AnnotationDetectContext.EMPTY_VISITOR, var2);
         this.fv = new FieldAnnotationDetector(AnnotationDetectContext.EMPTY_VISITOR, var2);
      } else {
         this.mv = null;
         this.fv = null;
      }

   }

   public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
      this.isClassV5 = (var1 & 255) >= 49;
      if (this.isClassV5) {
         if (!this.context.isClassLevelOnly()) {
            if (!var5.equals("java/lang/Object")) {
               this.visitSuper(convertClassName(var5));
            }

            String[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String var10 = var7[var9];
               this.visitSuper(convertClassName(var10));
            }

         }
      }
   }

   private void visitSuper(String var1) {
      visit(var1, this.finder, this.context);
   }

   private static String convertClassName(String var0) {
      return var0.replace('/', '.');
   }

   public static void visit(String var0, ClassFinder var1, AnnotationDetectContext var2) {
      Source var3 = var1.getClassSource(var0);
      InputStream var4 = null;

      try {
         if (var3 != null) {
            var4 = var3.getInputStream();
            ClassReader var5 = new ClassReader(var4);
            ClassAnnotationDetector var6 = new ClassAnnotationDetector(AnnotationDetectContext.EMPTY_VISITOR, var2, var1);
            var5.accept(var6, 7);
         }
      } catch (IOException var15) {
         var15.printStackTrace();
      } finally {
         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var14) {
            }
         }

      }

   }

   public static boolean isClassHasAnnotation(ClassFinder var0, String var1) {
      AnnotationDetectContext var2 = new AnnotationDetectContext(new String[0]);
      visit(var1, var0, var2);
      return var2.isAnnotationPresent();
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      if (!this.isClassV5) {
         return null;
      } else {
         if (!this.context.isAnnotationPresent()) {
            this.context.checkIfAnnotationPresent(var1, var2);
         }

         return null;
      }
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      return !this.isClassV5 ? null : this.mv;
   }

   public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
      return !this.isClassV5 ? null : this.fv;
   }
}
