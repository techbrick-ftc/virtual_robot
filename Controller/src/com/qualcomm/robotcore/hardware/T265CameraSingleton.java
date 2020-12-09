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
        double outputX = robotX - startX + offsetX;
        double outputY = robotY - startY + offsetY;
        System.out.printf("Robot: %f | %f\nStart: %f | %f\nOffset %f | %f\n", robotX, robotY, startX, startY, offsetX, offsetY);
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

    public void reset() {
        this.robotX = 0;
        this.robotY = 0;
        this.offsetX = 0;
        this.offsetY = 0;

        this.started = false;
    }
}