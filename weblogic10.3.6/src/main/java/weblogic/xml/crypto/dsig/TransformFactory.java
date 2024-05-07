package weblogic.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;

public interface TransformFactory {
   Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException;

   String getURI();
}
