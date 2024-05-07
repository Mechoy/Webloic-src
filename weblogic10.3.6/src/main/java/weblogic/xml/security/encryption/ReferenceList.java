package weblogic.xml.security.encryption;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.TestUtils;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class ReferenceList implements XMLEncConstants {
   private List references = new LinkedList();

   public ReferenceList() {
   }

   public ReferenceList(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public void addReference(DataReference var1) {
      this.references.add(var1);
   }

   public void addReference(KeyReference var1) {
      this.references.add(var1);
   }

   public Iterator getReferences() {
      return this.references.iterator();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ReferenceList:\n");
      Iterator var2 = this.references.iterator();

      while(var2.hasNext()) {
         var1.append("  ").append(var2.next()).append("\n");
      }

      return var1.toString();
   }

   static ReferenceList fromXML(XMLInputStream var0, String var1) throws XMLStreamException {
      return StreamUtils.peekElement(var0, var1, "ReferenceList") ? new ReferenceList(var0, var1) : null;
   }

   private void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      var1.skip();
      Object var3 = null;

      do {
         var3 = DataReference.fromXML(var1, var2);
         if (var3 == null) {
            var3 = KeyReference.fromXML(var1, var2);
         }

         if (var3 != null) {
            this.references.add(var3);
         }
      } while(var3 != null);

      StreamUtils.closeScope(var1, var2, "ReferenceList");
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      StreamUtils.addStart(var1, var2, "ReferenceList", var3);
      Iterator var4 = this.references.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         if (var5 instanceof DataReference) {
            ((DataReference)var5).toXML(var1, var2, var3 + 2);
         } else {
            ((KeyReference)var5).toXML(var1, var2, var3 + 2);
         }
      }

      StreamUtils.addEnd(var1, var2, "ReferenceList", var3);
   }

   public static void main(String[] var0) throws Exception {
      XMLInputStream var1 = var0.length > 0 ? TestUtils.createXMLInputStreamFromFile(var0[0]) : TestUtils.createXMLInputStreamFromString("<ReferenceList xmlns=\"http://www.w3.org/2001/04/xmlenc#\">\n  <DataReference URI=\"#ED1\"/>\n  <KeyReference URI=\"#EK1\"/>\n  <DataReference URI=\"#ED2\"/>\n</ReferenceList>\n ");
      ReferenceList var2 = new ReferenceList(var1, "http://www.w3.org/2001/04/xmlenc#");
      System.out.println(var2);
      XMLOutputStream var3 = TestUtils.createXMLOutputStream(System.out);
      var2.toXML(var3, "http://www.w3.org/2001/04/xmlenc#", 0);
      var3.flush();
   }
}
