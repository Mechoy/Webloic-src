package weblogic.jms.client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.JMSException;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicSession;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPeerGoneListener;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.PeerVersionable;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.Invocable;

public final class JMSXAConnection extends JMSConnection implements XAConnectionInternal, JMSPeerGoneListener, Externalizable, Invocable, Reconnectable, Cloneable {
   static final long serialVersionUID = -4665036665162468456L;
   private static final byte EXTVERSION = 1;

   public JMSXAConnection(JMSID var1, String var2, int var3, int var4, int var5, int var6, long var7, long var9, long var11, long var13, long var15, boolean var17, boolean var18, int var19, int var20, int var21, boolean var22, DispatcherWrapper var23, boolean var24, int var25, int var26, int var27, int var28, String var29, PeerVersionable var30, String var31, String var32, PeerInfo var33, int var34, int var35, int var36, int var37, int var38, long var39, long var41) {
      super(var1, var2, var3, var4, var5, var6, var7, var9, var11, var13, var15, true, var18, var19, var20, var21, var22, var23, var24, var25, var26, var27, var28, false, var29, var30, var31, var32, var33, var34, var35, var36, var37, var38, var39, var41);
   }

   public XAQueueSession createXAQueueSession() throws JMSException {
      SessionInternal var1 = this.createSessionInternal(false, 2, true, 2);
      return (XAQueueSession)var1;
   }

   public XATopicSession createXATopicSession() throws JMSException {
      SessionInternal var1 = this.createSessionInternal(false, 2, true, 1);
      return (XATopicSession)var1;
   }

   public XASession createXASession() throws JMSException {
      return (XASession)this.createSessionInternal(false, 2, true, 0);
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public JMSXAConnection() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeByte(1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      }
   }
}
