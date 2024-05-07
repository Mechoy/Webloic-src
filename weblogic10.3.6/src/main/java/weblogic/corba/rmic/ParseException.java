package weblogic.corba.rmic;

final class ParseException extends Exception {
   private static final long serialVersionUID = 2161006850779122473L;

   public ParseException(int var1, String var2) {
      super("at line (" + var1 + "): " + var2);
   }

   public ParseException(String var1) {
      super(var1);
   }
}
