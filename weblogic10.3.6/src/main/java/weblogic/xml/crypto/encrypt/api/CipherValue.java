package weblogic.xml.crypto.encrypt.api;

import weblogic.xml.crypto.api.XMLStructure;

public interface CipherValue extends CipherData, XMLStructure {
   byte[] getValue();
}
