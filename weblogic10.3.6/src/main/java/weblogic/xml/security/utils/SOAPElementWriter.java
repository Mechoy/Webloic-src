package weblogic.xml.security.utils;

import java.util.HashMap;
import java.util.Iterator;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import weblogic.utils.collections.Stack;
import weblogic.webservice.core.soap.NameImpl;
import weblogic.webservice.core.soap.SOAPElementImpl;

public class SOAPElementWriter extends AbstractXMLWriter {
   protected static final String DEFAULT_NAMESPACE = "";
   private final SOAPElement root;
   private SOAPElement parent;

   public SOAPElementWriter(SOAPElement var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Parent cannot be null");
      } else {
         this.openScope();
         this.parent = var1;
         this.root = var1;
         Stack var2 = new Stack();
         SOAPElement var3 = this.root;

         do {
            var2.push(var3);
            var3 = var3.getParentElement();
         } while(var3 != null);

         do {
            var3 = (SOAPElement)var2.pop();
            Iterator var4 = var3.getNamespacePrefixes();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               this.addNamespacePrefix(var5, this.root.getNamespaceURI(var5));
            }
         } while(!var2.isEmpty());

      }
   }

   public void writeStartElement(String var1, String var2) {
      if (this.parent == null) {
         throw new XMLWriterRuntimeException("Only a single child can be written");
      } else {
         String var3 = this.findPrefix(var1);
         if (var3 == null) {
            var3 = this.generatePrefix(var1);

            try {
               this.parent = this.parent.addChildElement(var2, var3, var1);
               this.openScope();
               this.bindNamespace(var3, var1);
            } catch (SOAPException var6) {
               throw new XMLWriterRuntimeException(var6);
            }
         } else {
            try {
               this.parent = this.parent.addChildElement(var2, var3, var1);
               this.openScope();
            } catch (SOAPException var5) {
               throw new XMLWriterRuntimeException(var5);
            }
         }

      }
   }

   public void writeAttribute(String var1, String var2, String var3) throws XMLWriterRuntimeException, IllegalStateException {
      if (this.parent == null) {
         throw new XMLWriterRuntimeException("Only a single child can be written");
      } else {
         String var4 = this.findOrBindNamespace(var1);

         try {
            this.parent.addAttribute(new NameImpl(var2, var4, var1), var3);
         } catch (SOAPException var6) {
            throw new XMLWriterRuntimeException(var6);
         }
      }
   }

   public void writeCharacters(String var1) throws XMLWriterRuntimeException {
      if (this.parent == null) {
         throw new XMLWriterRuntimeException("Only a single child can be written");
      } else {
         try {
            this.parent.addTextNode(var1);
         } catch (SOAPException var3) {
            throw new XMLWriterRuntimeException(var3);
         }
      }
   }

   public void writeEndElement() throws XMLWriterRuntimeException {
      if (this.parent == null) {
         throw new XMLWriterRuntimeException("Only a single child can be written");
      } else if (this.parent != this.root) {
         this.closeScope();
         this.parent = this.parent.getParentElement();
      } else {
         throw new XMLWriterRuntimeException("Unbalaneced end element");
      }
   }

   public void flush() throws XMLWriterRuntimeException {
   }

   public void close() throws XMLWriterRuntimeException {
      this.flush();
   }

   protected void bindNamespace(String var1, String var2) throws XMLWriterRuntimeException {
      if ("".equals(var1)) {
         this.bindDefaultNamespace(var2);
      } else {
         try {
            this.parent.addNamespaceDeclaration(var1, var2);
         } catch (SOAPException var4) {
            throw new XMLWriterRuntimeException(var4);
         }

         this.addNamespacePrefix(var1, var2);
      }
   }

   protected void bindDefaultNamespace(String var1) throws XMLWriterRuntimeException {
      try {
         this.parent.addNamespaceDeclaration("", var1);
      } catch (SOAPException var3) {
         throw new XMLWriterRuntimeException(var3);
      }

      this.addDefaultNamespace(var1);
   }

   public SOAPElement getSOAPElement() {
      return this.root;
   }

   public static void main(String[] var0) {
      SOAPElementWriter var1;
      SOAPElementWriter var2 = var1 = new SOAPElementWriter(new SOAPElementImpl());
      HashMap var3 = new HashMap();
      var3.put("uri:ns2", "myPrefix");
      var1.setDefaultPrefixes(var3);
      var2.writeStartElement("uri:ns1", "foo");
      var2.writeAttribute("uri:ns1", "bar", "myValue");
      var2.writeAttribute("uri:ns2", "baz", "myValue2");
      var2.writeStartElement("uri:ns2", "boo");
      var2.writeAttribute("uri:ns3", "blah", "myValue3");
      var2.writeCharacters("this is my text");
      var2.writeEndElement();
      var2.writeEndElement();
      SOAPElement var4 = ((SOAPElementWriter)var2).getSOAPElement();
      System.out.println(var4.toString());
   }
}
