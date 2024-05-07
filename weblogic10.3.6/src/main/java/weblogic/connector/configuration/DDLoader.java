package weblogic.connector.configuration;

import weblogic.connector.common.Debug;
import weblogic.xml.process.SAXValidationException;

public abstract class DDLoader {
   public static final String CONNECTOR10_RAR_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Connector 1.0//EN";

   protected void validateInitialCapacity(int var1) throws SAXValidationException {
      if (var1 < 0) {
         String var2 = Debug.getExceptionInitialCapacityMustBePositive();
         throw new SAXValidationException(var2);
      }
   }

   protected void validateShrinkPeriodMinutes(int var1) throws SAXValidationException {
      this.validateShrinkFrequencySeconds(var1 * 60);
   }

   protected void validateShrinkFrequencySeconds(int var1) throws SAXValidationException {
      if (var1 <= 0) {
         String var2 = Debug.getExceptionShrinkFrequencySecondsMustBePositive();
         throw new SAXValidationException(var2);
      }
   }

   protected void validateMaxIdleTime(int var1) throws SAXValidationException {
      this.validateInactiveConnectionTimeoutSeconds(var1);
   }

   protected void validateInactiveConnectionTimeoutSeconds(int var1) throws SAXValidationException {
      if (var1 < 0) {
         String var2 = Debug.getExceptionInactiveConnectionTimeoutSecondsNegative();
         throw new SAXValidationException(var2);
      }
   }

   protected void printDeprecationWarning(String var1, String var2) {
      if (var2 != null) {
         Debug.logDeprecationReplacedWarning(var1, var2);
      } else {
         Debug.logDeprecationNotUsedWarning(var1);
      }

   }

   protected void printDeletionWarning(String var1) {
      Debug.logDeprecationNotUsedWarning(var1);
   }
}
