package weblogic.wsee.security.wst.framework;

import java.util.HashMap;
import weblogic.wsee.security.wst.faults.InvalidRequestException;

public class TrustRequestorFactory {
   public static final String SPEC_V200502 = "http://schemas.xmlsoap.org/ws/2005/02/trust";
   public static final String SPEC_V13 = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   public static final String DEFAULT_SPEC = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   private static TrustRequestorFactory instance = new TrustRequestorFactory();
   private HashMap<String, String> implRegistry = new HashMap();

   private TrustRequestorFactory() {
      this.implRegistry.put("http://schemas.xmlsoap.org/ws/2005/02/trust", "weblogic.wsee.security.wst.internal.TrustRequestorImpl");
      this.implRegistry.put("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "weblogic.wsee.security.wst.internal.TrustRequestorImpl");
   }

   public static TrustRequestorFactory getInstance() {
      return instance;
   }

   public TrustRequestor createTrustRequestor(String var1) throws InvalidRequestException {
      return newTrustRequestorInstance(this.implRegistry, var1 != null ? var1 : "http://schemas.xmlsoap.org/ws/2005/02/trust");
   }

   private static final TrustRequestor newTrustRequestorInstance(HashMap<String, String> var0, String var1) throws InvalidRequestException {
      String var2 = (String)var0.get(var1);
      if (var2 == null) {
         throw new InvalidRequestException("Can not find the TrustRequestor implementation for spec: " + var1);
      } else {
         try {
            Class var3 = Class.forName(var2);
            return (TrustRequestor)var3.newInstance();
         } catch (ClassNotFoundException var4) {
            throw new InvalidRequestException("Can not load the impl class: " + var2);
         } catch (IllegalAccessException var5) {
            throw new InvalidRequestException("Can not access the impl class: " + var2);
         } catch (InstantiationException var6) {
            throw new InvalidRequestException("Can not instantiate the impl class: " + var2);
         } catch (ClassCastException var7) {
            throw new InvalidRequestException(var2 + " must implement the TrustRequestor interface");
         }
      }
   }
}
