package weblogic.auddi.util;

import java.util.Vector;

public class ShutdownManager {
   private static Vector s_appList = new Vector();

   private ShutdownManager() {
   }

   public static void registerApp(Shutdownable var0) {
      s_appList.addElement(var0);
   }

   public static void shutdownAll() throws ShutdownException {
      for(int var0 = 0; var0 < s_appList.size(); ++var0) {
         ((Shutdownable)s_appList.elementAt(var0)).shutdown();
      }

   }

   public static void main(String[] var0) throws Exception {
   }
}
