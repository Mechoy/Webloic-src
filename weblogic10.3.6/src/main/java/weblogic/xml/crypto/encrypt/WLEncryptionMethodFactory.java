package weblogic.xml.crypto.encrypt;

import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.api.MarshalException;

interface WLEncryptionMethodFactory {
   String getAlgorithm();

   WLEncryptionMethod getEncryptionMethod(AlgorithmParameterSpec var1, Integer var2);

   KeyWrap getKeyWrap(AlgorithmParameterSpec var1, Integer var2);

   EncryptionAlgorithm getEncryptionAlgorithm(AlgorithmParameterSpec var1, Integer var2);

   AlgorithmParameterSpec readParameters(XMLStreamReader var1) throws MarshalException;
}
