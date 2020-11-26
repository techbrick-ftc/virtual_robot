package com.qualcomm.robotcore.hardware;

public interface T265Camera extends HardwareDevice{

    public void start();

    public void stop();

    public T265Camera.CameraUpdate getLastReceivedCameraUpdate();

    public static class CameraUpdate {
        public final Pose2d pose;
        public final ChassisSpeeds velocity;
        public final T265Camera.PoseConfidence confidence;

        public CameraUpdate(Pose2d pose, ChassisSpeeds velocity, T265Camera.PoseConfidence confidence) {
            this.pose = pose;
            this.velocity = velocity;
            this.confidence = confidence;
        }
    }

    public static class Pose2d {
        private Transform2d transform2d;
        private Rotation2d rotation2d;

        public Pose2d(Transform2d transform2d, Rotation2d rotation2d) {
            this.transform2d = transform2d;
            this.rotation2d = rotation2d;
        }

        public Pose2d() {
            this(new Transform2d(), new Rotation2d());
        }

        public Transform2d getTransform() { return this.transform2d; }
        public Rotation2d getRotation() { return this.rotation2d; }
    }

    public static class Transform2d {
        private double x;
        private double y;

        public Transform2d(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Transform2d() {
            this(0, 0);
        }

        public double getX() { return this.x; }
        public double getY() { return this.y; }
    }

    public static class Rotation2d {}

    public static class ChassisSpeeds {}

    public static enum PoseConfidence {
        Failed,
        Low,
        Medium,
        High;

        private PoseConfidence() {
        }
    }
}
