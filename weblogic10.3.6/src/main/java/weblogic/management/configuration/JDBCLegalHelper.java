package weblogic.management.configuration;

import weblogic.jdbc.common.internal.JDBCConstants;
import weblogic.management.internal.ManagementTextTextFormatter;

public final class JDBCLegalHelper extends JDBCConstants {
   public static void validateDataSourceJNDIName(JDBCDataSourceMBean var0) throws IllegalArgumentException {
      TargetMBean[] var1 = var0.getTargets();
      if (var1 != null && var1.length > 0 && (var0.getJNDIName() == null || var0.getJNDIName().trim().length() == 0)) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getInvalidJNDIName(var0.getName()));
      }
   }
}
