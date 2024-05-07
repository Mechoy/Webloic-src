package weblogic.xml.util.xed;

import java.util.ArrayList;
import java.util.Iterator;

public class Assignment extends Command {
   private String name;
   private Variable lhs;
   private ArrayList rhs = new ArrayList();

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public Object evaluate(Context var1) throws StreamEditorException {
      Iterator var2 = this.rhs.iterator();
      if (!var2.hasNext()) {
         throw new StreamEditorException("Evaluation error:invalid right hand side" + this.toString());
      } else {
         String var3;
         for(var3 = (String)((Variable)var2.next()).evaluate(var1); var2.hasNext(); var3 = var3 + (String)((Variable)var2.next()).evaluate(var1)) {
         }

         this.lhs.assign(var3, var1);
         return this.lhs;
      }
   }

   public Variable getLHS() {
      return this.lhs;
   }

   public ArrayList getRHS() {
      return this.rhs;
   }

   public void setLHS(Variable var1) {
      this.lhs = var1;
   }

   public void addRHS(Variable var1) {
      this.rhs.add(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.lhs + "=");
      Iterator var2 = this.rhs.iterator();
      if (var2.hasNext()) {
         var1.append(var2.next().toString());
      }

      while(var2.hasNext()) {
         var1.append("+" + var2.next().toString());
      }

      return var1.toString();
   }
}
