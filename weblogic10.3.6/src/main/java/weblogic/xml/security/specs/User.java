package weblogic.xml.security.specs;

import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class User extends EntityDescriptor {
   public User(String var1, String var2) {
      super(var1, var2);
   }

   public User(XMLInputStream var1) throws XMLStreamException {
      super(var1);
   }

   public String elementTagName() {
      return "user";
   }
}
