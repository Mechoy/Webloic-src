package weblogic.ejb.spi;

public interface ScrubbedCache {
   void setScrubInterval(int var1);

   void startScrubber();

   void stopScrubber();
}
