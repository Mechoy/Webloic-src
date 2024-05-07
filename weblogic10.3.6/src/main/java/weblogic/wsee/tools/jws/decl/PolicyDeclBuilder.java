package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PolicyDeclBuilder {
   public static List<PolicyDecl> build(File var0, JAnnotatedElement var1) {
      File var2 = null;
      if (var0 != null) {
         var2 = var0.getParentFile();
      }

      ArrayList var3 = new ArrayList();
      JAnnotation var4 = var1.getAnnotation("weblogic.jws.Policy");
      JAnnotation var5 = var1.getAnnotation("weblogic.jws.Policies");
      if (var5 != null) {
         var3.addAll(processPoliciesAnnotation(var2, var5));
      } else if (var4 != null) {
         var3.add(new PolicyDecl(var2, var4));
      }

      return var3;
   }

   private static List<PolicyDecl> processPoliciesAnnotation(File var0, JAnnotation var1) {
      ArrayList var2 = new ArrayList();
      JAnnotationValue var3 = var1.getValue("value");
      if (var3 != null) {
         JAnnotation[] var4 = var3.asAnnotationArray();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               var2.add(new PolicyDecl(var0, var4[var5]));
            }
         }
      }

      return var2;
   }
}
