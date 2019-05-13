package edu.aptasinskij.platform;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public interface CashIn {

    interface Internal extends CashIn {

        void successOpen();

        void failOpen();

        void successClose();

        void failClose();

    }

    interface Transfer {
    }

    interface Factory {
        CashIn cashIn(Session session);
    }

    interface Oracle {

        void onNextOpen(Supplier<CashIn> cashInSupplier);

        void onNextClose(Supplier<CashIn> cashInSupplier);

    }

    static Factory factory(Context context) {
        return new CashInFactory(context);
    }

    static Oracle oracle(Context context) {
        return new CashInOracle(context);
    }

    void open(int maxBalance, Consumer<CashIn> success, Consumer<CashIn> fail);

    void close(Consumer<CashIn> success, Consumer<CashIn> fail);

    void transfer(Transfer transfer);

    final class ChannelImpl implements Internal {

        private final Context context;
        private final Session session;

        private Consumer<CashIn> successOpen;
        private Consumer<CashIn> failOpen;
        private int maxBalance;

        private Status status = Status.CREATED;

        public ChannelImpl(Context context, Session session) {
            this.context = context;
            this.session = session;
        }

        @Override
        public void open(int maxBalance, Consumer<CashIn> success, Consumer<CashIn> fail) {
            if (!Status.CREATED.equals(this.status)) throw new IllegalStateException();
            this.maxBalance = maxBalance;
            this.successOpen = Objects.requireNonNull(success);
            this.failOpen = Objects.requireNonNull(fail);
            this.status = Status.OPENING;
            Context.get(context, CashIn.Oracle.class, CashInOracle.NAME).onNextOpen(() -> this);
        }

        @Override
        public void successOpen() {
            if (!Status.OPENING.equals(this.status)) throw new IllegalStateException();
            this.status = Status.ACTIVE;
            this.successOpen.accept(this);
        }

        @Override
        public void failOpen() {
            if (!Status.OPENING.equals(this.status)) throw new IllegalStateException();
            this.status = Status.FAILED_TO_OPEN;
            this.failOpen.accept(this);
            this.session.cashInReleased(() -> this);
        }

        @Override
        public void close(Consumer<CashIn> success, Consumer<CashIn> fail) {

        }

        @Override
        public void successClose() {

        }

        @Override
        public void failClose() {

        }

        @Override
        public void transfer(Transfer transfer) {

        }

        @Override
        public String toString() {
            return "ChannelImpl{" +
                    "session=" + session +
                    ", maxBalance=" + maxBalance +
                    ", status=" + status +
                    '}';
        }

        private enum Status {
            CREATED, OPENING, FAILED_TO_OPEN, ACTIVE, CLOSING, FAILED_TO_CLOSE, CLOSED
        }

    }

    final class CashInFactory implements Factory {

        public static final String NAME = "cash-in-factory";

        private final Context context;

        public CashInFactory(Context context) {
            this.context = context;
        }

        @Override
        public CashIn cashIn(Session session) {
            return new ChannelImpl(this.context, session);
        }

    }

    final class CashInOracle implements Oracle {

        public static final String NAME = "cash-in-oracle";

        private static final Logger log = Logger.getLogger(CashInOracle.class.getName());

        private final Context context;

        public CashInOracle(Context context) {
            this.context = context;
        }

        @Override
        public void onNextOpen(Supplier<CashIn> cashInSupplier) {
            log.info(() -> String.format("OPEN: %s", cashInSupplier.get()));
        }

        @Override
        public void onNextClose(Supplier<CashIn> cashInSupplier) {

        }
    }

}
