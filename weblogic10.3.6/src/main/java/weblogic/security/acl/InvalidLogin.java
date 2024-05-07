package weblogic.security.acl;

import java.util.Vector;

/** @deprecated */
public final class InvalidLogin {
   private String user_name;
   private long locked_timestamp;
   private Vector failure_records;

   public InvalidLogin(String var1) {
      this.user_name = var1;
      this.locked_timestamp = 0L;
      this.failure_records = new Vector();
   }

   public InvalidLogin() {
      this.user_name = null;
      this.locked_timestamp = 0L;
      this.failure_records = new Vector();
   }

   String getName() {
      return this.user_name;
   }

   void setName(String var1) {
      this.erase();
      this.user_name = var1;
   }

   int getFailureCount() {
      return this.failure_records.size();
   }

   long getLockedTimestamp() {
      return this.locked_timestamp;
   }

   void setLockedTimestamp(long var1) {
      this.locked_timestamp = var1;
   }

   Vector getFailures() {
      return this.failure_records;
   }

   void erase() {
      this.user_name = null;
      this.locked_timestamp = 0L;
      this.failure_records.removeAllElements();
   }

   void addFailure(Object var1) {
      this.failure_records.addElement(var1);
   }

   public String toString() {
      return this.user_name + " " + this.locked_timestamp + " " + this.failure_records.size();
   }

   public Object getLatestFailure() {
      return this.failure_records.lastElement();
   }
}
