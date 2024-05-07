package weblogic.xml.security.transforms;

import weblogic.utils.Debug;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.utils.Observer;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLStreamException;

public class ExcC14NVerifyObserver implements Observer, DSIGConstants {
   private final ExcC14NTransform transform;
   private static final String XMLNS = "xmlns";
   private static final String DEFAULT_NS_PREFIX = "";

   public ExcC14NVerifyObserver(ExcC14NTransform var1) {
      this.transform = var1;
   }

   public boolean observe(XMLEvent var1) throws XMLStreamException {
      return false;
   }

   public boolean consumes() {
      return false;
   }

   private static void debugSay(String var0) {
      if (VERBOSE) {
         Debug.say(var0);
      }

   }
}
