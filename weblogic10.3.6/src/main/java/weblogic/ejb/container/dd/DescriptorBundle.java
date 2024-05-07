package weblogic.ejb.container.dd;

import java.util.ListResourceBundle;
import weblogic.utils.Debug;

public final class DescriptorBundle extends ListResourceBundle {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   public static final String NO_CMP_PROPERTIES_SET = "NO_CMP_PROPERTIES_SET";
   public static final String CMP_PROPERTIES_SET = "CMP_PROPERTIES_SET";
   static final Object[][] contents = new Object[][]{{"NO_CMP_PROPERTIES_SET", "No container managed deployment properties were set for CMP bean {0}"}, {"CMP_PROPERTIES_SET", "Container managed deployment properties were set for BMP bean {0}"}};

   public Object[][] getContents() {
      return contents;
   }

   static {
      if (debug) {
         int var0 = DescriptorBundle.class.getDeclaredFields().length;
         int var1 = contents.length;
         Debug.say("DeclaredCount is " + var0);
         Debug.say("contentsLength is " + var1);
         Debug.assertion(var0 - 4 == var1);
      }

   }
}
