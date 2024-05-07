package weblogic.wsee.context;

import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;

public class WebServiceHeaderImpl implements WebServiceHeader {
   private HashMap headers = new HashMap();

   public void put(QName var1, Object var2) {
      this.headers.put(var1, var2);
   }

   public Object get(QName var1) {
      return this.headers.get(var1);
   }

   public Object remove(QName var1) {
      return this.headers.remove(var1);
   }

   public Iterator names() {
      return this.headers.keySet().iterator();
   }

   public void clear() {
      this.headers.clear();
   }
}
