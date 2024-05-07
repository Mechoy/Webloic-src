package weblogic.wsee.conversation;

import javax.xml.namespace.QName;

public interface ConversationConstants {
   String CONV_81_NS = "http://www.openuri.org/2002/04/soap/conversation/";
   String CONV_NS = "http://www.openuri.org/2002/04/soap/conversation/";
   String CONV_PREFIX = "conv";
   String XML_TAG_START_HEADER = "StartHeader";
   String XML_TAG_CONTINUE_HEADER = "ContinueHeader";
   String XML_TAG_CONVERSATION_ID = "conversationID";
   String XML_TAG_CALLBACK_LOCATION = "callbackLocation";
   String XML_TAG_CALLBACK_HEADER = "CallbackHeader";
   String XML_TAG_SERVER_NAME = "serverName";
   String XML_TAG_APP_VERSION_ID = "appVersionId";
   QName CONV_HEADER_START = new QName("http://www.openuri.org/2002/04/soap/conversation/", "StartHeader", "conv");
   QName CONV_HEADER_CONTINUE = new QName("http://www.openuri.org/2002/04/soap/conversation/", "ContinueHeader", "conv");
   QName WLW81_CALLBACK_HEADER = new QName("http://www.openuri.org/2002/04/soap/conversation/", "CallbackHeader", "conv");
   QName[] CONV_HEADERS = new QName[]{CONV_HEADER_START, CONV_HEADER_CONTINUE};
   int CONVERSATION_VERSION_ONE = 1;
   int CONVERSATION_VERSION_TWO = 2;
   String CONVERSATION_SERVICE_ASYNC_CONTEXT_PATH = "/conversation/AsyncEndpoint";
   String CONVERSATION_KEY_PROPERTY = "weblogic.wsee.conversation.key";
   String CONVERSATION_WAIT_ID_PROPERTY = "weblogic.wsee.conversation.waitid";
   String CONVERSATION_WAIT_ID_TIMEOUT_PROPERTY = "weblogic.wsee.conversation.waitid.timeout";
   String CONVERSATION_EPR_SET_PROPERTY = "weblogic.wsee.conversation.epr.set";
   String CONVERSATION_STORE_NAME = "weblogic.wsee.conversation.store";
   String CONVERSATION_CORRELATION_ID = "weblogic.wsee.conversation.correlation.id";
   String CONVERSATION_STARTED_PROPERTY = "weblogic.wsee.conversation.started";
   QName[] WLW81_CALLBACK_HEADERS = new QName[]{WLW81_CALLBACK_HEADER};
}
