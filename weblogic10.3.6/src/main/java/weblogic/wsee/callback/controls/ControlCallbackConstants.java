package weblogic.wsee.callback.controls;

import javax.xml.namespace.QName;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public interface ControlCallbackConstants {
   QName[] CONTROL_CALLBACK_HEADERS = new QName[]{WSAddressingConstants.WSA_HEADER_REPLY_TO};
   String CONTROL_CALLBACK_EPR = "com.bea.control.callback.epr";
   String CONTROL_CALLBACK_NS = "http://www.bea.com/2005/08/controlcallback";
   String XML_TAG_CCB_CALLBACK_INFO = "ControlCallbackInfo";
   String CONTROL_CALLBACK_PREFIX = "controlcallback";
   QName CONTROL_CALLBACK_INFO_HEADER = new QName("http://www.bea.com/2005/08/controlcallback", "ControlCallbackInfo", "controlcallback");
   String CONTROL_CALLBACK_FAULT = "control.callback.fault";
   String CONTROL_CALLBACK_RETURN_VALUE = "control.callback.return.value";
   String CONTROL_CALLBACK_CONTEXT_PATH = "control.callback.context.path";
   String CONTROL_CALLBACK_SERVICE_URI = "control.callback.service.uri";
   String CONTROL_CALLBACK_INPUT_HEADERS = "control.callback.input.headers";
   String CONTROL_CALLBACK_OUTPUT_HEADERS = "control.callback.output.headers";
}
