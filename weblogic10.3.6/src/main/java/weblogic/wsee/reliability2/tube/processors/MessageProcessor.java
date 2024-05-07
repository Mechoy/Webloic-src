package weblogic.wsee.reliability2.tube.processors;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.policy.WsrmPolicyHelper;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.reliability2.tube.DispatchFactory;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public abstract class MessageProcessor {
   protected WsrmTubeUtils _tubeUtil;
   protected WSBinding _binding;
   protected AddressingVersion _addrVersion;
   protected SOAPVersion _soapVersion;
   protected WSDLPort _wsdlPort;
   protected WsrmPolicyHelper _policyHelper;
   protected DispatchFactory _dispatchFactory;

   protected MessageProcessor(WsrmTubeUtils var1) {
      this._tubeUtil = var1;
      this._binding = var1.getBinding();
      this._addrVersion = this._binding.getAddressingVersion();
      this._soapVersion = this._binding.getSOAPVersion();
      this._wsdlPort = var1.getWsdlPort();
      this._policyHelper = var1.getPolicyHelper();
      this._dispatchFactory = var1.getDispatchFactory();
   }

   public abstract boolean processOutbound(Packet var1, WsrmConstants.RMVersion var2, SequenceIdFactory var3, String var4) throws WsrmException;

   public abstract void handleInbound(Packet var1, WsrmConstants.RMVersion var2, WsrmTubeUtils.InboundMessageResult var3) throws WsrmException;
}
