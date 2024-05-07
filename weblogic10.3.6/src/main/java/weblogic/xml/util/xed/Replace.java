package weblogic.xml.util.xed;

import java.util.ArrayList;
import java.util.Iterator;

public class Replace extends Command {
   private String xml;
   private ArrayList assignments = new ArrayList();

   public void setXML(String var1) {
      this.xml = var1;
   }

   public String getXML() {
      return this.xml;
   }

   public String getName() {
      return "replace";
   }

   public void add(Assignment var1) {
      this.assignments.add(var1);
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      Iterator var2 = this.assignments.iterator();

      while(var2.hasNext()) {
         ((Command)var2.next()).evaluate(var1);
      }

      return null;
   }

   public String toString() {
      if (this.getXML() != null) {
         return super.toString() + " [" + this.getXML() + "]";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString() + "\n");
         Iterator var2 = this.assignments.iterator();

         while(var2.hasNext()) {
            var1.append("\t[" + var2.next().toString() + "]\n");
         }

         return var1.toString();
      }
   }
}
