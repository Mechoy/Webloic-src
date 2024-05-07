package weblogic.xml.crypto.api.dom;

import org.w3c.dom.Element;

public interface DOMIdMap {
   Element getElementById(String var1);

   void setIdAttributeNS(Element var1, String var2, String var3);
}
