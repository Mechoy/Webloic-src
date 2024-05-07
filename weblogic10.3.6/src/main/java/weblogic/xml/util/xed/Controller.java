package weblogic.xml.util.xed;

import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.xpath.XPathStreamObserver;

public class Controller implements XPathStreamObserver {
   private Context context;
   private Command command;
   private static final boolean debug = false;

   public Controller(Context var1, Command var2) {
      this.context = var1;
      this.command = var2;
   }

   public void observe(XMLEvent var1) {
      this.context.set(var1);
      this.evaluate();
   }

   public void observeAttribute(StartElement var1, Attribute var2) {
      this.context.set(var1, var2);
      this.evaluate();
   }

   public void observeNamespace(StartElement var1, Attribute var2) {
      this.context.set(var1, var2);
      this.evaluate();
   }

   public XMLInputStream getResult() throws XMLStreamException {
      return this.command.getResult();
   }

   private void evaluate() {
      try {
         this.command.evaluate(this.context);
      } catch (StreamEditorException var2) {
         var2.printStackTrace();
         System.out.println("Error: " + var2);
      }

   }
}
