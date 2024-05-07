package weblogic.ejb.spi;

import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;

public final class WeblogicEjbJarReader extends VersionMunger {
   public WeblogicEjbJarReader(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      super(var1, var2, "weblogic.j2ee.descriptor.wl.WeblogicEjbJarBeanImpl$SchemaHelper2", "http://xmlns.oracle.com/weblogic/weblogic-ejb-jar");
   }

   public boolean supportsValidation() {
      return true;
   }
}
