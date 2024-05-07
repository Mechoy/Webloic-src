package weblogic.rmi.internal;

import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.xml.dom.ResourceEntityResolver;

public final class RMIEntityResolver extends ResourceEntityResolver {
   private static final boolean debug = false;

   public RMIEntityResolver() {
      this.addEntityResource("-//BEA Systems, Inc.//RMI Runtime DTD 1.0//EN", "rmi.dtd", this.getClass());
      this.setUnresolvedIsFatal(true);
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      InputSource var3 = super.resolveEntity(var1, var2);
      if (var3 == null) {
         throw new SAXException("Could not access RMI DTD: " + var1);
      } else {
         return var3;
      }
   }
}
