package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.model.tree.TreeSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests the classes in the Model.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {
        AddressParserTest.class,
        DijkstraSPTest.class,
        MapModelTest.class,
        RoadTest.class,
        TreeSuite.class
} )
public class ModelSuite {}
