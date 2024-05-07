package weblogic.xml.crypto.dsig;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

public class DsigConstants {
   public static final String XMLNS_DSIG = "http://www.w3.org/2000/09/xmldsig#";
   public static final String DSIG_PREFIX = "dsig";
   public static final String SIGNATURE_ELEMENT = "Signature";
   public static final QName SIGNATURE_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Signature");
   public static final String KEYNAME_ELEMENT = "KeyName";
   public static final QName KEYNAME_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyName");
   public static final String KEYVALUE_ELEMENT = "KeyValue";
   public static final QName KEYVALUE_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
   public static final String OBJECT_ELEMENT = "Object";
   public static final String X509DATA_ELEMENT = "X509Data";
   public static final QName X509DATA_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data");
   public static final String X509ISSUER_SERIAL_ELEMENT = "X509IssuerSerial";
   public static final QName X509ISSUER_SERIAL_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
   public static final String X509ISSUER_NAME_ELEMENT = "X509IssuerName";
   public static final String X509SERIAL_NUMBER_ELEMENT = "X509SerialNumber";
   public static final String RETRIEVALMETHOD_ELEMENT = "RetrievalMethod";
   public static QName RETRIEVALMETHOD_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
   public static final String X509CERTIFICATE_ELEMENT = "X509Certificate";
   public static final QName X509CERTIFICATE_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
   public static final String X509SUBJECT_NAME_ELEMENT = "X509SubjectName";
   public static final QName X509SUBJECT_NAME_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
   public static final String X509KEY_IDENTIFIER_ELEMENT = "X509KeyIdentifier";
   public static final QName X509KEY_IDENTIFIER_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509KeyIdentifier");
   public static final String X509CRL_ELEMENT = "X509CRL";
   public static final QName X509CRL_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509CRL");
   public static final String ID_ATTRIBUTE = "Id";
   public static final QName ID_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Id", "dsig");
   public static final String PREFIX_LIST_ATTRIBUTE = "PrefixList";
   public static final String DEFAULT_NS = "#default";
   public static final String INCL_NS_ELEMENT = "InclusiveNamespaces";
   private static final String SOAP12_ENCODING_NS = "http://www.w3.org/2003/05/soap-encoding";
   private static final QName XSD_TYPE_NAME = new QName("http://www.w3.org/2001/XMLSchema-instance", "type");
   private static final QName SOAP11_ARRAYTYPE_NAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType");
   private static final QName SOAP12_ARRAYTYPE_NAME = new QName("http://www.w3.org/2003/05/soap-encoding", "arrayType");
   public static final List QNAME_VALUE_ATTRIBUTES = new ArrayList();

   static {
      QNAME_VALUE_ATTRIBUTES.add(XSD_TYPE_NAME);
      QNAME_VALUE_ATTRIBUTES.add(SOAP11_ARRAYTYPE_NAME);
      QNAME_VALUE_ATTRIBUTES.add(SOAP12_ARRAYTYPE_NAME);
   }
}
