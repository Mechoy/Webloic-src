package weblogic.xml.util.xed;

import java.io.StringReader;
import weblogic.xml.stream.BufferedXMLInputStream;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.stream.util.TypeFilter;

public class InsertCommand extends Command {
   private String xml;
   private BufferedXMLInputStream xis;
   private boolean hit = false;

   public String getName() {
      return "insert";
   }

   public void setXML(String var1) throws StreamEditorException {
      try {
         this.xml = var1;
         XMLInputStreamFactory var2 = XMLInputStreamFactory.newInstance();
         var2.setFilter(new TypeFilter(86));
         this.xis = var2.newBufferedInputStream(var2.newInputStream(new StringReader(var1)));
         this.xis.mark();

         while(this.xis.hasNext()) {
            this.xis.next();
         }

      } catch (XMLStreamException var3) {
         throw new StreamEditorException(var3.toString());
      }
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      this.hit = true;
      return null;
   }

   public String getXML() {
      return this.xml;
   }

   public String toString() {
      return super.toString() + " [" + this.getXML() + "]";
   }

   public XMLInputStream getResult() throws XMLStreamException {
      if (this.hit) {
         this.hit = false;
         this.xis.reset();
         this.xis.mark();
         return this.xis;
      } else {
         return null;
      }
   }
}
