package weblogic.xml.security.encryption;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import weblogic.xml.security.keyinfo.KeyResolver;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.ReferenceResolver;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;

public final class DecryptXMLInputStream implements XMLInputStream, XMLEncConstants {
   private final XMLInputStream source;
   private final String encoding;
   private final Set refs;
   private KeyResolver keyResolver;
   private DecryptXMLInputStream clearTextStream;

   public DecryptXMLInputStream(XMLInputStream var1) {
      this(var1, (Set)null, "UTF-8");
   }

   public DecryptXMLInputStream(XMLInputStream var1, ReferenceList var2, String var3) {
      this(var1, (Set)(var2 != null ? new HashSet() : null), var3);
      if (var2 != null) {
         Iterator var4 = var2.getReferences();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            if (var5 instanceof DataReference) {
               this.refs.add(((DataReference)var5).getURI());
            } else {
               this.refs.add(((KeyReference)var5).getURI());
            }
         }
      }

   }

   public DecryptXMLInputStream(XMLInputStream var1, ReferenceList var2) {
      this(var1, var2, "UTF-8");
   }

   private DecryptXMLInputStream(XMLInputStream var1, Set var2, String var3) {
      this.refs = var2;
      this.source = var1;
      this.encoding = var3;
   }

   public void setKeyResolver(KeyResolver var1) {
      this.keyResolver = var1;
   }

   public KeyResolver getKeyResolver() {
      return this.keyResolver;
   }

   public XMLEvent next() throws XMLStreamException {
      return this.getStream().next();
   }

   private static String getId(StartElement var0) {
      AttributeIterator var1 = var0.getAttributes();

      Attribute var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = var1.next();
      } while(!var2.getName().getLocalName().toLowerCase(Locale.ENGLISH).equals("id"));

      return var2.getValue();
   }

   private XMLInputStream getStream() throws XMLStreamException {
      XMLEvent var1;
      if (this.clearTextStream != null) {
         var1 = this.clearTextStream.peek();
         if (var1 != null && !var1.isEndDocument() && !var1.isNull()) {
            return this.clearTextStream;
         }

         this.clearTextStream = null;
      }

      var1 = this.source.peek();
      if (var1 != null && var1.isStartElement() && "EncryptedData".equals(var1.getName().getLocalName())) {
         StartElement var2 = (StartElement)var1;
         if (this.refs != null) {
            String var3 = getId(var2);
            if (var3 == null || !this.refs.contains("#" + var3)) {
               return this.source;
            }
         }

         EncryptedData var4 = (EncryptedData)XMLEncReader.read(this.source, 1);
         if (var4 != null) {
            var4.setKeyResolver(this.keyResolver);
            this.clearTextStream = new DecryptXMLInputStream(var4.getXMLInputStream(this.encoding), this.refs, this.encoding);
            this.clearTextStream.setKeyResolver(this.getKeyResolver());
            this.clearTextStream.setReferenceResolver(this.getReferenceResolver());
            if (this.clearTextStream.skip(256)) {
               this.clearTextStream.skip();
            }

            return this.getStream();
         }
      }

      return this.source;
   }

   public boolean hasNext() throws XMLStreamException {
      return this.getStream().hasNext();
   }

   public void skip() throws XMLStreamException {
      this.getStream().skip();
   }

   public void skipElement() throws XMLStreamException {
      this.getStream().skipElement();
   }

   public XMLEvent peek() throws XMLStreamException {
      return this.getStream().peek();
   }

   public boolean skip(int var1) throws XMLStreamException {
      if (this.clearTextStream != null) {
         if (this.clearTextStream.skip(var1)) {
            return true;
         } else if (this.source.skip(var1)) {
            this.clearTextStream = null;
            return true;
         } else {
            return false;
         }
      } else {
         return this.source.skip(var1);
      }
   }

   public boolean skip(XMLName var1) throws XMLStreamException {
      if (this.clearTextStream != null) {
         if (this.clearTextStream.skip(var1)) {
            return true;
         } else if (this.source.skip(var1)) {
            this.clearTextStream = null;
            return true;
         } else {
            return false;
         }
      } else {
         return this.source.skip(var1);
      }
   }

   public boolean skip(XMLName var1, int var2) throws XMLStreamException {
      if (this.clearTextStream != null) {
         if (this.clearTextStream.skip(var1, var2)) {
            return true;
         } else if (this.source.skip(var1, var2)) {
            this.clearTextStream = null;
            return true;
         } else {
            return false;
         }
      } else {
         return this.source.skip(var1, var2);
      }
   }

   public XMLInputStream getSubStream() throws XMLStreamException {
      return this.getStream().getSubStream();
   }

   public void close() throws XMLStreamException {
      if (this.clearTextStream != null) {
         this.clearTextStream.close();
      }

      this.source.close();
   }

   public ReferenceResolver getReferenceResolver() {
      return this.source.getReferenceResolver();
   }

   public void setReferenceResolver(ReferenceResolver var1) {
      this.source.setReferenceResolver(var1);
   }
}
