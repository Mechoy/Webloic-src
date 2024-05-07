package weblogic.ejb.container.dd.xml;

import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.xml.dom.ResourceEntityResolver;

public final class EJBEntityResolver extends ResourceEntityResolver {
   private static final boolean debug = false;

   public EJBEntityResolver() {
      this.addEntityResource("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "ejb11-jar.dtd", this.getClass());
      this.addEntityResource("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "ejb20-jar.dtd", this.getClass());
      this.addEntityResource("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN", "weblogic510-ejb-jar.dtd", this.getClass());
      this.addEntityResource("-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB//EN", "weblogic600-ejb-jar.dtd", this.getClass());
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      InputSource var3 = super.resolveEntity(var1, var2);
      if (var3 == null) {
         throw new SAXException(new IllegalResourceException(var1));
      } else {
         return var3;
      }
   }
}
