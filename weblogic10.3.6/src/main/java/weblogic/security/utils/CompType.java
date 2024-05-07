package weblogic.security.utils;

import weblogic.security.spi.ApplicationInfo;
import weblogic.security.spi.ApplicationInfo.ComponentType;

public class CompType {
   public static final int ALL = 0;
   public static final int EJB = 1;
   public static final int WEBAPP = 2;
   public static final int CONNECTOR = 3;
   public static final int WEBSERVICE = 4;
   public static final int JDBC = 5;
   public static final int NONE = 100;
   public static final int APPLICATION = 101;

   public static int getComponentType(ApplicationInfo.ComponentType var0) {
      if (var0 == null) {
         return 100;
      } else if (var0 == ComponentType.WEBAPP) {
         return 2;
      } else if (var0 == ComponentType.EJB) {
         return 1;
      } else {
         return var0 == ComponentType.APPLICATION ? 101 : 100;
      }
   }
}
