package weblogic.jms.extensions;

import javax.jms.Destination;
import weblogic.jms.common.JMSException;

public final class SequenceGapException extends JMSException {
   static final long serialVersionUID = -2031233154467385324L;
   private Destination destination;
   private int missingCount;

   public SequenceGapException(String var1, String var2, Destination var3, int var4) {
      super(var1, var2);
      this.destination = var3;
      this.missingCount = var4;
   }

   public SequenceGapException(String var1, Destination var2, int var3) {
      super(var1);
      this.destination = var2;
      this.missingCount = var3;
   }

   public Destination getJMSDestination() {
      return this.destination;
   }

   public int getMissingCount() {
      return this.missingCount;
   }

   public boolean isInformational() {
      return true;
   }
}
