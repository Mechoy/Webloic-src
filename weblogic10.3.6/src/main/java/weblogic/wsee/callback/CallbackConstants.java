package weblogic.wsee.callback;

import javax.xml.namespace.QName;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public interface CallbackConstants {
   QName[] CALLBACK_HEADERS = new QName[]{WSAddressingConstants.WSA_HEADER_REPLY_TO};
   String CALLBACK_SERVICE_CONTEXT_PATH_PREFIX = "/callback/";
   String CALLBACK_EPR = "weblogic.wsee.callback.epr";
   String CALLBACK_NS = "http://www.openuri.org/2006/03/callback";
   String CALLBACK_PREFIX = "callback";
   String XML_TAG_CLASS_NAME = "ClassName";
   String XML_TAG_STUB_NAME = "StubName";
   String XML_TAG_CALLER_SERVICEURI = "ServiceURI";
   String XML_TAG_ROLE_REQUIRED = "RoleRequired";
   String XML_TAG_CALLBACK_INFO = "CallbackInfo";
   QName CALLBACK_INFO_HEADER = new QName("http://www.openuri.org/2006/03/callback", "CallbackInfo", "callback");
   String CALLBACK_METHOD_PROPERTY = "weblogic.wsee.callback.method";
   String CALLBACK_CLASS_PROPERTY = "weblogic.wsee.callback.class";
   String CALLBACK_CONTEXT_PATH_PROPERTY = "weblogic.wsee.callback.contextpath";
   String CALLBACK_SERVICE_URI_PROPERTY = "weblogic.wsee.callback.serviceuri";
   String CALLBACK_SERVICE_SUFFIX = "Cb";
   String XML_TAG_CALLBACK_TO = "CallbackTo";
   QName HEADER_CALLBACK_TO = new QName("http://www.openuri.org/2006/03/callback", "CallbackTo", "callback");
   String CALLBACK_ROLE_NAME = "Callback";
   String SERVICE_ROLE_NAME = "Service";
   String ENCLOSING_JWS_CONTEXTPATH = "weblogic.wsee.enclosing.jws.contextpath";
   String ENCLOSING_JWS_SERVICENAME = "weblogic.wsee.enclosing.jws.servicename";
   String CALLBACK_APPVERSIONID_PROPERTY = "weblogic.wsee.callback.appversion";
   String XML_TAG_CALLER_APPVERSION = "AppVersion";
}
