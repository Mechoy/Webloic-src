package weblogic.auddi.xml;

import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;

class XMLTextTransformer {
   Transformer transformer;

   public XMLTextTransformer() {
      try {
         this.transformer = TransformerFactory.newInstance().newTransformer();
      } catch (TransformerConfigurationException var2) {
         var2.printStackTrace();
      }

   }

   public String transform(Node var1) {
      if (this.transformer == null) {
         return null;
      } else {
         StringWriter var2 = new StringWriter();
         DOMSource var3 = new DOMSource(var1);
         StreamResult var4 = new StreamResult(var2);

         try {
            this.transformer.transform(var3, var4);
         } catch (TransformerException var6) {
            var6.printStackTrace();
            return null;
         }

         return var2.toString();
      }
   }
}
