package org.usfirst.frc.team2984.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.usfirst.frc.team2984.robot.RobotMap;
import org.usfirst.frc.team2984.robot.commands.AlignToThePeg;
import org.usfirst.frc.team2984.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2984.robot.subsystems.Gyroscope;
import org.usfirst.frc.team2984.robot.util.MathUtil;
import org.usfirst.frc.team2984.robot.util.VisionTarget;
import org.usfirst.frc.team2984.robot.util.VisionTracker;
import org.usfirst.frc.team2984.util.DummyReporter;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class AlignToThePegTest {
	private AlignToThePeg command;
	
	private VisionTracker tracker;
	private DriveTrain driveTrain;
	private Gyroscope gyro;
	
	@Before
	public void before() {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
		
		tracker = mock(VisionTracker.class);
		driveTrain = mock(DriveTrain.class);
		gyro = mock(Gyroscope.class);
		command = new AlignToThePeg(tracker, driveTrain, gyro);
		RobotMap.pegAngle = 120;
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffStaightOnAndFarAway() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double angle = Math.toDegrees(invertCircleOffset(Math.asin(xOff/distance)));
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.atan(RobotMap.TARGET_DIMENSION.height/distance);
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-30D);
		
		command.execute();
		assertMotionAtAngle(30, 1, 0);
	}
	
	@Test
	public void testMotionGivenStaightOnAndFarAway() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double angle = Math.toDegrees(invertCircleOffset(Math.asin(xOff/distance)));
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.atan(RobotMap.TARGET_DIMENSION.height/distance);
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-60D);
		
		command.execute();
		assertMotionAtAngle(-60, 1, 0);
	}
	
	@Test
	public void testMotionGivenStaightOnAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = RobotMap.DOCKING_DISTANCE_THRESHOLD-1;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double angleOff = Math.asin(xOff/distance);
		double angle = Math.toDegrees(invertCircleOffset(angleOff));
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-60D);
		
		command.execute();
		assertMotionAtAngle(0, 0, 0);
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = RobotMap.DOCKING_DISTANCE_THRESHOLD-1;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double angle = Math.toDegrees(invertCircleOffset(Math.asin(xOff/distance)));
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-30D);
		
		command.execute();
		assertMotionAtAngle(30, 1, 0);
	}
	
	@Test
	public void testLocalMethod(){
		double start = Math.PI/3;
		double ten = Math.PI/18;
		double end = this.invertCircleOffset(start);

		double mid = MathUtil.yawFromRotatedCircle(ten, end);
		assertEquals(start,  mid, 0.00001);
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffOverAndTooClose() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = RobotMap.DOCKING_DISTANCE_THRESHOLD-1;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double angle = Math.toDegrees(invertCircleOffset(Math.asin(xOff/distance)));
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-90D);
		
		command.execute();
		assertMotionAtAngle(30, -1, 0);
	}
	
	@Test
	public void testMotionGivenThityDegreeseOffOver() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance) - Math.toRadians(RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD+1));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-90D);
		
		command.execute();
		assertMotionAtAngle(30, -1, -(RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD+1)*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR);
	}
	
	@Test
	public void testMotionGivenTwentyDegreeseOffUnder() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance) + Math.toRadians(RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD+1));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-40D);
		
		command.execute();
		assertMotionAtAngle(30, 1, (RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD+1)*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR);
	}
	
	@Test
	public void testMotionGivenTwoDegreeseOffUnder() {
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance) + Math.toRadians(RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD+1));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-69+RobotMap.DOCKING_YAW_THRESHOLD);
		
		command.execute();
		assertMotionAtAngle(-60, 1, (RobotMap.DOCKING_ROBOT_ANGLE_THRESHOLD+1)*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR);
	}
	
	@Test
	public void testMotionAtOnehunderedEightyDegreeseStaightOnAndFarAway() {
		RobotMap.pegAngle = 180;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(0D);
		
		command.execute();
		assertMotionAtAngle(0, 1, 0);
	}
	
	@Test
	public void testMotionAtTwohunderedAndFortyDegreeseStaightOnAndFarAway() {
		RobotMap.pegAngle = 240;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(60D);
		
		command.execute();
		assertMotionAtAngle(60, 1, 0);
	}
	
	@Test
	public void testMotionAtOnehunderedEightyDegreeseGyroThirtyOffVisionStaightOnAndFarAway() {
		RobotMap.pegAngle = 180;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(30D);
		
		command.execute();
		assertMotionAtAngle(90, 1, 0);
	}
	
	@Test
	public void testMotionAtOnehunderedEightyDegreeseGyroNegativeThirtyOffVisionStaightOnAndFarAway() {
		RobotMap.pegAngle = 180;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-30D);
		
		command.execute();
		assertMotionAtAngle(90, -1, 0);
	}
	
	@Test
	public void testMotionAtOnehunderedEightyDegreeseGyroStaightOnVisionFifteenAndFarAway() {
		RobotMap.pegAngle = 180;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance) + Math.PI/12);
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(0D);
		
		command.execute();
		assertMotionAtAngle(90, 15*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR, 15*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR);
	}
	
	@Test
	public void testMotionAtOnehunderedEightyDegreeseGyroStaightOnVisionTwentyAndFarAway() {
		RobotMap.pegAngle = 180;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance) + Math.PI/9);
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(0D);
		
		command.execute();
		assertMotionAtAngle(90, 20*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR, 20*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR);
	}
	
	@Test
	public void testMotionAtOnehunderedEightyDegreeseGyroNegativeFifteenVisionFifteenAndFarAway() {
		RobotMap.pegAngle = 180;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 100;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance) + Math.PI/12);
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-15D);
		
		command.execute();
		assertMotionAtAngle(0, 1, 15*RobotMap.ROBOT_ANGLE_PROPORIONAL_SCALAR);
	}
	
	@Test
	public void testMotionAtTwohunderedFortyDegreeseGyroNegativeOneVisionStaightOnAndClose() {
		RobotMap.pegAngle = 240;
		when(tracker.hasTrack()).thenReturn(true);
		double distance = 33;
		double xOff = -RobotMap.CAMERA_OFFSET;
		double inverseAngle = invertCircleOffset(Math.asin(xOff/distance));
		double angle = Math.toDegrees(inverseAngle);
		double inputAngle = angle / RobotMap.CAMERA_FOV.width * RobotMap.CAMERA_RESOLUTION.width;
		double inputHeight = RobotMap.CAMERA_RESOLUTION.height/RobotMap.CAMERA_FOV.height*Math.toDegrees(Math.atan(RobotMap.TARGET_DIMENSION.height/distance));
		when(tracker.getTarget()).thenReturn(new VisionTarget(inputAngle,0,inputHeight));
		when(gyro.getAngle()).thenReturn(-1D);
		
		command.execute();
		assertMotionAtAngle(150, -1, 0);
	}
	
	private double invertCircleOffset(double wanted){
		double alpha = Math.cos(Math.toRadians(RobotMap.CAMERA_ANGLE));
		double beta = Math.tan(Math.abs(wanted));
		return Math.copySign(Math.atan(alpha * beta), wanted);
	}
	
	private void assertMotionAtAngle(double angle, double speed, double rotation){
		ArgumentCaptor<Double> angleCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> speedCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> rotationCaptor = ArgumentCaptor.forClass(Double.class);
		verify(driveTrain).moveAtAngle(angleCaptor.capture(), speedCaptor.capture(), rotationCaptor.capture());
		assertEquals(angle, angleCaptor.getValue(), 0.00001);
		assertEquals(speed, speedCaptor.getValue(), 0.00001);
		assertEquals(rotation, rotationCaptor.getValue(), 0.00001);

	}
	
}