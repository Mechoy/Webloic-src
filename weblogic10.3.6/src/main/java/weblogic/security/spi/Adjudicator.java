package weblogic.security.spi;

/** @deprecated */
public interface Adjudicator {
   void initialize(String[] var1);

   boolean adjudicate(Result[] var1);
}
