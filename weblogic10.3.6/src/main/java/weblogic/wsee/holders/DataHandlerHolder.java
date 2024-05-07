package weblogic.wsee.holders;

import javax.activation.DataHandler;
import javax.xml.rpc.holders.Holder;

public class DataHandlerHolder implements Holder {
   public DataHandler value;

   public DataHandlerHolder() {
   }

   public DataHandlerHolder(DataHandler var1) {
      this.value = var1;
   }
}
