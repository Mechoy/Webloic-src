package weblogic.servlet.utils.annotation;

import com.bea.objectweb.asm.AnnotationVisitor;
import com.bea.objectweb.asm.MethodAdapter;
import com.bea.objectweb.asm.MethodVisitor;

public class MethodAnnotationDetector extends MethodAdapter {
   private AnnotationDetectContext context;

   public MethodAnnotationDetector(MethodVisitor var1, AnnotationDetectContext var2) {
      super(var1);
      this.context = var2;
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      if (!this.context.isAnnotationPresent()) {
         this.context.checkIfAnnotationPresent(var1, var2);
      }

      return null;
   }
}
