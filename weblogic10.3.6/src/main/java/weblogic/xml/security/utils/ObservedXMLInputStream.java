package weblogic.xml.security.utils;

import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class ObservedXMLInputStream extends XMLInputStreamBase {
   private Observer observer;

   public ObservedXMLInputStream(XMLInputStream var1, Observer var2) {
      super(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("observer cannot be null");
      } else {
         this.observer = var2;
      }
   }

   public XMLEvent next() throws XMLStreamException {
      XMLEvent var1 = this.source.next();
      if (this.observer != null) {
         boolean var2 = this.observer.observe(var1);
         if (!var2) {
            this.observer = null;
         }
      }

      return var1;
   }
}
