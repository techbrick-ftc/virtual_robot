package com.qualcomm.robotcore.hardware;

public class T265CameraSingleton {

    private static T265CameraSingleton instance = new T265CameraSingleton();
    private T265CameraSingleton(){}

    public static T265CameraSingleton getInstance(){
        return instance;
    }

    private double startX;
    private double startY;

    private double robotX;
    private double robotY;

    private double offsetX;
    private double offsetY;

    private boolean started = false;

    private T265Camera.CameraMode cameraMode = T265Camera.CameraMode.NORMAL;

    public void start() {
        if (!this.started) {
            this.started = true;
        } else {
            throw new RuntimeException("Camera already started!");
        }
    }

    public void stop() {
        if (this.started) {
            this.started = false;
        } else {
            throw new RuntimeException("Camera already stopped!");
        }
    }

    public synchronized T265Camera.CameraUpdate getLastReceivedCameraUpdate() {
        if (!this.started) { throw new RuntimeException("Camera not started!"); }
        double outputX = cameraMode.equals(T265Camera.CameraMode.ARCADE) ? robotX : robotX - startX + offsetX;
        double outputY = cameraMode.equals(T265Camera.CameraMode.ARCADE) ? robotY : robotY - startY + offsetY;
        return new T265Camera.CameraUpdate(new T265Camera.Pose2d(new T265Camera.Translation2d(outputX, outputY),
                                                                 new T265Camera.Rotation2d()),
                                           new T265Camera.ChassisSpeeds(),
                                           T265Camera.PoseConfidence.High);
    }

    public void setOffset(T265Camera.Translation2d offset) {
        this.offsetX = offset.getX();
        this.offsetY = offset.getY();
    }

    public synchronized void startCamera(double x, double y) {
        this.startX = x;
        this.startY = y;
    }

    public synchronized void updateCamera(double x, double y) {
        this.robotX = x;
        this.robotY = y;
    }

    public void setMode(T265Camera.CameraMode cameraMode) {
        this.cameraMode = cameraMode;
    }

    public void reset() {
        this.robotX = 0;
        this.robotY = 0;
        this.offsetX = 0;
        this.offsetY = 0;

        this.started = false;
    }


}