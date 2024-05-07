package weblogic.wsee.security.wst.framework;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.wsee.security.wst.faults.RequestFailedException;
import weblogic.wsee.util.Verbose;

public class TrustProcessorFactory {
   private static final boolean verbose = Verbose.isVerbose(TrustProcessorFactory.class);
   private ConcurrentHashMap<String, TrustProcessor> processors = new ConcurrentHashMap();
   private static TrustProcessorFactory instance = new TrustProcessorFactory();

   private TrustProcessorFactory() {
      try {
         TrustProcessor var1 = createTrustProcessor("weblogic.wsee.security.wst.internal.IssueProcessor");
         this.register(var1);
         this.register("/SCT", var1);
         this.register("http://schemas.xmlsoap.org/ws/2005/02/trust/RSTR/SCT", var1);
         this.register(createTrustProcessor("weblogic.wsee.security.wst.internal.RenewProcessor"));
         this.register(createTrustProcessor("weblogic.wsee.security.wst.internal.CancelProcessor"));
      } catch (RequestFailedException var2) {
         var2.printStackTrace();
      }

   }

   public static TrustProcessorFactory getInstance() {
      return instance;
   }

   public void register(TrustProcessor var1) {
      this.register(var1.getRequestType(), var1);
   }

   public void register(String var1, TrustProcessor var2) {
      this.processors.put(var1, var2);
   }

   public TrustProcessor getProcessor(String var1) throws RequestFailedException {
      if (verbose) {
         Verbose.log((Object)(" looking up TrustProcessor for requestType='" + var1 + "'"));
      }

      Set var2 = this.processors.entrySet();
      Iterator var3 = var2.iterator();

      Map.Entry var4;
      do {
         if (!var3.hasNext()) {
            throw new RequestFailedException("Can not find TrustProcessor for request type: " + var1);
         }

         var4 = (Map.Entry)var3.next();
      } while(!var1.endsWith((String)var4.getKey()));

      if (verbose) {
         Verbose.log((Object)(" for requestType='" + var1 + "', we will return '" + ((TrustProcessor)var4.getValue()).getClass().getName() + "'"));
      }

      return (TrustProcessor)var4.getValue();
   }

   private static final TrustProcessor createTrustProcessor(String var0) throws RequestFailedException {
      try {
         Class var1 = Class.forName(var0);
         return (TrustProcessor)var1.newInstance();
      } catch (ClassNotFoundException var2) {
         throw new RequestFailedException("Can not find trust processor " + var0);
      } catch (IllegalAccessException var3) {
         throw new RequestFailedException("Can not access trust processor " + var0);
      } catch (InstantiationException var4) {
         throw new RequestFailedException("Can not instantiate trust processor " + var0);
      } catch (ClassCastException var5) {
         throw new RequestFailedException(var0 + " is not a trust processor.");
      }
   }
}
