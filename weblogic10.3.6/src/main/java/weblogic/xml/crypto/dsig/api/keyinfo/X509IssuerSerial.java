package weblogic.xml.crypto.dsig.api.keyinfo;

import java.math.BigInteger;
import weblogic.xml.crypto.api.XMLStructure;

public interface X509IssuerSerial extends XMLStructure {
   String getIssuerName();

   BigInteger getSerialNumber();
}
