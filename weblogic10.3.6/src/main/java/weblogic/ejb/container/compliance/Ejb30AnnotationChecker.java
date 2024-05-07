package weblogic.ejb.container.compliance;

import java.util.Map;

public class Ejb30AnnotationChecker extends BaseComplianceChecker {
   public static void validateNameAnnotation(String var0, Map<String, String[]> var1, String[] var2) throws ComplianceException {
      String[] var3 = (String[])var1.get(var0);
      if (var3 != null) {
         throw new ComplianceException(EJBComplianceTextFormatter.getInstance().EJB_ANNOTATION_VALUE_IS_DUPLICATE(var0, var3[0], var3[1], var2[0], var2[1]));
      } else {
         var1.put(var0, var2);
      }
   }
}
