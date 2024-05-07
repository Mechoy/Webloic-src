package weblogic.wsee.security.wssp;

public interface AlgorithmSuiteInfo {
   String getSymSigUri();

   String getAsymSigUri();

   String getDigUri();

   String getEncUri();

   String getSymKwUri();

   String getAsymKwUri();

   String getCompKeyUri();

   String getSigKdUri();

   String getEncKdUri();

   int getMinSymKeyLength();

   int getMaxSymKeyLength();

   int getMinAsymKeyLength();

   int getMaxAsymKeyLength();

   String getC14nAlgUri();
}
