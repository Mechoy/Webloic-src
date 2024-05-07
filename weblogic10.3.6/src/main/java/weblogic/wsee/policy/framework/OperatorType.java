package weblogic.wsee.policy.framework;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class OperatorType implements Externalizable {
   public static final OperatorType ALL = new OperatorType("ALL");
   public static final OperatorType ONE_OR_MORE = new OperatorType("ONE_OR_MORE");
   public static final OperatorType EXACTLY_ONE = new OperatorType("EXACTLY_ONE");
   public static final OperatorType TERMINAL = new OperatorType("TERMINAL");
   private String myName;

   private OperatorType(String var1) {
      this.myName = var1;
   }

   public String toString() {
      return this.myName;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.myName = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.myName);
   }
}
