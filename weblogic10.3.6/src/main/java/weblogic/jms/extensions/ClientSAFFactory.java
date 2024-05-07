package weblogic.jms.extensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.jms.JMSException;
import weblogic.jms.safclient.ClientSAFImpl;
import weblogic.kernel.KernelStatus;

public class ClientSAFFactory {
   private static final String DEFAULT_FILENAME = "ClientSAF.xml";

   public static ClientSAF getClientSAF() throws ClientSAFDuplicateException, JMSException {
      try {
         return getClientSAF(new FileInputStream(new File("ClientSAF.xml")));
      } catch (FileNotFoundException var1) {
         throw new weblogic.jms.common.JMSException(var1);
      }
   }

   public static ClientSAF getClientSAF(InputStream var0) throws ClientSAFDuplicateException, JMSException {
      String var1 = System.getProperty("user.dir");
      if (var1 == null) {
         throw new JMSException("Cannot get the users current working directory");
      } else {
         return getClientSAF(new File(var1), var0);
      }
   }

   public static ClientSAF getClientSAF(File var0, InputStream var1) throws ClientSAFDuplicateException, JMSException {
      if (KernelStatus.isServer()) {
         throw new JMSException("An attempt was made to create a SAF client on a WLS server");
      } else if (var1 == null) {
         throw new JMSException("Must have an input stream");
      } else {
         ClientSAFImpl var2 = new ClientSAFImpl(var0, var1);
         return var2;
      }
   }
}
