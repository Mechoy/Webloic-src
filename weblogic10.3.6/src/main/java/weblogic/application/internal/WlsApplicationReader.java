package weblogic.application.internal;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;

public class WlsApplicationReader extends VersionMunger {
   private static final Map wlNameChanges = new HashMap();

   public WlsApplicationReader(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      super(var1, var2, "weblogic.j2ee.descriptor.wl.WeblogicApplicationBeanImpl$SchemaHelper2", "http://xmlns.oracle.com/weblogic/weblogic-application");
   }

   public Map getElementNameChanges() {
      return wlNameChanges;
   }

   protected VersionMunger.Continuation onEndDocument() {
      this.orderChildren();
      return CONTINUE;
   }

   protected VersionMunger.Continuation onCharacters(String var1) {
      if (this.currentEvent != null && "start-mdbs-with-application".equals(this.currentEvent.getElementName())) {
         this.currentEvent.setCharacters(var1.toLowerCase().toCharArray());
      }

      return CONTINUE;
   }

   static {
      wlNameChanges.put("data-source-name", "data-source-jndi-name");
   }
}
