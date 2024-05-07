package weblogic.diagnostics.module;

import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.VersionMunger;

public class WLDFVersionMunger extends VersionMunger {
   private static final String WLDF_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-diagnostics";
   private static final String BEA_WLDF_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/weblogic-diagnostics";
   private static final String WLDF_OLD_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/90/diagnostics";

   public WLDFVersionMunger(InputStream var1, WLDFDescriptorLoader var2) throws XMLStreamException {
      super(var1, var2, "weblogic.diagnostics.descriptor.WLDFResourceBeanImpl$SchemaHelper2", (String)"http://xmlns.oracle.com/weblogic/weblogic-diagnostics");
   }

   protected boolean isOldNamespaceURI(String var1) {
      return var1 != null && (var1.equals("http://www.bea.com/ns/weblogic/90/diagnostics") || var1.equals("http://www.bea.com/ns/weblogic/weblogic-diagnostics"));
   }
}
