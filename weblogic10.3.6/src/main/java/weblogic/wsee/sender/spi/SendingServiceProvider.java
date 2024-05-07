package weblogic.wsee.sender.spi;

import weblogic.wsee.sender.api.Preferences;
import weblogic.wsee.sender.api.SendingService;

public interface SendingServiceProvider extends SendingService {
   Preferences getPreferences();
}
