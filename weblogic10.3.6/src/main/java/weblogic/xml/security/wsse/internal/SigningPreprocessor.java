package weblogic.xml.security.wsse.internal;

import weblogic.xml.security.signature.InternalReference;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.signature.SoapSignXMLOutputStream;
import weblogic.xml.security.transforms.ExcC14NTransform;
import weblogic.xml.security.transforms.IncompatibleTransformException;
import weblogic.xml.security.transforms.Transform;
import weblogic.xml.security.transforms.TransformException;
import weblogic.xml.security.utils.TaggingPreprocessor;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class SigningPreprocessor extends TaggingPreprocessor {
   public static final String FRAGMENT_URI = "#";
   private final SoapSignXMLOutputStream stream;
   private final Signature sig;
   private final String c14nAlg;

   public SigningPreprocessor(Signature var1, String var2, SoapSignXMLOutputStream var3) {
      this.stream = var3;
      this.sig = var1;
      this.c14nAlg = var2;
   }

   public void begin(StartElement var1, XMLOutputStream var2, String var3) throws XMLStreamException {
      String var4 = "#" + var3;
      this.addReference(var4);
      var2.add(var1);
   }

   private void addReference(String var1) {
      InternalReference var2 = new InternalReference(var1);

      try {
         ExcC14NTransform var3 = (ExcC14NTransform)Transform.getTransform(this.c14nAlg);
         var2.addTransform(var3);
      } catch (IncompatibleTransformException var4) {
         var4.printStackTrace();
      } catch (TransformException var5) {
         var5.printStackTrace();
      }

      this.sig.addReference(var2);
      this.stream.addReference(var2);
   }

   public void end(EndElement var1, XMLOutputStream var2) throws XMLStreamException {
      var2.add(var1);
   }
}
