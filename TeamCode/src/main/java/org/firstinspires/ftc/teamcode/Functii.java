package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
        F
    4      2
    3      1
 */


public abstract class Functii extends Vuforia{

    public DcMotor Motor1 = null;
    public DcMotor Motor2 = null;
    public DcMotor Motor3 = null;
    public DcMotor Motor4 = null;
    public DcMotor HexMotor = null;
    public CRServo Servo = null;
    public CRServo Mascota = null;
    BNO055IMU Gyro = null;

    public void Initialize()
    {
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor4 = hardwareMap.dcMotor.get("Motor4");
        HexMotor = hardwareMap.dcMotor.get("HexMotor");
        Servo = hardwareMap.crservo.get("Servo");
        Mascota = hardwareMap.crservo.get("Mascota");

        /** Initializarea senzorului integrat*/
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "Calibrare1.json";       /**Trebuie chimbat cu denumirea filei*/
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        Gyro = hardwareMap.get(BNO055IMU.class, "imu2");
        Gyro.initialize(parameters);
        /**------------------------------------------------------*/


        telemetry.addData("merge", 100);

        VuforiaInitialize();
        TensorFlowInitialize();

    }
    public void StartV_T()
    {
        navigationTarget.activate();
        tfod.activate();
    }
    public void Down(double power,double time)
    {
        HexMotor.setPower(power);
        sleep((long)time*1000);
        HexMotor.setPower(0);
    }
    public void StopMotors()
    {
        Motor1.setPower(0.00);
        Motor2.setPower(0.00);
        Motor3.setPower(0.00);
        Motor4.setPower(0.00);
    }
    public void MoveFront(double power, double time)
    {
        Motor1.setPower(power);
        Motor2.setPower(power);
        Motor3.setPower(-power);
        Motor4.setPower(-power);
        sleep((long)(time * 1000));
        if(time != 0)
            StopMotors();
    }
    public void MoveBack(double power, double time)
    {
        Motor1.setPower(-power);
        Motor2.setPower(-power);
        Motor3.setPower(power);
        Motor4.setPower(power);
        sleep((long)(time * 1000));
        if(time != 0)
            StopMotors();
    }
    public void MoveRight(double power, double time)
    {
        Motor1.setPower(power);
        Motor2.setPower(-power);
        Motor3.setPower(power);
        Motor4.setPower(-power);
        sleep((long)(time * 1000));
        if(time != 0)
            StopMotors();
    }
    public void MoveLeft(double power, double time)
    {
        Motor1.setPower(1.05 * -power);
        Motor2.setPower(power);
        Motor3.setPower(-power);
        Motor4.setPower(1.05 * power);
        sleep((long)(time * 1000));
        if(time != 0)
            StopMotors();
    }
    public void RotateLeft(double power, double time)
    {
        Motor1.setPower(-power);
        Motor2.setPower(-power);
        Motor3.setPower(-power);
        Motor4.setPower(-power);
        sleep((long)(time * 1000));
        if(time != 0)
            StopMotors();
    }
    public void RotateRight(double power, double time)
    {
        Motor1.setPower(power);
        Motor2.setPower(power);
        Motor3.setPower(power);
        Motor4.setPower(power);
        sleep((long)(time * 1000));
        if(time != 0)
            StopMotors();
    }
    public void Rotate(double position)
    {
        int direction;
        double angle, IntPos;
        IntPos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
        angle = IntPos - position;
        if(angle < 0) angle  = -angle;
        if(IntPos  <= position)
        {
            while(IntPos <= position)
            {
                IntPos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                RotateLeft(0.13,0);
            }
        }
        else
        {
            while(IntPos >= position) {
                IntPos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                RotateRight(0.13, 0);
            }
        }
        StopMotors();
        /*int direction;
        double angle;
        //InitDirection += 180;
        position =  position + InitDirection;
        if(position > 360) position -= 360;
        if(InitDirection < 180)
        {
            if(position >= 180 + InitDirection)
            {
                direction = 1;   //left
                angle = 360 - position + InitDirection;
            }
            else if(position < InitDirection )
            {
                direction = 1;   //left
                angle =  InitDirection - position;
            }
            else
            {
                direction = 0;  //right
                angle = position - InitDirection;
            }

        }
        else
        {
            if(position <= InitDirection - 180)
            {
                direction = 0; //right
                angle = 360 - InitDirection + position;
            }
            else if(position > InitDirection )
            {
                direction = 0; //right;
                angle = position - InitDirection;
            }
            else
            {
                direction = 1; //left
                angle = InitDirection - position;
            }
            double power,error;

            if(angle < 20)
            {
                power = 0.1;
                error = 2;
            }
            else if(angle < 60)
            {
                power = 0.25;
                error = 3;
            }
            else
            {
                power = 0.4;
                error = 5;
            }

            if(direction == 0)
            {
                double End = position - error;
                double current_pos;
                int trecut = 1;
                double last_pos = 0;
                if(End < 0) End  += 360;
                current_pos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                if(current_pos < position )
                {
                    while(( current_pos <= End))
                    {
                        current_pos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                        RotateRight(power, 0);
                    }
                }
                 else
                {
                    while(current_pos - 360 * trecut <= End)
                    {
                        if (last_pos > current_pos)
                            trecut = 0;
                        last_pos = current_pos;
                        current_pos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                    }

                }
            }
            else
            {
                double End = position + error;
                double current_pos;
                int trecut = 1;
                double last_pos = 0;
                if(End > 360 ) End  -= 360;
                current_pos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                if(current_pos < position )
                {
                    while(( current_pos >= End))
                    {
                        current_pos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                        RotateRight(power, 0);
                    }
                }
                else
                {
                    while(current_pos + 360 * trecut >= End)
                    {
                        if (last_pos < current_pos)
                            trecut = 0;
                        last_pos = current_pos;
                        current_pos = Gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + 180;
                    }

                }
            }
        }
        StopMotors();
        */
    }

}