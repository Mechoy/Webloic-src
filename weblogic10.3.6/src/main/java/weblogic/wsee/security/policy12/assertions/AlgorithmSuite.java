package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class AlgorithmSuite extends NestedSecurityPolicy12Assertion {
   public static final String ALGORITHM_SUITE = "AlgorithmSuite";

   public QName getName() {
      return new QName(this.getNamespace(), "AlgorithmSuite", "sp");
   }

   public Basic256 getBasic256() {
      return (Basic256)this.getNestedAssertion(Basic256.class);
   }

   public Basic192 getBasic192() {
      return (Basic192)this.getNestedAssertion(Basic192.class);
   }

   public Basic128 getBasic128() {
      return (Basic128)this.getNestedAssertion(Basic128.class);
   }

   public TripleDes getTripleDes() {
      return (TripleDes)this.getNestedAssertion(TripleDes.class);
   }

   public Basic256Rsa15 getBasic256Rsa15() {
      return (Basic256Rsa15)this.getNestedAssertion(Basic256Rsa15.class);
   }

   public Basic128Rsa15 getBasic128Rsa15() {
      return (Basic128Rsa15)this.getNestedAssertion(Basic128Rsa15.class);
   }

   public Basic192Rsa15 getBasic192Rsa15() {
      return (Basic192Rsa15)this.getNestedAssertion(Basic192Rsa15.class);
   }

   public TripleDesRsa15 getTripleDesRsa15() {
      return (TripleDesRsa15)this.getNestedAssertion(TripleDesRsa15.class);
   }

   public Basic256Sha256 getBasic256Sha256() {
      return (Basic256Sha256)this.getNestedAssertion(Basic256Sha256.class);
   }

   public Basic192Sha256 getBasic192Sha256() {
      return (Basic192Sha256)this.getNestedAssertion(Basic192Sha256.class);
   }

   public Basic128Sha256 getBasic128Sha256() {
      return (Basic128Sha256)this.getNestedAssertion(Basic128Sha256.class);
   }

   public TripleDesSha256 getTripleDesSha256() {
      return (TripleDesSha256)this.getNestedAssertion(TripleDesSha256.class);
   }

   public Basic256Sha256Rsa15 getBasic256Sha256Rsa15() {
      return (Basic256Sha256Rsa15)this.getNestedAssertion(Basic256Sha256Rsa15.class);
   }

   public Basic192Sha256Rsa15 getBasic192Sha256Rsa15() {
      return (Basic192Sha256Rsa15)this.getNestedAssertion(Basic192Sha256Rsa15.class);
   }

   public Basic128Sha256Rsa15 getBasic128Sha256Rsa15() {
      return (Basic128Sha256Rsa15)this.getNestedAssertion(Basic128Sha256Rsa15.class);
   }

   public TripleDesSha256Rsa15 getTripleDesSha256Rsa15() {
      return (TripleDesSha256Rsa15)this.getNestedAssertion(TripleDesSha256Rsa15.class);
   }
}
