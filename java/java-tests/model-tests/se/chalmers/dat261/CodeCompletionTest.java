/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.chalmers.dat261;

import junit.framework.TestCase;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import se.chalmers.dat261.model.CodeCompletionModel;
import se.chalmers.dat261.model.ViewImplementationModel;

import javax.swing.*;

public class CodeCompletionTest extends TestCase {

  private void initializeTester(Tester tester){
    tester.buildGraph();
    tester.addListener(new VerboseListener());
    tester.addListener(new StopOnFailureListener());
    tester.addCoverageMetric(new TransitionCoverage());
    tester.addCoverageMetric(new StateCoverage());
    tester.addCoverageMetric(new ActionCoverage());
    tester.addCoverageMetric(new TransitionPairCoverage());

  }

  public void testCompletion() throws Exception {
    // Everything done on the fixture needs to be on the UI thread. ModelJUnit will use it's own threads, so we need some extra work to end
    // up on the right thread. Note that the below is non-blocking.
    SwingUtilities.invokeAndWait(() -> {
      ViewImplementationModel implementationModel = null;

      try {
      String test = "";
      Tester tester;
      implementationModel = new ViewImplementationModel();
      int nbrTests = 200;

      switch(test) {
        default:
          tester = new RandomTester(implementationModel);
          initializeTester(tester);
          tester.generate(nbrTests);
          break;
        case "AllRound":
          tester = new AllRoundTester(implementationModel);
          initializeTester(tester);
          for(int i = 0; i < nbrTests; i++){
            ((AllRoundTester) tester).allRoundTrips();
          }
          break;
        case "Greedy":
          tester = new GreedyTester(implementationModel);
          initializeTester(tester);
          for (int i = 0; i < nbrTests; i++) {
            ((GreedyTester)tester).doGreedyRandomActionOrReset();
          }
          break;
        case "LookAhead":
          tester = new LookaheadTester(implementationModel);
          initializeTester(tester);
          ((LookaheadTester)tester).setNewTransValue(1);
          ((LookaheadTester)tester).setNewActionValue(1);
          ((LookaheadTester)tester).setDepth(1);
          for (int i = 0; i < nbrTests; i++) {
            tester.generate();
          }
          break;
      }
        tester.printCoverage();
      }
      catch (Exception e) {
        e.printStackTrace();
        fail();
      }
      finally {
        try {
          implementationModel.cleanup();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
