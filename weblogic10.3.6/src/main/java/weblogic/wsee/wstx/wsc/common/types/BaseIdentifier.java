package weblogic.wsee.wstx.wsc.common.types;

import java.util.Map;
import javax.xml.namespace.QName;

public abstract class BaseIdentifier<I> {
   protected I delegate;

   protected BaseIdentifier(I var1) {
      this.delegate = var1;
   }

   public I getDelegate() {
      return this.delegate;
   }

   public abstract String getValue();

   public abstract void setValue(String var1);

   public abstract Map<QName, String> getOtherAttributes();

   public abstract QName getQName();
}
