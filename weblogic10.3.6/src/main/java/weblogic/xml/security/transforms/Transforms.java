package weblogic.xml.security.transforms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.signature.DSIGReader;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class Transforms implements DSIGConstants {
   List transforms = new ArrayList();
   private static final boolean VERBOSE = false;
   private static final XMLInputStreamFactory inFactory = XMLInputStreamFactory.newInstance();
   private Map namespaces = null;

   public Transforms() {
   }

   private Transforms(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public void add(Transform var1) throws IncompatibleTransformException {
      if (this.transforms.size() == 0) {
         this.transforms.add(var1);
      } else {
         Transform var2 = (Transform)this.transforms.get(this.transforms.size() - 1);
         var2.setDest(var1);
         this.transforms.add(var1);
      }

   }

   public Transform remove(Transform var1) throws IncompatibleTransformException {
      int var2 = this.transforms.indexOf(var1);
      if (var2 == -1) {
         return null;
      } else {
         Transform var3 = null;
         if (var2 < this.transforms.size() - 2) {
            var3 = (Transform)this.transforms.get(var2 + 1);
         }

         if (var2 > 1) {
            Transform var4 = (Transform)this.transforms.get(var2 - 1);
            var4.setDest(var3);
         }

         return (Transform)this.transforms.remove(var2);
      }
   }

   public final Transform[] getTransforms() {
      Transform[] var1 = new Transform[this.transforms.size()];
      var1 = (Transform[])((Transform[])this.transforms.toArray(var1));
      return var1;
   }

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      Transform var1 = this.getHead();
      return var1 == null ? null : var1.getXMLOutputStream();
   }

   public void setNamespaces(Map var1) {
      this.namespaces = var1;
      Transform var2 = this.getHead();
      if (var2 != null) {
         var2.setNamespaces(var1);
      }
   }

   public void perform(InputStream var1) throws XMLStreamException, IOException {
      Transform var2 = this.getHead();
      if (var2 != null) {
         if (var2 instanceof NodeTransform) {
            NodeTransform var3 = (NodeTransform)var2;
            XMLInputStream var4 = null;
            if (this.namespaces == null) {
               var4 = inFactory.newInputStream(var1);
            } else {
               var4 = inFactory.newFragmentInputStream(var1, this.namespaces);
            }

            XMLOutputStream var5 = var3.getXMLOutputStream();
            var5.add(var4);
            var5.flush();
         } else {
            OctetTransform var7 = (OctetTransform)var2;
            OutputStream var8 = var7.getOutputStream();
            byte[] var9 = new byte[4096];
            boolean var6 = false;

            int var10;
            while((var10 = var1.read(var9, 0, var9.length)) != -1) {
               var8.write(var9, 0, var10);
            }

            var8.flush();
            var8.close();
         }

      }
   }

   public int size() {
      return this.transforms.size();
   }

   public Transform get(int var1) {
      return (Transform)this.transforms.get(var1);
   }

   private Transform getHead() {
      return this.transforms.size() < 1 ? null : (Transform)this.transforms.get(0);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (this.transforms.size() >= 1) {
         StreamUtils.addStart(var1, var2, "Transforms", var3);

         for(int var4 = 0; var4 < this.size(); ++var4) {
            Transform var5 = this.get(var4);
            var5.toXML(var1, var2, var3 + 2);
         }

         StreamUtils.addEnd(var1, var2, "Transforms", var3);
      }
   }

   public static Transforms fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      return StreamUtils.peekElement(var0, var1, "Transforms") ? new Transforms(var0, var1) : null;
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      var1.skip();

      Transform var3;
      while((var3 = Transform.fromXML(var1, var2)) != null) {
         try {
            this.add(var3);
         } catch (IncompatibleTransformException var5) {
            throw new XMLStreamException(var5);
         }
      }

      StreamUtils.closeScope(var1, var2, "Transforms");
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<Transforms xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n  <Transform Algorithm=\"http://www.w3.org/TR/1999/REC-xpath-19991116\">\n    <XPath>\n      self::text()\n    </XPath>\n  </Transform>\n</Transforms>\n");
      Transforms var2 = (Transforms)DSIGReader.read(var1, 12);
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2000/09/xmldsig#", 0);
      var3.flush();
   }
}
