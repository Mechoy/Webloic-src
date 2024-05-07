package weblogic.management.internal;

import java.io.Serializable;
import javax.management.ObjectName;

public final class RelaySource implements Serializable {
   private static final long serialVersionUID = -7433304619769354526L;
   private ObjectName m_objectName;

   public RelaySource(ObjectName var1) {
      this.m_objectName = var1;
   }

   public ObjectName getObjectName() {
      return this.m_objectName;
   }
}
