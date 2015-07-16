package ravensproject;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;

import ravensproject.models.RavenFigureLevel.RFTransformation;
import ravensproject.models.RavenFigureLevel.ROMatcher;
import ravensproject.models.RavenObjectLevel.CorrespondingRO;
import ravensproject.models.RavenObjectLevel.ROTransformationInterface;

import java.util.Map;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
        
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {

        //System.out.print("solving problem: " + problem.getName());
        final long startTime = System.nanoTime();

        /*
        Map<String, RavensFigure> figuresMap = problem.getFigures();

        RavensFigure figureA = figuresMap.get("A");
        RavensFigure figureB = figuresMap.get("B");
        System.out.println("identying the matched RavensObject between Figures A and B: ");
        ROMatcher match = new ROMatcher(figureA, figureB);

        */

        /*
        for (CorrespondingRO cr : match.getMatchedROs()) {
            System.out.println("matched: in Figure A: " + cr.getRavensObject1().getName() + " --- in Figure B:"
                    + cr.getRavensObject2().getName());
            System.out.println("************************************");
        }
        */

        /*
        RFTransformation rfTransformation = new RFTransformation(figureA, figureB);

        for (ROTransformationInterface roTransformation : rfTransformation.compileROTransformationsInMatchedObjects()) {
            System.out.println("attributes transformed: " + roTransformation.getAttributeKeyName());
        }

        System.out.println("compiled Transformations of these two figures: " + rfTransformation.compileROTransformationsInMatchedObjects().size());

        System.out.println("----------------next RPM-----------------------------------------");

        */

        /*
        for (Map.Entry<String, RavensFigure> entry : figuresMap.entrySet()) {
            System.out.println("Figure name: " + entry.getKey());
        }
        */

        AgentDelegate delegate = new AgentDelegate(problem);
        delegate.solve();
        final long duration = System.nanoTime() - startTime;
        //System.out.println(", time used: " + ((double)duration/1000000) + " ms");
        return delegate.answerChoice;

//        return -1;
    }
}
