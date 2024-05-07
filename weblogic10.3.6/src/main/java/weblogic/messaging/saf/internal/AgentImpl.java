package weblogic.messaging.saf.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.health.HealthState;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.store.SAFStore;
import weblogic.messaging.saf.store.SAFStoreException;
import weblogic.messaging.saf.utils.Util;
import weblogic.work.WorkManager;

public abstract class AgentImpl implements Agent, Externalizable {
   static final long serialVersionUID = -8897468130390722827L;
   private static final String AGENT_WM_NAME_PREFIX = "weblogic.saf.agent.";
   protected String name;
   protected SAFStore store;
   protected SAFAgentAdmin agentAdmin;
   private int agentType;
   private static final byte EXTVERSION1 = 1;
   protected static final int _SENDINGAGENTTYPE = 1;
   protected static final int _RECEIVINGAGENTTYPE = 2;
   protected static final int STATE_CLOSED = 1;
   protected static final int STATE_STARTING = 2;
   protected static final int STATE_STARTED = 4;
   protected static final int STATE_CLOSING = 8;
   protected static SAFManagerImpl safManager = (SAFManagerImpl)SAFManagerImpl.getManager();
   protected HashMap conversationInfosFromStore = new HashMap();
   protected int state;
   protected int waitersCount;
   protected boolean isPaused;
   private WorkManager workManager;
   protected HealthState healthState = new HealthState(0);
   private static final int _VERSIONMASK = 255;
   private static final int _AGENTTYPESHIFT = 8;
   private static final int _AGENTTYPEMASK = 65280;

   public AgentImpl() {
   }

   AgentImpl(String var1, SAFAgentAdmin var2, SAFStore var3, int var4) throws SAFException {
      this.agentType = var4;
      this.init(var1, var2, var3);
      this.addAgentToStore(var3);
   }

   void init(String var1, SAFAgentAdmin var2, SAFStore var3) throws SAFException {
      this.name = var1;
      this.agentAdmin = var2;
      this.store = var3;
      this.init(var2.getMBean());
   }

   public void init(SAFAgentMBean var1) throws SAFException {
      this.startInitialize(var1);
      this.workManager = Util.findOrCreateWorkManager("weblogic.saf.agent." + var1.getName() + (this.agentType == 1 ? "Sender" : "Receiver"), -1, 1, -1);

      try {
         this.start();
         this.advertise();
      } catch (SAFException var3) {
         this.printDebug(var3.getMessage());
         throw var3;
      }
   }

   protected abstract void addToAgentFactory();

   protected abstract void removeFromAgentFactory();

   protected final void advertise() {
      if (!this.isPaused) {
         this.addToAgentFactory();
      }
   }

   protected final void unadvertise() {
      this.removeFromAgentFactory();
   }

   private void addAgentToStore(SAFStore var1) throws SAFStoreException {
      var1.addAgent(this);
      this.printDebug(" Agent = " + this.name + " has been added to SAFStore " + var1);
   }

   public final String getName() {
      return this.name;
   }

   public final String getStoreName() {
      return this.store.getEffectiveStoreName();
   }

   public final SAFAgentAdmin getAgentAdmin() {
      return this.agentAdmin;
   }

   protected static final SAFManagerImpl getSAFManager() {
      return safManager;
   }

   protected abstract void start() throws SAFException;

   protected abstract void startInitialize(SAFAgentMBean var1) throws SAFException;

   protected void waitForState(int var1) throws SAFException {
      while((this.state & var1) == 0) {
         ++this.waitersCount;

         try {
            this.wait();
         } catch (InterruptedException var7) {
            throw new SAFException(var7);
         } finally {
            --this.waitersCount;
         }
      }

   }

   private void printDebug(String var1) {
      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && this.agentType == 1) {
         SAFDebug.SAFSendingAgent.debug(var1);
      }

      if (SAFDebug.SAFReceivingAgent.isDebugEnabled() && this.agentType == 2) {
         SAFDebug.SAFReceivingAgent.debug(var1);
      }

   }

   public WorkManager getWorkManager() {
      return this.workManager;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 0;
      byte var3 = 1;
      var2 |= var3;
      var2 |= this.agentType << 8;
      var1.writeInt(var2);
      var1.writeUTF(this.name);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      this.agentType = (var2 & '\uff00') >>> 8;
      byte var3 = (byte)(var2 & 255);
      if (var3 != 1) {
         throw new IOException(" unknown version of the object");
      } else {
         this.name = var1.readUTF();
      }
   }

   void storeConversationInfo(SAFConversationInfo var1) throws SAFStoreException {
      this.store.addConversationInfo(var1);
      synchronized(this.conversationInfosFromStore) {
         this.conversationInfosFromStore.put(var1.getConversationName(), var1);
      }
   }

   static HealthState updateHealthState(HealthState var0, int var1, String var2) {
      String[] var3 = new String[]{var2};
      return updateHealthState(var0, var1, var3);
   }

   private static HealthState updateHealthState(HealthState var0, int var1, String[] var2) {
      HealthState var3;
      if (var0.getState() == var1) {
         String[] var4 = var0.getReasonCode();
         String[] var5 = new String[var4.length + 1];

         int var6;
         for(var6 = 0; var6 < var4.length; ++var6) {
            var5[var6] = var4[var6];
         }

         for(var6 = 0; var6 < var2.length; ++var6) {
            var5[var4.length + var6] = var2[var6];
         }

         var3 = new HealthState(var1, var5);
      } else {
         var3 = new HealthState(var1, var2);
      }

      return var3;
   }

   public void dump(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeAttribute("name", this.name);
      String var3 = this.store.getStoreName();
      var2.writeAttribute("storeName", var3 != null ? var3 : "");
      var2.writeAttribute("conversationsCurrentCount", String.valueOf(this.agentAdmin.getConversationsCurrentCount()));
      var2.writeAttribute("conversationsHighCount", String.valueOf(this.agentAdmin.getConversationsHighCount()));
      var2.writeAttribute("conversationsTotalCount", String.valueOf(this.agentAdmin.getConversationsTotalCount()));
   }
}
