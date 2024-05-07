package weblogic.xml.security.specs;

import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public abstract class EntityDescriptor implements SpecConstants {
   private String name = null;
   private String password = null;

   public EntityDescriptor(String var1, String var2) {
      this.name = var1;
      this.password = var2;
   }

   public EntityDescriptor(XMLInputStream var1) throws XMLStreamException {
      this.fromXMLInternal(var1);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public abstract String elementTagName();

   public void fromXMLInternal(XMLInputStream var1) throws XMLStreamException {
      XMLEvent var2 = StreamUtils.skipWS(var1, false);
      StartElement var3 = null;
      XMLName var4 = null;
      if (!var2.isStartElement()) {
         throw new XMLStreamException("Did not receive expected element " + this.elementTagName());
      } else {
         var3 = (StartElement)var2;
         var4 = var3.getName();
         if (!var4.getLocalName().equals(this.elementTagName())) {
            throw new XMLStreamException("Did not receive expected element " + this.elementTagName());
         } else {
            var2 = this.skipToElement(var1);
            if (!var2.isStartElement()) {
               throw new XMLStreamException(this.elementTagName() + " did not include a " + "name" + " or " + "password");
            } else {
               var3 = (StartElement)var2;
               String var5 = var3.getName().getLocalName();
               if (!"name".equals(var5)) {
                  throw new XMLStreamException("In " + this.elementTagName() + ", got " + var5 + " expected " + "name");
               } else {
                  this.name = getValue(var1, "name");
                  var2 = this.skipToElement(var1);
                  if (!var2.isStartElement()) {
                     throw new XMLStreamException(this.elementTagName() + " did not include a " + "password");
                  } else {
                     var3 = (StartElement)var2;
                     var5 = var3.getName().getLocalName();
                     if (!"password".equals(var5)) {
                        throw new XMLStreamException("In " + this.elementTagName() + ", got " + var5 + " expected " + "password");
                     } else {
                        this.password = getValue(var1, "password");
                        StreamUtils.closeScope(var1, this.elementTagName());
                     }
                  }
               }
            }
         }
      }
   }

   private XMLEvent skipToElement(XMLInputStream var1) throws XMLStreamException {
      XMLEvent var2;
      for(var2 = StreamUtils.skipWS(var1, true); !var2.isEndElement() && !var2.isStartElement(); var2 = StreamUtils.skipWS(var1, true)) {
         var1.next();
      }

      return var2;
   }

   public void toXML(XMLOutputStream var1) throws XMLStreamException {
      StreamUtils.addStart(var1, this.elementTagName());
      if (this.name != null) {
         StreamUtils.addElement(var1, "name", this.name, 0);
      }

      if (this.password != null) {
         StreamUtils.addElement(var1, "password", this.password, 0);
      }

      StreamUtils.addEnd(var1, this.elementTagName());
   }

   public static String getValue(XMLInputStream var0, String var1) throws XMLStreamException {
      XMLEvent var2 = getElementByTagName(var0, var1);
      if (var2 == null) {
         return null;
      } else {
         var2 = StreamUtils.skipWS(var0, false);
         if (!var2.isCharacterData()) {
            throw new XMLStreamException(var1 + " does not contain text: " + var2);
         } else {
            String var3 = ((CharacterData)var2).getContent();
            var2 = StreamUtils.skipWS(var0, false);
            if (!var2.isEndElement()) {
               throw new XMLStreamException("Malformed " + var1);
            } else {
               return var3.trim();
            }
         }
      }
   }

   private static XMLEvent getElementByTagName(XMLInputStream var0, String var1) throws XMLStreamException {
      XMLEvent var2 = StreamUtils.skipWS(var0, true);
      return var2.isStartElement() && var2.getName().getLocalName().equals(var1) ? var0.next() : null;
   }
}
