package weblogic.wsee.monitoring;

import java.util.Date;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.management.ManagementException;
import weblogic.wsee.util.Verbose;

public final class WseeHandlerRuntimeData extends WseeBaseRuntimeData {
   private static final boolean verbose = Verbose.isVerbose(WseeHandlerRuntimeData.class);
   private QName[] headers;
   private Class handlerClass = null;
   private boolean isInternal;
   private Throwable initError = null;
   private Throwable lastRequestError = null;
   private Throwable lastResponseError = null;
   private SOAPFaultException lastRequestSoapFault = null;
   private SOAPFaultException lastResponseSoapFault = null;
   private int requestSoapFaultsCount = 0;
   private int requestTerminationsCount = 0;
   private int requestErrorsCount = 0;
   private int responseSoapFaultsCount = 0;
   private int responseTerminationsCount = 0;
   private int responseErrorsCount = 0;
   private Date lastResetTime = null;

   WseeHandlerRuntimeData(String var1, Class var2, QName[] var3) throws ManagementException {
      super(var1, (WseeBaseRuntimeData)null);
      this.handlerClass = var2;
      this.headers = var3;
      this.isInternal = var2.getName().startsWith("weblogic.wsee");
      if (verbose) {
         Verbose.log((Object)("WseeHandlerRuntimeData[" + var1 + "]"));
      }

   }

   public Class getHandlerClass() {
      return this.handlerClass;
   }

   public QName[] getHeaders() {
      return this.headers;
   }

   public int getRequestSOAPFaultsCount() {
      return this.requestSoapFaultsCount;
   }

   public int getRequestTerminationsCount() {
      return this.requestTerminationsCount;
   }

   public int getRequestErrorsCount() {
      return this.requestErrorsCount;
   }

   public int getResponseSOAPFaultsCount() {
      return this.responseSoapFaultsCount;
   }

   public int getResponseTerminationsCount() {
      return this.responseTerminationsCount;
   }

   public int getResponseErrorsCount() {
      return this.responseErrorsCount;
   }

   public boolean isInternal() {
      return this.isInternal;
   }

   public void reportRequestSOAPFault(SOAPFaultException var1) {
      synchronized(this) {
         ++this.requestSoapFaultsCount;
         this.lastRequestSoapFault = var1;
      }
   }

   public void reportRequestTermination() {
      synchronized(this) {
         ++this.requestTerminationsCount;
      }
   }

   public void reportRequestError(Throwable var1) {
      synchronized(this) {
         ++this.requestErrorsCount;
         this.lastRequestError = var1;
      }
   }

   public void reportResponseSOAPFault(SOAPFaultException var1) {
      synchronized(this) {
         ++this.responseSoapFaultsCount;
         this.lastResponseSoapFault = var1;
      }
   }

   public void reportResponseTermination() {
      synchronized(this) {
         ++this.responseTerminationsCount;
      }
   }

   public void reportResponseError(Throwable var1) {
      synchronized(this) {
         ++this.responseErrorsCount;
         this.lastResponseError = var1;
      }
   }

   public void reportInitError(Throwable var1) {
      this.initError = var1;
   }

   public void reset() {
      synchronized(this) {
         this.lastResetTime = new Date();
         this.requestSoapFaultsCount = 0;
         this.requestTerminationsCount = 0;
         this.requestErrorsCount = 0;
         this.responseTerminationsCount = 0;
         this.responseErrorsCount = 0;
         this.lastRequestSoapFault = null;
         this.lastResponseSoapFault = null;
         this.lastRequestError = null;
         this.lastResponseError = null;
      }
   }

   public Date getLastResetTime() {
      return this.lastResetTime;
   }
}
