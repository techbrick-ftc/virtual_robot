package virtual_robot.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class VirtualGamePadController {

    @FXML StackPane joyStickLeftPane;
    @FXML StackPane joyStickRightPane;
    @FXML StackPane triggerLeftPane;
    @FXML StackPane triggerRightPane;
    @FXML Circle joyStickLeftHandle;
    @FXML Circle joyStickRightHandle;
    @FXML Circle triggerLeftHandle;
    @FXML Circle triggerRightHandle;
    @FXML Button btnX;
    @FXML Button btnY;
    @FXML Button btnA;
    @FXML Button btnB;
    @FXML Button btnLB;
    @FXML Button btnRB;
    @FXML Button btnU;
    @FXML Button btnL;
    @FXML Button btnR;
    @FXML Button btnD;

    volatile float left_stick_x = 0;
    volatile float left_stick_y = 0;
    volatile float right_stick_x = 0;
    volatile float right_stick_y = 0;
    volatile float left_trigger = 0;
    volatile float right_trigger = 0;
    volatile boolean xPressed = false;
    volatile boolean yPressed = false;
    volatile boolean aPressed = false;
    volatile boolean bPressed = false;
    volatile boolean lbPressed = false;
    volatile boolean rbPressed = false;
    volatile boolean uPressed = false;
    volatile boolean lPressed = false;
    volatile boolean rPressed = false;
    volatile boolean dPressed = false;

    VirtualRobotController virtualRobotController = null;

    void setVirtualRobotController(VirtualRobotController vrController){
        virtualRobotController = vrController;
    }

    @FXML
    private void handleJoystickDrag(MouseEvent arg){
        if (!virtualRobotController.getOpModeInitialized()) return;
        float x = (float)Math.max(10, Math.min(110, arg.getX()));
        float y = (float)Math.max(10, Math.min(110, arg.getY()));
        float triggerY = (float)Math.max(10, Math.min(110, arg.getY()));
        if (arg.getSource() == joyStickLeftPane){
            joyStickLeftHandle.setTranslateX(x-10);
            joyStickLeftHandle.setTranslateY(y-10);
            left_stick_x = (x - 60.0f) / 50.0f;
            left_stick_y = (y - 60.0f) / 50.0f;
        }
        else if (arg.getSource() == joyStickRightPane){
            joyStickRightHandle.setTranslateX(x-10);
            joyStickRightHandle.setTranslateY(y-10);
            right_stick_x = (x - 60.0f) / 50.0f;
            right_stick_y = (y - 60.0f) / 50.0f;
        } else if (arg.getSource() == triggerLeftPane) {
            triggerLeftHandle.setTranslateY(y-10);
            left_trigger = Math.abs((triggerY - 110) / 100);
        } else if (arg.getSource() == triggerRightPane) {
            triggerRightHandle.setTranslateY(y-10);
            right_trigger = Math.abs((triggerY - 110) / 100);
        }
    }

    @FXML
    private void handleGamePadButtonMouseEvent(MouseEvent arg){
        if (!virtualRobotController.getOpModeInitialized()) return;
        Button btn = (Button)arg.getSource();
        boolean result;

        if (arg.getEventType() == MouseEvent.MOUSE_EXITED || arg.getEventType() == MouseEvent.MOUSE_RELEASED) result = false;
        else if (arg.getEventType() == MouseEvent.MOUSE_PRESSED) result = true;
        else return;

        if (btn == btnX) xPressed = result;
        else if (btn == btnY) yPressed = result;
        else if (btn == btnA) aPressed = result;
        else if (btn == btnB) bPressed = result;
        else if (btn == btnLB) lbPressed = result;
        else if (btn == btnRB) rbPressed = result;
        else if (btn == btnU) uPressed = result;
        else if (btn == btnL) lPressed = result;
        else if (btn == btnR) rPressed = result;
        else if (btn == btnD) dPressed = result;
    }

    void resetGamePad(){
        left_stick_y = 0;
        left_stick_x = 0;
        right_stick_x = 0;
        right_stick_y = 0;
        left_trigger = 0;
        right_trigger = 0;
        aPressed = false;
        bPressed = false;
        xPressed = false;
        yPressed = false;
        lbPressed = false;
        rbPressed = false;
        uPressed = false;
        lPressed = false;
        rPressed = false;
        dPressed = false;
        joyStickLeftHandle.setTranslateX(50);
        joyStickLeftHandle.setTranslateY(50);
        joyStickRightHandle.setTranslateX(50);
        joyStickRightHandle.setTranslateY(50);
        triggerLeftHandle.setTranslateY(100);
        triggerRightHandle.setTranslateY(100);
    }

    public class ControllerState {

        public final float leftStickX;
        public final float leftStickY;
        public final float rightStickX;
        public final float rightStickY;
        public final float leftTrigger;
        public final float rightTrigger;
        public final boolean a;
        public final boolean b;
        public final boolean x;
        public final boolean y;
        public final boolean lb;
        public final boolean rb;
        public final boolean u;
        public final boolean l;
        public final boolean r;
        public final boolean d;

        public ControllerState(){
            leftStickX = VirtualGamePadController.this.left_stick_x;
            leftStickY = VirtualGamePadController.this.left_stick_y;
            rightStickX = VirtualGamePadController.this.right_stick_x;
            rightStickY = VirtualGamePadController.this.right_stick_y;
            leftTrigger = VirtualGamePadController.this.left_trigger;
            rightTrigger = VirtualGamePadController.this.right_trigger;
            a = VirtualGamePadController.this.aPressed;
            b = VirtualGamePadController.this.bPressed;
            x = VirtualGamePadController.this.xPressed;
            y = VirtualGamePadController.this.yPressed;
            lb = VirtualGamePadController.this.lbPressed;
            rb = VirtualGamePadController.this.rbPressed;
            u = VirtualGamePadController.this.uPressed;
            l = VirtualGamePadController.this.lPressed;
            r = VirtualGamePadController.this.rPressed;
            d = VirtualGamePadController.this.dPressed;
        }
    }

    ControllerState getState(){
        return new ControllerState();
    }

}
