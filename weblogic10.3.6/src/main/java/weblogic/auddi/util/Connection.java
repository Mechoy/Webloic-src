package weblogic.auddi.util;

public abstract class Connection {
   private Object m_connectionObject;
   private String m_connectionId;

   public Connection(Object var1, String var2) {
      this.m_connectionObject = var1;
      this.m_connectionId = var2;
   }

   public String getConnectionId() {
      return this.m_connectionId;
   }

   public Object getConnectionObject() {
      return this.m_connectionObject;
   }

   public abstract void close() throws ConnectException;

   public String getID() {
      return this.m_connectionId;
   }
}
