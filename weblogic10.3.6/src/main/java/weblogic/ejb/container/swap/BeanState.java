package weblogic.ejb.container.swap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BeanState {
   private HashMap fieldValues = new HashMap();

   public Object get(Field var1) {
      return this.fieldValues.get(var1);
   }

   public void update(Field var1, Object var2) {
      this.fieldValues.put(var1, var2);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.fieldValues.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.append(var3.getKey()).append("-->").append(var3.getValue()).append("\n");
      }

      return var1.toString();
   }
}
