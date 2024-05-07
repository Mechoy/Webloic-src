package weblogic.xml.dtdc;

import java.io.IOException;
import java.io.PrintWriter;
import org.xml.sax.AttributeList;

public class DataElement implements GeneratedElement {
   private String value;

   public DataElement(String var1) {
      this.value = var1;
   }

   public String getValue() {
      return this.value;
   }

   public void initialize(String var1, AttributeList var2) {
   }

   public void toXML(PrintWriter var1, int var2) throws IOException {
      this.toXML(var1);
   }

   public void toXML(PrintWriter var1) throws IOException {
      var1.print(this.value);
   }

   public boolean isEmpty() {
      return true;
   }

   public String getElementName() {
      return "CDATA";
   }

   public boolean equals(Object var1) {
      if (var1 instanceof DataElement) {
         DataElement var2 = (DataElement)var1;
         if (var2.getValue().trim().equals(this.getValue().trim())) {
            return true;
         }
      }

      return false;
   }
}
