package weblogic.iiop.csi;

public interface CSIConstants {
   short MTEstablishContext = 0;
   short MTCompleteEstablishContext = 1;
   short MTContextError = 4;
   short MTMessageInContext = 5;
   long X509AttributeCertChain = 1L;
   int ITTAbsent = 0;
   int ITTAnonymous = 1;
   int ITTPrincipalName = 2;
   int ITTX509CertChain = 4;
   int ITTDistinguishedName = 8;
   int CEMinor = 1;
   int CEMajorInvalidEvidence = 1;
   int CEMajorInvalidMechanism = 2;
   int CEMajorConflictingEvidence = 3;
   int CEMajorNoContext = 4;
}
