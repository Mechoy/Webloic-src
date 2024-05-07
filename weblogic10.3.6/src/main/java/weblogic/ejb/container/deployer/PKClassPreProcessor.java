package weblogic.ejb.container.deployer;

import com.bea.objectweb.asm.ClassAdapter;
import com.bea.objectweb.asm.ClassReader;
import com.bea.objectweb.asm.ClassVisitor;
import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.FieldVisitor;
import com.bea.objectweb.asm.Label;
import com.bea.objectweb.asm.MethodVisitor;
import com.bea.objectweb.asm.Type;
import com.bea.objectweb.asm.commons.AdviceAdapter;
import com.bea.objectweb.asm.tree.FieldNode;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.utils.classloaders.ClassPreProcessor;

public class PKClassPreProcessor extends EJBClassEnhancer implements ClassPreProcessor {
   private static final String _WL_HASHCODE_NAME = "_WL_HASHCODE_";
   private byte[] bytes;
   private Map<String, Boolean> pkClassNames = new HashMap();

   public PKClassPreProcessor(List<String> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.pkClassNames.put(var2.next(), Boolean.FALSE);
      }

   }

   public void initialize(Hashtable var1) {
   }

   public byte[] preProcess(String var1, byte[] var2) {
      if (this.pkClassNames.containsKey(var1) && !(Boolean)this.pkClassNames.get(var1)) {
         FieldNode var3 = new FieldNode(130, "_WL_HASHCODE_", Type.INT_TYPE.getDescriptor(), (String)null, 0);
         ClassReader var4 = new ClassReader(var2);
         int var6 = (var2[6] & 255) << 8 | var2[7] & 255;
         ClassWriter var5;
         if (var6 > 50) {
            var5 = new ClassWriter(var4, 2);
         } else {
            var5 = new ClassWriter(var4, 0);
         }

         HashCodeClassEnhancer var7 = new HashCodeClassEnhancer(var5, var3);
         var4.accept(var7, 4);
         this.bytes = var5.toByteArray();
         this.pkClassNames.put(var1, Boolean.TRUE);
         this.writeEnhancedClassBack(var1, this.bytes);
         return this.bytes;
      } else {
         return var2;
      }
   }

   private class HashCodeMethodEnhancer extends AdviceAdapter {
      private String owner;
      private Label label = new Label();

      protected HashCodeMethodEnhancer(MethodVisitor var2, int var3, String var4, String var5, String var6) {
         super(var2, var3, var4, var5);
         this.owner = var6;
      }

      protected void onMethodEnter() {
         this.visitVarInsn(25, 0);
         this.visitFieldInsn(180, this.owner, "_WL_HASHCODE_", Type.INT_TYPE.getDescriptor());
         this.visitJumpInsn(154, this.label);
         this.visitVarInsn(25, 0);
      }

      protected void onMethodExit(int var1) {
         this.visitFieldInsn(181, this.owner, "_WL_HASHCODE_", Type.INT_TYPE.getDescriptor());
         this.visitLabel(this.label);
         this.visitVarInsn(25, 0);
         this.visitFieldInsn(180, this.owner, "_WL_HASHCODE_", Type.INT_TYPE.getDescriptor());
      }

      public void visitMaxs(int var1, int var2) {
         super.visitMaxs(var1 + 1, var2);
      }
   }

   private class InitMethodAdapter extends AdviceAdapter {
      private String owner;

      public InitMethodAdapter(MethodVisitor var2, int var3, String var4, String var5, String var6) {
         super(var2, var3, var4, var5);
         this.owner = var6;
      }

      protected void onMethodEnter() {
      }

      protected void onMethodExit(int var1) {
         this.visitVarInsn(25, 0);
         this.visitInsn(3);
         this.visitFieldInsn(181, this.owner, "_WL_HASHCODE_", Type.INT_TYPE.getDescriptor());
      }

      public void visitMaxs(int var1, int var2) {
         super.visitMaxs(var1 + 1, var2);
      }
   }

   private class HashCodeClassEnhancer extends ClassAdapter {
      private final FieldNode fn;
      private boolean isFieldPresent = false;
      private String owner;
      private ClassVisitor cv;
      private boolean noNeedEnhance;

      public HashCodeClassEnhancer(ClassVisitor var2, FieldNode var3) {
         super(var2);
         this.fn = var3;
         this.cv = var2;
      }

      public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
         if ((var2 & 512) != 0) {
            this.cv.visit(var1, var2, var3, var4, var5, var6);
            this.noNeedEnhance = true;
         } else {
            this.owner = var3;
            this.cv.visit(var1, var2, var3, var4, var5, var6);
         }
      }

      public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
         Object var6 = this.cv.visitMethod(var1, var2, var3, var4, var5);
         if (this.noNeedEnhance) {
            return (MethodVisitor)var6;
         } else {
            if ("<init>".equals(var2)) {
               var6 = PKClassPreProcessor.this.new InitMethodAdapter((MethodVisitor)var6, var1, var2, var3, this.owner);
            }

            if ("hashCode".equals(var2) && "()I".equals(var3)) {
               var6 = PKClassPreProcessor.this.new HashCodeMethodEnhancer((MethodVisitor)var6, var1, var2, var3, this.owner);
            }

            return (MethodVisitor)var6;
         }
      }

      public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
         if (var2.equals("_WL_HASHCODE_")) {
            this.isFieldPresent = true;
         }

         return this.cv.visitField(var1, var2, var3, var4, var5);
      }

      public void visitEnd() {
         if (!this.isFieldPresent && !this.noNeedEnhance) {
            this.fn.accept(this.cv);
         }

         super.visitEnd();
      }
   }
}
