package edu.aptasinskij.platform;

public interface Camera {

    interface Factory {

        Camera camera(Session session);

    }

    static Factory factory(Context context) {
        return new CameraFactory(context);
    }

    final class CameraFactory implements Factory {

        public static final String NAME = "camera-factory";

        private final Context context;

        public CameraFactory(Context context) {
            this.context = context;
        }

        @Override
        public Camera camera(Session session) {
            return new CameraImpl(session);
        }
    }

    final class CameraImpl implements Camera {

        private final Session session;

        public CameraImpl(Session session) {
            this.session = session;
        }

    }

}
