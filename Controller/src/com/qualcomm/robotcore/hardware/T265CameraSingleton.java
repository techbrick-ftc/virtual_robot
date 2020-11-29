package com.qualcomm.robotcore.hardware;

public class T265CameraSingleton {

    private static T265CameraSingleton instance = new T265CameraSingleton();
    private T265CameraSingleton(){}

    public static T265CameraSingleton getInstance(){
        return instance;
    }

    private double robotX;
    private double robotY;

    public void start() {}

    public void stop() {}

    public synchronized T265Camera.CameraUpdate getLastReceivedCameraUpdate() {
        return new T265Camera.CameraUpdate(new T265Camera.Pose2d(new T265Camera.Transform2d(robotX, robotY),
                                                                 new T265Camera.Rotation2d()),
                                           new T265Camera.ChassisSpeeds(),
                                           T265Camera.PoseConfidence.High);
    }

    public synchronized void updateCamera(double x, double y) {
        this.robotX = x;
        this.robotY = y;
    }
}