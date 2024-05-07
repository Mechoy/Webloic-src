package weblogic.management.internal;

import java.util.Date;
import javax.management.ObjectName;

/** @deprecated */
public class AttributeChangeNotification extends javax.management.AttributeChangeNotification {
   private static final long serialVersionUID = -2892133712717201347L;
   private static long sequenceNumber = 0L;
   private String attributeType;

   public AttributeChangeNotification(ObjectName var1, String var2, Object var3, Object var4) {
      this(var1, generateSequenceNumber(), (new Date()).getTime(), "WebLogic MBean Attribute change for " + var2 + " from " + var3 + " to " + var4, var2, "UNKNOWN TYPE", var3, var4);
   }

   public AttributeChangeNotification(ObjectName var1, String var2, String var3, Object var4, Object var5) {
      this(var1, generateSequenceNumber(), (new Date()).getTime(), "WebLogic MBean Attribute change for " + var2 + " from " + var4 + " to " + var5, var2, var3, var4, var5);
   }

   public AttributeChangeNotification(Object var1, String var2, String var3, String var4, Object var5, Object var6) {
      this(var1, generateSequenceNumber(), (new Date()).getTime(), var2, var3, var4, var5, var6);
   }

   protected AttributeChangeNotification(Object var1, long var2, long var4, String var6, String var7, String var8, Object var9, Object var10) {
      super(var1, var2, var4, var6, var7, var8, var9, var10);
   }

   public static long getChangeNotificationCount() {
      return sequenceNumber;
   }

   private static synchronized long generateSequenceNumber() {
      return (long)(sequenceNumber++);
   }

   public String toString() {
      String var1 = this.getOldValue().toString();
      String var2 = this.getNewValue().toString();
      return this.getClass().getName() + ": " + this.getAttributeName() + " from " + var1 + " to " + var2 + " - " + super.toString();
   }
}
