package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.controller.ControllerSuite;
import dk.itu.kf04.g4tw.model.ModelSuite;
import dk.itu.kf04.g4tw.util.UtilSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The main test entry-point and the overall TestSuite for the project.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {ControllerSuite.class, ModelSuite.class, UtilSuite.class} )
public class MainTest {}
