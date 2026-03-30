package fa.nfa;

import java.util.*;

import static java.lang.Math.max;

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

    // region methods

    /**
     * Builds a map where the key is the quantity of input characters consumed,
     * and the value is a set containing all possible states.
     * @param input A string used for input on this NFA
     * @return HashMap where key is the number of consumed characters and value is all possible current states
     */
    public Map<Integer, Set<NFAState>> getTraceMap(String input)
    {
        Map<Integer, Set<NFAState>> traceMap = new HashMap<>();
        traceMap.put(0, new HashSet<>());
        traceMap.get(0).addAll(eClosure(initialState));

        for (int i = 0; i < input.length(); i++)
        {
            Set<NFAState> senders = traceMap.get(i);
            Set<NFAState> receivers = new HashSet<>();

            for (NFAState sender : senders)
            {
                receivers.addAll(getToState(sender, input.charAt(i)));
            }

            traceMap.put(i+1, receivers);
        }

        return traceMap;
    }

    @Override
    public boolean accepts(String s)
    {
        Map<Integer, Set<NFAState>> traceMap = getTraceMap(s);

        //tracemap.get(s.length()) is "map that contains all possible states after consuming all input"
        for (NFAState endState : traceMap.get(s.length())) if (finalStates.contains(endState)) return true;

        return false;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb)
    {
        Set<NFAState> reachable = new HashSet<>();

        for (NFAState senders : eClosure(from))
        {
            for (NFAState sender : senders.toStates(onSymb))
            {
                reachable.addAll(eClosure(sender));
            }
        }

        return reachable;
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
        //Same idea as a recursive traversal of the NFA, but manually tracked with a stack
        Deque<NFAState> workStack  = new ArrayDeque<>();
        HashSet<NFAState> closure = new HashSet<>();
        workStack.push(s);
        closure.add(s);

        while (!workStack.isEmpty())
        {
            NFAState current = workStack.pop();

            for (NFAState next : current.toStates(EPSILON))
            {
                if (!closure.contains(next))
                {
                    //This is the "recursive step"
                    closure.add(next);
                    workStack.push(next);
                }
            }
        }

        return closure;
    }

    @Override
    public int maxCopies(String s)
    {
        int maxConcurrent = 0;

        //Finds the set of states with input to process that has the largest cardinality
        for (Set<NFAState> states : getTraceMap(s).values()) maxConcurrent = max(maxConcurrent, states.size());

        return maxConcurrent;
    }

    @Override
    public boolean isDFA()
    {
        //Trivially check DFA rules, for any state, no epsilon transitions & no redundant transitions
        for (NFAState state : allStates.values())
        {
            if (state.hasTransition(EPSILON)) return false;

            for (Character c : alphabet) if (state.toStates(c).size() > 1) return false;
        }

        return true;
    }
    //endregion

}
