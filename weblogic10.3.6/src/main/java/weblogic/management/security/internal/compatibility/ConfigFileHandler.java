package weblogic.management.security.internal.compatibility;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ConfigFileHandler {
   Document doc = null;

   protected ConfigFileHandler() {
   }

   protected void startDocument() {
   }

   protected void endDocument() {
   }

   protected void startElement(String var1, XMLAttributeList var2, Node var3) {
   }

   protected void endElement(Node var1) {
   }

   protected Document getDocument() {
      return this.doc;
   }

   void setDocument(Document var1) {
      this.doc = var1;
   }
}
