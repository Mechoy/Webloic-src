package weblogic.jdbc.rowset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import weblogic.utils.AssertionError;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;

public final class XMLWriter {
   private final XMLOutputStream xos;
   private final String defaultNamespaceUri;

   public XMLWriter(XMLOutputStream var1) {
      this(var1, (String)null);
   }

   public XMLWriter(XMLOutputStream var1, String var2) {
      this.xos = var1;
      this.defaultNamespaceUri = var2;
   }

   private XMLName createName(String var1) {
      return ElementFactory.createXMLName(this.defaultNamespaceUri, var1);
   }

   public void writeStartElement(XMLName var1) throws IOException {
      this.xos.add(ElementFactory.createStartElement(var1));
   }

   public void writeStartElement(String var1) throws IOException {
      this.writeStartElement(this.createName(var1));
   }

   public void writeStartElement(XMLName var1, Iterator var2) throws IOException {
      this.xos.add(ElementFactory.createStartElement(var1, ElementFactory.createAttributeIterator(var2)));
   }

   public void writeStartElement(String var1, Iterator var2) throws IOException {
      this.writeStartElement(this.createName(var1), var2);
   }

   public void writeStartElement(XMLName var1, Iterator var2, Iterator var3) throws IOException {
      this.xos.add(ElementFactory.createStartElement(var1, ElementFactory.createAttributeIterator(var2), ElementFactory.createAttributeIterator(var3)));
   }

   public void writeStartElement(String var1, Iterator var2, Iterator var3) throws IOException {
      this.writeStartElement(this.createName(var1), var2, var3);
   }

   public void writeStartElement(XMLName var1, String var2, String var3) throws IOException {
      ArrayList var4 = new ArrayList();
      var4.add(ElementFactory.createAttribute(var2, var3));
      this.writeStartElement(var1, var4.iterator());
   }

   public void writeStartElement(XMLName var1, XMLName var2, String var3) throws IOException {
      ArrayList var4 = new ArrayList();
      var4.add(ElementFactory.createAttribute(var2, var3));
      this.writeStartElement(var1, var4.iterator());
   }

   public void writeStartElement(String var1, String var2, String var3) throws IOException {
      this.writeStartElement(this.createName(var1), var2, var3);
   }

   public void writeStartElement(String var1, XMLName var2, String var3) throws IOException {
      this.writeStartElement(this.createName(var1), var2, var3);
   }

   public void writeStartElement(String var1, XMLName var2, String var3, XMLName var4, String var5) throws IOException {
      ArrayList var6 = new ArrayList();
      var6.add(ElementFactory.createAttribute(var2, var3));
      var6.add(ElementFactory.createAttribute(var4, var5));
      this.writeStartElement(var1, var6.iterator());
   }

   public void writeStartElement(XMLName var1, String var2, String var3, String var4, String var5) throws IOException {
      ArrayList var6 = new ArrayList();
      var6.add(ElementFactory.createAttribute(var2, var3));
      var6.add(ElementFactory.createAttribute(var4, var5));
      this.writeStartElement(var1, var6.iterator());
   }

   public void writeStartElement(XMLName var1, Map var2) throws IOException {
      if (var2.isEmpty()) {
         this.writeStartElement(var1);
      } else {
         ArrayList var3 = new ArrayList();
         Iterator var4 = var2.keySet().iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            String var6 = (String)var2.get(var5);
            if (var5 instanceof String) {
               var3.add(ElementFactory.createAttribute((String)var5, var6));
            } else {
               if (!(var5 instanceof XMLName)) {
                  throw new AssertionError("Unexpected Name type: " + var5.getClass().getName());
               }

               var3.add(ElementFactory.createAttribute((XMLName)var5, var6));
            }
         }

         this.xos.add(ElementFactory.createStartElement(var1, ElementFactory.createAttributeIterator(var3.iterator())));
      }

   }

   public void writeCharacterData(String var1) throws IOException {
      this.xos.add(ElementFactory.createCharacterData(var1));
   }

   public void writeEndElement(XMLName var1) throws IOException {
      this.xos.add(ElementFactory.createEndElement(var1));
   }

   public void writeEndElement(String var1) throws IOException {
      this.writeEndElement(this.createName(var1));
   }

   public void writeSimpleElements(String var1, String var2) throws IOException {
      this.writeStartElement(var1);
      this.writeCharacterData(var2);
      this.writeEndElement(var1);
   }

   public void writeSimpleElements(String var1, boolean var2) throws IOException {
      this.writeSimpleElements(var1, (new Boolean(var2)).toString());
   }

   public void writeSimpleElements(String var1, int var2) throws IOException {
      this.writeSimpleElements(var1, (new Integer(var2)).toString());
   }
}
