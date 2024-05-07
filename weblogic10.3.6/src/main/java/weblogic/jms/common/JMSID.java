package weblogic.jms.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.JMSIDFactories;
import weblogic.messaging.common.IDFactory;
import weblogic.messaging.common.IDImpl;

public final class JMSID extends IDImpl {
   static final long serialVersionUID = -8642705714360206450L;
   public static IDFactory idFactory;

   public static JMSID create() {
      return new JMSID(idFactory);
   }

   private JMSID(IDFactory var1) {
      super(var1);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof JMSID) ? false : super.equals(var1);
      }
   }

   public JMSID() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws ClassNotFoundException, IOException {
      super.readExternal(var1);
   }

   static {
      idFactory = JMSIDFactories.idFactory;
   }
}
