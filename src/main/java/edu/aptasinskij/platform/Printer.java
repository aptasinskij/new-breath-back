package edu.aptasinskij.platform;

public interface Printer {

    interface Factory {
        Printer printer(Session session);
    }

    static Factory factory(Context context) {
        return new PrinterFactory(context);
    }

    final class PrinterFactory implements Factory {

        public static final String NAME = "printer-factory";

        private final Context context;

        private PrinterFactory(Context context) {
            this.context = context;
        }

        @Override
        public Printer printer(Session session) {
            return new PrinterImpl(this.context, session);
        }

    }

    final class PrinterImpl implements Printer {

        private final Context context;
        private final Session session;

        public PrinterImpl(Context context, Session session) {
            this.context = context;
            this.session = session;
        }

    }

}
