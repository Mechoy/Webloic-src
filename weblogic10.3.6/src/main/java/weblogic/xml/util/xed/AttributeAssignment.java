package weblogic.xml.util.xed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import weblogic.xml.stream.Attribute;

public class AttributeAssignment extends Assignment {
   private void addResult(ArrayList var1, Object var2) throws StreamEditorException {
      System.out.println("obj----->" + var2);
      if (var2 instanceof Attribute) {
         var1.add(var2);
      } else if (var2 instanceof Collection) {
         var1.addAll((Collection)var2);
      } else {
         throw new StreamEditorException("Unable to add" + var2);
      }
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      if (var1.getEventType() != 2) {
         return null;
      } else {
         Iterator var2 = this.getRHS().iterator();
         if (!var2.hasNext()) {
            throw new StreamEditorException("Evaluation error:invalid right hand side" + this.toString());
         } else {
            ArrayList var3 = new ArrayList();
            this.addResult(var3, ((Variable)var2.next()).evaluate(var1));

            while(var2.hasNext()) {
               this.addResult(var3, ((Variable)var2.next()).evaluate(var1));
            }

            this.getLHS().assign(var3, var1);
            return this.getLHS();
         }
      }
   }
}
