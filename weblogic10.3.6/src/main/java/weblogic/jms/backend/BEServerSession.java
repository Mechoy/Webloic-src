package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.ServerSession;
import javax.jms.Session;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSUtilities;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.work.WorkAdapter;

public final class BEServerSession extends WorkAdapter implements ServerSession, Externalizable {
   static final long serialVersionUID = 7855664711553219989L;
   private static final byte EXTVERSION = 1;
   private Connection connection;
   private Session session;
   private BEServerSessionPool serverSessionPool;
   private BEServerSession next;

   BEServerSession(Connection var1, Session var2, BEServerSessionPool var3) {
      this.connection = var1;
      this.session = var2;
      this.serverSessionPool = var3;
   }

   public BEServerSession() {
   }

   public BEServerSession getNext() {
      return this.next;
   }

   public void setNext(BEServerSession var1) {
      this.next = var1;
   }

   void close() throws JMSException {
      this.session.close();
   }

   void goBackInPool() {
      this.serverSessionPool.serverSessionPut(this);
   }

   public Session getSession() throws JMSException {
      return this.session;
   }

   public void start() {
      this.serverSessionPool.getBackEnd().getWorkManager().schedule(this);
   }

   public synchronized void run() {
      try {
         if (this.session != null) {
            this.session.run();
         } else if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Error running session for connection consumer");
         }
      } catch (Exception var6) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Error running session for connection consumer" + var6.toString());
         }
      } finally {
         this.goBackInPool();
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      var1.writeObject(this.serverSessionPool);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         this.serverSessionPool = (BEServerSessionPool)PortableRemoteObject.narrow(var1.readObject(), BEServerSessionPool.class);
      }
   }
}
