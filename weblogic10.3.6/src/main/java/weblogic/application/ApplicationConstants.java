package weblogic.application;

import java.io.File;

public interface ApplicationConstants {
   String APP_XML_URI = "META-INF/application.xml";
   String APP_CLIENT_URI = "META-INF/application-client.xml";
   String WL_APP_XML_URI = "META-INF/weblogic-application.xml";
   String WL_EXT_XML_NAME = "weblogic-extension.xml";
   String WL_EXT_XML_URI = "META-INF/weblogic-extension.xml";
   String APP_INF_CLASSES = "APP-INF" + File.separator + "classes";
   String APP_INF_LIB = "APP-INF" + File.separator + "lib";
   String APP_LIB = "lib";
   String J2EE_NAMESPACE_URI = "http://java.sun.com/xml/ns/j2ee";
   String JAVAEE_NAMESPACE_URI = "http://java.sun.com/xml/ns/javaee";
   String PERSISTENCE_NAMESPACE_URI = "http://java.sun.com/xml/ns/persistence";
   String WLS_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/90";
   String WLS95_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/10.0";
   String WLS_CMP11_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/60";
   String WLS95_PERSISTENCE_URI = "http://www.bea.com/ns/weblogic/10.0/persistence";
   String WEBLOGIC_APPLICATION_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-application";
   String WEBLOGIC_EXTENSION_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-extension";
   String WEBLOGIC_APPLICATION_CLIENT_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-application-client";
   String WEBLOGIC_EJB_JAR_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-ejb-jar";
   String WEBLOGIC_RDBMS_JAR_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-rdbms-jar";
   String BEA_WEBLOGIC_RDBMS_JAR_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/weblogic-rdbms-jar";
   String WEBLOGIC_INTERCEPTION_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-interception";
   String JDBC_DATA_SOURCE_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/jdbc-data-source";
   String WEBLOGIC_JMS_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-jms";
   String WEBLOGIC_CONNECTOR_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-connector";
   String BEA_WEBLOGIC_CONNECTOR_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/weblogic-connector";
   String WEBLOGIC_WEB_APP_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-web-app";
   String WEBLOGIC_WEBSERVICES_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-webservices";
   String WEBLOGIC_WSEE_CLIENTHANDLERCHAIN_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-wsee-clientHandlerChain";
   String BEA_WEBLOGIC_WSEE_CLIENTHANDLERCHAIN_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/weblogic-wsee-clientHandlerChain";
   String WEBSERVICE_POLICY_REF_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/webservice-policy-ref";
   String WEBLOGIC_WSEE_STANDALONECLIENT_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-wsee-standaloneclient";
   String BEA_WEBLOGIC_WSEE_STANDALONECLIENT_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/weblogic-wsee-standaloneclient";
   String DEPLOYMENT_PLAN_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/deployment-plan";
   String BEA_DEPLOYMENT_PLAN_NAMESPACE_URI = "http://www.bea.com/ns/weblogic/deployment-plan";
   String PERSISTENCE_CONFIGURATION_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/persistence-configuration";
   String UNSET_APP_CONTEXT_ROOT = "__BEA_WLS_INTERNAL_UNSET_CONTEXT_ROOT";
   String APP_NAME_TOKEN = "${APPNAME}";
   String DEBUG_APP_CONTAINER = "DebugAppContainer";
   String WEBLOGIC_COHERENCE_NAMESPACE_URI = "http://xmlns.oracle.com/weblogic/weblogic-coherence";
}
