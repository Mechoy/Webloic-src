package weblogic.xml.security.utils;

import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;

public abstract class XMLObjectReader {
   private ConcurrentHashMap types = new ConcurrentHashMap();

   public Object readObject(XMLInputStream var1) throws XMLStreamException {
      XMLName var2 = this.getNextName(var1);
      if (var2 == null) {
         return null;
      } else {
         Integer var3 = (Integer)this.types.get(var2);
         return var3 == null ? null : this.readObjectInternal(var3, var2.getNamespaceUri(), var1);
      }
   }

   public Object readObject(XMLInputStream var1, int var2) throws XMLStreamException {
      XMLName var3 = this.getNextName(var1);
      if (var3 == null) {
         return null;
      } else {
         Integer var4 = (Integer)this.types.get(var3);
         if (var4 == null) {
            return null;
         } else {
            return var4 != var2 ? null : this.readObjectInternal(var4, var3.getNamespaceUri(), var1);
         }
      }
   }

   private XMLName getNextName(XMLInputStream var1) throws XMLStreamException {
      XMLEvent var2 = StreamUtils.peekElement(var1);
      return var2 != null && var2.isStartElement() ? var2.getName() : null;
   }

   public abstract Object readObjectInternal(int var1, String var2, XMLInputStream var3) throws XMLStreamException;

   protected void register(String var1, String var2, int var3) {
      XMLName var4 = ElementFactory.createXMLName(var1, var2);
      this.types.put(var4, new Integer(var3));
   }
}
