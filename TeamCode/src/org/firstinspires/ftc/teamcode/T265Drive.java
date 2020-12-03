package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.T265Camera;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name = "T265Drive", group = "autonomous")
public class T265Drive extends LinearOpMode {

    // Variables
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor rl = null;
    private DcMotor rr = null;

    private BNO055IMU imu;

    private static T265Camera slamra = null;

    @Override
    public void runOpMode() {

        // Configure Hardware
        fl = hardwareMap.get(DcMotor.class, "front_left_motor");
        fr = hardwareMap.get(DcMotor.class, "front_right_motor");
        rl = hardwareMap.get(DcMotor.class, "back_left_motor");
        rr = hardwareMap.get(DcMotor.class, "back_right_motor");

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(new BNO055IMU.Parameters());

        slamra = new T265Camera(new T265Camera.Translation2d(), 0.1, hardwareMap.appContext);

        fl.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        telemetry.addData("Status", "Operational");
        telemetry.update();

        // Beginning of Autonomous
        DriveToPosition(-80, 40, 0, .5);
    }

    // Configures Drive Commands
    void Stop() {
        fl.setPower(0);
        fr.setPower(0);
        rl.setPower(0);
        rr.setPower(0);
    }

    void Drive(double power, int ticks) {
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setTargetPosition(ticks);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setTargetPosition(ticks);
        rl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rl.setTargetPosition(ticks);
        rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setTargetPosition(ticks);
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(power);
        rr.setPower(power);
        while (opModeIsActive()) {
            if (fl.getCurrentPosition() >= ticks) {
                break;
            }
        }
    }

    void Strafe(double power, int ticks) {
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setTargetPosition(ticks);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setTargetPosition(ticks);
        rl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rl.setTargetPosition(ticks);
        rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rr.setTargetPosition(ticks);
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(-power);
        rr.setPower(-power);
        while (opModeIsActive()) {
            if (fl.getCurrentPosition() >= ticks) {
                break;
            }
        }
    }

    void Rotate(double power, int value) {
        Orientation orientation;
        orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double target = orientation.firstAngle + value;
        if (value > 0) {
            fl.setPower(-power);
            fr.setPower(power);
            rl.setPower(-power);
            rr.setPower(power);
            while (opModeIsActive()) {
                Orientation current;
                current = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                if (current.firstAngle >= target) {
                    break;
                }
            }
        } else {
            fl.setPower(power);
            fr.setPower(-power);
            rl.setPower(power);
            rr.setPower(-power);
            while (opModeIsActive()) {
                Orientation current;
                current = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                if (current.firstAngle <= target) {
                    break;
                }
            }
        }
    }

    void DriveToPosition(double x, double y, double angle, double power) {
        T265Camera.CameraUpdate update = slamra.getLastReceivedCameraUpdate();
        double diffX = x - update.pose.getTranslation().getX();
        double diffY = y - update.pose.getTranslation().getY();
        double flPower, rlPower, frPower, rrPower;

        System.out.println("diffX:     " + diffX);
        System.out.println("diffY:     " + diffY);

        while (opModeIsActive() && (diffX > 1 || diffY > 1)) {
            System.out.println("LOOPING");
            update = slamra.getLastReceivedCameraUpdate();
            diffX = x - update.pose.getTranslation().getX();
            diffY = y - update.pose.getTranslation().getY();

            telemetry.addData("X", update.pose.getTranslation().getX());
            telemetry.addData("Y", update.pose.getTranslation().getY());
            telemetry.addData("diffX", diffX);
            telemetry.addData("diffY", diffY);
            telemetry.update();
            System.out.println("diffX:     " + diffX);
            System.out.println("diffY:     " + diffY);

            flPower = diffY + diffX;
            rlPower = diffY - diffX;
            frPower = diffY - diffX;
            rrPower = diffY + diffX;

            // Find the largest power
            double max = 0;
            max = Math.max(Math.abs(flPower), Math.abs(rlPower));
            max = Math.max(Math.abs(frPower), max);
            max = Math.max(Math.abs(rrPower), max);

            // Divide everything by max (it's positive so we don't need to worry
            // about signs)
            flPower /= max;
            rlPower /= max;
            frPower /= max;
            rrPower /= max;

            fl.setPower(flPower * power);
            rl.setPower(rlPower * power);
            fr.setPower(frPower * power);
            rr.setPower(rrPower * power);
        }

        Stop();
    }
}
