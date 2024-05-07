package weblogic.auddi.uddi;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.Util;

public class UDDIMessages {
   private static UDDIMessages s_instance = null;
   private ResourceBundle m_rb = null;
   private ArrayList m_bundles = new ArrayList();

   public static UDDIMessages getInstance() {
      if (s_instance == null) {
         Class var0 = UDDIMessages.class;
         synchronized(UDDIMessages.class) {
            if (s_instance == null) {
               s_instance = new UDDIMessages();
            }
         }
      }

      return s_instance;
   }

   public static void printKeys(int var0) {
      if (Logger.CanLog(var0)) {
         int var1 = getInstance().m_bundles.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            ResourceBundle var3 = (ResourceBundle)getInstance().m_bundles.get(var2);
            Logger.Log(var0, "=== ( " + var2 + " ) ====================================================");
            Enumeration var4 = var3.getKeys();

            while(var4.hasMoreElements()) {
               Logger.Log(var0, ">" + var4.nextElement().toString() + "<");
            }
         }
      }

   }

   public void register(String var1) {
      ResourceBundle var2 = Util.getResource(var1);
      this.m_bundles.add(var2);
   }

   public static String get(String var0) {
      return getIt(var0);
   }

   public static String get(String var0, String var1) {
      Object[] var2 = new Object[]{var1};
      MessageFormat var3 = new MessageFormat(get(var0));
      return var3.format(var2);
   }

   public static String get(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var1, var2};
      MessageFormat var4 = new MessageFormat(get(var0));
      return var4.format(var3);
   }

   public static String get(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var1, var2, var3};
      MessageFormat var5 = new MessageFormat(get(var0));
      return var5.format(var4);
   }

   public static String get(String var0, String[] var1) {
      MessageFormat var2 = new MessageFormat(get(var0));
      return var2.format(var1);
   }

   private static String getIt(String var0) {
      try {
         return getInstance().m_rb.getString(var0);
      } catch (MissingResourceException var5) {
         int var2 = getInstance().m_bundles.size();
         String var3 = null;

         for(int var4 = 0; var4 < var2; ++var4) {
            var3 = getIt(var0, (ResourceBundle)getInstance().m_bundles.get(var4));
            if (var3 != null) {
               return var3;
            }
         }

         Logger.error((Throwable)var5);
         return null;
      }
   }

   private static String getIt(String var0, ResourceBundle var1) {
      try {
         return var1.getString(var0);
      } catch (MissingResourceException var3) {
         return null;
      }
   }

   private UDDIMessages() {
      this.m_rb = Util.getResource(UDDIMessages.class);
   }

   public static void main(String[] var0) throws Exception {
      System.out.println(get("test.1"));
      System.out.println(get("test.2", "My Value"));
      System.out.println(get("test.4", "first", "second"));
      getInstance().register("MyMessagesFirst");
      getInstance().register("MyMessages");
      System.out.println(get("nasser.test.1"));
      System.out.println(get("nasser.test.2"));
      System.out.println(get("nasser.test.3"));
   }
}
