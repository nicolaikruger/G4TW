package dk.itu.kf04.g4tw;

import dk.itu.kf04.g4tw.model.AddressParserTest;
import dk.itu.kf04.g4tw.model.MapModelTest;
import dk.itu.kf04.g4tw.model.ModelSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The main test entry-point and the overall TestSuite for the project.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {ModelSuite.class} )
public class MainTest {

}
