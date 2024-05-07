package weblogic.wsee.async;

import javax.xml.namespace.QName;

public interface AsyncConstants {
   String ASYNC_NS = "http://www.bea.com/2004/11/async";
   String ASYNC_PREFIX = "async";
   String XML_TAG_ASYNC_INFO = "AsyncInfo";
   String XML_TAG_CLASS_NAME = "ClassName";
   String XML_TAG_DESCRIPTION = "Description";
   QName ASYNC_RESPONSE_HEADER = new QName("http://www.bea.com/2004/11/async", "AsyncInfo", "async");
   String ASYNC_SERVICE_SUFFIX = "AsyncService";
   String ASYNC_STORE_NAME = "weblogic.wsee.async.store";
   String ASYNC_PRE_CALL_CONTEXT_PROPERTY = "weblogic.wsee.async.pre.call.context";
   String ASYNC_INVOKE_PROPERTY = "weblogic.wsee.async.invoke";
   String ASYNC_INVOKE_NONJWS_PROPERTY = "weblogic.wsee.async.invokeNonJws";
   String ASYNC_INVOKE_STATE_PROPERTY = "weblogic.wsee.async.invoke.state";
   String ASYNC_RESPONSE_PROPERTY = "weblogic.wsee.async.res";
   String ASYNC_RES_EPR_PROPERTY = "weblogic.wsee.async.res.epr";
   String STUB_NAME_PROPERTY = "weblogic.wsee.stub.name";
   String METHOD_NAME_PROPERTY = "weblogic.wsee.method.name";
   String OPERATION_NAME_PROPERTY = "weblogic.wsee.operation.name";
   String RETURN_TYPE_PROPERTY = "weblogic.wsee.return.type";
   String ASYNC_SERVICE_URI = "/_async/AsyncResponseService";
   String ASYNC_RESPONSE_METHOD_MAP = "weblogic.wsee.async.response.map";
   String ASYNC_FAILURE_METHOD_MAP = "weblogic.wsee.async.failure.map";
   String ASYNC_RESPONSE_METHOD = "weblogic.wsee.async.response.method";
   String ENCLOSING_CLASS_NAME_PROPERTY = "weblogic.wsee.enclosing.classname";
   String ENCLOSING_JWS_SERVICEURI = "weblogic.wsee.enclosing.jws.serviceuri";
   String DISPATCH_POLICY = "weblogic.wsee.mdb.DispatchPolicy";
   String JAXWS_DISPATCH_POLICY = "weblogic.wsee.jaxws.mdb.DispatchPolicy";
   String URI = "ASYNC_URI";
   String DYNAMIC_MDB_BEAN_NAME = "weblogic.wsee.server.jms.MdbWS";
   String DYNAMIC_MDB_ERROR_BEAN_NAME = "weblogic.wsee.server.jms.MdbErrorWS";
   String DYNAMIC_MDB_TYPE = "javax.jms.Queue";
   int DYNAMIC_MDB_DEFAULT_TXN_TIMEOUT = 180;
   String SERVICE_URI = "SERV_URI";
   String BUFFERED_MESSAGE_JMS_DELIVERY_COUNT = "weblogic.wsee.buffer.BufferedMessageJmsDeliveryCount";
}
