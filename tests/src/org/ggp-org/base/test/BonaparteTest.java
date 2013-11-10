package org.ggp.base.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.ggp.base.util.game.TestGameRepository;
import org.ggp.base.util.gdl.grammar.Gdl;
import org.ggp.base.util.gdl.grammar.GdlConstant;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.gdl.grammar.GdlSentence;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.implementation.prover.ProverStateMachine;
import org.junit.Test;

public class BonaparteTest extends Assert {

    protected final ProverStateMachine sm = new ProverStateMachine();
    private Role france = new Role(GdlPool.getConstant("france"));
    private Role germany = new Role(GdlPool.getConstant("germany"));
    private Role russia = new Role(GdlPool.getConstant("russia"));

    @Test
    public void testCaseDippy() throws Exception {
        List<Gdl> desc = new TestGameRepository().getGame("bonaparte").getRules();
        sm.initialize(desc);
        MachineState state = sm.getInitialState();
        System.out.println("initial: "+state.toString().replace(",", "\n         "));
        assertFalse(sm.isTerminal(state));
        state = dippyMove(state, move("move par pic"), move2("move ber pru", "move mun sil"), move("move mos lvn"));
        System.out.println("step  1:\n"+formatState(state));
        assertFalse(sm.isTerminal(state));
        assertEquals(10, sm.getGoal(state, france));
        state = dippyMove(state, move("move pic bel"), move("support_hold sil pru"), move2("move lvn pru", "support_move war lvn pru"));
        System.out.println("step  2:\n"+formatState(state));
        assertFalse(sm.isTerminal(state));
        assertEquals(50, sm.getGoal(state, france));
        assertFact(state, "( army russia lvn )");
        assertFact(state, "( army germany pru )");
        List<Move> lm = sm.getLegalMoves(state, france);
        assertEquals(2, lm.size());
        state = dippyMove(state, move("build1 par"), move("noop"), move("noop"));
        System.out.println("step  3:\n"+formatState(state));
        assertFalse(sm.isTerminal(state));
        assertEquals(50, sm.getGoal(state, france));
        state = dippyMove(state, move2("move bel hol", "move par bur"), move("noop"), move2("move lvn pru", "support_move war lvn pru"));
        System.out.println("step  4:\n"+formatState(state));
        assertFact(state, "( army russia pru )");
        state = dippyMove(state, move2("move hol kie", "move mar pie"), move("move sil gal"), move("move war sil"));
        System.out.println("step  5:\n"+formatState(state));
        state = dippyMove(state, move("noop"), move("noop"), move("noop"));
        System.out.println("step  6:\n"+formatState(state));
        state = dippyMove(state, move2("move bur mun", "move kie ber"), move("noop"), move2("move pru sil", "move sil boh"));
        System.out.println("step  7:\n"+formatState(state));
        state = dippyMove(state, move("noop"), move("noop"), move("noop"));
        System.out.println("step  8:\n"+formatState(state));
        lm = sm.getLegalMoves(state, germany);
        assertEquals(1, lm.size());
        assertEquals(move("terminate"), lm.get(0));
        state = dippyMove(state, move("noop"), move("terminate"), move("noop"));
        System.out.println("step  9:\n"+formatState(state));
        state = dippyMove(state, move("move ber sil"), move("noop"), move2("move boh mun", "support_move sil boh mun"));
        System.out.println("step 10:\n"+formatState(state));
        assertFact(state, "( army russia boh )");
        assertFact(state, "( army france mun )");
        state = dippyMove(state, move("noop"), move("noop"), move2("move boh sil", "move sil pru"));
        System.out.println("step 11:\n"+formatState(state));
        lm = sm.getLegalMoves(state, germany);
        assertEquals(1, lm.size());
        state = dippyMove(state, move("build2 mar par"), move("noop"), move("noop"));
        System.out.println("step 12:\n"+formatState(state));
        state = dippyMove(state, move("move pie ven"), move("noop"), move2("move pru sil", "move sil boh"));
        System.out.println("step 13:\n"+formatState(state));
        state = dippyMove(state, move2("move ber mun", "move mun tyr"), move("noop"), move2("move boh mun", "support_move sil boh mun"));
        System.out.println("step 14:\n"+formatState(state));
        assertFact(state, "( army france ber )");
        assertFact(state, "( army france tyr )");
        assertFact(state, "( army russia mun )");
        assertFact(state, "( army russia sil )");
        assertFalse(sm.isTerminal(state));
        state = dippyMove(state, move("noop"), move("noop"), move("noop"));
        System.out.println("step 15:\n"+formatState(state));
        state = dippyMove(state, move("move tyr mun"), move("noop"), move2("move mun boh", "move sil mun"));
        System.out.println("step 16:\n"+formatState(state));
        assertFact(state, "( army france ber )");
        assertFact(state, "( army france tyr )");
        assertFact(state, "( army russia boh )");
        assertFact(state, "( army russia sil )");
        state = dippyMove(state, move("move tyr mun"), move("noop"), move("noop"));
        System.out.println("step 17:\n"+formatState(state));
        assertTrue(sm.isTerminal(state));
        assertEquals(100, sm.getGoal(state, france));
        //        assertEquals(Collections.singletonList(100), sm.getGoals(state));
    }

    @Test
    public void testManyArmies() throws Exception {
        List<Gdl> desc = new TestGameRepository().getGame("bonaparte").getRules();
        sm.initialize(desc);
        MachineState state = sm.getInitialState();
        System.out.println("initial: "+state.toString().replace(",", "\n         "));
        assertFalse(sm.isTerminal(state));
        state = dippyMove(state, move2("move mar pie", "move par pic"), move2("move ber kie", "move mun boh"), move2("move mos war", "move war gal"));
        System.out.println("step  1:\n"+formatState(state));
        state = dippyMove(state, move2("move pic bel", "move pie ven"), move2("move boh vie", "move kie hol"), move2("move gal bud", "move war sil"));
        System.out.println("step  2:\n"+formatState(state));
        state = dippyMove(state, move("build2 mar par"), move("build2 ber mun"), move("build1 war"));
        System.out.println("step  3:\n"+formatState(state));
        state = dippyMove(state, move2("move mar gas", "move par gas"), move("noop"), move("noop"));
        System.out.println("step  4:\n"+formatState(state));
        assertFact(state, "( army france mar )");
        assertFact(state, "( army france par )");

    }

    protected void assertFact(MachineState state, String fact) {
        String trueFact = "( true " + fact + " )";
        for(GdlSentence aFact : state.getContents()) {
            if(aFact.toString().equals(trueFact))
                return;
        }
        assertTrue("Fact "+fact+" is not true in state\n  "+formatState(state), false);
    }

    protected String formatState(MachineState state) {
        Object[] sc = state.getContents().toArray();
        for (int i = 0; i < sc.length; i += 1) {
                sc[i] = sc[i].toString();
        }
        Arrays.sort(sc);
        String stateStr = "";
        for (int i = 0; i < sc.length -1; i += 1) {
            stateStr = stateStr + sc[i] + ",\n   ";
        }
        stateStr = stateStr + sc[sc.length - 1];
        return stateStr;
    }

    private MachineState dippyMove(MachineState state, Move fra, Move ger, Move rus) throws Exception {
        String stateStr = formatState(state);
        List<Move> lmf = sm.getLegalMoves(state, france);
        assertTrue("Impossible french move " + fra + " in state " + stateStr, lmf.contains(fra));
        List<Move> lmg = sm.getLegalMoves(state, germany);
        assertTrue("Impossible german move " + ger + " in state " + stateStr, lmg.contains(ger));
        List<Move> lmr = sm.getLegalMoves(state, russia);
        assertTrue("Impossible rusian move " + rus + " in state " + stateStr, lmr.contains(rus));
        System.out.printf("Moves: %d %d %d = %d\n", lmf.size(), lmg.size(), lmr.size(), (long) lmf.size() * lmg.size() * lmr.size());
        return sm.getNextState(state, Arrays.asList(fra, ger, rus));
    }

    protected Move move(String description) {
        String[] parts = description.split(" ");
        GdlConstant head = GdlPool.getConstant(parts[0]);
        if(parts.length == 1)
            return new Move(head);
        List<GdlTerm> body = new ArrayList<GdlTerm>();
        for(int i = 1; i < parts.length; i++) {
            body.add(GdlPool.getConstant(parts[i]));
        }
        return new Move(GdlPool.getFunction(head, body));
    }

    protected Move move2(String movea, String moveb) {
        Move m1 = move(movea);
        Move m2 = move(moveb);
        GdlConstant head = GdlPool.getConstant("combine2");
        List<GdlTerm> body = Arrays.asList(m1.getContents(), m2.getContents());
        return new Move(GdlPool.getFunction(head, body));
    }
}
