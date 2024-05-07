package weblogic.xml.security.utils;

import weblogic.xml.babel.stream.XMLInputStreamFactoryImpl;
import weblogic.xml.stream.BufferedXMLInputStream;
import weblogic.xml.stream.ReferenceResolver;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;

public abstract class XMLInputStreamBase implements XMLInputStream {
   protected static final XMLInputStreamFactory factory = new XMLInputStreamFactoryImpl();
   protected XMLInputStream source;

   protected XMLInputStreamBase(XMLInputStream var1) {
      this.source = var1;
   }

   protected XMLInputStreamBase() {
   }

   public abstract XMLEvent next() throws XMLStreamException;

   public boolean hasNext() throws XMLStreamException {
      return this.source.hasNext();
   }

   public final void skip() throws XMLStreamException {
      this.next();
   }

   public final void skipElement() throws XMLStreamException {
      int var1 = 0;
      boolean var2 = false;

      while(this.hasNext() && !var2) {
         XMLEvent var3 = this.next();
         switch (var3.getType()) {
            case 2:
               ++var1;
               break;
            case 4:
               --var1;
               if (var1 <= 0) {
                  var2 = true;
               }
               break;
            case 128:
            case 512:
               var2 = true;
         }
      }

   }

   public XMLEvent peek() throws XMLStreamException {
      return this.source.peek();
   }

   public final boolean skip(int var1) throws XMLStreamException {
      while(this.hasNext()) {
         XMLEvent var2 = this.peek();
         if (var2 == null) {
            return false;
         }

         if (var2.getType() == 128) {
            return false;
         }

         if ((var2.getType() & var1) != 0) {
            return true;
         }

         this.next();
      }

      return false;
   }

   public final boolean skip(XMLName var1) throws XMLStreamException {
      while(this.hasNext()) {
         XMLEvent var2 = this.peek();
         switch (var2.getType()) {
            case 2:
            case 4:
               if (var1.equals(var2.getName())) {
                  return true;
               }
            default:
               this.next();
         }
      }

      return false;
   }

   public final boolean skip(XMLName var1, int var2) throws XMLStreamException {
      while(this.skip(var1)) {
         XMLEvent var3 = this.peek();
         if ((var3.getType() & var2) != 0) {
            return true;
         }

         this.next();
      }

      return false;
   }

   public XMLInputStream getSubStream() throws XMLStreamException {
      if (!(this.source instanceof BufferedXMLInputStream)) {
         this.source = factory.newBufferedInputStream(this.source);
      }

      return this.source.getSubStream();
   }

   public final void close() throws XMLStreamException {
      this.source.close();
   }

   public final ReferenceResolver getReferenceResolver() {
      return this.source.getReferenceResolver();
   }

   public final void setReferenceResolver(ReferenceResolver var1) {
      this.source.setReferenceResolver(var1);
   }
}
