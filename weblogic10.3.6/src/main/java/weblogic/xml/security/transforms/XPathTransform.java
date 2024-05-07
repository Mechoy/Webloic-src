package weblogic.xml.security.transforms;

import java.util.Map;
import weblogic.xml.security.signature.DSIGConstants;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.xpath.StreamXPath;
import weblogic.xml.xpath.XPathException;
import weblogic.xml.xpath.XPathStreamFactory;
import weblogic.xml.xpath.XPathStreamObserver;

public class XPathTransform extends NodeTransform implements DSIGConstants {
   private static final String XPATH_PROPERTY_NAME = "xpath";
   private NodeTransform dest;
   private StreamXPath xpath;

   protected XPathTransform() {
   }

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      XPathStreamFactory var1 = new XPathStreamFactory();
      XMLOutputStream var2 = this.dest.getXMLOutputStream();
      ObserverWriter var3 = new ObserverWriter(var2);
      var1.install(this.xpath, var3);
      return var1.createStream(var3);
   }

   public String getURI() {
      return "http://www.w3.org/TR/1999/REC-xpath-19991116";
   }

   public void setParameter(String var1, String var2) throws TransformException {
      if (var1.equalsIgnoreCase("xpath")) {
         try {
            this.xpath = new StreamXPath(var2);
         } catch (XPathException var4) {
            throw new TransformException("cannot create XPath transform", var4);
         }
      } else {
         throw new TransformException("Unknown property: " + var1 + "=" + var2);
      }
   }

   public void setNamespaces(Map var1) {
      this.dest.setNamespaces(var1);
   }

   public void setDest(NodeTransform var1) {
      this.dest = var1;
   }

   protected void toXMLInternal(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (this.xpath != null) {
         StreamUtils.addElement(var1, var2, "XPath", (String)this.xpath.toString(), var3, 2);
      }

   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      String var3 = StreamUtils.getValue(var1, var2, "XPath");

      try {
         this.setParameter("xpath", var3);
      } catch (TransformException var5) {
         throw new XMLStreamException(var5);
      }

      StreamUtils.required(var3, "XPathTransform", "XPath");
   }

   public String toString() {
      return "XPathTransform: " + this.xpath;
   }

   private static class ObserverWriter implements XMLOutputStream, XPathStreamObserver {
      private final XMLOutputStream dest;
      private XMLStreamException cachedException;

      public ObserverWriter(XMLOutputStream var1) {
         this.dest = var1;
      }

      public void observe(XMLEvent var1) {
         try {
            this.dest.add(var1);
         } catch (XMLStreamException var3) {
            this.cachedException = var3;
         }

      }

      public void observeAttribute(StartElement var1, Attribute var2) {
      }

      public void observeNamespace(StartElement var1, Attribute var2) {
      }

      public void add(XMLEvent var1) throws XMLStreamException {
         if (this.cachedException != null) {
            throw this.cachedException;
         }
      }

      public void add(XMLInputStream var1) throws XMLStreamException {
         if (this.cachedException != null) {
            throw this.cachedException;
         }
      }

      public void add(String var1) throws XMLStreamException {
         if (this.cachedException != null) {
            throw this.cachedException;
         }
      }

      public void add(Attribute var1) throws XMLStreamException {
         if (this.cachedException != null) {
            throw this.cachedException;
         }
      }

      public void close() throws XMLStreamException {
         this.dest.close();
      }

      public void close(boolean var1) throws XMLStreamException {
         this.dest.close(var1);
      }

      public void flush() throws XMLStreamException {
         this.dest.flush();
      }
   }
}
