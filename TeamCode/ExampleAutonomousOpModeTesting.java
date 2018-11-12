package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
Thanks Team Q #5270
 */

//DECLARE TELEOP
@TeleOp(name="Teleop Driving", group="Linear Opmode")  // @Autonomous(...) is the other common choice
public class ExampleAutonomousOpModeTesting extends LinearOpMode {

    //TIMEOUT
    private int timeout = 0; //Timeout flag, to make sure that inversion does not happen over and over

    //LIMITING FLAG FOR SPEED
    private int speedLimitingFlag = 2; //The code DIVIDES by this

    //INVERSION
    private int inverted = 1; //Inversion flag, to make the motors run inverted on the press of a button

    //TIMER
    private ElapsedTime runtime = new ElapsedTime(); //Timer

    //DRIVING
    private DcMotor leftMotorDriving = null; //This is the left driving motor
    private DcMotor rightMotorDriving = null; //This is the right driving motor

    //CAPPING
    private DcMotor capBallMotor1 = null; //This is one of the cap ball motors
    private DcMotor capBallMotor2 = null; //This is one of the cap ball motors
    private double leftServoCapBallMin = 0.5d;
    private double rightServoCapBallMin = 0.5d;
    private float cappingSpeed = 1.00f; //This is the speed at which the capping motors will move



    //SHOOTING
    private DcMotor intakeMotorLifter = null;
    private float intakePower = 0.1f;



    @Override
    public void runOpMode() throws InterruptedException {

        //DECLARE TELEMETRY
        telemetry.addData("Status", "Program Initialized");
        telemetry.update();

        //DRIVING MOTORS
        //set variables to motors
        leftMotorDriving  = hardwareMap.dcMotor.get("left motor"); //This is the left driving motor
        rightMotorDriving = hardwareMap.dcMotor.get("right motor"); //This is the right driving motor
        //set variables to mode
        leftMotorDriving.setDirection((DcMotor.Direction.FORWARD)); //This is the left motor driving being set to Forwards mode
        rightMotorDriving.setDirection((DcMotor.Direction.FORWARD)); //This is the right driving motor being set to Forwards mode

        //CAPPING MOTORS
        //set up capping motors
        capBallMotor1 = hardwareMap.dcMotor.get("cap ball motor 1"); //This is a capball motor
        capBallMotor2 = hardwareMap.dcMotor.get("cap ball motor 2"); //This is a capball motor
        //set variables to mode
        capBallMotor1.setDirection((DcMotor.Direction.REVERSE)); //This is the first capball motor being set to forwards mode
        capBallMotor2.setDirection((DcMotor.Direction.FORWARD)); //This is the second capball motor being set to forwards mode



        intakeMotorLifter = hardwareMap.dcMotor.get("ball shooter lifter motor");
        intakeMotorLifter.setDirection(DcMotorSimple.Direction.FORWARD);


        //OPMODE CODE
        waitForStart();  //Wait for the game to start
        runtime.reset(); //Reset the timer before it is supposed to start


        //OPMODE RUNNING
        while (opModeIsActive()) { //While opmode is running

            //DRIVING CONTROL CODE
            //This will make the robot move
            leftMotorDriving.setPower((-inverted*gamepad1.left_stick_y)/speedLimitingFlag);  //Set the left motor's power to be that of the gamepad's left stick
            rightMotorDriving.setPower((inverted*gamepad1.right_stick_y)/speedLimitingFlag); //Set the right motor's power to be that of the gamepad's right stick
            telemetry.addData("Left motor: ", -gamepad1.left_stick_y);
            telemetry.addData("Right motor: ", gamepad1.right_stick_y);
            telemetry.update();

            //Toggle inversion if the invert button is pushed and the timeout is in the area that it needs to be in so that it can not be spammed.
            if (gamepad1.x&&timeout>=75) {         //If the inversion button is pushed
                inverted = -inverted; //make the inverted flag the oppposite of the inversion flag. INVERTS
                timeout=0; //This resets the timeout
            }
            //This line increments the timeout for the inversion mode
            timeout++;


            //BALLCAPPING CONTROL CODE
            //capBAll

            if(gamepad2.a){ //if the a button is pressed, move the cap lift
                capBallMotor1.setPower(cappingSpeed);
                capBallMotor2.setPower(cappingSpeed);
            }else if(gamepad2.b){ //if the b button is pressed, do the opposite

                //if (gamepad2.b) {
                capBallMotor1.setPower(-cappingSpeed);
                capBallMotor2.setPower(-cappingSpeed);
            }else{ //Dont do anything.
                capBallMotor1.setPower(0);
                capBallMotor2.setPower(0);
            }



            //BALL SHOOTER CODE
            //move the lifter
            if (gamepad1.dpad_down) {
                intakeMotorLifter.setPower(intakePower);
            }
            else if (gamepad1.dpad_up) {
                intakeMotorLifter.setPower(-intakePower);
            }
            else {
                intakeMotorLifter.setPower(0);
            }


            //OPMODE DEFAULT CODE
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
            //Datalog
            telemetry.update();                                             //Pushes to terminal
        }
    }
}