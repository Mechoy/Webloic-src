package weblogic.xml.crypto.api;

import java.security.spec.AlgorithmParameterSpec;

public interface AlgorithmMethod {
   String getAlgorithm();

   AlgorithmParameterSpec getParameterSpec();
}
