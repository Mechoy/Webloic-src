package weblogic.wsee.wsdl;

import weblogic.xml.schema.binding.util.StdNamespace;

public interface WsdlConstants {
   String SOAP11 = "http://schemas.xmlsoap.org/wsdl/soap/";
   String SOAP12 = "http://schemas.xmlsoap.org/wsdl/soap12/";
   String SOAP_ENC = "http://schemas.xmlsoap.org/soap/encoding/";
   String SOAP12_ENC = "http://www.w3.org/2003/05/soap-encoding";
   String wsdlNS = StdNamespace.instance().wsdl();
   String schemaNS = "http://www.w3.org/2001/XMLSchema";
   String mimeNS = "http://schemas.xmlsoap.org/wsdl/mime/";
   String httpNS = "http://schemas.xmlsoap.org/wsdl/http/";
   String jmsTransport = "http://www.openuri.org/2002/04/soap/jms/";
   String jmsTransport12 = "http://www.openuri.org/2002/04/soap12/jms/";
   String httpTransport = "http://schemas.xmlsoap.org/soap/http";
   String httpsTransport = "http://schemas.xmlsoap.org/soap/https";
   String httpTransport12 = "http://schemas.xmlsoap.org/soap12/http";
   String httpsTransport12 = "http://schemas.xmlsoap.org/soap12/https";
}
