package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/*
 * simple little drive
 * isn't it cute?
 */

@TeleOp(name="Basic Drive", group="Mechanum")
public class BasicDrive extends LinearOpMode{

    // defines motors and runtime
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor rl = null;
    private DcMotor rr = null;

    @Override
    public void runOpMode() {

        // adds start telemetry
        telemetry.addData("status", "initialized");
        telemetry.update();

        // configures hardware
        fl = hardwareMap.get(DcMotor.class, "front_left_motor");
        fr = hardwareMap.get(DcMotor.class, "front_right_motor");
        rl = hardwareMap.get(DcMotor.class, "back_left_motor");
        rr = hardwareMap.get(DcMotor.class, "back_right_motor");

        // configures motor directions
        fl.setDirection(DcMotor.Direction.FORWARD);
        rl.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        // waits for start
        waitForStart();
        runtime.reset();

        // after start is pressed
        while(opModeIsActive()) {


            // sets up power variables
            double flPower;
            double frPower;
            double rlPower;
            double rrPower;

            // drive logic
            double drive = gamepad1.left_stick_y;
            double strafe = -gamepad1.left_stick_x;
            double rotate = -gamepad1.right_stick_x;

            flPower = Range.clip(drive + strafe + rotate, -1.0, 1.0);
            frPower = Range.clip(drive - strafe - rotate, -1.0, 1.0);
            rlPower = Range.clip(drive - strafe + rotate, -1.0, 1.0);
            rrPower = Range.clip(drive + strafe - rotate, -1.0, 1.0);

            fl.setPower(flPower);
            fr.setPower(frPower);
            rl.setPower(rlPower);
            rr.setPower(rrPower);

            // telemetry
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "flPower (%.2f), frPower (%.2f)", flPower, frPower);
            telemetry.addData("Motors", "rlPower (%.2f), rrPower (%.2f)", rlPower, rrPower);
            telemetry.update();
        }

    }
}
