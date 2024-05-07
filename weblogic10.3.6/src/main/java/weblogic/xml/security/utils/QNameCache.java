package weblogic.xml.security.utils;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;

public class QNameCache implements QNameFactory {
   private static QNameFactory ourInstance;
   private final Map namespaces = new HashMap();

   public static synchronized QNameFactory getInstance() {
      if (ourInstance == null) {
         ourInstance = new QNameCache();
      }

      return ourInstance;
   }

   public static synchronized void reset() {
      ourInstance = null;
      getInstance();
   }

   private QNameCache() {
   }

   public QName getQName(String var1, String var2) {
      Map var3 = (Map)this.namespaces.get(var1);
      if (var3 == null) {
         var3 = this.addNamespace(var1);
      }

      Object var4 = var3.get(var2);
      QName var5 = (QName)var4;
      if (var5 == null) {
         var5 = this.addQName(var3, var1, var2);
      }

      return var5;
   }

   private QName addQName(Map var1, String var2, String var3) {
      synchronized(var1) {
         QName var5 = (QName)var1.get(var3);
         if (var5 == null) {
            var5 = new QName(var2, var3);
            var1.put(var3, var5);
         }

         return var5;
      }
   }

   private Map addNamespace(String var1) {
      synchronized(this.namespaces) {
         Object var3 = (Map)this.namespaces.get(var1);
         if (var3 == null) {
            var3 = new HashMap();
            this.namespaces.put(var1, var3);
         }

         return (Map)var3;
      }
   }
}
