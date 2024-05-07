package weblogic.security.spi;

/** @deprecated */
public interface AdjudicationProvider extends SecurityProvider {
   Adjudicator getAdjudicator();
}
