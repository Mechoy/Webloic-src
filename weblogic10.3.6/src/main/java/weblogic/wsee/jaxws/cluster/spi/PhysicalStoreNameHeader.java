package weblogic.wsee.jaxws.cluster.spi;

import com.sun.xml.ws.message.StringHeader;
import javax.xml.namespace.QName;

public class PhysicalStoreNameHeader extends StringHeader {
   public static final QName QNAME = new QName("http://www.oracle.com/wsee/jaxws/cluster/spi", "PhysicalStoreName", "clspi");

   public PhysicalStoreNameHeader(String var1) {
      super(QNAME, var1);
      if (var1 == null || var1.length() < 1) {
         throw new IllegalArgumentException("Null or empty physical store name given: " + var1);
      }
   }
}
