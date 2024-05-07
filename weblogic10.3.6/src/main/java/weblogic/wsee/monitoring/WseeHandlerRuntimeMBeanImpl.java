package weblogic.wsee.monitoring;

import java.util.Date;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeHandlerRuntimeMBean;
import weblogic.wsee.util.Verbose;

public final class WseeHandlerRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeHandlerRuntimeMBean, WseeHandlerRuntimeData> implements WseeHandlerRuntimeMBean, HandlerStats {
   private static final boolean verbose = Verbose.isVerbose(WseeHandlerRuntimeMBeanImpl.class);

   public WseeHandlerRuntimeMBeanImpl() throws ManagementException {
      super((String)null, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeHandlerRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   WseeHandlerRuntimeMBeanImpl(String var1, Class var2, QName[] var3) throws ManagementException {
      super(var1, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
      WseeHandlerRuntimeData var4 = new WseeHandlerRuntimeData(var1, var2, var3);
      this.setData(var4);
      if (verbose) {
         Verbose.log((Object)("WseeHandlerRuntimeMbeanImpl[" + var1 + "]"));
      }

   }

   protected WseeHandlerRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeHandlerRuntimeMBeanImpl var3 = new WseeHandlerRuntimeMBeanImpl(var1, var2, this);
      return var3;
   }

   public Class getHandlerClass() {
      return ((WseeHandlerRuntimeData)this.getData()).getHandlerClass();
   }

   public QName[] getHeaders() {
      return ((WseeHandlerRuntimeData)this.getData()).getHeaders();
   }

   public int getRequestSOAPFaultsCount() {
      return ((WseeHandlerRuntimeData)this.getData()).getResponseSOAPFaultsCount();
   }

   public int getRequestTerminationsCount() {
      return ((WseeHandlerRuntimeData)this.getData()).getRequestTerminationsCount();
   }

   public int getRequestErrorsCount() {
      return ((WseeHandlerRuntimeData)this.getData()).getRequestErrorsCount();
   }

   public int getResponseSOAPFaultsCount() {
      return ((WseeHandlerRuntimeData)this.getData()).getResponseSOAPFaultsCount();
   }

   public int getResponseTerminationsCount() {
      return ((WseeHandlerRuntimeData)this.getData()).getResponseTerminationsCount();
   }

   public int getResponseErrorsCount() {
      return ((WseeHandlerRuntimeData)this.getData()).getResponseErrorsCount();
   }

   public boolean isInternal() {
      return ((WseeHandlerRuntimeData)this.getData()).isInternal();
   }

   public void reportRequestSOAPFault(SOAPFaultException var1) {
      ((WseeHandlerRuntimeData)this.getData()).reportRequestSOAPFault(var1);
   }

   public void reportRequestTermination() {
      ((WseeHandlerRuntimeData)this.getData()).reportRequestTermination();
   }

   public void reportRequestError(Throwable var1) {
      ((WseeHandlerRuntimeData)this.getData()).reportRequestError(var1);
   }

   public void reportResponseSOAPFault(SOAPFaultException var1) {
      ((WseeHandlerRuntimeData)this.getData()).reportResponseSOAPFault(var1);
   }

   public void reportResponseTermination() {
      ((WseeHandlerRuntimeData)this.getData()).reportResponseTermination();
   }

   public void reportResponseError(Throwable var1) {
      ((WseeHandlerRuntimeData)this.getData()).reportResponseError(var1);
   }

   public void reportInitError(Throwable var1) {
      ((WseeHandlerRuntimeData)this.getData()).reportInitError(var1);
   }

   public void reset() {
      ((WseeHandlerRuntimeData)this.getData()).reset();
   }

   public Date getLastResetTime() {
      return ((WseeHandlerRuntimeData)this.getData()).getLastResetTime();
   }
}
