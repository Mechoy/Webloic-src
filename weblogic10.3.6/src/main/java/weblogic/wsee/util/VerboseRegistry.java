package weblogic.wsee.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VerboseRegistry {
   private static final VerboseRegistry _instance = new VerboseRegistry();
   private HashMap<String, String[]> _subcomponents = new HashMap();
   private ReentrantReadWriteLock _subcomponentsLock = new ReentrantReadWriteLock(false);

   public static VerboseRegistry getInstance() {
      return _instance;
   }

   private VerboseRegistry() {
      this.addSubcomponent("soapRouting", new String[]{"weblogic.wsee.jaxws.cluster.*", "weblogic.wsee.mc.cluster.*", "weblogic.wsee.reliability2.tube.SequenceIDRoutingInfoFinder", "weblogic.wsee.wstx.wsat.cluster.*"});
      this.addSubcomponent("soapJaxRpc", new String[]{"weblogic.wsee.connection.soap.*", "!weblogic.wsee.connection.soap.SoapConnectionMessage*"});
      this.addSubcomponent("rmJaxRpc", new String[]{"weblogic.wsee.reliability.WsrmSAFManager", "weblogic.wsee.reliability.WsrmSAFSendingManager", "weblogic.wsee.reliability.WsrmSAFReceivingManager", "weblogic.wsee.reliability.WsrmSequenceSender", "weblogic.wsee.server.jms.WsDispatchMessageListener", "weblogic.wsee.reliability.WsrmSAFEndpoint", "weblogic.wsee.jws.container.ServerResponsePathDispatcher", "weblogic.wsee.async.AbstractAsyncResponseBean", "weblogic.wsee.async.AsyncResponseHandler", "weblogic.wsee.ws.dispatch.server.ServerDispatcher", "weblogic.wsee.handler.HandlerIteratorHistory", "weblogic.wsee.async.AsyncInvokeState", "weblogic.wsee.buffer.BufferManager"});
      this.addSubcomponent("runtime", new String[]{"weblogic.wsee.runtime.*", "weblogic.wsee.config.*", "weblogic.wsee.monitoring.*"});
      this.addSubcomponent("security", new String[]{"weblogic.wsee.security.wssc.*"});
      this.addSubcomponent("jaxWsRuntime", new String[]{"weblogic.wsee.jaxws.*", "com.oracle.xml.ws.client.async.*", "com.sun.xml.ws.client.*", "com.sun.xml.ws.server.WSEndpointImpl", "com.sun.xml.ws.api.pipe.Fiber"});
      this.addSubcomponent("soapJaxWs", new String[]{"com.sun.xml.ws.transport.http.client.HttpTransportPipe", "com.sun.xml.ws.transport.http.HttpAdapter"});
      this.addSubcomponent("persistence", new String[]{"weblogic.wsee.persistence.*"});
      this.addSubcomponent("sender", new String[]{"weblogic.wsee.sender.*"});
      this.addSubcomponent("bufferingJaxWs", new String[]{"weblogic.wsee.buffer2.*"});
      this.addSubcomponent("rmJaxWs", this.getSubcomponent("jaxWsRuntime", true), this.getSubcomponent("runtime", true), this.getSubcomponent("persistence", true), this.getSubcomponent("sender", true), this.getSubcomponent("bufferingJaxWs", true), new String[]{"weblogic.wsee.reliability2.*"});
      this.addSubcomponent("mc", this.getSubcomponent("jaxWsRuntime", true), this.getSubcomponent("runtime", true), this.getSubcomponent("persistence", true), new String[]{"weblogic.wsee.mc.*"});
      this.addSubcomponent("rsp", this.getSubcomponent("rmJaxWs", true), this.getSubcomponent("mc", true));
   }

   public boolean addSubcomponent(String var1, String[]... var2) {
      ArrayList var3 = new ArrayList();
      String[][] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String[] var7 = var4[var6];
         var3.addAll(Arrays.asList(var7));
      }

      String[] var11 = (String[])var3.toArray(new String[var3.size()]);

      boolean var12;
      try {
         this._subcomponentsLock.writeLock().lock();
         if (!this._subcomponents.containsKey(var1)) {
            this._subcomponents.put(var1, var11);
            var12 = true;
            return var12;
         }

         var12 = false;
      } finally {
         this._subcomponentsLock.writeLock().unlock();
      }

      return var12;
   }

   public String[] getSubcomponent(String var1) {
      return this.getSubcomponent(var1, false);
   }

   public String[] getSubcomponent(String var1, boolean var2) {
      try {
         this._subcomponentsLock.readLock().lock();
         if (!var1.endsWith("*")) {
            if (var2 && !this._subcomponents.containsKey(var1)) {
               throw new IllegalArgumentException("Unknown subcomponent: " + var1);
            } else {
               String[] var11 = (String[])this._subcomponents.get(var1);
               return var11;
            }
         } else {
            ArrayList var3 = new ArrayList();
            String var4 = var1.substring(0, var1.length() - 1);
            Iterator var5 = this._subcomponents.keySet().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               if (var4.length() == 0 || var6.startsWith(var4)) {
                  String[] var7 = this.getSubcomponent(var6, true);
                  var3.addAll(Arrays.asList(var7));
               }
            }

            String[] var12 = (String[])var3.toArray(new String[var3.size()]);
            return var12;
         }
      } finally {
         this._subcomponentsLock.readLock().unlock();
      }
   }
}
