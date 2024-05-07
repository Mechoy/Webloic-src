package weblogic.security.acl;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/** @deprecated */
public class SecurityMulticastRecord implements Serializable {
   String origin = null;
   int sequence_number = 0;
   long timestamp = 0L;

   SecurityMulticastRecord(String var1, int var2, long var3) {
      this.origin = var1;
      this.sequence_number = var2;
      this.timestamp = var3;
   }

   String eventOrigin() {
      return this.origin;
   }

   int eventSequenceNumber() {
      return this.sequence_number;
   }

   long eventTime() {
      return this.timestamp;
   }

   public String toString() {
      DateFormat var1 = DateFormat.getDateTimeInstance(2, 2);
      return "SecurityMulticastRecord: origin: " + this.origin + " seqnum: " + this.sequence_number + " timestamp: " + var1.format(new Date(this.timestamp));
   }
}
