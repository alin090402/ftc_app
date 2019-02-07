package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

public abstract class Vuforia extends LinearOpMode {

    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    TFObjectDetector tfod;
    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    List<Recognition> updatedRecognitions;
    VuforiaTrackables navigationTarget = null;
    public void VuforiaInitialize()
    {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ab9Ie/j/////AAABmTTj8bKoIUztnX5OHdJBSYt27p1WgIIkTqRTx4gUz24wlvSVc9Cmz1CbMyA7c0V34n1CFa9EZJuAODJhijgrKtsrCcVcVET+az9lPgdkGMU7AydeiNdRZBWSW9eMrgUg50RJpUp1S0qCrhOLl3Io9I1/92nrhOx8cVTZ54sCnGA8vCr38Z6Ko/h0ZABlgvXlowNswAHQg3BZzutSryb2l0YZ4ei9e/2aDi41FYcAK6Kf1lBGc8Xdrnk0w/LJXx1cz13R3m08KjwhaAhciXgz1qHvqaXD0wZVEgx+OjAlKfFNT2TAxk6lea6o4eb5Of9cQHDYS+fuUbLLQbioHh+KEmpJaRHnSxHZ2JffRp0yP/ju";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        navigationTarget = this.vuforia.loadTrackablesFromAsset("NavigationTarget");

        VuforiaTrackable IM1 = navigationTarget.get(0);
        IM1.setName("Luna");
        VuforiaTrackable IM2 = navigationTarget.get(1);
        IM1.setName("Pamant");
        VuforiaTrackable IM3 = navigationTarget.get(2);
        IM1.setName("Robot");
        VuforiaTrackable IM4 = navigationTarget.get(3);
        IM1.setName("Galaxie");

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(navigationTarget);

        float mPerInch        = 0.0254f;
        float mFieldWidth     = 3f - 0.002f;
        float mRobotWidth     = 0.45f;
        float mImageHeight    = 7.25f * 2f * mPerInch;

                /**Red Alliance*/
        OpenGLMatrix IM1Location = OpenGLMatrix
                .translation(-mFieldWidth / 2, 0, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ,
                        AngleUnit.DEGREES,90,90, 0));
        IM1.setLocation(IM1Location);
                 /**Front*/
        OpenGLMatrix IM2Location = OpenGLMatrix
                .translation(0, mFieldWidth / 2, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 0, 0));
        IM2.setLocation(IM2Location);
                /**Blue Alliance*/
        OpenGLMatrix IM3Location = OpenGLMatrix
                .translation( mFieldWidth / 2,0,0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ,
                        AngleUnit.DEGREES, 90,-90,0));
        IM3.setLocation(IM3Location);
                /***Back*/
        OpenGLMatrix IM4Location = OpenGLMatrix
                .translation(0,-mFieldWidth / 2,0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ,
                        AngleUnit.DEGREES, -90,0,0));
        IM4.setLocation(IM4Location);

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(mRobotWidth/2,0,0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.YZY,
                        AngleUnit.DEGREES, -90, 0, 0));
        ((VuforiaTrackableDefaultListener)IM1.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)IM2.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)IM3.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)IM4.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
    }
    public void TensorFlowInitialize()
    {

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
