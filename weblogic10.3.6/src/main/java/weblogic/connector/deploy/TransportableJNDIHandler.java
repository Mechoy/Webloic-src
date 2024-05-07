package weblogic.connector.deploy;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;

public class TransportableJNDIHandler extends JNDIHandler {
   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws Exception {
      Reference var5 = null;
      String var6 = null;
      if (!(var1 instanceof Reference)) {
         return null;
      } else {
         var5 = (Reference)var1;
         var6 = var5.getFactoryClassName();
         return !var6.equals(TransportableJNDIHandler.class.getName()) ? null : this.lookupObject(var1, var2, var3, var4, var5, var6);
      }
   }
}
