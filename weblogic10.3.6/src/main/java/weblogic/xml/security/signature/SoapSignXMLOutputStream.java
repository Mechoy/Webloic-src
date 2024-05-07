package weblogic.xml.security.signature;

import java.util.Iterator;
import java.util.Map;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.security.transforms.ExcC14NTransform;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.Observer;
import weblogic.xml.security.utils.XMLStreamObserver;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class SoapSignXMLOutputStream extends XMLOutputStreamBase implements DSIGConstants, NSOutputStream {
   private NSOutputStream dest;
   private XMLEvent lastSpace;
   private XMLStreamObserver unresolvedReferences;

   public SoapSignXMLOutputStream(XMLOutputStream var1) {
      this.unresolvedReferences = new XMLStreamObserver(WSSEConstants.ID_NAMESPACES);
      if (!(var1 instanceof NSOutputStream)) {
         this.dest = new NamespaceAwareXOS(var1);
      } else {
         this.dest = (NSOutputStream)var1;
      }

      this.addPrefix("http://www.w3.org/2000/09/xmldsig#", "dsig");
   }

   public void addSignature(Signature var1) throws XMLStreamException {
      this.setupSignature(var1);
   }

   protected void setupSignature(Signature var1) {
      Iterator var2 = var1.getReferences();

      while(var2.hasNext()) {
         Reference var3 = (Reference)var2.next();
         this.addReference(var3);
      }

   }

   public void addReference(Reference var1) {
      if (var1 instanceof InternalReference) {
         InternalReference var2 = (InternalReference)var1;
         String var3 = var1.getURI();
         this.unresolvedReferences.add((String)var3, var2);
         if (WSSEConstants.C14N_INCLUSIVE_NAMESPACES) {
            ExcC14NTransform.setupSign(this.unresolvedReferences, var3, var2);
         }
      }

   }

   public void addObserver(String var1, Observer var2) {
      this.unresolvedReferences.add(var1, var2);
   }

   protected void addXMLEvent(XMLEvent var1) throws XMLStreamException {
      if (var1 != null) {
         if (!this.unresolvedReferences.observe(var1)) {
            this.dest.add(var1);
         }

      }
   }

   public void flush() throws XMLStreamException {
      this.dest.flush();
   }

   public void close() throws XMLStreamException {
      this.flush();
      this.dest.close();
   }

   public void close(boolean var1) throws XMLStreamException {
      if (var1) {
         this.flush();
      }

      this.dest.close(var1);
   }

   public void addPrefix(String var1, String var2) {
      this.dest.addPrefix(var1, var2);
   }

   public Map getNamespaces() {
      return this.dest.getNamespaces();
   }
}
