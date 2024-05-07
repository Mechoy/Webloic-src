package weblogic.webservice.context;

import java.util.Iterator;
import javax.xml.namespace.QName;

/** @deprecated */
public interface WebServiceHeader {
   void put(QName var1, Object var2);

   Object get(QName var1);

   Object remove(QName var1);

   Iterator names();

   void clear();
}
