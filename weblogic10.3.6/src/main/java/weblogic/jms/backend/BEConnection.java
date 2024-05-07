package weblogic.jms.backend;

import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPeerGoneListener;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;

public final class BEConnection implements Invocable, JMSPeerGoneListener {
   private final HashMap connectionConsumers = new HashMap();
   private final HashMap sessions = new HashMap();
   private final HashMap tempDestinations = new HashMap();
   private long startStopSequenceNumber;
   private final JMSID connectionId;
   private JMSDispatcher feDispatcher;
   private final InvocableMonitor invocableMonitor;
   private boolean stopped = true;
   private int state = 0;
   private String connectionAddress = null;
   private transient int refCount;

   BEConnection(JMSDispatcher var1, JMSID var2, boolean var3, String var4) {
      this.feDispatcher = var1;
      this.connectionId = var2;
      this.stopped = var3;
      this.connectionAddress = var4;
      this.feDispatcher.addDispatcherPeerGoneListener(this);
      this.invocableMonitor = JMSService.getJMSService().getInvocableMonitor();
   }

   public synchronized long getStartStopSequenceNumber() {
      return this.startStopSequenceNumber;
   }

   public void setStartStopSequenceNumber(long var1) {
      this.startStopSequenceNumber = var1;
   }

   void setDispatcher(JMSDispatcher var1) {
      this.feDispatcher.removeDispatcherPeerGoneListener(this);
      this.feDispatcher = var1;
      var1.addDispatcherPeerGoneListener(this);
   }

   public JMSDispatcher getDispatcher() {
      return this.feDispatcher;
   }

   public String getAddress() {
      return this.connectionAddress;
   }

   public synchronized void tempDestinationAdd(BEDestinationImpl var1) throws JMSException {
      this.checkShutdownOrSuspended("create temporary destiantion");
      JMSID var2 = var1.getJMSID();
      if (this.tempDestinations.get(var2) == null) {
         this.tempDestinations.put(var2, var1);
      } else {
         throw new weblogic.jms.common.JMSException("Temporary destination exists, " + var2);
      }
   }

   public synchronized void tempDestinationRemove(JMSID var1) throws JMSException {
      BEDestinationImpl var2 = (BEDestinationImpl)this.tempDestinations.remove(var1);
      if (var2 == null) {
         throw new weblogic.jms.common.JMSException("Temporary destination not found, " + var1);
      } else {
         if (this.needToClose()) {
            this.close();
         }

      }
   }

   public synchronized void sessionAdd(BESession var1) throws JMSException {
      this.checkShutdownOrSuspended("create session");
      this.sessions.put(var1.getJMSID(), var1);
      InvocableManagerDelegate.delegate.invocableAdd(16, var1);
   }

   public synchronized void sessionRemove(JMSID var1) {
      this.sessions.remove(var1);
      InvocableManagerDelegate.delegate.invocableRemove(16, var1);
      if (this.needToClose()) {
         this.close();
      }

   }

   public synchronized void connectionConsumerAdd(BEConnectionConsumerCommon var1) throws JMSException {
      this.checkShutdownOrSuspended("create connection consumer");
      this.connectionConsumers.put(var1.getJMSID(), var1);
      InvocableManagerDelegate.delegate.invocableAdd(17, var1);
   }

   private void connectionConsumerClose(BEConnectionConsumerCloseRequest var1) throws JMSException {
      JMSID var2 = var1.getConnectionConsumerId();
      BEConnectionConsumerCommon var3 = (BEConnectionConsumerCommon)InvocableManagerDelegate.delegate.invocableFind(17, var2);
      var3.close();
      this.connectionConsumerRemove(var2);
   }

   private synchronized void connectionConsumerRemove(JMSID var1) {
      this.connectionConsumers.remove(var1);
      InvocableManagerDelegate.delegate.invocableRemove(17, var1);
      if (this.needToClose()) {
         this.close();
      }

   }

   private synchronized boolean needToClose() {
      return this.sessions.isEmpty() && this.tempDestinations.isEmpty() && this.connectionConsumers.isEmpty();
   }

   synchronized boolean isStopped() {
      return this.stopped;
   }

   synchronized void stop(long var1, boolean var3) {
      if (this.startStopSequenceNumber < var1) {
         this.startStopSequenceNumber = var1;
         if (!this.stopped) {
            this.stopped = true;
            if (var3) {
               this.state = 2;
            }

            Iterator var4 = this.sessions.values().iterator();

            while(var4.hasNext()) {
               BESession var5 = (BESession)var4.next();
               var5.stop();
            }

            var4 = this.connectionConsumers.values().iterator();

            while(var4.hasNext()) {
               BEConnectionConsumerCommon var6 = (BEConnectionConsumerCommon)var4.next();
               var6.stop();
            }

         }
      }
   }

   private void checkShutdownOrSuspended(String var1) throws JMSException {
      if ((this.state & 27) != 0) {
         throw new weblogic.jms.common.JMSException("Failed to " + var1 + " because JMS server shutdown or suspended");
      }
   }

   synchronized void checkShutdownOrSuspendedNeedLock(String var1) throws JMSException {
      if ((this.state & 27) != 0) {
         throw new weblogic.jms.common.JMSException("Failed to " + var1 + " bacause JMS server shutdown or suspended");
      }
   }

   public int incrementRefCount() {
      return ++this.refCount;
   }

   public int decrementRefCount() {
      return --this.refCount;
   }

   public void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("BEConnection.jmsPeerGone()");
      }

      this.peerGone();
   }

   private synchronized void peerGone() {
      JMSException var2 = null;
      InvocableManagerDelegate.delegate.invocableRemove(15, this.connectionId);
      this.feDispatcher.removeDispatcherPeerGoneListener(this);
      Iterator var1 = ((HashMap)this.sessions.clone()).values().iterator();

      while(var1.hasNext()) {
         try {
            ((BESession)var1.next()).peerGone();
         } catch (JMSException var7) {
            if (var2 == null) {
               var2 = var7;
            }
         }
      }

      var1 = ((HashMap)this.tempDestinations.clone()).values().iterator();

      while(var1.hasNext()) {
         BEDestinationImpl var3 = (BEDestinationImpl)var1.next();

         try {
            var3.deleteTempDestination();
            var3.getBackEnd().removeDestination(var3);
         } catch (JMSException var6) {
            if (var2 == null) {
               var2 = var6;
            }
         }
      }

      var1 = ((HashMap)this.connectionConsumers.clone()).values().iterator();

      while(var1.hasNext()) {
         try {
            BEConnectionConsumerCommon var8 = (BEConnectionConsumerCommon)var1.next();
            var8.close();
            this.connectionConsumerRemove(var8.getJMSID());
         } catch (JMSException var5) {
            if (var2 == null) {
               var2 = var5;
            }
         }
      }

      if (var2 != null) {
         JMSLogger.logJMSServerShutdownError(this.getDispatcher().getId().getName(), var2.getMessage(), var2);
      }

   }

   private synchronized void close() {
      if (this.needToClose()) {
         InvocableManagerDelegate.delegate.invocableRemove(15, this.connectionId);
         this.feDispatcher.removeDispatcherPeerGoneListener(this);
      }
   }

   synchronized void start(long var1) throws JMSException {
      this.checkShutdownOrSuspended("start connection");
      if (this.startStopSequenceNumber < var1) {
         this.startStopSequenceNumber = var1;
         if (this.stopped) {
            this.stopped = false;
            Iterator var3 = this.sessions.values().iterator();

            while(var3.hasNext()) {
               BESession var4 = (BESession)var3.next();
               var4.start();
            }

            var3 = this.connectionConsumers.values().iterator();

            while(var3.hasNext()) {
               BEConnectionConsumerCommon var5 = (BEConnectionConsumerCommon)var3.next();
               var5.start();
            }

         }
      }
   }

   public JMSID getJMSID() {
      return this.connectionId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 8975:
            this.connectionConsumerClose((BEConnectionConsumerCloseRequest)var1);
            break;
         case 9487:
            this.start(((BEConnectionStartRequest)var1).getStartStopSequenceNumber());
            break;
         case 9743:
            this.stop(((BEConnectionStopRequest)var1).getStartStopSequenceNumber(), ((BEConnectionStopRequest)var1).isStopForSuspend());
            break;
         default:
            throw new weblogic.jms.common.JMSException("No such method " + var1.getMethodId());
      }

      var1.setResult(new VoidResponse());
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Connection");
      var2.writeAttribute("id", this.connectionId != null ? this.connectionId.toString() : "");
      var2.writeAttribute("state", JMSService.getStateName(this.state));
      var2.writeAttribute("connectionAddress", this.connectionAddress != null ? this.connectionAddress : "");
      var2.writeStartElement("Sessions");
      HashMap var3 = (HashMap)this.sessions.clone();
      var2.writeAttribute("currentCount", String.valueOf(var3.size()));
      Iterator var4 = var3.values().iterator();

      while(var4.hasNext()) {
         BESession var5 = (BESession)var4.next();
         var5.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }
}
