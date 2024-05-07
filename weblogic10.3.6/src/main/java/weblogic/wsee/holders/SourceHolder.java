package weblogic.wsee.holders;

import javax.xml.rpc.holders.Holder;
import javax.xml.transform.Source;

public class SourceHolder implements Holder {
   public Source value;

   public SourceHolder() {
   }

   public SourceHolder(Source var1) {
      this.value = var1;
   }
}
