package weblogic.xml.security.signature;

import weblogic.xml.security.transforms.Transform;
import weblogic.xml.security.transforms.TransformException;
import weblogic.xml.security.transforms.Transforms;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class EnvelopedReference extends InternalReference {
   public EnvelopedReference() {
      super("");
      this.init();
   }

   public EnvelopedReference(DigestMethod var1) {
      super("", var1);
      this.init();
   }

   private void init() {
      try {
         this.addTransform(Transform.getTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature"));
      } catch (TransformException var2) {
         throw new AssertionError("Cannot find Enveloped signature transform");
      }
   }

   XMLOutputStream getXOS(Transforms var1) throws XMLStreamException {
      return var1.getXMLOutputStream();
   }
}
