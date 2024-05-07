package weblogic.application.descriptor;

class VariableAssignment {
   static final int OP_DEFAULT = -1;
   static final int OP_ADD = 1;
   static final int OP_REMOVE = 2;
   static final int OP_REPLACE = 3;
   String name;
   int op;

   public VariableAssignment(String var1, String var2) {
      this.name = var1;
      this.setOperation(var2);
   }

   public void setOperation(String var1) {
      if (var1.equals("add")) {
         this.op = 1;
      } else if (var1.equals("remove")) {
         this.op = 2;
      } else if (var1.equals("replace")) {
         this.op = 3;
      } else {
         this.op = -1;
      }

   }

   public String getName() {
      return this.name;
   }

   public int getOperation() {
      return this.op;
   }
}
