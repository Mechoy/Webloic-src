package weblogic.wsee.security.wst.faults;

import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;

public class WSTFaultUtil {
   public static void raiseFault(WSTFaultException var0) {
      throw new SOAPFaultException(var0.getFault(), var0.getFaultString(), (String)null, SOAPFaultUtil.newDetail(var0, false));
   }
}
