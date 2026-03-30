package fa.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NFA implements NFAInterface
{

    // region field
    private static final char EPSILON = 'e';
    private final Map<String, NFAState> allStates;
    private final Set<Character> alphabet;
    private NFAState initialState;
    private final Set<NFAState> finalStates;
    // endregion

    // region constructors
    public NFA()
    {
        this.allStates = new HashMap<>();
        this.alphabet = new HashSet<>();
        this.alphabet.add(EPSILON); //Should always be in set
        this.initialState = null;
        this.finalStates = new HashSet<>();
        this.finalStates.add(null); //HashSet.Add(null) after this will return false!
    }

    // region trivial methods
    @Override
    public boolean addState(String name)
    {

        return (allStates.putIfAbsent(name, new NFAState(name)) == null);
    }

    @Override
    public boolean setFinal(String name)
    {
        return finalStates.add(allStates.get(name)); //HashSet.add(null) returns false if null is given
    }

    @Override
    public boolean setStart(String name)
    {
        NFAState state = allStates.get(name);
        if (state == null) return false; //short circuit
        else initialState = state;

        return true;
    }

    @Override
    public void addSigma(char symbol)
    {
        alphabet.add(symbol);
    }

    @Override
    public Set<Character> getSigma()
    {
        return new HashSet<>(alphabet); //shallow copy
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb)
    {
        if (!alphabet.contains(onSymb)) return false;
        if (!allStates.keySet().containsAll(toStates)) return false;

        NFAState state = allStates.get(fromState);
        if (state == null) return false;

        for (String toState : toStates) state.addTransition(onSymb, allStates.get(toState));
        return true;
    }

    @Override
    public boolean isFinal(String name)
    {
        return (allStates.containsKey(name) && finalStates.contains(allStates.get(name)));
    }

    @Override
    public boolean isStart(String name)
    {
        return initialState.equals(allStates.get(name));
    }

    @Override
    public NFAState getState(String name)
    {
        return allStates.get(name);
    }

    // endregion

    @Override
    public boolean accepts(String s)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accepts'");
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getToState'");
    }

    /**
     * Traverses all epsilon transitions and determine d
     * what states can be reached from s through e
     *
     * @param s State to find eclosure of
     * @return set of states that can be reached from s on epsilon trans.
     */
    @Override
    public HashSet<NFAState> eClosure(NFAState s)
    {
        Set<NFAState> closure = s.toStates(EPSILON);
        closure.remove(s); //Prevent infinite recursion from epsilon self-loops in tests
        for (NFAState state : closure)
        {
            closure.addAll(eClosure(state));
        }
        closure.add(s);

        return (HashSet<NFAState>) closure;
    }

    @Override
    public int maxCopies(String s)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maxCopies'");
    }

    @Override
    public boolean isDFA()
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDFA'");
    }

    //endregion

}
