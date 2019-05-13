package edu.aptasinskij.platform;

import edu.aptasinskij.platform.Camera.CameraFactory;

import java.util.function.Supplier;

import static java.util.Objects.isNull;

public interface Session {

    interface Factory {

        Session session(String xToken, String mayaKey, String accessKey);

    }

    static Factory factory(Context context) {
        return new SessionFactory(context);
    }

    Camera camera();

    Printer printer();

    CashIn cashIn();

    void cashInReleased(Supplier<CashIn> cashInSupplier);

    CashOut cashOut();

    final class SessionFactory implements Factory {

        public static final String NAME = "session-factory";

        private final Context context;

        public SessionFactory(Context context) {
            this.context = context;
        }

        @Override
        public Session session(String xToken, String mayaKey, String accessKey) {
            return new SessionImpl(xToken, mayaKey, accessKey, this.context);
        }
    }

    final class SessionImpl implements Session {

        private final String xToken;
        private final String mayaKey;
        private final String accessKey;
        private final Context context;

        private Camera camera;
        private Printer printer;
        private CashIn cashIn;
        private CashOut cashOut;

        private SessionImpl(String xToken, String mayaKey, String accessKey, Context context) {
            this.xToken = xToken;
            this.mayaKey = mayaKey;
            this.accessKey = accessKey;
            this.context = context;
        }

        @Override
        public Camera camera() {
            if (isNull(camera)) {
                this.camera = Context.get(context, Camera.Factory.class, CameraFactory.NAME).camera(this);
            }
            return this.camera;
        }

        @Override
        public Printer printer() {
            if (isNull(printer)) {
                this.printer = Context.get(context, Printer.Factory.class, Printer.PrinterFactory.NAME).printer(this);
            }
            return this.printer;
        }

        @Override
        public CashIn cashIn() {
            if (isNull(cashIn)) {
                this.cashIn = Context.get(context, CashIn.Factory.class, CashIn.CashInFactory.NAME).cashIn(this);
            }
            return this.cashIn;
        }

        @Override
        public void cashInReleased(Supplier<CashIn> cashInSupplier) {
            this.cashIn = null;
        }

        @Override
        public CashOut cashOut() {
            throw new UnsupportedOperationException();
        }
    }

}
