package weblogic.servlet.utils.annotation;

import com.bea.objectweb.asm.AnnotationVisitor;
import com.bea.objectweb.asm.Attribute;
import com.bea.objectweb.asm.FieldVisitor;

public class FieldAnnotationDetector implements FieldVisitor {
   private FieldVisitor fv;
   private AnnotationDetectContext context;

   public FieldAnnotationDetector(FieldVisitor var1, AnnotationDetectContext var2) {
      this.fv = var1;
      this.context = var2;
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      if (!this.context.isAnnotationPresent()) {
         this.context.checkIfAnnotationPresent(var1, var2);
      }

      return null;
   }

   public void visitAttribute(Attribute var1) {
   }

   public void visitEnd() {
   }
}
