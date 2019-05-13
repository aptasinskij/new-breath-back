package edu.aptasinskij.platform;

import static java.lang.System.out;

public class App {

    private static final Context CONTEXT = Context.context();
    private static final Session.Factory SESSION_FACTORY = Session.factory(CONTEXT);

    private static final CashIn.Factory CASH_IN_FACTORY = CashIn.factory(CONTEXT);
    private static final CashIn.Oracle CASH_IN_ORACLE = CashIn.oracle(CONTEXT);

    private static final Camera.Factory CAMERA_FACTORY = Camera.factory(CONTEXT);
    private static final Printer.Factory PRINTER_FACTORY = Printer.factory(CONTEXT);

    static {
        CONTEXT.register(Session.SessionFactory.NAME, SESSION_FACTORY);

        CONTEXT.register(CashIn.CashInFactory.NAME, CASH_IN_FACTORY);
        CONTEXT.register(CashIn.CashInOracle.NAME, CASH_IN_ORACLE);

        CONTEXT.register(Printer.PrinterFactory.NAME, PRINTER_FACTORY);
        CONTEXT.register(Camera.CameraFactory.NAME, CAMERA_FACTORY);
    }

    public static void main(String[] args) {
        Session session = Context.get(CONTEXT, Session.Factory.class, Session.SessionFactory.NAME).session("1", "2", "3");
        CashIn cashIn = session.cashIn();
        cashIn.open(100, success -> out.println("Success"), fail -> out.println("Fail"));
        CashIn.Internal.class.cast(cashIn).successOpen();
    }

}
