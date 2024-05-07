package weblogic.corba.rmic;

import weblogic.utils.Getopt2;

public final class IDLMain {
   Getopt2 m_opts;
   IDLGenerator m_idlGenerator;

   public IDLMain(Getopt2 var1) {
      this.m_opts = var1;
      this.m_idlGenerator = new IDLGenerator(var1);
   }
}
