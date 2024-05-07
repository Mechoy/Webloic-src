package weblogic.security.acl.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import weblogic.security.internal.FileWriter;

class FileRealmFileWriter implements FileWriter {
   private Properties props;

   public FileRealmFileWriter(Properties var1) {
      this.props = var1;
   }

   public void write(OutputStream var1) throws IOException {
      this.props.store(var1, (String)null);
   }
}
