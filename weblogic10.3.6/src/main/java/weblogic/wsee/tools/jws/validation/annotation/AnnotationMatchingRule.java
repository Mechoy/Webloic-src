package weblogic.wsee.tools.jws.validation.annotation;

import com.bea.util.jam.JAnnotation;
import java.util.HashSet;
import java.util.Set;

public class AnnotationMatchingRule implements MatchingRule {
   private Set<String> annotations = new HashSet();

   public AnnotationMatchingRule(Class var1) {
      assert var1 != null;

      this.annotations.add(var1.getName());
   }

   public AnnotationMatchingRule(Class[] var1) {
      assert var1 != null : "Annotations not specified";

      Class[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         this.annotations.add(var5.getName());
      }

   }

   public boolean isMatch(JAnnotation var1) {
      return this.annotations.contains(var1.getQualifiedName());
   }
}
