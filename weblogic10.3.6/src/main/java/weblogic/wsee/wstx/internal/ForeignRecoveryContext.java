package weblogic.wsee.wstx.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.transaction.Transaction;
import javax.transaction.xa.Xid;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.EndpointReference;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;
import weblogic.wsee.wstx.wsat.Transactional;

public class ForeignRecoveryContext implements Externalizable {
   private static final long serialVersionUID = -3257083889097518770L;
   private static int klassVersion = 1032;
   private Xid fxid;
   private Transactional.Version version;
   private EndpointReference epr;

   public ForeignRecoveryContext() {
   }

   ForeignRecoveryContext(Xid var1) {
      this.fxid = var1;
   }

   public void setEndpointReference(EndpointReference var1, Transactional.Version var2) {
      this.epr = var1;
      this.version = var2;
   }

   public Xid getXid() {
      return this.fxid;
   }

   public EndpointReference getEndpointReference() {
      return this.epr;
   }

   Transactional.Version getVersion() {
      return this.version;
   }

   Transaction getTransaction() {
      if (this.fxid == null) {
         throw new AssertionError("No Tid to Xid mapping for " + this);
      } else {
         return ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).getTransaction(this.fxid);
      }
   }

   public void readExternal(ObjectInput var1) throws ClassNotFoundException, IOException {
      klassVersion = var1.readInt();
      this.fxid = (Xid)var1.readObject();
      this.debug("ForeignRecoveryContext.readExternal tid:" + this.fxid);
      this.version = (Transactional.Version)var1.readObject();
      int var2 = var1.readInt();
      byte[] var3 = new byte[var2];
      var1.readFully(var3);
      this.epr = EndpointReference.readFrom(new StreamSource(new ByteArrayInputStream(var3)));
      this.debug("ForeignRecoveryContext.readExternal EndpointReference:" + this.epr);
      ForeignRecoveryContextManager.getInstance().add(this);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(klassVersion);
      var1.writeObject(this.fxid);
      var1.writeObject(this.version);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      this.epr.writeTo(new StreamResult(var2));
      byte[] var3 = var2.toByteArray();
      var1.writeInt(var3.length);
      var1.write(var3);
   }

   public String toString() {
      return "ForeignRecoveryContext[tid=" + this.fxid + ", endPointreference=" + this.epr + ", version = " + this.version + "]";
   }

   private void debug(String var1) {
      if (ForeignRecoveryContextManager.getInstance().debugWSAT.isDebugEnabled()) {
         ForeignRecoveryContextManager.getInstance().debugWSAT.debug(this + " " + var1);
      }

   }
}
