package weblogic.jms.adapter;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.IllegalStateException;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.resource.spi.SecurityException;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import weblogic.jms.bridge.AdapterConnection;
import weblogic.jms.bridge.AdapterConnectionMetaData;

public class JMSManagedConnection implements ManagedConnection {
   private AdapterConnection con;
   private JMSConnectionRequestInfo connRequestInfo;
   private String user;
   private EventListenerManager listenerMgr;
   private ManagedConnectionFactory mcf;
   private transient PrintWriter logWriter;
   private boolean supportsXA;
   private boolean supportsLocalTx;
   private boolean destroyed;
   private Set connectionSet;
   private AdapterConnectionMetaData metaData;
   private ManagedConnectionMetaData conMetaData;

   JMSManagedConnection(ManagedConnectionFactory var1, String var2, AdapterConnection var3, JMSConnectionRequestInfo var4, boolean var5, boolean var6) {
      this.mcf = var1;
      this.user = var2;
      this.con = var3;
      this.connRequestInfo = var4;
      this.supportsXA = var5;
      this.supportsLocalTx = var6;
      this.connectionSet = new HashSet();
      this.listenerMgr = new EventListenerManager(this);

      try {
         this.metaData = ((JMSBaseConnection)var3).getMetaData();
      } catch (ResourceException var8) {
      }

      this.conMetaData = new ConnectionMetaDataImpl(this);
   }

   public Object getConnection(Subject var1, ConnectionRequestInfo var2) throws ResourceException {
      PasswordCredential var3 = Util.getPasswordCredential(this.mcf, var1, var2);
      if (var3 == null) {
         if (this.user != null) {
            throw new SecurityException("Principal does not match. Reauthentication not supported");
         }
      } else if (!var3.getUserName().equals(this.user)) {
         throw new SecurityException("Principal does not match. Reauthentication not supported");
      }

      synchronized(this) {
         this.checkIfDestroyed();
         JMSConnectionHandle var5 = new JMSConnectionHandle(this);
         this.addJMSConnectionHandle(var5);
         return var5;
      }
   }

   public void destroy() throws ResourceException {
      Iterator var1 = null;
      synchronized(this) {
         if (this.destroyed) {
            return;
         }

         var1 = this.connectionSet.iterator();
      }

      this.con.close();

      while(var1.hasNext()) {
         JMSConnectionHandle var2 = (JMSConnectionHandle)var1.next();
         var2.invalidate();
      }

      synchronized(this) {
         this.connectionSet.clear();
         this.destroyed = true;
      }
   }

   public void cleanup() throws ResourceException {
      Iterator var1 = null;
      synchronized(this) {
         if (this.isDestroyed()) {
            return;
         }

         var1 = this.connectionSet.iterator();
      }

      try {
         if (((JMSManagedConnectionFactory)this.mcf).isWLSAdapter()) {
            this.con.close();
         } else {
            ((JMSBaseConnection)this.con).cleanup();
         }
      } catch (Exception var6) {
      }

      while(var1.hasNext()) {
         JMSConnectionHandle var2 = (JMSConnectionHandle)var1.next();
         var2.invalidate();
      }

      synchronized(this) {
         this.connectionSet.clear();
      }
   }

   public void associateConnection(Object var1) throws ResourceException {
      this.checkIfDestroyed();
      if (var1 instanceof JMSConnectionHandle) {
         JMSConnectionHandle var2 = (JMSConnectionHandle)var1;
         var2.associateConnection(this);
      } else {
         throw new IllegalStateException("Invalid connection object: " + var1);
      }
   }

   public void addConnectionEventListener(ConnectionEventListener var1) {
      this.listenerMgr.addConnectorListener(var1);
   }

   public void removeConnectionEventListener(ConnectionEventListener var1) {
      this.listenerMgr.removeConnectorListener(var1);
   }

   public XAResource getXAResource() throws ResourceException {
      if (!this.supportsXA) {
         throw new NotSupportedException("XA transaction not supported");
      } else {
         this.checkIfDestroyed();
         return this.con.getXAResource();
      }
   }

   public synchronized LocalTransaction getLocalTransaction() throws ResourceException {
      if (!this.supportsLocalTx) {
         throw new NotSupportedException("Local transaction not supported");
      } else {
         this.checkIfDestroyed();
         return new SpiLocalTransactionImpl(this);
      }
   }

   public ManagedConnectionMetaData getMetaData() throws ResourceException {
      this.checkIfDestroyed();
      return this.conMetaData;
   }

   public synchronized void setLogWriter(PrintWriter var1) throws ResourceException {
      this.checkIfDestroyed();
      this.logWriter = var1;
   }

   public synchronized PrintWriter getLogWriter() throws ResourceException {
      this.checkIfDestroyed();
      return this.logWriter;
   }

   AdapterConnection getJMSBaseConnection() throws ResourceException {
      this.checkIfDestroyed();
      return this.con;
   }

   synchronized boolean isDestroyed() {
      return this.destroyed;
   }

   String getUserName() throws ResourceException {
      this.checkIfDestroyed();
      return this.user;
   }

   void sendEvent(int var1, Exception var2) throws ResourceException {
      if (var1 != 1) {
         synchronized(this) {
            if (this.destroyed) {
               return;
            }
         }
      }

      this.listenerMgr.sendEvent(var1, var2, (Object)null);
   }

   void sendEvent(int var1, Exception var2, Object var3) throws ResourceException {
      if (var1 != 1) {
         synchronized(this) {
            if (this.destroyed) {
               return;
            }
         }
      }

      this.listenerMgr.sendEvent(var1, var2, var3);
   }

   synchronized void removeJMSConnectionHandle(JMSConnectionHandle var1) throws ResourceException {
      if (!this.destroyed) {
         this.connectionSet.remove(var1);
      }
   }

   synchronized void addJMSConnectionHandle(JMSConnectionHandle var1) throws ResourceException {
      this.checkIfDestroyed();
      this.connectionSet.add(var1);
   }

   private synchronized void checkIfDestroyed() throws ResourceException {
      if (this.isDestroyed()) {
         throw new IllegalStateException("Managed connection is destroyed");
      }
   }

   ManagedConnectionFactory getManagedConnectionFactory() {
      return this.mcf;
   }

   JMSConnectionRequestInfo getConnectionRequestInfo() {
      return this.connRequestInfo;
   }

   synchronized int getMaxConnections() throws ResourceException {
      this.checkIfDestroyed();
      return this.connectionSet.size();
   }
}
