package dk.itu.kf04.g4tw;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import dk.itu.kf04.g4tw.model.tree.*;

/**
 * The main test entry-point and the overall TestSuite for the project.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { RBTreeTest.class, RBNodeTest.class } )
public class MainTest {}