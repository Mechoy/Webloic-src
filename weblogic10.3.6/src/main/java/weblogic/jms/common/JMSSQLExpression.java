package weblogic.jms.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.messaging.common.SQLExpression;

public final class JMSSQLExpression extends SQLExpression {
   public static final long serialVersionUID = 1287505877942351248L;
   private static final int EXTERNAL_VERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int HAS_SELECTOR_FLAG = 256;
   private static final int HAS_ID_FLAG = 512;
   private static final int NO_LOCAL_FLAG = 1024;
   private static final int NOT_FORWARDED_FLAG = 2048;
   private static final int HAS_CLIENT_ID_FLAG = 4096;
   private static final int HAS_CLIENT_ID_POLICY_FLAG = 8192;
   private boolean noLocal;
   private boolean notForwarded;
   private JMSID connectionId;
   private String clientId;
   private int clientIdPolicy = 0;

   public JMSSQLExpression() {
   }

   public JMSSQLExpression(String var1, boolean var2, JMSID var3, boolean var4) {
      super(var1);
      this.setNoLocal(var2);
      this.setConnectionId(var3);
      this.setNotForwarded(var4);
   }

   public JMSSQLExpression(String var1, boolean var2, JMSID var3, String var4, int var5) {
      super(var1);
      this.setNoLocal(var2);
      this.setConnectionId(var3);
      this.setNotForwarded(false);
      this.setClientId(var4);
      this.setClientIdPolicy(var5);
   }

   public JMSSQLExpression(String var1) {
      super(var1);
      this.setNoLocal(false);
      this.setConnectionId((JMSID)null);
      this.setNotForwarded(false);
   }

   public boolean isNull() {
      return this.selector == null && !this.notForwarded && (!this.noLocal || this.connectionId == null);
   }

   public boolean isNoLocal() {
      return this.noLocal;
   }

   public void setNoLocal(boolean var1) {
      this.noLocal = var1;
   }

   public JMSID getConnectionId() {
      return this.connectionId;
   }

   public void setConnectionId(JMSID var1) {
      this.connectionId = var1;
   }

   public void setClientId(String var1) {
      this.clientId = var1;
   }

   public String getClientId() {
      return this.clientId;
   }

   public int getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   public void setClientIdPolicy(int var1) {
      this.clientIdPolicy = var1;
   }

   public boolean isNotForwarded() {
      return this.notForwarded;
   }

   public void setNotForwarded(boolean var1) {
      this.notForwarded = var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.selector != null) {
         var2 |= 256;
      }

      if (this.connectionId != null) {
         var2 |= 512;
      }

      if (this.clientId != null) {
         var2 |= 4096;
      }

      if (this.clientIdPolicy != 0) {
         var2 |= 8192;
      }

      if (this.noLocal) {
         var2 |= 1024;
      }

      if (this.notForwarded) {
         var2 |= 2048;
      }

      var1.writeInt(var2);
      if (this.selector != null) {
         var1.writeUTF(this.selector);
      }

      if (this.connectionId != null) {
         this.connectionId.writeExternal(var1);
      }

      if (this.clientId != null) {
         var1.writeUTF(this.clientId);
      }

      if ((var2 & 8192) != 0) {
         var1.writeInt(this.clientIdPolicy);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      if ((var2 & 255) != 1) {
         throw new IOException("External version mismatch");
      } else {
         this.noLocal = (var2 & 1024) != 0;
         this.notForwarded = (var2 & 2048) != 0;
         if ((var2 & 256) != 0) {
            this.selector = var1.readUTF();
         }

         if ((var2 & 512) != 0) {
            this.connectionId = new JMSID();
            this.connectionId.readExternal(var1);
         }

         if ((var2 & 4096) != 0) {
            this.clientId = var1.readUTF();
         }

         if ((var2 & 8192) != 0) {
            this.clientIdPolicy = var1.readInt();
         }

      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[ ");
      if (this.selector != null) {
         var1.append("selector=\"");
         var1.append(this.selector);
         var1.append("\" ");
      }

      if (this.connectionId != null) {
         var1.append("connectionID=\"");
         var1.append(this.connectionId);
         var1.append("\" ");
      }

      if (this.clientId != null) {
         var1.append("clientId=\"");
         var1.append(this.clientId);
         var1.append("\" ");
      }

      var1.append("clientIdPoliy=\"");
      var1.append(this.clientIdPolicy);
      var1.append("\" ");
      if (this.noLocal) {
         var1.append("noLocal=true ");
      }

      if (this.notForwarded) {
         var1.append("notForwarded=true ");
      }

      var1.append(']');
      return var1.toString();
   }
}
