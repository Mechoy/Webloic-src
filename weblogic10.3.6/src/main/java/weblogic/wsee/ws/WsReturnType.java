package weblogic.wsee.ws;

import weblogic.wsee.util.ToStringWriter;

public class WsReturnType extends WsType {
   WsReturnType(String var1) {
      super(var1, 3);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
