package weblogic.corba.rmic;

public final class IDLExtraLine {
   String m_line;
   String m_mangledName;

   public IDLExtraLine() {
      this((String)null, (String)null);
   }

   public IDLExtraLine(String var1, String var2) {
      this.m_line = null;
      this.m_mangledName = null;
      this.m_mangledName = var1;
      this.m_line = var2;
   }

   public String getMangledName() {
      return this.m_mangledName;
   }

   public String toIDL() {
      return this.m_line;
   }
}
