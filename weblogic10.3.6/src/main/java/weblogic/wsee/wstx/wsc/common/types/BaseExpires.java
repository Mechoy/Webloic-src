package weblogic.wsee.wstx.wsc.common.types;

import java.util.Map;
import javax.xml.namespace.QName;

public abstract class BaseExpires<T> {
   protected T delegate;

   protected BaseExpires(T var1) {
      this.delegate = var1;
   }

   public T getDelegate() {
      return this.delegate;
   }

   public abstract long getValue();

   public abstract void setValue(long var1);

   public abstract Map<QName, String> getOtherAttributes();
}
