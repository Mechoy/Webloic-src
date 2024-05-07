package weblogic.xml.security.signature;

import java.util.Iterator;
import weblogic.xml.security.transforms.Transforms;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class EnvelopingReference extends InternalReference implements DSIGConstants {
   public EnvelopingReference(String var1, DigestMethod var2) {
      super(var1, var2);
   }

   protected void process(Transforms var1) throws InvalidReferenceException {
      try {
         XMLOutputStream var2 = this.getXOS(var1);
         this.writeEnveloped(var2, "http://www.w3.org/2000/09/xmldsig#");
         var2.flush();
      } catch (XMLStreamException var3) {
         throw new InvalidReferenceException("cannot process reference", var3, this);
      }
   }

   void writeEnveloped(XMLOutputStream var1, String var2) throws XMLStreamException {
      String var3 = this.getURI();
      var3 = var3.substring(1, var3.length());
      Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("Id", var3)};
      StreamUtils.addStart(var1, var2, "Object", var4);
      Iterator var5 = this.observed.iterator();

      while(var5.hasNext()) {
         var1.add((XMLEvent)var5.next());
      }

      StreamUtils.addEnd(var1, var2, "Object");
   }

   public boolean consumes() {
      return true;
   }
}
