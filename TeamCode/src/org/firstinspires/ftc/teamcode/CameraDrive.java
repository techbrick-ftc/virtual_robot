package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.T265Camera;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Math.abs;

public class CameraDrive {
    private DcMotor[] motors;
    private double[] motorSpeeds;
    private double[] angles;
    private T265Camera camera;
    private T265Camera.Translation2d translation2d;
    private BNO055IMU imu;
    private Telemetry telemetry;

    public void setUp(DcMotor[] motors, double[] angles, T265Camera camera, BNO055IMU imu) {
        setUp(motors, angles, camera, imu, null);
    }

    public void setUp(DcMotor[] motors, double[] angles, T265Camera camera, BNO055IMU imu, Telemetry telemetry) {
        if (motors.length != angles.length) {
            throw new RuntimeException("Motor array length and angle array length are not the same! Check your code!");
        }
        this.motors = motors;
        this.motorSpeeds = new double[motors.length];
        this.angles = angles;
        this.camera = camera;
        this.imu = imu;
        this.telemetry = telemetry;
    }

    public void goToPosition(double moveX, double moveY, TeleAuto callback) {
        goToPosition(moveX, moveY, 1, callback);
    }

    public void goToPosition(double moveX, double moveY, double speed, TeleAuto callback) {
        speed = Math.max(0.2, Math.min(speed, 0.3));

        boolean xComplete = false;
        boolean yComplete = false;

        while (callback.opModeIsActive()) {
            T265Camera.CameraUpdate up = camera.getLastReceivedCameraUpdate();

            this.translation2d = up.pose.getTranslation();

            double currentX = this.translation2d.getX();
            double currentY = this.translation2d.getY();

            double deltaX = xComplete ? 0 : moveX - currentX;
            double deltaY = yComplete ? 0 : moveY - currentY;

            xComplete = abs(deltaX) < 0.2;
            yComplete = abs(deltaY) < 0.2;

            if (xComplete && yComplete) {
                stopWheel();
                break;
            }

            double theta = Math.atan2(deltaX, deltaY);

            for (int i = 0; i < motors.length; i++) {
                motors[i].setPower(Math.sin(angles[i] - theta) * speed);
                motorSpeeds[i] = Math.sin(angles[i] - theta) * speed;
            }

            if (telemetry != null) {
                writeTelemetry(moveX, moveY, deltaX, deltaY, xComplete, yComplete);
            }
        }
    }

    private void stopWheel() {
        for (DcMotor motor : motors) {
            motor.setPower(0);
        }
    }

    private void writeTelemetry(double moveX, double moveY, double deltaX, double deltaY, boolean xComplete, boolean yComplete) {
        telemetry.addData("Current X", this.translation2d.getX());
        telemetry.addData("Current Y", this.translation2d.getY());
        telemetry.addData("Move X", moveX);
        telemetry.addData("Move Y", moveY);
        telemetry.addData("Delta X", deltaX);
        telemetry.addData("Delta Y", deltaY);
        telemetry.addData("X Complete", xComplete);
        telemetry.addData("Y Complete", yComplete);
        telemetry.update();
    }
}

interface TeleAuto {
    boolean opModeIsActive();
}
